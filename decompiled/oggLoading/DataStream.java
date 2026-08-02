/*
 * Decompiled with CFR 0.152.
 */
package oggLoading;

import java.nio.ByteBuffer;

public interface DataStream {
    public int getAlFormat();

    public int getSampleRate();

    public void setStartPoint(int var1);

    public ByteBuffer loadNextData();

    public boolean hasEnded();

    public void close();
}

