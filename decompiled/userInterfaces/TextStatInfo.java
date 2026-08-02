/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import mainGuis.ColourPalette;
import toolbox.Colour;

public class TextStatInfo {
    public final Colour nameColour;
    public final Colour valueColour;
    public final String name;
    public final String value;
    public final String description;

    public TextStatInfo(String name, String value, String description) {
        this.name = name;
        this.description = description;
        this.value = value;
        this.nameColour = ColourPalette.WHITE;
        this.valueColour = ColourPalette.BEIGE;
    }

    public TextStatInfo(String name, String value, Colour mainColour, String description) {
        this.name = name;
        this.value = value;
        this.nameColour = mainColour;
        this.valueColour = mainColour;
        this.description = description;
    }

    public TextStatInfo(String name, String value, Colour nameColour, Colour valueColour, String description) {
        this.name = name;
        this.value = value;
        this.nameColour = nameColour;
        this.valueColour = valueColour;
        this.description = description;
    }
}

