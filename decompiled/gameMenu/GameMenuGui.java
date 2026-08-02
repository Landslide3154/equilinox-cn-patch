/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import basics.DisplayManager;
import fontRendering.Text;
import gameMenu.GameMenuBackground;
import gameMenu.MainMenuGui;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import toolbox.Colour;
import userInterfaces.GuiImage;
import userInterfaces.ScalingImageUi;
import utils.MyFile;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SlideDriver;
import visualFxDrivers.ValueDriver;

public class GameMenuGui
extends GuiComponent {
    public static final Colour TEXT_COLOUR = new Colour(0.15f, 0.15f, 0.15f);
    public static final Vector2f BACK_BUTTON_POS = new Vector2f(0.023f, 0.83f);
    public static final float BACK_BUTTON_SCALE = 0.12f;
    public static final Texture BACK_ICON = Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, "back.png")).noFiltering().create();
    public static final Texture HEADER = Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, "equilinoxLogo3.png")).noFiltering().create();
    public static final Texture HEADER2 = Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, "tester.png")).noFiltering().create();
    private static final float MAIN_MENU_Y_POS = 0.35f;
    private static final float MAIN_MENU_Y_SIZE = 0.65f;
    private static final float SLIDE_TIME = 0.4f;
    public static final float ALPHA = 0.65f;
    public static final float HEADER_Y_SIZE = 0.4f;
    private ValueDriver secondaryDriver = new ConstantDriver(0.0f);
    private ValueDriver mainDriver = new ConstantDriver(0.0f);
    private boolean displayed = true;
    private boolean closeSecondary = false;
    private MainMenuGui mainMenu;
    private GuiComponent secondaryScreen;
    private GuiComponent tertiaryScreen;
    private GuiImage header;

    public GameMenuGui(GameMenuBackground superMenu) {
        this.mainMenu = new MainMenuGui(superMenu, this);
        this.addComponent(this.mainMenu, 0.0f, 0.35f, 1.0f, 0.65f);
        this.header = this.addHeader(HEADER);
        this.addVersion();
    }

    @Override
    public void show(boolean visible) {
        super.show(visible);
        if (!visible) {
            this.removeSecondaryScreen();
            this.secondaryDriver = new ConstantDriver(0.0f);
        }
    }

    public boolean isDisplayed() {
        return this.displayed;
    }

    public void setNewSecondaryScreen(GuiComponent secondScreen) {
        this.removeSecondaryScreen();
        this.secondaryScreen = secondScreen;
        this.addComponent(secondScreen, this.mainMenu.getRelativeX() + 1.0f, 0.35f, 1.0f, 0.65f);
        this.secondaryDriver = new SlideDriver(this.mainMenu.getRelativeX(), -1.0f, 0.4f);
    }

    public void setNewTertiaryScreen(GuiComponent thirdScreen) {
        this.removeTertiaryScreen();
        this.tertiaryScreen = thirdScreen;
        this.addComponent(this.tertiaryScreen, this.mainMenu.getRelativeX() + 2.0f, 0.35f, 1.0f, 0.65f);
        this.secondaryDriver = new SlideDriver(this.mainMenu.getRelativeX(), -2.0f, 0.4f);
    }

    public void closeSecondaryScreen() {
        this.secondaryDriver = new SlideDriver(this.mainMenu.getRelativeX(), 0.0f, 0.4f);
        this.closeSecondary = true;
        this.mainMenu.notifyBackOnScreen();
    }

    public void display(boolean display) {
        if (display) {
            this.show(true);
            this.mainDriver = new SlideDriver(this.getRelativeX(), 0.0f, 0.4f);
            this.displayed = true;
            this.mainMenu.notifyOpening();
            this.header.show(true);
        } else {
            this.mainDriver = new SlideDriver(this.getRelativeX(), -1.0f, 0.4f);
            this.displayed = false;
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        float mainValue = this.mainDriver.update(DisplayManager.getDeltaSeconds());
        float value = this.secondaryDriver.update(DisplayManager.getDeltaSeconds());
        this.mainMenu.setRelativeX(value);
        if (this.secondaryScreen != null) {
            this.secondaryScreen.setRelativeX(value + 1.0f);
        }
        if (this.tertiaryScreen != null) {
            this.tertiaryScreen.setRelativeX(value + 2.0f);
        }
        super.setRelativeX(mainValue);
        if (!this.displayed && mainValue <= -1.0f) {
            this.show(false);
            this.removeTertiaryScreen();
        }
        if (this.closeSecondary && this.secondaryScreen.getRelativeX() >= 1.0f) {
            this.removeSecondaryScreen();
            this.closeSecondary = false;
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addVersion() {
        Text text = Text.newText("Version 1.7.2").setFontSize(UiSettings.LARGE_FONT).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.7f, 0.3f, 1.0f);
    }

    private void removeSecondaryScreen() {
        if (this.secondaryScreen != null) {
            this.removeComponent(this.secondaryScreen);
            this.secondaryScreen = null;
        }
    }

    private void removeTertiaryScreen() {
        if (this.tertiaryScreen != null) {
            this.removeComponent(this.tertiaryScreen);
            this.tertiaryScreen = null;
        }
    }

    private GuiImage addHeader(Texture logoTexture) {
        ScalingImageUi header = new ScalingImageUi(logoTexture);
        header.setPreferredAspectRatio(4.0f);
        super.addCenteredComponentX(header, 0.5f, 0.0f, 0.4f);
        return header;
    }
}

