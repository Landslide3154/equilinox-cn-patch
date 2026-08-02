/*
 * Decompiled with CFR 0.152.
 */
package fruit;

import componentArchitecture.ComponentParams;
import componentArchitecture.ComponentType;
import org.lwjgl.util.vector.Vector3f;

public class DecayParams
extends ComponentParams {
    private final Vector3f initialVelcoity;

    public DecayParams(Vector3f initialVelcoity) {
        super(ComponentType.DECAY);
        this.initialVelcoity = initialVelcoity;
    }

    public Vector3f getInitialVelcoity() {
        return this.initialVelcoity;
    }
}

