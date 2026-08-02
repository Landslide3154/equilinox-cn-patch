/*
 * Decompiled with CFR 0.152.
 */
package iconGenerator;

import basics.Loader;
import blueprints.Blueprint;
import blueprints.SubBlueprint;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import iconGenerator.IconShader;
import java.nio.FloatBuffer;
import mainGuis.ColourPalette;
import materials.MaterialComponent;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import postProcessing.Fbo;
import resourceManagement.BlueprintRepository;
import textures.Texture;
import toolbox.Colour;
import toolbox.Maths;
import toolbox.OpenglUtils;

public class IconRenderer {
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 100.0f;
    private static final int MAX_VERTICES = 2000;
    private static final Vector3f LIGHT_DIR = new Vector3f(0.0f, -0.5f, -1.0f);
    private static final Vector2f LIGHT_BIAS = new Vector2f(0.4f, 0.7f);
    private static final Vector3f CAM_POS = new Vector3f(0.0f, 8.0f, 20.0f);
    private static final float PITCH = 10.0f;
    private static final float ENTITY_SCALE = 10.0f;
    private Fbo fbo;
    private Fbo resultFbo;
    private int vao;
    private int vbo;
    private IconShader shader;
    private Matrix4f modelMatrix = new Matrix4f();
    private Matrix4f viewMatrix = new Matrix4f();
    private Matrix4f projectionViewMatrix = new Matrix4f();
    private Matrix4f projectionMatrix = new Matrix4f();
    private FloatBuffer floatBuffer = BufferUtils.createFloatBuffer(20000);

    public IconRenderer(int width, int height) {
        this.fbo = Fbo.newFbo(width, height).antialias(4).withAlphaChannel(true).create();
        this.resultFbo = Fbo.newFbo(width, height).withAlphaChannel(true).create();
        this.vao = Loader.createVAO();
        this.vbo = Loader.createEmptyInterleavedVBO(this.vao, 2000, 0, 4, 3, 3);
        this.shader = new IconShader();
        this.loadLighting();
        this.projectionMatrix = Maths.createProjectionMatrix(width, height, 0.1f, 100.0f, 35.0f);
    }

    public void generateBlueprintIcons() {
        this.initialize();
        for (Blueprint blueprint : BlueprintRepository.getAllBlueprints()) {
            this.doIcon(blueprint);
        }
        this.finish();
    }

    public Texture getColourIcon(Blueprint blueprint, Colour colour) {
        this.initialize();
        Texture tex = this.renderIcon(blueprint, colour);
        this.finish();
        return tex;
    }

    private void doIcon(Blueprint blueprint) {
        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)blueprint.getComponent(ComponentType.INFO);
        if (info == null) {
            return;
        }
        if (info.getIcon() == null) {
            Texture texture = this.renderIcon(blueprint, null);
            info.setIcon(texture);
        }
    }

    public void cleanUp() {
        this.fbo.cleanUp();
        this.resultFbo.cleanUp();
        this.shader.cleanUp();
    }

    private Texture renderIcon(Blueprint blueprint, Colour colour) {
        this.prepareForIconRender();
        int vertexCount = this.storeMeshData(blueprint);
        if (colour == null) {
            this.loadMaterial(blueprint);
        } else {
            this.loadColour(colour);
        }
        this.updateTransformMatrices(blueprint);
        OpenglUtils.bindVAO(this.vao, 0, 1, 2);
        GL11.glDrawArrays(4, 0, vertexCount);
        this.resultFbo.bindFrameBuffer();
        this.resultFbo.createTextureAttachment(true, true);
        this.fbo.resolveMultisampledFbo(this.resultFbo);
        Texture texture = Texture.getEmptyTexture();
        texture.setTextureID(this.resultFbo.getColourTexture());
        return texture;
    }

    private void initialize() {
        this.shader.start();
        OpenglUtils.antialias(true);
        OpenglUtils.cullBackFaces(true);
    }

    private void prepareForIconRender() {
        this.fbo.bindFrameBuffer();
        OpenglUtils.prepareNewRenderParse(ColourPalette.DARKER_GREEN, 0.0f);
    }

    private int storeMeshData(Blueprint blueprint) {
        SubBlueprint model = blueprint.getMainSubBlueprint();
        Loader.refillVboWithData(this.vbo, this.floatBuffer, model.getFullModelData());
        return model.getVertexCount();
    }

    private void loadMaterial(Blueprint blueprint) {
        MaterialComponent.MaterialCompBlueprint mats = (MaterialComponent.MaterialCompBlueprint)blueprint.getComponent(ComponentType.MATERIAL);
        if (mats != null) {
            Colour material = mats.getExampleNaturalColour();
            this.loadColour(material);
        }
    }

    private void loadColour(Colour material) {
        this.shader.material.loadVec3(material.getVector());
    }

    private void finish() {
        this.shader.stop();
        this.fbo.unbindFrameBuffer();
        this.resultFbo.unbindFrameBuffer();
    }

    private void updateTransformMatrices(Blueprint blueprint) {
        float scale = 10.0f / blueprint.getIconMaxSize();
        Maths.updateModelMatrix(this.modelMatrix, new Vector3f(0.0f, blueprint.getIconY(), 0.0f), 0.0f, -45.0f, 0.0f, new Vector3f(scale, scale, scale));
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
}

