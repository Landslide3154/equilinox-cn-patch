/*
 * Decompiled with CFR 0.152.
 */
package picking;

import java.nio.ByteBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class PboDataDownloader {
    private static final int BYTES_PER_PIXEL = 4;
    private int[] pbos;
    private final int width;
    private final int height;
    private final int byteCount;
    private int currentPbo = 0;
    private boolean pbosFilled = false;
    private ByteBuffer buffer;
    private byte[] result;

    public PboDataDownloader(int width, int height, int pboCount) {
        this.width = width;
        this.height = height;
        this.byteCount = this.calculateByteCount();
        this.buffer = BufferUtils.createByteBuffer(this.byteCount);
        this.result = new byte[this.byteCount];
        this.emptyResult();
        this.initPbos(pboCount);
    }

    public byte[] downloadData(int x, int y) {
        GL15.glBindBuffer(35051, this.pbos[this.currentPbo]);
        if (this.pbosFilled) {
            this.readDataFromPbo();
        }
        GL11.glReadPixels(x, y, this.width, this.height, 6408, 5121, 0L);
        GL15.glBindBuffer(35051, 0);
        this.moveToNextPbo();
        return this.result;
    }

    public void reset() {
        this.emptyResult();
        this.pbosFilled = false;
        this.currentPbo = 0;
    }

    public void cleanUp() {
        GL15.glBindBuffer(35051, 0);
        int[] nArray = this.pbos;
        int n = this.pbos.length;
        int n2 = 0;
        while (n2 < n) {
            int pbo = nArray[n2];
            GL15.glDeleteBuffers(pbo);
            ++n2;
        }
    }

    private void emptyResult() {
        int i = 0;
        while (i < this.result.length) {
            this.result[i] = -1;
            ++i;
        }
    }

    private void initPbos(int count) {
        this.pbos = new int[count];
        int i = 0;
        while (i < this.pbos.length) {
            this.pbos[i] = this.createPbo();
            ++i;
        }
    }

    private void readDataFromPbo() {
        this.buffer = GL15.glMapBuffer(35051, 35000, this.buffer);
        this.buffer.get(this.result);
        GL15.glUnmapBuffer(35051);
        this.buffer.flip();
    }

    private int createPbo() {
        int pbo = GL15.glGenBuffers();
        GL15.glBindBuffer(35051, pbo);
        GL15.glBufferData(35051, this.byteCount, 35041);
        GL15.glBindBuffer(35051, 0);
        return pbo;
    }

    private int calculateByteCount() {
        int pixels = this.width * this.height;
        return pixels * 4;
    }

    private void moveToNextPbo() {
        ++this.currentPbo;
        if (this.currentPbo == this.pbos.length) {
            this.currentPbo = 0;
            this.pbosFilled = true;
        }
    }
}

