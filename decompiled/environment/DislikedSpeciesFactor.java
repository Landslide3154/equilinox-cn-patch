/*
 * Decompiled with CFR 0.152.
 */
package environment;

import classification.Classification;
import classification.Classifier;
import environment.EnviroFactor;
import environment.EnviroFactorBlueprint;
import gameManaging.GameManager;
import gridLayout.FilterId;
import languages.GameText;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector3f;
import speciesInformation.SpeciesInfoLine;
import terrains.TerrainVertex;
import toolbox.Colour;
import utils.CSVReader;
import world.GridIterator;
import world.GridSection;

public class DislikedSpeciesFactor
implements EnviroFactor {
    private static final String FACTOR_NAME = GameText.getText(265);
    public static final int ID = 6;
    private final DislikedSpeciesFactorBlueprint blueprint;
    private float factor;
    private int dislikedSpeciesCount;

    private DislikedSpeciesFactor(DislikedSpeciesFactorBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    @Override
    public String getName() {
        return FACTOR_NAME;
    }

    @Override
    public String getValue() {
        return String.valueOf(this.dislikedSpeciesCount) + "/" + this.blueprint.dislikedSpecies.length;
    }

    @Override
    public Colour getColour() {
        float fac = 1.0f - this.factor;
        return Colour.interpolateColours(ColourPalette.BRIGHT_RED, ColourPalette.WHITE, fac * fac, null);
    }

    @Override
    public float getInfluence() {
        return this.blueprint.influence;
    }

    @Override
    public float recalculate(Vector3f position, TerrainVertex terrainVertex, int range) {
        boolean[] checkList = this.getSpeciesCheckList(position, range);
        float total = checkList.length;
        this.dislikedSpeciesCount = 0;
        boolean[] blArray = checkList;
        int n = checkList.length;
        int n2 = 0;
        while (n2 < n) {
            boolean b = blArray[n2];
            if (b) {
                ++this.dislikedSpeciesCount;
            }
            ++n2;
        }
        this.factor = (float)this.dislikedSpeciesCount / total;
        return 1.0f - this.factor;
    }

    private boolean[] getSpeciesCheckList(Vector3f pos, int range) {
        boolean[] checkList = new boolean[this.blueprint.dislikedSpecies.length];
        GridIterator iterator = GameManager.getWorld().getIterator(pos.x, pos.z, range);
        while (iterator.hasNext()) {
            this.compareSectionWithCheckList(iterator.next(), checkList);
        }
        return checkList;
    }

    private void compareSectionWithCheckList(GridSection checkSection, boolean[] checkList) {
        int i = 0;
        while (i < this.blueprint.dislikedSpecies.length) {
            int n = i;
            checkList[n] = checkList[n] | checkSection.getEntityCount(this.blueprint.dislikedSpecies[i]) > 0;
            ++i;
        }
    }

    /* synthetic */ DislikedSpeciesFactor(DislikedSpeciesFactorBlueprint dislikedSpeciesFactorBlueprint, DislikedSpeciesFactor dislikedSpeciesFactor) {
        this(dislikedSpeciesFactorBlueprint);
    }

    public static class DislikedSpeciesFactorBlueprint
    implements EnviroFactorBlueprint {
        private float influence;
        private Classification[] dislikedSpecies;

        public DislikedSpeciesFactorBlueprint(Classification[] dislikedSpecies, float influence) {
            this.dislikedSpecies = dislikedSpecies;
            this.influence = influence;
        }

        @Override
        public EnviroFactor createInstance() {
            return new DislikedSpeciesFactor(this, null);
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
            return 5;
        }

        @Override
        public SpeciesInfoLine getInfo() {
            String value = "";
            int i = 0;
            while (i < this.dislikedSpecies.length) {
                value = String.valueOf(value) + this.dislikedSpecies[i].getName();
                if (i != this.dislikedSpecies.length - 1) {
                    value = String.valueOf(value) + ", ";
                }
                ++i;
            }
            return new SpeciesInfoLine(FACTOR_NAME, value);
        }

        public static DislikedSpeciesFactorBlueprint loadDisikedSpeciesFactor(CSVReader reader) {
            int count = reader.getNextLabelInt();
            Classification[] faveSpecies = new Classification[count];
            int i = 0;
            while (i < count) {
                String key = reader.getNextString();
                faveSpecies[i] = Classifier.getClassification(key);
                ++i;
            }
            return new DislikedSpeciesFactorBlueprint(faveSpecies, reader.getNextLabelFloat());
        }
    }
}

