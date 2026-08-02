/*
 * Decompiled with CFR 0.152.
 */
package toolbar;

import biomes.Biome;
import fontRendering.Text;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;

public class BiomeData {
    private Biome biome;
    private Text biomeText;
    private int count;
    private Text countText;
    private Text dash;

    protected BiomeData(Biome biome, int count) {
        this.initBiomeText(biome);
        this.initCountText(count);
        this.initDashText();
    }

    protected void update(Biome newBiome, int newCount) {
        if (newBiome != this.biome) {
            this.updateBiome(newBiome);
        }
        if (newCount != this.count) {
            this.updateCount(newCount);
        }
    }

    protected Text getBiomeText() {
        return this.biomeText;
    }

    protected Text getCountText() {
        return this.countText;
    }

    protected Text getDashText() {
        return this.dash;
    }

    private void initBiomeText(Biome biome) {
        this.biome = biome;
        this.biomeText = Text.newText(biome.toString()).setFontSize(UiSettings.NORM_FONT).rightAlign().create();
        this.biomeText.setColour(biome.getColour());
    }

    private void initCountText(int count) {
        this.count = count;
        this.countText = Text.newText(String.valueOf(count) + "%").setFontSize(UiSettings.NORM_FONT).create();
        this.countText.setColour(this.biome.getColour());
    }

    private void initDashText() {
        this.dash = Text.newText("-").center().setFontSize(UiSettings.NORM_FONT).create();
        this.dash.setColour(ColourPalette.WHITE);
    }

    private void updateBiome(Biome biome) {
        this.biome = biome;
        this.biomeText.setText(biome.toString());
        this.biomeText.setColour(biome.getColour());
        this.countText.setColour(biome.getColour());
    }

    private void updateCount(int newCount) {
        this.count = newCount;
        this.countText.setText(String.valueOf(newCount) + "%");
        this.countText.setColour(this.biome.getColour());
    }
}

