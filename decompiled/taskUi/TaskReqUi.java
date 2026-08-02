/*
 * Decompiled with CFR 0.152.
 */
package taskUi;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import tasks.TaskRequirement;
import toolbox.Colour;
import userInterfaces.PlainProgressBarUi;

public class TaskReqUi
extends GuiComponent {
    private static final Colour TEXT_COLOUR = ColourPalette.BEIGE;
    private static final float X_PAD = 0.05f;
    private static final float Y_PAD = 0.05f;
    private static final float PROGRESS_TEXT_Y_PAD = 0.0f;
    private static final float TEXT_HEIGHT = 0.5f;
    private static final float BAR_HEIGHT = 0.39999998f;
    private static final float CONTENT_WIDTH = 0.9f;
    private static final float BAR_START = 0.55f;
    private TaskRequirement taskReq;
    private Text progressText;
    private PlainProgressBarUi progressBar;
    private float fontSize;

    public TaskReqUi(TaskRequirement taskReq, float fontSize) {
        this.taskReq = taskReq;
        this.fontSize = fontSize;
        taskReq.notifyOverviewUpdated();
    }

    @Override
    protected void init() {
        this.addText();
        this.addProgressBar();
        this.addProgressText();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        if (this.taskReq.isOverviewUpdateRequired()) {
            this.progressBar.setProgress(this.taskReq.getProgress());
            this.progressText.setText(this.taskReq.getProgressText());
            if (this.taskReq.isComplete() || this.taskReq.getTask().isRepeatable() && this.taskReq.getTask().isAutoCollect()) {
                this.progressBar.setBarColour(ColourPalette.LIGHT_GREEN);
            } else {
                this.progressBar.setBarColour(ColourPalette.GREEN);
            }
            this.taskReq.notifyOverviewUpdated();
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addText() {
        Text text = Text.newText(String.valueOf(this.taskReq.getShortDescription()) + ":").setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(TEXT_COLOUR);
        super.addText(text, 0.05f, 0.05f, 2.0f);
    }

    private void addProgressBar() {
        this.progressBar = new PlainProgressBarUi(ColourPalette.GREEN, ColourPalette.LIGHT_GREY, this.taskReq.getProgress());
        super.addComponent(this.progressBar, 0.05f, 0.55f, 0.9f, 0.39999998f);
        if (this.taskReq.isComplete() || this.taskReq.alreadyCompleted()) {
            this.progressBar.setBarColour(ColourPalette.LIGHT_GREEN);
        }
    }

    private void addProgressText() {
        this.progressText = Text.newText(this.taskReq.getProgressText()).center().setFontSize(UiSettings.SMALL_FONT).create();
        this.progressText.setColour(ColourPalette.WHITE);
        super.addText(this.progressText, 0.05f, 0.55f, 0.9f);
    }
}

