/*
 * Decompiled with CFR 0.152.
 */
package perching;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import languages.GameText;
import org.lwjgl.util.vector.Vector4f;
import perching.PerchComponent;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class PerchCompBlueprint
extends ComponentBlueprint {
    private static final String NEST_SPOT = GameText.getText(1074);
    protected final Vector4f[] perchPositions;

    protected PerchCompBlueprint(Vector4f[] perchPositions) {
        super(ComponentType.PERCH);
        this.perchPositions = perchPositions;
    }

    @Override
    public Component createInstance() {
        return new PerchComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", NEST_SPOT));
    }
}

