/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import basics.DisplayManager;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.GuiImage;

public class TitlePanelUi
extends GuiComponent {
    private static final float FONT_SIZE = UiSettings.LARGE_FONT;
    private static final int TITLE_HEIGHT_PIXELS = 25;
    private static final int TITLE_GAP_PIXELS = 10;
    private static final int CONTENT_Y_PIXELS = 35;
    private static final int LINE_THICKNESS = 1;
    private static final float LINE_LENGTH = 1.0f;
    private final String name;
    private boolean center;
    private GuiComponent content;

    public TitlePanelUi(String name, GuiComponent content) {
        this.name = name;
        this.content = content;
    }

    public TitlePanelUi(String name, boolean center, GuiComponent content) {
        this.name = name;
        this.content = content;
        this.center = center;
    }

    @Override
    protected void init() {
        super.init();
        this.addTitle();
        this.addLine();
        this.addContent();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addTitle() {
        Text title = null;
        title = this.center ? Text.newText(this.name).center().setFontSize(FONT_SIZE).create() : Text.newText(this.name).setFontSize(FONT_SIZE).create();
        title.setColour(ColourPalette.BEIGE);
        super.addText(title, 0.0f, 0.0f, 1.0f);
    }

    private void addLine() {
        GuiImage image = new GuiImage(GuiRepository.BLOCK);
        image.getTexture().setOverrideColour(ColourPalette.BEIGE);
        float yPos = 25.0f / ((float)DisplayManager.getUiHeight() * super.getScale().y);
        float yScale = 1.0f / ((float)DisplayManager.getUiHeight() * super.getScale().y);
        super.addComponent(image, this.center ? 0.0f : 0.0f, yPos, 1.0f, yScale);
    }

    private void addContent() {
        float yPos = 35.0f / ((float)DisplayManager.getUiHeight() * super.getScale().y);
        super.addComponent(this.content, 0.0f, yPos, 1.0f, 1.0f - yPos);
    }
}

