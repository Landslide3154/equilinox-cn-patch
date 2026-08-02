/*
 * Decompiled with CFR 0.152.
 */
package equipping;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import equipping.EquipCompBlueprint;
import gameManaging.GameManager;
import growth.GrowthComponent;
import health.LifeComponent;
import instances.Entity;
import instances.EntityListener;
import java.util.List;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class EquipComponent
extends Component {
    private final EquipCompBlueprint blueprint;
    private Entity equippedEntity;
    private Transformation transform;
    private GrowthComponent growth;
    private Entity entity;
    private boolean falling = false;
    private Vector3f fallingVelocity = new Vector3f();
    private Vector4f holdPosition = new Vector4f();
    private static final float FALL_SPEED = 0.8f;
    private Vector3f temp = new Vector3f();

    protected EquipComponent(EquipCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    public boolean isHolding() {
        return this.equippedEntity != null;
    }

    public void equip(Entity entity) {
        this.letDrop();
        this.equippedEntity = entity;
        this.equippedEntity.pickUp();
    }

    public Entity getHeldEntity() {
        return this.equippedEntity;
    }

    public void removeHeldItem() {
        if (this.equippedEntity != null && !this.equippedEntity.isDead()) {
            this.equippedEntity.die(null, false);
        }
        this.equippedEntity = null;
    }

    public void throwDown(Vector3f initialVel) {
        if (this.equippedEntity == null) {
            return;
        }
        if (this.equippedEntity.isDead()) {
            this.equippedEntity = null;
            return;
        }
        this.falling = true;
        this.fallingVelocity.set(initialVel);
    }

    public void letDrop() {
        if (this.equippedEntity != null && !this.equippedEntity.isDead()) {
            this.equippedEntity.getTransform().clampToTerrain(0.0f);
            this.equippedEntity.getTransform().setXRotation(0.0f);
            this.equippedEntity.getTransform().setZRotation(0.0f);
            this.equippedEntity.putDown();
        }
        this.falling = false;
        this.equippedEntity = null;
    }

    @Override
    public void update() {
        super.update();
        if (this.equippedEntity != null) {
            if (this.equippedEntity.isDead()) {
                this.equippedEntity = null;
                return;
            }
            if (this.falling) {
                this.letEntityFall();
                return;
            }
            Matrix4f.transform(this.transform.getModelMatrix(), this.blueprint.getPosition(this.growth.getStageNumber()), this.holdPosition);
            Transformation entityTransform = this.equippedEntity.getTransform();
            entityTransform.setPosition(this.holdPosition.x, this.holdPosition.y, this.holdPosition.z);
            entityTransform.setYRotation(this.transform.getRotY());
            entityTransform.setXRotation(this.transform.getRotX());
            entityTransform.setZRotation(this.transform.getRotZ());
            GameManager.getWorld().getEntityGrid().updateInGrid(this.equippedEntity);
        }
    }

    private void letEntityFall() {
        Transformation entityTransform = this.equippedEntity.getTransform();
        this.fallingVelocity.y -= 8.0f * GameManager.getGameSeconds();
        this.temp.set(this.fallingVelocity);
        this.temp.scale(GameManager.getGameSeconds());
        entityTransform.increasePosition(this.temp);
        if (entityTransform.checkWithTerrain() <= 0.0f) {
            this.equippedEntity.putDown();
            LifeComponent life = (LifeComponent)this.equippedEntity.getComponent(ComponentType.LIFE);
            if (life != null) {
                life.health.takeDamage(1000, this.entity);
            }
            this.falling = false;
            this.equippedEntity = null;
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

    @Override
    public void create(ComponentBundle bundle) {
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        this.entity = bundle.getEntity();
        this.addListeners(bundle.getEntity());
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    private void addListeners(Entity entity) {
        EntityListener listener = new EntityListener(){

            @Override
            public void execute() {
                EquipComponent.this.letDrop();
            }
        };
        entity.getNotifier().addIncapacitatedListener(listener);
        entity.getNotifier().addRemoveListener(listener);
    }
}

