/*
 * Decompiled with CFR 0.152.
 */
package shadows;

import basics.CameraInterface;
import batches.DynamicBatch;
import environment.EnvironmentVariables;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import shadows.ShadowBox;
import shadows.ShadowFrameBuffer;
import shadows.ShadowMapDynamicRenderer;
import shadows.ShadowMapStaticRenderer;
import shadows.ShadowShader;
import world.Chunk;

public class ShadowMapMasterRenderer {
    private static final int SHADOW_MAP_SIZE = 4096;
    private ShadowFrameBuffer shadowFbo;
    private ShadowShader shader;
    private ShadowBox shadowBox;
    private Matrix4f projectionMatrix = new Matrix4f();
    private Matrix4f lightViewMatrix = new Matrix4f();
    private Matrix4f projectionViewMatrix = new Matrix4f();
    private Matrix4f offset = ShadowMapMasterRenderer.createOffset();
    private ShadowMapStaticRenderer staticRenderer;
    private ShadowMapDynamicRenderer dynamicRenderer;

    public ShadowMapMasterRenderer(CameraInterface camera) {
        this.shader = new ShadowShader();
        this.shadowBox = new ShadowBox(this.lightViewMatrix, camera);
        this.shadowFbo = new ShadowFrameBuffer(4096, 4096);
        this.staticRenderer = new ShadowMapStaticRenderer(this.shader, this.projectionViewMatrix);
        this.dynamicRenderer = new ShadowMapDynamicRenderer(this.shader, this.projectionViewMatrix);
    }

    public void render(Chunk[] chunks, DynamicBatch dynamicBatch) {
        this.prepare(EnvironmentVariables.getVariables().getLightDirection(), this.shadowBox);
        this.staticRenderer.render(chunks, this.shadowBox);
        this.dynamicRenderer.render(dynamicBatch);
        this.finish();
    }

    public void updateShadowBox() {
        this.shadowBox.update();
    }

    public float getShadowDistance() {
        return this.shadowBox.getShadowDistance();
    }

    public Matrix4f getToShadowMapSpaceMatrix() {
        return Matrix4f.mul(this.offset, this.projectionViewMatrix, null);
    }

    public void cleanUp() {
        this.shader.cleanUp();
        this.shadowFbo.cleanUp();
    }

    public ShadowBox getShadowBox() {
        return this.shadowBox;
    }

    public int getShadowMap() {
        return this.shadowFbo.getShadowMap();
    }

    protected Matrix4f getLightSpaceTransform() {
        return this.lightViewMatrix;
    }

    private void prepare(Vector3f lightDirection, ShadowBox box) {
        this.updateOrthoProjectionMatrix(box.getWidth(), box.getHeight(), box.getLength());
        this.updateLightViewMatrix(lightDirection, box.getCenter());
        Matrix4f.mul(this.projectionMatrix, this.lightViewMatrix, this.projectionViewMatrix);
        this.shadowFbo.bindFrameBuffer();
        GL11.glEnable(2929);
        GL11.glClear(256);
        this.shader.start();
    }

    private void finish() {
        this.shader.stop();
        this.shadowFbo.unbindFrameBuffer();
    }

    private void updateLightViewMatrix(Vector3f direction, Vector3f position) {
        direction.normalise();
        position.negate();
        this.lightViewMatrix.setIdentity();
        float h = new Vector2f(direction.x, direction.z).length();
        float pitch = (float)Math.acos(h);
        Matrix4f.rotate(pitch, new Vector3f(1.0f, 0.0f, 0.0f), this.lightViewMatrix, this.lightViewMatrix);
        float yaw = (float)Math.toDegrees((float)Math.atan(direction.x / direction.z));
        if (direction.z > 0.0f) {
            yaw -= 180.0f;
        }
        Matrix4f.rotate((float)(-Math.toRadians(yaw)), new Vector3f(0.0f, 1.0f, 0.0f), this.lightViewMatrix, this.lightViewMatrix);
        Matrix4f.translate(position, this.lightViewMatrix, this.lightViewMatrix);
    }

    private void updateOrthoProjectionMatrix(float width, float height, float length) {
        this.projectionMatrix.setIdentity();
        this.projectionMatrix.m00 = 2.0f / width;
        this.projectionMatrix.m11 = 2.0f / height;
        this.projectionMatrix.m22 = -2.0f / length;
        this.projectionMatrix.m33 = 1.0f;
    }

    private static Matrix4f createOffset() {
        Matrix4f offset = new Matrix4f();
        offset.translate(new Vector3f(0.5f, 0.5f, 0.5f));
        offset.scale(new Vector3f(0.5f, 0.5f, 0.5f));
        return offset;
    }
}

