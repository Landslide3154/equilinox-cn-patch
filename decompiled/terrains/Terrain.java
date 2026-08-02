/*
 * Decompiled with CFR 0.152.
 */
package terrains;

import basics.Loader;
import gameManaging.GameManager;
import glRequestProcessing.GlRequest;
import glRequestProcessing.GlRequestProcessor;
import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import terrains.TerrainLoader;
import terrains.TerrainVertex;
import toolbox.Colour;

public class Terrain {
    public static final int GRID_COUNT = 27;
    public static final int VERTEX_COUNT = 28;
    public static final float SIZE = 20.0f;
    public static final float GRID_SQUARE_SIZE = 0.7407407f;
    private static final FloatBuffer BUFFER = BufferUtils.createFloatBuffer(5488);
    private int vao;
    private int colourVbo;
    private int indicesLength;
    private TerrainVertex[][] terrainVertices;
    private boolean loaded = false;
    private boolean dirty = true;

    public static Terrain createTerrain(final int gridX, final int gridZ, final float[][] heights, final Vector3f[][] normals, Colour[][] backgroundColours) {
        final Terrain terrain = new Terrain(backgroundColours);
        GlRequestProcessor.sendRequest(new GlRequest(){

            @Override
            public void executeGlRequest() {
                TerrainLoader.loadTerrain(terrain, gridX, gridZ, heights, normals);
            }
        });
        return terrain;
    }

    public int getVao() {
        return this.vao;
    }

    public int getIndicesLength() {
        return this.indicesLength;
    }

    public TerrainVertex getVertex(int x, int z) {
        return this.terrainVertices[z][x];
    }

    public void setDirty() {
        this.dirty = true;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void update() {
        if (this.dirty) {
            GameManager.getSession().getSceneData().updateTerrain(this);
            this.dirty = false;
        }
    }

    public void delete() {
        this.loaded = false;
        this.terrainVertices = null;
        GlRequestProcessor.sendRequest(new GlRequest(){

            @Override
            public void executeGlRequest() {
                Loader.deleteVaoFromCache(Terrain.this.vao);
            }
        });
    }

    public void updateVboData() {
        float[] interleavedData = this.getColourData();
        Loader.refillVboWithData(this.colourVbo, BUFFER, interleavedData);
        this.dirty = false;
    }

    protected void setVboData(int vao, int vbo, int indicesLength) {
        this.vao = vao;
        this.colourVbo = vbo;
        this.indicesLength = indicesLength;
        this.updateVboData();
        this.loaded = true;
    }

    private Terrain(Colour[][] backgroundColours) {
        this.createTerrainVertices(backgroundColours);
    }

    private void createTerrainVertices(Colour[][] colours) {
        this.terrainVertices = new TerrainVertex[colours.length][colours.length];
        int z = 0;
        while (z < colours.length) {
            int x = 0;
            while (x < colours.length) {
                this.terrainVertices[z][x] = new TerrainVertex(colours[z][x], false);
                ++x;
            }
            ++z;
        }
    }

    private float[] getColourData() {
        float[] data = new float[5488];
        int pointer = 0;
        int z = 0;
        while (z < this.terrainVertices.length) {
            int x = 0;
            while (x < this.terrainVertices.length) {
                pointer = this.addVertexData(data, pointer, this.terrainVertices[z][x]);
                ++x;
            }
            ++z;
        }
        return data;
    }

    private int addVertexData(float[] data, int pointer, TerrainVertex vertex) {
        Colour targetColour = vertex.getTargetColour();
        Colour lastKnownColour = vertex.getLastKnownColour();
        data[pointer++] = targetColour.getR();
        data[pointer++] = targetColour.getG();
        data[pointer++] = targetColour.getB();
        data[pointer++] = lastKnownColour.getR();
        data[pointer++] = lastKnownColour.getG();
        data[pointer++] = lastKnownColour.getB();
        data[pointer++] = vertex.getLastUpdateTime();
        return pointer;
    }
}

