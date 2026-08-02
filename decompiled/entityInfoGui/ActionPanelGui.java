/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import componentArchitecture.Action;
import entityInfoGui.ActionButtonGui;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;

public class ActionPanelGui
extends GuiComponent {
    private static final float BUTTON_WIDTH = 0.6f;
    private final List<Action> actions;
    private final float yGap;

    public ActionPanelGui(List<Action> actions, int numberOfLines) {
        this.actions = actions;
        this.yGap = 1.0f / (float)numberOfLines;
    }

    @Override
    protected void init() {
        float y = 0.0f;
        for (Action action : this.actions) {
            super.addComponent(new ActionButtonGui(action), 0.19999999f, y + this.yGap / 4.0f, 0.6f, this.yGap);
            y += this.yGap * 1.5f;
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }
}

