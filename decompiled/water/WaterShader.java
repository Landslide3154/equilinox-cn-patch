/*
 * Decompiled with CFR 0.152.
 */
package water;

import shaders.ShaderProgram;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformVec2;
import shaders.UniformVec3;
import utils.MyFile;

public class WaterShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("water", "waterVertex.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("water", "waterFragment.glsl");
    protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
    protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
    protected UniformFloat worldRadius = new UniformFloat("worldRadius");
    protected UniformFloat waveTime = new UniformFloat("waveTime");
    protected UniformFloat waterHeight = new UniformFloat("waterHeight");
    protected UniformFloat amplitude = new UniformFloat("amplitude");
    protected UniformFloat fadeOutPeriod = new UniformFloat("fadeOutPeriod");
    protected UniformVec3 lightDirection = new UniformVec3("lightDirection");
    protected UniformVec3 lightColour = new UniformVec3("lightColour");
    protected UniformVec3 cameraPosition = new UniformVec3("cameraPosition");
    protected UniformVec2 worldCenter = new UniformVec2("worldCenter");
    protected UniformVec2 mistValues = new UniformVec2("mistValues");
    protected UniformVec3 mistColour = new UniformVec3("mistColour");

    public WaterShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, new String[0]);
        super.storeAllUniformLocations(this.projectionMatrix, this.viewMatrix, this.worldRadius, this.fadeOutPeriod, this.lightDirection, this.lightColour, this.waveTime, this.cameraPosition, this.waterHeight, this.worldCenter, this.amplitude, this.mistValues, this.mistColour);
    }
}

