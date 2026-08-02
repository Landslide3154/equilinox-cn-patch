/*
 * Decompiled with CFR 0.152.
 */
package toolbar;

import fontRendering.Text;
import glRequestProcessing.GlRequest;
import glRequestProcessing.GlRequestProcessor;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Maths;

public class DppmCounter
extends GuiComponent {
    private static final String PLUS_TEXT = "+ ";
    private static final String DP_TEXT = GameText.getText(156);
    private static final float COUNT_TEXT_X = 0.05f;
    private static final float DP_TEXT_X = 0.64f;
    private Text countText;
    private Text dpText;

    public DppmCounter(int initialCount) {
        this.addCountText();
        this.addDpText();
        this.setCount(0);
    }

    public void setCount(int count) {
        final String countString = PLUS_TEXT + Maths.formatNumber(count);
        GlRequestProcessor.sendRequest(new GlRequest(){

            @Override
            public void executeGlRequest() {
                DppmCounter.this.countText.setText(countString);
            }
        });
    }

    private void removeTexts() {
        if (this.dpText != null) {
            super.deleteText(this.dpText);
            this.dpText = null;
        }
        if (this.countText != null) {
            super.deleteText(this.countText);
            this.countText = null;
        }
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

    private void addDpText() {
        if (this.dpText != null) {
            return;
        }
        this.dpText = Text.newText(DP_TEXT).setFontSize(UiSettings.LARGE_FONT).create();
        this.dpText.setColour(ColourPalette.GREEN);
        super.addText(this.dpText, 0.64f, 0.19f, 1.0f);
    }

    private void addCountText() {
        if (this.countText != null) {
            return;
        }
        this.countText = Text.newText(" ").setFontSize(UiSettings.LARGE_FONT).create();
        this.countText.setColour(ColourPalette.GREEN);
        super.addText(this.countText, 0.05f, 0.19f, 1.0f);
    }
}

