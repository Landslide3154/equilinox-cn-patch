/*
 * Decompiled with CFR 0.152.
 */
package fighting;

import classification.Classification;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import fighting.HostileComponent;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class HostileCompBlueprint
extends ComponentBlueprint {
    private static final String ABILITY = GameText.getText(445);
    protected final Classification enemyClass;
    protected final float averageAttackTime;
    protected final boolean shouldNotify;

    protected HostileCompBlueprint(Classification enemyClass, float averageAttackTime, boolean shouldNotifyPrey) {
        super(ComponentType.HOSTILE);
        this.shouldNotify = shouldNotifyPrey;
        this.enemyClass = enemyClass;
        this.averageAttackTime = averageAttackTime;
    }

    @Override
    public Component createInstance() {
        return new HostileComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
    }
}

