/*
 * Decompiled with CFR 0.152.
 */
package equipping;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import equipping.EquipComponent;
import java.util.List;
import java.util.Map;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class EquipCompBlueprint
extends ComponentBlueprint {
    private final Vector4f[] equipPositions;

    public EquipCompBlueprint(Vector3f[] equipPoints) {
        super(ComponentType.EQUIP);
        this.equipPositions = new Vector4f[equipPoints.length];
        int i = 0;
        while (i < this.equipPositions.length) {
            Vector3f pos = equipPoints[i];
            this.equipPositions[i] = new Vector4f(pos.x, pos.y, pos.z, 1.0f);
            ++i;
        }
    }

    protected Vector4f getPosition(int index) {
        return this.equipPositions[index];
    }

    @Override
    public Component createInstance() {
        return new EquipComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}

