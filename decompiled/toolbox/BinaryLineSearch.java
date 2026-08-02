/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import gameManaging.GameManager;
import org.lwjgl.util.vector.Vector3f;

public class BinaryLineSearch {
    private final Vector3f abovePoint;
    private final Vector3f ray = new Vector3f();
    private final int recursion;
    private final float aimheight;

    public BinaryLineSearch(Vector3f abovePoint, Vector3f belowPoint, int recursion, float aimHeight) {
        this.abovePoint = abovePoint;
        Vector3f.sub(belowPoint, abovePoint, this.ray);
        this.recursion = recursion;
        this.aimheight = aimHeight;
    }

    public Vector3f doSearch() {
        Vector3f samplePos = this.binarySearch(0, 0.0f, 1.0f);
        samplePos.y = GameManager.getWorld().getHeightOfTerrain(samplePos.x, samplePos.z);
        return samplePos;
    }

    private Vector3f getPointOnRay(float distance) {
        Vector3f scaledRay = new Vector3f(this.ray.x * distance, this.ray.y * distance, this.ray.z * distance);
        return Vector3f.add(this.abovePoint, scaledRay, null);
    }

    private Vector3f binarySearch(int count, float start, float finish) {
        float half = start + (finish - start) / 2.0f;
        if (count >= this.recursion) {
            return this.getPointOnRay(half);
        }
        if (this.intersectionInRange(start, half)) {
            return this.binarySearch(++count, start, half);
        }
        return this.binarySearch(++count, half, finish);
    }

    private boolean intersectionInRange(float start, float finish) {
        Vector3f startPoint = this.getPointOnRay(start);
        Vector3f endPoint = this.getPointOnRay(finish);
        return !this.isUnderHeight(startPoint) && this.isUnderHeight(endPoint);
    }

    private boolean isUnderHeight(Vector3f testPoint) {
        float terrainHeight = GameManager.getWorld().getHeightOfTerrain(testPoint.x, testPoint.z);
        return terrainHeight < this.aimheight;
    }
}

