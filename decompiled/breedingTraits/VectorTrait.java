/*
 * Decompiled with CFR 0.152.
 */
package breedingTraits;

import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class VectorTrait {
    private static final float NORMALIZING = 0.4f;
    public final Vector3f value;
    private final Vector3f base1;
    private final Vector3f base2;
    private final float deviation;
    private final float max;

    public VectorTrait(Vector3f value, Vector3f base1, Vector3f base2, float averageDeviation, float max) {
        this.value = value;
        this.max = max;
        this.base1 = base1;
        this.base2 = base2;
        this.deviation = averageDeviation;
    }

    public Vector3f getValue() {
        return this.value;
    }

    public Vector3f reproduce(boolean boost) {
        Vector3f newVector = this.deviateVector();
        if (!boost) {
            this.addBias(newVector);
        }
        return newVector;
    }

    private Vector3f deviateVector() {
        Vector3f newVector = new Vector3f();
        newVector.x = this.deviateValue(this.value.x);
        newVector.y = this.deviateValue(this.value.y);
        newVector.z = this.deviateValue(this.value.z);
        return newVector;
    }

    private float deviateValue(float original) {
        return Maths.clamp((float)(Maths.RANDOM.nextGaussian() * (double)this.deviation + (double)original), 0.0f, this.max);
    }

    private void addBias(Vector3f newVector) {
        Vector3f toBase = Maths.getClosestPointLineSegment(this.base1, this.base2, newVector);
        Vector3f.sub(toBase, newVector, toBase);
        toBase.scale(0.4f);
        Vector3f.add(newVector, toBase, newVector);
    }
}

