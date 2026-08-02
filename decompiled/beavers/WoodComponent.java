/*
 * Decompiled with CFR 0.152.
 */
package beavers;

import beavers.WoodCompBlueprint;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import entityInfoGui.PopUpInfoGui;
import java.util.List;
import toolbox.Colour;
import utils.BinaryReader;
import utils.BinaryWriter;

public class WoodComponent
extends Component {
    private WoodCompBlueprint blueprint;

    protected WoodComponent(WoodCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    public float getCuttingTime() {
        return this.blueprint.cuttingTime;
    }

    public float getBarkFactor() {
        return this.blueprint.barkFactor;
    }

    public Colour getWoodColour() {
        return this.blueprint.barkColour;
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    @Override
    public void create(ComponentBundle bundle) {
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
    }
}

