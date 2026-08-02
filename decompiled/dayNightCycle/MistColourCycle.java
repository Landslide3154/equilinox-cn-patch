/*
 * Decompiled with CFR 0.152.
 */
package dayNightCycle;

import dayNightCycle.ColourCycle;
import toolbox.Colour;

public class MistColourCycle
extends ColourCycle {
    private static final Colour NIGHT_COLOUR = new Colour(74.0f, 104.0f, 156.0f, true);
    private static final Colour DAY_COLOUR = new Colour(246.0f, 229.0f, 197.0f, true);
    private static final Colour DAWN_COLOUR = new Colour(255.0f, 174.0f, 217.0f, true);
    private static final Colour DUSK_COLOUR = new Colour(255.0f, 222.0f, 210.0f, true);
    private static final float MIDNIGHT_END = 3.0f;
    private static final float MIDDAY_START = 8.0f;
    private static final float MIDDAY_END = 18.0f;

    public MistColourCycle() {
        super(NIGHT_COLOUR, DAY_COLOUR, DAWN_COLOUR, DUSK_COLOUR, 3.0f, 8.0f, 18.0f);
    }
}

