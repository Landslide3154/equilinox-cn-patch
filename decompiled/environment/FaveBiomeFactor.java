/*
 * Decompiled with CFR 0.152.
 */
package environment;

import biomes.Biome;
import environment.EnviroFactor;
import environment.EnviroFactorBlueprint;
import gridLayout.FilterId;
import languages.GameText;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector3f;
import speciesInformation.SpeciesInfoLine;
import terrains.TerrainVertex;
import toolbox.Colour;
import utils.CSVReader;

public class FaveBiomeFactor
implements EnviroFactor {
    public static final int ID = 4;
    private static final String FAVE_BIOME = GameText.getText(919);
    private final FaveBiomeFactorBlueprint blueprint;
    private float factor;

    private FaveBiomeFactor(FaveBiomeFactorBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    @Override
    public String getName() {
        return FAVE_BIOME;
    }

    @Override
    public String getValue() {
        return String.valueOf(Math.round(this.factor * 100.0f)) + "%";
    }

    @Override
    public Colour getColour() {
        return ColourPalette.WHITE;
    }

    @Override
    public float getInfluence() {
        return this.blueprint.influence;
    }

    @Override
    public float recalculate(Vector3f position, TerrainVertex terrainVertex, int range) {
        this.factor = terrainVertex.getBiomePercent(this.blueprint.favouriteBiome) / 100.0f;
        return this.factor;
    }

    /* synthetic */ FaveBiomeFactor(FaveBiomeFactorBlueprint faveBiomeFactorBlueprint, FaveBiomeFactor faveBiomeFactor) {
        this(faveBiomeFactorBlueprint);
    }

    public static class FaveBiomeFactorBlueprint
    implements EnviroFactorBlueprint {
        private Biome favouriteBiome;
        private float influence;

        public FaveBiomeFactorBlueprint(Biome favouriteBiome, float influence) {
            this.favouriteBiome = favouriteBiome;
            this.influence = influence;
        }

        @Override
        public EnviroFactor createInstance() {
            return new FaveBiomeFactor(this, null);
        }

        @Override
        public void addFilterValues(FilterId filter) {
        }

        @Override
        public int compareTo(EnviroFactorBlueprint o) {
            return this.getPriority() - o.getPriority();
        }

        @Override
        public int getPriority() {
            return 1;
        }

        @Override
        public SpeciesInfoLine getInfo() {
            return new SpeciesInfoLine(FAVE_BIOME, this.favouriteBiome.toString());
        }

        public static FaveBiomeFactorBlueprint loadFaveBiomeFactor(CSVReader reader) {
            Biome fave = Biome.values()[reader.getNextLabelInt()];
            float influence = reader.getNextLabelFloat();
            return new FaveBiomeFactorBlueprint(fave, influence);
        }
    }
}

