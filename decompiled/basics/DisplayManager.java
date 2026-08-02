/*
 * Decompiled with CFR 0.152.
 */
package basics;

import errors.ErrorManager;
import graphicsOptions.DisplaySizes;
import graphicsOptions.GraphicsOptions;
import languages.GameText;
import org.lwjgl.Sys;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.PixelFormat;
import toolbox.IconLoader;
import toolbox.RollingAverage;
import utils.FileUtils;
import utils.MyFile;

public class DisplayManager {
    private static DisplaySizes DISPLAY_SIZE = GraphicsOptions.displaySize;
    private static int WIDTH = GraphicsOptions.displaySize.getWidth();
    private static int HEIGHT = GraphicsOptions.displaySize.getHeight();
    public static final int FPS_CAP = 100;
    private static final float MAX_DELTA = 0.1f;
    private static final float STABLE_DELTA_TIME = 2.0f;
    private static final int TITLE_TEXT_ID = 1;
    private static final MyFile ICON16_FILE = new MyFile(FileUtils.RES_FOLDER, "icon16.png");
    private static final MyFile ICON32_FILE = new MyFile(FileUtils.RES_FOLDER, "icon32.png");
    private static final MyFile ICON128_FILE = new MyFile(FileUtils.RES_FOLDER, "icon128.png");
    private static long lastFrameTime;
    private static float delta;
    private static float time;
    private static float aspectRatio;
    private static boolean closeRequested;
    private static int UI_WIDTH;
    private static int UI_HEIGHT;
    private static float UI_SCALE;
    private static boolean BORDERLESS;
    private static RollingAverage deltaAverage;

    static {
        time = 0.0f;
        closeRequested = false;
        deltaAverage = new RollingAverage(15);
    }

    public static void createDisplay(boolean tryBoth) {
        ContextAttribs attribs = new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true);
        try {
            DisplayMode desktop = Display.getDesktopDisplayMode();
            if (GraphicsOptions.displaySize == DisplaySizes.FULL_SCREEN || WIDTH > desktop.getWidth() || HEIGHT > desktop.getHeight()) {
                Display.setDisplayModeAndFullscreen(desktop);
                GraphicsOptions.displaySize = DisplaySizes.FULL_SCREEN;
                DISPLAY_SIZE = DisplaySizes.FULL_SCREEN;
            } else {
                Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            }
            BORDERLESS = GraphicsOptions.BORDERLESS;
            if (BORDERLESS) {
                System.setProperty("org.lwjgl.opengl.Window.undecorated", "true");
            }
            Display.setIcon(IconLoader.load(ICON16_FILE, ICON32_FILE, ICON128_FILE));
            Display.setVSyncEnabled(GraphicsOptions.VSYNC);
            Display.setInitialBackground(1.0f, 1.0f, 1.0f);
            Display.setTitle(GameText.getText(1));
            Display.create(new PixelFormat(), attribs);
            WIDTH = Display.getWidth();
            HEIGHT = Display.getHeight();
            UI_SCALE = GraphicsOptions.UI_SIZE;
            UI_WIDTH = (int)((float)WIDTH / GraphicsOptions.UI_SIZE);
            UI_HEIGHT = (int)((float)HEIGHT / GraphicsOptions.UI_SIZE);
        }
        catch (Exception e) {
            ErrorManager.createErrorLog("FailedDisplayCreation", e);
            e.printStackTrace();
            if (tryBoth) {
                GraphicsOptions.displaySize = DisplaySizes.FULL_SCREEN;
                DisplayManager.createDisplay(false);
            }
            ErrorManager.crashWithUserAlert("Failed to Launch!", "Equilinox has unfortunately failed to create a display. This usually happens when the computer does not support the required version of OpenGL (3.3+) so please check to see if that is the case. You can contact the developer at thinmatrix@gmail.com for more help.", e);
        }
        GL11.glViewport(0, 0, WIDTH, HEIGHT);
        aspectRatio = (float)WIDTH / (float)HEIGHT;
        lastFrameTime = DisplayManager.getCurrentTime();
        delta = 0.01f;
    }

    public static float getUiScale() {
        return UI_SCALE;
    }

    public static boolean isBorderless() {
        return BORDERLESS;
    }

    public static DisplaySizes getDisplaySize() {
        return DISPLAY_SIZE;
    }

    public static boolean hasIntegerScaleGuis() {
        return UI_SCALE % 1.0f == 0.0f;
    }

    public static int getWidth() {
        return WIDTH;
    }

    public static boolean isMinitureWidth() {
        return WIDTH < 1100;
    }

    public static boolean isMinitureHeight() {
        return HEIGHT < 700;
    }

    public static int getHeight() {
        return HEIGHT;
    }

    public static int getUiWidth() {
        return UI_WIDTH;
    }

    public static int getUiHeight() {
        return UI_HEIGHT;
    }

    public static float getDeltaSeconds() {
        return delta;
    }

    public static float getAspectRatio() {
        return aspectRatio;
    }

    public static float getTime() {
        return time;
    }

    public static void updateDisplay() {
        Display.update();
        Display.sync(100);
        DisplayManager.updateDelta();
    }

    public static void closeDisplay() {
        Display.destroy();
    }

    public static void requestClosure() {
        closeRequested = true;
    }

    public static boolean isOpen() {
        return !Display.isCloseRequested() && !closeRequested;
    }

    private static void updateDelta() {
        long currentFrameTime = DisplayManager.getCurrentTime();
        float del = Math.min(0.1f, (float)(currentFrameTime - lastFrameTime) / 1000.0f);
        lastFrameTime = currentFrameTime;
        if ((time += del) < 2.0f) {
            del = 0.01f;
        }
        deltaAverage.addValue(del);
        delta = deltaAverage.calculate();
    }

    private static long getCurrentTime() {
        return Sys.getTime() * 1000L / Sys.getTimerResolution();
    }
}

