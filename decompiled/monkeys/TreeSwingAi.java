/*
 * Decompiled with CFR 0.152.
 */
package monkeys;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityBundle.EntityBundle;
import gameManaging.GameManager;
import instances.Entity;
import interpolation.Timer;
import languages.GameText;
import monkeys.TreeSwingComponent;
import movementUtils.JumpToTarget;
import movementUtils.ProjectileBounce;
import org.lwjgl.util.vector.Vector3f;
import perching.PerchComponent;
import perching.PerchSlot;

public class TreeSwingAi
implements Ai {
    private static final String DESC = GameText.getText(422);
    private static final float JUMP_DUR = 0.5f;
    private static final float START_RADIUS = 0.5f;
    private static final float BOUNCINESS = 0.35f;
    private static final float PRIORITY = 4.0f;
    private final MovementComp mover;
    private final TreeSwingComponent treeSwingComp;
    private final InformationComponent info;
    private JumpToTarget jumpMovement;
    private ProjectileBounce dismountMove;
    private final Timer hangTimer = Timer.createLoopingTimer(1.0f, 2.5f, true);
    private PerchSlot target = null;
    private boolean onFloor = true;
    private boolean flying = false;
    private boolean hanging = false;
    private boolean ending = false;

    protected TreeSwingAi(Entity entity, TreeSwingComponent treeSwingComp, MovementComp mover, InformationComponent info) {
        this.info = info;
        this.mover = mover;
        this.treeSwingComp = treeSwingComp;
    }

    @Override
    public boolean carryOut() {
        if (this.ending) {
            return this.updateDismount();
        }
        if (this.target == null) {
            this.findNewTarget();
        }
        if (!this.targetAvailable()) {
            return this.endAi();
        }
        if (this.onFloor) {
            this.goToTree();
        } else if (this.hanging) {
            this.updateHang();
        } else if (this.flying) {
            this.updateFlight();
        }
        return false;
    }

    @Override
    public float getPriority() {
        return 4.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.treeSwingComp;
    }

    @Override
    public void interrupt() {
        this.target = null;
        this.onFloor = true;
        this.hanging = false;
        this.flying = false;
        this.ending = false;
        this.mover.block(false);
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    private boolean endAi() {
        this.ending = true;
        if (this.onFloor) {
            this.mover.block(false);
            return true;
        }
        this.dismountMove = new ProjectileBounce(this.mover.getTransform(), new Vector3f(), 1, 0.35f);
        this.updateDismount();
        return false;
    }

    private void goToTree() {
        boolean reached = this.mover.goToTarget(this.target.getWorldPosition(), false, 0.5f);
        if (reached && this.mover.normalize()) {
            this.onFloor = false;
            this.launchToTarget();
        }
    }

    private void updateFlight() {
        this.mover.block(true);
        boolean finished = this.jumpMovement.update(this.target.getWorldPosition());
        if (finished) {
            this.flying = false;
            this.hanging = true;
            this.mover.getTransform().setPosition(this.target.getWorldPosition());
        }
    }

    private void launchToTarget() {
        this.flying = true;
        this.jumpMovement = new JumpToTarget(this.mover.getTransform(), 0.5f, this.target.getWorldPosition(), 0.0f);
    }

    private void updateHang() {
        this.mover.block(true);
        if (this.hangTimer.check()) {
            this.hanging = false;
            this.endAi();
        }
    }

    private boolean updateDismount() {
        this.mover.block(true);
        boolean finished = this.dismountMove.update();
        if (finished) {
            this.mover.block(false);
            return true;
        }
        return false;
    }

    private boolean targetAvailable() {
        return this.target != null && this.target.isInExistence();
    }

    private void findNewTarget() {
        PerchComponent perch = this.getNewNearbyBranch();
        this.target = perch != null ? perch.getRandomAvailableSlot() : null;
    }

    private PerchComponent getNewNearbyBranch() {
        EntityBundle bundle = GameManager.getWorld().getListOfEntities(ComponentType.PERCH, this.info.getRoamingRange(), this.info.getBasePosition().x, this.info.getBasePosition().z);
        Entity[] entityArray = bundle.getRandomList(5);
        int n = entityArray.length;
        int n2 = 0;
        while (n2 < n) {
            Entity tree = entityArray[n2];
            PerchComponent perch = (PerchComponent)tree.getComponent(ComponentType.PERCH);
            if (perch.hasAvailableSlots()) {
                return perch;
            }
            ++n2;
        }
        return null;
    }
}

