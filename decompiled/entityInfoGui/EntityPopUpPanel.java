/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import entityInfoGui.EntityInfoGui;
import toolbox.Colour;
import userInterfaces.GuiPanel;

public abstract class EntityPopUpPanel
extends GuiPanel {
    public EntityPopUpPanel(Colour colour, float alpha) {
        super(colour, alpha);
    }

    public abstract void addToParentPanel(EntityInfoGui var1);

    public abstract float getMaxY();

    public abstract float getMinY();
}

