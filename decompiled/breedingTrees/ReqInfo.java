/*
 * Decompiled with CFR 0.152.
 */
package breedingTrees;

import mainGuis.ColourPalette;
import toolbox.Colour;

public class ReqInfo {
    public final String name;
    public final String value;
    public final Colour valueColour;

    public ReqInfo(String name, String value, Colour colour) {
        this.name = name;
        this.value = value;
        this.valueColour = colour;
    }

    public ReqInfo(String name, String value) {
        this.name = name;
        this.value = value;
        this.valueColour = ColourPalette.BEIGE;
    }
}

