/*
 * Decompiled with CFR 0.152.
 */
package shops;

import basics.DisplayManager;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import visualFxDrivers.BounceDriver;
import visualFxDrivers.SlideDriver;

public class PlusOneGui
extends GuiComponent {
    private static final float MAX_TIME = 0.5f;
    private float time = 0.0f;

    public PlusOneGui(float fontSize) {
        Text text = Text.newText("+1").center().setFontSize(fontSize).create();
        text.setColour(ColourPalette.WHITE);
        text.setAlphaDriver(new BounceDriver(0.0f, 1.0f, 0.5f));
        text.setScaleDriver(new SlideDriver(1.0f, 1.5f, 0.5f));
        super.addText(text, 0.0f, 0.0f, 1.0f);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        this.time += DisplayManager.getDeltaSeconds();
        if (this.time > 0.5f) {
            this.remove();
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }
}

