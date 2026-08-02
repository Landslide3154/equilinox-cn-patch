/*
 * Decompiled with CFR 0.152.
 */
package sessionStats;

import gameManaging.GameManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import mainGuis.EquilinoxGuis;
import session.GameMode;
import session.Session;
import sessionStats.LockStatus;
import time.Calendar;
import toolbar.Toolbar;
import toolbox.Colour;
import utils.BinaryReader;
import utils.BinaryWriter;

public class Stats {
    private static final float MINUTE = 60.0f;
    private static final float DP_UPDATE_TIME = 11.0f;
    private static final float MIN_FRACTION = 0.18333334f;
    private Calendar timeAndDate;
    private int dp = 2500;
    private int dpPerMin = 0;
    private boolean loaded = false;
    private LockStatus lockStatus;
    private Session session;
    private float time = 0.0f;
    private static final int MAX_COLS = 6;
    private List<Colour> recentColours = new ArrayList<Colour>();
    private static final int MAX = 2130000000;

    private Stats(Session session) {
        this.session = session;
        EquilinoxGuis.getToolBar().getDiseaseCounter().reset();
        EquilinoxGuis.getToolBar().getHungerCounter().reset();
    }

    public static Stats loadStats(BinaryReader reader, Session session) throws Exception {
        Stats stats = new Stats(session);
        if (session.getMode() == GameMode.BUILD) {
            int count = reader.readInt();
            int i = 0;
            while (i < count) {
                stats.recentColours.add(new Colour(reader.readVector()));
                ++i;
            }
        }
        stats.dp = reader.readInt();
        stats.dpPerMin = reader.readInt();
        stats.timeAndDate = Calendar.load(reader);
        stats.lockStatus = LockStatus.loadLockStatus(reader);
        stats.loaded = true;
        return stats;
    }

    public void addRecentColour(Colour colour) {
        this.recentColours.add(0, colour.duplicate());
        Iterator<Colour> iterator = this.recentColours.iterator();
        iterator.next();
        while (iterator.hasNext()) {
            Colour test = iterator.next();
            float diff = Colour.calculateDifference(colour, test);
            if (!(diff < 0.014f)) continue;
            iterator.remove();
        }
        while (this.recentColours.size() > 6) {
            this.recentColours.remove(this.recentColours.size() - 1);
        }
    }

    public List<Colour> getRecentColours() {
        return this.recentColours;
    }

    public static Stats createNewStats(Session session) {
        Stats stats = new Stats(session);
        stats.lockStatus = LockStatus.newLockStatus();
        stats.loaded = true;
        stats.timeAndDate = Calendar.init();
        return stats;
    }

    public void update() {
        this.timeAndDate.update(GameManager.getGameSeconds());
        this.time += GameManager.getGameSeconds();
        if (this.time >= 11.0f) {
            this.time %= 11.0f;
            this.increaseDp(this.getDpChange());
        }
    }

    public Calendar getCalendar() {
        return this.timeAndDate;
    }

    public void export(BinaryWriter writer) throws IOException {
        if (this.session.getMode() == GameMode.BUILD) {
            writer.writeInt(this.recentColours.size());
            for (Colour colour : this.recentColours) {
                writer.writeVector(colour.getVector());
            }
        }
        writer.writeInt(this.dp);
        writer.writeInt(this.dpPerMin);
        this.timeAndDate.save(writer);
        this.lockStatus.export(writer);
    }

    public LockStatus getLockStatus() {
        return this.lockStatus;
    }

    public int getDpCount() {
        return this.dp;
    }

    public int getDpPerMinute() {
        return this.dpPerMin;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void increaseDp(int increase) {
        if (this.dp > 2130000000 || !GameManager.isNormalMode()) {
            return;
        }
        this.dp += increase;
        EquilinoxGuis.getToolBar().getDpCounter().increaseCount(increase);
    }

    public void setDpPerMinute(int amount) {
        if (amount != this.dpPerMin) {
            this.dpPerMin = amount;
            EquilinoxGuis.getToolBar().getDppmCounter().setCount(this.dpPerMin);
        }
    }

    public void updateToolbar(GameMode mode) {
        Toolbar toolBar = EquilinoxGuis.getToolBar();
        if (mode == GameMode.NORMAL) {
            toolBar.getDpCounter().setCount(this.dp, true);
        } else {
            toolBar.getDpCounter().setModeName(mode);
        }
        toolBar.getDpCounter().showDpText(mode == GameMode.NORMAL);
        toolBar.getDppmCounter().show(mode == GameMode.NORMAL);
        if (mode == GameMode.NORMAL) {
            toolBar.getDppmCounter().setCount(this.dpPerMin);
        }
        toolBar.reduce(mode != GameMode.NORMAL);
    }

    private int getDpChange() {
        return (int)((float)this.dpPerMin * 0.18333334f);
    }
}

