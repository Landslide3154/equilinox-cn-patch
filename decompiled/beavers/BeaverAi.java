/*
 * Decompiled with CFR 0.152.
 */
package beavers;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import beavers.BeaverComponent;
import beavers.DenBuildingAi;
import beavers.StickPickingAi;
import beavers.TreeHuntingAi;
import components.InformationComponent;
import equipping.EquipComponent;
import languages.GameText;

public class BeaverAi
implements Ai {
    private static final float PRIORITY = 8.0f;
    private static final String DESC = GameText.getText(178);
    private final BeaverComponent beaverComp;
    private final TreeHuntingAi treeHuntingAi;
    private final StickPickingAi stickPickingAi;
    private final DenBuildingAi denBuildingAi;

    protected BeaverAi(BeaverComponent beaverComp, MovementComp mover, InformationComponent info, EquipComponent equip) {
        this.beaverComp = beaverComp;
        this.treeHuntingAi = new TreeHuntingAi(mover, info);
        this.stickPickingAi = new StickPickingAi(mover, info, equip);
        this.denBuildingAi = new DenBuildingAi(beaverComp, mover, info, equip);
    }

    @Override
    public boolean carryOut() {
        boolean finished = false;
        if (this.stickPickingAi.hasStick()) {
            finished = this.denBuildingAi.doAi();
        } else if (this.treeHuntingAi.isGnawing() || !this.stickPickingAi.goGetStick()) {
            finished = this.treeHuntingAi.doTreeHuntingAi();
        }
        return finished;
    }

    @Override
    public float getPriority() {
        return 8.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.beaverComp;
    }

    @Override
    public void interrupt() {
        System.out.println("INTERRUPT");
        this.treeHuntingAi.interrupt();
        this.stickPickingAi.interrupt();
        this.denBuildingAi.interrupt();
    }

    @Override
    public String getDescription() {
        return DESC;
    }
}

