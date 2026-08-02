/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import basics.EngineMaster;
import frustumCulling.FrustumCuller;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import shadows.ShadowBox;
import toolbox.GridAABB;

public class GridSquare {
    public final int gridId;
    public final int gridX;
    public final int gridZ;
    private GridAABB aabb;
    private boolean visible = true;
    private boolean isShadowed = true;

    public GridSquare(int gridId, int gridCount, float squareWidth) {
        this.gridId = gridId;
        this.gridX = gridId % gridCount;
        this.gridZ = gridId / gridCount;
        this.setUpAabb(squareWidth);
    }

    public GridSquare(int gridX, int gridZ, int gridCount, float squareWidth) {
        this.gridX = gridX;
        this.gridZ = gridZ;
        this.gridId = gridZ * gridCount + gridX;
        this.setUpAabb(squareWidth);
    }

    public void testInFrustum() {
        FrustumCuller culler = EngineMaster.getFrustumCuller();
        this.visible = culler.isInFrustum(this.aabb.mins, this.aabb.maxs);
        ShadowBox shadowCuller = EngineMaster.getShadowBox();
        this.isShadowed = shadowCuller.isInBox(this.aabb.mins, this.aabb.maxs);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isShadowed() {
        return this.isShadowed;
    }

    public Vector3f getCenter() {
        return this.aabb.center;
    }

    public Vector2f[] getFourPoints() {
        float minX = this.aabb.mins.x + 0.25f * this.aabb.getSizeX();
        float minZ = this.aabb.mins.z + 0.25f * this.aabb.getSizeZ();
        float maxX = this.aabb.mins.x + 0.75f * this.aabb.getSizeX();
        float maxZ = this.aabb.mins.z + 0.75f * this.aabb.getSizeZ();
        return new Vector2f[]{new Vector2f(minX, minZ), new Vector2f(minX, maxZ), new Vector2f(maxX, minZ), new Vector2f(maxX, maxZ)};
    }

    public boolean isVisible() {
        return this.visible;
    }

    public Vector3f getTopLeftPosition() {
        return this.aabb.mins;
    }

    protected void testMaxHeight(float height) {
        this.aabb.maxs.y = Math.max(this.aabb.maxs.y, height);
    }

    protected void testMinHeight(float height) {
        this.aabb.mins.y = Math.min(this.aabb.mins.y, height);
    }

    private void setUpAabb(float squareWidth) {
        float minX = (float)this.gridX * squareWidth;
        float minZ = (float)this.gridZ * squareWidth;
        Vector3f mins = new Vector3f(minX, 0.0f, minZ);
        Vector3f maxs = new Vector3f(minX + squareWidth, 0.0f, minZ + squareWidth);
        this.aabb = new GridAABB(mins, maxs);
    }
}

