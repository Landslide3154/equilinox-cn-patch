/*
 * Decompiled with CFR 0.152.
 */
package beavers;

import baseMovement.MovementComp;
import beavers.BeaverComponent;
import beavers.BuildSpotFinder;
import blueprints.Blueprint;
import building.BuildComponent;
import classification.Classification;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityBundle.EntityBundle;
import equipping.EquipComponent;
import gameManaging.GameManager;
import instances.Entity;
import interpolation.Timer;
import materials.MaterialComponent;
import org.lwjgl.util.vector.Vector3f;
import resourceManagement.BlueprintRepository;
import session.GameMode;
import toolbox.Colour;
import toolbox.Maths;
import toolbox.Transformation;

public class DenBuildingAi {
    private static final int BUILD_POINTS = 35;
    private static final int DEN_ID = 102;
    private final Timer waitTimer = Timer.createOneOffTimer(2.0f, true).start();
    private final MovementComp mover;
    private final InformationComponent info;
    private final EquipComponent equipComp;
    private final BeaverComponent beaverComp;
    private Entity denEntity;
    private Vector3f targetPosition;

    protected DenBuildingAi(BeaverComponent beaverComp, MovementComp mover, InformationComponent info, EquipComponent equipComp) {
        this.mover = mover;
        this.info = info;
        this.equipComp = equipComp;
        this.beaverComp = beaverComp;
    }

    protected void interrupt() {
        this.denEntity = null;
        this.targetPosition = null;
        this.equipComp.letDrop();
        this.waitTimer.reset();
    }

    protected boolean doAi() {
        if (this.denEntity == null) {
            this.findDenEntity();
        }
        if (this.targetPosition == null) {
            this.equipComp.letDrop();
            this.beaverComp.notifyDenBuildingImpossible();
            return true;
        }
        if (this.denEntity != null && (this.denEntity.isDead() || this.denEntity.isGrabbed())) {
            this.denEntity = null;
            this.targetPosition = null;
            return false;
        }
        return this.goToBuildDen();
    }

    private void findDenEntity() {
        Classification treeClass = BlueprintRepository.getBlueprint(102).getSpeciesClassification();
        Vector3f basePos = this.info.getBasePosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(treeClass, this.info.getRoamingRange(), basePos.x, basePos.z);
        if (!bundle.isEmpty()) {
            this.denEntity = bundle.getRandomEntity();
            this.targetPosition = this.denEntity.getTransform().getPosition();
        } else if (this.targetPosition == null) {
            this.targetPosition = new BuildSpotFinder().findBuildSpot(this.info.getBasePosition(), this.info.getRoamingRange());
        }
    }

    private boolean goToBuildDen() {
        boolean reached = this.mover.goToTarget(this.targetPosition, false, 0.2f);
        if (reached && this.waitTimer.check()) {
            this.buildDen();
            return true;
        }
        return false;
    }

    private void buildDen() {
        if (this.denEntity != null) {
            BuildComponent builder = (BuildComponent)this.denEntity.getComponent(ComponentType.BUILD);
            this.updateColour(builder);
            if (GameManager.getGameMode() != GameMode.BUILD) {
                builder.build(35, true);
            }
            this.beaverComp.setLastUsedDen(this.denEntity);
        } else {
            this.createNewBuildDen();
        }
        this.equipComp.removeHeldItem();
    }

    private void updateColour(BuildComponent builder) {
        float value = 35.0f / (float)(builder.getBuildPoints() + 35);
        MaterialComponent stickMat = (MaterialComponent)this.equipComp.getHeldEntity().getComponent(ComponentType.MATERIAL);
        Colour denColour = ((MaterialComponent)this.denEntity.getComponent(ComponentType.MATERIAL)).getMaterial();
        Colour.interpolateColours(denColour, stickMat.getMaterial(), value, denColour);
    }

    private void createNewBuildDen() {
        Blueprint model = BlueprintRepository.getBlueprint(102);
        Transformation.TransformBlueprint transform = (Transformation.TransformBlueprint)model.getComponent(ComponentType.TRANSFORM);
        this.targetPosition.y = GameManager.getWorld().getHeightOfTerrain(this.targetPosition.x, this.targetPosition.z);
        Transformation.TransformParams params = new Transformation.TransformParams(new Vector3f(this.targetPosition), Maths.RANDOM.nextFloat() * 360.0f, transform.generateRandomScale());
        Entity entity = model.createInstance(params);
        MaterialComponent matInfo = (MaterialComponent)this.equipComp.getHeldEntity().getComponent(ComponentType.MATERIAL);
        if (matInfo != null) {
            ((MaterialComponent)entity.getComponent(ComponentType.MATERIAL)).forceColourUpdate();
            ((MaterialComponent)entity.getComponent(ComponentType.MATERIAL)).getMaterial().setColour(matInfo.getMaterial());
        }
        GameManager.getSession().getWorld().addInstance(entity, false);
    }
}

