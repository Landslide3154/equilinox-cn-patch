/*
 * Decompiled with CFR 0.152.
 */
package edgeCovering;

import blueprints.Blueprint;
import componentArchitecture.ComponentParams;
import dataManagement.DataUpdateManager;
import instances.Entity;
import resourceManagement.BlueprintRepository;

public class EdgeManager {
    private Entity edge;

    public EdgeManager(DataUpdateManager sceneData, float edgeHeight, float scale) {
        Blueprint blueprint = BlueprintRepository.getBlueprint(1003);
        this.edge = blueprint.createInstance(new ComponentParams[0]);
        this.edge.turnOffShadow();
        this.edge.getTransform().setScale(scale);
        this.edge.getTransform().setYPosition(edgeHeight);
        sceneData.addDynamicEntity(this.edge);
    }

    public void update() {
    }
}

