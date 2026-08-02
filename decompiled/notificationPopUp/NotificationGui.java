/*
 * Decompiled with CFR 0.152.
 */
package notificationPopUp;

import basics.DisplayManager;
import fontRendering.Text;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import guis.GuiTexture;
import interpolation.SmoothFloat;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import notificationPopUp.Notifier;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import toolbox.MyMouse;
import userInterfaces.GuiImage;
import userInterfaces.Listener;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SlideDriver;
import visualFxDrivers.ValueDriver;

public class NotificationGui
extends GuiComponent {
    private static final float SHOW_TIME = 7.0f;
    private static final int WIDTH_PIXELS = 380;
    private static final float WIDTH = 380.0f / (float)DisplayManager.getUiWidth();
    private static final float X_POS = 1.0f - WIDTH;
    private static final float OUT_TIME = 0.3f;
    private static final float TITLE_FONT = UiSettings.TITLE_FONT;
    private static final float TEXT_FONT = UiSettings.NORM_FONT;
    private static final float TEXT_START_X = 0.24f;
    private static final float TITLE_START_Y = 0.12f;
    private static final float TITLE_BIG_Y = 0.01f;
    private static final float TEXT_BIG_Y = 0.37f;
    private static final float TEXT_START_Y = 0.52f;
    private static final float PADDING_RIGHT = 0.01f;
    private static final float IMAGE_PADDING_X = 0.04f;
    private float timeRemaining = 7.0f;
    private int index;
    private SmoothFloat topPosition;
    private ValueDriver xPos = new ConstantDriver(X_POS);
    private boolean alive = true;
    private boolean open = true;
    private GuiTexture background;
    private GuiTexture inner;
    private GuiImage image;
    private final String titleString;
    private final String desc;
    private final Texture icon;
    private Text title;
    private Text description;
    private boolean mousedOver = false;
    private Listener listener;

    protected NotificationGui(int index, SmoothFloat topPosition, Texture icon, String title, String desc) {
        this.index = index;
        this.titleString = title;
        this.desc = desc;
        this.icon = icon;
        this.topPosition = topPosition;
        this.initTextures();
        super.setRenderLevel(1);
        GuiMaster.addComponent(this, X_POS, this.calculatePositionY(), WIDTH, Notifier.GUI_HEIGHT);
    }

    @Override
    protected void init() {
        super.init();
        this.addTitle(this.titleString);
        this.addText(this.desc);
        this.addImage(this.icon);
    }

    protected void setListener(Listener listener) {
        this.listener = listener;
    }

    protected boolean updateNotification(int index) {
        this.index = index;
        if (this.open) {
            this.timeRemaining -= DisplayManager.getDeltaSeconds();
            if (this.timeRemaining <= 0.0f) {
                this.close();
            }
        }
        return this.alive;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        float borderWidth = 1.0f / ((float)DisplayManager.getUiWidth() * scale.x);
        float borderHeight = 1.0f / ((float)DisplayManager.getUiHeight() * scale.y);
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
        this.inner.setPosition(position.x + borderWidth * scale.x, position.y + borderHeight * scale.y, (1.0f - borderWidth * 2.0f) * scale.x, (1.0f - borderHeight * 2.0f) * scale.y);
    }

    @Override
    protected void updateSelf() {
        this.background.update();
        this.inner.update();
        this.checkMouseOver();
        this.checkClick();
        super.setRelativeY(this.calculatePositionY());
        float xPosition = this.xPos.update(DisplayManager.getDeltaSeconds());
        super.setRelativeX(xPosition);
        this.alive = xPosition < 1.0f;
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
        data.addTexture(this.getLevel(), this.inner);
    }

    private void checkMouseOver() {
        if (!this.mousedOver && super.isMouseOver()) {
            this.mousedOver = true;
            this.inner.setOverrideColour(ColourPalette.LIGHT_GREY);
        } else if (this.mousedOver && !super.isMouseOver()) {
            this.mousedOver = false;
            this.inner.setOverrideColour(ColourPalette.DARK_GREY);
        }
    }

    private void checkClick() {
        if (this.mousedOver && MyMouse.getActiveMouse().isLeftClick()) {
            this.close();
            this.inner.setOverrideColour(ColourPalette.MIDDLE_GREY);
            GameManager.getEntityPicker().clear();
            if (this.listener != null) {
                this.listener.eventOccurred(true);
            }
        } else if (this.mousedOver && MyMouse.getActiveMouse().isRightClick()) {
            this.close();
            this.inner.setOverrideColour(ColourPalette.MIDDLE_GREY);
        }
    }

    private float calculatePositionY() {
        return this.topPosition.get() + (float)this.index * (Notifier.GUI_HEIGHT + Notifier.GAP_HEIGHT);
    }

    private void close() {
        this.xPos = new SlideDriver(X_POS, 1.0f, 0.3f);
        this.background.setAlphaDriver(new SlideDriver(1.0f, 0.0f, 0.15f));
        this.inner.setAlphaDriver(new SlideDriver(0.75f, 0.0f, 0.3f));
        this.title.setAlphaDriver(new SlideDriver(1.0f, 0.0f, 0.3f));
        this.description.setAlphaDriver(new SlideDriver(1.0f, 0.0f, 0.3f));
        this.image.getTexture().setAlphaDriver(new SlideDriver(1.0f, 0.0f, 0.3f));
        this.open = false;
    }

    private void initTextures() {
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(ColourPalette.BRIGHT_GREY);
        this.inner = new GuiTexture(GuiRepository.BLOCK);
        this.inner.setOverrideColour(ColourPalette.DARK_GREY);
        this.inner.setAlphaDriver(new ConstantDriver(0.75f));
        this.inner.setBlurry(true);
    }

    private void addTitle(String titleString) {
        this.title = Text.newText(titleString).setFontSize(TITLE_FONT).create();
        this.title.setColour(ColourPalette.BEIGE);
        super.addText(this.title, 0.24f, 0.12f, 0.76f);
    }

    private void addText(String desc) {
        this.description = Text.newText(desc).setFontSize(TEXT_FONT).create();
        this.description.setColour(ColourPalette.WHITE);
        super.addText(this.description, 0.24f, 0.52f, 0.75f);
        int lines = this.description.getNumberOfLines();
        if (lines > 1) {
            this.title.setRelativeY(0.01f);
            this.description.setRelativeY(0.37f);
        }
    }

    private void addImage(Texture icon) {
        this.image = new GuiImage(icon);
        float imageStart = 0.04f;
        float imageEnd = 0.19999999f;
        super.addCenteredComponent(this.image, imageStart + (imageEnd - imageStart) / 2.0f, 0.5f, imageEnd - imageStart);
    }
}

