/*
 * Decompiled with CFR 0.152.
 */
package entityBundle;

import entityBundle.EntityIterator;
import instances.Entity;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import toolbox.Maths;

public class EntityBundle
implements Iterable<Entity> {
    private List<List<Entity>> entities;

    public EntityBundle(List<List<Entity>> entities) {
        this.entities = entities;
    }

    public EntityBundle() {
        this.entities = new ArrayList<List<Entity>>();
    }

    public int getListCount() {
        return this.entities.size();
    }

    public void merge(EntityBundle bundle) {
        if (bundle != null) {
            this.entities.addAll(bundle.entities);
        }
    }

    public boolean isEmpty() {
        return this.entities.isEmpty();
    }

    public Entity[] getRandomList(int size) {
        if (this.isEmpty()) {
            return new Entity[0];
        }
        int bundleSize = this.getSize();
        int startPlace = Maths.RANDOM.nextInt(bundleSize);
        Entity[] result = new Entity[size < bundleSize ? size : bundleSize];
        int gap = 1;
        if (size < bundleSize) {
            float maxGap = (float)(bundleSize - 1) / (float)(size - 1);
            gap = (int)Math.floor(maxGap);
        }
        this.iterate(result, startPlace, gap);
        return result;
    }

    private void iterate(Entity[] outputArray, int startIndex, int gap) {
        int element = startIndex;
        int list = 0;
        while (list < this.entities.size()) {
            List<Entity> entityList = this.entities.get(list);
            if (entityList.size() > element) break;
            element -= entityList.size();
            ++list;
        }
        this.fillOutputArray(list, element, gap, outputArray);
    }

    private void fillOutputArray(int list, int element, int gap, Entity[] outputArray) {
        int pos = 0;
        List<Entity> currentList = this.entities.get(list);
        while (pos < outputArray.length) {
            if (element < currentList.size()) {
                outputArray[pos++] = currentList.get(element);
                element += gap;
                continue;
            }
            element -= currentList.size();
            ++list;
            currentList = this.entities.get(list %= this.entities.size());
        }
    }

    public int getSize() {
        int count = 0;
        for (List<Entity> list : this.entities) {
            count += list.size();
        }
        return count;
    }

    public Entity getRandomEntity() {
        if (this.isEmpty()) {
            return null;
        }
        int index = Maths.RANDOM.nextInt(this.getSize());
        for (List<Entity> list : this.entities) {
            if (list.size() <= index) {
                index -= list.size();
                continue;
            }
            return list.get(index);
        }
        System.err.println("Error choosing random entitiy from EntityBundle");
        return null;
    }

    public Entity get(int index) {
        for (List<Entity> list : this.entities) {
            if (list.size() <= index) {
                index -= list.size();
                continue;
            }
            return list.get(index);
        }
        return null;
    }

    public void remove(List<Entity> entityList) {
        this.entities.remove(entityList);
    }

    public void addEntityList(List<Entity> entityList) {
        this.entities.add(entityList);
    }

    @Override
    public Iterator<Entity> iterator() {
        return new EntityIterator(this.entities);
    }
}

