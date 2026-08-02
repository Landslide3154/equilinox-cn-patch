/*
 * Decompiled with CFR 0.152.
 */
package saves;

import gameManaging.GameManager;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import session.GameMode;
import session.Session;
import utils.BinaryReader;
import utils.BinaryWriter;

public class SaveSlotInfo {
    private Calendar lastPlayed = Calendar.getInstance();
    private int population;
    private int tasksComplete;
    private int dp;
    private GameMode mode = GameMode.NORMAL;

    public void updateInfo(Session session) {
        this.lastPlayed = Calendar.getInstance();
        this.tasksComplete = GameManager.getTaskManager().calculateCompletedTaskCount();
        this.population = GameManager.getWorld().getEntityGrid().getEntityCount();
        this.dp = GameManager.getSession().getStats().getDpCount();
        this.mode = session.getMode();
    }

    public void save(BinaryWriter writer, Session session) throws IOException {
        this.updateInfo(session);
        writer.writeLong(this.lastPlayed.getTime().getTime());
        writer.writeInt(this.tasksComplete);
        writer.writeInt(this.population);
        writer.writeInt(this.dp);
        writer.writeInt(this.mode.ordinal());
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public Calendar getLastPlayedDate() {
        return this.lastPlayed;
    }

    public int getPopulation() {
        return this.population;
    }

    public int getTasksComplete() {
        return this.tasksComplete;
    }

    public int getDp() {
        return this.dp;
    }

    public GameMode getGameMode() {
        return this.mode;
    }

    public void load(BinaryReader reader) throws Exception {
        long dateTime = reader.readLong();
        this.lastPlayed = Calendar.getInstance();
        this.lastPlayed.setTime(new Date(dateTime));
        this.tasksComplete = reader.readInt();
        this.population = reader.readInt();
        this.dp = reader.readInt();
        this.mode = reader.getVersion() < 7 ? GameMode.NORMAL : GameMode.values()[reader.readInt()];
    }
}

