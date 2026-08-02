/*
 * Decompiled with CFR 0.152.
 */
package ai;

import ai.EggAi;
import ai.FollowParentAi;
import ai.StopStartWithSwimAi;
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

public class TortoiseAi
implements AiProgramBlueprint {
    private float minIdleTime = 5.0f;
    private float maxIdleTime = 10.0f;

    @Override
    public Ai createInstance(ComponentBundle bundle) {
        MovementComp movementComp = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        GrowthComponent grower = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        InformationComponent info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        Transformation transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        EggAi eggAi = new EggAi();
        FollowParentAi youngAi = new FollowParentAi(movementComp, transform, info);
        StopStartWithSwimAi grownAi = new StopStartWithSwimAi(movementComp, transform, info);
        grownAi.setIdleTimes(this.minIdleTime, this.maxIdleTime);
        return new AgeDependentAI(grower, eggAi, youngAi, grownAi);
    }

    @Override
    public void loadSettings(CSVReader reader) {
    }
}

