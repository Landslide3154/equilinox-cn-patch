/*
 * Decompiled with CFR 0.152.
 */
package materials;

import languages.GameText;
import toolbox.Colour;

public enum PresetColour {
    CUSTOM(GameText.getText(939), new Colour()),
    RED(GameText.getText(940), new Colour(0.945f, 0.569f, 0.569f)),
    RUBY_RED(GameText.getText(1097), new Colour(162.0f, 79.0f, 79.0f, true)),
    YELLOW(GameText.getText(941), new Colour(0.835f, 0.827f, 0.502f)),
    ORANGE(GameText.getText(942), new Colour(0.882f, 0.706f, 0.494f)),
    DARK_ORANGE(GameText.getText(943), new Colour(0.678f, 0.404f, 0.333f)),
    LIGHT_BLUE(GameText.getText(944), new Colour(0.467f, 0.729f, 0.769f)),
    DARK_BLUE(GameText.getText(945), new Colour(0.314f, 0.329f, 0.545f)),
    PINK(GameText.getText(946), new Colour(0.933f, 0.631f, 0.823f)),
    LIGHT_PINK(GameText.getText(1096), new Colour(255.0f, 198.0f, 241.0f, true)),
    PURPLE(GameText.getText(947), new Colour(0.741f, 0.537f, 0.835f)),
    DARK_PURPLE(GameText.getText(948), new Colour(76.0f, 55.0f, 88.0f, true)),
    LILAC(GameText.getText(1093), new Colour(204.0f, 194.0f, 247.0f, true)),
    CYAN(GameText.getText(949), new Colour(0.533f, 0.89f, 0.792f)),
    BEIGE(GameText.getText(950), new Colour(209.0f, 192.0f, 162.0f, true)),
    MUD(GameText.getText(951), new Colour(105.0f, 102.0f, 83.0f, true)),
    LIGHT_BROWN(GameText.getText(952), new Colour(0.612f, 0.506f, 0.38f)),
    BROWN(GameText.getText(952), new Colour(0.51f, 0.4f, 0.306f)),
    DARK_BROWN(GameText.getText(953), new Colour(78.0f, 61.0f, 61.0f, true)),
    YELLOW_GREEN(GameText.getText(954), new Colour(0.655f, 0.733f, 0.439f)),
    DARK_GREEN(GameText.getText(955), new Colour(0.282f, 0.435f, 0.266f)),
    BLUE_GREEN(GameText.getText(956), new Colour(0.345f, 0.576f, 0.439f)),
    BRIGHT_GREEN(GameText.getText(1099), new Colour(149.0f, 245.0f, 161.0f, true)),
    LIGHT_GREEN(GameText.getText(957), new Colour(0.553f, 0.89f, 0.553f)),
    GREEN(GameText.getText(958), new Colour(0.365f, 0.588f, 0.365f)),
    PALE_GREEN(GameText.getText(959), new Colour(0.792f, 0.871f, 0.522f)),
    MUD_GREEN(GameText.getText(960), new Colour(0.478f, 0.522f, 0.325f)),
    GOLD(GameText.getText(961), new Colour(0.702f, 0.639f, 0.31f)),
    WHITE(GameText.getText(962), new Colour(0.9f, 0.9f, 0.9f)),
    GREY(GameText.getText(963), new Colour(0.5f, 0.5f, 0.5f)),
    LIGHT_GREY(GameText.getText(1094), new Colour(0.75f, 0.75f, 0.75f)),
    DARK_GREY(GameText.getText(1095), new Colour(0.25f, 0.25f, 0.25f)),
    BLACK(GameText.getText(964), new Colour(0.15f, 0.15f, 0.15f)),
    NEON_RED(GameText.getText(1132), new Colour(255.0f, 102.0f, 102.0f, true)),
    NEON_ORANGE(GameText.getText(1133), new Colour(255.0f, 202.0f, 91.0f, true)),
    NEON_YELLOW(GameText.getText(1134), new Colour(255.0f, 245.0f, 102.0f, true)),
    NEON_GREEN(GameText.getText(1135), new Colour(194.0f, 255.0f, 102.0f, true)),
    NEON_BLUE(GameText.getText(1136), new Colour(102.0f, 255.0f, 235.0f, true)),
    NEON_PURPLE(GameText.getText(1137), new Colour(125.0f, 85.0f, 255.0f, true)),
    NEON_PINK(GameText.getText(1139), new Colour(255.0f, 102.0f, 235.0f, true));

    private final Colour colour;
    private final String name;

    private PresetColour(String name, Colour colour) {
        this.colour = colour;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Colour getColour() {
        return this.colour;
    }
}

