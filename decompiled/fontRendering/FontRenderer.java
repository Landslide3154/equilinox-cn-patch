/*
 * Decompiled with CFR 0.152.
 */
package fontRendering;

import fontRendering.FontShader;
import fontRendering.FontType;
import fontRendering.Text;
import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.OpenglUtils;

public class FontRenderer {
    private FontShader shader = new FontShader();

    public void render(Map<FontType, List<Text>> texts) {
        this.prepare();
        for (FontType font : texts.keySet()) {
            OpenglUtils.bindTextureToBank(font.getTextureAtlas(), 0);
            for (Text text : texts.get((Object)font)) {
                this.renderText(text);
            }
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
    }

    private void endRendering() {
        this.shader.stop();
        OpenglUtils.disableBlending();
        OpenglUtils.enableDepthTesting();
    }

    private void renderText(Text text) {
        if (text.isEmpty()) {
            return;
        }
        OpenglUtils.bindVAO(text.getMesh(), 0, 1);
        Vector2f position = text.getPosition();
        this.setScissorTest(text.getClippingBounds());
        this.shader.transform.loadVec3(position.x, position.y, text.getScale());
        Colour colour = text.getColour();
        this.shader.colour.loadVec4(colour.getR(), colour.getG(), colour.getB(), text.getTransparency());
        this.shader.borderColour.loadVec3(text.getBorderColour().getVector());
        this.shader.edgeData.loadVec2(text.calculateEdgeStart(), text.calculateAntialiasSize());
        this.shader.borderSizes.loadVec2(text.getTotalBorderSize(), text.getGlowSize());
        GL11.glDrawArrays(4, 0, text.getVertexCount());
        OpenglUtils.unbindVAO(0, 1);
    }

    private void setScissorTest(int[] bounds) {
        if (bounds == null) {
            OpenglUtils.disableScissorTest();
        } else {
            OpenglUtils.enableScissorTest(bounds[0], bounds[1], bounds[2], bounds[3]);
        }
    }
}

