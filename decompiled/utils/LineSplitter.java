/*
 * Decompiled with CFR 0.152.
 */
package utils;

import org.lwjgl.util.vector.Vector3f;
import utils.FileUtils;

public class LineSplitter {
    private int pointer = 0;
    private String[] data;

    public LineSplitter(String string) {
        this.data = string.split(";");
    }

    public LineSplitter(String string, String separator) {
        this.data = string.split(separator);
    }

    public String getNextString() {
        return this.data[this.pointer++];
    }

    public int getNextInt() {
        return Integer.parseInt(this.data[this.pointer++]);
    }

    public long getNextLong() {
        return Long.parseLong(this.data[this.pointer++]);
    }

    public float getNextFloat() {
        return Float.parseFloat(this.data[this.pointer++]);
    }

    public Vector3f getNextVector() {
        float x = this.getNextFloat();
        float y = this.getNextFloat();
        float z = this.getNextFloat();
        return new Vector3f(x, y, z);
    }

    public double getNextDouble() {
        return Double.parseDouble(this.data[this.pointer++]);
    }

    public boolean getNextBool() {
        return FileUtils.readBoolean(this.data[this.pointer++]);
    }

    public boolean hasMoreValues() {
        return this.pointer < this.data.length;
    }
}

