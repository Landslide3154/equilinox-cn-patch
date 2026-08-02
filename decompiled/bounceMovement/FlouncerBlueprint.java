/*
 * Decompiled with CFR 0.152.
 */
package bounceMovement;

import bounceMovement.BounceBaseBlueprint;
import bounceMovement.FlouncerMovement;
import componentArchitecture.Component;

public class FlouncerBlueprint
extends BounceBaseBlueprint {
    public static final int ID = 8;
    protected final float bounceRotation;
    protected final float standardHeight;

    public FlouncerBlueprint(float speed, float rotSpeed, float bouncePower, float bounceRot, float standardHeight) {
        super(speed, rotSpeed, bouncePower);
        this.bounceRotation = bounceRot;
        this.standardHeight = standardHeight;
    }

    @Override
    public Component createInstance() {
        return new FlouncerMovement(this);
    }
}

