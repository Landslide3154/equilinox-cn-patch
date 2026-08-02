/*
 * Decompiled with CFR 0.152.
 */
package utils;

import java.io.BufferedReader;
import java.io.IOException;
import org.lwjgl.util.vector.Vector3f;
import utils.FileUtils;
import utils.LineSplitter;
import utils.MyFile;

public class CSVReader {
    private final String SEPARATOR;
    private BufferedReader reader;
    private LineSplitter splitter;

    public CSVReader(MyFile file) throws Exception {
        this.reader = file.getReader();
        this.SEPARATOR = ";";
    }

    public String nextLine() {
        String line = null;
        try {
            line = this.reader.readLine();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        if (line != null) {
            this.splitter = new LineSplitter(line, this.SEPARATOR);
            return line;
        }
        return null;
    }

    public String getNextString() {
        return this.splitter.getNextString();
    }

    public String getNextLabelString() {
        this.getNextString();
        return this.getNextString();
    }

    public float getNextLabelFloat() {
        this.getNextString();
        return this.getNextFloat();
    }

    public int getNextInt() {
        return this.splitter.getNextInt();
    }

    public long getNextLong() {
        return this.splitter.getNextLong();
    }

    public float getNextFloat() {
        return this.splitter.getNextFloat();
    }

    public int[] getNextLabelIntArray() {
        this.getNextString();
        int count = this.getNextInt();
        int[] array = new int[count];
        int i = 0;
        while (i < count) {
            array[i] = this.getNextInt();
            ++i;
        }
        return array;
    }

    public float[] getNextLabelFloatArray() {
        this.getNextString();
        int count = this.getNextInt();
        float[] array = new float[count];
        int i = 0;
        while (i < count) {
            array[i] = this.getNextFloat();
            ++i;
        }
        return array;
    }

    public Vector3f getNextLabelVector() {
        this.getNextString();
        return this.getNextVector();
    }

    public Vector3f getNextVector() {
        float x = this.splitter.getNextFloat();
        float y = this.splitter.getNextFloat();
        float z = this.splitter.getNextFloat();
        return new Vector3f(x, y, z);
    }

    public boolean isEndOfLine() {
        return !this.splitter.hasMoreValues();
    }

    public boolean getNextBool() {
        return this.splitter.getNextBool();
    }

    public boolean getNextLabelBool() {
        this.getNextString();
        return this.getNextBool();
    }

    public int getNextLabelInt() {
        this.getNextString();
        return this.getNextInt();
    }

    public void close() {
        FileUtils.closeBufferedReader(this.reader);
    }
}

