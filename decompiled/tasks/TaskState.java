/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import gridLayout.CategoryNames;
import gridLayout.FilterOptions;
import languages.GameText;
import mainGuis.ColourPalette;
import toolbox.Colour;

public enum TaskState {
    UNSTARTED(GameText.getText(133), ColourPalette.MIDDLE_GREY, 300),
    IN_PROGRESS(GameText.getText(134), new Colour(133.0f, 113.0f, 102.0f, true), 200),
    CLAIM_REWARD(GameText.getText(135), ColourPalette.BASE_BLUE, 100),
    COMPLETE(GameText.getText(136), ColourPalette.LIGHT_GREEN, 400),
    LOCKED(GameText.getText(137), ColourPalette.MIDDLE_GREY, 500);

    private static final String ALL;
    private static final String REPEATING;
    public final Colour colour;
    public final String name;
    public final int weight;

    static {
        ALL = GameText.getText(1050);
        REPEATING = GameText.getText(641);
    }

    private TaskState(String name, Colour colour, int weight) {
        this.colour = colour;
        this.name = name;
        this.weight = weight;
    }

    public static FilterOptions getCategories() {
        TaskState[] states = TaskState.values();
        String[] cats = new String[states.length + 1];
        int i = 0;
        while (i < cats.length - 1) {
            cats[i] = states[i].name;
            ++i;
        }
        cats[cats.length - 1] = REPEATING;
        return new FilterOptions(new CategoryNames(ALL, cats, true));
    }
}

