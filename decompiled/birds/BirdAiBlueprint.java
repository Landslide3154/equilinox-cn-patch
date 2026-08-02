/*
 * Decompiled with CFR 0.152.
 */
package birds;

import ai.StopStartAi;
import aiBasics.AgeDependentAI;
import aiBasics.AiRoutine;
import aiComponent.Ai;
import aiComponent.AiProgramBlueprint;
import baseMovement.MovementComp;
import birds.BabyBirdAi;
import birds.GrownBirdAi;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import growth.GrowthComponent;
import perching.PercherComponent;
import toolbox.Transformation;
import utils.CSVReader;

public class BirdAiBlueprint
implements AiProgramBlueprint {
    private float circleRot = 50.0f;
    private float circleMinTime = 2.0f;

    @Override
    public Ai createInstance(ComponentBundle bundle) {
        MovementComp mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        GrowthComponent grower = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        InformationComponent info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        Transformation transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        PercherComponent perchComp = (PercherComponent)bundle.getComponent(ComponentType.PERCHER);
        AiRoutine baby = perchComp != null ? new StopStartAi(mover, transform, info, true) : new BabyBirdAi(mover, transform, info, true);
        return new AgeDependentAI(grower, baby, new GrownBirdAi(this, mover, transform, info, perchComp));
    }

    @Override
    public void loadSettings(CSVReader reader) {
        if (!reader.isEndOfLine()) {
            this.circleRot = reader.getNextLabelFloat();
            this.circleMinTime = reader.getNextLabelFloat();
        }
    }

    public float getCircleRot() {
        return this.circleRot;
    }

    public float getCircleMinTime() {
        return this.circleMinTime;
    }
}

