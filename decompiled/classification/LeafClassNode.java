/*
 * Decompiled with CFR 0.152.
 */
package classification;

import blueprints.Blueprint;
import classification.ClassNode;
import classification.Classification;
import classification.NormalClassNode;
import entityBundle.EntityBundle;
import instances.Entity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import toolbox.Maths;
import world.GridSection;

public class LeafClassNode
extends ClassNode {
    private final Map<Integer, List<Entity>> entities = new HashMap<Integer, List<Entity>>();

    protected LeafClassNode(NormalClassNode parent, Classification classification) {
        super(parent, classification);
    }

    @Override
    protected void add(Entity entity, String key, GridSection section) {
        Blueprint species = entity.getBlueprint();
        List<Entity> entityList = this.entities.get(species.getId());
        if (entityList == null) {
            entityList = this.addNewSpecies(species, section);
        }
        entityList.add(entity);
        super.increaseEntityCount();
    }

    @Override
    protected void remove(Entity entity, String key, GridSection section) {
        Blueprint species = entity.getBlueprint();
        List<Entity> entityList = this.entities.get(species.getId());
        entityList.remove(entity);
        if (entityList.isEmpty()) {
            this.removeSpecies(species, entityList, section);
        }
        super.decreaseEntityCount();
    }

    @Override
    protected EntityBundle get(String classKey, EntityBundle bundle) {
        int speciesId = Integer.parseInt(classKey);
        List<Entity> entityList = this.entities.get(speciesId);
        if (entityList == null) {
            return null;
        }
        if (bundle == null) {
            bundle = new EntityBundle();
        }
        bundle.addEntityList(entityList);
        return bundle;
    }

    @Override
    protected void get(EntityBundle entityBundle) {
        for (List<Entity> entityList : this.entities.values()) {
            entityBundle.addEntityList(entityList);
        }
    }

    @Override
    protected Entity getRandomEntity(String key) {
        int speciesId = Integer.parseInt(key);
        List<Entity> entityList = this.entities.get(speciesId);
        if (entityList == null) {
            return null;
        }
        return entityList.get(Maths.RANDOM.nextInt(entityList.size()));
    }

    @Override
    protected int getEntityCount(String key) {
        int speciesId = Integer.parseInt(key);
        List<Entity> entityList = this.entities.get(speciesId);
        if (entityList == null) {
            return 0;
        }
        return entityList.size();
    }

    @Override
    protected Entity getRandomEntity() {
        int index = Maths.RANDOM.nextInt(this.getEntityCount());
        int current = 0;
        for (List<Entity> entityList : this.entities.values()) {
            if ((current += entityList.size()) <= index) continue;
            return entityList.get(Maths.RANDOM.nextInt(entityList.size()));
        }
        return null;
    }

    private List<Entity> addNewSpecies(Blueprint species, GridSection section) {
        ArrayList<Entity> entityList = new ArrayList<Entity>();
        this.entities.put(species.getId(), entityList);
        if (section != null) {
            section.registerNewSpecies(species, entityList);
        }
        return entityList;
    }

    private void removeSpecies(Blueprint species, List<Entity> entityList, GridSection section) {
        this.entities.remove(species.getId());
        if (this.entities.isEmpty()) {
            this.getParent().removeChild(this);
        }
        if (section != null) {
            section.unregisterSpecies(species, entityList);
        }
    }
}

