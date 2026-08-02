/*
 * Decompiled with CFR 0.152.
 */
package clouds;

import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Maths;

public class CloudBatcher {
    private int totalDataSize;
    private List<float[]> instanceData = new ArrayList<float[]>();

    protected void addModel(float[] modelData, Vector3f pos, float yRot, float scale) {
        Matrix4f modelMatrix = Maths.createModelMatrix(pos, yRot, scale);
        float[] transformedData = this.transformModelData(modelData, modelMatrix);
        this.instanceData.add(transformedData);
        this.totalDataSize += modelData.length;
    }

    protected float[] getInstanceData() {
        return Maths.concatenateArrays(this.instanceData, this.totalDataSize);
    }

    private float[] transformModelData(float[] blueprintData, Matrix4f modelmatrix) {
        float[] entityData = new float[blueprintData.length];
        int i = 0;
        while (i < entityData.length / 10) {
            int vertexPointer = i * 10;
            Vector3f position = CloudBatcher.getTransformedPosition(vertexPointer, blueprintData, modelmatrix);
            Vector3f normal = CloudBatcher.getTransformedNormal(vertexPointer + 4, blueprintData, modelmatrix);
            Vector3f colour = CloudBatcher.getVector(vertexPointer + 7, blueprintData);
            CloudBatcher.storeVectorInArray(position, vertexPointer, entityData);
            entityData[vertexPointer + 3] = blueprintData[vertexPointer + 3];
            CloudBatcher.storeVectorInArray(normal, vertexPointer + 4, entityData);
            CloudBatcher.storeVectorInArray(colour, vertexPointer + 7, entityData);
            ++i;
        }
        return entityData;
    }

    private static Vector3f getTransformedPosition(int pointer, float[] data, Matrix4f modelMat) {
        Vector3f position3f = CloudBatcher.getVector(pointer, data);
        Vector4f position4f = new Vector4f(position3f.x, position3f.y, position3f.z, 1.0f);
        Matrix4f.transform(modelMat, position4f, position4f);
        position3f.set(position4f);
        return position3f;
    }

    private static Vector3f getTransformedNormal(int pointer, float[] data, Matrix4f modelMat) {
        Matrix3f rotMat = Maths.getRotationMatrix(modelMat);
        Vector3f normal = CloudBatcher.getVector(pointer, data);
        Matrix3f.transform(rotMat, normal, normal);
        return normal;
    }

    private static Vector3f getVector(int pointer, float[] data) {
        float x = data[pointer++];
        float y = data[pointer++];
        float z = data[pointer++];
        return new Vector3f(x, y, z);
    }

    private static void storeVectorInArray(Vector3f vector, int pointer, float[] data) {
        data[pointer++] = vector.x;
        data[pointer++] = vector.y;
        data[pointer++] = vector.z;
    }
}

