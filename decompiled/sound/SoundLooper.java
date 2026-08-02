/*
 * Decompiled with CFR 0.152.
 */
package sound;

import audio.SoundLoopingNode;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import instances.EntityListener;
import java.util.List;
import sound.SoundLooperBlueprint;
import toolbox.TransformChangeListener;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class SoundLooper
extends Component {
    private final SoundLooperBlueprint blueprint;
    private SoundLoopingNode soundNode;

    protected SoundLooper(SoundLooperBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void update() {
        super.update();
        this.soundNode.update();
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
        final Transformation transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.soundNode = new SoundLoopingNode(this.blueprint.soundEffect, transform.getPosition());
        transform.addChangeListener(new TransformChangeListener(){

            @Override
            public void transformChanged() {
                SoundLooper.this.soundNode.updatePosition(transform.getPosition());
            }
        });
        bundle.getEntity().getNotifier().addRemoveListener(new EntityListener(){

            @Override
            public void execute() {
                SoundLooper.this.soundNode.stopPlaying();
            }
        });
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }
}

