/*
 * Decompiled with CFR 0.152.
 */
package taskUi;

import guiRendering.GuiRenderData;
import guis.GuiTexture;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import userInterfaces.GuiClickable;

public class NotifyBellUi
extends GuiClickable {
    protected static final Colour TINT_COL_2 = new Colour(104.0f, 136.0f, 148.0f, true);
    protected static final Colour TINT_COL = new Colour(101.0f, 163.0f, 186.0f, true);
    private static final Colour BELL_COL = ColourPalette.LIGHT_GREY;
    private static final Colour BELL_COL_MO = ColourPalette.BRIGHT_GREY;
    private static final Colour CIRCLE_ON = ColourPalette.GREEN;
    private static final Colour CIRCLE_OFF = ColourPalette.FLAT_RED;
    private GuiTexture bellTexture;
    private GuiTexture circleTexture;

    public NotifyBellUi(boolean on) {
        super(true, 1.0f);
        this.initTextures(on);
        if (on) {
            super.setOn();
        }
    }

    @Override
    public void toggle() {
        super.toggle();
        if (super.isToggledOn()) {
            this.circleTexture.setOverrideColour(CIRCLE_ON);
        } else {
            this.circleTexture.setOverrideColour(CIRCLE_OFF);
        }
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        if (super.isMouseOver()) {
            this.bellTexture.setOverrideColour(BELL_COL_MO);
        } else {
            this.bellTexture.setOverrideColour(BELL_COL);
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.bellTexture.setPosition(position.x, position.y, scale.x, scale.y);
        this.circleTexture.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.bellTexture);
        data.addTexture(this.getLevel(), this.circleTexture);
    }

    private void initTextures(boolean on) {
        this.bellTexture = new GuiTexture(GuiRepository.NOTIFY_BELL);
        this.bellTexture.setOverrideColour(BELL_COL);
        this.circleTexture = new GuiTexture(GuiRepository.NOTIFY_BELL_CIRCLE);
        this.circleTexture.setOverrideColour(on ? CIRCLE_ON : CIRCLE_OFF);
    }
}

