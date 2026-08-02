/*
 * Decompiled with CFR 0.152.
 */
package spitting;

import breedingTraits.FloatTraitBlueprint;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import languages.GameText;
import org.lwjgl.util.vector.Vector4f;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import spitting.SpitComponent;

public class SpitCompBlueprint
extends ComponentBlueprint {
    private static final String ABILITY = GameText.getText(1102);
    private static final String ANGER = GameText.getText(1103);
    private Vector4f spitPosition;

    protected SpitCompBlueprint(Vector4f spitPosition) {
        super(ComponentType.SPITTING);
        this.spitPosition = spitPosition;
        super.addTrait(new FloatTraitBlueprint(ANGER, 1.0f, 2.0f, 11.5f){

            @Override
            public String formatTrait(float value) {
                return String.valueOf(Math.round(value * 100.0f)) + "%";
            }
        });
    }

    public Vector4f getSpitPosition() {
        return this.spitPosition;
    }

    @Override
    public Component createInstance() {
        return new SpitComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
    }
}

