/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import basics.DisplayManager;
import textures.Texture;
import userInterfaces.GuiImage;

public class SplashScreenLogo
extends GuiImage {
    private static final float MOVE_AMOUNT = 0.2f;
    private static final float FACTOR = 5.0f;
    private float targetX;
    private boolean left;

    public SplashScreenLogo(Texture image, boolean left) {
        super(image);
        this.left = left;
    }

    @Override
    protected void init() {
        super.init();
        float flipper = this.left ? -1 : 1;
        this.targetX = super.getRelativeX() + 0.2f * flipper;
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        float difference = this.targetX - super.getRelativeX();
        float change = difference * DisplayManager.getDeltaSeconds() * 5.0f;
        super.setRelativeX(super.getRelativeX() + change);
    }
}

