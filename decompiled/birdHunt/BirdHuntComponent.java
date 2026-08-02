/*
 * Decompiled with CFR 0.152.
 */
package birdHunt;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import birdHunt.BirdHuntAi;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import componentArchitecture.ControlBehaviour;
import components.InformationComponent;
import entityBundle.EntityBundle;
import entityInfoGui.PopUpInfoGui;
import flying.BirdMovement;
import gameManaging.GameManager;
import growth.GrowthComponent;
import hunting.PreyComp;
import instances.Entity;
import java.io.IOException;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;
import world.GridIterator;

public class BirdHuntComponent
extends Component
implements AiProvidingComponent {
    private static final Classification MEAT = Classifier.getClassification("em");
    private static final Classification PREY = Classifier.getClassification("ahs");
    private static final int RANGE = 15;
    private static final int BLOCK_RANGE = 5;
    private static final int BLOCK_PER_ROW = 3;
    private static final float BLOCK_WIDTH = 12.5f;
    private static final float HALF_BLOCKS = 1.5f;
    private static final int BLOCK_COUNT = 9;
    private static final float MIN_WAIT_NORMAL = 1.5f;
    private static final float MAX_WAIT_NORMAL = 2.5f;
    private static final float MIN_WAIT_MEAT = 6.0f;
    private static final float MAX_WAIT_MEAT = 15.0f;
    private static final float HUNT_COOLDOWN_MIN = 50.0f;
    private static final float HUNT_COOLDOWN_MAX = 60.0f;
    private Entity entity;
    private BirdMovement mover;
    private GrowthComponent growth;
    private InformationComponent info;
    private AiComponent aiComp;
    private Entity nearbyMeat = null;
    private boolean hunting = false;
    private boolean skipEvenSquares = Maths.RANDOM.nextBoolean();
    private float timer = Maths.randomNumberBetween(3.0f, 10.0f);
    private int block = Maths.RANDOM.nextInt(9);
    Vector2f topLeft = new Vector2f();

    protected BirdHuntComponent(ComponentBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void update() {
        if (!this.checkIfActionNeeded()) {
            return;
        }
        if (this.isMeatNearby()) {
            this.resetTimer(6.0f, 15.0f);
            return;
        }
        Entity prey = this.getSuitablePrey();
        if (prey == null) {
            this.resetTimer(1.5f, 2.5f);
            return;
        }
        this.goHunting(prey);
    }

    @Override
    public void notifyAiFinished() {
        this.hunting = false;
        this.resetTimer(50.0f, 60.0f);
    }

    private void goHunting(Entity prey) {
        this.notifyNearbyBirds();
        this.hunting = true;
        this.aiComp.queueAiProgram(new BirdHuntAi(prey, this.entity, this.mover, this));
    }

    @Override
    public void getControlableBehaviour(List<ControlBehaviour> behaviours) {
        behaviours.add(new ControlBehaviour("Scare Prey", 18, false){

            @Override
            public void doAction() {
                BirdHuntComponent.this.alertPrey(Classifier.getAnimalClassification());
            }
        });
    }

    protected void alertPrey(Classification preyClass) {
        if (preyClass == null) {
            return;
        }
        Vector3f pos = this.mover.getTransform().getPosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(preyClass, 3, pos.x, pos.z);
        for (Entity prey : bundle) {
            PreyComp fleeComp = (PreyComp)((Object)prey.getComponent(ComponentType.FLEE));
            if (fleeComp == null) continue;
            fleeComp.alertToDanger(this.entity);
        }
    }

    private boolean isMeatNearby() {
        if (this.nearbyMeat == null || this.nearbyMeat.isDead()) {
            this.findNearbyMeat();
        }
        return this.nearbyMeat != null && !this.nearbyMeat.isDead();
    }

    private void findNearbyMeat() {
        Vector3f basePos = this.info.getBasePosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(MEAT, this.info.getRoamingRange(), basePos.x, basePos.z);
        if (!bundle.isEmpty()) {
            this.nearbyMeat = bundle.getRandomEntity();
        }
    }

    private Entity getSuitablePrey() {
        Entity[] possibles;
        EntityBundle bundle = this.getNearbyPrey(PREY);
        Entity[] entityArray = possibles = bundle.getRandomList(5);
        int n = possibles.length;
        int n2 = 0;
        while (n2 < n) {
            Entity possiblePrey = entityArray[n2];
            if (this.isSuitablePrey(possiblePrey)) {
                return possiblePrey;
            }
            ++n2;
        }
        return null;
    }

    private boolean isSuitablePrey(Entity prey) {
        if (prey.isDead() || prey.isGrabbed()) {
            return false;
        }
        GrowthComponent growth = (GrowthComponent)prey.getComponent(ComponentType.GROWTH);
        float growthFactor = growth.getGrowthFactor();
        return growthFactor > 0.5f;
    }

    private EntityBundle getNearbyPrey(Classification preyType) {
        Vector2f pos = this.getCenterPosition();
        GridIterator iterator = GameManager.getWorld().getIterator(pos.x, pos.y, 5, true, this.skipEvenSquares);
        this.skipEvenSquares = !this.skipEvenSquares;
        ++this.block;
        this.block %= 9;
        EntityBundle bundle = new EntityBundle();
        while (iterator.hasNext()) {
            iterator.next().getEntities(preyType, bundle);
        }
        return bundle;
    }

    private Vector2f getCenterPosition() {
        Vector3f basePos = this.info.getBasePosition();
        float halfWidth = 12.5f;
        this.topLeft.x = basePos.x - halfWidth;
        this.topLeft.y = basePos.z - halfWidth;
        int col = this.block % 3;
        int row = this.block / 3;
        return new Vector2f(this.topLeft.x + (float)col * 12.5f, this.topLeft.y + (float)row * 12.5f);
    }

    private void notifyNearbyBirds() {
        Vector3f basePos = this.info.getBasePosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(this.entity.getBlueprint(), this.info.getRoamingRange(), basePos.x, basePos.z);
        for (Entity bird : bundle) {
            if (bird == this.entity) continue;
            BirdHuntComponent hunting = (BirdHuntComponent)bird.getComponent(ComponentType.BIRD_HUNT);
            hunting.notifyOtherHunt();
        }
    }

    private void notifyOtherHunt() {
        if (!this.hunting) {
            this.resetTimer(50.0f, 60.0f);
        }
    }

    private boolean checkIfActionNeeded() {
        if (this.growth.getGrowthFactor() < 0.5f || this.hunting) {
            return false;
        }
        this.timer -= GameManager.getGameSeconds();
        return !(this.timer > 0.0f);
    }

    private void resetTimer(float minWait, float maxWait) {
        this.timer = Maths.randomNumberBetween(minWait, maxWait);
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        this.mover = (BirdMovement)bundle.getComponent(ComponentType.MOVEMENT);
        this.info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        this.aiComp = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.entity = bundle.getEntity();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }
}

