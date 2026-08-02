/*
 * Decompiled with CFR 0.152.
 */
package batches;

import batches.MemorySlot;
import batches.StaticMemoryManager;
import componentArchitecture.ComponentType;
import instances.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import materials.MaterialComponent;
import objectPools.Vec3Pool;
import objectPools.Vec4Pool;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Colour;
import toolbox.Maths;

public class StaticBatch {
    public static final int STANDARD_VERTEX_COUNT = 70000;
    public static final int EXTRA_SPACE = 30000;
    private static Vector3f weights = new Vector3f();
    private StaticMemoryManager memoryManager;
    private Map<Entity, MemorySlot> entitySlots = new HashMap<Entity, MemorySlot>();

    public StaticBatch() {
        this.memoryManager = new StaticMemoryManager(70000);
    }

    public StaticBatch(List<Entity> entities, int batchId) {
        if (entities.isEmpty()) {
            this.memoryManager = new StaticMemoryManager(70000);
            return;
        }
        float[][] allEntityData = new float[entities.size()][];
        int floatCount = 0;
        int i = 0;
        while (i < entities.size()) {
            Entity entity = entities.get(i);
            entity.setBatchId(batchId);
            allEntityData[i] = StaticBatch.getInstanceData(entity);
            floatCount += allEntityData[i].length;
            ++i;
        }
        int vertexCount = floatCount / 10;
        int capacity = Math.max(vertexCount + 30000, 70000);
        this.memoryManager = new StaticMemoryManager(capacity);
        MemorySlot[] slots = this.memoryManager.massAllocateMemory(allEntityData);
        int i2 = 0;
        while (i2 < slots.length) {
            this.entitySlots.put(entities.get(i2), slots[i2]);
            ++i2;
        }
    }

    public void delete() {
        this.memoryManager.delete();
        this.entitySlots.clear();
    }

    public int getVao() {
        return this.memoryManager.getVao();
    }

    public boolean isEmpty() {
        return this.getVertexCount() == 0;
    }

    public int getVertexCount() {
        return this.memoryManager.getVertexCount();
    }

    public void removeEntity(Entity entity) {
        MemorySlot slot = this.entitySlots.remove(entity);
        if (slot == null) {
            new Exception().printStackTrace();
        } else {
            this.memoryManager.unmapMemory(slot);
        }
    }

    public void updateEntity(Entity entity) {
        this.removeEntity(entity);
        this.addEntity(entity);
    }

    public void addEntity(Entity entity) {
        float[] entityData = StaticBatch.getInstanceData(entity);
        MemorySlot slot = this.memoryManager.allocateMemory(entityData);
        this.entitySlots.put(entity, slot);
    }

    public boolean defrag() {
        return this.memoryManager.refactor();
    }

    private void massAddEntities(List<Entity> entities, int batchId) {
        if (entities.isEmpty()) {
            return;
        }
        float[][] allEntityData = new float[entities.size()][];
        int floatCount = 0;
        int i = 0;
        while (i < entities.size()) {
            Entity entity = entities.get(i);
            entity.setBatchId(batchId);
            allEntityData[i] = StaticBatch.getInstanceData(entity);
            floatCount += allEntityData[i].length;
            ++i;
        }
        int vertexCount = floatCount / 10;
        System.out.println("VERTEX COUNT: " + vertexCount);
        MemorySlot[] slots = this.memoryManager.massAllocateMemory(allEntityData);
        int i2 = 0;
        while (i2 < slots.length) {
            this.entitySlots.put(entities.get(i2), slots[i2]);
            ++i2;
        }
    }

    private static float[] getInstanceData(Entity entity) {
        float[] blueprintData = entity.getSubBlueprint().getFullModelData();
        float[] entityData = new float[blueprintData.length];
        Matrix4f modelMat = entity.getTransform().getModelMatrix();
        int i = 0;
        while (i < entityData.length / 10) {
            int vertexPointer = i * 10;
            Vector3f position = StaticBatch.getTransformedPosition(vertexPointer, blueprintData, modelMat);
            Vector3f normal = StaticBatch.getTransformedNormal(vertexPointer + 4, blueprintData, modelMat);
            Vector3f colour = StaticBatch.getVector(vertexPointer + 7, blueprintData);
            StaticBatch.customizeColour(colour, entity);
            StaticBatch.storeVectorInArray(position, vertexPointer, entityData);
            entityData[vertexPointer + 3] = blueprintData[vertexPointer + 3];
            StaticBatch.storeVectorInArray(normal, vertexPointer + 4, entityData);
            StaticBatch.storeVectorInArray(colour, vertexPointer + 7, entityData);
            Vec3Pool.release(position, normal, colour);
            ++i;
        }
        return entityData;
    }

    private static void customizeColour(Vector3f colour, Entity entity) {
        if (colour.x >= 0.0f || !entity.hasComponent(ComponentType.MATERIAL)) {
            return;
        }
        MaterialComponent materials = (MaterialComponent)entity.getComponent(ComponentType.MATERIAL);
        Colour materialColour = materials.getMaterial();
        StaticBatch.decodeColourCode((int)colour.y);
        float offset = colour.z;
        colour.x = Maths.clamp(materialColour.getR() + offset * StaticBatch.weights.x, 0.0f, 1.0f);
        colour.y = Maths.clamp(materialColour.getG() + offset * StaticBatch.weights.y, 0.0f, 1.0f);
        colour.z = Maths.clamp(materialColour.getB() + offset * StaticBatch.weights.z, 0.0f, 1.0f);
    }

    private static void decodeColourCode(int code) {
        StaticBatch.weights.x = code % 2;
        code = (code - (int)StaticBatch.weights.x) / 2;
        StaticBatch.weights.y = code % 2;
        code = (code - (int)StaticBatch.weights.y) / 2;
        StaticBatch.weights.z = code;
    }

    private static Vector3f getTransformedNormal(int pointer, float[] data, Matrix4f modelMat) {
        Matrix3f rotMat = Maths.getRotationMatrix(modelMat);
        Vector3f normal = StaticBatch.getVector(pointer, data);
        Matrix3f.transform(rotMat, normal, normal);
        return normal;
    }

    private static Vector3f getTransformedPosition(int pointer, float[] data, Matrix4f modelMat) {
        Vector3f position3f = StaticBatch.getVector(pointer, data);
        Vector4f position4f = Vec4Pool.get(position3f.x, position3f.y, position3f.z, 1.0f);
        Matrix4f.transform(modelMat, position4f, position4f);
        position3f.set(position4f);
        Vec4Pool.release(position4f);
        return position3f;
    }

    private static Vector3f getVector(int pointer, float[] data) {
        float x = data[pointer++];
        float y = data[pointer++];
        float z = data[pointer++];
        return Vec3Pool.get(x, y, z);
    }

    private static void storeVectorInArray(Vector3f vector, int pointer, float[] data) {
        data[pointer++] = vector.x;
        data[pointer++] = vector.y;
        data[pointer++] = vector.z;
    }
}

