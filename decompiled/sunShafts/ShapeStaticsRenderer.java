/*
 * Decompiled with CFR 0.152.
 */
package sunShafts;

import batches.StaticBatch;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import sunShafts.ShapeShader;
import toolbox.OpenglUtils;
import world.Chunk;

public class ShapeStaticsRenderer {
    private Matrix4f projectionViewMatrix;
    private ShapeShader shader;

    protected ShapeStaticsRenderer(ShapeShader shader, Matrix4f projectionViewMatrix) {
        this.shader = shader;
        this.projectionViewMatrix = projectionViewMatrix;
    }

    protected void render(Chunk[] chunks) {
        this.shader.projectionView.loadMatrix(this.projectionViewMatrix);
        this.shader.modelMatrix.loadMatrix(new Matrix4f());
        OpenglUtils.antialias(false);
        OpenglUtils.cullBackFaces(true);
        Chunk[] chunkArray = chunks;
        int n = chunks.length;
        int n2 = 0;
        while (n2 < n) {
            Chunk chunk = chunkArray[n2];
            if (chunk.isVisible()) {
                this.renderBatch(chunk.getStaticBatch());
            }
            ++n2;
        }
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    private void renderBatch(StaticBatch batch) {
        OpenglUtils.bindVAO(batch.getVao(), 0);
        GL11.glDrawArrays(4, 0, batch.getVertexCount());
    }
}

