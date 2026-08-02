/*
 * Decompiled with CFR 0.152.
 */
package main;

import resourceManagement.SoundCache;

public class TemporaryPlacement {
    public static void doTemporaryLoadingOfResources() {
        SoundCache.CACHE.requestSound("splash", true).withVolume(10.0f);
        SoundCache.CACHE.requestSound("thud", true).withVolume(0.4f);
        SoundCache.CACHE.requestSound("grassPlace", true);
        SoundCache.CACHE.requestSound("sheepBaa2", true);
    }
}

