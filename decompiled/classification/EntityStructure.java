/*
 * Decompiled with CFR 0.152.
 */
package classification;

import blueprints.Blueprint;
import classification.ClassNode;
import classification.Classification;
import classification.Classifier;
import classification.NormalClassNode;
import entityBundle.EntityBundle;
import instances.Entity;
import world.GridSection;

public class EntityStructure {
    private ClassNode head = new NormalClassNode(null, Classifier.ALL_SPECIES);

    public void addEntity(Entity entity, GridSection section) {
        this.head.add(entity, entity.getBlueprint().getClassification().getKey(), section);
    }

    public void removeEntity(Entity entity, GridSection section) {
        this.head.remove(entity, entity.getBlueprint().getClassification().getKey(), section);
    }

    public void clear() {
        this.head = new NormalClassNode(null, Classifier.ALL_SPECIES);
    }

    public EntityBundle getEntities(String speciesKey) {
        return this.head.get(speciesKey, null);
    }

    public EntityBundle getEntities(String speciesKey, EntityBundle bundle) {
        return this.head.get(speciesKey, bundle);
    }

    public EntityBundle getAllEntities() {
        EntityBundle bundle = new EntityBundle();
        this.head.get(bundle);
        return bundle;
    }

    public EntityBundle getEntities(Blueprint species) {
        return this.getEntities(species.getSpeciesClassification().getKey());
    }

    public EntityBundle getEntities(Blueprint species, EntityBundle bundle) {
        return this.getEntities(species.getSpeciesClassification().getKey(), bundle);
    }

    public EntityBundle getEntities(Classification classification) {
        return this.getEntities(classification.getKey());
    }

    public EntityBundle getEntities(Classification classification, EntityBundle bundle) {
        return this.getEntities(classification.getKey(), bundle);
    }

    public int getEntityCount(Classification classification) {
        return this.head.getEntityCount(classification.getKey());
    }

    public Entity getRandomEntity(Classification classification) {
        return this.head.getRandomEntity(classification.getKey());
    }

    public boolean isEmpty() {
        return this.head.getEntityCount() == 0;
    }
}

