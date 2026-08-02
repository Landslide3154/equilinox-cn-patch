/*
 * Decompiled with CFR 0.152.
 */
package saves;

import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import instances.Entity;
import java.util.Map;
import saves.SaveAdapter;
import utils.BinaryReader;

public class AdapterBundle {
    private final SaveAdapter[] adapters;

    public AdapterBundle(SaveAdapter ... adapters) {
        this.adapters = adapters;
    }

    public boolean tryAlternativeLoad(int blueprintId, Entity entity, Map<ComponentType, ComponentBlueprint> components, ComponentBundle compBundle, BinaryReader reader) {
        int version = reader.getVersion();
        SaveAdapter[] saveAdapterArray = this.adapters;
        int n = this.adapters.length;
        int n2 = 0;
        while (n2 < n) {
            SaveAdapter adapter = saveAdapterArray[n2];
            if (adapter.required(version, blueprintId)) {
                adapter.loadOldVersionComponents(entity, components, compBundle, reader);
                return true;
            }
            ++n2;
        }
        return false;
    }
}

