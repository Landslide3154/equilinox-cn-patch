/*
 * Decompiled with CFR 0.152.
 */
package aiComponent;

import aiComponent.Ai;
import componentArchitecture.ComponentBundle;
import utils.CSVReader;

public interface AiProgramBlueprint {
    public Ai createInstance(ComponentBundle var1);

    public void loadSettings(CSVReader var1);
}

