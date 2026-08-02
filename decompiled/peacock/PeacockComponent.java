/*
 * Decompiled with CFR 0.152.
 */
package peacock;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import components.MeshComponent;
import entityBundle.EntityBundle;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import growth.GrowthComponent;
import instances.Entity;
import interpolation.Timer;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import peacock.PeacockAi;
import utils.BinaryReader;
import utils.BinaryWriter;

public class PeacockComponent
extends Component
implements AiProvidingComponent {
    private MeshComponent meshComp;
    private InformationComponent infoComp;
    private AiComponent aiComp;
    private MovementComp mover;
    private GrowthComponent growth;
    private Entity entity;
    private boolean flaring = false;
    private Timer flareTimer = Timer.createLoopingTimer(25.0f, 60.0f, true);
    private Timer endFlare = Timer.createLoopingTimer(1.0f, 5.0f, true);
    private boolean ending = false;

    protected PeacockComponent(ComponentBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void update() {
        Entity target;
        super.update();
        if (this.growth.getGrowthFactor() < 0.75f) {
            return;
        }
        if (this.ending && this.endFlare.check()) {
            this.ending = false;
            this.flaring = false;
            this.feathersUp(false);
        }
        if (!this.flaring && this.flareTimer.check() && (target = this.getNearbyTarget()) != null) {
            PeacockComponent otherPeacock = (PeacockComponent)target.getComponent(ComponentType.PEACOCK);
            this.rage(target);
            otherPeacock.rage(this.entity);
        }
    }

    public void rage(Entity target) {
        this.flareTimer.reset();
        this.flaring = true;
        this.feathersUp(true);
        this.aiComp.queueAiProgram(new PeacockAi(target, this, this.mover));
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
        actions.add(new Action("Flare", 1){

            @Override
            public void carryOut() {
                PeacockComponent.this.feathersUp(PeacockComponent.this.meshComp.getCurrentStageNumber() == 2);
            }
        });
    }

    private Entity getNearbyTarget() {
        Entity[] checkEntities;
        Vector3f basePos = this.infoComp.getBasePosition();
        EntityBundle bundle = GameManager.getWorld().getListOfEntities(ComponentType.PEACOCK, 2, basePos.x, basePos.z);
        Entity[] entityArray = checkEntities = bundle.getRandomList(3);
        int n = checkEntities.length;
        int n2 = 0;
        while (n2 < n) {
            Entity entity = entityArray[n2];
            GrowthComponent growth = (GrowthComponent)entity.getComponent(ComponentType.GROWTH);
            if (entity != this.entity && growth.getGrowthFactor() > 0.75f) {
                return entity;
            }
            ++n2;
        }
        return null;
    }

    public void feathersUp(boolean up) {
        if (this.meshComp.getCurrentStageNumber() >= 2) {
            this.meshComp.updateModelStage(up ? 3 : 2);
        }
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.meshComp = (MeshComponent)bundle.getComponent(ComponentType.MESH);
        this.entity = bundle.getEntity();
        this.infoComp = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        this.aiComp = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    @Override
    public void notifyAiFinished() {
        this.ending = true;
    }
}

