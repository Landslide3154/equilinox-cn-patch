/*
 * Decompiled with CFR 0.152.
 */
package session;

import blueprints.Blueprint;
import instances.Entity;
import instances.EntityGetRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import resourceManagement.BlueprintRepository;
import utils.BinaryReader;
import world.World;

public class EntityLoad {
    private List<List<Entity>> staticBatches;
    private List<Entity> dynamicBatch;
    private Map<Integer, Entity> allEntities = new HashMap<Integer, Entity>();
    private Map<Integer, List<EntityGetRequest>> requests = new HashMap<Integer, List<EntityGetRequest>>();
    private int nextEntityId;

    private EntityLoad() {
        this.dynamicBatch = new ArrayList<Entity>();
        this.initStaticBatches();
    }

    public void makeRequestForEntity(EntityGetRequest request) {
        Entity entity = this.allEntities.get(request.getId());
        if (entity != null) {
            request.provideEntity(entity);
        } else {
            List<EntityGetRequest> requestList = this.requests.get(request.getId());
            if (requestList == null) {
                requestList = new ArrayList<EntityGetRequest>();
                this.requests.put(request.getId(), requestList);
            }
            requestList.add(request);
        }
    }

    public static EntityLoad loadEntities(BinaryReader reader) throws Exception {
        EntityLoad entities = new EntityLoad();
        entities.nextEntityId = reader.readInt();
        int entityCount = reader.readInt();
        int i = 0;
        while (i < entityCount) {
            EntityLoad.loadEntity(reader, entities);
            ++i;
        }
        return entities;
    }

    public int getNextEntityId() {
        return this.nextEntityId;
    }

    public List<List<Entity>> getStaticBatches() {
        return this.staticBatches;
    }

    public List<Entity> getDynamicBatch() {
        return this.dynamicBatch;
    }

    private static void loadEntity(BinaryReader reader, EntityLoad entities) throws Exception {
        int blueprintID = reader.readInt();
        if (blueprintID <= 0) {
            return;
        }
        Blueprint blueprint = BlueprintRepository.getBlueprint(blueprintID);
        boolean isStatic = reader.readBoolean();
        Entity entity = blueprint.createInstance(reader, entities);
        EntityLoad.checkForRequests(entity, entities);
        entities.allEntities.put(entity.getId(), entity);
        if (isStatic) {
            int batchId = World.calculateBatchId(entity);
            try {
                entities.staticBatches.get(batchId).add(entity);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            entities.dynamicBatch.add(entity);
        }
    }

    private static void checkForRequests(Entity entity, EntityLoad entities) {
        List<EntityGetRequest> entityRequests = entities.requests.get(entity.getId());
        if (entityRequests != null) {
            for (EntityGetRequest request : entityRequests) {
                request.provideEntity(entity);
            }
            entityRequests.clear();
        }
    }

    private void initStaticBatches() {
        this.staticBatches = new ArrayList<List<Entity>>();
        int i = 0;
        while (i < 50) {
            this.staticBatches.add(new ArrayList());
            ++i;
        }
    }
}

