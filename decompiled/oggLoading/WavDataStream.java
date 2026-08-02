/*
 * Decompiled with CFR 0.152.
 */
package oggLoading;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import oggLoading.DataStream;
import org.lwjgl.BufferUtils;
import utils.MyFile;

public class WavDataStream
implements DataStream {
    private final int alFormat;
    private final int sampleRate;
    private final int totalBytes;
    private final int chunkSize;
    private final AudioInputStream audioStream;
    private final ByteBuffer buffer;
    private final byte[] data;
    private int totalBytesRead = 0;

    private WavDataStream(AudioInputStream stream, int chunkSize) {
        this.audioStream = stream;
        this.chunkSize = chunkSize;
        AudioFormat format = stream.getFormat();
        this.alFormat = WavDataStream.getOpenAlFormat(format.getChannels(), format.getSampleSizeInBits());
        this.buffer = BufferUtils.createByteBuffer(chunkSize);
        this.data = new byte[chunkSize];
        this.sampleRate = (int)format.getSampleRate();
        this.totalBytes = (int)(stream.getFrameLength() * (long)format.getFrameSize());
    }

    @Override
    public void setStartPoint(int bytesRead) {
        this.totalBytesRead = bytesRead;
        try {
            this.audioStream.read(this.data, 0, bytesRead);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getSampleRate() {
        return this.sampleRate;
    }

    @Override
    public int getAlFormat() {
        return this.alFormat;
    }

    @Override
    public ByteBuffer loadNextData() {
        try {
            int bytesRead = this.audioStream.read(this.data, 0, this.chunkSize);
            this.totalBytesRead += bytesRead;
            this.buffer.clear();
            this.buffer.put(this.data, 0, bytesRead);
            this.buffer.flip();
        }
        catch (IOException e) {
            e.printStackTrace();
            System.err.println("Couldn't read more bytes from audio stream!");
        }
        return this.buffer;
    }

    @Override
    public boolean hasEnded() {
        return this.totalBytesRead >= this.totalBytes;
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

    public static WavDataStream openWavStream(MyFile wavFile, int chunkSizeBytes) throws Exception {
        BufferedInputStream bufferedInput = new BufferedInputStream(wavFile.getInputStream());
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(bufferedInput);
        return new WavDataStream(audioStream, chunkSizeBytes);
    }

    private static int getOpenAlFormat(int channels, int bitsPerSample) {
        if (channels == 1) {
            return bitsPerSample == 8 ? 4352 : 4353;
        }
        return bitsPerSample == 8 ? 4354 : 4355;
    }
}

