/*
 * Decompiled with CFR 0.152.
 */
package speciesInformation;

import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.GuiImage;

public class TierGui
extends GuiComponent {
    private static final float Y_SCALE = 0.8f;
    private static final float X_GAP = 0.1f;
    private final int tier;

    public TierGui(int tier) {
        this.tier = tier;
    }

    @Override
    protected void init() {
        if (this.tier <= 0) {
            return;
        }
        int i = 0;
        while (i < this.tier) {
            GuiImage image = new GuiImage(GuiRepository.NEW);
            super.addCenteredComponentYScaleY(image, 0.5f, (float)i * 0.1f, 0.8f);
            ++i;
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

