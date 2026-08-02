/*
 * Decompiled with CFR 0.152.
 */
package basics;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

public class Loader {
    private static Map<Integer, List<Integer>> vaoCache = new HashMap<Integer, List<Integer>>();

    public static int createInterleavedVAO(float[] data, int ... lengths) {
        int vertexArrayID = Loader.createVAO();
        Loader.storeInterleavedDataInVAO(vertexArrayID, data, lengths);
        return vertexArrayID;
    }

    public static int createInterleavedVAO(int vertexCount, float[] ... data) {
        int vertexArrayID = Loader.createVAO();
        float[] interleavedData = Loader.interleaveFloatData(vertexCount, data);
        int[] lengths = new int[data.length];
        int i = 0;
        while (i < data.length) {
            lengths[i] = data[i].length / vertexCount;
            ++i;
        }
        Loader.storeInterleavedDataInVAO(vertexArrayID, interleavedData, lengths);
        return vertexArrayID;
    }

    public static int createInterleavedVAO(float[] interleavedData, int[] indices, int ... lengths) {
        int vertexArrayID = Loader.createVAO();
        Loader.createIndicesVBO(vertexArrayID, indices);
        Loader.storeInterleavedDataInVAO(vertexArrayID, interleavedData, lengths);
        return vertexArrayID;
    }

    public static int createVAO() {
        int vertexArrayID = GL30.glGenVertexArrays();
        GL30.glBindVertexArray(vertexArrayID);
        ArrayList associatedVbos = new ArrayList();
        vaoCache.put(vertexArrayID, associatedVbos);
        return vertexArrayID;
    }

    public static int createIndicesVBO(int vaoID, int[] indices) {
        IntBuffer indicesBuffer = BufferUtils.createIntBuffer(indices.length);
        indicesBuffer.put(indices);
        indicesBuffer.flip();
        int indicesBufferId = GL15.glGenBuffers();
        vaoCache.get(vaoID).add(indicesBufferId);
        GL15.glBindBuffer(34963, indicesBufferId);
        GL15.glBufferData(34963, indicesBuffer, 35044);
        return indicesBufferId;
    }

    public static int createInterleavedInstancedVbo(int vaoID, int maxInstanceCount, int startingAttribute, int ... lengths) {
        return Loader.createVbo(vaoID, maxInstanceCount, startingAttribute, true, lengths);
    }

    public static int createEmptyInterleavedVBO(int vaoID, int maxVertexCount, int startingAttribute, int ... lengths) {
        return Loader.createVbo(vaoID, maxVertexCount, startingAttribute, false, lengths);
    }

    public static void storeDataInVbo(int vbo, FloatBuffer buffer, float[] data, int startIndex) {
        buffer.clear();
        buffer.put(data);
        buffer.flip();
        GL15.glBindBuffer(34962, vbo);
        GL15.glBufferSubData(34962, (long)(startIndex * 4), buffer);
        GL15.glBindBuffer(34962, 0);
    }

    public static void refillVboWithData(int vbo, FloatBuffer buffer, float[] data) {
        buffer.clear();
        buffer.put(data);
        buffer.flip();
        GL15.glBindBuffer(34962, vbo);
        GL15.glBufferData(34962, buffer.capacity() * 4, 35048);
        GL15.glBufferSubData(34962, 0L, buffer);
        GL15.glBindBuffer(34962, 0);
    }

    public static void refillVboWithData(int vbo, FloatBuffer buffer, float[] data, int dataLength) {
        buffer.clear();
        buffer.put(data, 0, dataLength);
        buffer.flip();
        GL15.glBindBuffer(34962, vbo);
        GL15.glBufferData(34962, buffer.capacity() * 4, 35048);
        GL15.glBufferSubData(34962, 0L, buffer);
        GL15.glBindBuffer(34962, 0);
    }

    public static void cleanUpModelMemory() {
        GL20.glDisableVertexAttribArray(0);
        GL15.glBindBuffer(34962, 0);
        GL30.glBindVertexArray(0);
        for (int vaoID : vaoCache.keySet()) {
            List<Integer> vbos = vaoCache.get(vaoID);
            for (int vbo : vbos) {
                GL15.glDeleteBuffers(vbo);
            }
            GL30.glDeleteVertexArrays(vaoID);
        }
        vaoCache.clear();
    }

    public static void deleteVaoFromCache(int vao) {
        List<Integer> vbos = vaoCache.remove(vao);
        for (int vbo : vbos) {
            GL15.glDeleteBuffers(vbo);
        }
        GL30.glDeleteVertexArrays(vao);
    }

    public static void storeInterleavedDataInVAO(int vaoID, float[] data, int ... lengths) {
        FloatBuffer interleavedData = Loader.storeDataInBuffer(data);
        int bufferObjectID = GL15.glGenBuffers();
        vaoCache.get(vaoID).add(bufferObjectID);
        GL15.glBindBuffer(34962, bufferObjectID);
        GL15.glBufferData(34962, interleavedData, 35044);
        int total = 0;
        int i = 0;
        while (i < lengths.length) {
            total += lengths[i];
            ++i;
        }
        int vertexByteCount = 4 * total;
        total = 0;
        int i2 = 0;
        while (i2 < lengths.length) {
            GL20.glVertexAttribPointer(i2, lengths[i2], 5126, false, vertexByteCount, 4 * total);
            total += lengths[i2];
            ++i2;
        }
        GL15.glBindBuffer(34962, 0);
        GL30.glBindVertexArray(0);
    }

    public static float[] interleaveFloatData(int count, float[] ... data) {
        int totalSize = 0;
        int[] lengths = new int[data.length];
        int i = 0;
        while (i < data.length) {
            int elementLength;
            lengths[i] = elementLength = data[i].length / count;
            totalSize += data[i].length;
            ++i;
        }
        float[] interleavedBuffer = new float[totalSize];
        int pointer = 0;
        int i2 = 0;
        while (i2 < count) {
            int j = 0;
            while (j < data.length) {
                int elementLength = lengths[j];
                int k = 0;
                while (k < elementLength) {
                    interleavedBuffer[pointer++] = data[j][i2 * elementLength + k];
                    ++k;
                }
                ++j;
            }
            ++i2;
        }
        return interleavedBuffer;
    }

    private static FloatBuffer storeDataInBuffer(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    private static int createVbo(int vaoID, int maxCount, int startingAttribute, boolean instanced, int ... lengths) {
        int bufferObjectID = GL15.glGenBuffers();
        vaoCache.get(vaoID).add(bufferObjectID);
        int total = 0;
        int i = 0;
        while (i < lengths.length) {
            total += lengths[i];
            ++i;
        }
        int vertexByteCount = 4 * total;
        int maxSize = vertexByteCount * maxCount;
        GL30.glBindVertexArray(vaoID);
        GL15.glBindBuffer(34962, bufferObjectID);
        GL15.glBufferData(34962, maxSize, 35048);
        total = 0;
        int i2 = 0;
        while (i2 < lengths.length) {
            GL20.glVertexAttribPointer(i2 + startingAttribute, lengths[i2], 5126, false, vertexByteCount, 4 * total);
            if (instanced) {
                GL33.glVertexAttribDivisor(i2 + startingAttribute, 1);
            }
            total += lengths[i2];
            ++i2;
        }
        GL15.glBindBuffer(34962, 0);
        GL30.glBindVertexArray(0);
        return bufferObjectID;
    }
}

