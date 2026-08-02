/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import basics.DisplayManager;
import gameManaging.GameManager;
import gameManaging.GameState;
import gameMenu.GameMenuGui;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import guis.GuiTexture;
import interpolation.SmoothFloat;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import toolbox.Colour;
import userInterfaces.Listener;
import utils.MyFile;
import visualFxDrivers.SlideDriver;

public class GameMenuBackground
extends GuiComponent {
    private static final float MAX_CHANGE = 0.02f;
    private static final float IMG_SCALE = 1.04f;
    private static final float START = -0.01999998f;
    public static final Colour NORM_COLOUR = ColourPalette.DARK_GREY;
    private static final Texture BACKGROUND = Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, "background.png")).noFiltering().create();
    public static final float SLIDE_TIME = 0.4f;
    private static final float FADE_OUT_TIME = 2.0f;
    private GuiTexture backPicture;
    private GuiTexture blackout;
    private GameMenuGui menu;
    private boolean displayed = true;
    private boolean quit = false;
    private boolean isLoading = false;
    private SmoothFloat backgroundX = new SmoothFloat(-0.01999998f, 1.5f);
    private SmoothFloat backgroundY = new SmoothFloat(-0.01999998f, 1.5f);
    private List<Listener> listeners = new ArrayList<Listener>();

    public GameMenuBackground() {
        this.menu = new GameMenuGui(this);
        this.blackout = new GuiTexture(GuiRepository.BLACKOUT);
        this.backPicture = new GuiTexture(BACKGROUND);
        super.addComponent(this.menu, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    public boolean isLoading() {
        return this.isLoading;
    }

    public void setLoading() {
        this.isLoading = true;
    }

    public void display(boolean display) {
        this.menu.display(display);
        this.displayed = display;
        this.notifyListeners();
        if (display) {
            GameManager.gameState.setState(GameState.GAME_MENU);
            if (!this.isShown()) {
                this.show(true);
            }
            this.backPicture.setAlphaDriver(new SlideDriver(this.backPicture.getAlpha(), 1.0f, 0.4f));
        } else {
            this.backPicture.setAlphaDriver(new SlideDriver(this.backPicture.getAlpha(), 0.0f, 0.4f));
        }
    }

    public void addMenuListener(Listener menuListener) {
        this.listeners.add(menuListener);
    }

    public boolean isDisplayed() {
        return this.displayed;
    }

    protected void quit() {
        this.quit = true;
        this.backPicture.setAlphaDriver(new SlideDriver(this.backPicture.getAlpha(), 0.0f, 2.0f));
    }

    @Override
    protected void updateSelf() {
        this.backPicture.update();
        if (!this.displayed && !this.menu.isShown()) {
            GameManager.gameState.endState(GameState.GAME_MENU);
            this.show(false);
            this.isLoading = false;
        }
        this.checkQuit();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        float desiredAspect;
        float displayAspect = DisplayManager.getAspectRatio();
        if (displayAspect > (desiredAspect = 1.7777778f)) {
            float scaleY = displayAspect / desiredAspect;
            float posY = -(scaleY - 1.0f) / 2.0f;
            this.backPicture.setPosition(position.x, posY, scale.x, scaleY);
        } else {
            float scaleX = desiredAspect / displayAspect;
            float posX = -(scaleX - 1.0f) / 2.0f;
            this.backPicture.setPosition(posX, position.y, scaleX, scale.y);
        }
        this.blackout.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        if (this.quit) {
            data.addTexture(this.getLevel(), this.blackout);
        }
        data.addTexture(this.getLevel(), this.backPicture);
    }

    private void checkQuit() {
        if (this.quit && this.backPicture.getAlpha() == 0.0f) {
            DisplayManager.requestClosure();
        }
    }

    private void notifyListeners() {
        for (Listener listener : this.listeners) {
            listener.eventOccurred(this.displayed);
        }
    }

    public static Colour getStandardColour() {
        return NORM_COLOUR;
    }

    protected static Colour getBlockedColour() {
        return ColourPalette.MIDDLE_GREY;
    }
}

