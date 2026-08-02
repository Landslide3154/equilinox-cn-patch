/*
 * Decompiled with CFR 0.152.
 */
package fighting;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import breedingTraits.Trait;
import breedingTraits.TraitBlueprint;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import componentArchitecture.ControlBehaviour;
import entityBundle.EntityBundle;
import entityInfoGui.PopUpInfoGui;
import fighting.FightAi;
import fighting.FightCompBlueprint;
import gameManaging.GameManager;
import health.DamageListener;
import health.LifeComponent;
import hunting.HuntComponent;
import hunting.PreyComp;
import instances.Entity;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;

public class FightComponent
extends Component
implements AiProvidingComponent {
    private static final String SCARE = GameText.getText(644);
    private static final int ALERT_RANGE = 2;
    private static final float CLOSE_FACTOR = 0.6f;
    public final FightCompBlueprint blueprint;
    private AiComponent aiComp;
    private MovementComp mover;
    private Entity entity;
    private boolean fighting = false;

    protected FightComponent(FightCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    public void fight(Entity target, Classification preyType, HuntComponent huntComp) {
        if (!this.fighting) {
            this.fighting = true;
            this.aiComp.queueAiProgram(new FightAi(this.entity, this.mover, this, target, preyType, huntComp, huntComp != null));
        }
    }

    public void attackOnce(Entity target, Classification enemyClass) {
        if (!this.fighting) {
            this.fighting = true;
            this.aiComp.queueAiProgram(new FightAi(this.entity, this.mover, this, target, true, false, enemyClass));
        }
    }

    public boolean isFighting() {
        return this.fighting;
    }

    @Override
    public void getControlableBehaviour(List<ControlBehaviour> behaviours) {
        behaviours.add(new ControlBehaviour(SCARE, 18, false){

            @Override
            public void doAction() {
                FightComponent.this.alertPrey(Classifier.getAnimalClassification());
            }
        });
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    @Override
    public void loadComponentTraits(ComponentBlueprint blueprint, BinaryReader reader) throws Exception {
        List<TraitBlueprint> traitBlueprints = blueprint.getTraits();
        if (traitBlueprints == null) {
            return;
        }
        ArrayList<Trait> traits = new ArrayList<Trait>(traitBlueprints.size());
        for (TraitBlueprint traitBlueprint : traitBlueprints) {
            if (reader.getVersion() < 10) {
                traits.add(traitBlueprint.createRandomInstance());
                continue;
            }
            Trait trait = traitBlueprint.loadInstance(reader);
            if (trait == null) continue;
            traits.add(trait);
        }
        super.setTraits(traits);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.aiComp = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.entity = bundle.getEntity();
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        if (this.blueprint.takesRevenge) {
            LifeComponent life = (LifeComponent)bundle.getComponent(ComponentType.LIFE);
            life.getHealth().addDamageListener(new DamageListener(){

                @Override
                public void damageOccurred(Entity attacker) {
                    if (attacker != null) {
                        FightComponent.this.fight(attacker, null, null);
                    }
                }
            });
        }
    }

    protected void alertPrey(Classification preyClass) {
        if (preyClass == null) {
            return;
        }
        Vector3f pos = this.mover.getTransform().getPosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(preyClass, 2, pos.x, pos.z);
        for (Entity prey : bundle) {
            PreyComp fleeComp = (PreyComp)((Object)prey.getComponent(ComponentType.FLEE));
            if (fleeComp == null || !this.preyInRange(pos, prey.getTransform().getPosition(), fleeComp.getSafeRangeSquared())) continue;
            fleeComp.alertToDanger(this.entity);
        }
    }

    private boolean preyInRange(Vector3f predatorPos, Vector3f preyPos, float dangerRadius) {
        Vector3f.sub(preyPos, predatorPos, Maths.VEC3);
        return Maths.VEC3.lengthSquared() < dangerRadius * 0.6f;
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    @Override
    public void notifyAiFinished() {
        this.fighting = false;
    }
}

