/*
 * Decompiled with CFR 0.152.
 */
package beavers;

import baseMovement.MovementComp;
import classification.Classification;
import components.InformationComponent;
import entityBundle.EntityBundle;
import equipping.EquipComponent;
import gameManaging.GameManager;
import instances.Entity;
import org.lwjgl.util.vector.Vector3f;
import resourceManagement.BlueprintRepository;

public class StickPickingAi {
    private static final int STICK_ID = 100;
    private static final int SELECTION_SIZE = 10;
    private static final float TARGET_REACH = 0.1f;
    private final MovementComp mover;
    private final InformationComponent info;
    private final EquipComponent equipComp;
    private Entity targetStick;

    protected StickPickingAi(MovementComp mover, InformationComponent info, EquipComponent equipComp) {
        this.mover = mover;
        this.info = info;
        this.equipComp = equipComp;
    }

    protected void interrupt() {
        this.equipComp.letDrop();
        this.targetStick = null;
    }

    protected boolean hasStick() {
        return this.equipComp.isHolding();
    }

    protected boolean goGetStick() {
        if (this.targetStick == null) {
            this.chooseTargetStick();
        }
        if (this.targetStick == null) {
            return false;
        }
        if (this.targetStick.isDead() || this.targetStick.isGrabbed()) {
            this.targetStick = null;
            return true;
        }
        boolean reached = this.mover.goToTarget(this.targetStick.getTransform().getPosition(), false, 0.1f);
        if (reached) {
            this.equipComp.equip(this.targetStick);
        }
        return true;
    }

    private void chooseTargetStick() {
        Classification stickClass = BlueprintRepository.getBlueprint(100).getSpeciesClassification();
        Vector3f basePos = this.info.getBasePosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(stickClass, this.info.getRoamingRange(), basePos.x, basePos.z);
        if (bundle.isEmpty()) {
            return;
        }
        this.checkForSuitableStick(bundle);
    }

    private void checkForSuitableStick(EntityBundle bundle) {
        Entity[] randomSelection;
        Entity[] entityArray = randomSelection = bundle.getRandomList(10);
        int n = randomSelection.length;
        int n2 = 0;
        while (n2 < n) {
            Entity stick = entityArray[n2];
            if (!stick.isDead() && !stick.isGrabbed()) {
                this.targetStick = stick;
                return;
            }
            ++n2;
        }
    }
}

