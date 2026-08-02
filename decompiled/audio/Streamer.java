/*
 * Decompiled with CFR 0.152.
 */
package audio;

import audio.AudioController;
import audio.Sound;
import audio.SoundLoader;
import audio.SoundSource;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import oggLoading.DataStream;
import oggLoading.DataStreamFactory;

public class Streamer {
    private static final int NUM_BUFFERS = 2;
    private SoundSource source;
    private AudioController controller;
    private DataStream stream;
    private boolean initialBufferPlaying = true;
    private List<Integer> unusedBuffers = new ArrayList<Integer>();
    private List<Integer> bufferQueue = new ArrayList<Integer>();

    protected Streamer(Sound sound, SoundSource source, AudioController controller) throws Exception {
        System.out.println("Streaming " + sound.getSoundFile());
        this.source = source;
        this.controller = controller;
        this.stream = DataStreamFactory.openStream(sound);
        this.stream.setStartPoint(sound.getBytesRead());
        int i = 0;
        while (i < 2) {
            this.unusedBuffers.add(SoundLoader.generateBuffer());
            ++i;
        }
    }

    protected void delete() {
        this.stream.close();
        for (Integer buffer : this.bufferQueue) {
            SoundLoader.deleteBuffer(buffer);
        }
        for (Integer buffer : this.unusedBuffers) {
            SoundLoader.deleteBuffer(buffer);
        }
    }

    protected boolean update() {
        if (!this.controller.isActive()) {
            return false;
        }
        if (!this.stream.hasEnded() && this.source.isPlaying()) {
            if (!this.unusedBuffers.isEmpty()) {
                this.queueUnusedBuffer();
            } else if (this.isTopBufferFinished()) {
                this.refillTopBuffer();
            }
        }
        return this.controller.isActive();
    }

    private void queueUnusedBuffer() {
        int buffer = this.unusedBuffers.remove(0);
        this.loadNextDataIntoBuffer(buffer);
        this.queueBuffer(buffer);
    }

    private void refillTopBuffer() {
        int buffer = this.unqueueTopBuffer();
        this.loadNextDataIntoBuffer(buffer);
        this.queueBuffer(buffer);
    }

    private void loadNextDataIntoBuffer(int buffer) {
        ByteBuffer data = this.stream.loadNextData();
        SoundLoader.loadSoundDataIntoBuffer(buffer, data, this.stream.getAlFormat(), this.stream.getSampleRate());
    }

    private boolean isTopBufferFinished() {
        int finishedBufferCount = this.source.getFinishedBuffersCount();
        if (finishedBufferCount > 0 && this.initialBufferPlaying) {
            --finishedBufferCount;
            this.source.unqueue();
            this.initialBufferPlaying = false;
        }
        return finishedBufferCount > 0;
    }

    private int unqueueTopBuffer() {
        int topBuffer = this.bufferQueue.remove(0);
        this.source.unqueue();
        return topBuffer;
    }

    private void queueBuffer(int buffer) {
        if (this.source.isPlaying()) {
            this.source.queue(buffer);
            this.bufferQueue.add(buffer);
        }
    }
}

