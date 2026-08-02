/*
 * Decompiled with CFR 0.152.
 */
package bounceMovement;

import bounceMovement.BounceBaseBlueprint;
import bounceMovement.WaddleMovement;
import componentArchitecture.Component;

public class WaddleMoveBlueprint
extends BounceBaseBlueprint {
    public static final int ID = 15;

    public WaddleMoveBlueprint(float speed, float rotSpeed, float bouncePower) {
        super(speed, rotSpeed, bouncePower);
    }

    @Override
    public Component createInstance() {
        return new WaddleMovement(this);
    }
}

