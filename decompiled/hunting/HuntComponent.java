/*
 * Decompiled with CFR 0.152.
 */
package hunting;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityBundle.EntityBundle;
import entityInfoGui.PopUpInfoGui;
import equipping.EquipComponent;
import events.EventData;
import events.EventManager;
import fighting.FightComponent;
import gameManaging.GameManager;
import growth.GrowthComponent;
import hunting.HaulAi;
import hunting.HuntCompBlueprint;
import instances.Entity;
import interpolation.Timer;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;
import world.GridIterator;

public class HuntComponent
extends Component
implements AiProvidingComponent {
    private static final Classification MEAT = Classifier.getClassification("em");
    private static final float HUNT_COOLDOWN = 35.0f;
    private final HuntCompBlueprint blueprint;
    private Entity entity;
    private AiComponent aiComp;
    private MovementComp mover;
    private FightComponent fightComp;
    private EquipComponent equipComp;
    private InformationComponent info;
    private Timer timer = Timer.createLoopingTimer(10.0f, 17.0f, true);
    private boolean packHunting = true;
    private Entity nearbyFood = null;
    private boolean skipEvenSquares = true;
    private boolean hauling = false;

    protected HuntComponent(HuntCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void update() {
        super.update();
        if (this.fightComp.isFighting() || this.hauling) {
            return;
        }
        if (this.timer.check() && !this.checkFood()) {
            this.tryToHunt();
        }
    }

    private void tryToHunt() {
        ChosenPrey targetPrey = this.getTarget();
        if (targetPrey != null) {
            this.huntTarget(targetPrey.target, targetPrey.preyType);
        }
    }

    public void notifyOfHunt(Entity target, Classification preyType) {
        if (!this.fightComp.isFighting() && !this.hauling) {
            this.timer.resetTo(35.0f + Maths.randomNumberBetween(0.0f, 5.0f));
            this.fightComp.fight(target, preyType, this);
            this.timer.reset();
        }
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    public void notifyKill(Entity deadTarget) {
        this.hauling = true;
        this.aiComp.queueAiProgram(new HaulAi(this.mover, this.equipComp, this.info, MEAT, this));
        EventManager.FIGHT_SUCCESS.registerEvent(new EventData(deadTarget), this.entity.getBlueprint().getSpeciesClassification().getKey());
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.aiComp = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        this.fightComp = (FightComponent)bundle.getComponent(ComponentType.FIGHT);
        this.info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        this.entity = bundle.getEntity();
        this.equipComp = (EquipComponent)bundle.getComponent(ComponentType.EQUIP);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    private boolean checkFood() {
        if (this.nearbyFood == null || this.nearbyFood.isDead()) {
            this.searchForNearbyFood();
        }
        return this.nearbyFood != null && !this.nearbyFood.isDead();
    }

    private void searchForNearbyFood() {
        Vector3f basePos = this.info.getBasePosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(MEAT, this.info.getRoamingRange(), basePos.x, basePos.z);
        if (!bundle.isEmpty()) {
            this.timer.reset();
            this.nearbyFood = bundle.getRandomEntity();
        }
    }

    private void huntTarget(Entity target, Classification preyType) {
        if (this.packHunting) {
            this.notifyFriends(target, preyType);
        }
        this.timer.resetTo(35.0f + Maths.randomNumberBetween(0.0f, 5.0f));
        this.fightComp.fight(target, preyType, this);
    }

    private void notifyFriends(Entity target, Classification preyType) {
        Vector3f pos = this.info.getBasePosition();
        Classification species = this.entity.getBlueprint().getSpeciesClassification();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(species, this.info.getRoamingRange(), pos.x, pos.z);
        for (Entity entity : bundle) {
            HuntComponent huntComp = (HuntComponent)entity.getComponent(ComponentType.HUNT);
            huntComp.notifyOfHunt(target, preyType);
        }
    }

    private ChosenPrey getTarget() {
        Classification[] classificationArray = this.blueprint.prey;
        int n = this.blueprint.prey.length;
        int n2 = 0;
        while (n2 < n) {
            Entity[] possibles;
            Classification preyType = classificationArray[n2];
            EntityBundle bundle = this.getNearbyPrey(preyType);
            Entity[] entityArray = possibles = bundle.getRandomList(5);
            int n3 = possibles.length;
            int n4 = 0;
            while (n4 < n3) {
                Entity entity = entityArray[n4];
                GrowthComponent growth = (GrowthComponent)entity.getComponent(ComponentType.GROWTH);
                float growthFactor = growth.getGrowthFactor();
                if (this.blueprint.huntsYoung && growthFactor < 0.5f || this.blueprint.huntsOld && growthFactor >= 0.5f) {
                    return new ChosenPrey(entity, preyType);
                }
                ++n4;
            }
            ++n2;
        }
        this.skipEvenSquares = !this.skipEvenSquares;
        return null;
    }

    private EntityBundle getNearbyPrey(Classification preyType) {
        Vector3f pos = this.info.getBasePosition();
        GridIterator iterator = GameManager.getWorld().getIterator(pos.x, pos.z, this.blueprint.huntingRange, true, this.skipEvenSquares);
        EntityBundle bundle = new EntityBundle();
        while (iterator.hasNext()) {
            iterator.next().getEntities(preyType, bundle);
        }
        return bundle;
    }

    @Override
    public void notifyAiFinished() {
        this.hauling = false;
    }

    private static class ChosenPrey {
        public final Entity target;
        public final Classification preyType;

        public ChosenPrey(Entity target, Classification preyType) {
            this.target = target;
            this.preyType = preyType;
        }
    }
}

