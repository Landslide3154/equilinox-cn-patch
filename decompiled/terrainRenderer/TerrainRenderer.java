/*
 * Decompiled with CFR 0.152.
 */
package terrainRenderer;

import basics.CameraInterface;
import basics.DisplayManager;
import basics.EngineMaster;
import basics.MasterRenderer;
import environment.EnvironmentVariables;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;
import terrainRenderer.TerrainShader;
import terrains.Terrain;
import toolbox.Highlight;
import toolbox.OpenglUtils;
import toolbox.WorldHighlights;
import world.Chunk;

public class TerrainRenderer {
    private TerrainShader shader = new TerrainShader();
    private TerrainShader.TerrainShaderHL highlightShader = new TerrainShader.TerrainShaderHL();
    private TerrainShader.TerrainShaderHL2 highlightShader2 = new TerrainShader.TerrainShaderHL2();
    private static final float NORMAL_WIDTH = 0.4f;
    private static final float MIN_RADIUS = 0.7f;
    private static final float NORMAL_WIDTH_ACTUAL = 0.28f;

    public TerrainRenderer() {
        this.initShader(this.shader);
        this.initShader(this.highlightShader);
        this.initShader(this.highlightShader2);
    }

    public void render(Chunk[] chunks, Vector4f clipPlane, boolean waterPass) {
        this.prepare(EngineMaster.getCamera(), clipPlane, waterPass);
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
        this.getCurrentShader().stop();
    }

    public void cleanUp() {
        this.shader.cleanUp();
        this.highlightShader.cleanUp();
        this.highlightShader2.cleanUp();
    }

    private void prepare(CameraInterface camera, Vector4f clipPlane, boolean waterPass) {
        OpenglUtils.cullBackFaces(true);
        OpenglUtils.antialias(!waterPass);
        TerrainShader currentShader = this.getCurrentShader();
        currentShader.start();
        currentShader.mistColour.loadVec3(EnvironmentVariables.MIST_COL.getVector());
        currentShader.mistValues.loadVec2(EnvironmentVariables.MIST_VALS);
        currentShader.clipPlane.loadVec4(clipPlane);
        currentShader.viewMatrix.loadMatrix(camera.getViewMatrix());
        EnvironmentVariables atmosphere = EnvironmentVariables.getVariables();
        currentShader.lightDirection.loadVec3(atmosphere.getLightDirection());
        currentShader.lightColour.loadVec3(atmosphere.getLightColour().getVector());
        currentShader.lightBias.loadVec2(atmosphere.getAmbientWeighting(), atmosphere.getDiffuseWeighting());
        currentShader.time.loadFloat(DisplayManager.getTime());
        this.loadHighlightInfo();
    }

    private void renderTerrain(Terrain terrain) {
        OpenglUtils.bindVAO(terrain.getVao(), 0, 1, 2, 3);
        GL11.glDrawElements(4, terrain.getIndicesLength(), 5125, 0L);
        OpenglUtils.unbindVAO(0, 1, 2, 3);
    }

    private void initShader(TerrainShader shader) {
        shader.start();
        shader.projectionMatrix.loadMatrix(MasterRenderer.getProjectionMatrix());
        shader.worldCenter.loadVec2(50.0f, 50.0f);
        shader.worldRadius.loadFloat(50.0f);
        shader.fadeOutPeriod.loadFloat(10.0f);
        shader.stop();
    }

    private TerrainShader getCurrentShader() {
        WorldHighlights worldHighlights = WorldHighlights.getHighlights();
        if (worldHighlights.getHighlightCount() == 1) {
            return this.highlightShader;
        }
        if (worldHighlights.getHighlightCount() == 2) {
            return this.highlightShader2;
        }
        return this.shader;
    }

    private void loadHighlightInfo() {
        WorldHighlights worldHighlights = WorldHighlights.getHighlights();
        if (worldHighlights.getHighlightCount() == 1) {
            Highlight highlight = worldHighlights.getActiveHighlight();
            this.highlightShader.highlightInfo.loadVec3(highlight.getInfo());
            if (highlight.getInfo().z < 0.7f) {
                this.highlightShader.highlightThickness.loadFloat(1.4f);
            } else {
                float amount = 0.28f / highlight.getInfo().z;
                this.highlightShader.highlightThickness.loadFloat(1.0f + amount);
            }
            this.highlightShader.highlightColour.loadVec3(highlight.getColour().getVector());
        } else if (worldHighlights.getHighlightCount() == 2) {
            Highlight highlight1 = worldHighlights.getHighlight1();
            Highlight highlight2 = worldHighlights.getHighlight2();
            this.highlightShader2.highlightInfo.loadVec3(highlight1.getInfo());
            this.highlightShader2.highlightColour.loadVec3(highlight1.getColour().getVector());
            this.highlightShader2.highlightInfo2.loadVec3(highlight2.getInfo());
            this.highlightShader2.highlightColour2.loadVec3(highlight2.getColour().getVector());
        }
    }
}

