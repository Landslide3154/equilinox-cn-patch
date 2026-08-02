/*
 * Decompiled with CFR 0.152.
 */
package monkeys;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import languages.GameText;
import monkeys.TreeSwingComponent;
import org.lwjgl.util.vector.Vector3f;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class TreeSwingCompBlueprint
extends ComponentBlueprint {
    private static final String ABILITY = GameText.getText(425);
    protected final Vector3f handPos;

    protected TreeSwingCompBlueprint(Vector3f handPos) {
        super(ComponentType.TREE_SWING);
        this.handPos = handPos;
    }

    @Override
    public Component createInstance() {
        return new TreeSwingComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
    }
}

