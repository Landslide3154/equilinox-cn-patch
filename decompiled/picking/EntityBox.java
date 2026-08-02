/*
 * Decompiled with CFR 0.152.
 */
package picking;

import components.MeshComponent;
import picking.Box;
import toolbox.TransformChangeListener;
import toolbox.Transformation;

public class EntityBox
extends Box
implements TransformChangeListener {
    private final MeshComponent mesh;

    public EntityBox(Transformation transform, MeshComponent mesh) {
        super(mesh.getCurrentModelStage().getAABB(), transform);
        this.mesh = mesh;
        transform.addChangeListener(this);
    }

    @Override
    protected boolean isDirty() {
        super.setAabb(this.mesh.getCurrentModelStage().getAABB());
        return super.isDirty();
    }

    @Override
    public void transformChanged() {
        super.setDirty();
    }
}

