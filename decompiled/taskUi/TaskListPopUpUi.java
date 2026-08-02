/*
 * Decompiled with CFR 0.152.
 */
package taskUi;

import fontRendering.Text;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import tasks.Task;
import userInterfaces.GuiPanel;

public class TaskListPopUpUi
extends GuiPanel {
    public static final float BUFF = 0.05f;
    public static final float TEXT_GAP = 0.27f;
    public static final float POPUP_WIDTH = 0.34f;
    private Task[] unlockableTasks;
    private float buff;
    private float textHeight;

    public TaskListPopUpUi(Task[] unlockableTasks) {
        super(GuiRepository.BLOCK, ColourPalette.MIDDLE_GREY, 1, ColourPalette.LIGHT_GREY);
        super.setRenderLevel(1);
        this.unlockableTasks = unlockableTasks;
        float total = 0.1f + 0.27f * (float)unlockableTasks.length;
        this.buff = 0.05f / total;
        this.textHeight = 0.27f / total;
    }

    @Override
    protected void init() {
        super.init();
        float yPos = this.buff;
        Task[] taskArray = this.unlockableTasks;
        int n = this.unlockableTasks.length;
        int n2 = 0;
        while (n2 < n) {
            Task task = taskArray[n2];
            Text text = Text.newText(task.name).center().setFontSize(UiSettings.NORM_FONT).create();
            text.setColour(ColourPalette.WHITE);
            super.addText(text, 0.0f, yPos, 1.0f);
            yPos += this.textHeight;
            ++n2;
        }
    }
}

