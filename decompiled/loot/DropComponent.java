/*
 * Decompiled with CFR 0.152.
 */
package loot;

import blueprints.Blueprint;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import instances.Entity;
import instances.EntityListener;
import java.util.List;
import loot.DropCompBlueprint;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class DropComponent
extends Component {
    private DropCompBlueprint blueprint;
    private boolean hunted = false;

    protected DropComponent(DropCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
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

    public void indicateHunted() {
        this.hunted = true;
    }

    @Override
    public void create(final ComponentBundle bundle) {
        bundle.getEntity().getNotifier().addRemoveListener(new EntityListener(){

            @Override
            public void execute() {
                if (!DropComponent.this.hunted) {
                    return;
                }
                Blueprint itemBlueprint = DropComponent.this.blueprint.getDropBlueprint();
                Transformation transform = bundle.getEntity().getTransform();
                Transformation.TransformBlueprint transformBlueprint = transform.getBlueprint();
                Transformation.TransformParams params = new Transformation.TransformParams(new Vector3f(transform.getPosition()), Maths.RANDOM.nextFloat() * 360.0f, transformBlueprint.generateRandomScale());
                Entity entity = itemBlueprint.createInstance(params);
                GameManager.getSession().getWorld().addInstance(entity, false);
            }
        });
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }
}

