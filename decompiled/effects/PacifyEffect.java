/*
 * Decompiled with CFR 0.152.
 */
package effects;

import componentArchitecture.ComponentType;
import effects.Effect;
import fighting.HostileComponent;
import instances.Entity;
import java.util.List;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;

public class PacifyEffect
implements Effect {
    private static final String ABILITY = GameText.getText(452);
    private final float time;

    public PacifyEffect(float time) {
        this.time = time;
    }

    @Override
    public void apply(Entity entity) {
        HostileComponent hostileComp = (HostileComponent)entity.getComponent(ComponentType.HOSTILE);
        if (hostileComp != null) {
            hostileComp.pacify(this.time);
        }
    }

    @Override
    public void getInfo(List<SpeciesInfoLine> info) {
        info.add(new SpeciesInfoLine("Ability", ABILITY));
    }
}

