/*
 * Decompiled with CFR 0.152.
 */
package sunShafts;

import batches.BlueprintBundle;
import batches.DynamicBatch;
import batches.SubBlueprintBundle;
import blueprints.Blueprint;
import instances.Entity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import sunShafts.ShapeShader;
import toolbox.OpenglUtils;

public class ShapeDynamicRenderer {
    private Matrix4f projectionViewMatrix;
    private ShapeShader shader;

    protected ShapeDynamicRenderer(ShapeShader shader, Matrix4f projectionViewMatrix) {
        this.shader = shader;
        this.projectionViewMatrix = projectionViewMatrix;
    }

    protected void render(DynamicBatch batch) {
        this.shader.projectionView.loadMatrix(this.projectionViewMatrix);
        OpenglUtils.bindVAO(batch.getVao(), 0);
        OpenglUtils.antialias(false);
        OpenglUtils.cullBackFaces(true);
        for (Blueprint blueprint : batch.getData().keySet()) {
            BlueprintBundle bundle = batch.getData().get(blueprint);
            for (SubBlueprintBundle subBundle : bundle.getSubBundles().values()) {
                for (Entity entity : subBundle.getEntities()) {
                    if (!entity.isVisible()) continue;
                    this.prepareInstance(entity);
                    GL11.glDrawArrays(4, subBundle.getStartVertex(), subBundle.getVertexCount());
                }
            }
        }
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f modelMatrix = entity.getTransform().getModelMatrix();
        this.shader.modelMatrix.loadMatrix(modelMatrix);
    }
}

