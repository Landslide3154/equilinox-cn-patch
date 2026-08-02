/*
 * Decompiled with CFR 0.152.
 */
package hunting;

import classification.Classification;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import hunting.FleeComponent;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class FleeCompBlueprint
extends ComponentBlueprint {
    private static final String HIDE_ABILITY = GameText.getText(484);
    private static final String ABILITY = GameText.getText(400);
    public final float safeRangeSquared;
    public final Classification hidingSpot;
    public final boolean canSwim;
    public final boolean canGoOnLand;

    protected FleeCompBlueprint(float safeRange, Classification hidingSpot, boolean swims, boolean land) {
        super(ComponentType.FLEE);
        this.safeRangeSquared = safeRange * safeRange;
        this.hidingSpot = hidingSpot;
        this.canGoOnLand = land;
        this.canSwim = swims;
    }

    @Override
    public Component createInstance() {
        return new FleeComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> infos) {
        infos.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
        if (this.hidingSpot != null) {
            infos.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", String.valueOf(HIDE_ABILITY) + " " + this.hidingSpot.getName()));
        }
    }
}

