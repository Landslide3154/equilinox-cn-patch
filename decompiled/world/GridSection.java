/*
 * Decompiled with CFR 0.152.
 */
package world;

import blueprints.Blueprint;
import classification.Classification;
import classification.EntityStructure;
import componentArchitecture.ComponentType;
import entityBundle.EntityBundle;
import instances.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.GridSquare;
import toolbox.Maths;
import world.ClosestPointFinder;

public class GridSection
extends GridSquare {
    public static final float SIZE = 2.5f;
    public static final float DIAGONAL_SIZE = 2.5f * Maths.ROOT_2;
    private static final float MAX_ENTITY_HEIGHT = 1.0f;
    private EntityStructure entityTree = new EntityStructure();
    private Map<ComponentType, EntityBundle> componentDistribution = new HashMap<ComponentType, EntityBundle>();
    private ClosestPointFinder closestPoint;
    private float distanceFromCamera;

    protected GridSection(int gridX, int gridZ, float[][] heights) {
        super(gridX, gridZ, 40, 2.5f);
        this.findMinMaxHeights(heights);
        Vector3f topLeft = super.getTopLeftPosition();
        this.closestPoint = new ClosestPointFinder(new Vector2f(topLeft.x, topLeft.z), 2.5f);
    }

    public EntityBundle getEntities(Blueprint species) {
        return this.entityTree.getEntities(species);
    }

    public EntityBundle getEntities(Blueprint species, EntityBundle bundle) {
        return this.entityTree.getEntities(species, bundle);
    }

    public EntityBundle getEntities(String speciesKey) {
        return this.entityTree.getEntities(speciesKey);
    }

    public EntityBundle getEntities(String speciesKey, EntityBundle bundle) {
        return this.entityTree.getEntities(speciesKey, bundle);
    }

    public EntityBundle getEntities(Classification classification) {
        return this.entityTree.getEntities(classification);
    }

    public EntityBundle getAllEntities() {
        return this.entityTree.getAllEntities();
    }

    public EntityBundle getEntities(Classification classification, EntityBundle bundle) {
        return this.entityTree.getEntities(classification, bundle);
    }

    public EntityBundle getEntitiesWithComponent(ComponentType type) {
        return this.componentDistribution.get((Object)type);
    }

    public Entity getRandomEntity(Classification classification) {
        return this.entityTree.getRandomEntity(classification);
    }

    @Override
    public void testInFrustum() {
        if (this.entityTree.isEmpty()) {
            super.setVisible(true);
            this.distanceFromCamera = 0.0f;
        } else {
            super.testInFrustum();
            this.distanceFromCamera = this.closestPoint.getDistance();
        }
    }

    public float getDistanceFromCam() {
        return this.distanceFromCamera;
    }

    public int getEntityCount(Classification classification) {
        return this.entityTree.getEntityCount(classification);
    }

    public void unregisterEntity(Entity entity) {
        this.entityTree.removeEntity(entity, this);
        entity.setCurrentGridSection(null);
    }

    public void registerEntity(Entity entity) {
        this.entityTree.addEntity(entity, this);
        entity.setCurrentGridSection(this);
    }

    public void registerNewSpecies(Blueprint blueprint, List<Entity> entityList) {
        for (ComponentType componentType : blueprint.getComponentTypes()) {
            EntityBundle bundle = this.componentDistribution.get((Object)componentType);
            if (bundle == null) {
                bundle = new EntityBundle();
                this.componentDistribution.put(componentType, bundle);
            }
            bundle.addEntityList(entityList);
        }
    }

    public void unregisterSpecies(Blueprint blueprint, List<Entity> entityList) {
        for (ComponentType componentType : blueprint.getComponentTypes()) {
            EntityBundle bundle = this.componentDistribution.get((Object)componentType);
            bundle.remove(entityList);
            if (!bundle.isEmpty()) continue;
            this.componentDistribution.remove((Object)componentType);
        }
    }

    private void findMinMaxHeights(float[][] heights) {
        float vertsPerSquare = 3.375f;
        int startX = (int)(vertsPerSquare * (float)this.gridX);
        int startZ = (int)(vertsPerSquare * (float)this.gridZ);
        int count = (int)(Math.ceil(vertsPerSquare) + 1.0);
        int z = startZ;
        while (z <= startZ + count) {
            int x = startX;
            while (x <= startX + count) {
                if (z < heights.length && x < heights[z].length) {
                    this.testMaxHeight(heights[z][x] + 1.0f);
                    this.testMinHeight(heights[z][x]);
                }
                ++x;
            }
            ++z;
        }
    }
}

