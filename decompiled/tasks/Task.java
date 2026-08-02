/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import audio.SoundMaestro;
import events.EventData;
import events.EventManager;
import gameManaging.GameManager;
import gridLayout.CurrentFilterSettings;
import gridLayout.GridComponent;
import gridLayout.ItemPageGui;
import guis.GuiComponent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import languages.ComplexString;
import languages.GameText;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import session.GameMode;
import session.Session;
import taskUi.TaskInformationGui;
import taskUi.TaskUi;
import tasks.Reward;
import tasks.TaskManager;
import tasks.TaskRequirement;
import tasks.TaskState;
import userInterfaces.Listener;
import utils.BinaryReader;
import utils.BinaryWriter;

public class Task
implements GridComponent {
    private static final String COMPLETE_MESSAGE = GameText.getText(106);
    private static final ComplexString COLLECT_MESSAGE = GameText.getComplexText(107);
    private static final ComplexString AUTO_MESSAGE = GameText.getComplexText(108);
    protected final int id;
    public final String name;
    public final String description;
    private Task prerequisite;
    private Integer linkedHelpTab = null;
    private boolean pinned = false;
    private boolean repeatable = false;
    private boolean autoCollect = false;
    private boolean notify = true;
    private TaskState state = TaskState.UNSTARTED;
    private TaskUi currentUi = null;
    private List<Reward> rewards = new ArrayList<Reward>();
    private List<TaskRequirement> requirements = new ArrayList<TaskRequirement>();

    public Task(int id, String name, String desc, List<TaskRequirement> requirements, List<Reward> rewards) {
        this.id = id;
        this.name = name;
        this.description = desc;
        this.requirements = requirements;
        this.rewards = rewards;
        Collections.sort(this.rewards);
        this.setTaskInRequirements();
    }

    public Task(int id, boolean repeatable, String name, String desc, List<TaskRequirement> requirements, List<Reward> rewards) {
        this.id = id;
        this.name = name;
        this.description = desc;
        this.requirements = requirements;
        this.rewards = rewards;
        Collections.sort(this.rewards);
        this.repeatable = repeatable;
        this.setTaskInRequirements();
    }

    public void linkUi(TaskUi ui) {
        this.currentUi = ui;
    }

    public TaskUi getCurrentUi() {
        return this.currentUi;
    }

    public void setLinkedHelpTab(int tab) {
        this.linkedHelpTab = tab;
    }

    public Integer getLinkedHelpTab() {
        return this.linkedHelpTab;
    }

    @Override
    public GuiComponent getComponentGui(ItemPageGui page) {
        return new TaskUi(this);
    }

    @Override
    public int compareTo(GridComponent task) {
        return this.getWeight() > ((Task)task).getWeight() ? 1 : -1;
    }

    public boolean isRepeatable() {
        return this.repeatable;
    }

    public boolean alreadyCompleted() {
        return this.repeatable && this.autoCollect;
    }

    public boolean isAutoCollect() {
        return this.autoCollect;
    }

    public void check() {
        for (TaskRequirement requirement : this.requirements) {
            if (requirement.isComplete()) continue;
            requirement.check();
        }
    }

    public void unlock() {
        this.determineState();
    }

    public boolean isLocked() {
        return this.state == TaskState.LOCKED;
    }

    public void setPrerequisite(Task task) {
        this.prerequisite = task;
    }

    public void pin(boolean pin) {
        this.pinned = pin;
    }

    public boolean isPinned() {
        return this.pinned;
    }

    @Override
    public boolean matchesSearch(String searchString) {
        return this.name.toLowerCase().contains(searchString.toLowerCase());
    }

    public void complete() {
        SoundMaestro.playSystemSound(GuiSounds.CASH);
        this.state = TaskState.COMPLETE;
        for (Reward reward : this.rewards) {
            reward.payOut();
        }
        if (this.repeatable) {
            if (!this.autoCollect) {
                EventManager.TASK_COMPLETE.registerEvent(new EventData(), Integer.toString(this.id));
            }
            this.autoCollect = true;
            this.state = TaskState.IN_PROGRESS;
            for (TaskRequirement requirement : this.requirements) {
                requirement.reset(GameManager.getSession());
            }
        } else {
            EventManager.TASK_COMPLETE.registerEvent(new EventData(), Integer.toString(this.id));
        }
    }

    public void updateState() {
        if (this.state == TaskState.COMPLETE || this.state == TaskState.CLAIM_REWARD || this.state == TaskState.LOCKED || GameManager.getGameMode() != GameMode.NORMAL) {
            return;
        }
        this.determineState();
        if (this.state == TaskState.CLAIM_REWARD) {
            if (this.autoCollect) {
                this.autoComplete();
            } else {
                this.switchToClaimState();
            }
        }
    }

    public TaskState getState() {
        return this.state;
    }

    public List<Reward> getRewards() {
        return this.rewards;
    }

    public List<TaskRequirement> getRequirements(boolean reorder) {
        if (!reorder) {
            return this.requirements;
        }
        int unstartedIndex = 0;
        int startedIndex = 0;
        ArrayList<TaskRequirement> orderedReqs = new ArrayList<TaskRequirement>(this.requirements.size());
        for (TaskRequirement req : this.requirements) {
            if (req.isComplete()) {
                orderedReqs.add(req);
                continue;
            }
            if (req.isStarted()) {
                orderedReqs.add(startedIndex, req);
                ++startedIndex;
                ++unstartedIndex;
                continue;
            }
            orderedReqs.add(unstartedIndex, req);
            ++unstartedIndex;
        }
        return orderedReqs;
    }

    public int getRequirementCount() {
        return this.requirements.size();
    }

    public boolean needsChecking() {
        for (TaskRequirement requirement : this.requirements) {
            if (!requirement.needsChecking()) continue;
            return true;
        }
        return false;
    }

    public Task getPrerequisite() {
        return this.prerequisite;
    }

    @Override
    public boolean isInFilterGroup(CurrentFilterSettings filterSettings) {
        if (filterSettings.getFilter(0).isNoFilter()) {
            return true;
        }
        int mainCat = filterSettings.getFilter(0).getMainCategory();
        if (this.repeatable && this.autoCollect) {
            return mainCat == TaskState.values().length;
        }
        return this.state.ordinal() == mainCat;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public boolean notificationsOn() {
        return this.notify;
    }

    protected void linkTaskToRewards(TaskManager manager) {
        for (Reward reward : this.rewards) {
            reward.linkTask(this, manager);
        }
    }

    protected void export(BinaryWriter writer) throws IOException {
        boolean complete;
        boolean bl = complete = this.state == TaskState.COMPLETE;
        if (this.repeatable) {
            writer.writeBoolean(this.autoCollect);
            writer.writeBoolean(this.notify);
        }
        writer.writeBoolean(this.pinned);
        writer.writeBoolean(complete);
        writer.writeBoolean(this.isLocked());
        if (!complete) {
            for (TaskRequirement requirement : this.requirements) {
                requirement.export(writer);
            }
        }
    }

    protected void reset(Session newSession) {
        this.autoCollect = false;
        this.notify = true;
        this.pinned = false;
        for (TaskRequirement requirement : this.requirements) {
            requirement.reset(newSession);
        }
        if (this.prerequisite != null) {
            this.state = TaskState.LOCKED;
        } else {
            this.determineState();
        }
    }

    protected void resetLight(Session newSession) {
        this.autoCollect = false;
        this.notify = true;
        this.pinned = false;
        for (TaskRequirement requirement : this.requirements) {
            requirement.reset(newSession);
        }
        this.determineState();
    }

    protected void loadState(BinaryReader reader, Session newSession) throws Exception {
        if (this.repeatable) {
            this.autoCollect = reader.readBoolean();
            this.notify = reader.readBoolean();
        }
        this.pinned = reader.readBoolean();
        boolean isComplete = reader.readBoolean();
        boolean isLocked = reader.readBoolean();
        if (isComplete) {
            this.setCompleted();
        } else {
            for (TaskRequirement requirement : this.requirements) {
                requirement.loadState(reader, newSession);
            }
            if (!isLocked) {
                this.state = TaskState.UNSTARTED;
                this.determineState();
            } else {
                this.state = TaskState.LOCKED;
            }
        }
    }

    private void switchToClaimState() {
        EquilinoxGuis.notify(COMPLETE_MESSAGE, COLLECT_MESSAGE.getString(this.name), GuiRepository.TASK_DONE_256, GuiSounds.COMPLETE, new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                TaskInformationGui.openTaskInfo(Task.this);
                EquilinoxGuis.getToolBar().stopTaskButtonWobble();
            }
        });
        EquilinoxGuis.getToolBar().wobbleTaskButton();
    }

    private void autoComplete() {
        if (this.notify) {
            EquilinoxGuis.notify(COMPLETE_MESSAGE, AUTO_MESSAGE.getString(this.name), GuiRepository.TASK_DONE_256, GuiSounds.COMPLETE, new Listener(){

                @Override
                public void eventOccurred(boolean on) {
                    TaskInformationGui.openTaskInfo(Task.this);
                }
            });
        }
        this.state = TaskState.IN_PROGRESS;
        for (Reward reward : this.rewards) {
            reward.payOut();
        }
        for (TaskRequirement requirement : this.requirements) {
            requirement.reset(GameManager.getSession());
        }
    }

    private int getWeight() {
        int weight = this.id;
        if (!this.pinned) {
            weight += 1000;
        }
        return weight += this.state.weight;
    }

    private void setCompleted() {
        this.state = TaskState.COMPLETE;
        for (TaskRequirement requirement : this.requirements) {
            requirement.setCompleted();
        }
        this.setRewardsCompleted();
    }

    private void determineState() {
        boolean finished = true;
        boolean started = false;
        for (TaskRequirement requirement : this.requirements) {
            finished &= requirement.isComplete();
            started |= requirement.isStarted();
        }
        this.state = finished ? TaskState.CLAIM_REWARD : (started || this.autoCollect ? TaskState.IN_PROGRESS : TaskState.UNSTARTED);
    }

    private void setRewardsCompleted() {
        for (Reward reward : this.rewards) {
            reward.setStateUnlocked();
        }
    }

    private void setTaskInRequirements() {
        for (TaskRequirement req : this.requirements) {
            req.setTask(this);
        }
    }
}

