/*
 * Decompiled with CFR 0.152.
 */
package saves;

import errors.ErrorManager;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import saves.SaveSlot;
import utils.FileUtils;

public class Saves {
    private static final File SAVES_FOLDER = new File(FileUtils.getRootFolder(), "Equilinox_0_Saves");
    private static final File SAVES_FOLDER_BACKUP = new File("Equilinox_0_Saves");
    protected static String SAVE_FILE_NAME = "Equilinox_0";
    protected static String SAVE_FILE_EXT = ".dat";
    private File activeSavesFolder;
    private SaveSlot[] slots;

    public Saves(int slotCount) {
        this.initSavesFolder();
        Map<Integer, String> existingFileNames = this.getExistingFiles();
        this.slots = new SaveSlot[slotCount];
        int i = 0;
        while (i < slotCount) {
            String name = existingFileNames.get(i);
            this.slots[i] = name == null ? new SaveSlot(i, this) : new SaveSlot(i, name, this);
            ++i;
        }
    }

    public boolean hasFreeSlots() {
        SaveSlot[] saveSlotArray = this.slots;
        int n = this.slots.length;
        int n2 = 0;
        while (n2 < n) {
            SaveSlot slot = saveSlotArray[n2];
            if (slot.isEmpty()) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    public SaveSlot[] getSaveSlots() {
        return this.slots;
    }

    public List<SaveSlot> getCorruptSaves() {
        ArrayList<SaveSlot> corrupts = new ArrayList<SaveSlot>();
        SaveSlot[] saveSlotArray = this.slots;
        int n = this.slots.length;
        int n2 = 0;
        while (n2 < n) {
            SaveSlot slot = saveSlotArray[n2];
            if (slot.isCorrupt()) {
                corrupts.add(slot);
            }
            ++n2;
        }
        return corrupts;
    }

    public SaveSlot getFirstWorld() {
        SaveSlot[] saveSlotArray = this.slots;
        int n = this.slots.length;
        int n2 = 0;
        while (n2 < n) {
            SaveSlot slot = saveSlotArray[n2];
            if (!slot.isEmpty() && !slot.isCorrupt()) {
                return slot;
            }
            ++n2;
        }
        return null;
    }

    public int getSlotCount() {
        return this.slots.length;
    }

    public void deleteSave(int i) {
        SaveSlot slot = this.slots[i];
        slot.delete();
    }

    public SaveSlot getWaitingSlot() {
        SaveSlot[] saveSlotArray = this.slots;
        int n = this.slots.length;
        int n2 = 0;
        while (n2 < n) {
            SaveSlot slot = saveSlotArray[n2];
            if (slot.isEmpty()) {
                return slot;
            }
            ++n2;
        }
        return null;
    }

    public SaveSlot createNewSave() {
        SaveSlot[] saveSlotArray = this.slots;
        int n = this.slots.length;
        int n2 = 0;
        while (n2 < n) {
            SaveSlot slot = saveSlotArray[n2];
            if (slot.isEmpty()) {
                slot.createFile();
                return slot;
            }
            ++n2;
        }
        return null;
    }

    public SaveSlot getSaveSlot(int number) {
        if (number >= this.slots.length) {
            return null;
        }
        return this.slots[number];
    }

    private void initSavesFolder() {
        boolean folderMade = this.createSavesFolder(SAVES_FOLDER);
        if (!folderMade && !(folderMade = this.createSavesFolder(SAVES_FOLDER_BACKUP))) {
            ErrorManager.crashWithUserAlert("Error!", "The game was unable to create a saves folder. THis is usually a permissions error. You could try installing the game in a different location on your computer and try again, or try running as an administrator. Contact the dev at thinmatrix@gmail.com if you need more help.", "No error log available.");
        }
    }

    private boolean createSavesFolder(File folder) {
        boolean folderMade;
        if (!folder.exists() && !(folderMade = folder.mkdir())) {
            ErrorManager.createErrorLog("Save folder creation failed", "The game was unable to create a saves folder at the following location " + folder.getPath());
            return false;
        }
        this.activeSavesFolder = folder;
        return true;
    }

    public File getSavesFolder() {
        return this.activeSavesFolder;
    }

    public void deleteAllTempFiles() {
        File[] fileArray = this.activeSavesFolder.listFiles();
        int n = fileArray.length;
        int n2 = 0;
        while (n2 < n) {
            File file = fileArray[n2];
            try {
                if (file.getName().startsWith("tempEq")) {
                    FileUtils.deleteFile(file);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            ++n2;
        }
    }

    private Map<Integer, String> getExistingFiles() {
        HashMap<Integer, String> existingFiles = new HashMap<Integer, String>();
        File[] fileArray = this.activeSavesFolder.listFiles();
        int n = fileArray.length;
        int n2 = 0;
        while (n2 < n) {
            File file = fileArray[n2];
            try {
                String[] fileNameData = file.getName().split("-");
                if (fileNameData[0].equals(SAVE_FILE_NAME)) {
                    int number = Integer.parseInt(fileNameData[1]);
                    String name = fileNameData[2].replaceAll("_", " ");
                    existingFiles.put(number, name.split("\\.")[0]);
                }
            }
            catch (Exception e) {
                e.printStackTrace();
                System.err.println("Invalid save file name: " + file.getName());
            }
            ++n2;
        }
        return existingFiles;
    }
}

