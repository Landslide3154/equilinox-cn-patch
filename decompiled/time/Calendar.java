/*
 * Decompiled with CFR 0.152.
 */
package time;

import gameManaging.GameManager;
import gameManaging.GameState;
import java.io.IOException;
import languages.GameText;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import org.lwjgl.input.Keyboard;
import session.GameMode;
import toolbox.MyKeyboard;
import utils.BinaryReader;
import utils.BinaryWriter;

public class Calendar {
    private static final String TITLE = GameText.getText(683);
    private static final String MESSAGE = GameText.getText(684);
    private static final String YEAR = GameText.getText(892);
    private static final String YEARS = GameText.getText(893);
    private static final String DAY = GameText.getText(894);
    private static final String DAYS = GameText.getText(895);
    private static final String HR = GameText.getText(896);
    private static final String HRS = GameText.getText(897);
    private static final float NOTIFY_TIME = 0.25125f;
    public static final float DAY_LENGTH_SECONDS = 720.0f;
    public static final float CHEAT_DAY_SECONDS = 10.0f;
    private static final float START_TIME = 0.25f;
    public static final float HOURS_IN_DAY = 24.0f;
    public static final float HOUR_LENGTH = 30.0f;
    public static final int QUARTERS = 1;
    public static final int DAYS_PER_QUARTER = 10;
    private int day;
    private float time;
    private boolean notified;
    private boolean pauseTime = false;

    private Calendar(int day, float time) {
        this.day = day;
        this.time = time;
        this.notified = this.isAfterNotifyTime();
    }

    public static Calendar init() {
        return new Calendar(0, 0.25f);
    }

    public static Calendar load(BinaryReader reader) throws Exception {
        int day = reader.readInt();
        float time = reader.readFloat();
        return new Calendar(day, time);
    }

    public void update(float delta) {
        if (GameManager.getGameState() == GameState.SPLASH_SCREEN) {
            return;
        }
        if (!this.notified && this.isAfterNotifyTime()) {
            this.notifyWelcomeMessage();
        }
        this.checkPauseTime();
        this.checkTimeChange(delta);
        this.updateTime(delta);
    }

    private void updateTime(float delta) {
        delta = this.pauseTime ? 0.0f : delta;
        this.time += delta / 720.0f;
        if (this.time >= 1.0f) {
            ++this.day;
            this.time %= 1.0f;
        }
    }

    public void increaseTime(float amount) {
        this.time += amount;
        if (this.time <= 0.0f) {
            this.time += 1.0f;
        }
    }

    private void checkPauseTime() {
        if (Keyboard.isKeyDown(42)) {
            MyKeyboard.getKeyboard().keyDownEventOccurred(55);
        }
    }

    public void pauseTime(boolean pause) {
        this.pauseTime = pause;
    }

    private void checkTimeChange(float delta) {
        if (Keyboard.isKeyDown(42)) {
            Keyboard.isKeyDown(78);
            Keyboard.isKeyDown(74);
        }
    }

    private void notifyWelcomeMessage() {
        if (GameManager.getGameMode() == GameMode.NORMAL) {
            EquilinoxGuis.notify(TITLE, MESSAGE, GuiRepository.WELCOME, GuiSounds.NOTIFY);
        }
        EquilinoxGuis.getToolBar().wobbleTaskButton();
        this.notified = true;
    }

    public float getRawTime() {
        return this.time;
    }

    private boolean isAfterNotifyTime() {
        return this.day > 0 || this.time >= 0.25125f;
    }

    public boolean isNightTime() {
        return this.time > 0.9f || this.time < 0.25f;
    }

    public int getTimeHours() {
        return (int)(this.time * 24.0f);
    }

    public int getTimeMinutes() {
        return (int)(this.time * 24.0f % 1.0f * 60.0f);
    }

    public int getTimeMinutesNearest(int interval) {
        int minutes = this.getTimeMinutes();
        return minutes / interval * interval;
    }

    public int getYear() {
        return this.day / 10 + 1;
    }

    public int getQuarter() {
        return this.day / 10 % 1;
    }

    public int getDay() {
        return this.day % 10 + 1;
    }

    public void save(BinaryWriter writer) throws IOException {
        writer.writeInt(this.day);
        writer.writeFloat(this.time);
    }

    public static String formatTimeHours(float timeInHours) {
        return Calendar.formatTimeDays(timeInHours / 24.0f);
    }

    public static String formatTimeSeconds(float timeInSeconds) {
        return Calendar.formatTimeHours(timeInSeconds / 30.0f);
    }

    public static String formatTimeDays(float timeInDays) {
        int daysPerYear = 10;
        float years = timeInDays / (float)daysPerYear;
        if (years > 1.0f) {
            int days;
            int yearInt = (int)years;
            String text = String.valueOf(yearInt) + " " + (yearInt != 1 ? YEARS : YEAR);
            if (years < 10.0f && (days = (int)(years % 1.0f * (float)daysPerYear)) != 0) {
                text = String.valueOf(text) + ", " + days + " " + (days != 1 ? DAYS : DAY);
            }
            return text;
        }
        if (timeInDays > 1.0f) {
            int hours;
            int daysInt = (int)timeInDays;
            String text = String.valueOf(daysInt) + " " + (daysInt != 1 ? DAYS : DAY);
            if (timeInDays < 10.0f && (hours = (int)(timeInDays % 1.0f * 24.0f)) != 0) {
                text = String.valueOf(text) + ", " + hours + " " + (hours != 1 ? HRS : HR);
            }
            return text;
        }
        float hours = timeInDays * 24.0f;
        if (hours < 10.0f) {
            return String.valueOf(String.format("%.1f", Float.valueOf(hours))) + " " + HRS;
        }
        return String.valueOf((int)hours) + " " + HRS;
    }
}

