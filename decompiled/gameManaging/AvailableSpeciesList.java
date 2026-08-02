/*
 * Decompiled with CFR 0.152.
 */
package gameManaging;

import errors.ErrorManager;
import gameManaging.GameManager;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import saves.SaveSlot;
import session.GameMode;
import sessionStats.LockStatus;
import utils.BinaryReader;
import utils.BinaryWriter;
import utils.FileUtils;

public class AvailableSpeciesList {
    private static final File LIST_FILE = new File(FileUtils.getRootFolder(), "unlockList.dat");
    private Set<Integer> unlockedSpecies = new HashSet<Integer>();
    private boolean unableToLoad = false;

    public AvailableSpeciesList() {
        this.unlockedSpecies.add(134);
        this.unlockedSpecies.add(162);
        this.unlockedSpecies.add(163);
        this.unlockedSpecies.add(166);
        this.unlockedSpecies.add(181);
        this.unlockedSpecies.add(6);
        this.unlockedSpecies.add(9);
        this.unlockedSpecies.add(35);
    }

    public boolean failedToLoad() {
        return this.unableToLoad;
    }

    public void addSpecies(LockStatus lockStatus) {
        this.unlockedSpecies.addAll(lockStatus.getUnlockedSpecies());
    }

    public Set<Integer> getAvailableSpecies() {
        return this.unlockedSpecies;
    }

    public void generateNewList() {
        System.out.println("GENERATING NEW SPECIES LIST");
        this.unlockedSpecies.clear();
        this.unlockedSpecies.add(134);
        this.unlockedSpecies.add(162);
        this.unlockedSpecies.add(163);
        this.unlockedSpecies.add(166);
        this.unlockedSpecies.add(181);
        this.unlockedSpecies.add(6);
        this.unlockedSpecies.add(9);
        this.unlockedSpecies.add(35);
        try {
            boolean success = this.loadListFromSaveFiles();
            this.unableToLoad = !success;
        }
        catch (Exception e) {
            this.unableToLoad = true;
            ErrorManager.createErrorLog("Unlock_List_Fail", e);
            e.printStackTrace();
        }
    }

    public boolean load() {
        block4: {
            if (LIST_FILE.exists()) break block4;
            return false;
        }
        try {
            BinaryReader reader = new BinaryReader(LIST_FILE);
            int count = reader.readInt();
            int i = 0;
            while (i < count) {
                this.unlockedSpecies.add(reader.readInt());
                ++i;
            }
            reader.close();
            this.unableToLoad = false;
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void save() {
        if (this.unableToLoad) {
            return;
        }
        try {
            BinaryWriter writer = new BinaryWriter(LIST_FILE);
            writer.writeInt(this.unlockedSpecies.size());
            for (Integer i : this.unlockedSpecies) {
                writer.writeInt(i);
            }
            writer.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean loadListFromSaveFiles() {
        boolean success = false;
        boolean noSaves = true;
        SaveSlot[] saveSlotArray = GameManager.sessionManager.getSaves().getSaveSlots();
        int n = saveSlotArray.length;
        int n2 = 0;
        while (n2 < n) {
            SaveSlot slot = saveSlotArray[n2];
            if (!slot.isEmpty()) {
                noSaves = false;
                success |= this.loadSpeciesFromSave(slot);
            }
            ++n2;
        }
        return noSaves || success;
    }

    private boolean loadSpeciesFromSave(SaveSlot save) {
        try {
            BinaryReader reader = new BinaryReader(save.getSaveFile());
            reader.setVersion(reader.readBoolean() ? reader.readInt() : 0);
            boolean normal = this.skipInfo(reader);
            if (normal) {
                this.readSpecies(reader);
            }
            reader.close();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean skipInfo(BinaryReader reader) throws Exception {
        GameMode mode;
        reader.readLong();
        reader.readInt();
        reader.readInt();
        reader.readInt();
        if (reader.getVersion() >= 7) {
            reader.readInt();
        }
        reader.readVector();
        reader.readFloat();
        reader.readFloat();
        GameMode gameMode = mode = reader.getVersion() < 6 ? GameMode.NORMAL : GameMode.values()[reader.readInt()];
        if (mode == GameMode.BUILD) {
            int count = reader.readInt();
            int i = 0;
            while (i < count) {
                reader.readVector();
                ++i;
            }
        }
        reader.readInt();
        reader.readInt();
        reader.readInt();
        reader.readFloat();
        return mode == GameMode.NORMAL;
    }

    private void readSpecies(BinaryReader reader) throws Exception {
        int count = reader.readInt();
        int i = 0;
        while (i < count) {
            int blueprintID = reader.readInt();
            this.unlockedSpecies.add(blueprintID);
            ++i;
        }
    }
}

