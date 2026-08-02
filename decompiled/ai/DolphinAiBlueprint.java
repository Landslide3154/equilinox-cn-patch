/*
 * Decompiled with CFR 0.152.
 */
package ai;

import ai.DolphinAi;
import aiComponent.Ai;
import aiComponent.AiProgramBlueprint;
import baseMovement.MovementComp;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import toolbox.Transformation;
import utils.CSVReader;

public class DolphinAiBlueprint
implements AiProgramBlueprint {
    @Override
    public Ai createInstance(ComponentBundle bundle) {
        MovementComp movementComp = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        InformationComponent info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        Transformation transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        return new DolphinAi(info, movementComp, transform);
    }

    @Override
    public void loadSettings(CSVReader reader) {
    }
}

