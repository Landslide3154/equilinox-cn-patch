/*
 * Decompiled with CFR 0.152.
 */
package loot;

import blueprints.Blueprint;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import loot.DropComponent;
import resourceManagement.BlueprintRepository;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class DropCompBlueprint
extends ComponentBlueprint {
    private final int dropBlueprintId;

    protected DropCompBlueprint(int dropBlueprintId) {
        super(ComponentType.DROP);
        this.dropBlueprintId = dropBlueprintId;
    }

    public Blueprint getDropBlueprint() {
        return BlueprintRepository.getBlueprint(this.dropBlueprintId);
    }

    @Override
    public Component createInstance() {
        return new DropComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
    }
}

