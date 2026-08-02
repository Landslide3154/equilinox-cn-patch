/*
 * Decompiled with CFR 0.152.
 */
package simpleAnimations;

import gameManaging.GameManager;
import simpleAnimations.Animation;
import simpleAnimations.AnimationBlueprint;
import toolbox.Maths;
import toolbox.Transformation;
import utils.CSVReader;

public class BounceAnimation
implements Animation {
    private final Transformation transform;
    private final BounceAnimationBlueprint blueprint;
    private float time;

    protected BounceAnimation(Transformation transform, BounceAnimationBlueprint blueprint) {
        this.transform = transform;
        this.blueprint = blueprint;
        this.time = Maths.RANDOM.nextFloat() * blueprint.period;
    }

    @Override
    public void carryOut() {
        this.time += GameManager.getGameSeconds();
        this.time %= this.blueprint.period;
        float terrainHeight = GameManager.getWorld().getHeightOfTerrain(this.transform.getPosition().x, this.transform.getPosition().z);
        double phase = (double)(this.time / this.blueprint.period * 2.0f) * Math.PI;
        float wave = (float)(Math.sin(phase) * 0.5 + 0.5) * this.blueprint.height;
        this.transform.setYPosition(terrainHeight + wave);
    }

    protected static class BounceAnimationBlueprint
    implements AnimationBlueprint {
        private final float height;
        private final float period;

        protected BounceAnimationBlueprint(CSVReader reader) {
            this.height = reader.getNextFloat();
            this.period = reader.getNextFloat();
        }

        @Override
        public Animation createInstance(Transformation transform) {
            return new BounceAnimation(transform, this);
        }
    }
}

