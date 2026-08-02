/*
 * Decompiled with CFR 0.152.
 */
package gameManaging;

import basics.DisplayManager;
import gameManaging.GameManager;
import gameManaging.GameState;
import interpolation.SmoothFloat;
import toolbox.MyKeyboard;

public class GameSpeed {
    public SmoothFloat timeSpeedControl = new SmoothFloat(1.0f, 10.0f);
    private float timeSpeed = 1.0f;
    private SpeedSetting current = SpeedSetting.NORMAL;

    public void update() {
        MyKeyboard keyboard = MyKeyboard.getKeyboard();
        if (keyboard.isKeyDown(57) && GameManager.getGameState() != GameState.GAME_MENU && this.current == SpeedSetting.NORMAL) {
            this.pause();
        } else if (!keyboard.isKeyDown(57) && this.current == SpeedSetting.PAUSED) {
            this.normalSpeed();
        }
        this.timeSpeedControl.update(DisplayManager.getDeltaSeconds());
        this.timeSpeed = this.timeSpeedControl.get();
    }

    public float getGameSpeed() {
        return this.timeSpeed;
    }

    public void normalSpeed() {
        this.current = SpeedSetting.NORMAL;
        this.timeSpeedControl.setTarget(this.current.speed);
    }

    public void fastSpeed() {
        this.current = SpeedSetting.FAST;
        this.timeSpeedControl.setTarget(this.current.speed);
    }

    public void pause() {
        if (this.current != SpeedSetting.FAST) {
            this.current = SpeedSetting.PAUSED;
            this.timeSpeedControl.setTarget(this.current.speed);
        }
    }

    public static enum SpeedSetting {
        NORMAL(1.0f),
        PAUSED(0.05f),
        FAST(9.5f);

        private float speed;

        private SpeedSetting(float speed) {
            this.speed = speed;
        }
    }
}

