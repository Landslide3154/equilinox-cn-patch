/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import gameManaging.GameManager;
import gameManaging.GameState;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class MyKeyboard {
    public static final int ENTER = 13;
    public static final int BACK = 8;
    public static final int SPACE = 32;
    private static final MyKeyboard ACTIVE_KEYBOARD = new MyKeyboard();
    private List<Integer> keysDownThisFrame = new ArrayList<Integer>();
    private List<Integer> keysUpThisFrame = new ArrayList<Integer>();
    private List<Integer> charsThisFrame = new ArrayList<Integer>();
    private List<Integer> keysDown = new ArrayList<Integer>();
    private boolean blocked = true;
    private boolean lock = false;

    private MyKeyboard() {
    }

    public static MyKeyboard getKeyboard() {
        return ACTIVE_KEYBOARD;
    }

    public void block(boolean blocked) {
        this.lock = blocked;
    }

    /*
     * Unable to fully structure code
     */
    public void update() {
        this.blocked = GameManager.getGameState() == GameState.SPLASH_SCREEN || this.lock != false;
        this.keysDownThisFrame.clear();
        this.keysUpThisFrame.clear();
        this.charsThisFrame.clear();
        if (Display.isActive()) ** GOTO lbl25
        this.keysDown.clear();
        return;
lbl-1000:
        // 1 sources

        {
            key = Keyboard.getEventKey();
            if (Keyboard.getEventKeyState()) {
                this.keysDownThisFrame.add(key);
                ascii = this.getValidAsciiValue(Keyboard.getEventCharacter());
                if (key == 14 || key == 211) {
                    ascii = 8;
                }
                if (ascii != 0) {
                    this.charsThisFrame.add(ascii);
                }
                this.keysDown.add(key);
                continue;
            }
            this.keysUpThisFrame.add(key);
            this.keysDown.remove((Object)key);
lbl25:
            // 3 sources

            ** while (Keyboard.next())
        }
lbl26:
        // 1 sources

    }

    public boolean isKeyDown(int key) {
        if (this.keysDown.contains(key)) {
            return !this.blocked;
        }
        return false;
    }

    public boolean keyDownEventOccurred(Integer key) {
        if (this.keysDownThisFrame.contains(key)) {
            return !this.blocked;
        }
        return false;
    }

    public boolean keyUpEventOccurred(Integer key) {
        if (this.keysUpThisFrame.contains(key)) {
            return !this.blocked;
        }
        return false;
    }

    public boolean isKeyDownIgnoreBlock(int key) {
        return this.keysDown.contains(key);
    }

    public boolean keyDownEventOccurredIgnoreBlock(Integer key) {
        return this.keysDownThisFrame.contains(key);
    }

    public boolean keyUpEventOccurredIgnoreBlock(Integer key) {
        return this.keysUpThisFrame.contains(key);
    }

    public List<Integer> getChars() {
        return this.charsThisFrame;
    }

    private int getValidAsciiValue(char c) {
        char ascii = c;
        if (this.isSpecialChar(ascii) || this.isLetter(ascii) || this.isNumber(ascii)) {
            return ascii;
        }
        return 0;
    }

    private boolean isSpecialChar(int ascii) {
        return ascii == 32 || ascii == 8 || ascii == 13;
    }

    private boolean isLetter(int ascii) {
        return ascii >= 65 && ascii <= 90 || ascii >= 97 && ascii <= 122;
    }

    private boolean isNumber(int ascii) {
        return ascii >= 48 && ascii <= 57;
    }
}

