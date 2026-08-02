/*
 * Decompiled with CFR 0.152.
 */
package hunting;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import classification.Classification;
import components.InformationComponent;
import entityBundle.EntityBundle;
import equipping.EquipComponent;
import gameManaging.GameManager;
import instances.Entity;
import interpolation.Timer;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class HaulAi
implements Ai {
    private static final float PRIORITY = 9.6f;
    private static final int MAX_DROP_CHECKS = 7;
    private final Classification haulItemClass;
    private final MovementComp mover;
    private final EquipComponent equipper;
    private final InformationComponent info;
    private final AiProvidingComponent component;
    private Timer waitTimer = Timer.createLoopingTimer(5.0f, true);
    private Entity target = null;
    private Vector3f dropPosition = null;
    private Vector3f waitPoint = null;
    private boolean walkingAway = false;

    public HaulAi(MovementComp mover, EquipComponent equipper, InformationComponent info, Classification haulItemClass, AiProvidingComponent component) {
        this.mover = mover;
        this.equipper = equipper;
        this.haulItemClass = haulItemClass;
        this.component = component;
        this.info = info;
    }

    @Override
    public boolean carryOut() {
        if (this.walkingAway) {
            return this.mover.goToTarget(this.waitPoint, false, 0.2f);
        }
        if (this.target != null) {
            if (this.target.isDead()) {
                return true;
            }
            return this.haul();
        }
        this.checkWaitPoint();
        this.mover.goToTarget(this.waitPoint, false, 0.2f);
        this.searchForItem();
        return this.waitTimer.check();
    }

    private void checkWaitPoint() {
        if (this.waitPoint == null) {
            Vector3f pos = this.mover.getTransform().getPosition();
            this.waitPoint = Maths.randomPointOnSquare(pos.x, pos.z, 0.5f);
        }
    }

    private boolean haul() {
        if (this.equipper.isHolding()) {
            this.takeToDropPoint();
            return false;
        }
        if (this.target.isGrabbed()) {
            return true;
        }
        this.collectItem();
        return false;
    }

    private void takeToDropPoint() {
        boolean reached = this.mover.goToTarget(this.dropPosition, false, 0.2f);
        if (reached) {
            this.equipper.letDrop();
            this.walkingAway = true;
            Vector3f pos = this.mover.getTransform().getPosition();
            this.waitPoint = Maths.randomPointOnSquare(pos.x, pos.z, 1.0f);
        }
    }

    private void collectItem() {
        boolean reached = this.mover.goToTarget(this.target.getTransform().getPosition(), false, 0.2f);
        if (reached) {
            this.equipper.equip(this.target);
        }
        int count = 0;
        while (this.dropPosition == null) {
            this.dropPosition = this.info.getRandomInRangePoint();
            float height = GameManager.getWorld().getHeightOfTerrain(this.dropPosition.x, this.dropPosition.z);
            if (height > GameManager.getWorld().getWaterHeight() || ++count >= 7) break;
            this.dropPosition = null;
        }
    }

    private void searchForItem() {
        Vector3f pos = this.mover.getTransform().getPosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(this.haulItemClass, 2, pos.x, pos.z);
        if (!bundle.isEmpty()) {
            this.target = bundle.getRandomEntity();
        }
    }

    @Override
    public float getPriority() {
        return 9.6f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.component;
    }

    @Override
    public void interrupt() {
        this.equipper.letDrop();
        this.waitPoint = null;
        this.walkingAway = false;
        this.waitTimer.reset();
        this.dropPosition = null;
    }

    @Override
    public String getDescription() {
        return "Hauling";
    }
}

