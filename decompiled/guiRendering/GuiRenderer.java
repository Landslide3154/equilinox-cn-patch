/*
 * Decompiled with CFR 0.152.
 */
package guiRendering;

import basics.Loader;
import basics.MasterRenderer;
import guiRendering.GuiShader;
import guis.GuiTexture;
import org.lwjgl.opengl.GL11;
import toolbox.OpenglUtils;

public class GuiRenderer {
    private static final float[] POSITIONS = new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f};
    private GuiShader shader = new GuiShader();
    private int vao = Loader.createInterleavedVAO(POSITIONS, 2);

    public void render(Iterable<GuiTexture> guis) {
        this.prepare();
        OpenglUtils.bindTextureToBank(MasterRenderer.getOutputTexture(), 1);
        for (GuiTexture gui : guis) {
            this.renderGui(gui);
        }
        this.endRendering();
    }

    public void cleanUp() {
        this.shader.cleanUp();
    }

    private void prepare() {
        OpenglUtils.antialias(false);
        OpenglUtils.enableAlphaBlending();
        OpenglUtils.disableDepthTesting();
        OpenglUtils.cullBackFaces(true);
        this.shader.start();
        OpenglUtils.bindVAO(this.vao, 0);
    }

    private void renderGui(GuiTexture gui) {
        if (!gui.getTexture().isLoaded()) {
            return;
        }
        OpenglUtils.bindTextureToBank(gui.getTexture().getID(), 0);
        this.setScissorTest(gui.getClippingBounds());
        if (gui.isAdditive()) {
            OpenglUtils.enableAdditiveBlending();
        }
        this.shader.alpha.loadFloat(gui.getAlpha());
        this.shader.usesBlur.loadBoolean(gui.usesBlur());
        this.shader.flipTexture.loadBoolean(gui.isFlipTexture());
        this.shader.transform.loadVec4(gui.getPosition().x, gui.getPosition().y, gui.getScale().x, gui.getScale().y);
        this.shader.useOverrideColour.loadBoolean(gui.hasOverrideColour());
        if (gui.hasOverrideColour()) {
            this.shader.overrideColour.loadVec3(gui.getOverrideColour().getVector());
        }
        GL11.glDrawArrays(5, 0, 4);
        if (gui.isAdditive()) {
            OpenglUtils.enableAlphaBlending();
        }
    }

    private void endRendering() {
        OpenglUtils.disableScissorTest();
        OpenglUtils.unbindVAO(0);
        this.shader.stop();
        OpenglUtils.disableBlending();
        OpenglUtils.enableDepthTesting();
    }

    private void setScissorTest(int[] bounds) {
        if (bounds == null) {
            OpenglUtils.disableScissorTest();
        } else {
            OpenglUtils.enableScissorTest(bounds[0], bounds[1], bounds[2], bounds[3]);
        }
    }
}

