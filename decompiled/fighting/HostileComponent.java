/*
 * Decompiled with CFR 0.152.
 */
package fighting;

import classification.Classification;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityBundle.EntityBundle;
import entityInfoGui.PopUpInfoGui;
import fighting.FightComponent;
import fighting.HostileCompBlueprint;
import gameManaging.GameManager;
import instances.Entity;
import interpolation.Timer;
import java.io.IOException;
import java.util.List;
import languages.GameText;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Transformation;
import userInterfaces.TextStatInfo;
import utils.BinaryReader;
import utils.BinaryWriter;

public class HostileComponent
extends Component {
    private static final int SEARCH_RANGE = 1;
    private static final String NAME = GameText.getText(453);
    private static final String VALUE = GameText.getText(454);
    private static final String CAUSE = GameText.getText(455);
    private static final String ATTACK = GameText.getText(446);
    private static final float TIME_VAR = 0.2f;
    private final Timer attackTimer;
    private final Classification enemyClass;
    private Transformation transform;
    private FightComponent fightComp;
    private final HostileCompBlueprint blueprint;
    private float pacifyTime = 0.0f;

    protected HostileComponent(HostileCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
        this.enemyClass = blueprint.enemyClass;
        float variance = blueprint.averageAttackTime * 0.2f;
        float minTime = blueprint.averageAttackTime - variance;
        float maxTime = blueprint.averageAttackTime + variance;
        this.attackTimer = Timer.createLoopingTimer(minTime, maxTime, true);
    }

    @Override
    public void update() {
        if (this.fightComp.isFighting() || this.pacifyTime > 0.0f) {
            this.updatePacify();
            return;
        }
        if (this.attackTimer.check() && !this.fightComp.isFighting()) {
            this.attackNearbyTarget();
        }
    }

    private void updatePacify() {
        this.pacifyTime -= GameManager.getGameSeconds();
        if (this.pacifyTime < 0.0f) {
            this.pacifyTime = 0.0f;
        }
    }

    private void attackNearbyTarget() {
        Entity target = this.getTarget();
        if (target != null) {
            this.fightComp.attackOnce(target, this.blueprint.shouldNotify ? this.enemyClass : null);
        }
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    public void pacify(float time) {
        this.pacifyTime = Math.max(this.pacifyTime, time);
    }

    @Override
    public void getPerformanceBuffsInfo(List<TextStatInfo> info) {
        if (this.pacifyTime > 0.0f) {
            info.add(new TextStatInfo(NAME, VALUE, ColourPalette.WHITE, ColourPalette.BASE_BLUE, CAUSE));
        }
    }

    @Override
    public void getActions(List<Action> actions) {
        actions.add(new Action(ATTACK, 1){

            @Override
            public void carryOut() {
                HostileComponent.this.attackNearbyTarget();
            }
        });
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeFloat(this.pacifyTime);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.fightComp = (FightComponent)bundle.getComponent(ComponentType.FIGHT);
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
        this.pacifyTime = reader.readFloat();
    }

    private Entity getTarget() {
        Vector3f pos = this.transform.getPosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(this.enemyClass, 1, pos.x, pos.z);
        if (bundle.isEmpty()) {
            return null;
        }
        return bundle.getRandomEntity();
    }
}

