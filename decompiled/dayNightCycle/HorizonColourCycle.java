/*
 * Decompiled with CFR 0.152.
 */
package dayNightCycle;

import dayNightCycle.ColourCycle;
import toolbox.Colour;

public class HorizonColourCycle
extends ColourCycle {
    private static final Colour NIGHT_COLOUR = new Colour(51.0f, 51.0f, 108.0f, true);
    private static final Colour DAY_COLOUR = new Colour(0.6f, 0.9f, 1.0f);
    private static final Colour DAWN_COLOUR = new Colour(238.0f, 124.0f, 145.0f, true);
    private static final Colour DUSK_COLOUR = new Colour(255.0f, 160.0f, 119.0f, true);
    private static final float MIDNIGHT_END = 3.0f;
    private static final float MIDDAY_START = 8.0f;
    private static final float MIDDAY_END = 18.0f;

    public HorizonColourCycle() {
        super(NIGHT_COLOUR, DAY_COLOUR, DAWN_COLOUR, DUSK_COLOUR, 3.0f, 8.0f, 18.0f);
    }
}

