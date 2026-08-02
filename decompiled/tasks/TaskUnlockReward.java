/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import guis.GuiComponent;
import languages.ComplexString;
import languages.GameText;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import taskUi.TaskInformationGui;
import taskUi.TaskListPopUpUi;
import taskUi.TaskRewardGui;
import tasks.Reward;
import tasks.Task;
import tasks.TaskManager;
import tasks.TaskRewardOrder;
import userInterfaces.Listener;

public class TaskUnlockReward
extends Reward {
    private static final String UNLOCK_TITLE = GameText.getText(138);
    private static final String UNLOCKS_TITLE = GameText.getText(139);
    private static final ComplexString UNLOCK_MESSAGE = GameText.getComplexText(140);
    private static final ComplexString UNLOCKS_MESSAGE = GameText.getComplexText(141);
    private static final ComplexString UNLOCK_INFO = GameText.getComplexText(142);
    private static final ComplexString UNLOCKS_INFO = GameText.getComplexText(143);
    private final int[] tasksUnlocked;
    private Task[] unlockableTasks;

    public TaskUnlockReward(int[] taskIds) {
        this.tasksUnlocked = taskIds;
    }

    @Override
    public void setStateUnlocked() {
    }

    @Override
    public void payOut() {
        Task[] taskArray = this.unlockableTasks;
        int n = this.unlockableTasks.length;
        int n2 = 0;
        while (n2 < n) {
            Task task = taskArray[n2];
            task.unlock();
            ++n2;
        }
        if (this.unlockableTasks.length == 1) {
            EquilinoxGuis.notify(UNLOCK_TITLE, UNLOCK_MESSAGE.getString("\"" + this.unlockableTasks[0].name + "\""), GuiRepository.TASKS_256, null, new Listener(){

                @Override
                public void eventOccurred(boolean on) {
                    TaskInformationGui.openTaskInfo(TaskUnlockReward.this.unlockableTasks[0]);
                }
            });
        } else {
            EquilinoxGuis.notify(UNLOCKS_TITLE, UNLOCKS_MESSAGE.getString(Integer.toString(this.unlockableTasks.length)), GuiRepository.TASKS_256, null);
        }
    }

    @Override
    public String getInfo() {
        if (this.unlockableTasks.length == 1) {
            return UNLOCK_INFO.getString("\"" + this.unlockableTasks[0].name + "\"");
        }
        return UNLOCKS_INFO.getString(Integer.toString(this.unlockableTasks.length));
    }

    @Override
    public void linkTask(Task task, TaskManager manager) {
        this.unlockableTasks = new Task[this.tasksUnlocked.length];
        int i = 0;
        while (i < this.unlockableTasks.length) {
            this.unlockableTasks[i] = manager.getTask(this.tasksUnlocked[i]);
            this.unlockableTasks[i].setPrerequisite(task);
            ++i;
        }
    }

    @Override
    public boolean hasExtraInfo() {
        return this.unlockableTasks.length > 1;
    }

    @Override
    public GuiComponent addExtraInfo(TaskRewardGui panel, float xPos, float yPos) {
        float height = 0.1f + (float)this.tasksUnlocked.length * 0.27f;
        TaskListPopUpUi test = new TaskListPopUpUi(this.unlockableTasks);
        panel.addComponent(test, xPos, yPos - height, 0.34f, height);
        return test;
    }

    @Override
    public int getOrderingWeight() {
        return TaskRewardOrder.TASK.ordinal();
    }
}

