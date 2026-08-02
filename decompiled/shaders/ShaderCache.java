/*
 * Decompiled with CFR 0.152.
 */
package shaders;

import java.util.HashMap;
import java.util.Map;

public class ShaderCache {
    private Map<Integer, Boolean> booleanUniforms = new HashMap<Integer, Boolean>();
    private Map<Integer, Float> floatUniforms = new HashMap<Integer, Float>();

    public boolean needsUpdating(Integer location, boolean value) {
        Boolean currentValue = this.booleanUniforms.get(location);
        if (currentValue == null || currentValue != value) {
            this.booleanUniforms.put(location, value);
            return true;
        }
        return false;
    }

    public boolean needsUpdating(int location, float value) {
        Float currentValue = this.floatUniforms.get(location);
        if (currentValue == null || currentValue.floatValue() != value) {
            this.floatUniforms.put(location, Float.valueOf(value));
            return true;
        }
        return false;
    }
}

