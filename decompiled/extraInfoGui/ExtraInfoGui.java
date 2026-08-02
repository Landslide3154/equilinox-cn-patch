/*
 * Decompiled with CFR 0.152.
 */
package extraInfoGui;

import basics.DisplayManager;
import extraInfoGui.ExtraFrameGui;
import extraInfoGui.ExtraInfoContent;
import extraInfoGui.ExtraToolbarGui;
import guis.GuiMaster;
import java.util.ArrayList;
import java.util.List;
import mainGuis.EquilinoxGuis;
import mainGuis.EscListener;
import textures.Texture;
import toolTips.ToolTipInfo;
import userInterfaces.Listener;

public class ExtraInfoGui {
    protected static final int WIDTH_PIXELS = DisplayManager.isMinitureWidth() ? 380 : 512;
    protected static final float WIDTH = (float)WIDTH_PIXELS / (float)DisplayManager.getUiWidth();
    protected static final float X_POS = 1.0f - WIDTH;
    private ExtraToolbarGui toolbar;
    private ExtraFrameGui frame;
    private boolean open = false;
    private List<Listener> oneTimeListeners = new ArrayList<Listener>();

    public ExtraInfoGui() {
        this.toolbar = new ExtraToolbarGui(this);
        this.frame = new ExtraFrameGui();
        EquilinoxGuis.addEscListener(new EscListener(){

            @Override
            public boolean escPressed() {
                return ExtraInfoGui.this.close();
            }
        });
    }

    public void update() {
        if (this.open && GuiMaster.clickedOffGui()) {
            this.close();
        }
    }

    public void display(String name, List<Texture> tabIcons, List<ToolTipInfo> toolTips, ExtraInfoContent panelContents) {
        this.open = true;
        this.toolbar.display(name, tabIcons, toolTips);
        this.frame.display(panelContents);
    }

    public void addOneTimeCloseListener(Listener listener) {
        this.oneTimeListeners.add(listener);
    }

    public boolean close() {
        if (this.open) {
            this.open = false;
            this.toolbar.undisplay();
            this.frame.undisplay();
            this.notifyListeners();
            return true;
        }
        return false;
    }

    public ExtraToolbarGui getToolbarGui() {
        return this.toolbar;
    }

    public ExtraFrameGui getContentFrame() {
        return this.frame;
    }

    private void notifyListeners() {
        for (Listener listener : this.oneTimeListeners) {
            listener.eventOccurred(true);
        }
        this.oneTimeListeners.clear();
    }
}

