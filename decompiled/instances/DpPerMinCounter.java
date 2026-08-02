/*
 * Decompiled with CFR 0.152.
 */
package instances;

import componentArchitecture.ComponentType;
import java.util.HashMap;
import java.util.Map;

public class DpPerMinCounter {
    private int baseDpPerMin;
    private int dpPerMin = 0;
    private boolean dirty = false;
    private boolean producesDp = false;
    private Map<ComponentType, Float> modifiers = new HashMap<ComponentType, Float>();

    public void setBaseDpPerMin(int baseDp) {
        this.producesDp = baseDp > 0;
        this.baseDpPerMin = baseDp;
    }

    public boolean producesDp() {
        return this.producesDp;
    }

    public int getDpPerMinute() {
        if (this.producesDp && this.dirty) {
            this.recalculate();
        }
        return this.dpPerMin;
    }

    public void registerModifier(ComponentType type, float modifier) {
        this.modifiers.put(type, Float.valueOf(modifier));
        this.dirty = true;
    }

    private void recalculate() {
        float totalModifier = 1.0f;
        for (Float mod : this.modifiers.values()) {
            totalModifier *= mod.floatValue();
        }
        this.dpPerMin = Math.round((float)this.baseDpPerMin * totalModifier);
        this.dirty = false;
    }
}

