/*
 * Decompiled with CFR 0.152.
 */
package terrainRenderer;

import shaders.ShaderProgram;
import shaders.UniformBoolean;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import shaders.UniformVec2;
import shaders.UniformVec3;
import utils.MyFile;

public class TerrainHDShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("terrainRenderer", "terrainHDVertex.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("terrainRenderer", "terrainHDFragment.glsl");
    protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
    protected UniformMatrix shadowSpaceMatrix = new UniformMatrix("shadowSpaceMatrix");
    protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
    protected UniformFloat worldRadius = new UniformFloat("worldRadius");
    protected UniformFloat fadeOutPeriod = new UniformFloat("fadeOutPeriod");
    protected UniformVec3 lightDirection = new UniformVec3("lightDirection");
    protected UniformVec3 lightColour = new UniformVec3("lightColour");
    protected UniformVec3 skyColour = new UniformVec3("skyColour");
    protected UniformVec2 lightBias = new UniformVec2("lightingBias");
    protected UniformFloat time = new UniformFloat("time");
    protected UniformSampler shadowMap = new UniformSampler("shadowMap");
    protected UniformVec2 worldCenter = new UniformVec2("worldCenter");
    protected UniformFloat shadowDistance = new UniformFloat("shadowDistance");
    protected UniformBoolean showGrid = new UniformBoolean("showGrid");
    protected UniformVec2 mistValues = new UniformVec2("mistValues");
    protected UniformVec3 mistColour = new UniformVec3("mistColour");
    protected UniformFloat shadowDarkness = new UniformFloat("shadowDarkness");

    public TerrainHDShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, new String[0]);
        super.storeAllUniformLocations(this.projectionMatrix, this.viewMatrix, this.worldRadius, this.fadeOutPeriod, this.lightDirection, this.lightColour, this.lightBias, this.time, this.shadowMap, this.shadowSpaceMatrix, this.worldCenter, this.shadowDistance, this.showGrid, this.skyColour, this.mistValues, this.mistColour, this.shadowDarkness);
    }

    public TerrainHDShader(MyFile vertexShader, MyFile fragmentShader) {
        super(vertexShader, fragmentShader, new String[0]);
    }

    public static class TerrainHDShaderHL
    extends TerrainHDShader {
        private static final MyFile VERTEX_SHADER_HL = new MyFile("terrainRenderer", "terrainHDVertexHL.glsl");
        private static final MyFile FRAGMENT_SHADER_HL = new MyFile("terrainRenderer", "terrainHDFragmentHL.glsl");
        protected UniformVec3 highlightInfo = new UniformVec3("highlightInfo");
        protected UniformFloat highlightThickness = new UniformFloat("highlightThickness");
        protected UniformVec3 highlightColour = new UniformVec3("highlightColour");

        public TerrainHDShaderHL() {
            super(VERTEX_SHADER_HL, FRAGMENT_SHADER_HL);
            super.storeAllUniformLocations(this.projectionMatrix, this.viewMatrix, this.worldRadius, this.fadeOutPeriod, this.lightDirection, this.lightColour, this.lightBias, this.time, this.shadowMap, this.shadowSpaceMatrix, this.worldCenter, this.shadowDistance, this.highlightInfo, this.highlightColour, this.skyColour, this.mistValues, this.mistColour, this.shadowDarkness, this.highlightThickness);
        }
    }

    public static class TerrainHDShaderHL2
    extends TerrainHDShader {
        private static final MyFile VERTEX_SHADER_HL = new MyFile("terrainRenderer", "terrainHDVertexHL.glsl");
        private static final MyFile FRAGMENT_SHADER_HL = new MyFile("terrainRenderer", "terrainHDFragmentHL2.glsl");
        protected UniformVec3 highlightInfo = new UniformVec3("highlightInfo");
        protected UniformVec3 highlightColour = new UniformVec3("highlightColour");
        protected UniformVec3 highlightInfo2 = new UniformVec3("highlightInfo2");
        protected UniformVec3 highlightColour2 = new UniformVec3("highlightColour2");

        public TerrainHDShaderHL2() {
            super(VERTEX_SHADER_HL, FRAGMENT_SHADER_HL);
            super.storeAllUniformLocations(this.projectionMatrix, this.viewMatrix, this.worldRadius, this.fadeOutPeriod, this.lightDirection, this.lightColour, this.lightBias, this.time, this.shadowMap, this.shadowSpaceMatrix, this.worldCenter, this.shadowDistance, this.highlightInfo, this.highlightColour, this.highlightInfo2, this.highlightColour2, this.skyColour, this.mistValues, this.mistColour, this.shadowDarkness);
        }
    }
}

