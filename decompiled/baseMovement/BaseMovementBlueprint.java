/*
 * Decompiled with CFR 0.152.
 */
package baseMovement;

import breedingTraits.FloatTraitBlueprint;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import languages.GameText;

public abstract class BaseMovementBlueprint
extends ComponentBlueprint {
    private static final String SPEED = GameText.getText(931);
    private static final String M_PER_S = GameText.getText(932);
    public final float rotSpeed;
    public final boolean hasEggStage;
    private float runFactor = 2.0f;

    protected BaseMovementBlueprint(float baseSpeed, float rotSpeed) {
        super(ComponentType.MOVEMENT);
        this.rotSpeed = rotSpeed;
        this.hasEggStage = false;
        this.addSpeedTrait(baseSpeed);
    }

    protected BaseMovementBlueprint(float baseSpeed, float rotSpeed, boolean eggStage) {
        super(ComponentType.MOVEMENT);
        this.rotSpeed = rotSpeed;
        this.hasEggStage = eggStage;
        this.addSpeedTrait(baseSpeed);
    }

    protected BaseMovementBlueprint(float baseSpeed) {
        super(ComponentType.MOVEMENT);
        this.hasEggStage = false;
        this.rotSpeed = 180.0f;
        this.addSpeedTrait(baseSpeed);
    }

    public void setRunFactor(float runFactor) {
        this.runFactor = runFactor;
    }

    public float getRunFactor() {
        return this.runFactor;
    }

    private void addSpeedTrait(float speed) {
        super.addTrait(new FloatTraitBlueprint(SPEED, speed, 3.0f, 9.0f){

            @Override
            public String formatTrait(float value) {
                String valueShort = String.format("%.1f", Float.valueOf(value * 10.0f));
                return String.valueOf(valueShort) + " " + M_PER_S;
            }
        });
    }
}

