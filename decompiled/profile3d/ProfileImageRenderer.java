/*
 * Decompiled with CFR 0.152.
 */
package profile3d;

import basics.Loader;
import blueprints.Blueprint;
import blueprints.SubBlueprint;
import componentArchitecture.ComponentType;
import java.nio.FloatBuffer;
import mainGuis.ColourPalette;
import materials.MaterialComponent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import postProcessing.Fbo;
import profile3d.ProfileShader;
import toolbox.Colour;
import toolbox.Maths;
import toolbox.OpenglUtils;

public class ProfileImageRenderer {
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 100.0f;
    private static final int MAX_VERTICES = 2000;
    private static final float CYCLE_TIME = 2.0f;
    private static final Vector3f LIGHT_DIR = new Vector3f(0.0f, -0.5f, -1.0f);
    private static final Vector2f LIGHT_BIAS = new Vector2f(0.4f, 0.7f);
    private static final Vector3f CAM_POS = new Vector3f(0.0f, 8.0f, 20.0f);
    private static final float PITCH = 10.0f;
    private static final float ENTITY_SCALE = 10.0f;
    private Fbo fbo;
    private Fbo resultFbo;
    private int vao;
    private int vbo;
    private float rotation = 0.0f;
    private float scale = 1.0f;
    private ProfileShader shader;
    private Matrix4f modelMatrix = new Matrix4f();
    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f projectionViewMatrix = new Matrix4f();
    private Matrix4f projectionMatrix = new Matrix4f();
    private FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(20000);
    private SubBlueprint model;
    private Blueprint blueprint;
    private Colour material;
    private int startingVertex = 0;

    public ProfileImageRenderer(int width, int height) {
        this.fbo = Fbo.newFbo(width, height).antialias(4).withAlphaChannel(true).create();
        this.resultFbo = Fbo.newFbo(width, height).withAlphaChannel(true).create();
        this.vao = Loader.createVAO();
        this.vbo = Loader.createEmptyInterleavedVBO(this.vao, 2000, 0, 4, 3, 3);
        this.shader = new ProfileShader();
        this.loadLighting();
        this.createProjectionMatrix(width, height);
    }

    public void changeModel(Blueprint blueprint) {
        this.blueprint = blueprint;
        this.model = blueprint.getMainSubBlueprint();
        Loader.refillVboWithData(this.vbo, this.floatBuffer, this.model.getFullModelData());
        this.setMaterial();
        this.scale = 10.0f / blueprint.getMaxSize();
        this.startingVertex = 0;
    }

    public int getTexture() {
        return this.resultFbo.getColourTexture();
    }

    public void increaseRotation(float dRot) {
        this.rotation += dRot;
    }

    public void update() {
        this.prepare();
        this.updateTransformMatrices();
        GL11.glDrawArrays(4, this.startingVertex, this.model.getVertexCount());
        this.unbind();
    }

    public void cleanUp() {
        this.fbo.cleanUp();
        this.resultFbo.cleanUp();
        this.shader.cleanUp();
    }

    private void prepare() {
        this.fbo.bindFrameBuffer();
        OpenglUtils.prepareNewRenderParse(ColourPalette.DARK_GREY, 0.0f);
        OpenglUtils.antialias(true);
        OpenglUtils.cullBackFaces(true);
        this.shader.start();
        OpenglUtils.bindVAO(this.vao, 0, 1, 2);
    }

    private void setMaterial() {
        MaterialComponent.MaterialCompBlueprint mats = (MaterialComponent.MaterialCompBlueprint)this.blueprint.getComponent(ComponentType.MATERIAL);
        if (mats != null) {
            this.material = mats.getExampleNaturalColour();
            this.shader.start();
            this.shader.material.loadVec3(this.material.getVector());
            this.shader.stop();
        }
    }

    private void unbind() {
        OpenglUtils.unbindVAO(this.vao);
        this.shader.stop();
        this.fbo.unbindFrameBuffer();
        this.fbo.resolveMultisampledFbo(this.resultFbo);
    }

    private void updateTransformMatrices() {
        Maths.updateModelMatrix(this.modelMatrix, new Vector3f(0.0f, 0.0f, 0.0f), 0.0f, this.rotation, 0.0f, new Vector3f(this.scale, this.scale, this.scale));
        Maths.createViewMatrix(this.viewMatrix, CAM_POS, 10.0f, 0.0f);
        Matrix4f.mul(this.projectionMatrix, this.viewMatrix, this.projectionViewMatrix);
        this.shader.modelMatrix.loadMatrix(this.modelMatrix);
        this.shader.projectionViewMatrix.loadMatrix(this.projectionViewMatrix);
    }

    private void loadLighting() {
        this.shader.start();
        this.shader.lightBias.loadVec2(LIGHT_BIAS);
        this.shader.lightColour.loadVec3(1.0f, 1.0f, 1.0f);
        this.shader.lightDirection.loadVec3(LIGHT_DIR);
        this.shader.stop();
    }

    private void createProjectionMatrix(float width, float height) {
        float farPlane = 100.0f;
        float nearPlane = 0.1f;
        float aspectRatio = width / height;
        float y_scale = (float)(1.0 / Math.tan(Math.toRadians(17.5)));
        float x_scale = y_scale / aspectRatio;
        float frustum_length = farPlane - nearPlane;
        this.projectionMatrix.m00 = x_scale;
        this.projectionMatrix.m11 = y_scale;
        this.projectionMatrix.m22 = -((farPlane + nearPlane) / frustum_length);
        this.projectionMatrix.m23 = -1.0f;
        this.projectionMatrix.m32 = -(2.0f * nearPlane * farPlane / frustum_length);
        this.projectionMatrix.m33 = 0.0f;
    }
}

