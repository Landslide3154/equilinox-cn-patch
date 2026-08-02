/*
 * Decompiled with CFR 0.152.
 */
package aiComponent;

import ai.BeeAiBlueprint;
import ai.DolphinAiBlueprint;
import ai.MeerkatAi;
import ai.PatrolAiBlueprint;
import ai.PatrolWithSwimAi;
import ai.SwimAiBlueprint;
import ai.TortoiseAi;
import ai.WalkingBirdAiBlueprint;
import aiComponent.AiProgramBlueprint;
import birds.BirdAiBlueprint;
import utils.CSVReader;

public enum AiProgramType {
    PATROL{

        @Override
        protected AiProgramBlueprint createProgramBlueprint() {
            return new PatrolAiBlueprint();
        }
    }
    ,
    SWIM{

        @Override
        protected AiProgramBlueprint createProgramBlueprint() {
            return new SwimAiBlueprint();
        }
    }
    ,
    BIRD{

        @Override
        protected AiProgramBlueprint createProgramBlueprint() {
            return new BirdAiBlueprint();
        }
    }
    ,
    WALKING_BIRD{

        @Override
        protected AiProgramBlueprint createProgramBlueprint() {
            return new WalkingBirdAiBlueprint();
        }
    }
    ,
    BEE{

        @Override
        protected AiProgramBlueprint createProgramBlueprint() {
            return new BeeAiBlueprint();
        }
    }
    ,
    PATROL_WITH_SWIM{

        @Override
        protected AiProgramBlueprint createProgramBlueprint() {
            return new PatrolWithSwimAi();
        }
    }
    ,
    TORTOISE{

        @Override
        protected AiProgramBlueprint createProgramBlueprint() {
            return new TortoiseAi();
        }
    }
    ,
    MEERKAT{

        @Override
        protected AiProgramBlueprint createProgramBlueprint() {
            return new MeerkatAi();
        }
    }
    ,
    DOLPHIN{

        @Override
        protected AiProgramBlueprint createProgramBlueprint() {
            return new DolphinAiBlueprint();
        }
    };


    private AiProgramType() {
    }

    public AiProgramBlueprint loadProgramBlueprint(CSVReader reader) {
        AiProgramBlueprint program = this.createProgramBlueprint();
        program.loadSettings(reader);
        return program;
    }

    protected abstract AiProgramBlueprint createProgramBlueprint();

    /* synthetic */ AiProgramType(String string, int n, AiProgramType aiProgramType) {
        this();
    }
}

