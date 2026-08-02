/*
 * Decompiled with CFR 0.152.
 */
package radialBlur;

import environment.EnvironmentVariables;
import org.lwjgl.util.vector.Vector2f;
import postProcessing.PostProcessingFilter;
import radialBlur.RadialBlurShader;
import shaders.ShaderProgram;

public class RadialBlur
extends PostProcessingFilter {
    public RadialBlur(int width, int height) {
        super(new RadialBlurShader(), width, height, false);
    }

    @Override
    protected void prepareShader(ShaderProgram shader) {
        Vector2f sunCoords = EnvironmentVariables.getVariables().getSunScreenPosition();
        if (sunCoords != null) {
            ((RadialBlurShader)shader).lightPositionOnScreen.loadVec2(sunCoords.x, 1.0f - sunCoords.y);
        }
    }
}

