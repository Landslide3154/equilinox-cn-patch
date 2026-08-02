/*
 * Decompiled with CFR 0.152.
 */
package generation;

import java.util.LinkedHashMap;
import java.util.Map;
import toolbox.Colour;
import toolbox.Maths;
import world.WorldConfigs;

public class ColourCalculator {
    private static final Colour SEA_COLOUR = new Colour(214.0f, 237.0f, 146.0f, true);
    private static final Colour COAST_COLOUR = new Colour(210.0f, 193.0f, 134.0f, true);
    public static final Colour BACK_COLOUR = new Colour(111.0f, 80.0f, 45.0f, true);
    private static Map<Float, Colour> colourProfile = ColourCalculator.initColours();

    public static Map<Float, Colour> initColours() {
        LinkedHashMap<Float, Colour> colourProfile = new LinkedHashMap<Float, Colour>();
        colourProfile.put(Float.valueOf(-6.0f), SEA_COLOUR);
        colourProfile.put(Float.valueOf(-2.0f), COAST_COLOUR);
        colourProfile.put(Float.valueOf(4.0f), BACK_COLOUR);
        colourProfile.put(Float.valueOf(10.5f), new Colour(0.7f, 0.7f, 0.75f));
        return colourProfile;
    }

    public static Colour getColour(float height, WorldConfigs configs) {
        float factor = 8.0f / (configs.getMaxHeight() - configs.getWaterHeight());
        float aboveWater = height - configs.getWaterHeight();
        float altitude = aboveWater * factor;
        Map.Entry<Float, Colour> previous = null;
        Map.Entry<Float, Colour> next = null;
        for (Map.Entry<Float, Colour> entry : colourProfile.entrySet()) {
            if (previous == null) {
                previous = entry;
            }
            if (entry.getKey().floatValue() > altitude) {
                next = entry;
                break;
            }
            previous = entry;
        }
        float gap = ((Float)next.getKey()).floatValue() - ((Float)previous.getKey()).floatValue();
        float into = altitude - previous.getKey().floatValue();
        float blend = Maths.clamp(into / gap, 0.0f, 1.0f);
        return Colour.interpolateColours(previous.getValue(), next.getValue(), blend, null);
    }
}

