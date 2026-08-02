/*
 * Decompiled with CFR 0.152.
 */
package world;

import biomes.AmbienceController;
import biomes.BiomeGrid;
import biomes.SpreaderCompBlueprint;
import blueprints.Blueprint;
import classification.Classification;
import classification.Classifier;
import clouds.CloudFactory;
import clouds.CloudManager;
import componentArchitecture.ComponentType;
import dataManagement.DataUpdateManager;
import edgeCovering.EdgeManager;
import entityBundle.EntityBundle;
import extra.IntWrap;
import instances.Entity;
import java.io.IOException;
import main.Camera;
import org.lwjgl.util.vector.Vector3f;
import session.EntityLoad;
import terrains.HeightFinder;
import terrains.Terrain;
import terrains.TerrainVertex;
import toolbox.Colour;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;
import water.Water;
import world.Chunk;
import world.EntitiesGrid;
import world.GridIterator;
import world.GridSection;
import world.PerSectionCode;
import world.UnplaceableReason;
import world.WorldConfigs;
import world.WorldLoader;

public class World {
    public static final float GRAVITY = 10.0f;
    private static final Classification ROCKS = Classifier.getClassification("erl");
    private static final Classification HIVES = Classifier.getClassification("es57");
    public static final Colour BACK_COLOUR = new Colour(111.0f, 80.0f, 45.0f, true);
    public static final int CHUNK_COUNT = 5;
    public static final int CHUNK_BATCH_COUNT = 2;
    public static final int NUM_CHUNKS = 25;
    public static final int NUM_STATIC_BATCHES = 50;
    public static final int DESIRED_VERTEX_COUNT = 138;
    public static final int WORLD_VERTEX_COUNT = 136;
    public static final float SIZE = 100.0f;
    public static final float FADE_OUT_PERIOD = 10.0f;
    public static final float CLUTTER_MAX_DIS = 30.0f;
    public static final float MAX_ALTITUDE = 8.0f;
    public static final float CLUTTER_TRANS_FACTOR = 0.15f;
    public static final float CLUTTER_TRANS_PERIOD = 4.5f;
    private final WorldConfigs configs;
    private Terrain[][] terrains;
    private Chunk[] chunks;
    private Water water;
    private HeightFinder heightFinder;
    private EntitiesGrid entityGrid;
    private BiomeGrid grid;
    private AmbienceController ambienceController;
    private CloudManager clouds;
    private EdgeManager edge;

    public static World generateWorld(WorldConfigs configs) {
        World world = WorldLoader.generateWorld(configs);
        return world;
    }

    public static World loadWorld(BinaryReader file, EntityLoad entities) {
        World world = WorldLoader.loadWorld(file, entities);
        return world;
    }

    protected World(WorldConfigs configs, Terrain[][] terrains, float[][] heights, EntityLoad entities) {
        this.terrains = terrains;
        this.configs = configs;
        this.heightFinder = new HeightFinder(heights, 100.0f, configs.getWaterHeight() + 0.5f);
        this.entityGrid = entities != null ? new EntitiesGrid(entities, heights, entities.getNextEntityId()) : new EntitiesGrid(heights);
        this.water = new Water(configs.getWaterHeight(), 100.0f, this.heightFinder);
        this.initChunks(heights);
        this.grid = new BiomeGrid(this);
        this.ambienceController = new AmbienceController(this.grid);
    }

    public boolean isOnWorld(Vector3f point) {
        return point.x >= 0.0f && point.x < 100.0f && point.z >= 0.0f && point.z < 100.0f;
    }

    public void addInstance(Entity entity, boolean urgent) {
        this.entityGrid.addInstance(entity, urgent);
        this.updateBiome(entity, true);
    }

    public void removeEntity(Entity entity, boolean urgent) {
        this.entityGrid.removeEntity(entity, urgent);
        this.updateBiome(entity, false);
    }

    public void createClouds(DataUpdateManager sceneData) {
        this.clouds = CloudFactory.create(sceneData, 100.0f);
        this.edge = new EdgeManager(sceneData, this.configs.getWaterHeight() + 0.5f, 100.0f);
    }

    public EntitiesGrid getEntityGrid() {
        return this.entityGrid;
    }

    public Chunk[] getChunks() {
        return this.chunks;
    }

    public Water getWater() {
        return this.water;
    }

    public float getSize() {
        return 100.0f;
    }

    public WorldConfigs getConfigs() {
        return this.configs;
    }

    public void delete() {
        this.water.delete();
        int i = 0;
        while (i < this.terrains.length) {
            int j = 0;
            while (j < this.terrains[i].length) {
                this.terrains[i][j].delete();
                ++j;
            }
            ++i;
        }
        this.terrains = null;
        this.entityGrid.delete();
        this.ambienceController.stopAll();
    }

    public void update() {
        this.entityGrid.update();
        this.updateTerrains();
        this.updateChunks();
        this.grid.update();
        this.clouds.update();
        this.edge.update();
    }

    public void updateAmbientSounds() {
        this.ambienceController.update(Camera.getCamera().getListenerPosition());
    }

    public boolean isLoaded() {
        boolean loaded = this.water != null && this.water.isLoaded();
        loaded &= this.terrainsLoaded();
        return loaded &= this.entityGrid != null;
    }

    public void export(BinaryWriter writer) throws IOException {
        this.entityGrid.export(writer);
        this.configs.export(writer);
    }

    public UnplaceableReason isAcceptableLocation(Blueprint object, float x, float z) {
        float y = this.getHeightOfTerrain(x, z);
        if (!object.canBeUnderwater() && y < this.configs.getWaterHeight() - object.getAcceptableHeightOffset()) {
            return UnplaceableReason.NEEDS_DRY_LAND;
        }
        if (!object.canBeOverwater() && y > this.configs.getWaterHeight() + object.getAcceptableHeightOffset()) {
            return UnplaceableReason.NEEDS_WATER;
        }
        GridSection square = this.getEntityGrid().getSectionAtPosition(x, z);
        if (square == null || x < 0.5f || x > 99.5f || z < 0.5f || z > 99.5f) {
            return UnplaceableReason.OFF_WORLD;
        }
        int i = square.gridX - 1;
        while (i <= square.gridX + 1) {
            int j = square.gridZ - 1;
            while (j <= square.gridZ + 1) {
                GridSection gridSquare = this.getEntityGrid().getSection(i, j);
                if (gridSquare != null) {
                    Classification classification = object.getClassification();
                    while (classification.needsSuperPlacement()) {
                        classification = classification.getParent();
                    }
                    EntityBundle entities = gridSquare.getEntities(classification);
                    EntityBundle rocks = gridSquare.getEntities(ROCKS);
                    if (entities != null || rocks != null) {
                        if (entities == null) {
                            entities = rocks;
                        } else {
                            entities.merge(rocks);
                        }
                        for (Entity entity : entities) {
                            if (!this.checkEntityTooClose(entity, x, z)) continue;
                            return UnplaceableReason.ENTITY_TOO_CLOSE;
                        }
                    }
                }
                ++j;
            }
            ++i;
        }
        return UnplaceableReason.NO_PROBLEM;
    }

    private boolean checkEntityTooClose(Entity entity, float x, float z) {
        float limit = entity.getMaxWidth() / 2.5f;
        float limitSquared = limit * limit;
        Vector3f entityPos = entity.getTransform().getPosition();
        float dis = Maths.getComparitableDistance(x, z, entityPos.x, entityPos.z);
        return dis < limitSquared;
    }

    public int getPopulation(final Classification classification, int range, float x, float z) {
        final IntWrap count = new IntWrap();
        this.iterateGridSquaresNew(x, z, range, new PerSectionCode(){

            @Override
            public void execute(GridSection gridSquare) {
                count.number += gridSquare.getEntityCount(classification);
            }
        });
        return count.number;
    }

    public EntityBundle getListOfSpecies(Blueprint species, int range, float x, float z) {
        return this.getListOfSpecies(species.getSpeciesClassification(), range, x, z);
    }

    public EntityBundle getListOfSpecies(final Classification classification, int range, float x, float z) {
        final EntityBundle bundle = new EntityBundle();
        this.iterateGridSquaresNew(x, z, range, new PerSectionCode(){

            @Override
            public void execute(GridSection gridSquare) {
                gridSquare.getEntities(classification, bundle);
            }
        });
        return bundle;
    }

    public EntityBundle getListOfEntities(final ComponentType type, int range, float x, float z) {
        final EntityBundle bundle = new EntityBundle();
        this.iterateGridSquaresNew(x, z, range, new PerSectionCode(){

            @Override
            public void execute(GridSection gridSquare) {
                bundle.merge(gridSquare.getEntitiesWithComponent(type));
            }
        });
        return bundle;
    }

    public void iterateGridSquares(int range, float x, float z, PerSectionCode action) {
        GridSection square = this.getEntityGrid().getSectionAtPosition(x, z);
        if (square == null) {
            return;
        }
        int i = square.gridX - range;
        while (i <= square.gridX + range) {
            int j = square.gridZ - range;
            while (j <= square.gridZ + range) {
                GridSection gridSquare = this.getEntityGrid().getSection(i, j);
                if (gridSquare != null) {
                    action.execute(gridSquare);
                }
                ++j;
            }
            ++i;
        }
    }

    private void iterateEvenGridSquares(int range, float x, float z, PerSectionCode action) {
        int startX = this.getStartCoord(range, x);
        int startZ = this.getStartCoord(range, z);
        int i = startX;
        while (i < startX + range) {
            int j = startZ;
            while (j < startZ + range) {
                GridSection gridSquare = this.getEntityGrid().getSection(i, j);
                if (gridSquare != null) {
                    action.execute(gridSquare);
                }
                ++j;
            }
            ++i;
        }
    }

    public void iterateGridSquaresNew(float x, float z, int range, PerSectionCode action) {
        if (range % 2 == 0) {
            this.iterateEvenGridSquares(range, x, z, action);
        } else {
            this.iterateGridSquares((range - 1) / 2, x, z, action);
        }
    }

    public GridIterator getIterator(float x, float z, int range, boolean skipHalf, boolean skipEven) {
        if (range % 2 == 0) {
            int startX = this.getStartCoord(range, x);
            int startZ = this.getStartCoord(range, z);
            return new GridIterator(this.entityGrid, startX, startZ, range, skipHalf, skipEven);
        }
        int gridX = (int)(x / 2.5f);
        int gridZ = (int)(z / 2.5f);
        int halfRange = (range - 1) / 2;
        return new GridIterator(this.entityGrid, gridX - halfRange, gridZ - halfRange, range, skipHalf, skipEven);
    }

    public GridIterator getIterator(float x, float z, int range) {
        return this.getIterator(x, z, range, false, false);
    }

    private int getStartCoord(int range, float pos) {
        float n = pos / 2.5f;
        int middle = Math.round(n);
        return middle - range / 2;
    }

    public EntityBundle getListOfSimilarNearbySpecies(Entity entity, int range) {
        Vector3f pos = entity.getTransform().getPosition();
        return this.getListOfSpecies(entity.getBlueprint(), range, pos.x, pos.z);
    }

    public float getHeightOfTerrain(float x, float z) {
        return this.heightFinder.getHeight(x, z);
    }

    public Vector3f getNormalOfTerrain(float x, float z) {
        return this.heightFinder.getNormal(x, z);
    }

    public float getAltitude(float x, float z) {
        return this.getAltitude(this.getHeightOfTerrain(x, z));
    }

    public float getWaterHeight() {
        return this.configs.getWaterHeight();
    }

    public TerrainVertex getTerrainVertex(float x, float z) {
        int gridX = (int)(x / 0.7407407f);
        int gridZ = (int)(z / 0.7407407f);
        int terrainX = gridX / 27;
        int terrainZ = gridZ / 27;
        int squareX = gridX % 27;
        int squareZ = gridZ % 27;
        Terrain terrain = this.getTerrain(terrainX, terrainZ);
        if (terrain == null) {
            return null;
        }
        return terrain.getVertex(squareX, squareZ);
    }

    public void updateBiome(Entity entity, boolean increase) {
        SpreaderCompBlueprint spreadData = (SpreaderCompBlueprint)entity.getBlueprint().getComponent(ComponentType.SPREADER);
        if (spreadData == null) {
            return;
        }
        Vector3f position = entity.getTransform().getPosition();
        int gridX = (int)(position.x / 0.7407407f);
        int gridZ = (int)(position.z / 0.7407407f);
        int z = gridZ - spreadData.distance;
        while (z <= gridZ + spreadData.distance) {
            int x = gridX - spreadData.distance;
            while (x <= gridX + spreadData.distance) {
                if (x >= 0 && z >= 0) {
                    int terrainX = x / 27;
                    int terrainZ = z / 27;
                    int squareX = x % 27;
                    int squareZ = z % 27;
                    this.updateVertex(this.getTerrain(terrainX, terrainZ), squareX, squareZ, x, z, gridX, gridZ, spreadData, increase);
                    if (squareX == 0) {
                        this.updateVertex(this.getTerrain(terrainX - 1, terrainZ), 27, squareZ, x, z, gridX, gridZ, spreadData, increase);
                    }
                    if (squareZ == 0) {
                        this.updateVertex(this.getTerrain(terrainX, terrainZ - 1), squareX, 27, x, z, gridX, gridZ, spreadData, increase);
                    }
                    if (squareZ == 0 && squareX == 0) {
                        this.updateVertex(this.getTerrain(terrainX - 1, terrainZ - 1), 27, 27, x, z, gridX, gridZ, spreadData, increase);
                    }
                }
                ++x;
            }
            ++z;
        }
    }

    public float getAltitude(float height) {
        float aboveWater = height - this.getWaterHeight();
        float factor = 8.0f / (this.configs.getMaxHeight() - this.configs.getWaterHeight());
        return factor * aboveWater * 10.0f;
    }

    public static int calculateBatchId(Entity entity) {
        Vector3f pos = entity.getTransform().getPosition();
        World.clampPositionToInsideWorld(pos);
        int gridX = (int)(pos.x / 20.0f);
        int gridZ = (int)(pos.z / 20.0f);
        int batchId = gridZ * 5 + gridX;
        if (entity.isClutter()) {
            batchId += 25;
        }
        return batchId;
    }

    public static void clampPositionToWorld(Vector3f pos) {
        pos.x = Maths.clamp(pos.x, 0.0f, 100.0f);
        pos.z = Maths.clamp(pos.z, 0.0f, 100.0f);
    }

    public static void clampPositionToInsideWorld(Vector3f pos) {
        pos.x = Maths.clamp(pos.x, 0.1f, 99.9f);
        pos.z = Maths.clamp(pos.z, 0.1f, 99.9f);
    }

    protected boolean terrainsLoaded() {
        boolean loaded = true;
        int i = 0;
        while (i < this.terrains.length) {
            int j = 0;
            while (j < this.terrains[i].length) {
                loaded &= this.terrains[i][j].isLoaded();
                ++j;
            }
            ++i;
        }
        return loaded;
    }

    private Terrain getTerrain(int gridX, int gridZ) {
        if (gridX < 0 || gridX >= this.terrains.length || gridZ < 0 || gridZ >= this.terrains.length) {
            return null;
        }
        return this.terrains[gridZ][gridX];
    }

    private void updateVertex(Terrain terrain, int squareX, int squareZ, int x, int z, int gridX, int gridZ, SpreaderCompBlueprint spreadData, boolean increase) {
        if (terrain != null) {
            terrain.setDirty();
            int distanceSquared = this.calcDistanceSquared(x, z, gridX, gridZ);
            if (distanceSquared < spreadData.rangeSquared) {
                TerrainVertex vertex = terrain.getVertex(squareX, squareZ);
                int amount = (int)(spreadData.strength * (1.0f - (float)distanceSquared / (float)spreadData.rangeSquared));
                if (amount > 0) {
                    if (increase) {
                        vertex.addBiomeWeights(spreadData.biome, amount);
                    } else {
                        vertex.removeBiomeWeights(spreadData.biome, amount);
                    }
                }
            }
        }
    }

    private int calcDistanceSquared(int x, int z, int gridX, int gridZ) {
        int dX = x - gridX;
        int dZ = z - gridZ;
        return dX * dX + dZ * dZ;
    }

    private void updateTerrains() {
        int i = 0;
        while (i < this.terrains.length) {
            int j = 0;
            while (j < this.terrains[i].length) {
                this.terrains[i][j].update();
                ++j;
            }
            ++i;
        }
    }

    private void updateChunks() {
        Chunk[] chunkArray = this.chunks;
        int n = this.chunks.length;
        int n2 = 0;
        while (n2 < n) {
            Chunk chunk = chunkArray[n2];
            chunk.update();
            ++n2;
        }
    }

    private void initChunks(float[][] heights) {
        this.chunks = new Chunk[25];
        int i = 0;
        while (i < this.chunks.length) {
            int terrainX = i % 5;
            int terrainZ = i / 5;
            this.chunks[i] = new Chunk(i, this.terrains[terrainZ][terrainX], heights);
            ++i;
        }
    }
}

