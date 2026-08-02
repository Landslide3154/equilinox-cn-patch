/*
 * Decompiled with CFR 0.152.
 */
package gameManaging;

import audio.SoundMaestro;
import gameManaging.GameManager;
import graphicsOptions.GraphicsOptions;
import java.io.File;
import java.io.IOException;
import java.util.List;
import languages.Language;
import saves.SaveSlot;
import utils.BinaryReader;
import utils.BinaryWriter;

public class UserConfigs {
    private static int saveSlotId = 0;
    private static Language language = Language.ENGLISH;
    private static int presetId = 2;
    private static int[] corruptSlots = new int[0];
    private static final File CONFIGS_FILE = new File("Equilinox_0_UserConfigs.dat");

    public static void loadConfigs() {
        try {
            if (!CONFIGS_FILE.createNewFile()) {
                BinaryReader reader = new BinaryReader(CONFIGS_FILE);
                corruptSlots = new int[reader.readInt()];
                int i = 0;
                while (i < corruptSlots.length) {
                    UserConfigs.corruptSlots[i] = reader.readInt();
                    ++i;
                }
                saveSlotId = reader.readInt();
                language = Language.values()[reader.readInt()];
                presetId = reader.readInt();
                GraphicsOptions.loadSettings(reader);
                SoundMaestro.getMusicPlayer().loadSettings(reader);
                SoundMaestro.SOUND_VOLUME = reader.readFloat();
                reader.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading user configs.");
        }
    }

    public static void checkCorruption(SaveSlot slot) {
        int[] nArray = corruptSlots;
        int n = corruptSlots.length;
        int n2 = 0;
        while (n2 < n) {
            int slotNumber = nArray[n2];
            if (slotNumber == slot.getNumber()) {
                slot.setCorrupt();
                return;
            }
            ++n2;
        }
    }

    public static int getPresetId() {
        return presetId;
    }

    public static void setPresetId(int id) {
        presetId = id;
    }

    public static int getSaveSlotId() {
        return saveSlotId;
    }

    public static void setLanguage(Language lan) {
        language = lan;
    }

    public static Language getLanguage() {
        return language;
    }

    public static void saveConfigs() throws IOException {
        BinaryWriter writer = new BinaryWriter(CONFIGS_FILE);
        List<SaveSlot> corruptFiles = GameManager.sessionManager.getSaves().getCorruptSaves();
        writer.writeInt(corruptFiles.size());
        for (SaveSlot slot : corruptFiles) {
            writer.writeInt(slot.getNumber());
        }
        writer.writeInt(GameManager.getSession().getSave().getNumber());
        writer.writeInt(language.ordinal());
        writer.writeInt(presetId);
        GraphicsOptions.saveSettings(writer);
        SoundMaestro.getMusicPlayer().exportSettings(writer);
        writer.writeFloat(SoundMaestro.SOUND_VOLUME);
        writer.close();
    }
}

