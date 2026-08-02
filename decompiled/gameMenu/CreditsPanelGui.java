/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import fontRendering.Text;
import gameMenu.CreditsInfo;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.Map;
import languages.GameText;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;

public class CreditsPanelGui
extends GuiComponent {
    private static final String CREDITS = GameText.getText(59);
    protected static final float LINE_HEIGHT = 0.07f;
    private static final float INFO_FONT = 1.0f;
    private static final float TITLE_FONT = 1.6f;
    protected static final float Y_START = 0.08f;
    private static final float CENTER = 0.5f;
    private static final float CENTER_GAP = 0.05f;
    protected static final float TITLE_GAP = 0.15f;
    private static final float HALF_CENTER_GAP = 0.025f;
    private float yPos = 0.08f;
    private final CreditsInfo creditsInfo;
    private final float totalYSize;

    public CreditsPanelGui(CreditsInfo creditsInfo) {
        this.creditsInfo = creditsInfo;
        this.totalYSize = 0.23f + (float)creditsInfo.getLineCount() * 0.07f;
    }

    public float getTotalYSize() {
        return this.totalYSize;
    }

    @Override
    protected void init() {
        super.init();
        this.addTitle();
        for (Map.Entry<String, String[]> entry : this.creditsInfo.getCredits().entrySet()) {
            this.addInfo(entry.getKey(), entry.getValue());
        }
    }

    private void addTitle() {
        Text text = Text.newText(CREDITS).center().setFontSize(1.6f).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, this.yPos / this.totalYSize, 1.0f);
        this.yPos += 0.15f;
    }

    private void addInfo(String job, String ... names) {
        Text text = Text.newText(job).setFontSize(1.0f).rightAlign().create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, this.yPos / this.totalYSize, 0.475f);
        this.addNames(0, names);
    }

    private void addNames(int index, String[] names) {
        if (index == names.length - 1) {
            this.addNameText(names[index++]);
        } else {
            this.addNameText(String.valueOf(names[index++]) + " - " + names[index++]);
        }
        this.yPos += 0.07f;
        if (index == names.length) {
            return;
        }
        this.addNames(index, names);
    }

    private void addNameText(String names) {
        Text text = Text.newText(names).setFontSize(1.0f).create();
        text.setColour(ColourPalette.GREEN);
        super.addText(text, 0.525f, this.yPos / this.totalYSize, 1.0f);
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
}

