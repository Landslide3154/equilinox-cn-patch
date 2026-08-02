/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import extraInfoGui.ExtraInfoContent;
import guiRendering.GuiRenderData;
import org.lwjgl.util.vector.Vector2f;

public class GuiClippingPanel
extends ExtraInfoContent {
    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.setClippingBounds(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void updateSelf() {
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }
}

