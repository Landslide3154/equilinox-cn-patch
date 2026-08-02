/*
 * Decompiled with CFR 0.152.
 */
package text3D;

import basics.DisplayManager;
import blueprints.Blueprint;
import bottomBar.BottomBarUi;
import breedingTraits.FloatTrait;
import componentArchitecture.ComponentType;
import entityBundle.EntityBundle;
import fontRendering.Text;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import instances.Entity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import languages.GameText;
import main.Camera;
import mainGuis.ColourPalette;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import text3D.Text3D;
import toolbox.Maths;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiImage;
import userInterfaces.Tab2ButtonUi;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SlideDriver;
import visualFxDrivers.ValueDriver;

public class TraitDisplayOptionUi
extends GuiComponent {
    public static final int WIDTH_PIXELS = 200;
    public static final float WIDTH = 200.0f / (float)DisplayManager.getUiWidth();
    private static final int RANGE = 5;
    private static final float MAX_DIS = 5.0f;
    private static final float MAX_DIS_SQR = 25.0f;
    private static final float TRANS_DIS_SQR = 17.5f;
    private static final float CAM_DIS_MAX = 30.0f;
    private static final float CAM_DIS_LOW = 25.5f;
    private static final String DISPLAY_TEXT = GameText.getText(1061);
    private static final float TEXT_GAP = 0.04f;
    private final Blueprint species;
    private final ComponentType component;
    private final int traitIndex;
    private List<Text3D> activeTexts = new ArrayList<Text3D>();
    private Vector3f terrainPoint;
    private GuiTexture background;
    private boolean stopped = false;
    private ValueDriver yDriver;

    public TraitDisplayOptionUi(Blueprint species, ComponentType component, int traitIndex) {
        this.species = species;
        this.component = component;
        this.traitIndex = traitIndex;
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setOverrideColour(ColourPalette.DARK_GREY);
        this.background.setBlurry(true);
        this.background.setAlphaDriver(new ConstantDriver(0.75f));
        this.yDriver = new SlideDriver(1.0f, 1.0f - BottomBarUi.HEIGHT, 0.2f);
    }

    public Blueprint getSpecies() {
        return this.species;
    }

    public ComponentType getTraitType() {
        return this.component;
    }

    public int getTraitIndex() {
        return this.traitIndex;
    }

    @Override
    protected void init() {
        super.init();
        float squareWidth = super.getRelativeWidthCoords(1.0f);
        this.addSquare(squareWidth);
        this.addButton(squareWidth);
        this.addText();
    }

    public void stopShowingTraits() {
        this.stopped = true;
        for (Text3D text : this.activeTexts) {
            text.destroy();
        }
        this.yDriver = new SlideDriver(this.getRelativeY(), 1.0f, 0.2f);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void updateSelf() {
        this.background.update();
        float yPos = this.yDriver.update(DisplayManager.getDeltaSeconds());
        super.setRelativeY(yPos);
        if (this.stopped) {
            if (super.getRelativeY() >= 1.0f) {
                this.remove();
            }
            return;
        }
        EntityBundle nearbyEntities = this.getRelevantNearbyEntities();
        if (nearbyEntities != null) {
            this.loopNearbyEntities(nearbyEntities);
        }
        this.updateCurrentTexts();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
    }

    private void addText() {
        Text text = Text.newText(String.valueOf(DISPLAY_TEXT) + ":").rightAlign().setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, 0.0f, 0.46f);
        Text traitText = Text.newText(this.species.getComponent(this.component).getTraitBlueprint(this.traitIndex).getName()).setFontSize(UiSettings.NORM_FONT).create();
        traitText.setColour(ColourPalette.BEIGE);
        super.addText(traitText, 0.54f, 0.0f, 1.0f);
    }

    private void addSquare(float squareWidth) {
        GuiImage square = new GuiImage(GuiRepository.BLOCK);
        square.getTexture().setOverrideColour(ColourPalette.MIDDLE_GREY);
        super.addComponent(square, 1.0f - squareWidth, 0.0f, squareWidth, 1.0f);
    }

    private void loopNearbyEntities(EntityBundle nearbyEntities) {
        float camDis = Camera.getCamera().getAimDistance();
        for (Entity entity : nearbyEntities) {
            Text3D text = entity.getText3D();
            Vector3f entityPos = entity.getTransform().getPosition();
            float dis = Maths.getComparitableDistance(this.terrainPoint.x, this.terrainPoint.z, entityPos.x, entityPos.z);
            float factor = 1.0f - Maths.smoothStep(17.5f, 25.0f, dis);
            float heightFactor = 1.0f - Maths.quickStep(25.5f, 30.0f, camDis);
            if ((factor *= heightFactor) == 0.0f) continue;
            if (text != null) {
                text.setActive();
            } else {
                text = new Text3D(entity, this.getText(entity));
                this.activeTexts.add(text);
            }
            text.setAlpha(factor);
        }
    }

    private void addButton(float squareWidth) {
        Tab2ButtonUi button = new Tab2ButtonUi(GuiRepository.CLOSE, GuiRepository.CLOSE, ColourPalette.WHITE, false);
        button.setPreferredPixelSize(20);
        button.setMouseOverColour(ColourPalette.LIGHT_GREY);
        super.addPixelComp(button, 1.0f - squareWidth, 0.0f);
        button.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    EquilinoxGuis.hideTraitDisplayOption();
                    GameManager.getEntityPicker().deselect();
                }
            }
        });
    }

    private void updateCurrentTexts() {
        Iterator<Text3D> texts = this.activeTexts.iterator();
        while (texts.hasNext()) {
            Text3D text = texts.next();
            boolean active = text.update();
            if (active) continue;
            text.destroy();
            texts.remove();
        }
    }

    private String getText(Entity entity) {
        FloatTrait trait = (FloatTrait)entity.getComponent(this.component).getTrait(this.traitIndex);
        return String.valueOf(trait.blueprint.getName()) + ": " + trait.getFormattedTrait();
    }

    private EntityBundle getRelevantNearbyEntities() {
        this.terrainPoint = Camera.getCamera().getCameraPicker().getCurrentTerrainPoint();
        if (this.terrainPoint == null) {
            return null;
        }
        return GameManager.getWorld().getListOfSpecies(this.species, 5, this.terrainPoint.x, this.terrainPoint.z);
    }
}

