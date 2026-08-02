/*
 * Decompiled with CFR 0.152.
 */
package audio;

import audio.SoundLoader;
import glRequestProcessing.GlRequest;
import glRequestProcessing.GlRequestProcessor;
import resourceProcessing.RequestProcessor;
import resourceProcessing.ResourceRequest;
import toolbox.Maths;
import utils.MyFile;

public class Sound {
    public final String id;
    private int bufferID;
    private MyFile file;
    private float volume = 1.0f;
    private boolean loaded = false;
    private boolean randomPitch = false;
    private float minPitch;
    private float maxPitch;
    private boolean needsStreaming;
    private int bytesRead;
    private final boolean oggFile;

    private Sound(MyFile soundFile) {
        this.file = soundFile;
        String[] names = soundFile.getName().split("\\.");
        this.id = names[0];
        this.oggFile = names[1].contains("ogg");
    }

    public static Sound loadSoundNow(MyFile soundFile) {
        Sound sound = new Sound(soundFile);
        SoundLoader.doInitialSoundLoad(sound);
        return sound;
    }

    public static Sound loadSoundInBackground(MyFile soundFile) {
        final Sound sound = new Sound(soundFile);
        RequestProcessor.sendRequest(new ResourceRequest(){

            @Override
            public void doResourceRequest() {
                SoundLoader.doInitialSoundLoad(sound);
            }
        });
        return sound;
    }

    public boolean isOggFile() {
        return this.oggFile;
    }

    public Sound randomizePitch(float min, float max) {
        this.randomPitch = true;
        this.minPitch = min;
        this.maxPitch = max;
        return this;
    }

    public void setNeedsStreaming(boolean needsStreaming) {
        this.needsStreaming = needsStreaming;
    }

    public void delete() {
        if (!this.loaded) {
            return;
        }
        GlRequestProcessor.sendRequest(new GlRequest(){

            @Override
            public void executeGlRequest() {
                SoundLoader.deleteBuffer(Sound.this.bufferID);
            }
        });
        this.loaded = false;
    }

    public Sound withVolume(float volume) {
        this.volume = volume;
        return this;
    }

    public String getId() {
        return this.id;
    }

    public float getVolume() {
        return this.volume;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    protected boolean needsStreaming() {
        return this.needsStreaming;
    }

    protected int getBytesRead() {
        return this.bytesRead;
    }

    protected float getPitch() {
        if (!this.randomPitch) {
            return 1.0f;
        }
        return Maths.randomNumberBetween(this.minPitch, this.maxPitch);
    }

    protected void setBuffer(int buffer, int bytesRead) {
        this.bufferID = buffer;
        this.bytesRead = bytesRead;
        this.loaded = true;
    }

    protected int getBufferID() {
        return this.bufferID;
    }

    public MyFile getSoundFile() {
        return this.file;
    }
}

