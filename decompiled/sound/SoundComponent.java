/*
 * Decompiled with CFR 0.152.
 */
package sound;

import audio.SoundEffect;
import audio.SoundEmitter;
import basics.DisplayManager;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import java.util.List;
import toolbox.TransformChangeListener;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class SoundComponent
extends Component {
    private Transformation transform;
    private SoundEmitter sounder;

    protected SoundComponent(ComponentBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    public void playEffect(SoundEffect sound, float pitch) {
        this.sounder.playSound(sound, pitch);
    }

    @Override
    public void update() {
        this.sounder.update(DisplayManager.getDeltaSeconds());
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.sounder = new SoundEmitter(this.transform.getPosition());
        this.transform.addChangeListener(new TransformChangeListener(){

            @Override
            public void transformChanged() {
                SoundComponent.this.sounder.updatePosition();
            }
        });
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }
}

