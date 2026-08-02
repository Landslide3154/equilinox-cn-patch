/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import events.EventData;
import events.EventListener;
import events.EventManager;
import gameManaging.GameManager;
import gameManaging.GameState;
import gridLayout.PageTracker;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import session.Session;
import tasks.NewTaskUpdater;
import tasks.Task;
import tasks.TaskCreator;
import tasks.TaskState;
import utils.BinaryReader;
import utils.BinaryWriter;

public class TaskManager {
    private Map<Integer, Task> tasks = new LinkedHashMap<Integer, Task>();
    private List<Task> tasksNeedingChecking = new ArrayList<Task>();
    private int taskToCheck = 0;
    private int completedCount = 0;
    private PageTracker tracker = new PageTracker(1);

    public TaskManager() {
        this.createTasks();
        this.initTasks();
        EventManager.TASK_COMPLETE.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                TaskManager.this.calculateCompletedTaskCount();
            }
        }, new String[0]);
    }

    public PageTracker getTracker() {
        return this.tracker;
    }

    public void update() {
        if (GameManager.getGameState() != GameState.GAME_MENU && GameManager.sessionManager.hasWorldReady() && !this.tasksNeedingChecking.isEmpty()) {
            Task task = this.tasksNeedingChecking.get(this.taskToCheck);
            task.check();
            ++this.taskToCheck;
            this.taskToCheck %= this.tasksNeedingChecking.size();
        }
    }

    public Task getTask(int id) {
        return this.tasks.get(id);
    }

    public void reset(Session newSession) {
        for (Task task : this.tasks.values()) {
            task.reset(newSession);
        }
        this.completedCount = 0;
        this.tracker.reset();
    }

    public void loadState(BinaryReader reader, Session loadingSession) throws Exception {
        NewTaskUpdater taskUpdater = new NewTaskUpdater(reader.getVersion(), loadingSession, this.tasks);
        int count = reader.readInt();
        this.completedCount = 0;
        taskUpdater.updateTask(57, 4);
        taskUpdater.updateTask(58, 5);
        taskUpdater.updateTask(59, 8);
        taskUpdater.updateTask(60, 9);
        taskUpdater.updateTask(61, 11);
        taskUpdater.updateTask(62, 12);
        int i = 0;
        while (i < count) {
            Task task = this.tasks.get(reader.readInt());
            task.loadState(reader, loadingSession);
            if (task.getState() == TaskState.COMPLETE || task.alreadyCompleted()) {
                ++this.completedCount;
            }
            ++i;
        }
    }

    public int getTaskCount() {
        return this.tasks.size();
    }

    public int calculateCompletedTaskCount() {
        int count = 0;
        for (Task task : this.tasks.values()) {
            if (task.getState() != TaskState.COMPLETE && !task.alreadyCompleted()) continue;
            ++count;
        }
        this.completedCount = count;
        return count;
    }

    public int getCompletedTaskCount() {
        return this.completedCount;
    }

    public void exportState(BinaryWriter writer) throws IOException {
        writer.writeInt(this.tasks.size());
        for (Map.Entry<Integer, Task> task : this.tasks.entrySet()) {
            writer.writeInt(task.getKey());
            task.getValue().export(writer);
        }
    }

    public List<Task> getTasks() {
        ArrayList<Task> taskList = new ArrayList<Task>();
        taskList.addAll(this.tasks.values());
        return taskList;
    }

    private void initTasks() {
        for (Task task : this.tasks.values()) {
            task.linkTaskToRewards(this);
            if (!task.needsChecking()) continue;
            this.tasksNeedingChecking.add(task);
        }
    }

    private void createTasks() {
        List<Task> createdTasks = TaskCreator.getTasks();
        for (Task task : createdTasks) {
            this.tasks.put(task.id, task);
        }
    }
}

