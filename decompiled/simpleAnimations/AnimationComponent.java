/*
 * Decompiled with CFR 0.152.
 */
package simpleAnimations;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import java.util.List;
import simpleAnimations.Animation;
import simpleAnimations.AnimationCompBlueprint;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class AnimationComponent
extends Component {
    private AnimationCompBlueprint blueprint;
    private Animation animation;

    protected AnimationComponent(AnimationCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void create(ComponentBundle bundle) {
        Transformation transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.animation = this.blueprint.getAnimBlueprint().createInstance(transform);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void update() {
        this.animation.carryOut();
    }

    @Override
    public void export(BinaryWriter writer) {
    }
}

