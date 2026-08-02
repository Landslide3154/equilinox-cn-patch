/*
 * Decompiled with CFR 0.152.
 */
package birdHunt;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import blueprints.Blueprint;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityBundle.EntityBundle;
import equipping.EquipComponent;
import events.EventData;
import events.EventManager;
import flying.BirdMovement;
import gameManaging.GameManager;
import hunting.PreyComp;
import instances.Entity;
import interpolation.Timer;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import resourceManagement.BlueprintRepository;
import toolbox.Maths;
import toolbox.Transformation;

public class BirdHuntAi
implements Ai {
    private static final String HUNTING_DESC = GameText.getText(182);
    private static final Classification MEERKAT = Classifier.getClassification("ahs178");
    private static final float MAX_TIME = 50.0f;
    private static final int PRIORITY = 101;
    private static final int NEST_ID = 174;
    private static final float DROP_RANGE = 0.4f;
    private static final float GRAB_RANGE = 0.0025000002f;
    private final AiProvidingComponent birdHuntComponent;
    private final Entity hunter;
    private final BirdMovement huntingMover;
    private final EquipComponent equipComp;
    private final InformationComponent info;
    private final Entity prey;
    private final Transformation preyTransform;
    private Vector3f dropPosition = null;
    private boolean goingHome = false;
    private float timeSpent = 0.0f;
    private final boolean isChasingMeerkat;
    private Timer alertTimer = Timer.createLoopingTimer(0.2f, true).randomize();

    public BirdHuntAi(Entity prey, Entity hunter, BirdMovement mover, AiProvidingComponent component) {
        this.birdHuntComponent = component;
        this.prey = prey;
        this.hunter = hunter;
        this.huntingMover = mover;
        this.preyTransform = prey.getTransform();
        this.equipComp = (EquipComponent)hunter.getComponent(ComponentType.EQUIP);
        this.info = (InformationComponent)hunter.getComponent(ComponentType.INFO);
        this.isChasingMeerkat = prey.getBlueprint() == BlueprintRepository.getBlueprint(178);
    }

    @Override
    public boolean carryOut() {
        boolean landed;
        if (this.goingHome) {
            return this.flyBackHome();
        }
        if (this.prey.isDead() || this.prey.isGrabbed()) {
            return true;
        }
        if (this.isChasingMeerkat) {
            PreyComp preyComp;
            if (this.alertTimer.check()) {
                this.alertPrey(MEERKAT);
            }
            if ((preyComp = (PreyComp)((Object)this.prey.getComponent(ComponentType.FLEE))).isInvulnerable()) {
                return true;
            }
        }
        if (!(landed = this.landOnPrey())) {
            return this.checkTime();
        }
        EventManager.EAGLE_CATCH.registerEvent(new EventData(), this.prey.getBlueprint().getSpeciesClassification().getKey());
        this.equipComp.equip(this.prey);
        this.chooseDropPosition();
        this.goingHome = true;
        return false;
    }

    protected void alertPrey(Classification preyClass) {
        Vector3f pos = this.huntingMover.getTransform().getPosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(preyClass, 2, pos.x, pos.z);
        for (Entity prey : bundle) {
            PreyComp fleeComp = (PreyComp)((Object)prey.getComponent(ComponentType.FLEE));
            if (fleeComp == null) continue;
            fleeComp.alertToDanger(this.hunter);
        }
    }

    private boolean flyBackHome() {
        boolean homeReached = this.huntingMover.goToTarget(this.dropPosition, false, 0.4f);
        if (homeReached) {
            this.equipComp.throwDown(this.huntingMover.getVelocity());
        }
        return homeReached;
    }

    private boolean checkTime() {
        this.timeSpent += GameManager.getGameSeconds();
        return this.timeSpent > 50.0f;
    }

    private void chooseDropPosition() {
        Vector3f basePos = this.info.getBasePosition();
        Blueprint nest = BlueprintRepository.getBlueprint(174);
        EntityBundle nests = GameManager.getWorld().getListOfSpecies(nest, this.info.getRoamingRange(), basePos.x, basePos.z);
        if (!nests.isEmpty()) {
            Entity nestEntity = nests.getRandomEntity();
            this.dropPosition = new Vector3f(nestEntity.getTransform().getPosition());
        } else {
            this.dropPosition = new Vector3f(this.info.getBasePosition());
        }
    }

    private boolean landOnPrey() {
        Vector3f preyPos = this.getPreyPosition();
        this.huntingMover.indicateHunting();
        boolean landed = this.huntingMover.land(preyPos);
        if (landed) {
            return true;
        }
        float preyDistance = Maths.getComparitableDistance(preyPos, this.hunter.getTransform().getPosition());
        return preyDistance < 0.0025000002f;
    }

    private Vector3f getPreyPosition() {
        Vector3f preyPos = new Vector3f(this.preyTransform.getPosition());
        float terrainheight = GameManager.getWorld().getHeightOfTerrain(preyPos.x, preyPos.z);
        if (preyPos.y - terrainheight < 0.2f) {
            preyPos.y = terrainheight;
        }
        preyPos.y += 0.025f;
        return preyPos;
    }

    @Override
    public float getPriority() {
        return 101.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.birdHuntComponent;
    }

    @Override
    public String getDescription() {
        return HUNTING_DESC;
    }

    @Override
    public void interrupt() {
        this.timeSpent = 0.0f;
    }
}

