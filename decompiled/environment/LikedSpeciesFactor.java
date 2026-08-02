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

public class LikedSpeciesFactor
implements EnviroFactor {
    private static final String LIKED_SPECIES = GameText.getText(918);
    public static final int ID = 5;
    private final LikedSpeciesFactorBlueprint blueprint;
    private float factor;
    private int likedSpeciesCount;

    private LikedSpeciesFactor(LikedSpeciesFactorBlueprint blueprint) {
        this.blueprint = blueprint;
    }

    @Override
    public String getName() {
        return LIKED_SPECIES;
    }

    @Override
    public String getValue() {
        return String.valueOf(this.likedSpeciesCount) + "/" + this.blueprint.likedSpecies.length;
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
        boolean[] checkList = this.getSpeciesCheckList(position, range);
        float total = checkList.length;
        this.likedSpeciesCount = 0;
        boolean[] blArray = checkList;
        int n = checkList.length;
        int n2 = 0;
        while (n2 < n) {
            boolean b = blArray[n2];
            if (b) {
                ++this.likedSpeciesCount;
            }
            ++n2;
        }
        this.factor = (float)this.likedSpeciesCount / total;
        return this.factor;
    }

    private boolean[] getSpeciesCheckList(Vector3f pos, int range) {
        boolean[] checkList = new boolean[this.blueprint.likedSpecies.length];
        GridIterator iterator = GameManager.getWorld().getIterator(pos.x, pos.z, range);
        while (iterator.hasNext()) {
            GridSection square = iterator.next();
            this.compareSectionWithCheckList(square, checkList);
        }
        return checkList;
    }

    private void compareSectionWithCheckList(GridSection checkSection, boolean[] checkList) {
        if (checkSection != null) {
            int i = 0;
            while (i < this.blueprint.likedSpecies.length) {
                int n = i;
                checkList[n] = checkList[n] | checkSection.getEntityCount(this.blueprint.likedSpecies[i]) > 0;
                ++i;
            }
        }
    }

    /* synthetic */ LikedSpeciesFactor(LikedSpeciesFactorBlueprint likedSpeciesFactorBlueprint, LikedSpeciesFactor likedSpeciesFactor) {
        this(likedSpeciesFactorBlueprint);
    }

    public static class LikedSpeciesFactorBlueprint
    implements EnviroFactorBlueprint {
        private float influence;
        private Classification[] likedSpecies;

        public LikedSpeciesFactorBlueprint(Classification[] likedSpecies, float influence) {
            this.likedSpecies = likedSpecies;
            this.influence = influence;
        }

        @Override
        public EnviroFactor createInstance() {
            return new LikedSpeciesFactor(this, null);
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
            return 4;
        }

        @Override
        public SpeciesInfoLine getInfo() {
            String value = "";
            int i = 0;
            while (i < this.likedSpecies.length) {
                value = String.valueOf(value) + this.likedSpecies[i].getName();
                if (i != this.likedSpecies.length - 1) {
                    value = String.valueOf(value) + ", ";
                }
                ++i;
            }
            return new SpeciesInfoLine(LIKED_SPECIES, value);
        }

        public static LikedSpeciesFactorBlueprint loadLikedSpeciesFactor(CSVReader reader) {
            int count = reader.getNextLabelInt();
            Classification[] faveSpecies = new Classification[count];
            int i = 0;
            while (i < count) {
                String key = reader.getNextString();
                faveSpecies[i] = Classifier.getClassification(key);
                ++i;
            }
            return new LikedSpeciesFactorBlueprint(faveSpecies, reader.getNextLabelFloat());
        }
    }
}

