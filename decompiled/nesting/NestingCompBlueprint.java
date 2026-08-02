/*
 * Decompiled with CFR 0.152.
 */
package nesting;

import classification.Classification;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import languages.GameText;
import nesting.NestingComponent;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class NestingCompBlueprint
extends ComponentBlueprint {
    private static final String ABILITY = GameText.getText(430);
    protected final boolean decreasesModel;
    protected final int hatchStage;
    protected final Classification nest;

    protected NestingCompBlueprint(Classification nest, int hatchStage, boolean descreaseModel) {
        super(ComponentType.NESTING);
        this.nest = nest;
        this.decreasesModel = descreaseModel;
        this.hatchStage = hatchStage;
    }

    @Override
    public Component createInstance() {
        return new NestingComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
    }
}

