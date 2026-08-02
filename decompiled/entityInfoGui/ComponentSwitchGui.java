/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import entityInfoGui.InfoType;
import entityInfoGui.PopUpInfoGui;
import userInterfaces.GuiCheckBox;
import userInterfaces.Listener;

public class ComponentSwitchGui
extends PopUpInfoGui {
    private boolean on;
    private Listener listener;

    public ComponentSwitchGui(String name, float font, boolean on, Listener listener) {
        super(name, InfoType.SWITCH, font);
        this.on = on;
        this.listener = listener;
    }

    @Override
    protected void initValueGui() {
        GuiCheckBox guiSwitch = new GuiCheckBox(this.on);
        guiSwitch.addListener(this.listener);
        super.addPixelCompCenterY(guiSwitch, 0.54f, 0.5f);
    }
}

