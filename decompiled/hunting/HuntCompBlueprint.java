/*
 * Decompiled with CFR 0.152.
 */
package hunting;

import classification.Classification;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import hunting.HuntComponent;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class HuntCompBlueprint
extends ComponentBlueprint {
    private static final String HUNTS = GameText.getText(474);
    protected final int huntingRange;
    protected final Classification[] prey;
    protected final boolean huntsYoung;
    protected final boolean huntsOld;

    protected HuntCompBlueprint(int huntingRange, Classification[] prey, boolean huntsYoung, boolean huntsOld) {
        super(ComponentType.HUNT);
        this.huntingRange = huntingRange;
        this.prey = prey;
        this.huntsYoung = huntsYoung;
        this.huntsOld = huntsOld;
    }

    @Override
    public Component createInstance() {
        return new HuntComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        String preyString = "";
        int i = 0;
        while (i < this.prey.length) {
            preyString = String.valueOf(preyString) + this.prey[i].getName();
            if (i < this.prey.length - 1) {
                preyString = String.valueOf(preyString) + ", ";
            }
            ++i;
        }
        info.get((Object)SpeciesInfoType.PREFERENCES).add(new SpeciesInfoLine(HUNTS, preyString));
    }
}

