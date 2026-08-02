/*
 * Decompiled with CFR 0.152.
 */
package musicTab;

import fontRendering.Text;
import gridLayout.GridGui;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ChangeListener;
import userInterfaces.GuiSlider;

public class VolumeChooserUi
extends GuiComponent {
    private static final float SLIDER_Y = 0.7f;
    private static final float SLIDER_HEIGHT = 0.25f;
    private static final float SLIDER_WIDTH = 0.9f;
    private final String title;
    private final float initialValue;
    private final ChangeListener listener;

    protected VolumeChooserUi(String title, float initialValue, ChangeListener listener) {
        this.title = title;
        this.listener = listener;
        this.initialValue = initialValue;
    }

    @Override
    protected void init() {
        super.init();
        this.addTitle();
        this.addVolumeSlider();
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

    private void addTitle() {
        Text text = Text.newText(this.title).setFontSize(GridGui.FONT_SIZE).center().create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, 0.0f, 1.0f);
    }

    private void addVolumeSlider() {
        GuiSlider slider = new GuiSlider(this.initialValue);
        slider.addChangeListener(this.listener);
        super.addComponent(slider, 0.050000012f, 0.7f, 0.9f, 0.25f);
    }
}

