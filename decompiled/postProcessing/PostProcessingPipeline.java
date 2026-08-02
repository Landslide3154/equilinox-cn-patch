/*
 * Decompiled with CFR 0.152.
 */
package postProcessing;

public interface PostProcessingPipeline {
    public void carryOutProcessing(int var1, int var2, boolean var3);

    public int getOutputTexture();

    public void cleanUp();
}

