/*
 * Decompiled with CFR 0.152.
 */
package graphicsOptions;

import java.util.ArrayList;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public enum DisplaySizes {
    FULL_SCREEN("Full-Screen", 0, 0),
    WIN_1024_576("1024 x 576", 1024, 576),
    WIN_1024_768("1024 x 768", 1024, 768),
    WIN_1280_720("1280 x 720", 1280, 720),
    WIN_1600_900("1600 x 900", 1600, 900),
    WIN_1920_1080("1920 x 1080", 1920, 1080),
    WIN_2560_1440("2560 x 1440", 2560, 1440),
    WIN_2560_1080("2560 x 1080", 2560, 1440),
    WIN_3200_1800("3200 x 1800", 3200, 1800);

    private final String name;
    private final int width;
    private final int height;

    private DisplaySizes(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        if (this == FULL_SCREEN) {
            return Display.getDesktopDisplayMode().getWidth();
        }
        return this.width;
    }

    public int getHeight() {
        if (this == FULL_SCREEN) {
            return Display.getDesktopDisplayMode().getHeight();
        }
        return this.height;
    }

    public String toString() {
        return this.name;
    }

    public static DisplaySizes[] getUsableDisplaySizes() {
        DisplayMode desktop = Display.getDesktopDisplayMode();
        ArrayList<DisplaySizes> sizes = new ArrayList<DisplaySizes>();
        int i = 0;
        while (i < DisplaySizes.values().length) {
            DisplaySizes size = DisplaySizes.values()[i];
            if (size.width <= 1280 || size.width <= desktop.getWidth() && size.height <= desktop.getHeight()) {
                sizes.add(size);
            }
            ++i;
        }
        return sizes.toArray(new DisplaySizes[sizes.size()]);
    }
}

