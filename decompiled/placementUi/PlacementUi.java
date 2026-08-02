/*
 * Decompiled with CFR 0.152.
 */
package placementUi;

import basics.DisplayManager;
import fontRendering.Text;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import guis.GuiTexture;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import placementUi.IconDisplay;
import session.GameMode;
import textures.Texture;
import toolbox.Maths;
import toolbox.MyMouse;
import userInterfaces.GuiImage;
import visualFxDrivers.BounceDriver;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.ValueDriver;

public class PlacementUi
extends GuiComponent {
    public static final int OFFSET = 10;
    public static final String FREE = GameText.getText(1146);
    private static final float OFFSET_X = 10.0f / (float)DisplayManager.getUiWidth();
    private static final float OFFSET_Y = 10.0f / (float)DisplayManager.getUiHeight();
    public static final int PIXEL_WIDTH = 60;
    public static final int TOTAL_WIDTH = 80;
    private static final float WIDTH = 60.0f / (float)DisplayManager.getUiWidth();
    private static final float HEIGHT = 80.0f / (float)DisplayManager.getUiHeight();
    private static final Texture DNA = GuiRepository.DNA_ICON;
    private static final float PULSE_TIME = 0.25f;
    private static final float PULSE_SIZE = 0.7f;
    private static final float PRICE_Y = -0.01f;
    private final int price;
    private final Texture icon;
    private GuiTexture priceBackground;
    private Text priceText;
    private IconDisplay iconDisplay;
    private boolean canPlace = false;
    private final boolean isDuplicate;
    private ValueDriver sizeDriver = new ConstantDriver(1.0f);

    public PlacementUi(Texture icon, int price, boolean duplicate) {
        this.price = price;
        this.icon = icon;
        this.isDuplicate = duplicate;
        super.setRenderLevel(2);
        GuiMaster.addComponent(this, 0.0f, 0.0f, WIDTH, HEIGHT);
        this.priceBackground = new GuiTexture(GuiRepository.BLOCK);
        this.priceBackground.setOverrideColour(ColourPalette.LIGHT_GREY);
    }

    public void indicatePlaceable(boolean canPlace) {
        this.canPlace = canPlace;
        this.priceBackground.setOverrideColour(ColourPalette.LIGHT_GREY);
        if (super.isInitialized()) {
            this.iconDisplay.indicatePlaceable(canPlace);
        }
    }

    public void pulse() {
        this.sizeDriver = new BounceDriver(1.0f, 0.7f, 0.25f);
        this.iconDisplay.pulse(0.25f);
    }

    @Override
    protected void init() {
        super.init();
        this.iconDisplay = new IconDisplay(this.icon, this.canPlace);
        super.addComponentX(this.iconDisplay, 0.0f, 0.0f, 1.0f);
        this.addPrice();
        if (this.isDuplicate) {
            this.addGeneticsIcon();
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        float yHeight = super.getRelativeHeightCoords(1.0f);
        this.priceBackground.setPosition(position.x, position.y + scale.y * yHeight, scale.x, scale.y * (1.0f - yHeight));
    }

    @Override
    protected void updateSelf() {
        MyMouse mouse = MyMouse.getActiveMouse();
        super.setRelativePosition(mouse.getX() + OFFSET_X, mouse.getY() + OFFSET_Y);
        float scale = this.sizeDriver.update(DisplayManager.getDeltaSeconds());
        super.setRelativeScale(WIDTH * scale, HEIGHT * scale);
        this.updatePriceColour();
    }

    private void updatePriceColour() {
        if (GameManager.getGameMode() != GameMode.NORMAL || GameManager.getSession().getStats().getDpCount() >= this.price) {
            this.priceText.setColour(ColourPalette.WHITE);
        } else {
            this.priceText.setColour(ColourPalette.FLAT_RED);
        }
    }

    @Override
    public boolean isMouseOverFocusIrrelevant() {
        return false;
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.priceBackground);
    }

    private void addGeneticsIcon() {
        GuiImage icon = new GuiImage(DNA);
        int size = 18;
        icon.setPreferredPixelSize(size);
        float iconEnd = super.getRelativeHeightCoords(1.0f);
        super.addPixelComp(icon, 1.0f - super.pixelsToRelativeX(size + 2), iconEnd - super.pixelsToRelativeX(size));
    }

    private void addPrice() {
        String text = GameManager.getGameMode() != GameMode.NORMAL ? FREE : String.valueOf(Maths.formatNumber(this.price)) + " dp";
        this.priceText = Text.newText(text).center().setFontSize(UiSettings.NORM_FONT).create();
        this.priceText.setColour(ColourPalette.WHITE);
        float yPos = super.getRelativeHeightCoords(1.0f);
        super.addText(this.priceText, -1.0f, yPos + -0.01f, 3.0f);
    }
}

