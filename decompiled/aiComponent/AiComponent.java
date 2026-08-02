/*
 * Decompiled with CFR 0.152.
 */
package aiComponent;

import ai.ControlAi;
import aiComponent.Ai;
import aiComponent.AiCompBlueprint;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import controllerUi.ControlUi;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.InfoType;
import entityInfoGui.PopUpInfoGui;
import entityInfoGui.TextInfo;
import gameManaging.GameManager;
import gameManaging.GameState;
import instances.Entity;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class AiComponent
extends Component
implements AiProvidingComponent {
    private static final String CONTROL = GameText.getText(176);
    private static final String CURRENT_ACTION = GameText.getText(177);
    private AiCompBlueprint blueprint;
    private Ai idleAi;
    private List<Ai> queuedAi = new ArrayList<Ai>();
    private Entity entity;

    protected AiComponent(AiCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.idleAi = this.blueprint.getAiProgramBlueprint().createInstance(bundle);
        this.entity = bundle.getEntity();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    public void interruptCurrentAi() {
        if (this.queuedAi.size() == 0) {
            this.idleAi.interrupt();
        } else {
            this.queuedAi.get(0).interrupt();
        }
    }

    public void queueAiProgram(Ai aiProgram) {
        int i = 0;
        while (i < this.queuedAi.size()) {
            if (aiProgram.getPriority() > this.queuedAi.get(i).getPriority()) {
                if (i == 0) {
                    Ai currentAi = this.queuedAi.get(0);
                    currentAi.interrupt();
                }
                this.queuedAi.add(i, aiProgram);
                return;
            }
            ++i;
        }
        if (this.queuedAi.size() == 0) {
            this.idleAi.interrupt();
        }
        this.queuedAi.add(aiProgram);
    }

    public void cancelAiProgram(AiProvidingComponent component) {
        int i = 0;
        while (i < this.queuedAi.size()) {
            if (this.queuedAi.get(i).getComponent() == component) {
                Ai cancelledAi = this.queuedAi.remove(i);
                if (i == 0) {
                    cancelledAi.interrupt();
                }
                return;
            }
            ++i;
        }
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
        info.add(new TextInfo(CURRENT_ACTION, EntityInfoGui.FONT_SIZE, InfoType.CURRENT_ACTION){

            @Override
            public String getValue() {
                if (!AiComponent.this.queuedAi.isEmpty()) {
                    return ((Ai)AiComponent.this.queuedAi.get(0)).getDescription();
                }
                return AiComponent.this.idleAi.getDescription();
            }
        });
    }

    @Override
    public void getActions(List<Action> actions) {
        actions.add(new Action(CONTROL, 5){

            @Override
            public void carryOut() {
                MovementComp movement = (MovementComp)((Object)AiComponent.this.entity.getComponent(ComponentType.MOVEMENT));
                Transformation transform = (Transformation)AiComponent.this.entity.getComponent(ComponentType.TRANSFORM);
                if (movement != null) {
                    ControlUi.openControlUI(AiComponent.this.entity, AiComponent.this);
                    AiComponent.this.queueAiProgram(new ControlAi(transform, movement, AiComponent.this, AiComponent.this.entity.getControlableBehaviour()));
                    GameManager.getEntityPicker().indicateCurrentControlled(true);
                    GameManager.gameState.setState(GameState.CONTROL);
                }
            }
        });
    }

    @Override
    public void update() {
        if (!this.queuedAi.isEmpty()) {
            this.doHighestPriorityAi();
        } else {
            this.idleAi.carryOut();
        }
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    private void doHighestPriorityAi() {
        boolean finished = this.queuedAi.get(0).carryOut();
        if (finished) {
            Ai finishedAi = this.queuedAi.remove(0);
            finishedAi.getComponent().notifyAiFinished();
        }
    }

    @Override
    public void notifyAiFinished() {
    }
}

