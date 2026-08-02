/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import java.util.LinkedList;

public class RollingAverage {
    private final int max;
    private int count = 0;
    private final LinkedList<Float> values = new LinkedList();

    public RollingAverage(int count) {
        this.max = count;
    }

    public void addValue(float value) {
        if (this.count >= this.max) {
            this.values.removeFirst();
        } else {
            ++this.count;
        }
        this.values.addLast(Float.valueOf(value));
    }

    public float calculate() {
        float total = 0.0f;
        for (Float f : this.values) {
            total += f.floatValue();
        }
        return total / (float)this.count;
    }
}

