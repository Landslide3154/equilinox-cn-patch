/*
 * Decompiled with CFR 0.152.
 */
package utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import utils.MyFile;

public class FileUtils {
    public static final String FILE_SEPARATOR = "/";
    public static final String SEPARATOR = ";";
    public static MyFile RES_FOLDER = new MyFile("res");
    public static final int TRUE = 1;
    public static final int FALSE = 0;

    public static boolean readBoolean(String value) {
        int boolValue = Integer.parseInt(value);
        return boolValue == 1;
    }

    public static int booleanToInt(boolean bool) {
        return bool ? 1 : 0;
    }

    public static void closeBufferedReader(BufferedReader reader) {
        try {
            reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void renameFile(File original, File newName) throws IOException {
        Files.move(original.toPath(), newName.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isDirectory()) {
            File[] fileArray = file.listFiles();
            int n = fileArray.length;
            int n2 = 0;
            while (n2 < n) {
                File innerFile = fileArray[n2];
                FileUtils.deleteFile(innerFile);
                ++n2;
            }
        }
        file.delete();
    }

    public static File getRootFolder() {
        File file = new File(FileUtils.class.getProtectionDomain().getCodeSource().getLocation().getFile());
        File parent = file.getParentFile();
        return new File(parent.getAbsolutePath().replace("%20", " "));
    }
}

