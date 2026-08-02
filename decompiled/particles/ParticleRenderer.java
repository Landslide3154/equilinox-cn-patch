/*
 * Decompiled with CFR 0.152.
 */
package particles;

import basics.CameraInterface;
import basics.Loader;
import environment.EnvironmentVariables;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import particles.Particle;
import particles.ParticleColourShader;
import particles.ParticleShader;
import particles.ParticleTexture;
import resourceManagement.ParticleAtlasCache;
import toolbox.Colour;
import toolbox.OpenglUtils;

public class ParticleRenderer {
    private static final int MAX_PARTICLES = 10000;
    private static final float[] VERTICES = new float[]{-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
    private static final int INSTANCE_DATA_SIZE = 21;
    private FloatBuffer buffer;
    private int vao;
    private int vbo;
    private ParticleShader shader;
    private ParticleColourShader colourShader;
    private Vector3f lighting = new Vector3f();
    private Vector3f glowLight = new Vector3f(1.0f, 1.0f, 1.0f);
    private int pointer = 0;
    private static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);
    private static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
    private static final Vector3f SCALE = new Vector3f();
    private static final Matrix4f modelMatrix = new Matrix4f();
    private static final Matrix4f modelViewMatrix = new Matrix4f();

    protected ParticleRenderer(Matrix4f projectionMatrix) {
        this.vao = Loader.createInterleavedVAO(VERTICES, 2);
        this.vbo = Loader.createInterleavedInstancedVbo(this.vao, 10000, 1, 4, 4, 4, 4, 4, 1);
        this.buffer = BufferUtils.createFloatBuffer(210000);
        this.initShaders(projectionMatrix);
    }

    protected void render(Map<ParticleTexture, List<Particle>> particles, List<Particle> colourParticles, List<Particle> additiveColourParticles, CameraInterface camera) {
        Matrix4f viewMatrix = camera.getViewMatrix();
        this.prepare();
        this.shader.start();
        for (ParticleTexture texture : particles.keySet()) {
            this.renderTexturedParticles(texture, particles, viewMatrix);
        }
        this.shader.stop();
        this.colourShader.start();
        GL13.glActiveTexture(33984);
        GL11.glBindTexture(3553, ParticleAtlasCache.TRIANGLE.getID());
        this.renderColourParticles(colourParticles, viewMatrix, false);
        this.renderColourParticles(additiveColourParticles, viewMatrix, true);
        this.colourShader.stop();
        this.finishRendering();
    }

    protected void cleanUp() {
        this.shader.cleanUp();
    }

    private void calculateLightingFactor() {
        Vector3f lightDir = EnvironmentVariables.getVariables().getLightDirection();
        float brightness = Math.max(0.0f, -lightDir.y);
        Vector3f diffuse = new Vector3f(EnvironmentVariables.getVariables().getLightColour().getVector());
        diffuse.scale(brightness * EnvironmentVariables.getVariables().getDiffuseWeighting());
        Vector3f ambient = new Vector3f(EnvironmentVariables.getVariables().getLightColour().getVector());
        ambient.scale(EnvironmentVariables.getVariables().getAmbientWeighting());
        Vector3f.add(diffuse, ambient, this.lighting);
    }

    private void renderTexturedParticles(ParticleTexture texture, Map<ParticleTexture, List<Particle>> particles, Matrix4f viewMatrix) {
        List<Particle> particleList = particles.get(texture);
        this.bindTexture(texture);
        int particlesCount = Math.min(particleList.size(), 9999);
        float[] vboData = new float[particlesCount * 21];
        this.pointer = 0;
        int count = 0;
        for (Particle particle : particleList) {
            this.updateModelViewMatrix(particle, viewMatrix, vboData);
            this.updateTexCoordInfo(particle, vboData);
            if (++count == particlesCount) break;
        }
        Loader.refillVboWithData(this.vbo, this.buffer, vboData);
        GL31.glDrawArraysInstanced(5, 0, 4, particlesCount);
    }

    private void renderColourParticles(List<Particle> colourParticles, Matrix4f viewMatrix, boolean additive) {
        if (additive) {
            this.colourShader.lighting.loadVec3(this.glowLight);
            GL11.glBlendFunc(770, 1);
        } else {
            this.colourShader.lighting.loadVec3(this.lighting);
            GL11.glBlendFunc(770, 771);
        }
        int particlesCount = Math.min(colourParticles.size(), 9999);
        float[] vboData = new float[particlesCount * 21];
        this.pointer = 0;
        int count = 0;
        for (Particle particle : colourParticles) {
            this.updateModelViewMatrix(particle, viewMatrix, vboData);
            this.updateColourInfo(particle, vboData);
            vboData[this.pointer++] = particle.getTransparency();
            if (++count == particlesCount) break;
        }
        Loader.refillVboWithData(this.vbo, this.buffer, vboData);
        GL31.glDrawArraysInstanced(5, 0, 4, colourParticles.size());
    }

    private void updateTexCoordInfo(Particle particle, float[] data) {
        data[this.pointer++] = particle.getTexOffset1().x;
        data[this.pointer++] = particle.getTexOffset1().y;
        data[this.pointer++] = particle.getTexOffset2().x;
        data[this.pointer++] = particle.getTexOffset2().y;
        data[this.pointer++] = particle.getBlend();
    }

    private void updateColourInfo(Particle particle, float[] data) {
        Colour colour = particle.getColour();
        data[this.pointer++] = colour.getR();
        data[this.pointer++] = colour.getG();
        data[this.pointer++] = colour.getB();
        data[this.pointer++] = 1.0f;
    }

    private void bindTexture(ParticleTexture texture) {
        this.bindTexture(texture.getTextureID(), texture.isAdditive(), texture.glows());
        this.shader.numberOfRows.loadFloat(texture.getNumberOfRows());
    }

    private void initShaders(Matrix4f projectionMatrix) {
        this.shader = new ParticleShader();
        this.shader.start();
        this.shader.projectionMatrix.loadMatrix(projectionMatrix);
        this.shader.stop();
        this.colourShader = new ParticleColourShader();
        this.colourShader.start();
        this.colourShader.projectionMatrix.loadMatrix(projectionMatrix);
        this.colourShader.stop();
    }

    private void bindTexture(int textureId, boolean additive, boolean glows) {
        if (additive) {
            GL11.glBlendFunc(770, 1);
            this.shader.lighting.loadVec3(this.glowLight);
        } else {
            if (glows) {
                this.shader.lighting.loadVec3(this.glowLight);
            } else {
                this.shader.lighting.loadVec3(this.lighting);
            }
            GL11.glBlendFunc(770, 771);
        }
        GL13.glActiveTexture(33984);
        GL11.glBindTexture(3553, textureId);
    }

    private void updateModelViewMatrix(Particle p, Matrix4f viewMatrix, float[] vboData) {
        modelMatrix.setIdentity();
        Matrix4f.translate(p.getPosition(), modelMatrix, modelMatrix);
        ParticleRenderer.modelMatrix.m00 = viewMatrix.m00;
        ParticleRenderer.modelMatrix.m01 = viewMatrix.m10;
        ParticleRenderer.modelMatrix.m02 = viewMatrix.m20;
        ParticleRenderer.modelMatrix.m10 = viewMatrix.m01;
        ParticleRenderer.modelMatrix.m11 = viewMatrix.m11;
        ParticleRenderer.modelMatrix.m12 = viewMatrix.m21;
        ParticleRenderer.modelMatrix.m20 = viewMatrix.m02;
        ParticleRenderer.modelMatrix.m21 = viewMatrix.m12;
        ParticleRenderer.modelMatrix.m22 = viewMatrix.m22;
        Matrix4f.mul(viewMatrix, modelMatrix, modelViewMatrix);
        Matrix4f.rotate((float)Math.toRadians(p.getRotation()), Z_AXIS, modelViewMatrix, modelViewMatrix);
        Matrix4f.rotate((float)Math.toRadians(p.getRotX()), Y_AXIS, modelViewMatrix, modelViewMatrix);
        float scale = p.getScale();
        SCALE.set(scale, scale, scale);
        Matrix4f.scale(SCALE, modelViewMatrix, modelViewMatrix);
        this.storeMatrixData(modelViewMatrix, vboData);
    }

    private void storeMatrixData(Matrix4f matrix, float[] data) {
        data[this.pointer++] = matrix.m00;
        data[this.pointer++] = matrix.m01;
        data[this.pointer++] = matrix.m02;
        data[this.pointer++] = matrix.m03;
        data[this.pointer++] = matrix.m10;
        data[this.pointer++] = matrix.m11;
        data[this.pointer++] = matrix.m12;
        data[this.pointer++] = matrix.m13;
        data[this.pointer++] = matrix.m20;
        data[this.pointer++] = matrix.m21;
        data[this.pointer++] = matrix.m22;
        data[this.pointer++] = matrix.m23;
        data[this.pointer++] = matrix.m30;
        data[this.pointer++] = matrix.m31;
        data[this.pointer++] = matrix.m32;
        data[this.pointer++] = matrix.m33;
    }

    private void prepare() {
        OpenglUtils.bindVAO(this.vao, 0, 1, 2, 3, 4, 5, 6);
        OpenglUtils.cullBackFaces(false);
        GL11.glEnable(3042);
        GL11.glDepthMask(false);
        this.calculateLightingFactor();
    }

    private void finishRendering() {
        GL11.glDepthMask(true);
        OpenglUtils.cullBackFaces(true);
        GL11.glDisable(3042);
        OpenglUtils.unbindVAO(0, 1, 2, 3, 4, 5, 6);
    }
}

