/*
 * Decompiled with CFR 0.152.
 */
package sound;

import audio.SoundEffect;
import basics.DisplayManager;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import componentArchitecture.ControlBehaviour;
import entityInfoGui.PopUpInfoGui;
import events.EventData;
import events.EventManager;
import growth.GrowthComponent;
import java.util.List;
import languages.GameText;
import sound.RandomSounderBlueprint;
import sound.SoundComponent;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;

public class RandomSounder
extends Component {
    private static final float RAN_MIN = -0.04f;
    private static final float RAN_MAX = 0.04f;
    private static final float YOUNG_PITCH_EXTRA = 0.15f;
    private static final String MAKE_NOISE = GameText.getText(930);
    private final RandomSounderBlueprint soundBlueprint;
    private GrowthComponent growthComponent;
    private SoundComponent sounder;
    private float timeTillSound;

    protected RandomSounder(RandomSounderBlueprint blueprint) {
        super(blueprint);
        this.soundBlueprint = blueprint;
        this.timeTillSound = Maths.RANDOM.nextFloat() * (blueprint.getRandomExtraTime() + blueprint.getMinWaitTime());
    }

    @Override
    public void getControlableBehaviour(List<ControlBehaviour> behaviours) {
        ControlBehaviour behaviour = new ControlBehaviour(MAKE_NOISE, 18, false){

            @Override
            public void doAction() {
                EventManager.NOISE.registerEvent(new EventData(), new String[0]);
                RandomSounder.this.playRandomSound();
            }
        };
        behaviours.add(behaviour);
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
        actions.add(new Action(MAKE_NOISE, 1){

            @Override
            public void carryOut() {
                RandomSounder.this.playRandomSound();
                RandomSounder.this.generateWaitTime();
            }
        });
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.sounder = (SoundComponent)bundle.getComponent(ComponentType.SOUND);
        this.growthComponent = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    @Override
    public void update() {
        this.timeTillSound -= DisplayManager.getDeltaSeconds();
        if (this.timeTillSound < 0.0f) {
            this.playRandomSound();
            this.generateWaitTime();
        }
    }

    private void playRandomSound() {
        if (this.growthComponent == null || this.growthComponent.getStageNumber() >= this.soundBlueprint.getStageRequirement()) {
            SoundEffect sound = this.soundBlueprint.getRandomSound();
            this.sounder.playEffect(sound, this.getPitch());
        }
    }

    private float getPitch() {
        float base = 1.0f;
        if (this.growthComponent != null) {
            base = 1.0f + 0.15f * (1.0f - this.growthComponent.getGrowthFactor());
        }
        float extra = Maths.randomNumberBetween(-0.04f, 0.04f);
        return base + extra;
    }

    private void generateWaitTime() {
        float minWaitTime = this.soundBlueprint.getMinWaitTime();
        float maxWaitTime = minWaitTime + this.soundBlueprint.getRandomExtraTime();
        this.timeTillSound = Maths.randomNumberBetween(minWaitTime, maxWaitTime);
    }
}

