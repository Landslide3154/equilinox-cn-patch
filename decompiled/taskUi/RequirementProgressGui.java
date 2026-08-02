/*
 * Decompiled with CFR 0.152.
 */
package taskUi;

import basics.DisplayManager;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import tasks.TaskRequirement;
import toolbox.Colour;
import userInterfaces.PlainProgressBarUi;

public class RequirementProgressGui
extends GuiComponent {
    private TaskRequirement requirement;
    private PlainProgressBarUi progressBar;
    private Text progressText;

    protected RequirementProgressGui(TaskRequirement requirement) {
        this.requirement = requirement;
        requirement.notifyInfoUpdated();
    }

    @Override
    protected void init() {
        this.addProgressBar();
        this.addText();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        if (this.requirement.isInfoUpdateRequired()) {
            this.progressBar.setProgress(this.requirement.getProgress());
            this.progressText.setText(this.requirement.getProgressText());
            if (this.requirement.isComplete() || this.requirement.alreadyCompleted()) {
                this.progressBar.setBarColour(ColourPalette.LIGHT_GREEN);
            } else {
                this.progressBar.setBarColour(ColourPalette.GREEN);
            }
            this.requirement.notifyInfoUpdated();
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addProgressBar() {
        Colour colour = this.requirement.isComplete() || this.requirement.alreadyCompleted() ? ColourPalette.LIGHT_GREEN : ColourPalette.GREEN;
        this.progressBar = new PlainProgressBarUi(colour, ColourPalette.LIGHT_GREY, this.requirement.getProgress());
        super.addComponent(this.progressBar, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    private void addText() {
        float fontSize = 468.0f / (float)DisplayManager.getUiHeight();
        this.progressText = Text.newText(this.requirement.getProgressText()).center().setFontSize(fontSize).create();
        this.progressText.setColour(ColourPalette.WHITE);
        super.addText(this.progressText, 0.0f, 0.0f, 1.0f);
    }
}

