/*
 * Decompiled with CFR 0.152.
 */
package entityRenderers;

import shaders.ShaderProgram;
import shaders.UniformFloat;
import shaders.UniformMatrix;
import shaders.UniformVec2;
import shaders.UniformVec3;
import shaders.UniformVec4;
import utils.MyFile;

public class StaticShader
extends ShaderProgram {
    private static final MyFile VERTEX_SHADER = new MyFile("entityRenderers", "staticVertex.glsl");
    private static final MyFile FRAGMENT_SHADER = new MyFile("entityRenderers", "staticFragment.glsl");
    protected UniformMatrix projectionMatrix = new UniformMatrix("projectionMatrix");
    protected UniformMatrix viewMatrix = new UniformMatrix("viewMatrix");
    protected UniformFloat worldRadius = new UniformFloat("worldRadius");
    protected UniformFloat fadeOutPeriod = new UniformFloat("fadeOutPeriod");
    protected UniformFloat alpha = new UniformFloat("alpha");
    protected UniformFloat time = new UniformFloat("time");
    protected UniformVec3 lightDirection = new UniformVec3("lightDirection");
    protected UniformVec3 lightColour = new UniformVec3("lightColour");
    protected UniformVec2 lightBias = new UniformVec2("lightingBias");
    protected UniformVec4 clipPlane = new UniformVec4("clipPlane");
    protected UniformVec2 worldCenter = new UniformVec2("worldCenter");
    protected UniformVec2 mistValues = new UniformVec2("mistValues");
    protected UniformVec3 mistColour = new UniformVec3("mistColour");

    public StaticShader() {
        super(VERTEX_SHADER, FRAGMENT_SHADER, new String[0]);
        super.storeAllUniformLocations(this.projectionMatrix, this.viewMatrix, this.worldRadius, this.fadeOutPeriod, this.lightDirection, this.lightColour, this.lightBias, this.clipPlane, this.worldCenter, this.alpha, this.time, this.mistValues, this.mistColour);
    }
}

