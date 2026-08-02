/*
 * Decompiled with CFR 0.152.
 */
package picking;

import basics.DisplayManager;
import basics.EngineMaster;
import basics.MasterRenderer;
import componentArchitecture.ComponentType;
import instances.Entity;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import openglObjects.Attribute;
import openglObjects.Vao;
import openglObjects.Vbo;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Matrix4f;
import picking.Box;
import picking.Fbo;
import picking.FboBuilder;
import picking.PboDataDownloader;
import picking.PickingShader;
import toolbox.Colour;
import toolbox.OpenglUtils;
import world.GridSection;

public class Picker3D {
    private static final float DETAILED_RANGE = 20.0f;
    private static final float OUT_OF_RANGE = 78.0f;
    private static final int MAX_INSTANCES = 7000;
    private static final int INSTANCE_DATA_LENGTH = 68;
    private static final Colour WHITE = new Colour(1.0f, 1.0f, 1.0f);
    private static final int MAX_BYTE_VAL = 255;
    private static final float[] VERTICES = new float[]{-0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f};
    private static final int[] INDICES;
    private static final int FBO_DOWNSCALE = 8;
    private static final int PBO_COUNT = 3;
    private PickingShader shader;
    private Vao vao;
    private Vbo vbo;
    private Fbo fbo;
    private Entity entity;
    private Matrix4f projectionView = new Matrix4f();
    private Matrix4f mvpMatrix = new Matrix4f();
    private byte[] colour = new byte[4];
    private ByteBuffer instanceData = BufferUtils.createByteBuffer(476000);
    private ByteBuffer tempData = ByteBuffer.allocate(476000).order(ByteOrder.nativeOrder());
    private int instanceCount;
    private PboDataDownloader downloader;
    private static int F1;
    private static int F2;

    static {
        int[] nArray = new int[30];
        nArray[1] = 1;
        nArray[2] = 3;
        nArray[3] = 3;
        nArray[4] = 1;
        nArray[5] = 2;
        nArray[6] = 3;
        nArray[7] = 2;
        nArray[8] = 7;
        nArray[9] = 7;
        nArray[10] = 2;
        nArray[11] = 6;
        nArray[12] = 7;
        nArray[13] = 6;
        nArray[14] = 4;
        nArray[15] = 4;
        nArray[16] = 6;
        nArray[17] = 5;
        nArray[18] = 4;
        nArray[19] = 5;
        nArray[22] = 5;
        nArray[23] = 1;
        nArray[24] = 4;
        nArray[26] = 7;
        nArray[27] = 7;
        nArray[29] = 3;
        INDICES = nArray;
        F1 = 256;
        F2 = F1 * 256;
    }

    public Picker3D() {
        this.vao = Vao.create();
        this.vao.bind();
        FloatBuffer buffer = BufferUtils.createFloatBuffer(VERTICES.length);
        buffer.put(VERTICES);
        buffer.flip();
        this.vao.initDataFeed(buffer, 35044, new Attribute(0, 5126, 3));
        IntBuffer indexBuffer = BufferUtils.createIntBuffer(INDICES.length);
        indexBuffer.put(INDICES);
        indexBuffer.flip();
        this.vao.createIndexBuffer(indexBuffer);
        this.vbo = this.vao.createDataFeed(7000, 35048, new Attribute(1, 5126, 4, false, true), new Attribute(2, 5126, 4, false, true), new Attribute(3, 5126, 4, false, true), new Attribute(4, 5126, 4, false, true), new Attribute(5, 5121, 4, true, true));
        this.vao.unbind();
        this.fbo = Fbo.newFbo(DisplayManager.getWidth() / 8, DisplayManager.getHeight() / 8).nearestFiltering().setDepthBuffer(FboBuilder.DepthBufferType.RENDER_BUFFER).create();
        this.shader = new PickingShader();
        this.downloader = new PboDataDownloader(1, 1, 3);
    }

    public int getFboTexture() {
        return this.fbo.getColourTexture();
    }

    public void update(Map<Integer, Entity> worldEntities) {
        this.getInstanceData(worldEntities);
        this.instanceData.clear();
        this.instanceData.put(this.tempData.array(), 0, this.tempData.position());
        this.instanceData.flip();
        this.renderInstances();
        byte[] result = this.readPixelColour();
        if (this.noColourFound(result)) {
            this.entity = null;
        } else {
            this.pickEntity(result, worldEntities);
        }
    }

    public Entity getPickedEntity() {
        return this.entity;
    }

    public void reset() {
        this.downloader.reset();
    }

    public void cleanUp() {
        this.vao.delete(true);
        this.downloader.cleanUp();
    }

    private void getInstanceData(Map<Integer, Entity> worldEntities) {
        this.tempData.clear();
        this.instanceCount = 0;
        for (Entity entity : worldEntities.values()) {
            if (!entity.hasComponent(ComponentType.INFO) || !entity.isVisible()) continue;
            Box[] extraBoxes = null;
            GridSection section = entity.getCurrentGridSection();
            if (section != null && section.getDistanceFromCam() < 20.0f) {
                extraBoxes = entity.getExtraBoundingBoxes();
            } else if (section != null && section.getDistanceFromCam() > 78.0f) continue;
            if (extraBoxes != null) {
                Box[] boxArray = extraBoxes;
                int n = extraBoxes.length;
                int n2 = 0;
                while (n2 < n) {
                    Box box = boxArray[n2];
                    this.storeEntityData(entity, box, this.tempData);
                    ++n2;
                }
                continue;
            }
            this.storeEntityData(entity, entity.getBoundingBox(), this.tempData);
        }
    }

    private boolean noColourFound(byte[] colour) {
        return Picker3D.convertUnsignedByte(colour[0]) == 255;
    }

    private void storeEntityData(Entity entity, Box boundingBox, ByteBuffer allData) {
        if (allData.remaining() < 68) {
            return;
        }
        Picker3D.encodeIdIntoColour(entity.getId(), this.colour);
        this.loadMvpMatrix(boundingBox, allData);
        allData.put(this.colour);
        ++this.instanceCount;
    }

    private byte[] readPixelColour() {
        this.fbo.bindToRead();
        byte[] res = this.downloader.downloadData(Mouse.getX() / 8, Mouse.getY() / 8);
        this.fbo.unbindFrameBuffer();
        return res;
    }

    private void renderInstances() {
        this.prepare();
        this.vbo.refill(this.instanceData);
        GL31.glDrawElementsInstanced(4, INDICES.length, 5125, 0L, this.instanceCount);
        this.endRendering();
    }

    private void pickEntity(byte[] result, Map<Integer, Entity> worldEntities) {
        int index = Picker3D.decodeIdFromColour(result);
        this.entity = worldEntities.get(index);
        if (this.entity == null) {
            System.out.println(String.valueOf(index) + " gave no entity!");
        }
    }

    private static int convertUnsignedByte(byte b) {
        return b & 0xFF;
    }

    private static void encodeIdIntoColour(int id, byte[] colour) {
        colour[3] = -1;
        colour[2] = (byte)(id % 256);
        colour[1] = (byte)((id /= 256) % 256);
        colour[0] = (byte)((id /= 256) % 256);
    }

    private static int decodeIdFromColour(byte[] colour) {
        int id = Picker3D.convertUnsignedByte(colour[2]);
        id += Picker3D.convertUnsignedByte(colour[1]) * F1;
        return id += Picker3D.convertUnsignedByte(colour[0]) * F2;
    }

    private void prepare() {
        this.fbo.bindFrameBuffer();
        OpenglUtils.prepareNewRenderPass(WHITE);
        this.vao.bind();
        this.calculateProjectionViewMatrix();
        this.shader.start();
    }

    private void endRendering() {
        this.shader.stop();
        this.fbo.unbindFrameBuffer();
        this.vao.unbind();
    }

    private void calculateProjectionViewMatrix() {
        Matrix4f projection = MasterRenderer.getProjectionMatrix();
        Matrix4f view = EngineMaster.getCamera().getViewMatrix();
        Matrix4f.mul(projection, view, this.projectionView);
    }

    private void loadMvpMatrix(Box box, ByteBuffer allData) {
        Matrix4f modelMat = box.getModelMatrix();
        Matrix4f.mul(this.projectionView, modelMat, this.mvpMatrix);
        allData.putFloat(this.mvpMatrix.m00);
        allData.putFloat(this.mvpMatrix.m01);
        allData.putFloat(this.mvpMatrix.m02);
        allData.putFloat(this.mvpMatrix.m03);
        allData.putFloat(this.mvpMatrix.m10);
        allData.putFloat(this.mvpMatrix.m11);
        allData.putFloat(this.mvpMatrix.m12);
        allData.putFloat(this.mvpMatrix.m13);
        allData.putFloat(this.mvpMatrix.m20);
        allData.putFloat(this.mvpMatrix.m21);
        allData.putFloat(this.mvpMatrix.m22);
        allData.putFloat(this.mvpMatrix.m23);
        allData.putFloat(this.mvpMatrix.m30);
        allData.putFloat(this.mvpMatrix.m31);
        allData.putFloat(this.mvpMatrix.m32);
        allData.putFloat(this.mvpMatrix.m33);
    }
}

