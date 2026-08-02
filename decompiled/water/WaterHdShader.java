/*
 * Decompiled with CFR 0.152.
 */
package water;

import shaders.ShaderProgram;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformSampler;
import shaders.UniformVec2;
import shaders.UniformVec3;
import utils.MyFile;

public class WaterHdShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("water", "waterHdVertex.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("water", "waterHdFragment.glsl");
    protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
    protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
    protected UniformFloat worldRadius = new UniformFloat("worldRadius");
    protected UniformFloat waveTime = new UniformFloat("waveTime");
    protected UniformFloat waterHeight = new UniformFloat("waterHeight");
    protected UniformFloat fadeOutPeriod = new UniformFloat("fadeOutPeriod");
    protected UniformVec3 lightDirection = new UniformVec3("lightDirection");
    protected UniformVec3 lightColour = new UniformVec3("lightColour");
    protected UniformVec3 cameraPosition = new UniformVec3("cameraPosition");
    protected UniformSampler refractionTexture = new UniformSampler("refractionTexture");
    protected UniformSampler depthTexture = new UniformSampler("depthTexture");
    protected UniformSampler reflectionTexture = new UniformSampler("reflectionTexture");
    protected UniformVec2 frustumNearFar = new UniformVec2("frustumNearFar");
    protected UniformVec2 worldCenter = new UniformVec2("worldCenter");
    protected UniformFloat amplitude = new UniformFloat("amplitude");
    protected UniformVec3 skyColour = new UniformVec3("skyColour");
    protected UniformVec2 mistValues = new UniformVec2("mistValues");
    protected UniformVec3 mistColour = new UniformVec3("mistColour");

    public WaterHdShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, new String[0]);
        super.storeAllUniformLocations(this.projectionMatrix, this.viewMatrix, this.worldRadius, this.fadeOutPeriod, this.lightDirection, this.lightColour, this.waveTime, this.cameraPosition, this.waterHeight, this.refractionTexture, this.depthTexture, this.reflectionTexture, this.frustumNearFar, this.worldCenter, this.amplitude, this.skyColour, this.mistValues, this.mistColour);
    }
}

