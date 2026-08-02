/*
 * Decompiled with CFR 0.152.
 */
package meerkats;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
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
import growth.GrowthComponent;
import instances.Entity;
import interpolation.Timer;
import java.io.IOException;
import java.util.List;
import meerkats.BurrowingAi;
import meerkats.HoleHideComponent;
import meerkats.TimeOutComponent;
import org.lwjgl.util.vector.Vector3f;
import resourceManagement.BlueprintRepository;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;
import world.GridSection;

public class BurrowComponent
extends Component
implements AiProvidingComponent {
    private Timer burrowTimer = Timer.createLoopingTimer(15.0f, 100.0f, true);
    private static final int BURROW_ID = 179;
    private static final int TEST_COUNT = 8;
    private AiComponent aiComponent;
    private MovementComp mover;
    private HoleHideComponent holeHideComp;
    private GrowthComponent growth;
    private Entity entity;
    private boolean burrowing = false;
    private boolean underground = false;

    protected BurrowComponent(ComponentBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void update() {
        if (!this.readyToBurrow()) {
            return;
        }
        GridSection square = this.entity.getCurrentGridSection();
        Entity burrow = BurrowComponent.findLocalBurrow(square);
        if (burrow != null) {
            this.startBurrowing(burrow);
        } else {
            this.startNewBurrow(square);
        }
    }

    protected boolean isUnderground() {
        return this.underground;
    }

    protected void setUnderground(boolean underground) {
        this.underground = underground;
    }

    private void startBurrowing(Entity burrow) {
        this.burrowing = true;
        ((TimeOutComponent)burrow.getComponent(ComponentType.TIME_OUT)).reset();
        this.aiComponent.queueAiProgram(new BurrowingAi(burrow, this.entity, this, this.mover));
    }

    private void startNewBurrow(GridSection square) {
        Vector3f burrowPos = BurrowComponent.getSuitableBurrowLocation(square, true);
        if (burrowPos == null) {
            return;
        }
        this.aiComponent.queueAiProgram(new BurrowingAi(burrowPos, this.entity, this, this.mover));
        this.burrowing = true;
    }

    private boolean readyToBurrow() {
        return this.growth.getGrowthFactor() > 0.5f && !this.burrowing && !this.holeHideComp.isInvulnerable() && this.burrowTimer.check();
    }

    @Override
    public void notifyAiFinished() {
        this.underground = false;
        this.burrowing = false;
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
        this.aiComponent = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        this.holeHideComp = (HoleHideComponent)bundle.getComponent(ComponentType.FLEE);
        this.entity = bundle.getEntity();
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    public static Entity findLocalBurrow(GridSection section) {
        EntityBundle burrows = section.getEntities(BlueprintRepository.getBlueprint(179));
        if (burrows == null) {
            return null;
        }
        return burrows.getRandomEntity();
    }

    public static Vector3f getSuitableBurrowLocation(GridSection square, boolean canReturnNull) {
        Vector3f pos = null;
        int i = 0;
        while (i < 8) {
            pos = BurrowComponent.getRandomLocation(square);
            if (BurrowComponent.isSuitableLocation(pos)) {
                return pos;
            }
            ++i;
        }
        return canReturnNull ? null : pos;
    }

    public static Vector3f getRandomLocation(GridSection square) {
        Vector3f topLeft = square.getTopLeftPosition();
        float x = topLeft.x + Maths.RANDOM.nextFloat() * 2.5f;
        float z = topLeft.z + Maths.RANDOM.nextFloat() * 2.5f;
        float y = GameManager.getWorld().getHeightOfTerrain(x, z);
        return new Vector3f(x, y, z);
    }

    public static boolean isSuitableLocation(Vector3f position) {
        if (position.y < GameManager.getWorld().getWaterHeight()) {
            return false;
        }
        if (BurrowComponent.collidesWithObject(Classifier.getClassification("erl"), position)) {
            return false;
        }
        return !BurrowComponent.collidesWithObject(Classifier.getClassification("pt"), position);
    }

    private static boolean collidesWithObject(Classification classification, Vector3f pos) {
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(classification, 2, pos.x, pos.z);
        if (bundle == null || bundle.isEmpty()) {
            return false;
        }
        for (Entity entity : bundle) {
            if (!BurrowComponent.checkEntityTooClose(entity, pos.x, pos.z)) continue;
            return true;
        }
        return false;
    }

    private static boolean checkEntityTooClose(Entity entity, float x, float z) {
        Vector3f entityPos = entity.getTransform().getPosition();
        float dis = Maths.getComparitableDistance(x, z, entityPos.x, entityPos.z);
        return dis < 0.25f;
    }
}

