/*
 * Decompiled with CFR 0.152.
 */
package treeCharging;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityBundle.EntityBundle;
import fruit.FruitFallComponent;
import fruit.FruiterComponent;
import gameManaging.GameManager;
import instances.Entity;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import treeCharging.ReboundMovement;
import treeCharging.TreeChargeComponent;

public class TreeChargeAi
implements Ai {
    private static final String DESC = GameText.getText(189);
    private static final float NEAR_RADIUS = 0.2f;
    private static final int CHECK_COUNT = 5;
    private static final float PRIORITY = 4.94f;
    private final TreeChargeComponent chargeComp;
    private final MovementComp mover;
    private final InformationComponent info;
    private Entity targetTree;
    private boolean bouncingBack = false;
    private final ReboundMovement reboundMove;

    protected TreeChargeAi(Entity entity, TreeChargeComponent chargeComp, MovementComp mover, InformationComponent info) {
        this.chargeComp = chargeComp;
        this.mover = mover;
        this.info = info;
        this.reboundMove = new ReboundMovement(entity, mover.getTransform());
    }

    @Override
    public boolean carryOut() {
        if (this.bouncingBack) {
            return this.doBounceBack();
        }
        this.getTarget();
        if (this.targetTree == null || this.targetTree.isDead() || this.targetTree.isGrabbed()) {
            return true;
        }
        this.chargeAtTree();
        return false;
    }

    @Override
    public float getPriority() {
        return 4.94f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.chargeComp;
    }

    @Override
    public void interrupt() {
        this.targetTree = null;
        this.bouncingBack = false;
        this.mover.block(false);
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    private void chargeAtTree() {
        boolean reached = this.mover.goToTarget(this.targetTree.getTransform().getPosition(), true, 0.2f);
        if (reached) {
            this.initializeBounce();
        }
    }

    private void initializeBounce() {
        this.mover.block(true);
        this.bouncingBack = true;
        ((FruitFallComponent)this.targetTree.getComponent(ComponentType.FRUIT_FALL)).dropAll();
        this.reboundMove.initBounce(this.targetTree.getTransform().getPosition());
    }

    private boolean doBounceBack() {
        boolean finished = this.reboundMove.updateBounce();
        if (finished) {
            this.mover.block(false);
        }
        return finished;
    }

    private void getTarget() {
        if (this.targetTree == null) {
            Vector3f basePos = this.info.getBasePosition();
            EntityBundle bundle = GameManager.getWorld().getListOfEntities(ComponentType.FRUIT_FALL, this.info.getRoamingRange(), basePos.x, basePos.z);
            Entity[] possibleTrees = bundle.getRandomList(5);
            this.chooseSuitableTree(possibleTrees);
        }
    }

    private void chooseSuitableTree(Entity[] possibleTrees) {
        Entity[] entityArray = possibleTrees;
        int n = possibleTrees.length;
        int n2 = 0;
        while (n2 < n) {
            Entity testTree = entityArray[n2];
            FruiterComponent fruiter = (FruiterComponent)testTree.getComponent(ComponentType.FRUITER);
            if (fruiter.hasFruit()) {
                this.targetTree = testTree;
                return;
            }
            ++n2;
        }
    }
}

