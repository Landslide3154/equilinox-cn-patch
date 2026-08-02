/*
 * Decompiled with CFR 0.152.
 */
package death;

import death.DeathAiBlueprint;
import death.DeathAiLoader;
import death.FadeDeathBlueprint;
import death.FallDeathBlueprint;
import death.FloaterDeathBlueprint;
import death.ParticleDeathBlueprint;
import death.SpawnDeathBlueprint;
import death.UpDownDeathBlueprint;
import utils.CSVReader;

public enum DeathAiType {
    FADE_DEATH(new DeathAiLoader(){

        @Override
        public DeathAiBlueprint loadDeathAi(CSVReader reader) {
            return new FadeDeathBlueprint().loadInfo(reader);
        }
    }),
    PARTICLE_DEATH(new DeathAiLoader(){

        @Override
        public DeathAiBlueprint loadDeathAi(CSVReader reader) {
            return new ParticleDeathBlueprint().loadInfo(reader);
        }
    }),
    FALL_DEATH(new DeathAiLoader(){

        @Override
        public DeathAiBlueprint loadDeathAi(CSVReader reader) {
            return new FallDeathBlueprint().loadInfo(reader);
        }
    }),
    UP_DOWN_DEATH(new DeathAiLoader(){

        @Override
        public DeathAiBlueprint loadDeathAi(CSVReader reader) {
            return new UpDownDeathBlueprint().loadInfo(reader);
        }
    }),
    SPAWN_DEATH(new DeathAiLoader(){

        @Override
        public DeathAiBlueprint loadDeathAi(CSVReader reader) {
            return new SpawnDeathBlueprint().loadInfo(reader);
        }
    }),
    FLOAT_DEATH(new DeathAiLoader(){

        @Override
        public DeathAiBlueprint loadDeathAi(CSVReader reader) {
            return new FloaterDeathBlueprint().loadInfo(reader);
        }
    });

    private DeathAiLoader loader;

    private DeathAiType(DeathAiLoader loader) {
        this.loader = loader;
    }

    public DeathAiBlueprint load(CSVReader reader) {
        return this.loader.loadDeathAi(reader);
    }
}

