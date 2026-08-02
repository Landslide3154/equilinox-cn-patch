/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import java.util.ArrayList;
import java.util.List;
import userInterfaces.GuiButton;
import userInterfaces.Listener;

public class GuiButtonGroup {
    private List<GuiButton> buttons = new ArrayList<GuiButton>();
    private GuiButton currentlyActive = null;
    private boolean tabs = false;

    public GuiButtonGroup() {
    }

    public GuiButtonGroup(boolean tabs) {
        this.tabs = tabs;
    }

    public void addButton(GuiButton button, boolean selected) {
        this.addButton(button);
        if (selected) {
            button.toggle();
        }
    }

    public void addButton(final GuiButton button) {
        if (this.tabs) {
            button.disableManualTurnOff();
        }
        this.buttons.add(button);
        button.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                if (on) {
                    GuiButtonGroup.this.select(button);
                } else {
                    GuiButtonGroup.this.currentlyActive = null;
                }
            }
        });
    }

    public List<GuiButton> getButtons() {
        return this.buttons;
    }

    private void select(GuiButton button) {
        this.turnOffCurrentlyActive();
        this.currentlyActive = button;
    }

    public void turnOffCurrentlyActive() {
        if (this.currentlyActive != null) {
            this.currentlyActive.toggle();
            this.currentlyActive = null;
        }
    }

    public boolean areAllOff() {
        for (GuiButton b : this.buttons) {
            if (!b.isToggledOn()) continue;
            return false;
        }
        return true;
    }
}

