/*
 * Decompiled with CFR 0.152.
 */
package ai;

import ai.FollowParentAi;
import ai.StopStandAi;
import aiBasics.AgeDependentAI;
import aiComponent.Ai;
import aiComponent.AiProgramBlueprint;
import baseMovement.MovementComp;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import growth.GrowthComponent;
import toolbox.Transformation;
import utils.CSVReader;

public class MeerkatAi
implements AiProgramBlueprint {
    private float minIdleTime = 7.0f;
    private float maxIdleTime = 15.0f;

    @Override
    public Ai createInstance(ComponentBundle bundle) {
        MovementComp movementComp = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        GrowthComponent grower = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        InformationComponent info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        Transformation transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        FollowParentAi youngAi = new FollowParentAi(movementComp, transform, info);
        StopStandAi grownAi = new StopStandAi(movementComp, transform, info);
        grownAi.setIdleTimes(this.minIdleTime, this.maxIdleTime);
        return new AgeDependentAI(grower, youngAi, grownAi);
    }

    @Override
    public void loadSettings(CSVReader reader) {
        if (!reader.isEndOfLine()) {
            this.minIdleTime = reader.getNextLabelFloat();
            this.maxIdleTime = reader.getNextLabelFloat();
        }
    }
}

