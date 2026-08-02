/*
 * Decompiled with CFR 0.152.
 */
package sleeping;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import instances.Entity;
import java.io.IOException;
import java.util.List;
import sleeping.SleepAi;
import sleeping.SleepCompBlueprint;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class SleepComponent
extends Component
implements AiProvidingComponent {
    private final SleepCompBlueprint blueprint;
    private Transformation transform;
    private AiComponent aiComponent;
    private Entity entity;
    private float sleepStartTime;
    private float sleepEndTime;
    private boolean asleep = false;

    protected SleepComponent(SleepCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
        this.sleepStartTime = blueprint.generateStartSleepTime();
        this.sleepEndTime = blueprint.generateEndSleepTime();
    }

    @Override
    public void update() {
        super.update();
        if (!this.asleep && this.shouldBeSleeping()) {
            this.goToSleep();
        }
    }

    @Override
    public void notifyAiFinished() {
        this.sleepStartTime = this.blueprint.generateStartSleepTime();
        this.asleep = false;
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeBoolean(this.asleep);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.aiComponent = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.entity = bundle.getEntity();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
        boolean sleeping = reader.readBoolean();
        if (sleeping) {
            this.goToSleep();
        }
    }

    private void goToSleep() {
        this.sleepEndTime = this.blueprint.generateEndSleepTime();
        this.aiComponent.queueAiProgram(new SleepAi(this.entity, this, this.transform, this.sleepEndTime));
        this.asleep = true;
    }

    private boolean shouldBeSleeping() {
        float rawTime = GameManager.getSession().getStats().getCalendar().getRawTime();
        if (this.sleepStartTime > this.sleepEndTime) {
            return rawTime > this.sleepStartTime || rawTime < this.sleepEndTime;
        }
        return rawTime > this.sleepStartTime && rawTime < this.sleepEndTime;
    }
}

