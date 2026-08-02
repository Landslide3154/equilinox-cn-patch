/*
 * Decompiled with CFR 0.152.
 */
package oggLoading;

import java.io.IOException;
import java.nio.ByteBuffer;
import oggLoading.DataStream;
import oggLoading.OggInputStream;
import org.lwjgl.BufferUtils;
import utils.MyFile;

public class OggDataStream
implements DataStream {
    private final int alFormat;
    private final int sampleRate;
    private final int chunkSize;
    private final OggInputStream audioStream;
    private final ByteBuffer buffer;

    private OggDataStream(OggInputStream stream, int chunkSizeBytes) {
        this.audioStream = stream;
        this.chunkSize = chunkSizeBytes - chunkSizeBytes % 8;
        this.alFormat = stream.getFormat();
        this.buffer = BufferUtils.createByteBuffer(this.chunkSize);
        this.sampleRate = stream.getRate();
    }

    @Override
    public void setStartPoint(int startBytes) {
        try {
            if (startBytes > this.chunkSize) {
                this.audioStream.read(new byte[startBytes], 0, startBytes);
            } else {
                this.buffer.clear();
                this.audioStream.read(this.buffer, 0, startBytes);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getAlFormat() {
        return this.alFormat;
    }

    @Override
    public int getSampleRate() {
        return this.sampleRate;
    }

    @Override
    public ByteBuffer loadNextData() {
        block3: {
            try {
                this.buffer.clear();
                int bytesRead = this.audioStream.read(this.buffer, 0, this.chunkSize);
                this.buffer.flip();
                if (bytesRead > 0) break block3;
                return null;
            }
            catch (IOException e) {
                e.printStackTrace();
                System.err.println("Couldn't read more bytes from audio stream!");
                return null;
            }
        }
        return this.buffer;
    }

    @Override
    public boolean hasEnded() {
        return this.audioStream.isEndOfStream();
    }

    @Override
    public void close() {
        try {
            this.audioStream.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static OggDataStream openOggStream(MyFile oggFile, int chunkSizeBytes) throws Exception {
        OggInputStream stream = new OggInputStream(oggFile.getInputStream());
        return new OggDataStream(stream, chunkSizeBytes);
    }
}

