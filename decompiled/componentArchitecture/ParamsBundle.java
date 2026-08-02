/*
 * Decompiled with CFR 0.152.
 */
package componentArchitecture;

import componentArchitecture.ComponentParams;
import componentArchitecture.ComponentType;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ParamsBundle {
    private Map<ComponentType, ComponentParams> parameters = new HashMap<ComponentType, ComponentParams>();

    public ParamsBundle(ComponentParams ... params) {
        ComponentParams[] componentParamsArray = params;
        int n = params.length;
        int n2 = 0;
        while (n2 < n) {
            ComponentParams param = componentParamsArray[n2];
            this.parameters.put(param.getType(), param);
            ++n2;
        }
    }

    public void addParams(ComponentParams params) {
        this.parameters.put(params.getType(), params);
    }

    public ComponentParams getParameters(ComponentType type) {
        return this.parameters.get((Object)type);
    }

    public ComponentParams[] getParameterArray() {
        ComponentParams[] array = new ComponentParams[this.parameters.values().size()];
        Iterator<ComponentParams> iterator = this.parameters.values().iterator();
        int pointer = 0;
        while (iterator.hasNext()) {
            array[pointer++] = iterator.next();
        }
        return array;
    }
}

