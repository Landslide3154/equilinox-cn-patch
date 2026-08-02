/*
 * Decompiled with CFR 0.152.
 */
package shops;

import audio.SoundMaestro;
import fontRendering.Text;
import gameManaging.GameManager;
import gridLayout.ItemPageGui;
import guiRendering.GuiRenderData;
import guis.GuiTexture;
import interpolation.Timer;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import session.GameMode;
import shops.ShopItem;
import toolbox.Colour;
import toolbox.Maths;
import userInterfaces.ClickListener;
import userInterfaces.GuiButton;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;
import userInterfaces.GuiImage;
import userInterfaces.Listener;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SlideDriver;

public class ShopItemGui
extends GuiClickable {
    private static final String LOCKED = GameText.getText(91);
    private static final String AVAILABLE = GameText.getText(1143);
    private static final Colour BACKGROUND = ColourPalette.DARKER_GREEN;
    private static final Colour RED_CASH_BAR = ColourPalette.FLAT_RED;
    private static final Colour GREYED_OUT = new Colour(0.8f, 0.8f, 0.8f, false);
    private static final float BACKGROUND_ALPHA = 0.55f;
    private static final float INFO_ICON_X = 0.73f;
    private static final float INFO_ICON_Y = 0.02f;
    private static final float INFO_ICON_SCALE_X = 0.25f;
    private static final float TEXT_Y_BUF = 0.006f;
    private static final float LOCK_ICON_X = 0.05f;
    private static final float LOCK_ICON_Y = 0.05f;
    private static final float LOCK_ICON_SCALE_X = 0.35f;
    private static final float STAR_HEIGHT = 0.1f;
    private static final float STAR_Y_OFFSET = 0.01f;
    private static final float WOBBLE_FACTOR = 1.1f;
    private static final float WOBBLE_TIME = 0.5f;
    private static final float BOUNCE_TIME = 0.35f;
    private float fontSize;
    public static final int MIN_PIXELS_WIDE = 100;
    private static final float BAR_HEIGHT = 0.2f;
    public static final float ASPECT_RATIO = 0.71428573f;
    private static final float ICON_HEIGHT_Y = 0.71428573f;
    private static final float BAR_HEIGHT_Y = 0.14285715f;
    private GuiTexture background;
    private GuiTexture nameBar;
    private GuiTexture cashBar;
    private GuiButton infoButton;
    private boolean affordable;
    private GuiImage icon;
    private Text priceText;
    private ShopItem item;
    private ItemPageGui pageGui;
    private GuiImage lockIcon;
    private boolean lockedDisplay;
    private final Timer timer = Timer.createOneOffTimer(0.3f, false);

    public ShopItemGui(ShopItem item, ItemPageGui pageGui) {
        this.item = item;
        this.pageGui = pageGui;
        this.affordable = this.isAffordable(item);
        this.initGuiTextures();
    }

    @Override
    protected void init() {
        this.lockedDisplay = this.item.isLocked();
        this.calcFontSizes();
        this.addName();
        this.addIcon();
        this.addCost();
        this.addInfoButton();
        this.addLockedIcon();
        this.addTierStars();
        this.addMouseoverListener();
        this.addClickListener();
        if (this.item.isNew()) {
            this.addNewEffect();
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.background.setPosition(position.x, position.y, scale.x, scale.y * 0.71428573f * 1.05f);
        float cashBarHeight = 0.85714287f;
        this.nameBar.setPosition(position.x, position.y + 0.71428573f * scale.y, scale.x, scale.y * 0.28571427f);
        this.cashBar.setPosition(position.x, position.y + cashBarHeight * scale.y, scale.x, scale.y * (1.0f - cashBarHeight));
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.background.update();
        if (!this.item.isLocked()) {
            if (this.lockedDisplay) {
                this.unlockUi();
            }
            if (this.isAffordable(this.item)) {
                this.cashBar.setOverrideColour(ColourPalette.LIGHT_GREEN);
                if (!this.affordable) {
                    super.bounce(0.35f, 1.1f);
                    this.affordable = true;
                }
            } else {
                this.cashBar.setOverrideColour(RED_CASH_BAR);
                this.affordable = false;
            }
        }
        if (this.timer.check()) {
            this.pageGui.registerMouseover(this, this.item.getLockedMouseover());
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
        data.addTexture(this.getLevel(), this.nameBar);
        data.addTexture(this.getLevel(), this.cashBar);
    }

    private void calcFontSizes() {
        this.fontSize = UiSettings.NORM_FONT;
    }

    private void addIcon() {
        this.icon = new GuiImage(this.item.getIcon());
        GuiTexture texture = this.icon.getTexture();
        texture.flip(this.item.flipIcon());
        if (this.item.isLocked()) {
            texture.setOverrideColour(GREYED_OUT);
        }
        super.addComponentX(this.icon, 0.0f, 0.0f, 1.0f);
    }

    private void initGuiTextures() {
        this.background = new GuiTexture(GuiRepository.COOL);
        this.background.setOverrideColour(this.item.isLocked() ? ColourPalette.LOCKED_BACKGROUND : BACKGROUND);
        this.background.setAlphaDriver(new ConstantDriver(0.55f));
        this.nameBar = new GuiTexture(GuiRepository.BLOCK);
        this.nameBar.setOverrideColour(ColourPalette.LIGHT_GREY);
        this.cashBar = new GuiTexture(GuiRepository.BLOCK);
        this.cashBar.setOverrideColour(this.item.isLocked() ? ColourPalette.MIDDLE_GREY : (this.isAffordable(this.item) ? ColourPalette.LIGHT_GREEN : RED_CASH_BAR));
    }

    private void addName() {
        Text text = Text.newText(this.item.getName()).center().setFontSize(this.fontSize).create();
        text.setColour(this.item.isSpecial() ? ColourPalette.SPECIAL : ColourPalette.WHITE);
        super.addText(text, 0.0f, 0.7202857f, 1.0f);
    }

    private void addCost() {
        this.priceText = Text.newText(this.getCostString()).center().setFontSize(this.fontSize).create();
        this.priceText.setColour(ColourPalette.WHITE);
        super.addText(this.priceText, 0.0f, 0.86314285f, 1.0f);
    }

    private void unlockUi() {
        this.priceText.setText(this.getCostString());
        this.background.setOverrideColour(BACKGROUND);
        this.lockIcon.remove();
        this.icon.getTexture().setOverrideColour(null);
        this.lockedDisplay = false;
        this.item.setNotNew();
        super.bounce(0.35f, 1.1f);
        this.addInfoButton();
    }

    private void addTierStars() {
        float width = super.getRelativeWidthCoords(0.1f);
        float halfWidth = width * 0.5f;
        float leftFromCenter = halfWidth * (float)(this.item.getTier() - 1);
        float leftStart = 0.5f - leftFromCenter;
        int i = 0;
        while (i < this.item.getTier()) {
            GuiImage image = new GuiImage(GuiRepository.NEW);
            super.addCenteredComponentX(image, leftStart + (float)i * width, 0.6042857f, 0.1f);
            ++i;
        }
    }

    private void addLockedIcon() {
        if (!this.item.isLocked()) {
            return;
        }
        this.lockIcon = new GuiImage(GuiRepository.LOCK_ICON);
        this.lockIcon.setPreferredPixelSize(36);
        super.addPixelComp(this.lockIcon, 0.05f, super.getRelativeHeightCoords(0.05f));
    }

    private void addInfoButton() {
        if (this.item.isLocked()) {
            return;
        }
        super.setUnclickableRegion(new Vector2f(0.73f, 0.02f), new Vector2f(0.25f, super.getRelativeHeightCoords(0.25f)));
        this.infoButton = new GuiButton(GuiRepository.INFO);
        this.infoButton.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                SoundMaestro.playSystemSound(GuiSounds.SELECT);
                ShopItemGui.this.item.displayInfo();
            }
        });
        final GuiTexture texture = this.infoButton.getGuiTexture();
        texture.setAlphaDriver(new ConstantDriver(0.0f));
        texture.update();
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver()) {
                    texture.setAlphaDriver(new SlideDriver(texture.getAlpha(), 1.0f, 0.3f));
                } else if (event.isMouseOff()) {
                    texture.setAlphaDriver(new SlideDriver(texture.getAlpha(), 0.0f, 0.2f));
                }
            }
        });
        super.addComponentX(this.infoButton, 0.73f, 0.02f, 0.25f);
    }

    private void addClickListener() {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick() && !ShopItemGui.this.item.isLocked()) {
                    ShopItemGui.this.buy();
                }
            }
        });
    }

    private void addMouseoverListener() {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver() && ShopItemGui.this.item.isLocked()) {
                    ShopItemGui.this.timer.start();
                } else if (event.isMouseOff()) {
                    ShopItemGui.this.timer.stop();
                    ShopItemGui.this.pageGui.unregisterMouseover(ShopItemGui.this);
                } else if (ShopItemGui.this.item.isLocked() && event.isLeftClick()) {
                    ShopItemGui.this.timer.stop();
                    ShopItemGui.this.pageGui.unregisterMouseover(ShopItemGui.this);
                    ShopItemGui.this.item.reactToLockedClick();
                }
            }
        });
    }

    private void buy() {
        if (this.isAffordable(this.item)) {
            this.item.buy();
        } else {
            SoundMaestro.playSystemSound(GuiSounds.NEGATIVE);
        }
    }

    private String getCostString() {
        if (this.item.isLocked()) {
            return LOCKED;
        }
        if (GameManager.getGameMode() != GameMode.NORMAL) {
            return AVAILABLE;
        }
        return String.valueOf(Maths.formatNumber(this.item.getPrice())) + " dp";
    }

    private void addNewEffect() {
        super.wobble(1.1f, 0.5f);
        this.item.setNotNew();
    }

    private boolean isAffordable(ShopItem item) {
        if (GameManager.getGameMode() != GameMode.NORMAL) {
            return true;
        }
        return item.getPrice() <= GameManager.getSession().getStats().getDpCount();
    }
}

