/*
 * Decompiled with CFR 0.152.
 */
package utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.lwjgl.util.vector.Vector3f;
import session.GameMode;

public class BinaryWriter {
    private DataOutputStream writer;
    private GameMode mode;

    public BinaryWriter(File file) throws FileNotFoundException {
        this.writer = new DataOutputStream(new FileOutputStream(file));
    }

    public void setMode(GameMode mode) {
        this.mode = mode;
    }

    public GameMode getMode() {
        return this.mode;
    }

    public void writeInt(int value) throws IOException {
        this.writer.writeInt(value);
    }

    public void writeBoolean(boolean bool) throws IOException {
        this.writer.writeBoolean(bool);
    }

    public void writeFloat(float value) throws IOException {
        this.writer.writeFloat(value);
    }

    public void writeLong(long value) throws IOException {
        this.writer.writeLong(value);
    }

    public void writeShort(short value) throws IOException {
        this.writer.writeShort(value);
    }

    public void writeString(String value) throws IOException {
        this.writer.writeUTF(value);
    }

    public void writeVector(Vector3f vector) throws IOException {
        this.writeFloat(vector.x);
        this.writeFloat(vector.y);
        this.writeFloat(vector.z);
    }

    public void close() throws IOException {
        this.writer.close();
    }
}

