/*
 * Decompiled with CFR 0.152.
 */
package mainGuis;

import audio.Sound;
import basics.DisplayManager;
import blueprints.Blueprint;
import bottomBar.BottomBarUi;
import componentArchitecture.ComponentType;
import entityInfoGui.TabController;
import extraInfoGui.ExtraInfoGui;
import gameManaging.GameManager;
import gameManaging.GameState;
import gameMenu.GameMenuBackground;
import guis.GuiMaster;
import java.util.ArrayList;
import java.util.List;
import mainGuis.EscListener;
import mainGuis.GuiSounds;
import notificationPopUp.NotificationLog;
import notificationPopUp.Notifier;
import profile3d.Profile3D;
import session.GameMode;
import text3D.TraitDisplayOptionUi;
import textures.Texture;
import toolbar.Toolbar;
import toolbox.MyKeyboard;
import toolbox.MyMouse;
import userInterfaces.Listener;

public class EquilinoxGuis {
    private static GameMenuBackground gameMenu;
    private static Toolbar toolbar;
    public static ExtraInfoGui extraInfoGui;
    private static Notifier notifier;
    private static boolean menuOpen;
    private static List<EscListener> escListeners;
    private static TraitDisplayOptionUi traitOption;
    private static final NotificationLog notificationLog;
    private static BottomBarUi bottomBar;

    static {
        notifier = new Notifier();
        menuOpen = false;
        escListeners = new ArrayList<EscListener>();
        traitOption = null;
        notificationLog = new NotificationLog();
    }

    public static void init() {
        MyMouse.getActiveMouse().initCursor();
        gameMenu = new GameMenuBackground();
        EquilinoxGuis.addToolBar();
        GuiMaster.addComponent(gameMenu, 0.0f, 0.0f, 1.0f, 1.0f);
        extraInfoGui = new ExtraInfoGui();
        Profile3D.initProfile3D();
        EquilinoxGuis.addTimeDisplay();
        GuiSounds.init();
        EquilinoxGuis.addTraitDisplayCloseListener();
    }

    public static void update() {
        notifier.update();
        TabController.update(DisplayManager.getDeltaSeconds());
        if (MyKeyboard.getKeyboard().keyDownEventOccurred(1) && !gameMenu.isLoading()) {
            if (gameMenu.isDisplayed()) {
                gameMenu.display(false);
            } else {
                boolean uiOpen = EquilinoxGuis.notifyEscListeners();
                if (!uiOpen) {
                    gameMenu.display(true);
                }
            }
        }
        menuOpen = gameMenu.isShown();
        EquilinoxGuis.checkGuiStateChange();
        extraInfoGui.update();
    }

    public static void notify(String title, String text, Texture icon, Sound sound) {
        notifier.notify(title, text, icon, sound);
    }

    public static void notify(String title, String text, Texture icon, Sound sound, Listener listener) {
        notifier.notify(title, text, icon, sound, listener);
    }

    public static NotificationLog getNotificationLog() {
        return notificationLog;
    }

    public static boolean isMenuOpen() {
        return menuOpen;
    }

    public static ExtraInfoGui getExtraInfoGui() {
        return extraInfoGui;
    }

    public static Toolbar getToolBar() {
        return toolbar;
    }

    public static void addGameMenuListener(Listener listener) {
        gameMenu.addMenuListener(listener);
    }

    public static void cleanUp() {
        Profile3D.cleanUp();
    }

    public static TraitDisplayOptionUi getTraitDisplayOption() {
        return traitOption;
    }

    public static void showTraitDisplayOption(Blueprint species, ComponentType component, int traitIndex) {
        EquilinoxGuis.hideTraitDisplayOption();
        traitOption = new TraitDisplayOptionUi(species, component, traitIndex);
        float gap = 4.0f / (float)DisplayManager.getUiWidth();
        if (GameManager.getGameMode() == GameMode.NORMAL) {
            GuiMaster.addComponent(traitOption, BottomBarUi.X_POS - (TraitDisplayOptionUi.WIDTH + gap), 1.0f - BottomBarUi.HEIGHT, TraitDisplayOptionUi.WIDTH, BottomBarUi.HEIGHT);
        } else {
            float width = 67.0f / (float)DisplayManager.getUiWidth();
            GuiMaster.addComponent(traitOption, BottomBarUi.X_POS - (TraitDisplayOptionUi.WIDTH + width), 1.0f - BottomBarUi.HEIGHT, TraitDisplayOptionUi.WIDTH, BottomBarUi.HEIGHT);
        }
    }

    public static void hideTraitDisplayOption() {
        if (traitOption != null) {
            traitOption.stopShowingTraits();
            traitOption = null;
        }
    }

    public static boolean notifyEscListeners() {
        boolean uiOpen = false;
        for (EscListener listener : escListeners) {
            uiOpen |= listener.escPressed();
        }
        return uiOpen;
    }

    public static void addEscListener(EscListener listener) {
        escListeners.add(listener);
    }

    private static void addTraitDisplayCloseListener() {
        gameMenu.addMenuListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                if (on) {
                    EquilinoxGuis.hideTraitDisplayOption();
                }
            }
        });
    }

    public static BottomBarUi getBottomBar() {
        return bottomBar;
    }

    private static void addTimeDisplay() {
        bottomBar = new BottomBarUi();
        gameMenu.addMenuListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                if (!on) {
                    bottomBar.display();
                } else {
                    bottomBar.undisplay();
                }
            }
        });
    }

    private static void checkGuiStateChange() {
        if (GuiMaster.isMouseInGui()) {
            GameManager.gameState.suggestState(GameState.IN_GUI);
        } else if (!GuiMaster.isMouseInGui()) {
            GameManager.gameState.endState(GameState.IN_GUI);
        }
    }

    private static void addToolBar() {
        toolbar = new Toolbar();
        gameMenu.addMenuListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                toolbar.display(!on);
            }
        });
        EquilinoxGuis.addEscListener(new EscListener(){

            @Override
            public boolean escPressed() {
                return toolbar.closeGuestPanel();
            }
        });
    }
}

