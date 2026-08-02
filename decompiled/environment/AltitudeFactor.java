/*
 * Decompiled with CFR 0.152.
 */
package environment;

import entityInfoGui.BarMouseOverGui;
import environment.EnviroFactor;
import environment.EnviroFactorBlueprint;
import gameManaging.GameManager;
import gridLayout.FilterId;
import languages.ComplexString;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import speciesInformation.SpeciesInfoLine;
import terrains.TerrainVertex;
import toolbox.Colour;
import toolbox.Maths;
import utils.CSVReader;

public class AltitudeFactor
implements EnviroFactor {
    private static final String FACTOR_NAME = GameText.getText(645);
    private static final String GOOD = GameText.getText(646);
    private static final String POOR = GameText.getText(647);
    private static final String NOT_IDEAL = GameText.getText(1123);
    private static final float TRANSITION = 0.25f;
    public static final int ID = 1;
    private AltitudeFactorBlueprint blueprint;
    private float altitudeFactor;
    private final float lowBound;
    private final float upBound;

    private AltitudeFactor(AltitudeFactorBlueprint blueprint) {
        this.blueprint = blueprint;
        this.upBound = (float)blueprint.maxAltitude * 1.25f;
        this.lowBound = (float)blueprint.minAltitude - (float)Math.abs(blueprint.minAltitude) * 0.25f;
    }

    @Override
    public String getName() {
        return FACTOR_NAME;
    }

    @Override
    public String getValue() {
        if (this.altitudeFactor <= 0.0f) {
            return POOR;
        }
        if (this.altitudeFactor >= 1.0f) {
            return GOOD;
        }
        return NOT_IDEAL;
    }

    @Override
    public float getInfluence() {
        return this.blueprint.influence;
    }

    @Override
    public float recalculate(Vector3f position, TerrainVertex vertex, int range) {
        float altitude = GameManager.getWorld().getAltitude(position.y);
        boolean noMin = this.blueprint.minAltitude == 0;
        boolean noMax = this.blueprint.maxAltitude == 100;
        float minFactor = noMin ? 1.0f : Maths.quickStep(this.lowBound, this.blueprint.minAltitude, altitude);
        float maxFactor = noMax ? 1.0f : 1.0f - Maths.quickStep(this.blueprint.maxAltitude, this.upBound, altitude);
        this.altitudeFactor = Math.min(minFactor, maxFactor);
        return this.altitudeFactor;
    }

    @Override
    public Colour getColour() {
        return Colour.interpolateColours(BarMouseOverGui.NEGATIVE_COLOUR, BarMouseOverGui.NORMAL_COLOUR, this.altitudeFactor, null);
    }

    /* synthetic */ AltitudeFactor(AltitudeFactorBlueprint altitudeFactorBlueprint, AltitudeFactor altitudeFactor) {
        this(altitudeFactorBlueprint);
    }

    public static class AltitudeFactorBlueprint
    implements EnviroFactorBlueprint {
        private static final String PREFERRED_ALT = GameText.getText(651);
        private static final ComplexString BELOW = GameText.getComplexText(648);
        private static final ComplexString ABOVE = GameText.getComplexText(649);
        private static final ComplexString BETWEEN = GameText.getComplexText(650);
        private int minAltitude;
        private int maxAltitude;
        private float influence;

        public AltitudeFactorBlueprint(int min, int max, float influence) {
            this.minAltitude = min;
            this.maxAltitude = max;
            this.influence = influence;
        }

        @Override
        public EnviroFactor createInstance() {
            return new AltitudeFactor(this, null);
        }

        @Override
        public void addFilterValues(FilterId filter) {
        }

        @Override
        public SpeciesInfoLine getInfo() {
            String value = null;
            value = this.minAltitude == 0 ? BELOW.getString(Integer.toString(this.maxAltitude)) : (this.maxAltitude == 100 ? ABOVE.getString(Integer.toString(this.minAltitude)) : BETWEEN.getString(Integer.toString(this.minAltitude), Integer.toString(this.maxAltitude)));
            return new SpeciesInfoLine(PREFERRED_ALT, value);
        }

        public static EnviroFactorBlueprint loadAltitudeFactor(CSVReader reader) {
            int min = reader.getNextLabelInt();
            int max = reader.getNextLabelInt();
            return new AltitudeFactorBlueprint(min, max, reader.getNextLabelFloat());
        }

        @Override
        public int compareTo(EnviroFactorBlueprint o) {
            return this.getPriority() - o.getPriority();
        }

        @Override
        public int getPriority() {
            return 0;
        }
    }
}

