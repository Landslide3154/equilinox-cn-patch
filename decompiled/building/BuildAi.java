/*
 * Decompiled with CFR 0.152.
 */
package building;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import blueprints.Blueprint;
import building.BuildComponent;
import building.BuildVisitComponent;
import building.BuilderComponent;
import classification.Classification;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityBundle.EntityBundle;
import gameManaging.GameManager;
import instances.Entity;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import perching.PerchComponent;
import perching.PerchSlot;
import perching.PercherComponent;
import resourceManagement.BlueprintRepository;
import session.GameMode;
import toolbox.Maths;
import toolbox.Transformation;
import world.GridSection;
import world.PerSectionCode;

public class BuildAi
implements Ai {
    private static final String DESC = GameText.getText(181);
    private static final float PRIORITY = 3.0f;
    private static final float STOP_LENGTH = 3.0f;
    private final BuilderComponent builderComp;
    private final MovementComp mover;
    private final InformationComponent info;
    private final Blueprint buildModel;
    private boolean arrived = false;
    private float stopTime = 0.0f;
    private Entity buildEntity;
    private Vector3f targetPosition;
    private PerchSlot perch = null;

    protected BuildAi(BuilderComponent buildComp, MovementComp mover, InformationComponent info) {
        this.builderComp = buildComp;
        this.mover = mover;
        this.info = info;
        this.buildModel = BlueprintRepository.getBlueprint(buildComp.getBlueprint().buildModelId);
    }

    @Override
    public boolean carryOut() {
        if (this.targetPosition == null) {
            this.getTarget();
        }
        if (!this.targetAvailable()) {
            return true;
        }
        if (this.arrived) {
            return this.pauseForAction();
        }
        this.arrived = this.mover.land(this.targetPosition);
        return false;
    }

    private boolean targetAvailable() {
        if (this.targetPosition == null) {
            return false;
        }
        if (this.buildEntity != null) {
            return !this.buildEntity.isDead() && !this.buildEntity.isGrabbed();
        }
        if (this.perch != null) {
            return this.perch.isAvailable();
        }
        return true;
    }

    @Override
    public float getPriority() {
        return 3.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.builderComp;
    }

    private void getTarget() {
        EntityBundle nearbyBuilds = this.getCurrentNearbyBuilds();
        if (!nearbyBuilds.isEmpty()) {
            this.chooseRandomBuildTarget(nearbyBuilds);
        } else {
            this.getNewBuildSpot();
        }
    }

    private void getNewBuildSpot() {
        if (this.builderComp.getBlueprint().needsPerch) {
            this.findFreePerchSpot();
        } else {
            this.chooseRandomFloorTarget();
        }
    }

    private void findFreePerchSpot() {
        EntityBundle perchEntities = this.getPerchSpotEntities(this.info);
        if (!perchEntities.isEmpty()) {
            Entity[] potentialPerches;
            Entity[] entityArray = potentialPerches = perchEntities.getRandomList(4);
            int n = potentialPerches.length;
            int n2 = 0;
            while (n2 < n) {
                Entity perchEntity = entityArray[n2];
                PerchSlot slot = ((PerchComponent)perchEntity.getComponent(ComponentType.PERCH)).getRandomAvailableSlot();
                if (slot != null) {
                    this.targetPosition = slot.getWorldPosition();
                    this.perch = slot;
                    return;
                }
                ++n2;
            }
        }
    }

    private void chooseRandomFloorTarget() {
        int counter = 0;
        Vector3f bestPos = null;
        float bestNormal = -1.0f;
        while (this.targetPosition == null && counter < 15) {
            this.targetPosition = this.info.getRandomInRangePoint();
            this.targetPosition.y = GameManager.getWorld().getHeightOfTerrain(this.targetPosition.x, this.targetPosition.z);
            if (this.targetPosition.y < GameManager.getWorld().getConfigs().getWaterHeight()) {
                this.targetPosition = null;
            } else {
                float normal = GameManager.getWorld().getNormalOfTerrain((float)this.targetPosition.x, (float)this.targetPosition.z).y;
                if (normal > 0.992f) {
                    return;
                }
                if (normal > bestNormal) {
                    bestNormal = normal;
                    bestPos = this.targetPosition;
                }
                this.targetPosition = null;
            }
            ++counter;
        }
        if (bestPos != null) {
            this.targetPosition = bestPos;
        }
    }

    private void chooseRandomBuildTarget(EntityBundle nearbyBuilds) {
        this.buildEntity = nearbyBuilds.getRandomEntity();
        this.targetPosition = this.buildEntity.getTransform().getPosition();
    }

    private EntityBundle getCurrentNearbyBuilds() {
        Classification buildClass = this.buildModel.getSpeciesClassification();
        Vector3f pos = this.info.getBasePosition();
        return GameManager.getWorld().getListOfSpecies(buildClass, this.info.getRoamingRange(), pos.x, pos.z);
    }

    private EntityBundle getPerchSpotEntities(InformationComponent info) {
        Vector3f position = info.getBasePosition();
        final EntityBundle bigBundle = new EntityBundle();
        GameManager.getWorld().iterateGridSquaresNew(position.x, position.z, info.getRoamingRange(), new PerSectionCode(){

            @Override
            public void execute(GridSection gridSquare) {
                bigBundle.merge(gridSquare.getEntitiesWithComponent(ComponentType.PERCH));
            }
        });
        return bigBundle;
    }

    private boolean pauseForAction() {
        this.stopTime += GameManager.getGameSeconds();
        if (this.stopTime > 3.0f) {
            if (this.buildEntity != null) {
                this.visitEntity();
            } else {
                this.initializeBuild();
            }
            return true;
        }
        return false;
    }

    private void visitEntity() {
        if (this.buildEntity.isDead() || this.buildEntity.isGrabbed()) {
            return;
        }
        BuildComponent theBuild = (BuildComponent)this.buildEntity.getComponent(ComponentType.BUILD);
        BuildVisitComponent visitComp = this.builderComp.getVisitComponent();
        if (visitComp != null) {
            visitComp.visit(this.buildEntity, theBuild.isFullyBuilt());
        }
        if (!theBuild.isFullyBuilt() && GameManager.getGameMode() != GameMode.BUILD) {
            theBuild.build(this.builderComp.getBlueprint().buildPoints, false);
        }
    }

    private void initializeBuild() {
        Vector3f pos = this.info.getBasePosition();
        int count = GameManager.getWorld().getPopulation(this.buildModel.getSpeciesClassification(), this.info.getRoamingRange(), pos.x, pos.z);
        if (count > 0) {
            return;
        }
        Transformation.TransformBlueprint transform = (Transformation.TransformBlueprint)this.buildModel.getComponent(ComponentType.TRANSFORM);
        Transformation.TransformParams params = new Transformation.TransformParams(new Vector3f(this.targetPosition), Maths.RANDOM.nextFloat() * 360.0f, transform.generateRandomScale());
        Entity entity = this.buildModel.createInstance(params);
        GameManager.getSession().getWorld().addInstance(entity, false);
        if (this.perch != null) {
            PercherComponent percher = (PercherComponent)entity.getComponent(ComponentType.PERCHER);
            percher.perchOnSpot(this.perch, true);
        }
    }

    @Override
    public void interrupt() {
        this.arrived = false;
        this.stopTime = 0.0f;
    }

    @Override
    public String getDescription() {
        return DESC;
    }
}

