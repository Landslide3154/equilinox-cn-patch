/*
 * Decompiled with CFR 0.152.
 */
package beavers;

import baseMovement.MovementComp;
import beavers.WoodComponent;
import blueprints.Blueprint;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityBundle.EntityBundle;
import events.EventData;
import events.EventManager;
import fruit.DecayParams;
import gameManaging.GameManager;
import growth.GrowthComponent;
import health.LifeComponent;
import instances.Entity;
import interpolation.Timer;
import materials.MaterialComponent;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import particleSpawns.PointSpawn;
import particles.ParticleSystem;
import resourceManagement.BlueprintRepository;
import session.GameMode;
import toolbox.Colour;
import toolbox.Maths;
import toolbox.Transformation;

public class TreeHuntingAi {
    private static final float STOP_RADIUS = 0.25f;
    private static final float GNAW_TIME = 4.0f;
    private static final Colour CHIP_COLOUR = new Colour(239.0f, 203.0f, 133.0f, true);
    private static final float SPAWN_SPEED = 3.0f;
    private static final int TWIG_ID = 100;
    private static final int BARK_ID = 101;
    private static final String TREE_CLASS = "pt";
    private static final float BARK_CHANCE = 0.4f;
    private static final float PRODUCT_PER_SEC = 4.0f;
    private static final int TREE_SAMPLE_SIZE = 4;
    private static final ParticleSystem GNAW_EFFECT = TreeHuntingAi.createGnawParticleSystem();
    private final MovementComp mover;
    private final InformationComponent info;
    private Timer gnawTimer;
    private Entity targetTree;
    private boolean gnawing = false;
    private Matrix4f particleMatrix = new Matrix4f();
    private boolean matrixDirty = true;

    protected TreeHuntingAi(MovementComp mover, InformationComponent info) {
        this.mover = mover;
        this.info = info;
    }

    protected boolean isGnawing() {
        return this.gnawing;
    }

    protected boolean doTreeHuntingAi() {
        boolean treeAvailable;
        if (this.targetTree == null) {
            this.findTargetTree();
        }
        if (treeAvailable = this.checkTreeAvailable()) {
            return this.dealWithTargetTree();
        }
        return true;
    }

    protected void interrupt() {
        this.gnawing = false;
        this.targetTree = null;
        this.matrixDirty = true;
        if (this.gnawTimer != null) {
            this.gnawTimer.start();
        }
    }

    private void findTargetTree() {
        Classification treeClass = Classifier.getClassification(TREE_CLASS);
        Vector3f basePos = this.info.getBasePosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(treeClass, this.info.getRoamingRange(), basePos.x, basePos.z);
        if (!bundle.isEmpty()) {
            float growthFactor = this.getOldestEntity(bundle);
            WoodComponent woodInfo = (WoodComponent)this.targetTree.getComponent(ComponentType.WOOD);
            float cuttingTime = woodInfo == null ? 4.0f : woodInfo.getCuttingTime();
            this.gnawTimer = Timer.createOneOffTimer(cuttingTime * growthFactor, true).start();
        }
    }

    private float getOldestEntity(EntityBundle bundle) {
        Entity[] chosenEntities;
        Entity oldest = null;
        float oldestGrowth = 0.0f;
        Entity[] entityArray = chosenEntities = bundle.getRandomList(4);
        int n = chosenEntities.length;
        int n2 = 0;
        while (n2 < n) {
            Entity entity = entityArray[n2];
            GrowthComponent growth = (GrowthComponent)entity.getComponent(ComponentType.GROWTH);
            if (oldest == null || growth.getGrowthFactor() > oldestGrowth) {
                oldest = entity;
                oldestGrowth = growth.getGrowthFactor();
            }
            ++n2;
        }
        this.targetTree = oldest;
        return oldestGrowth;
    }

    private boolean checkTreeAvailable() {
        return this.targetTree != null && !this.targetTree.isDead() && !this.targetTree.isGrabbed();
    }

    private boolean dealWithTargetTree() {
        if (this.gnawing) {
            return this.gnaw();
        }
        this.goToTree();
        return false;
    }

    private boolean gnaw() {
        if (this.gnawTimer.check()) {
            this.fellTree();
            return true;
        }
        if (this.matrixDirty) {
            this.updateMatrix();
        }
        if (Maths.chance(GameManager.getGameSeconds() * 4.0f)) {
            this.spawnProduct();
        }
        GNAW_EFFECT.generateParticles(this.particleMatrix, CHIP_COLOUR, 1.0f);
        return false;
    }

    private void goToTree() {
        this.gnawing = this.mover.goToTarget(this.targetTree.getTransform().getPosition(), false, 0.25f);
    }

    private void fellTree() {
        if (GameManager.getGameMode() == GameMode.BUILD) {
            return;
        }
        LifeComponent lifeComp = (LifeComponent)this.targetTree.getComponent(ComponentType.LIFE);
        if (lifeComp != null) {
            EventManager.TREE_CUT.registerEvent(new EventData(), new String[0]);
            this.targetTree.die(lifeComp.getDeathAi(), false);
        } else {
            this.targetTree.die(null, false);
        }
    }

    private static ParticleSystem createGnawParticleSystem() {
        ParticleSystem system = new ParticleSystem(CHIP_COLOUR, false, new PointSpawn(), 80.0f, 1.8f, 0.5f, 0.5f, 0.06f);
        system.setDirection(new Vector3f(1.0f, 0.65f, 0.0f), 0.15f);
        system.setScaleError(0.35f);
        system.setDirectionLocalSpace();
        system.setOffset(new Vector3f(0.0f, 0.05f, 0.0f));
        system.setLifeError(0.4f);
        system.randomizeRotation();
        system.setXRotation(200.0f);
        system.setFadeValues(1.0f, 0.0f, 0.9f);
        return system;
    }

    private void updateMatrix() {
        this.particleMatrix.setIdentity();
        Vector3f treePos = this.targetTree.getTransform().getPosition();
        this.particleMatrix.translate(treePos);
        float rotation = this.mover.getTransform().getRotY() + 90.0f;
        this.particleMatrix.rotate((float)Math.toRadians(rotation), Maths.UP);
        this.matrixDirty = false;
    }

    private void spawnProduct() {
        WoodComponent woodInfo = (WoodComponent)this.targetTree.getComponent(ComponentType.WOOD);
        float barkChance = woodInfo == null ? 0.4f : woodInfo.getBarkFactor();
        Transformation.TransformBlueprint transformBlueprint = this.targetTree.getTransform().getBlueprint();
        Vector3f treePos = this.targetTree.getTransform().getPosition();
        Vector3f pos = new Vector3f(treePos.x, treePos.y + 0.05f, treePos.z);
        Transformation.TransformParams params = new Transformation.TransformParams(new Vector3f(pos), Maths.RANDOM.nextFloat() * 360.0f, transformBlueprint.generateRandomScale());
        DecayParams params2 = new DecayParams(Maths.generateVectorWithinCone(Maths.UP, 0.5f, 3.0f));
        Blueprint fruitBlueprint = BlueprintRepository.getBlueprint(Maths.chance(barkChance) ? 101 : 100);
        Entity entity = fruitBlueprint.createInstance(params, params2);
        if (woodInfo != null) {
            ((MaterialComponent)entity.getComponent(ComponentType.MATERIAL)).forceColourUpdate();
            ((MaterialComponent)entity.getComponent(ComponentType.MATERIAL)).getMaterial().setColour(woodInfo.getWoodColour());
        }
        GameManager.getSession().getWorld().addInstance(entity, false);
    }
}

