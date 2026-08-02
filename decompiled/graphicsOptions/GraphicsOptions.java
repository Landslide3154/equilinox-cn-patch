/*
 * Decompiled with CFR 0.152.
 */
package graphicsOptions;

import environment.EnvironmentVariables;
import graphicsOptions.DisplaySizes;
import java.io.IOException;
import utils.BinaryReader;
import utils.BinaryWriter;

public class GraphicsOptions {
    public static boolean HD_WATER = true;
    public static boolean SHADOWS = true;
    public static boolean ANTI_ALIASING = true;
    public static boolean DOF_EFFECT = false;
    public static boolean SUN_RAYS = false;
    public static int MSAA_SAMPLES = 4;
    public static boolean LENS_FLARE = true;
    public static boolean PARTICLES = true;
    public static boolean BORDERLESS = false;
    public static boolean VSYNC = false;
    public static float UI_SIZE = 1.0f;
    public static DisplaySizes displaySize = DisplaySizes.WIN_1280_720;

    public static boolean needsPostProcessing() {
        return DOF_EFFECT || SUN_RAYS && EnvironmentVariables.getVariables().isSunVisible();
    }

    public static void saveSettings(BinaryWriter writer) throws IOException {
        writer.writeInt(displaySize.ordinal());
        writer.writeBoolean(BORDERLESS);
        writer.writeBoolean(HD_WATER);
        writer.writeBoolean(SHADOWS);
        writer.writeBoolean(ANTI_ALIASING);
        writer.writeBoolean(DOF_EFFECT);
        writer.writeBoolean(SUN_RAYS);
        writer.writeBoolean(LENS_FLARE);
        writer.writeBoolean(PARTICLES);
        writer.writeBoolean(VSYNC);
        writer.writeFloat(UI_SIZE);
    }

    public static void loadSettings(BinaryReader reader) throws Exception {
        displaySize = DisplaySizes.values()[reader.readInt()];
        BORDERLESS = reader.readBoolean();
        HD_WATER = reader.readBoolean();
        SHADOWS = reader.readBoolean();
        ANTI_ALIASING = reader.readBoolean();
        DOF_EFFECT = reader.readBoolean();
        SUN_RAYS = reader.readBoolean();
        LENS_FLARE = reader.readBoolean();
        PARTICLES = reader.readBoolean();
        VSYNC = reader.readBoolean();
        UI_SIZE = reader.readFloat();
        if (UI_SIZE == 0.0f) {
            UI_SIZE = 1.0f;
        }
    }
}

