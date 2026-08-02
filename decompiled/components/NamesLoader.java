/*
 * Decompiled with CFR 0.152.
 */
package components;

import toolbox.Maths;
import utils.CSVReader;
import utils.FileUtils;
import utils.MyFile;

public class NamesLoader {
    private static final MyFile NAME_FILE = new MyFile(FileUtils.RES_FOLDER, "animalNames.csv");
    private static String[] NAMES;

    public static void loadUpNames() {
        CSVReader reader = null;
        try {
            reader = new CSVReader(NAME_FILE);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Couldn't read animal names sheet!");
            System.exit(-1);
        }
        reader.nextLine();
        int count = reader.getNextLabelInt();
        NAMES = new String[count];
        int i = 0;
        while (i < count) {
            reader.nextLine();
            NamesLoader.NAMES[i] = reader.getNextString();
            ++i;
        }
        reader.close();
    }

    public static String getRandomName() {
        return NAMES[Maths.RANDOM.nextInt(NAMES.length)];
    }
}

