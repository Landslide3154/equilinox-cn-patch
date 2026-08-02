/*
 * Decompiled with CFR 0.152.
 */
package world;

import batches.StaticBatch;
import gameManaging.GameManager;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import terrains.Terrain;
import toolbox.GridSquare;
import toolbox.Maths;
import world.ClosestPointFinder;

public class Chunk
extends GridSquare {
    public static final float SIZE = 20.0f;
    public static final int VERTEX_COUNT = 28;
    private static final float MAX_ENTITY_HEIGHT = 1.0f;
    private final Terrain terrain;
    private final int normalBatchId;
    private final int clutterBatchId;
    private ClosestPointFinder closestPoint;
    private float distanceFromCamera;

    public Chunk(int chunkId, Terrain terrain, float[][] heights) {
        super(chunkId, 5, 20.0f);
        this.normalBatchId = chunkId;
        this.clutterBatchId = chunkId + 25;
        this.terrain = terrain;
        Vector3f topLeft = super.getTopLeftPosition();
        this.closestPoint = new ClosestPointFinder(new Vector2f(topLeft.x, topLeft.z), 20.0f);
        this.findMinMaxHeights(heights);
    }

    public void update() {
        super.testInFrustum();
        this.distanceFromCamera = this.closestPoint.getDistance();
    }

    public boolean isClutterVisible() {
        return super.isVisible() && this.distanceFromCamera <= 30.0f;
    }

    public float getClutterAlpha() {
        float transitionStart = 25.5f;
        float amountInside = (this.distanceFromCamera - transitionStart) / 4.5f;
        amountInside = Maths.clamp(amountInside, 0.0f, 1.0f);
        return 1.0f - amountInside;
    }

    public Terrain getTerrain() {
        return this.terrain;
    }

    public StaticBatch getStaticBatch() {
        return GameManager.getSession().getSceneData().getStaticBatch(this.normalBatchId);
    }

    public StaticBatch getClutterBatch() {
        return GameManager.getSession().getSceneData().getStaticBatch(this.clutterBatchId);
    }

    private void findMinMaxHeights(float[][] heights) {
        int startZ;
        int startX = 27 * this.gridX;
        int z = startZ = 27 * this.gridZ;
        while (z < startZ + 28) {
            int x = startX;
            while (x < startX + 28) {
                this.testMaxHeight(heights[z][x] + 1.0f);
                this.testMinHeight(heights[z][x]);
                ++x;
            }
            ++z;
        }
    }
}

