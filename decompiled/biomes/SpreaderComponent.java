/*
 * Decompiled with CFR 0.152.
 */
package biomes;

import biomes.SpreaderCompBlueprint;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import entityInfoGui.PopUpInfoGui;
import java.util.List;
import utils.BinaryReader;
import utils.BinaryWriter;

public class SpreaderComponent
extends Component {
    private final SpreaderCompBlueprint spreadData;

    public SpreaderComponent(SpreaderCompBlueprint spreadData) {
        super(spreadData);
        this.spreadData = spreadData;
    }

    public SpreaderCompBlueprint getSpreadData() {
        return this.spreadData;
    }

    @Override
    public void create(ComponentBundle bundle) {
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void export(BinaryWriter writer) {
    }
}

