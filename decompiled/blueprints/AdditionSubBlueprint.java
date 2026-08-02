/*
 * Decompiled with CFR 0.152.
 */
package blueprints;

import blueprints.SubBlueprint;
import toolbox.Maths;

public class AdditionSubBlueprint
extends SubBlueprint {
    private SubBlueprint base;

    public AdditionSubBlueprint(SubBlueprint base, float[] extraData) {
        super(extraData, base.getAABB(), base.getExtraAabbs(), 1.0f);
        this.base = base;
    }

    @Override
    public int getDataLength() {
        return super.getDataLength() + this.base.getDataLength();
    }

    @Override
    public float[] getFullModelData() {
        return Maths.concatenateArrays(this.base.getFullModelData(), super.getUniqueStageData());
    }
}

