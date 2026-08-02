/*
 * Decompiled with CFR 0.152.
 */
package geneticModificationUi;

import audio.SoundMaestro;
import componentArchitecture.ComponentType;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.EntityPopUpPanel;
import fontRendering.Text;
import gameManaging.GameManager;
import geneticModificationUi.GeneticsPanelUi;
import growth.GrowthComponent;
import instances.Entity;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import mainGuis.UiSettings;
import materials.MaterialComponent;
import session.GameMode;
import toolbox.Colour;
import toolbox.Maths;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiImage;
import userInterfaces.TextButtonUi;
import visualFxDrivers.ConstantDriver;
import world.World;

public abstract class ModifierUI
extends EntityPopUpPanel {
    private static final String BUY = GameText.getText(938);
    private static final String CONFIRM = GameText.getText(1147);
    private static final String FREE = GameText.getText(1146);
    private static final String CANCEL = GameText.getText(937);
    private static final String COST = GameText.getText(936);
    private static final int PANEL_WIDTH = 250;
    public static final int PANEL_HEIGHT = 165;
    protected static final float TITLE_Y = 7.0f;
    private static final float LINE_HEIGHT = 1.0f;
    private static final float LINE_WIDTH = 0.9f;
    protected static final float LINE_Y_PX = 37.0f;
    protected static final float GAP_AFTER_TITLE = 21.0f;
    protected static final float BUTTON_HEIGHT = 22.0f;
    protected static final float BOTTOM_PAD = 15.0f;
    protected static final float BOX_HEIGHT = 20.0f;
    protected static final float BOX_GAP = 5.0f;
    protected static final float BOX_X = 0.4f;
    protected static final float SIDE_PAD = 0.1f;
    protected static final float BOX_WIDTH = 0.5f;
    protected static final float SLIDER_WIDTH = 0.8f;
    protected static final float SLIDER_HEIGHT = 15.0f;
    private final GeneticsPanelUi mainPanel;
    private final int pixelHeight;
    private TextButtonUi buyButton;
    private Text costText;
    private GuiImage costBox;
    private int currentPrice;
    private boolean top = false;
    private float extraYHeight;

    public ModifierUI(GeneticsPanelUi mainPanel, int pixelHeight) {
        super(ColourPalette.DARK_GREY, 0.75f);
        super.setBlurry();
        this.mainPanel = mainPanel;
        this.pixelHeight = pixelHeight;
    }

    @Override
    protected void init() {
        super.init();
        this.addCost();
        this.addButtons();
        this.addLine();
    }

    protected abstract void confirm();

    @Override
    protected void updateSelf() {
        super.updateSelf();
        if (GameManager.getGameMode() != GameMode.NORMAL || this.isAffordable()) {
            this.costBox.getTexture().setOverrideColour(ColourPalette.LIGHT_GREY);
            this.costText.setColour(ColourPalette.WHITE);
            this.buyButton.block(this.currentPrice == 0);
        } else {
            this.costBox.getTexture().setOverrideColour(ColourPalette.DARK_GREY);
            this.costText.setColour(ColourPalette.BRIGHT_RED);
            this.buyButton.block(true);
        }
    }

    private void addLine() {
        float yPos = super.pixelsToRelativeY(37.0f);
        GuiImage line = new GuiImage(GuiRepository.BLOCK);
        line.getTexture().setOverrideColour(ColourPalette.LIGHT_GREY);
        super.addComponent(line, 0.050000012f, yPos, 0.9f, super.pixelsToRelativeY(1.0f));
    }

    protected void updatePrice(int price) {
        this.currentPrice = price;
        if (GameManager.getGameMode() == GameMode.NORMAL) {
            this.costText.setText(ModifierUI.getCostString(price));
        }
    }

    private void addButtons() {
        String string = GameManager.isNormalMode() ? BUY : CONFIRM;
        this.buyButton = new TextButtonUi(string, ColourPalette.GREEN, UiSettings.NORM_FONT, ColourPalette.WHITE, 0.0f);
        float height = super.pixelsToRelativeY(22.0f);
        super.addComponent(this.buyButton, 0.0f, 1.0f - height, 0.5f, height);
        this.buyButton.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick() && (ModifierUI.this.isAffordable() || !GameManager.isNormalMode())) {
                    GameManager.getSession().getStats().increaseDp(-ModifierUI.this.currentPrice);
                    ModifierUI.this.confirm();
                    ModifierUI.this.buildModeUpdate();
                    SoundMaestro.playSystemSound(GuiSounds.CASH);
                    ModifierUI.this.mainPanel.turnOffButtons();
                }
            }
        });
        TextButtonUi cancelButton = new TextButtonUi(CANCEL, ColourPalette.FLAT_RED, UiSettings.NORM_FONT, ColourPalette.WHITE, 0.0f);
        super.addComponent(cancelButton, 0.5f, 1.0f - height, 0.5f, height);
        cancelButton.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    ModifierUI.this.mainPanel.turnOffButtons();
                }
            }
        });
    }

    private void buildModeUpdate() {
        if (GameManager.getGameMode() == GameMode.BUILD) {
            GrowthComponent growth;
            Entity entity = this.mainPanel.getEntity();
            MaterialComponent matComp = (MaterialComponent)entity.getComponent(ComponentType.MATERIAL);
            if (matComp != null) {
                matComp.forceColourUpdate();
            }
            if ((growth = (GrowthComponent)entity.getComponent(ComponentType.GROWTH)) != null) {
                growth.forceUpdate();
            }
            if (entity.isStatic()) {
                int newBatchId = World.calculateBatchId(entity);
                GameManager.getSession().getSceneData().updateStaticEntity(entity, newBatchId);
            }
        }
    }

    private void addCost() {
        float yPos = 1.0f - super.pixelsToRelativeY(57.0f);
        this.addLabelText(String.valueOf(COST) + ":", yPos);
        String costString = GameManager.getGameMode() == GameMode.NORMAL ? ModifierUI.getCostString(0L) : FREE;
        this.costText = this.addValueText(costString, ColourPalette.BEIGE, yPos);
        this.costBox = this.addBox(yPos);
    }

    protected GuiImage addBox(float yPos) {
        GuiImage box = new GuiImage(GuiRepository.BLOCK);
        box.getTexture().setAlphaDriver(new ConstantDriver(0.2f));
        box.getTexture().setOverrideColour(ColourPalette.LIGHT_GREY);
        super.addComponent(box, 0.4f, yPos, 0.5f, super.pixelsToRelativeY(20.0f));
        return box;
    }

    protected Text addValueText(String name, Colour colour, float yPos) {
        Text text = Text.newText(name).center().setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(colour);
        super.addText(text, 0.4f, yPos, 0.5f);
        return text;
    }

    protected void addLabelText(String name, float yPos) {
        Text text = Text.newText(name).setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.1f, yPos, 1.0f);
    }

    private static String getCostString(long cost) {
        return String.valueOf(Maths.formatNumber(cost)) + " dp";
    }

    private boolean isAffordable() {
        return this.currentPrice <= GameManager.getSession().getStats().getDpCount();
    }

    @Override
    public void addToParentPanel(EntityInfoGui parentPanel) {
        float pad = 5.0f / parentPanel.getPixelWidth();
        float width = 250.0f / parentPanel.getPixelWidth();
        float height = (float)this.pixelHeight / parentPanel.getPixelHeight();
        this.extraYHeight = height - 1.0f;
        parentPanel.addComponent(this, 1.0f + pad, 0.0f, width, height);
    }

    @Override
    public float getMaxY() {
        return this.extraYHeight;
    }

    @Override
    public float getMinY() {
        return 0.0f;
    }
}

