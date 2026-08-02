/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

public class TabSetting {
    private float time;
    private int tabNumber;

    public void setTabOpen(int tabNumber) {
        this.tabNumber = tabNumber;
        this.time = 0.0f;
    }

    public float updateTime(float delta) {
        this.time += delta;
        return this.time;
    }

    public void resetTime() {
        this.time = 0.0f;
    }

    public float getTime() {
        return this.time;
    }

    public int getTabNumber() {
        return this.tabNumber;
    }
}

