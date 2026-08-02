/*
 * Decompiled with CFR 0.152.
 */
package meerkats;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityBundle.EntityBundle;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import hunting.PreyComp;
import instances.Entity;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import meerkats.BurrowComponent;
import meerkats.HoleHideAi;
import meerkats.HoleHideCompBlueprint;
import meerkats.TimeOutComponent;
import org.lwjgl.util.vector.Vector3f;
import resourceManagement.BlueprintRepository;
import toolbox.Maths;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;
import world.GridSection;

public class HoleHideComponent
extends Component
implements PreyComp,
AiProvidingComponent {
    private static final int BURROW_ID = 179;
    private final HoleHideCompBlueprint blueprint;
    private AiComponent aiComp;
    private Transformation transform;
    private MovementComp mover;
    private Entity entity;
    private InformationComponent info;
    private boolean fleeing = false;
    private boolean underground = false;
    private Set<Entity> predators = new HashSet<Entity>();

    protected HoleHideComponent(HoleHideCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void notifyAiFinished() {
        this.underground = false;
        this.fleeing = false;
    }

    @Override
    public void alertToDanger(Entity predator) {
        this.reactToDanger(predator);
        this.alertFriends(predator);
    }

    private void alertFriends(Entity predator) {
        Vector3f basePos = this.info.getBasePosition();
        EntityBundle friends = GameManager.getWorld().getListOfSpecies(this.entity.getBlueprint(), this.info.getRoamingRange(), basePos.x, basePos.z);
        for (Entity friend : friends) {
            if (friend == this.entity) continue;
            HoleHideComponent hideComp = (HoleHideComponent)friend.getComponent(ComponentType.FLEE);
            hideComp.reactToDanger(predator);
        }
    }

    private void reactToDanger(Entity predator) {
        this.predators.add(predator);
        if (this.fleeing) {
            return;
        }
        Entity hole = this.findNearbyHole();
        if (hole == null) {
            return;
        }
        ((TimeOutComponent)hole.getComponent(ComponentType.TIME_OUT)).reset();
        this.aiComp.queueAiProgram(new HoleHideAi(this.entity, this, this.mover, hole.getTransform().getPosition()));
        this.fleeing = true;
    }

    private Entity findNearbyHole() {
        GridSection square = this.entity.getCurrentGridSection();
        Entity hole = HoleHideComponent.findLocalBurrow(square);
        if (hole != null) {
            return hole;
        }
        Vector3f basePos = this.info.getBasePosition();
        EntityBundle holes = GameManager.getWorld().getListOfSpecies(BlueprintRepository.getBlueprint(179), this.info.getRoamingRange(), basePos.x, basePos.z);
        if (holes == null) {
            return null;
        }
        return holes.getRandomEntity();
    }

    public static Entity findLocalBurrow(GridSection section) {
        EntityBundle burrows = section.getEntities(BlueprintRepository.getBlueprint(179));
        if (burrows == null) {
            return null;
        }
        return burrows.getRandomEntity();
    }

    protected void updatePredatorList() {
        Iterator<Entity> iterator = this.predators.iterator();
        while (iterator.hasNext()) {
            Entity predator = iterator.next();
            if (!this.outOfRange(predator)) continue;
            iterator.remove();
        }
    }

    protected void clearPredators() {
        this.predators.clear();
    }

    private boolean outOfRange(Entity predator) {
        if (predator.isDead() || predator.isGrabbed()) {
            return true;
        }
        Vector3f predatorPos = predator.getTransform().getPosition();
        Vector3f.sub(predatorPos, this.transform.getPosition(), Maths.VEC3);
        return Maths.VEC3.lengthSquared() > this.blueprint.getSafeRangeSquared();
    }

    protected boolean isPredatorNearby() {
        return !this.predators.isEmpty();
    }

    protected Set<Entity> getPredators() {
        return this.predators;
    }

    public void setUnderground(boolean underground) {
        this.underground = underground;
    }

    @Override
    public boolean isInvulnerable() {
        BurrowComponent burrowingComp = (BurrowComponent)this.entity.getComponent(ComponentType.BURROW);
        return this.underground || burrowingComp.isUnderground();
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.aiComp = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.entity = bundle.getEntity();
        this.info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    @Override
    public float getSafeRangeSquared() {
        return this.blueprint.getSafeRangeSquared();
    }
}

