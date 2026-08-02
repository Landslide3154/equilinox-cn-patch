/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import java.util.ArrayList;
import java.util.List;
import userInterfaces.GuiTextButton;
import userInterfaces.Listener;

public class GuiTextButtonGroup {
    private List<GuiTextButton> buttons = new ArrayList<GuiTextButton>();
    private GuiTextButton currentlyActive;

    public void addButton(final GuiTextButton button, boolean selected) {
        this.buttons.add(button);
        if (selected) {
            this.setSelected(button);
        }
        button.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                GuiTextButtonGroup.this.setSelected(button);
            }
        });
    }

    private void setSelected(GuiTextButton button) {
        button.highlight(true);
        if (this.currentlyActive != null) {
            this.currentlyActive.highlight(false);
        }
        this.currentlyActive = button;
    }
}

