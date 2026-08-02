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

public class UnsuitableBiomeFactor
implements EnviroFactor {
    private static final String UNSUITABLE = GameText.getText(917);
    public static final int ID = 3;
    private final UnsuitableBiomeFactorBlueprint blueprint;
    private float factor;

    private UnsuitableBiomeFactor(UnsuitableBiomeFactorBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    @Override
    public String getName() {
        return UNSUITABLE;
    }

    @Override
    public String getValue() {
        return String.valueOf(Math.round((1.0f - this.factor) * 100.0f)) + "%";
    }

    @Override
    public Colour getColour() {
        return Colour.interpolateColours(ColourPalette.BRIGHT_RED, ColourPalette.WHITE, this.factor * this.factor, null);
    }

    @Override
    public float getInfluence() {
        return this.blueprint.influence;
    }

    @Override
    public float recalculate(Vector3f position, TerrainVertex terrainVertex, int range) {
        float total = 0.0f;
        Biome[] biomeArray = this.blueprint.unsuitableBiomes;
        int n = biomeArray.length;
        int n2 = 0;
        while (n2 < n) {
            Biome biome = biomeArray[n2];
            total += terrainVertex.getBiomePercent(biome);
            ++n2;
        }
        this.factor = 1.0f - total / 100.0f;
        return this.factor;
    }

    /* synthetic */ UnsuitableBiomeFactor(UnsuitableBiomeFactorBlueprint unsuitableBiomeFactorBlueprint, UnsuitableBiomeFactor unsuitableBiomeFactor) {
        this(unsuitableBiomeFactorBlueprint);
    }

    public static class UnsuitableBiomeFactorBlueprint
    implements EnviroFactorBlueprint {
        private Biome[] unsuitableBiomes;
        private float influence;
        private boolean hasPreferred = false;

        public UnsuitableBiomeFactorBlueprint(Biome[] unsuitableBiomes, float influence) {
            this.unsuitableBiomes = unsuitableBiomes;
            this.influence = influence;
        }

        public void setHasPreferred() {
            this.hasPreferred = true;
        }

        @Override
        public EnviroFactor createInstance() {
            return new UnsuitableBiomeFactor(this, null);
        }

        @Override
        public int compareTo(EnviroFactorBlueprint o) {
            return this.getPriority() - o.getPriority();
        }

        @Override
        public int getPriority() {
            return 3;
        }

        @Override
        public void addFilterValues(FilterId filter) {
            if (!this.hasPreferred) {
                int i = 0;
                while (i < Biome.values().length) {
                    if (!this.hasBiome(i)) {
                        filter.add(1, 0, i);
                    }
                    ++i;
                }
            }
        }

        private boolean hasBiome(int biome) {
            Biome[] biomeArray = this.unsuitableBiomes;
            int n = this.unsuitableBiomes.length;
            int n2 = 0;
            while (n2 < n) {
                Biome b = biomeArray[n2];
                if (b.ordinal() == biome) {
                    return true;
                }
                ++n2;
            }
            return false;
        }

        @Override
        public SpeciesInfoLine getInfo() {
            String value = "";
            int i = 0;
            while (i < this.unsuitableBiomes.length) {
                value = String.valueOf(value) + this.unsuitableBiomes[i].toString();
                if (i != this.unsuitableBiomes.length - 1) {
                    value = String.valueOf(value) + ", ";
                }
                ++i;
            }
            return new SpeciesInfoLine(UNSUITABLE, value);
        }

        public static UnsuitableBiomeFactorBlueprint loadUnsuitableBiomeFactor(CSVReader reader) {
            Biome[] biomes = UnsuitableBiomeFactorBlueprint.loadBiomes(reader);
            return new UnsuitableBiomeFactorBlueprint(biomes, reader.getNextLabelFloat());
        }

        private static Biome[] loadBiomes(CSVReader reader) {
            int count = reader.getNextLabelInt();
            Biome[] biomes = new Biome[count];
            int i = 0;
            while (i < count) {
                biomes[i] = Biome.values()[reader.getNextInt()];
                ++i;
            }
            return biomes;
        }
    }
}

