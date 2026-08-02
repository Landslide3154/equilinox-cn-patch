/*
 * Decompiled with CFR 0.152.
 */
package entityRenderers;

import basics.CameraInterface;
import basics.MasterRenderer;
import batches.BlueprintBundle;
import batches.DynamicBatch;
import batches.SubBlueprintBundle;
import blueprints.Blueprint;
import componentArchitecture.ComponentType;
import entityRenderers.EntityShader;
import environment.EnvironmentVariables;
import instances.Entity;
import instances.Tinter;
import materials.MaterialComponent;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;
import toolbox.Colour;
import toolbox.OpenglUtils;

public class EntityRenderer {
    private EntityShader shader = new EntityShader();

    public EntityRenderer() {
        this.shader.start();
        this.initShader();
        this.shader.stop();
    }

    public void render(DynamicBatch batch, CameraInterface camera, Vector4f clipPlane, boolean hdRender) {
        this.prepare(camera, clipPlane, hdRender);
        OpenglUtils.bindVAO(batch.getVao(), 0, 1, 2);
        for (Blueprint blueprint : batch.getData().keySet()) {
            this.prepareBlueprint(blueprint);
            BlueprintBundle bundle = batch.getData().get(blueprint);
            for (SubBlueprintBundle subBundle : bundle.getSubBundles().values()) {
                for (Entity entity : subBundle.getEntities()) {
                    if (!entity.isVisible()) continue;
                    this.prepareInstance(entity);
                    GL11.glDrawArrays(4, subBundle.getStartVertex(), subBundle.getVertexCount());
                    this.finishInstance(entity);
                }
            }
        }
        OpenglUtils.disableBlending();
        OpenglUtils.unbindVAO(0, 1, 2);
        this.shader.stop();
    }

    public void cleanUp() {
        this.shader.cleanUp();
    }

    private void prepare(CameraInterface camera, Vector4f clipPlane, boolean hdRender) {
        OpenglUtils.cullBackFaces(true);
        OpenglUtils.antialias(hdRender);
        OpenglUtils.enableAlphaBlending();
        this.shader.start();
        this.shader.mistColour.loadVec3(EnvironmentVariables.MIST_COL.getVector());
        this.shader.mistValues.loadVec2(EnvironmentVariables.MIST_VALS);
        this.shader.clipPlane.loadVec4(clipPlane);
        this.shader.viewMatrix.loadMatrix(camera.getViewMatrix());
        EnvironmentVariables atmosphere = EnvironmentVariables.getVariables();
        this.shader.lightDirection.loadVec3(atmosphere.getLightDirection());
        this.shader.lightColour.loadVec3(atmosphere.getLightColour().getVector());
        this.shader.lightBias.loadVec2(atmosphere.getAmbientWeighting(), atmosphere.getDiffuseWeighting());
    }

    private void prepareBlueprint(Blueprint blueprint) {
    }

    private void finishInstance(Entity entity) {
        if (entity.getTinter().hasTint()) {
            this.shader.tintColour.loadVec4(1.0f, 1.0f, 1.0f, 0.0f);
        }
    }

    private void initShader() {
        this.shader.projectionMatrix.loadMatrix(MasterRenderer.getProjectionMatrix());
        this.shader.worldCenter.loadVec2(50.0f, 50.0f);
        this.shader.worldRadius.loadFloat(50.0f);
        this.shader.fadeOutPeriod.loadFloat(10.0f);
    }

    private void prepareInstance(Entity entity) {
        Tinter tinter;
        this.shader.alpha.loadFloat(entity.getAlpha());
        MaterialComponent materials = (MaterialComponent)entity.getComponent(ComponentType.MATERIAL);
        if (materials != null) {
            this.shader.material.loadVec3(materials.getMaterial().getVector());
        }
        if ((tinter = entity.getTinter()).hasTint()) {
            Colour tint = tinter.getColour();
            this.shader.tintColour.loadVec4(tint.getR(), tint.getG(), tint.getB(), tinter.getAlpha());
        }
        Matrix4f modelMatrix = entity.getTransform().getModelMatrix();
        this.shader.modelMatrix.loadMatrix(modelMatrix);
    }
}

