/*
 * Decompiled with CFR 0.152.
 */
package languages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import languages.ComplexString;
import languages.Language;
import utils.CSVReader;
import utils.FileUtils;
import utils.MyFile;

public class GameText {
    public static final MyFile LANGUAGE_FILE = new MyFile(FileUtils.RES_FOLDER, "languageSheet.csv");
    private static Map<Integer, List<String>> gameTexts = new HashMap<Integer, List<String>>();
    private static int languageId;

    public static void init(int langId) {
        languageId = langId;
        try {
            CSVReader reader = new CSVReader(LANGUAGE_FILE);
            reader.nextLine();
            while (reader.nextLine() != null) {
                int id = reader.getNextInt();
                reader.getNextString();
                ArrayList<String> texts = new ArrayList<String>();
                int i = 0;
                while (i < Language.values().length) {
                    texts.add(reader.getNextString());
                    ++i;
                }
                gameTexts.put(id, texts);
            }
            reader.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Problem loading language text file!");
            System.exit(-1);
        }
    }

    public static int getLanguageId() {
        return languageId;
    }

    public static ComplexString getComplexText(int id) {
        return new ComplexString(GameText.getText(id));
    }

    public static String getText(int id) {
        List<String> text = gameTexts.get(id);
        if (text != null) {
            return text.get(languageId);
        }
        System.err.println("No game text with the ID " + id);
        System.exit(-1);
        return null;
    }
}

