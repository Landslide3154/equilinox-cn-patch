/*
 * Decompiled with CFR 0.152.
 */
package dayNightCycle;

import dayNightCycle.ColourCycle;
import toolbox.Colour;

public class SkyColourCycle
extends ColourCycle {
    private static final Colour NIGHT_COLOUR = new Colour(78.0f, 113.0f, 164.0f, true);
    private static final Colour DAY_COLOUR = new Colour(246.0f, 236.0f, 229.0f, true);
    private static final Colour DAWN_COLOUR = new Colour(254.0f, 197.0f, 145.0f, true);
    private static final Colour DUSK_COLOUR = new Colour(255.0f, 239.0f, 170.0f, true);
    private static final float MIDNIGHT_END = 3.0f;
    private static final float MIDDAY_START = 8.0f;
    private static final float MIDDAY_END = 18.0f;

    public SkyColourCycle() {
        super(NIGHT_COLOUR, DAY_COLOUR, DAWN_COLOUR, DUSK_COLOUR, 3.0f, 8.0f, 18.0f);
    }
}

