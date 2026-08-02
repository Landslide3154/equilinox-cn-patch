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
import toolbox.Maths;
import utils.CSVReader;

public class PreferredBiomeFactor
implements EnviroFactor {
    private static final String SUITABLE_BIOME = GameText.getText(617);
    private static final String SUITABLE_BIOMES = GameText.getText(618);
    private static final String BARREN = GameText.getText(619);
    public static final int ID = 2;
    public static final float BARREN_DAMPING = 0.7f;
    private final PreferredBiomeFactorBlueprint blueprint;
    private float factor;

    private PreferredBiomeFactor(PreferredBiomeFactorBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    @Override
    public String getName() {
        return SUITABLE_BIOME;
    }

    @Override
    public String getValue() {
        return String.valueOf(Math.round(this.factor * 100.0f)) + "%";
    }

    @Override
    public Colour getColour() {
        float fac = Maths.clamp(this.factor / 0.9f, 0.0f, 1.0f);
        return Colour.interpolateColours(ColourPalette.BRIGHT_RED, ColourPalette.WHITE, fac * fac, null);
    }

    @Override
    public float getInfluence() {
        return this.blueprint.influence;
    }

    @Override
    public float recalculate(Vector3f position, TerrainVertex terrainVertex, int range) {
        float total = 0.0f;
        Biome[] biomeArray = this.blueprint.suitableBiomes;
        int n = biomeArray.length;
        int n2 = 0;
        while (n2 < n) {
            Biome biome = biomeArray[n2];
            total += terrainVertex.getBiomePercent(biome);
            ++n2;
        }
        if (this.blueprint.growsInBarren) {
            total += Math.max(0.0f, (float)(100 - terrainVertex.getTotalWeights()) * 0.7f);
        }
        this.factor = Math.min(1.0f, total / this.blueprint.idealFactor);
        return this.factor;
    }

    /* synthetic */ PreferredBiomeFactor(PreferredBiomeFactorBlueprint preferredBiomeFactorBlueprint, PreferredBiomeFactor preferredBiomeFactor) {
        this(preferredBiomeFactorBlueprint);
    }

    public static class PreferredBiomeFactorBlueprint
    implements EnviroFactorBlueprint {
        private Biome[] suitableBiomes;
        private boolean growsInBarren;
        private float influence;
        private float idealFactor;

        public PreferredBiomeFactorBlueprint(Biome[] suitableBiomes, boolean growsInBarren, float idealFactor, float influence) {
            this.suitableBiomes = suitableBiomes;
            this.growsInBarren = growsInBarren;
            this.influence = influence;
            this.idealFactor = idealFactor;
        }

        @Override
        public EnviroFactor createInstance() {
            return new PreferredBiomeFactor(this, null);
        }

        @Override
        public int compareTo(EnviroFactorBlueprint o) {
            return this.getPriority() - o.getPriority();
        }

        @Override
        public int getPriority() {
            return 2;
        }

        @Override
        public SpeciesInfoLine getInfo() {
            String value = "";
            if (this.growsInBarren) {
                value = String.valueOf(value) + BARREN;
                if (this.suitableBiomes.length > 0) {
                    value = String.valueOf(value) + ", ";
                }
            }
            int i = 0;
            while (i < this.suitableBiomes.length) {
                value = String.valueOf(value) + this.suitableBiomes[i].toString();
                if (i != this.suitableBiomes.length - 1) {
                    value = String.valueOf(value) + ", ";
                }
                ++i;
            }
            return new SpeciesInfoLine(SUITABLE_BIOMES, value);
        }

        public static PreferredBiomeFactorBlueprint loadSuitableBiomeFactor(CSVReader reader) {
            boolean growsBarren = reader.getNextLabelBool();
            Biome[] biomes = PreferredBiomeFactorBlueprint.loadBiomes(reader);
            float idealFactor = reader.getNextLabelFloat();
            float influence = reader.getNextLabelFloat();
            return new PreferredBiomeFactorBlueprint(biomes, growsBarren, idealFactor, influence);
        }

        private static Biome[] loadBiomes(CSVReader reader) {
            int count = reader.getNextLabelInt();
            if (count == 0) {
                return null;
            }
            Biome[] biomes = new Biome[count];
            int i = 0;
            while (i < count) {
                biomes[i] = Biome.values()[reader.getNextInt()];
                ++i;
            }
            return biomes;
        }

        @Override
        public void addFilterValues(FilterId filter) {
            Biome[] biomeArray = this.suitableBiomes;
            int n = this.suitableBiomes.length;
            int n2 = 0;
            while (n2 < n) {
                Biome biome = biomeArray[n2];
                filter.add(1, 0, biome.ordinal());
                ++n2;
            }
        }
    }
}

