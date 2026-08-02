/*
 * Decompiled with CFR 0.152.
 */
package particleComponent;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import particleComponent.ParticleComponent;
import particles.ParticleSystem;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class ParticleCompBlueprint
extends ComponentBlueprint {
    private float rangeSquared;
    private ParticleSystem system;
    private int[] modelStages;
    private boolean takesMaterial;

    protected ParticleCompBlueprint(ParticleSystem system, float range, boolean takesMaterial, int[] modelStages) {
        super(ComponentType.PARTICLES);
        this.system = system;
        this.modelStages = modelStages;
        this.takesMaterial = takesMaterial;
        this.rangeSquared = range * range;
    }

    @Override
    public Component createInstance() {
        return new ParticleComponent(this);
    }

    public boolean doesTakeMaterial() {
        return this.takesMaterial;
    }

    public float getRangeSquared() {
        return this.rangeSquared;
    }

    @Override
    public void delete() {
    }

    public boolean isActiveStage(int stage) {
        int[] nArray = this.modelStages;
        int n = this.modelStages.length;
        int n2 = 0;
        while (n2 < n) {
            int modelStage = nArray[n2];
            if (modelStage == stage) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }

    protected ParticleSystem getSystem() {
        return this.system;
    }
}

