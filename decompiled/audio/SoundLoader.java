/*
 * Decompiled with CFR 0.152.
 */
package audio;

import audio.Sound;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import oggLoading.DataStream;
import oggLoading.DataStreamFactory;
import org.lwjgl.openal.AL10;

public class SoundLoader {
    private static List<Integer> buffers = new ArrayList<Integer>();

    protected static void doInitialSoundLoad(Sound sound) {
        try {
            DataStream stream = DataStreamFactory.openStream(sound);
            ByteBuffer byteBuffer = stream.loadNextData();
            int bufferID = SoundLoader.generateBuffer();
            SoundLoader.loadSoundDataIntoBuffer(bufferID, byteBuffer, stream.getAlFormat(), stream.getSampleRate());
            sound.setNeedsStreaming(!stream.hasEnded());
            sound.setBuffer(bufferID, byteBuffer.limit());
            stream.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Couldn't load sound file " + sound.getSoundFile());
        }
    }

    protected static int generateBuffer() {
        int bufferID = AL10.alGenBuffers();
        buffers.add(bufferID);
        return bufferID;
    }

    protected static void deleteBuffer(Integer bufferID) {
        buffers.remove(bufferID);
        AL10.alDeleteBuffers(bufferID);
        if (AL10.alGetError() != 0) {
            System.err.println("Problem deleting sound buffer.");
        }
    }

    protected static void cleanUp() {
        for (int buffer : buffers) {
            AL10.alDeleteBuffers(buffer);
        }
        if (AL10.alGetError() != 0) {
            System.err.println("Problem deleting sound buffers.");
        }
    }

    protected static void loadSoundDataIntoBuffer(int bufferID, ByteBuffer data, int format, int sampleRate) {
        AL10.alBufferData(bufferID, format, data, sampleRate);
        int error = AL10.alGetError();
        if (error != 0) {
            System.err.println("Problem loading sound data into buffer. " + error);
        }
    }
}

