/*
 * Decompiled with CFR 0.152.
 */
package sunShafts;

import environment.EnvironmentVariables;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import postProcessing.PostProcessingFilter;
import radialBlur.RadialBlur;
import sunShafts.ShapeMapMasterRenderer;
import world.Chunk;

public class SunShaftMaster {
    protected static final int DOWNSCALE_FACTOR = 2;
    private ShapeMapMasterRenderer shapeMapRenderer = new ShapeMapMasterRenderer();
    private PostProcessingFilter radialBlur = new RadialBlur(Display.getWidth() / 2, Display.getHeight() / 2);

    public void renderSunShafts(Chunk[] chunks) {
        Vector2f sunScreenPos = EnvironmentVariables.getVariables().getSunScreenPosition();
        if (!EnvironmentVariables.getVariables().isSunVisible()) {
            return;
        }
        this.shapeMapRenderer.render(chunks, sunScreenPos);
        this.radialBlur.applyFilter(false, this.shapeMapRenderer.getShapeTexture());
    }

    public int getTexture() {
        return this.radialBlur.getOutputTexture();
    }

    public void cleanUp() {
        this.radialBlur.cleanUp();
        this.shapeMapRenderer.cleanUp();
    }
}

