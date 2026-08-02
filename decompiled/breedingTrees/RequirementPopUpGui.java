/*
 * Decompiled with CFR 0.152.
 */
package breedingTrees;

import basics.DisplayManager;
import blueprints.Blueprint;
import breedingTrees.ReqInfo;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import fontRendering.Text;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import guis.GuiTexture;
import health.LifeCompBlueprint;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.MyMouse;
import userInterfaces.GuiImage;
import visualFxDrivers.SlideDriver;

public class RequirementPopUpGui
extends GuiComponent {
    private static final int PIXEL_WIDTH = 230;
    private static final float HEIGHT = 0.91f;
    private static final int BORDER_PIXELS = 1;
    private static final float BORDER_WIDTH = 0.004347826f;
    private static final float WIDTH = 230.0f / (float)DisplayManager.getUiWidth();
    public static final Colour TEXT_COLOUR = ColourPalette.WHITE;
    public static final Colour REQ_TEXT_COLOUR = ColourPalette.BEIGE;
    private static final String REQ_TEXT = GameText.getText(515);
    private static final String BASE_TEXT = GameText.getText(516);
    private static final String UNDISCOVER_TEXT = GameText.getText(517);
    private static final float TITLE_HEIGHT = 0.2f;
    private static final float REQ_TITLE_HEIGHT = 0.25f;
    private static final float CONTENT_HEIGHT = 0.16f;
    private static final float TOP_PAD = 0.05f;
    private static final float BOTTOM_PAD = 0.07f;
    private static final float BREAK = 0.1f;
    private static final float LINE_LENGTH = 0.8f;
    private static final float CENTER_GAP = 0.1f;
    private static final float HALF_CENTER_GAP = 0.05f;
    private static final float FADE_TIME = 0.1f;
    private GuiTexture center;
    private GuiTexture border;
    private GuiImage line;
    private List<Text> texts = new ArrayList<Text>();
    private List<Text> valueTexts = new ArrayList<Text>();
    private float ySize;
    private boolean undiscovered;
    private boolean hasReqs;

    public RequirementPopUpGui(Blueprint species) {
        this.initTextures();
        MyMouse mouse = MyMouse.getActiveMouse();
        LifeCompBlueprint lifeInfo = (LifeCompBlueprint)species.getComponent(ComponentType.LIFE);
        List<ReqInfo> reqs = lifeInfo.breedInfo.getRequirementGuis();
        this.undiscovered = !GameManager.getSession().getStats().getLockStatus().isUnlocked(species);
        this.determineSizeY(reqs.size(), species);
        this.setRenderLevel(1);
        this.hasReqs = !reqs.isEmpty();
        float realWidth = this.undiscovered ? WIDTH : WIDTH / 1.8f;
        GuiMaster.addComponent(this, mouse.getX() - realWidth, mouse.getY(), realWidth, this.ySize * WIDTH * 0.91f);
        this.addName(species);
        if (this.undiscovered) {
            this.addUndiscoveredNote();
        }
        if (GameManager.BREED_TREES.isBaseSpecies(species)) {
            this.addBaseText();
        } else if (this.undiscovered && !reqs.isEmpty()) {
            this.addRequirementText(species);
            this.addRequirementContents(reqs);
        }
    }

    @Override
    protected void init() {
        super.init();
        float maxTextSize = 0.39999998f;
        for (Text text : this.valueTexts) {
            float relTextSize = text.getActualWidth() / super.getScale().x;
            float overflow = relTextSize - maxTextSize;
            if (!(overflow > 0.0f)) continue;
            super.setRelativeScaleX(super.getRelativeScaleX() * (1.0f + overflow));
        }
        if (this.undiscovered && this.hasReqs) {
            this.addLine();
        }
        this.fadeIn();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        float pixelsHeight = (float)DisplayManager.getUiHeight() * scale.y;
        float borderHeight = 1.0f / pixelsHeight;
        this.border.setPosition(position.x, position.y, scale.x, scale.y);
        this.center.setPosition(position.x + 0.004347826f * scale.x, position.y + borderHeight * scale.y, 0.99130434f * scale.x, (1.0f - borderHeight * 2.0f) * scale.y);
    }

    protected void fade(float start, float end) {
        this.center.setAlphaDriver(new SlideDriver(start, end, 0.1f));
        this.border.setAlphaDriver(new SlideDriver(start, end, 0.15f));
        if (this.line != null) {
            this.line.getTexture().setAlphaDriver(new SlideDriver(start, end, 0.1f));
        }
        for (Text text : this.texts) {
            text.setAlphaDriver(new SlideDriver(start, end, 0.1f));
        }
    }

    protected void fadeIn() {
        this.fade(0.0f, 1.0f);
    }

    @Override
    public boolean isMouseOverFocusIrrelevant() {
        return false;
    }

    @Override
    protected void updateSelf() {
        this.center.update();
        this.border.update();
        MyMouse mouse = MyMouse.getActiveMouse();
        super.setRelativePosition(mouse.getX() - super.getScale().x, mouse.getY());
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.border);
        data.addTexture(this.getLevel(), this.center);
    }

    private void addName(Blueprint species) {
        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)species.getComponent(ComponentType.INFO);
        Text title = Text.newText(info.getName()).setFontSize(UiSettings.LARGE_FONT).center().create();
        boolean isBase = GameManager.BREED_TREES.isBaseSpecies(species);
        title.setColour(isBase ? ColourPalette.BASE_BLUE : (this.undiscovered ? ColourPalette.BRIGHT_RED.duplicate().scale(1.25f) : ColourPalette.WHITE));
        this.texts.add(title);
        super.addText(title, 0.0f, 0.05f / this.ySize, 1.0f);
    }

    private void addRequirementText(Blueprint species) {
        Text text = Text.newText(REQ_TEXT).setFontSize(UiSettings.LARGE_FONT).center().create();
        text.setColour(ColourPalette.WHITE);
        float yPos = 0.51f;
        this.texts.add(text);
        super.addText(text, 0.0f, yPos / this.ySize, 1.0f);
    }

    private void addLine() {
        this.line = new GuiImage(GuiRepository.BLOCK);
        this.line.getTexture().setOverrideColour(ColourPalette.LIGHT_GREY);
        float yPos = 0.46f;
        super.addComponent(this.line, 0.099999994f, yPos / this.ySize, 0.8f, super.pixelsToRelativeY(1.0f));
    }

    private void addBaseText() {
        Text text = Text.newText(BASE_TEXT).setFontSize(UiSettings.NORM_FONT).center().create();
        text.setColour(ColourPalette.WHITE);
        float yPos = this.undiscovered ? 0.41000003f : 0.25f;
        super.addText(text, 0.0f, yPos / this.ySize, 1.0f);
        this.texts.add(text);
    }

    private void determineSizeY(int reqCount, Blueprint species) {
        this.ySize = 0.32f;
        if (GameManager.BREED_TREES.isBaseSpecies(species)) {
            this.ySize += 0.16f;
        }
        if (this.undiscovered) {
            this.ySize += 0.16f;
            if (reqCount > 0) {
                this.ySize += 0.1f;
                this.ySize += 0.25f;
                this.ySize += (float)reqCount * 0.16f;
            }
        }
    }

    private void addRequirementContents(List<ReqInfo> components) {
        float yPos = 0.76f;
        for (ReqInfo info : components) {
            this.addRequirement(info, yPos / this.ySize);
            yPos += 0.16f;
        }
    }

    private void addUndiscoveredNote() {
        Text text = Text.newText(UNDISCOVER_TEXT).setFontSize(UiSettings.NORM_FONT).center().create();
        text.setColour(ColourPalette.BRIGHT_RED);
        super.addText(text, 0.0f, 0.25f / this.ySize, 1.0f);
        this.texts.add(text);
    }

    private void initTextures() {
        this.border = new GuiTexture(GuiRepository.BLOCK);
        this.border.setOverrideColour(ColourPalette.BRIGHT_GREY);
        this.center = new GuiTexture(GuiRepository.BLOCK);
        this.center.setOverrideColour(ColourPalette.MIDDLE_GREY);
    }

    private void addRequirement(ReqInfo info, float yPos) {
        Text nameText = Text.newText(info.name).rightAlign().setFontSize(UiSettings.NORM_FONT).create();
        nameText.setColour(ColourPalette.BRIGHT_GREY);
        super.addText(nameText, 0.0f, yPos, 0.45f);
        this.texts.add(nameText);
        Text dash = Text.newText("-").center().setFontSize(UiSettings.NORM_FONT).create();
        dash.setColour(ColourPalette.WHITE);
        super.addText(dash, 0.0f, yPos, 1.0f);
        this.texts.add(dash);
        Text valueText = Text.newText(info.value).setFontSize(UiSettings.NORM_FONT).create();
        valueText.setColour(info.valueColour);
        super.addText(valueText, 0.55f, yPos, 1.0f);
        this.texts.add(valueText);
        this.valueTexts.add(valueText);
    }
}

