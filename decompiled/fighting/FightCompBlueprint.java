/*
 * Decompiled with CFR 0.152.
 */
package fighting;

import breedingTraits.FloatTraitBlueprint;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import fighting.AttackAnimation;
import fighting.FightComponent;
import fighting.LungeAnimation;
import fighting.StingAnimation;
import java.util.List;
import java.util.Map;
import languages.GameText;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class FightCompBlueprint
extends ComponentBlueprint {
    private static final int NORMAL_ANIM = 0;
    private static final int STING_ANIM = 1;
    private static final String DAMAGE_TEXT = GameText.getText(192);
    private static final String ABILITY = GameText.getText(401);
    private final int damage;
    protected final boolean takesRevenge;
    protected final float biteRange;
    protected final float pauseTime;
    private final int animationId;

    protected FightCompBlueprint(int damage, boolean takesRevenge, int animationId, float biteRange, float pause) {
        super(ComponentType.FIGHT);
        super.addTrait(new FloatTraitBlueprint("Attack Power", damage, 1.3f, 18.0f){

            @Override
            public String formatTrait(float value) {
                return Integer.toString(Math.round(value * 10.0f));
            }
        });
        this.damage = damage;
        this.animationId = animationId;
        this.biteRange = biteRange;
        this.takesRevenge = takesRevenge;
        this.pauseTime = pause;
    }

    @Override
    public Component createInstance() {
        return new FightComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.GENERAL).add(new SpeciesInfoLine(DAMAGE_TEXT, Integer.toString(this.damage)));
        if (this.takesRevenge) {
            info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
        }
    }

    public AttackAnimation getAnimation() {
        if (this.animationId == 0) {
            return new LungeAnimation();
        }
        if (this.animationId == 1) {
            return new StingAnimation();
        }
        return new LungeAnimation();
    }
}

