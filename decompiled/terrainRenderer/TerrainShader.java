/*
 * Decompiled with CFR 0.152.
 */
package terrainRenderer;

import shaders.ShaderProgram;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformVec2;
import shaders.UniformVec3;
import shaders.UniformVec4;
import utils.MyFile;

public class TerrainShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("terrainRenderer", "terrainVertex.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("terrainRenderer", "terrainFragment.glsl");
    protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
    protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
    protected UniformFloat worldRadius = new UniformFloat("worldRadius");
    protected UniformFloat fadeOutPeriod = new UniformFloat("fadeOutPeriod");
    protected UniformVec3 lightDirection = new UniformVec3("lightDirection");
    protected UniformVec3 lightColour = new UniformVec3("lightColour");
    protected UniformVec2 lightBias = new UniformVec2("lightingBias");
    protected UniformVec2 worldCenter = new UniformVec2("worldCenter");
    protected UniformFloat time = new UniformFloat("time");
    protected UniformVec4 clipPlane = new UniformVec4("clipPlane");
    protected UniformVec2 mistValues = new UniformVec2("mistValues");
    protected UniformVec3 mistColour = new UniformVec3("mistColour");

    public TerrainShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, new String[0]);
        super.storeAllUniformLocations(this.projectionMatrix, this.viewMatrix, this.worldRadius, this.fadeOutPeriod, this.lightDirection, this.lightColour, this.lightBias, this.time, this.clipPlane, this.worldCenter, this.mistValues, this.mistColour);
    }

    public TerrainShader(MyFile vertexShader, MyFile fragmentShader) {
        super(vertexShader, fragmentShader, new String[0]);
    }

    public static class TerrainShaderHL
    extends TerrainShader {
        private static final MyFile VERTEX_SHADER_HL = new MyFile("terrainRenderer", "terrainVertexHL.glsl");
        private static final MyFile FRAGMENT_SHADER_HL = new MyFile("terrainRenderer", "terrainFragmentHL.glsl");
        protected UniformVec3 highlightInfo = new UniformVec3("highlightInfo");
        protected UniformVec3 highlightColour = new UniformVec3("highlightColour");
        protected UniformFloat highlightThickness = new UniformFloat("highlightThickness");

        public TerrainShaderHL() {
            super(VERTEX_SHADER_HL, FRAGMENT_SHADER_HL);
            super.storeAllUniformLocations(this.projectionMatrix, this.viewMatrix, this.worldRadius, this.fadeOutPeriod, this.lightDirection, this.lightColour, this.lightBias, this.time, this.clipPlane, this.worldCenter, this.highlightInfo, this.highlightColour, this.mistValues, this.mistColour, this.highlightThickness);
        }
    }

    public static class TerrainShaderHL2
    extends TerrainShader {
        private static final MyFile VERTEX_SHADER_HL = new MyFile("terrainRenderer", "terrainVertexHL.glsl");
        private static final MyFile FRAGMENT_SHADER_HL = new MyFile("terrainRenderer", "terrainFragmentHL2.glsl");
        protected UniformVec3 highlightInfo = new UniformVec3("highlightInfo");
        protected UniformVec3 highlightColour = new UniformVec3("highlightColour");
        protected UniformVec3 highlightInfo2 = new UniformVec3("highlightInfo2");
        protected UniformVec3 highlightColour2 = new UniformVec3("highlightColour2");

        public TerrainShaderHL2() {
            super(VERTEX_SHADER_HL, FRAGMENT_SHADER_HL);
            super.storeAllUniformLocations(this.projectionMatrix, this.viewMatrix, this.worldRadius, this.fadeOutPeriod, this.lightDirection, this.lightColour, this.lightBias, this.time, this.clipPlane, this.worldCenter, this.highlightInfo, this.highlightColour, this.highlightInfo2, this.highlightColour2, this.mistValues, this.mistColour);
        }
    }
}

