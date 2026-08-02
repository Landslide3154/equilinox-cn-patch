/*
 * Decompiled with CFR 0.152.
 */
package equipping;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import death.FadeDeath;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.PopUpInfoGui;
import entityInfoGui.TextInfo;
import equipping.ItemCompBlueprint;
import gameManaging.GameManager;
import instances.Entity;
import java.io.IOException;
import java.util.List;
import languages.GameText;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;

public class ItemComponent
extends Component {
    private static final String DECAY_TITLE = GameText.getText(195);
    private static final String SECONDS = GameText.getText(196);
    private static final float FADE_TIME = 0.5f;
    private static final float VARIANCE = 0.15f;
    private final ItemCompBlueprint blueprint;
    private Entity entity;
    private float timeTillDecay;

    protected ItemComponent(ItemCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
        this.resetDecayTime();
    }

    @Override
    public void update() {
        super.update();
        this.timeTillDecay -= GameManager.getGameSeconds();
        if (this.timeTillDecay < 0.0f) {
            this.entity.die(new FadeDeath(0.5f, this.entity), false);
        }
    }

    public void resetDecayTime() {
        this.timeTillDecay = Maths.getRandomVariance(this.blueprint.averageDecayTime, 0.15f);
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
        info.add(new TextInfo(DECAY_TITLE, EntityInfoGui.FONT_SIZE){

            @Override
            public String getValue() {
                return String.valueOf((int)ItemComponent.this.timeTillDecay + 1) + " " + SECONDS;
            }
        });
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeFloat(this.timeTillDecay);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.entity = bundle.getEntity();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.timeTillDecay = reader.readFloat();
        this.create(bundle);
    }
}

