/*
 * Decompiled with CFR 0.152.
 */
package carnivorePlant;

import breedingTraits.FloatTrait;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentParams;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityInfoGui.PopUpInfoGui;
import events.EventData;
import events.EventManager;
import gameManaging.GameManager;
import instances.Entity;
import java.io.IOException;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import particleSpawns.PointSpawn;
import particles.ParticleSystem;
import particles.ParticleTexture;
import resourceManagement.ParticleAtlasCache;
import session.GameMode;
import toolbox.Maths;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class TongueShootComp
extends Component {
    private static final float TOTAL_TIME = 0.4f;
    private static final float TONGUE_MIN_WIDTH = 0.17f;
    private static final float TONGUE_MAX_WIDTH = 0.35f;
    private static final ParticleSystem BLOOD = TongueShootComp.createBloodSplash();
    private static final ParticleSystem SMOKE = TongueShootComp.createSmokeParticleSystem();
    private Transformation transform;
    private Entity entity;
    private float time = 0.0f;
    private Entity insect;
    private Vector3f vectorToInsect = new Vector3f();
    private Vector2f vectorToInsect2d = new Vector2f();
    private float distanceToInsect = 0.0f;
    private boolean caught = false;
    private Vector3f caughtPos;

    protected TongueShootComp(ComponentBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void update() {
        if (!this.targetValid()) {
            this.entity.die(null, false);
            return;
        }
        try {
            this.updateVectorToInsect();
            this.updateTongueRotation();
        }
        catch (Exception e) {
            System.err.println("Error in the calculations");
        }
        this.updateTongueLength();
    }

    private void updateVectorToInsect() {
        Vector3f tonguePos = this.transform.getPosition();
        Vector3f insectPos = this.caught ? this.caughtPos : this.insect.getTransform().getPosition();
        Vector3f.sub(insectPos, tonguePos, this.vectorToInsect);
        this.vectorToInsect2d.x = this.vectorToInsect.x;
        this.vectorToInsect2d.y = this.vectorToInsect.z;
        this.distanceToInsect = this.vectorToInsect.length();
    }

    private void updateTongueRotation() {
        float xRot = (float)Math.toDegrees(-Math.asin(this.vectorToInsect.y / this.distanceToInsect));
        float yRot = Maths.calculateVectorRotationY(this.vectorToInsect2d);
        this.transform.setXRotation(xRot);
        this.transform.setYRotation(yRot);
    }

    private boolean targetValid() {
        return this.insect != null && !this.insect.isDead() && (this.caught || !this.insect.isGrabbed());
    }

    private void updateTongueLength() {
        this.time += GameManager.getGameSeconds();
        float factor = this.time / 0.4f;
        float value = Maths.fakeSin(-1.0f, 1.0f, factor * 0.5f);
        if (!this.caught && factor >= 0.5f) {
            this.catchInsect();
        }
        float width = Maths.interpolate(0.35f, 0.17f, value);
        this.transform.setScale(width);
        this.transform.updateModelMatrix(this.distanceToInsect * value * (1.0f / width));
        if (this.caught) {
            this.moveInsectWithTongue(value);
        }
        if (factor >= 1.0f) {
            this.eatFly();
        }
    }

    private void eatFly() {
        BLOOD.pulseParticles(this.transform.getPosition(), 1.0f);
        SMOKE.pulseParticles(this.transform.getPosition(), 1.0f);
        if (GameManager.getGameMode() == GameMode.BUILD) {
            this.addNewFly(this.insect);
        }
        EventManager.INSECT_TONGUE.registerEvent(new EventData(), this.insect.getBlueprint().getSpeciesClassification().getKey());
        this.insect.die(null, true);
        this.entity.die(null, false);
    }

    private void addNewFly(Entity oldFly) {
        InformationComponent oldInfo = (InformationComponent)oldFly.getComponent(ComponentType.INFO);
        Vector3f newPos = oldInfo.getRandomInRangePoint();
        Transformation transform = oldFly.getTransform();
        newPos.y = GameManager.getWorld().getHeightOfTerrain(newPos.x, newPos.z);
        Transformation.TransformParams params = new Transformation.TransformParams(new Vector3f(newPos), Maths.RANDOM.nextFloat() * 360.0f, (FloatTrait)transform.getScaleTrait().duplicate());
        Entity newEntity = oldFly.duplicate(params);
        ((InformationComponent)newEntity.getComponent(ComponentType.INFO)).setBasePosition(oldInfo.getBasePosition());
        GameManager.getSession().getWorld().addInstance(newEntity, true);
    }

    private void moveInsectWithTongue(float value) {
        Vector3f newPos = Maths.interpolate(this.transform.getPosition(), this.caughtPos, value);
        this.insect.getTransform().setPosition(newPos);
    }

    private void catchInsect() {
        this.caught = true;
        this.caughtPos = new Vector3f(this.insect.getTransform().getPosition());
        this.insect.pickUp();
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
        this.entity = bundle.getEntity();
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        TongueShootParams params = (TongueShootParams)bundle.getParameters(ComponentType.TONGUE_SHOOT);
        this.insect = params.insect;
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.entity = bundle.getEntity();
    }

    private static ParticleSystem createBloodSplash() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(20);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 12.0f, 1.2f, 0.5f, 0.44f, 0.028f);
        system.setDirection(Maths.UP, 0.2f);
        system.setScaleError(0.5f);
        system.setSpeedError(0.3f);
        system.setLifeError(0.3f);
        system.randomizeRotation();
        return system;
    }

    private static ParticleSystem createSmokeParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(21);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 1.0f, 0.18f, 0.01f, 1.0f, 0.6f);
        system.setDirection(Maths.UP, 0.25f);
        system.setLifeError(0.3f);
        system.randomizeRotation();
        return system;
    }

    public static class TongueShootParams
    extends ComponentParams {
        private final Entity insect;

        public TongueShootParams(Entity insect) {
            super(ComponentType.TONGUE_SHOOT);
            this.insect = insect;
        }
    }
}

