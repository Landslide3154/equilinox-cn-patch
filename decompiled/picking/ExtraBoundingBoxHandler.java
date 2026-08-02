/*
 * Decompiled with CFR 0.152.
 */
package picking;

import components.MeshComponent;
import picking.AABB;
import picking.Box;
import toolbox.TransformChangeListener;
import toolbox.Transformation;

public class ExtraBoundingBoxHandler
implements TransformChangeListener {
    private final MeshComponent mesh;
    private final Transformation transform;
    private int currentMeshStage;
    private Box[] boxes;

    public ExtraBoundingBoxHandler(MeshComponent mesh, Transformation transform) {
        this.transform = transform;
        this.mesh = mesh;
        this.setUpBoundingBoxes();
        transform.addChangeListener(this);
    }

    public Box[] getExtraBoundingBoxes() {
        if (this.mesh.getCurrentStageNumber() != this.currentMeshStage) {
            this.setUpBoundingBoxes();
        }
        return this.boxes;
    }

    @Override
    public void transformChanged() {
        if (this.boxes != null) {
            Box[] boxArray = this.boxes;
            int n = this.boxes.length;
            int n2 = 0;
            while (n2 < n) {
                Box box = boxArray[n2];
                box.setDirty();
                ++n2;
            }
        }
    }

    private void setUpBoundingBoxes() {
        this.currentMeshStage = this.mesh.getCurrentStageNumber();
        AABB[] aabbs = this.mesh.getCurrentModelStage().getExtraAabbs();
        if (aabbs == null) {
            this.boxes = null;
            return;
        }
        this.boxes = new Box[aabbs.length];
        int i = 0;
        while (i < this.boxes.length) {
            this.boxes[i] = new Box(aabbs[i], this.transform);
            ++i;
        }
    }
}

