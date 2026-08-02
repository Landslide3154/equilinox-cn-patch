/*
 * Decompiled with CFR 0.152.
 */
package spitting;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import spitting.ProjectileComponent;

public class ProjectileCompBlueprint
extends ComponentBlueprint {
    protected ProjectileCompBlueprint() {
        super(ComponentType.PROJECTILE);
    }

    @Override
    public Component createInstance() {
        return new ProjectileComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}

