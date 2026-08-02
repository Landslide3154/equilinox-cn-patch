/*
 * Decompiled with CFR 0.152.
 */
package toolTips;

import basics.DisplayManager;
import entityInfoGui.EntityInfoGui;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import userInterfaces.GuiPanel;
import visualFxDrivers.SlideDriver;

public class TitledPanelGui
extends GuiPanel {
    private static final int BORDER_PIXELS = 1;
    private static final int LEFT_PAD_PIXELS = 11;
    private static final float TITLE_Y_PIXELS = 3.0f;
    private static final int RIGHT_PAD_PIXELS = 8;
    private static final int TITLE_BAR_HEIGHT_PIXELS = 26;
    private static final float TOP_PAD_PIXELS = 5.0f;
    private static final float TEXT_Y_PIXELS = 31.0f;
    private static final float BOTTOM_PAD_PIXELS = 9.0f;
    private static final float TITLE_BAR_HEIGHT = 26.0f / (float)DisplayManager.getUiHeight();
    private String title;
    private GuiTexture titleBar = new GuiTexture(GuiRepository.BLOCK);
    private float titleY;
    private float leftPadding;
    private float rightPadding;
    private float topPadding;
    private float bottomPadding;
    private Text titleText;
    private GuiComponent contentPanel;

    public TitledPanelGui(String title, Colour backgroundColour, Colour borderColour) {
        super(GuiRepository.BLOCK, backgroundColour, 1, borderColour);
        this.titleBar.setOverrideColour(borderColour);
        this.title = title;
    }

    public void setContent(GuiComponent content) {
        this.contentPanel = content;
    }

    @Override
    protected void init() {
        super.init();
        this.calculatePaddings();
        this.addTitle();
        this.addContentPanel();
    }

    @Override
    public void fadeOut(float time) {
        this.titleBar.setAlphaDriver(new SlideDriver(this.titleBar.getAlpha(), 0.0f, time));
        this.titleText.setAlphaDriver(new SlideDriver(1.0f, 0.0f, time));
        super.fadeOut(time);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.titleBar.update();
    }

    protected void resize(float contentHeight) {
        float pixelHeight = 40.0f;
        float heightWithoutContent = pixelHeight / (float)DisplayManager.getUiHeight();
        super.setRelativeScale(super.getRelativeScaleX(), heightWithoutContent + contentHeight);
        this.calculatePaddings();
        this.titleText.setRelativeY(this.titleY);
        this.contentPanel.setRelativeY(this.topPadding);
        this.contentPanel.setRelativeScaleY(1.0f - (this.topPadding + this.bottomPadding));
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.titleBar.setPosition(position.x, position.y, scale.x, TITLE_BAR_HEIGHT);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        super.getGuiTextures(data);
        data.addTexture(this.getLevel(), this.titleBar);
    }

    private void calculatePaddings() {
        float pixelWidth = this.getScale().x * (float)DisplayManager.getUiWidth();
        float pixelHeight = this.getScale().y * (float)DisplayManager.getUiHeight();
        this.leftPadding = 11.0f / pixelWidth;
        this.rightPadding = 8.0f / pixelWidth;
        this.titleY = 3.0f / pixelHeight;
        this.topPadding = 31.0f / pixelHeight;
        this.bottomPadding = 9.0f / pixelHeight;
    }

    private void addTitle() {
        this.titleText = Text.newText(this.title).setFontSize(EntityInfoGui.FONT_SIZE).create();
        this.titleText.setColour(ColourPalette.WHITE);
        super.addText(this.titleText, this.leftPadding, this.titleY, 1.0f - (this.leftPadding + this.rightPadding));
    }

    private void addContentPanel() {
        super.addComponent(this.contentPanel, this.leftPadding, this.topPadding, 1.0f - (this.leftPadding + this.rightPadding), 1.0f - (this.topPadding + this.bottomPadding));
    }
}

