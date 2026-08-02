/*
 * Decompiled with CFR 0.152.
 */
package fishHunt;

import baseMovement.MovementComp;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityBundle.EntityBundle;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import hunting.PreyComp;
import instances.Entity;
import interpolation.Timer;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;

public class FishHuntComponent
extends Component {
    private static final Classification PREY = Classifier.getClassification("afs");
    private static final int ALERT_RANGE = 2;
    private static final float CLOSE_FACTOR = 0.6f;
    private final Timer timer = Timer.createLoopingTimer(0.2f, true);
    private Entity entity;
    private MovementComp mover;

    protected FishHuntComponent(ComponentBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void update() {
        super.update();
        if (this.timer.check()) {
            this.alertPrey();
        }
    }

    private void alertPrey() {
        Vector3f pos = this.mover.getTransform().getPosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(PREY, 2, pos.x, pos.z);
        for (Entity prey : bundle) {
            PreyComp fleeComp = (PreyComp)((Object)prey.getComponent(ComponentType.FLEE));
            if (fleeComp == null || !this.preyInRange(pos, prey.getTransform().getPosition(), fleeComp.getSafeRangeSquared())) continue;
            fleeComp.alertToDanger(this.entity);
        }
    }

    private boolean preyInRange(Vector3f predatorPos, Vector3f preyPos, float dangerRadius) {
        Vector3f.sub(preyPos, predatorPos, Maths.VEC3);
        return Maths.VEC3.lengthSquared() < dangerRadius * 0.6f;
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.entity = bundle.getEntity();
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }
}

