/*
 * Decompiled with CFR 0.152.
 */
package ai;

import ai.EggAi;
import ai.FollowParentAi;
import ai.StopStartAi;
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

public class WalkingBirdAiBlueprint
implements AiProgramBlueprint {
    private float minIdleTime;
    private float maxIdleTime;
    private boolean stayOnLand;

    @Override
    public Ai createInstance(ComponentBundle bundle) {
        MovementComp movementComp = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        GrowthComponent grower = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        InformationComponent info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        Transformation transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        EggAi eggAi = new EggAi();
        FollowParentAi youngAi = new FollowParentAi(movementComp, transform, info);
        StopStartAi grownAi = new StopStartAi(movementComp, transform, info, this.stayOnLand);
        grownAi.setIdleTimes(this.minIdleTime, this.maxIdleTime);
        return new AgeDependentAI(grower, eggAi, youngAi, grownAi);
    }

    @Override
    public void loadSettings(CSVReader reader) {
        this.minIdleTime = reader.getNextLabelFloat();
        this.maxIdleTime = reader.getNextLabelFloat();
        this.stayOnLand = reader.getNextLabelBool();
    }
}

