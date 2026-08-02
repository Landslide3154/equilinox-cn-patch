/*
 * Decompiled with CFR 0.152.
 */
package mainGuis;

import basics.DisplayManager;
import guis.GuiMaster;
import textures.Texture;
import utils.MyFile;

public class GuiRepository {
    public static final Texture DP_ICON = GuiRepository.loadGuiTexture("dpIcon2.png");
    public static final Texture BLOCK = GuiRepository.loadGuiTexture("plain.png");
    public static final Texture INFO = GuiRepository.loadGuiTexture("info.png");
    public static final Texture LOCKED = GuiRepository.loadGuiTexture("locked.png");
    public static final Texture UNLOCKED = GuiRepository.loadGuiTexture("unlocked.png");
    public static final Texture BLOB = GuiRepository.loadGuiTexture("blob.png");
    public static final Texture EXIT = GuiRepository.loadGuiTexture("exit.png");
    public static final Texture SEPARATOR = GuiRepository.loadGuiTexture("separator.png");
    public static final Texture UNKNOWN = GuiRepository.loadGuiTexture("unknown.png");
    public static final Texture SUN = GuiRepository.loadGuiTexture("sun.png");
    public static final Texture DISEASE = GuiRepository.loadGuiTexture("diseaseWhite.png");
    public static final Texture TASKS = GuiRepository.loadGuiTexture("tasks.png");
    public static final Texture MUSIC = GuiRepository.loadGuiTexture("music.png");
    public static final Texture CONFIRM = GuiRepository.loadGuiTexture("confirm.png");
    public static final Texture DELETE = GuiRepository.loadGuiTexture("delete.png");
    public static final Texture TICK = GuiRepository.loadGuiTexture("tick.png");
    public static final Texture CROSS = GuiRepository.loadGuiTexture("cross.png");
    public static final Texture TM = GuiRepository.loadGuiTexture("tmSplash.png");
    public static final Texture JGM = GuiRepository.loadGuiTexture("jgmSplash.png");
    public static final Texture DS = GuiRepository.loadGuiTexture("dsSplash.png");
    public static final Texture BLACKOUT = GuiRepository.loadGuiTexture("black2.png");
    public static final Texture PIN = GuiRepository.loadGuiTexture("pin.png");
    public static final Texture NEW_SPECIES = GuiRepository.loadGuiTexture("newSpecies.png");
    public static final Texture NEW = GuiRepository.loadGuiTexture("new.png");
    public static final Texture BREED_ICON = GuiRepository.loadGuiTexture("speedyBreed.png");
    public static final Texture STATS = GuiRepository.loadPixelPerfectTexture("stats.png");
    public static final Texture FADE = GuiRepository.loadGuiTexture("greenFade.png");
    public static final Texture COOL = GuiRepository.loadGuiTexture("cool.png");
    public static final Texture EDIT = GuiRepository.loadGuiTexture("edit.png");
    public static final Texture DNA_MAIN = GuiRepository.loadGuiTexture("dnaBigFade.png");
    public static final Texture DNA_BUTTON = GuiRepository.loadGuiTexture("dnaButton.png");
    public static final Texture CROSS_HAIR = GuiRepository.loadPixelPerfectTexture("crosshair.png");
    public static final Texture SELECT_BAR = GuiRepository.loadPixelPerfectTexture("selectorBar.png");
    public static final Texture POINTER = GuiRepository.loadGuiTexture("pointer.png");
    public static final Texture COLOUR_WHEEL = GuiRepository.loadGuiTexture("colourWheel.png");
    public static final Texture BRIGHT_CONTROL = GuiRepository.loadGuiTexture("brightnessControl.png");
    public static final Texture PLAY = GuiRepository.loadGuiTexture("play.png");
    public static final Texture WAIT = GuiRepository.loadGuiTexture("wait.png");
    public static final Texture REPEAT = GuiRepository.loadGuiTexture("repeat.png");
    public static final Texture DOWN_ARROW = GuiRepository.loadGuiTexture("downArrow.png");
    public static final Texture NOTIFY_BELL = GuiRepository.loadGuiTexture("notifyBell.png");
    public static final Texture NOTIFY_BELL_CIRCLE = GuiRepository.loadGuiTexture("notifyBellCircle.png");
    public static final Texture TICK_EMPTY = GuiRepository.loadPixelPerfectTexture("emptyTick.png");
    public static final Texture TICK_FILL = GuiRepository.loadPixelPerfectTexture("filledTick.png");
    public static final Texture PLUS = GuiRepository.loadPixelPerfectTexture("plus.png");
    public static final Texture MINUS = GuiRepository.loadPixelPerfectTexture("minus.png");
    public static final Texture FAST_FORWARD = GuiRepository.loadGuiTexture("fastForward.png");
    public static final Texture FAST_FORWARD2 = GuiRepository.loadGuiTexture("fastForward2.png");
    public static final Texture RIGHT_ARROW = GuiRepository.loadGuiTexture("rightArrow.png");
    public static final Texture LEFT_ARROW = GuiRepository.loadGuiTexture("leftArrow.png");
    public static final Texture HELP_LIL_ICON = GuiRepository.loadGuiTexture("help2.png");
    public static final Texture DROP_MENU_ARROW = GuiRepository.loadGuiTexture("dropMenuArrow.png");
    public static final Texture EVOLVE_PAUSE = GuiRepository.loadPixelPerfectTexture("evolvePause.png");
    public static final Texture BREED = GuiRepository.loadPixelPerfectTexture("evolveIcon.png");
    public static final Texture BREED20 = GuiRepository.loadPixelPerfectTexture("evolveIcon20.png");
    public static final Texture BREED_UP_UP = GuiRepository.loadPixelPerfectTexture("evolveIconUp.png");
    public static final Texture DNA_ICON = GuiRepository.loadPixelPerfectTexture("dna2.png");
    public static final Texture HAND = GuiRepository.loadPixelPerfectTexture("handRight.png");
    public static final Texture INFO_ICON = GuiRepository.loadPixelPerfectTexture("info2.png");
    public static final Texture INFO_ICON_20 = GuiRepository.loadPixelPerfectTexture("infoIcon.png");
    public static final Texture STRONG = GuiRepository.loadPixelPerfectTexture("strong.png");
    public static final Texture CIRCLE_TICK = GuiRepository.loadPixelPerfectTexture("circleTick.png");
    public static final Texture MUSIC_ICON = GuiRepository.loadPixelPerfectTexture("musicIcon.png");
    public static final Texture HELP_ICON2 = GuiRepository.loadPixelPerfectTexture("helpIcon2.png");
    public static final Texture HELP_ICON_BIG = GuiRepository.loadGuiTexture("helpIcon.png");
    public static final Texture LIST_ICON = GuiRepository.loadPixelPerfectTexture("chart20.png");
    public static final Texture EYEDROP = GuiRepository.loadPixelPerfectTexture("eyedrop.png");
    public static final Texture SPECIES_ICON = GuiRepository.loadPixelPerfectTexture("speciesIcon.png");
    public static final Texture TASK_ICON = GuiRepository.loadPixelPerfectTexture("taskIcon.png");
    public static final Texture LOCK_ICON = GuiRepository.loadGuiTexture("lock36.png");
    public static final Texture CIRCLE_TICK_EMPTY = GuiRepository.loadPixelPerfectTexture("circleTickEmpty.png");
    public static final Texture CONTROL = GuiRepository.loadPixelPerfectTexture("controls18.png");
    public static final Texture PLAYLIST = GuiRepository.loadGuiTexture("playlist.png");
    public static final Texture TASKS_256 = GuiRepository.loadGuiTexture("tasks256.png");
    public static final Texture SPECIES_256 = GuiRepository.loadGuiTexture("species256.png");
    public static final Texture ANIMAL_128 = GuiRepository.loadGuiTexture("animal128.png");
    public static final Texture DNA_256 = GuiRepository.loadGuiTexture("dna256.png");
    public static final Texture TASK_DONE_256 = GuiRepository.loadGuiTexture("taskDone256.png");
    public static final Texture ITEMS_256 = GuiRepository.loadGuiTexture("items256.png");
    public static final Texture MUSIC_256 = GuiRepository.loadGuiTexture("music256.png");
    public static final Texture WELCOME = GuiRepository.loadGuiTexture("welcome.png");
    public static final Texture PROG_BAR = GuiRepository.loadGuiTexture("loading.png");
    public static final Texture CHECK_EMPTY = GuiRepository.loadGuiTexture("checkboxBlank.png");
    public static final Texture CHECK_FILLED = GuiRepository.loadGuiTexture("checkboxFilled.png");
    public static final Texture ARROW_ON = GuiRepository.loadPixelPerfectTexture("arrowRightOn.png");
    public static final Texture ARROW_OFF = GuiRepository.loadPixelPerfectTexture("arrowRightOff.png");
    public static final Texture ARROW_OFF_2 = GuiRepository.loadPixelPerfectTexture("arrowRightOff2.png");
    public static final Texture TICK_ICON = GuiRepository.loadPixelPerfectTexture("tick20.png");
    public static final Texture ANIMAL_ICON = GuiRepository.loadPixelPerfectTexture("animal3.png");
    public static final Texture SPANNER_OFF = GuiRepository.loadPixelPerfectTexture("spannerOff.png");
    public static final Texture SPANNER_ON = GuiRepository.loadPixelPerfectTexture("spannerOn.png");
    public static final Texture SPANNER_ONLY = GuiRepository.loadPixelPerfectTexture("spannerOnly.png");
    public static final Texture GS1 = GuiRepository.loadGuiTexture("helpPages/getStart1.png");
    public static final Texture GS2 = GuiRepository.loadGuiTexture("helpPages/getStart2.png");
    public static final Texture GS3 = GuiRepository.loadGuiTexture("helpPages/getStart3.png");
    public static final Texture GS4 = GuiRepository.loadGuiTexture("helpPages/getStart4.png");
    public static final Texture GS5 = GuiRepository.loadGuiTexture("helpPages/getStart5.png");
    public static final Texture WB1 = GuiRepository.loadGuiTexture("helpPages/wildBasic1.png");
    public static final Texture WB2 = GuiRepository.loadGuiTexture("helpPages/wildBasic2.png");
    public static final Texture WB3 = GuiRepository.loadGuiTexture("helpPages/wildBasic3.png");
    public static final Texture WB4 = GuiRepository.loadGuiTexture("helpPages/wildBasic4.png");
    public static final Texture STATUS1 = GuiRepository.loadGuiTexture("helpPages/status1.png");
    public static final Texture STATUS2 = GuiRepository.loadGuiTexture("helpPages/status2.png");
    public static final Texture DP1 = GuiRepository.loadGuiTexture("helpPages/dp1.png");
    public static final Texture B1 = GuiRepository.loadGuiTexture("helpPages/biome1.png");
    public static final Texture B2 = GuiRepository.loadGuiTexture("helpPages/biome2.png");
    public static final Texture D1 = GuiRepository.loadGuiTexture("helpPages/dis1.png");
    public static final Texture D2 = GuiRepository.loadGuiTexture("helpPages/dis2.png");
    public static final Texture GEN1 = GuiRepository.loadGuiTexture("helpPages/gen1.png");
    public static final Texture GEN2 = GuiRepository.loadGuiTexture("helpPages/gen2.png");
    public static final Texture EVO1 = GuiRepository.loadGuiTexture("helpPages/evo1.png");
    public static final Texture EVO2 = GuiRepository.loadGuiTexture("helpPages/evo2.png");
    public static final Texture EVO3 = GuiRepository.loadGuiTexture("helpPages/evo3.png");
    public static final Texture EVO4 = GuiRepository.loadGuiTexture("helpPages/evo4.png");
    public static final Texture FILTER_OFF = GuiRepository.loadPixelPerfectTexture("filterOff.png");
    public static final Texture CLOSE = GuiRepository.loadPixelPerfectTexture("close.png");
    public static final Texture DISPLAY_ON = GuiRepository.loadPixelPerfectTexture("displayOn.png");
    public static final Texture DISPLAY_OFF = GuiRepository.loadPixelPerfectTexture("displayOff.png");
    public static final Texture ERASER = GuiRepository.loadPixelPerfectTexture("eraser20.png");
    public static final Texture EVOLVE_ARROW = GuiRepository.loadPixelPerfectTexture("evolveArrow.png");
    public static final Texture SEARCH = GuiRepository.loadPixelPerfectTexture("search.png");
    public static final Texture DAY_ICON = GuiRepository.loadPixelPerfectTexture("dayNight.png");
    public static final Texture DAY_ICON_PAUSE = GuiRepository.loadPixelPerfectTexture("dayNightPause2.png");
    public static final Texture HUNGER = GuiRepository.loadGuiTexture("food.png");
    public static final Texture[] LINES = new Texture[]{GuiRepository.loadGuiTexture("line0b.png"), GuiRepository.loadGuiTexture("line1.png"), GuiRepository.loadGuiTexture("line2.png"), GuiRepository.loadGuiTexture("line3.png"), GuiRepository.loadGuiTexture("line4.png"), GuiRepository.loadGuiTexture("line5.png"), GuiRepository.loadGuiTexture("line6.png"), GuiRepository.loadGuiTexture("line7.png")};

    private static Texture loadGuiTexture(String name) {
        return Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, name)).noFiltering().clampEdges().create();
    }

    private static Texture loadPixelPerfectTexture(String name) {
        if (DisplayManager.hasIntegerScaleGuis()) {
            return Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, name)).noFiltering().nearestFiltering().clampEdges().create();
        }
        return GuiRepository.loadGuiTexture(name);
    }
}

