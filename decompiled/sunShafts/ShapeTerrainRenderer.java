/*
 * Decompiled with CFR 0.152.
 */
package sunShafts;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import sunShafts.ShapeShader;
import terrains.Terrain;
import toolbox.OpenglUtils;
import world.Chunk;

public class ShapeTerrainRenderer {
    private Matrix4f projectionViewMatrix;
    private ShapeShader shader;

    protected ShapeTerrainRenderer(Matrix4f projectionViewMatrix, ShapeShader shader) {
        this.shader = shader;
        this.projectionViewMatrix = projectionViewMatrix;
    }

    protected void render(Chunk[] chunks) {
        this.prepare();
        Chunk[] chunkArray = chunks;
        int n = chunks.length;
        int n2 = 0;
        while (n2 < n) {
            Chunk chunk = chunkArray[n2];
            if (chunk.isVisible()) {
                this.renderTerrain(chunk.getTerrain());
            }
            ++n2;
        }
    }

    private void renderTerrain(Terrain terrain) {
        OpenglUtils.bindVAO(terrain.getVao(), 0);
        GL11.glDrawElements(4, terrain.getIndicesLength(), 5125, 0L);
        OpenglUtils.unbindVAO(0);
    }

    private void prepare() {
        this.shader.projectionView.loadMatrix(this.projectionViewMatrix);
        this.shader.modelMatrix.loadMatrix(new Matrix4f());
        OpenglUtils.antialias(false);
        OpenglUtils.cullBackFaces(true);
    }
}

