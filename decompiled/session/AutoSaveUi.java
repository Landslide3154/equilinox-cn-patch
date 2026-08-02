/*
 * Decompiled with CFR 0.152.
 */
package session;

import basics.DisplayManager;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import visualFxDrivers.SlideDriver;

public class AutoSaveUi
extends GuiComponent {
    private static final String MESSAGE = GameText.getText(661);
    private static final int FRAME_COUNT = 4;
    private static final float FADE_TIME = 0.6f;
    private Text text;
    private int frames = 0;
    private float timer = 1.0f;

    @Override
    protected void init() {
        super.init();
        this.text = Text.newText(MESSAGE).center().setFontSize(UiSettings.LARGE_FONT).create();
        this.text.setColour(ColourPalette.WHITE);
        super.addText(this.text, 0.0f, 0.0f, 1.0f);
    }

    @Override
    public boolean isMouseOverFocusIrrelevant() {
        return false;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        ++this.frames;
        if (this.frames == 4) {
            this.text.setAlphaDriver(new SlideDriver(1.0f, 0.0f, 0.6f));
        } else if (this.frames >= 4) {
            this.timer -= DisplayManager.getDeltaSeconds() / 0.6f;
            if (this.timer <= 0.0f) {
                this.remove();
            }
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }
}

