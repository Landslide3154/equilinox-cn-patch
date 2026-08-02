/*
 * Decompiled with CFR 0.152.
 */
package helpUi;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import helpUi.ContentSection;
import helpUi.HelpDisplayUi;
import helpUi.HelpPanelContent;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import userInterfaces.GuiImage;

public class TutorialPageUi
extends GuiComponent {
    private static final float X_PAD = 0.07f;
    private static final int IMAGE_Y_PAD = 9;
    private static final int SECTION_Y_PAD = 25;
    private static final int TOP_PAD = 5;
    private static final float X_WIDTH = 0.86f;
    private static final float TITLE_HEIGHT_PIXELS = 60.0f;
    private static final float LINE_Y_PIXELS = 45.0f;
    private static final float HEADING_HEIGHT_PIXELS = 20.0f;
    private static final float IMAGE_ASPECT = 1.7777778f;
    private static final float BOTTOM_PAD = 8.0f;
    private final HelpPanelContent content;
    private float imageHeight;
    private float imagePadHeight;
    private float yPos = 0.0f;
    private HelpDisplayUi display;

    public TutorialPageUi(HelpPanelContent content, HelpDisplayUi display) {
        this.content = content;
        this.display = display;
    }

    @Override
    protected void init() {
        super.init();
        this.calcImageHeight();
        this.yPos = 5.0f / super.getPixelHeight();
        this.yPos = this.addTitle(this.yPos);
        this.addLine();
        this.yPos = this.addIntro(this.yPos);
        this.yPos += 25.0f / super.getPixelHeight();
        int i = 0;
        while (i < this.content.sections.length) {
            this.yPos = this.addSection(i + 1, this.content.sections[i], this.yPos);
            this.yPos += 25.0f / super.getPixelHeight();
            ++i;
        }
        this.yPos += 8.0f / super.getPixelHeight();
        this.display.resize(this.yPos);
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

    private void addLine() {
        GuiImage image = new GuiImage(GuiRepository.BLOCK);
        image.getTexture().setOverrideColour(ColourPalette.LIGHT_GREY);
        float yPos = 45.0f / super.getPixelHeight();
        super.addComponent(image, 0.06999999f, yPos, 0.86f, super.pixelsToRelativeY(1.0f));
    }

    private void calcImageHeight() {
        float widthPixels = super.getPixelWidth() * 0.86f;
        float heightPixels = widthPixels / 1.7777778f;
        this.imageHeight = heightPixels / super.getPixelHeight();
        this.imagePadHeight = 9.0f / super.getPixelHeight();
    }

    private float addTitle(float yPos) {
        Text titleText = Text.newText(this.content.title).center().setFontSize(UiSettings.V_BIG_FONT).create();
        titleText.setColour(ColourPalette.WHITE);
        super.addText(titleText, 0.0f, yPos, 1.0f);
        return yPos + 60.0f / super.getPixelHeight();
    }

    private float addIntro(float yPos) {
        Text introText = Text.newText(this.content.intro).justify().setFontSize(UiSettings.NORM_FONT).create();
        introText.setColour(ColourPalette.WHITE);
        super.addText(introText, 0.07f, yPos, 0.86f);
        return yPos + introText.getHeight() / super.getScale().y;
    }

    private float addSection(int index, ContentSection section, float yPos) {
        this.addSectionHeader(index, section.title, yPos);
        yPos += 20.0f / super.getPixelHeight();
        yPos += this.imagePadHeight;
        if (section.image != null) {
            this.addSectionImage(section.image, yPos);
            yPos += this.imageHeight + this.imagePadHeight;
        }
        float size = this.addSectionDescription(section.description, yPos);
        return yPos += size / super.getScale().y;
    }

    private void addSectionHeader(int index, String header, float yPos) {
        Text titleText = Text.newText(String.valueOf(index) + ") " + header).setFontSize(UiSettings.LARGE_FONT).create();
        titleText.setColour(ColourPalette.WHITE);
        super.addText(titleText, 0.07f, yPos, 1.0f);
    }

    private void addSectionImage(Texture texture, float yPos) {
        GuiImage image = new GuiImage(texture);
        super.addComponent(image, 0.07f, yPos, 0.86f, this.imageHeight);
    }

    private float addSectionDescription(String desc, float yPos) {
        Text description = Text.newText(desc).justify().setFontSize(UiSettings.NORM_FONT).create();
        description.setColour(ColourPalette.WHITE);
        super.addText(description, 0.07f, yPos, 0.86f);
        return description.getHeight();
    }
}

