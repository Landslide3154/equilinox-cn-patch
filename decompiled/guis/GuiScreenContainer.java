/*
 * Decompiled with CFR 0.152.
 */
package guis;

import guiRendering.GuiRenderData;
import guis.GuiComponent;
import org.lwjgl.util.vector.Vector2f;

public class GuiScreenContainer
extends GuiComponent {
    private boolean mouseInGui = false;

    protected GuiScreenContainer() {
        super.forceInitialization(0.0f, 0.0f, 1.0f, 1.0f);
    }

    protected boolean isMouseInGui() {
        return this.mouseInGui;
    }

    @Override
    protected void updateSelf() {
        this.checkMouseOver();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    protected void checkMouseOver() {
        for (GuiComponent childComponent : super.getComponents()) {
            if (!childComponent.isShown() || !childComponent.isMouseOverFocusIrrelevant()) continue;
            this.mouseInGui = true;
            return;
        }
        this.mouseInGui = false;
    }
}

