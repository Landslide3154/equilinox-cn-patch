/*
 * Decompiled with CFR 0.152.
 */
package biomes;

import biomes.Biome;
import biomes.SpreaderComponent;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class SpreaderCompBlueprint
extends ComponentBlueprint {
    private static final String SPREADS = GameText.getText(914);
    private static final String BIOME = GameText.getText(915);
    public final Biome biome;
    public final float strength;
    public final int distance;
    public final int rangeSquared;
    public final Vector3f colourOffsets;

    protected SpreaderCompBlueprint(Biome biome, float strength, int distance, Vector3f colourOffsets) {
        super(ComponentType.SPREADER);
        this.biome = biome;
        this.strength = strength;
        this.distance = distance;
        this.rangeSquared = distance * distance;
        this.colourOffsets = colourOffsets;
    }

    @Override
    public Component createInstance() {
        return new SpreaderComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.GENERAL).add(new SpeciesInfoLine(SPREADS, String.valueOf(this.biome.toString()) + " " + BIOME));
    }
}

