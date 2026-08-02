/*
 * Decompiled with CFR 0.152.
 */
package birds;

import gameManaging.GameManager;
import toolbox.Maths;
import toolbox.Transformation;

public class BirdSittingAnimation {
    private static final float SPEED = 0.5f;
    private static final float ROCK_AMOUNT = 7.0f;
    private final Transformation transform;
    private float startingRotX;
    private float time = 0.0f;

    protected BirdSittingAnimation(Transformation transform) {
        this.transform = transform;
    }

    protected void indicateStart() {
        this.time = 0.0f;
    }

    protected void doAnimation() {
        this.time += GameManager.getGameSeconds() * 0.5f;
        if (this.time >= 1.0f) {
            this.time = 0.0f;
        }
        this.transform.setXRotation(Maths.fakeSin(this.startingRotX - 7.0f, this.startingRotX + 7.0f, this.time));
    }
}

