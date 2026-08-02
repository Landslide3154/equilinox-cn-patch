/*
 * Decompiled with CFR 0.152.
 */
package world;

import classification.EntityStructure;
import events.EventData;
import events.EventManager;
import gameManaging.GameManager;
import instances.Entity;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.lwjgl.util.vector.Vector3f;
import session.EntityLoad;
import utils.BinaryWriter;
import world.AddableEntity;
import world.GridSection;
import world.World;

public class EntitiesGrid {
    private static final float DPPM_UPDATE_TIME = 2.5f;
    private int nextId = 1;
    public static final int GRID_COUNT = 40;
    private Map<Integer, Entity> worldEntities = new HashMap<Integer, Entity>();
    private EntityStructure sortedEntities = new EntityStructure();
    private GridSection[][] grid;
    private Map<AddableEntity, Boolean> additions = new HashMap<AddableEntity, Boolean>();
    private Map<Entity, Boolean> removals = new HashMap<Entity, Boolean>();
    private float dppmTimer = 0.0f;

    protected EntitiesGrid(float[][] heights) {
        this.initGrid(heights);
    }

    protected EntitiesGrid(EntityLoad entities, float[][] heights, int nextIdNotUsedRightNow) {
        this.initGrid(heights);
        for (List<Entity> staticBatch : entities.getStaticBatches()) {
            for (Entity entity : staticBatch) {
                entity.setId(this.nextId);
                this.worldEntities.put(this.nextId++, entity);
                this.sortedEntities.addEntity(entity, null);
                this.updateInGrid(entity);
            }
        }
        for (Entity entity : entities.getDynamicBatch()) {
            entity.setId(this.nextId);
            this.worldEntities.put(this.nextId++, entity);
            this.sortedEntities.addEntity(entity, null);
            this.updateInGrid(entity);
        }
    }

    public int getEntityCount() {
        return this.worldEntities.size();
    }

    public EntityStructure getSortedEntities() {
        return this.sortedEntities;
    }

    public Map<Integer, Entity> getWorldEntities() {
        return this.worldEntities;
    }

    public GridSection getSection(int gridX, int gridZ) {
        if (gridX >= 0 && gridZ >= 0 && gridX < this.grid.length && gridZ < this.grid.length) {
            return this.grid[gridX][gridZ];
        }
        return null;
    }

    public void updateInGrid(Entity entity) {
        Vector3f position = entity.getTransform().getPosition();
        GridSection section = this.getSectionAtPosition(position.x, position.z);
        if (entity.getCurrentGridSection() != section && section != null) {
            entity.unregisterFromCurrentGridSection();
            section.registerEntity(entity);
        }
    }

    public GridSection getSectionAtPosition(float x, float z) {
        int gridX = (int)(x / 2.5f);
        int gridZ = (int)(z / 2.5f);
        return this.getSection(gridX, gridZ);
    }

    protected void update() {
        int newDpPerMinute = 0;
        for (Entity entity : this.worldEntities.values()) {
            newDpPerMinute += entity.update();
        }
        this.updateDppmStats(newDpPerMinute);
        this.discardRemovals();
        this.introduceAdditions();
        this.updateGridSquareVisibilities();
    }

    protected void addInstance(AddableEntity entity, boolean urgent) {
        this.additions.put(entity, urgent);
    }

    protected void removeEntity(Entity entity, Boolean urgent) {
        this.removals.put(entity, urgent);
    }

    protected synchronized void export(BinaryWriter writer) throws IOException {
        writer.writeInt(this.nextId);
        writer.writeInt(this.worldEntities.size());
        for (Entity entity : this.worldEntities.values()) {
            entity.export(writer);
        }
    }

    protected void delete() {
        this.worldEntities.clear();
        this.additions.clear();
        this.removals.clear();
        this.sortedEntities.clear();
        this.grid = null;
    }

    private void introduceAdditions() {
        Iterator<Map.Entry<AddableEntity, Boolean>> iterator = this.additions.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<AddableEntity, Boolean> entry = iterator.next();
            Entity newEntity = entry.getKey().getEntity();
            if (newEntity == null) continue;
            this.addEntityToScene(newEntity, entry.getValue());
            iterator.remove();
        }
    }

    private void updateDppmStats(int dppm) {
        this.dppmTimer += GameManager.getGameSeconds();
        if (this.dppmTimer >= 2.5f) {
            this.dppmTimer %= 2.5f;
            GameManager.getSession().getStats().setDpPerMinute(dppm);
        }
    }

    private void discardRemovals() {
        for (Entity entity : this.removals.keySet()) {
            this.removeEntityFromGrid(entity);
        }
        this.removals.clear();
    }

    private void updateGridSquareVisibilities() {
        GridSection[][] gridSectionArray = this.grid;
        int n = this.grid.length;
        int n2 = 0;
        while (n2 < n) {
            GridSection[] row;
            GridSection[] gridSectionArray2 = row = gridSectionArray[n2];
            int n3 = row.length;
            int n4 = 0;
            while (n4 < n3) {
                GridSection section = gridSectionArray2[n4];
                section.testInFrustum();
                ++n4;
            }
            ++n2;
        }
    }

    private synchronized void addEntityToScene(Entity entity, boolean urgent) {
        this.addEntity(entity);
        this.updateInGrid(entity);
        if (entity.isStatic()) {
            int batchId = World.calculateBatchId(entity);
            GameManager.getSession().getSceneData().addStaticEntity(entity, batchId, urgent);
        } else {
            GameManager.getSession().getSceneData().addDynamicEntity(entity);
        }
    }

    private synchronized void removeEntityFromGrid(Entity entity) {
        this.worldEntities.remove(entity.getId());
        this.sortedEntities.removeEntity(entity, null);
        EventManager.POPULATION_REMOVE.registerEvent(new EventData(), entity.getBlueprint().getClassification().getKeyAsArray());
        entity.unregisterFromCurrentGridSection();
        if (entity.isCurrentlyInStaticBatch()) {
            GameManager.getSession().getSceneData().removeStaticEntity(entity, this.removals.get(entity));
        } else {
            GameManager.getSession().getSceneData().removeDynamicEntity(entity);
        }
    }

    private void initGrid(float[][] heights) {
        this.grid = new GridSection[40][40];
        int x = 0;
        while (x < 40) {
            int z = 0;
            while (z < 40) {
                this.grid[x][z] = new GridSection(x, z, heights);
                ++z;
            }
            ++x;
        }
    }

    private void addEntity(Entity entity) {
        entity.setId(this.nextId);
        this.worldEntities.put(this.nextId++, entity);
        this.sortedEntities.addEntity(entity, null);
        EventManager.POPULATION_ADD.registerEvent(new EventData(), entity.getBlueprint().getSpeciesClassification().getKeyAsArray());
    }
}

