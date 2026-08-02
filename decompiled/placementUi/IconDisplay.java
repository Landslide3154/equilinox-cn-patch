/*
 * Decompiled with CFR 0.152.
 */
package placementUi;

import basics.DisplayManager;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import textures.Texture;
import toolbox.Colour;
import userInterfaces.GuiImage;
import userInterfaces.GuiPanel;
import visualFxDrivers.BounceDriver;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.ValueDriver;

public class IconDisplay
extends GuiPanel {
    private static final Colour GLOW_COL = ColourPalette.DARKER_GREEN.duplicate().scale(1.3f);
    private static final float ICON_SIZE = 0.95f;
    private final Texture icon;
    private GuiImage iconImage;
    private boolean canPlace;
    private ValueDriver glowFactor = new ConstantDriver(0.0f);
    private Colour currentColour;
    private Colour actualColour = new Colour();

    public IconDisplay(Texture icon, boolean canPlace) {
        super(GuiRepository.COOL, canPlace ? ColourPalette.DARKER_GREEN : ColourPalette.LOCKED_BACKGROUND, 1, ColourPalette.LIGHT_GREY);
        this.canPlace = canPlace;
        this.currentColour = canPlace ? ColourPalette.DARKER_GREEN : ColourPalette.LOCKED_BACKGROUND;
        this.icon = icon;
    }

    public void indicatePlaceable(boolean placeable) {
        this.canPlace = placeable;
        this.iconImage.getTexture().setOverrideColour(placeable ? null : ColourPalette.WHITE);
        this.currentColour = this.canPlace ? ColourPalette.DARKER_GREEN : ColourPalette.LOCKED_BACKGROUND;
    }

    public void pulse(float pulseTime) {
        this.glowFactor = new BounceDriver(0.0f, 1.0f, pulseTime);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        float factor = this.glowFactor.update(DisplayManager.getDeltaSeconds());
        super.setColour(Colour.interpolateColours(this.currentColour, GLOW_COL, factor, this.actualColour));
    }

    @Override
    protected void init() {
        super.init();
        this.iconImage = new GuiImage(this.icon);
        this.iconImage.getTexture().setOverrideColour(this.canPlace ? null : ColourPalette.WHITE);
        this.iconImage.getTexture().flip(true);
        super.addCenteredComponent(this.iconImage, 0.5f, 0.5f, 0.95f);
    }
}

