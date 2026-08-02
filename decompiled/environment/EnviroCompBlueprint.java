/*
 * Decompiled with CFR 0.152.
 */
package environment;

import environment.EnviroComponent;
import environment.EnviroFactor;
import environment.EnviroFactorBlueprint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.lwjgl.util.vector.Vector3f;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import utils.BinaryReader;

public class EnviroCompBlueprint {
    private final List<EnviroFactorBlueprint> factorBlueprints;

    public EnviroCompBlueprint(List<EnviroFactorBlueprint> factorBlueprints) {
        this.factorBlueprints = factorBlueprints;
        Collections.sort(factorBlueprints);
    }

    public List<EnviroFactorBlueprint> getFactorBlueprints() {
        return this.factorBlueprints;
    }

    public EnviroComponent createInstance(Vector3f basePosition, int range) {
        ArrayList<EnviroFactor> components = new ArrayList<EnviroFactor>();
        for (EnviroFactorBlueprint factorBlueprint : this.factorBlueprints) {
            components.add(factorBlueprint.createInstance());
        }
        return new EnviroComponent(components, basePosition, range);
    }

    public EnviroComponent loadInstance(BinaryReader reader, Vector3f basePosition, int range) throws Exception {
        float environmentSatisfaction = reader.readFloat();
        ArrayList<EnviroFactor> components = new ArrayList<EnviroFactor>();
        for (EnviroFactorBlueprint factorBlueprint : this.factorBlueprints) {
            components.add(factorBlueprint.createInstance());
        }
        return new EnviroComponent(components, environmentSatisfaction, basePosition, range);
    }

    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        for (EnviroFactorBlueprint factor : this.factorBlueprints) {
            info.get((Object)SpeciesInfoType.PREFERENCES).add(factor.getInfo());
        }
    }
}

