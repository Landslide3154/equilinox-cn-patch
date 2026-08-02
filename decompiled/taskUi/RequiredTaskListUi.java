/*
 * Decompiled with CFR 0.152.
 */
package taskUi;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.LinkedList;
import languages.ComplexString;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import speciesInformation.SpeciesInfoGui;
import taskUi.TaskInformationGui;
import tasks.Task;
import tasks.TaskState;
import toolbox.Colour;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.TextButtonUi;

public class RequiredTaskListUi
extends GuiComponent {
    private static final ComplexString MORE_MESSAGE = GameText.getComplexText(640);
    private static final float BUTTON_HEIGHT = 0.1f;
    private static final float ASPECT_RATIO = 9.0f;
    private static final float GAP = 0.04f;
    private static final float TEXT_Y = 0.1f;
    private static final int MAX_TASKS_DEFAULT = 8;
    private static final Colour GREY = ColourPalette.MIDDLE_GREY.duplicate().scale(0.8f);
    private final Task rootTask;
    private final int maxTasks;
    private final boolean includeRoot;
    private final float gapY;
    private float currentY = 0.0f;

    public RequiredTaskListUi(Task rootTask) {
        this.rootTask = rootTask;
        this.includeRoot = false;
        this.maxTasks = 8;
        this.gapY = 0.04f;
    }

    public RequiredTaskListUi(Task rootTask, boolean showRoot, int maxTasks, float gap) {
        this.rootTask = rootTask;
        this.maxTasks = maxTasks;
        this.includeRoot = showRoot;
        this.gapY = gap;
    }

    @Override
    protected void init() {
        LinkedList<Task> linkedList = new LinkedList<Task>();
        Task startTask = this.includeRoot ? this.rootTask : this.rootTask.getPrerequisite();
        this.addAllChildTasks(startTask, linkedList);
        int count = 0;
        boolean overflow = linkedList.size() > this.maxTasks + 1;
        for (Task task : linkedList) {
            this.addTaskButton(task);
            if (overflow && ++count == this.maxTasks) break;
        }
        if (overflow) {
            this.addMoreMessage(linkedList.size() - this.maxTasks);
        }
    }

    private void addMoreMessage(int number) {
        Text moreText = Text.newText(MORE_MESSAGE.getString(Integer.toString(number))).center().setFontSize(UiSettings.NORM_FONT).create();
        moreText.setColour(this.includeRoot ? ColourPalette.BEIGE : ColourPalette.WHITE);
        super.addText(moreText, 0.0f, this.currentY, 1.0f);
    }

    private void addTaskButton(Task task) {
        TextButtonUi button = new TextButtonUi(task.name, this.getColour(task), SpeciesInfoGui.FONT_SIZE, task.getState() == TaskState.LOCKED ? ColourPalette.WHITE : GREY, 0.1f);
        button.setPreferredAspectRatio(9.0f);
        this.addListener(button, task);
        super.addCenteredComponentX(button, 0.5f, this.currentY, 0.1f);
        this.currentY += 0.1f + this.gapY;
    }

    private void addAllChildTasks(Task rootTask, LinkedList<Task> list) {
        list.addFirst(rootTask);
        if (rootTask.getPrerequisite() != null && rootTask.getPrerequisite().getState() != TaskState.COMPLETE) {
            this.addAllChildTasks(rootTask.getPrerequisite(), list);
        }
    }

    private Colour getColour(Task task) {
        if (task.getState() == TaskState.LOCKED) {
            return ColourPalette.FLAT_RED;
        }
        return ColourPalette.BEIGE;
    }

    private void addListener(TextButtonUi button, final Task task) {
        button.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    TaskInformationGui.openTaskInfo(task);
                }
            }
        });
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

