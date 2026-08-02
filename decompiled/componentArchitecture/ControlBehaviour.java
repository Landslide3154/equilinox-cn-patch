/*
 * Decompiled with CFR 0.152.
 */
package componentArchitecture;

import org.lwjgl.input.Keyboard;

public abstract class ControlBehaviour {
    private String name;
    private int key;
    private boolean continuous;

    public ControlBehaviour(String name, int key, boolean continuous) {
        this.name = name;
        this.key = key;
        this.continuous = continuous;
    }

    public String getName() {
        return this.name;
    }

    public String getKeyName() {
        return String.valueOf(Keyboard.getKeyName(this.key)) + " key";
    }

    public int getKey() {
        return this.key;
    }

    public boolean isContinuous() {
        return this.continuous;
    }

    public abstract void doAction();
}

