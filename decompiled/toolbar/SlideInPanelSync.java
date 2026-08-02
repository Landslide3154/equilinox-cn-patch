/*
 * Decompiled with CFR 0.152.
 */
package toolbar;

import guis.GuiComponent;
import toolbar.SlideInPanel;

public class SlideInPanelSync {
    private SlideInPanel currentlyOpen = null;
    private SlideInPanel waitingPanel;
    private GuiComponent waitingContent;

    public void update() {
        if (this.currentlyOpen != null && !this.currentlyOpen.isShown()) {
            this.currentlyOpen = null;
            if (this.waitingPanel != null) {
                this.currentlyOpen = this.waitingPanel;
                this.waitingPanel = null;
                this.currentlyOpen.display(this.waitingContent);
                this.waitingContent = null;
            }
        }
    }

    public void display(SlideInPanel panel, GuiComponent content) {
        if (this.currentlyOpen == null || this.currentlyOpen == panel) {
            panel.display(content);
            this.currentlyOpen = panel;
        } else {
            this.waitingPanel = panel;
            this.waitingContent = content;
            this.currentlyOpen.undisplayPanel();
        }
    }

    public boolean close() {
        if (this.currentlyOpen != null) {
            this.currentlyOpen.undisplayPanel();
            this.currentlyOpen = null;
            this.waitingPanel = null;
            this.waitingContent = null;
            return true;
        }
        return false;
    }

    public boolean hasWaiting() {
        return this.waitingPanel != null;
    }
}

