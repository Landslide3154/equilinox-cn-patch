/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import java.util.ArrayList;
import java.util.List;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;

public class GuiClickableGroup {
    private List<GuiClickable> buttons = new ArrayList<GuiClickable>();
    private GuiClickable currentlyActive = null;
    private boolean tabs = false;

    public GuiClickableGroup() {
    }

    public GuiClickableGroup(boolean tabs) {
        this.tabs = tabs;
    }

    public void addButton(GuiClickable button, boolean selected) {
        this.addButton(button);
        if (selected) {
            button.toggle();
            this.currentlyActive = button;
        }
    }

    public void addButton(final GuiClickable button) {
        if (this.tabs) {
            button.disableManualTurnOff();
        }
        this.buttons.add(button);
        button.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    GuiClickableGroup.this.select(button);
                } else if (event.isToggleOff()) {
                    GuiClickableGroup.this.currentlyActive = null;
                }
            }
        });
    }

    public List<GuiClickable> getButtons() {
        return this.buttons;
    }

    public boolean areAllOff() {
        for (GuiClickable button : this.buttons) {
            if (!button.isToggledOn()) continue;
            return false;
        }
        return true;
    }

    private void select(GuiClickable button) {
        this.turnOffCurrentlyActive();
        this.currentlyActive = button;
    }

    public boolean turnOffCurrentlyActive() {
        if (this.currentlyActive != null) {
            this.currentlyActive.toggle();
            this.currentlyActive = null;
            return true;
        }
        return false;
    }
}

