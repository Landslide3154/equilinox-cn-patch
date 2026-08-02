/*
 * Decompiled with CFR 0.152.
 */
package dayNightCycle;

import dayNightCycle.ColourCycle;
import toolbox.Colour;

public class LightColourCycle
extends ColourCycle {
    private static final Colour NIGHT_COLOUR = new Colour(0.42f, 0.45f, 0.9f);
    private static final Colour DAY_COLOUR = new Colour(1.0f, 0.95f, 0.95f);
    private static final Colour DAWN_COLOUR = new Colour(1.0f, 0.45f, 0.56f);
    private static final Colour DUSK_COLOUR = new Colour(1.0f, 0.87f, 0.6f);
    private static final float MIDNIGHT_END = 3.0f;
    private static final float MIDDAY_START = 8.0f;
    private static final float MIDDAY_END = 18.0f;

    public LightColourCycle() {
        super(NIGHT_COLOUR, DAY_COLOUR, DAWN_COLOUR, DUSK_COLOUR, 3.0f, 8.0f, 18.0f);
    }
}

