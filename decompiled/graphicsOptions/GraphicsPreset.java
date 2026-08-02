/*
 * Decompiled with CFR 0.152.
 */
package graphicsOptions;

import languages.GameText;

public enum GraphicsPreset {
    LOWEST(GameText.getText(971), false, false, false, false, false, false),
    LOW(GameText.getText(972), true, false, false, false, false, true),
    MEDIUM(GameText.getText(973), true, true, true, false, false, true),
    HIGH(GameText.getText(974), true, true, true, false, true, true),
    CUSTOM(GameText.getText(970), true, true, true, true, true, true);

    private final String name;
    private boolean shadows;
    private boolean water;
    private boolean aa;
    private boolean dof;
    private boolean shafts;
    private boolean flare;

    private GraphicsPreset(String name, boolean shadows, boolean water, boolean aa, boolean dof, boolean shafts, boolean flare) {
        this.name = name;
        this.shadows = shadows;
        this.water = water;
        this.aa = aa;
        this.dof = dof;
        this.shafts = shafts;
        this.flare = flare;
    }

    public static GraphicsPreset[] getPresets() {
        GraphicsPreset[] presets = new GraphicsPreset[GraphicsPreset.values().length - 1];
        int i = 0;
        while (i < presets.length) {
            presets[i] = GraphicsPreset.values()[i];
            ++i;
        }
        return presets;
    }

    public String toString() {
        return this.name;
    }

    public boolean isShadows() {
        return this.shadows;
    }

    public void setShadows(boolean shadows) {
        this.shadows = shadows;
    }

    public boolean isWater() {
        return this.water;
    }

    public void setWater(boolean water) {
        this.water = water;
    }

    public boolean isAa() {
        return this.aa;
    }

    public void setAa(boolean aa) {
        this.aa = aa;
    }

    public boolean isDof() {
        return this.dof;
    }

    public void setDof(boolean dof) {
        this.dof = dof;
    }

    public boolean isShafts() {
        return this.shafts;
    }

    public void setShafts(boolean shafts) {
        this.shafts = shafts;
    }

    public boolean isFlare() {
        return this.flare;
    }

    public void setFlare(boolean flare) {
        this.flare = flare;
    }
}

