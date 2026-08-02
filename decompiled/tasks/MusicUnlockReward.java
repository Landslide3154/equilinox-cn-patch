/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import guis.GuiComponent;
import languages.ComplexString;
import languages.GameText;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import music.MusicTrack;
import taskUi.TaskRewardGui;
import tasks.Reward;
import tasks.Task;
import tasks.TaskManager;
import tasks.TaskRewardOrder;

public class MusicUnlockReward
extends Reward {
    private static final String UNLOCK_TITLE = GameText.getText(653);
    private static final ComplexString UNLOCKS_MESSAGE = GameText.getComplexText(654);
    private static final ComplexString UNLOCK_INFO = GameText.getComplexText(652);
    private final MusicTrack music;

    public MusicUnlockReward(MusicTrack music) {
        this.music = music;
    }

    @Override
    public void setStateUnlocked() {
        this.music.unlock();
    }

    @Override
    public void payOut() {
        this.music.unlock();
        EquilinoxGuis.notify(UNLOCK_TITLE, UNLOCKS_MESSAGE.getString(this.music.getName()), GuiRepository.MUSIC_256, null);
    }

    @Override
    public String getInfo() {
        return UNLOCK_INFO.getString(this.music.getName());
    }

    @Override
    public GuiComponent addExtraInfo(TaskRewardGui panel, float xPos, float yPos) {
        return null;
    }

    @Override
    public boolean hasExtraInfo() {
        return false;
    }

    @Override
    public void linkTask(Task task, TaskManager manager) {
    }

    @Override
    public int getOrderingWeight() {
        return TaskRewardOrder.MUSIC.ordinal();
    }
}

