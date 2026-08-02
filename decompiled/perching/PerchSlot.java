/*
 * Decompiled with CFR 0.152.
 */
package perching;

import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import growth.GrowthComponent;
import instances.Entity;
import instances.EntityGetRequest;
import instances.EntityListener;
import java.io.IOException;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import perching.PercherComponent;
import toolbox.TransformChangeListener;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class PerchSlot {
    private final GrowthComponent grower;
    private final Entity entity;
    private final Transformation entityTransform;
    private final Vector4f modelSpacePosition;
    private Vector3f worldPosition = new Vector3f();
    private boolean needsRecalc = true;
    private PercherComponent currentPercher = null;

    protected PerchSlot(Vector4f modelSpacePosition, Entity entity, Transformation entityTransform, GrowthComponent grower) {
        this.modelSpacePosition = modelSpacePosition;
        this.grower = grower;
        this.entityTransform = entityTransform;
        this.entity = entity;
        this.addTransformListener();
        this.addUnavailableListener();
    }

    protected void export(BinaryWriter writer) throws IOException {
        writer.writeInt(this.currentPercher.getEntityId());
    }

    protected void load(BinaryReader reader, ComponentBundle bundle) throws Exception {
        int entityId = reader.readInt();
        bundle.requestEntity(new EntityGetRequest(entityId){

            @Override
            public void provideEntity(Entity entity) {
                PercherComponent percher = (PercherComponent)entity.getComponent(ComponentType.PERCHER);
                percher.perchOnSpot(PerchSlot.this, true);
            }
        });
    }

    protected boolean needsExporting() {
        return this.currentPercher != null && this.currentPercher.shouldSavePerch();
    }

    protected void fill(PercherComponent percher) {
        this.currentPercher = percher;
    }

    protected void vacate(PercherComponent percher) {
        if (percher == this.currentPercher) {
            this.currentPercher = null;
        }
    }

    public boolean isAvailable() {
        return this.currentPercher == null && this.isInExistence();
    }

    public boolean isInExistence() {
        return this.grower.isFullyGrown() && !this.entity.isDead() && !this.entity.isGrabbed();
    }

    public Vector3f getWorldPosition() {
        if (this.needsRecalc) {
            this.calculateWorldPosition();
            this.needsRecalc = false;
        }
        return this.worldPosition;
    }

    private void calculateWorldPosition() {
        Vector4f temp = Matrix4f.transform(this.entityTransform.getModelMatrix(), this.modelSpacePosition, null);
        this.worldPosition.set(temp);
    }

    private void addUnavailableListener() {
        this.entity.getNotifier().addIncapacitatedListener(new EntityListener(){

            @Override
            public void execute() {
                if (PerchSlot.this.currentPercher != null) {
                    PerchSlot.this.currentPercher.notifyPerchRemoval();
                    PerchSlot.this.currentPercher = null;
                }
            }
        });
    }

    private void addTransformListener() {
        this.entityTransform.addChangeListener(new TransformChangeListener(){

            @Override
            public void transformChanged() {
                PerchSlot.this.needsRecalc = true;
            }
        });
    }
}

