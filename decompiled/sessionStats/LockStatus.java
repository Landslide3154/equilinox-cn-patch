/*
 * Decompiled with CFR 0.152.
 */
package sessionStats;

import blueprints.Blueprint;
import events.EventData;
import events.EventManager;
import gameManaging.GameManager;
import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;
import utils.BinaryReader;
import utils.BinaryWriter;

public class LockStatus {
    private Set<Integer> unlockedBlueprints = new LinkedHashSet<Integer>();

    public static LockStatus loadLockStatus(BinaryReader reader) throws Exception {
        LockStatus lockStatus = new LockStatus();
        int count = reader.readInt();
        int i = 0;
        while (i < count) {
            int blueprintID = reader.readInt();
            lockStatus.unlockedBlueprints.add(blueprintID);
            ++i;
        }
        return lockStatus;
    }

    public static LockStatus newLockStatus() {
        LockStatus lock = new LockStatus();
        return lock;
    }

    private LockStatus() {
    }

    public boolean isUnlocked(Blueprint blueprint) {
        return this.unlockedBlueprints.contains(blueprint.getId());
    }

    public int getUnlockedCount() {
        return this.unlockedBlueprints.size();
    }

    public Set<Integer> getUnlockedSpecies() {
        return this.unlockedBlueprints;
    }

    public boolean unlockBlueprint(Blueprint blueprint) {
        boolean unlocked = this.unlockedBlueprints.add(blueprint.getId());
        if (unlocked) {
            EventManager.SPECIES_UNLOCK.registerEvent(new EventData(blueprint), Integer.toString(blueprint.getId()));
            if (!GameManager.BREED_TREES.isBaseSpecies(blueprint)) {
                return true;
            }
        }
        return false;
    }

    public void export(BinaryWriter writer) throws IOException {
        writer.writeInt(this.unlockedBlueprints.size());
        for (Integer id : this.unlockedBlueprints) {
            writer.writeInt(id);
        }
    }
}

