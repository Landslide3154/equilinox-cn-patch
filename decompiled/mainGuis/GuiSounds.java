/*
 * Decompiled with CFR 0.152.
 */
package mainGuis;

import audio.Sound;
import resourceManagement.SoundCache;
import toolbox.Maths;

public class GuiSounds {
    private static final float MIN_PITCH = 0.92f;
    private static final float MAX_PITCH = 1.08f;
    public static final Sound CASH = GuiSounds.getSound("kerching");
    public static final Sound COMPLETE = GuiSounds.getSound("bell").withVolume(1.5f);
    public static final Sound UNLOCK = GuiSounds.getSound("unlock").withVolume(0.3f);
    public static final Sound MOUSE_OVER = GuiSounds.getSound("button1").withVolume(0.3f);
    public static final Sound DELETE = GuiSounds.getSound("deleteSave").withVolume(0.6f);
    public static final Sound NEGATIVE = GuiSounds.getSound("negative");
    public static final Sound SELECT = GuiSounds.getSound("selected").randomizePitch(0.92f, 1.08f).withVolume(0.4f);
    public static final Sound NOTIFY = GuiSounds.getSound("notify").withVolume(0.4f);
    private static final Sound[] CLICK_SOUNDS = new Sound[]{SoundCache.CACHE.requestSound("click1", true).withVolume(0.3f).randomizePitch(0.92f, 1.08f), SoundCache.CACHE.requestSound("click3", true).withVolume(0.3f).randomizePitch(0.92f, 1.08f), SoundCache.CACHE.requestSound("click2", true).withVolume(0.3f).randomizePitch(0.92f, 1.08f), SoundCache.CACHE.requestSound("click4", true).withVolume(0.3f).randomizePitch(0.92f, 1.08f)};

    public static void init() {
    }

    private static Sound getSound(String name) {
        return SoundCache.CACHE.requestSound(name, false);
    }

    public static Sound getClickSound() {
        int index = Maths.RANDOM.nextInt(CLICK_SOUNDS.length);
        return CLICK_SOUNDS[index];
    }
}

