/*
 * Decompiled with CFR 0.152.
 */
package environmentWarning;

import environment.EnviroCompBlueprint;
import environment.EnviroFactor;
import environment.EnviroFactorBlueprint;
import gameManaging.GameManager;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import terrains.TerrainVertex;

public class EnvironmentProbe {
    private final int range;
    private List<EnviroFactor> factors = new ArrayList<EnviroFactor>();
    private float environmentSatisfaction = 1.0f;

    public EnvironmentProbe(EnviroCompBlueprint enviroComp, int range) {
        this.range = range;
        for (EnviroFactorBlueprint factorBlueprint : enviroComp.getFactorBlueprints()) {
            this.factors.add(factorBlueprint.createInstance());
        }
    }

    public void recalculate(Vector3f terrainPos) {
        this.environmentSatisfaction = 1.0f;
        TerrainVertex vertex = GameManager.getWorld().getTerrainVertex(terrainPos.x, terrainPos.z);
        if (vertex == null) {
            return;
        }
        for (EnviroFactor factor : this.factors) {
            float multiplier = factor.recalculate(terrainPos, vertex, this.range);
            float value = 1.0f - factor.getInfluence() + multiplier * factor.getInfluence();
            this.environmentSatisfaction *= value;
        }
    }

    public float getSatisfaction() {
        return this.environmentSatisfaction;
    }

    public List<EnviroFactor> getFactors() {
        return this.factors;
    }
}

