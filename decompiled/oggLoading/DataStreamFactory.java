/*
 * Decompiled with CFR 0.152.
 */
package oggLoading;

import audio.Sound;
import oggLoading.DataStream;
import oggLoading.OggDataStream;
import oggLoading.WavDataStream;

public class DataStreamFactory {
    public static DataStream openStream(Sound sound) throws Exception {
        if (sound.isOggFile()) {
            return OggDataStream.openOggStream(sound.getSoundFile(), 100000);
        }
        return WavDataStream.openWavStream(sound.getSoundFile(), 100000);
    }
}

