/*
 * Decompiled with CFR 0.152.
 */
package saves;

import gameManaging.UserConfigs;
import java.io.File;
import java.io.IOException;
import saves.SaveSlotInfo;
import saves.Saves;
import session.Session;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;
import utils.FileUtils;

public class SaveSlot {
    public static final String TEMP_NAME = "tempEq";
    private static final int SAVE_VERSION = 13;
    private String name;
    private int number;
    private File saveFile;
    private SaveSlotInfo info;
    private boolean corrupt = false;
    private final Saves saves;
    private File temporaryFile;

    protected SaveSlot(int number, Saves saves) {
        this.saves = saves;
        this.number = number;
        this.generateDefaultName();
        this.setUpFile();
        this.loadInfo();
        UserConfigs.checkCorruption(this);
    }

    protected SaveSlot(int number, String name, Saves saves) {
        this.saves = saves;
        this.number = number;
        this.name = name;
        this.setUpFile();
        this.loadInfo();
        UserConfigs.checkCorruption(this);
    }

    public void setCorrupt() {
        this.corrupt = true;
    }

    public int getNumber() {
        return this.number;
    }

    public void setName(String name) {
        this.name = name;
        File oldFile = this.saveFile;
        this.setUpFile();
        try {
            FileUtils.renameFile(oldFile, this.saveFile);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

    public BinaryWriter getWriter(Session session) throws Exception {
        this.createTemporaryFile();
        BinaryWriter writer = new BinaryWriter(this.temporaryFile);
        writer.setMode(session.getMode());
        writer.writeBoolean(true);
        writer.writeInt(13);
        this.info.save(writer, session);
        return writer;
    }

    public void finishSaving() throws Exception {
        FileUtils.renameFile(this.temporaryFile, this.saveFile);
    }

    private void createTemporaryFile() throws Exception {
        this.temporaryFile = new File(this.saves.getSavesFolder(), TEMP_NAME + this.number + "-" + Maths.RANDOM.nextInt() + Saves.SAVE_FILE_EXT);
        this.temporaryFile.createNewFile();
    }

    public BinaryReader getReader() throws Exception {
        BinaryReader reader;
        boolean hasVersion = (reader = new BinaryReader(this.saveFile)).readBoolean();
        reader.setVersion(hasVersion ? reader.readInt() : 0);
        System.out.println("LOADING SAVE FILE: " + this.name + ", VERSION: " + reader.getVersion());
        this.info.load(reader);
        return reader;
    }

    public boolean isCorrupt() {
        return this.corrupt;
    }

    public boolean isEmpty() {
        return !this.saveFile.exists();
    }

    public void delete() {
        FileUtils.deleteFile(this.saveFile);
        this.info = null;
        this.corrupt = false;
        this.generateDefaultName();
        this.setUpFile();
    }

    public SaveSlotInfo getInfo() {
        return this.info;
    }

    protected void createFile() {
        if (this.isEmpty()) {
            try {
                this.saveFile.createNewFile();
                this.info = new SaveSlotInfo();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateDefaultName() {
        this.name = "Save " + (this.number + 1);
    }

    private void setUpFile() {
        String fileName = this.name.replaceAll(" ", "_");
        this.saveFile = new File(this.saves.getSavesFolder(), String.valueOf(Saves.SAVE_FILE_NAME) + "-" + this.number + "-" + fileName + Saves.SAVE_FILE_EXT);
    }

    public File getSaveFile() {
        return this.saveFile;
    }

    private void loadInfo() {
        if (!this.isEmpty()) {
            this.info = new SaveSlotInfo();
            BinaryReader reader = null;
            try {
                reader = new BinaryReader(this.saveFile);
                reader.setVersion(reader.readBoolean() ? reader.readInt() : 0);
                this.info.load(reader);
                reader.close();
            }
            catch (Exception e) {
                reader.close();
                e.printStackTrace();
                this.corrupt = true;
                System.err.println("Couldn't load save slot info for slot " + this.number);
            }
        }
    }
}

