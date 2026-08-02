/*
 * Decompiled with CFR 0.152.
 */
package health;

import breeding.BreedingComponent;
import breedingTraits.FloatTrait;
import classification.Classifier;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import death.DeathAi;
import eating.EatingComponent;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.PopUpInfoGui;
import entityInfoGui.ProgressInfo;
import entityInfoGui.TextInfo;
import environment.EnviroComponent;
import gameManaging.GameManager;
import gameManaging.Ticker;
import guis.GuiComponent;
import health.Disease;
import health.Health;
import health.HealthPopUp;
import health.LifeCompBlueprint;
import instances.Entity;
import instances.EntityListener;
import interpolation.Timer;
import java.io.IOException;
import java.util.List;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import session.GameMode;
import time.Calendar;
import toolbox.Maths;
import toolbox.Transformation;
import userInterfaces.TextStatInfo;
import utils.BinaryReader;
import utils.BinaryWriter;

public class LifeComponent
extends Component {
    private static final int RESISTANCE_TRAIT = 0;
    private static final String AGE_STAT = GameText.getText(872);
    private static final String DIS_STAT = GameText.getText(999);
    private static final String DISEASED = GameText.getText(1000);
    private static final String NOT_DISEASED = GameText.getText(1001);
    private static final String GENERATIONS = GameText.getText(1002);
    private static final String NAME = GameText.getText(875);
    private static final float UPDATE_TIME = 1.0f;
    private static final float DP_UPDATE_TIME = 6.0f;
    private final Ticker ticker = new Ticker(1.0f);
    private final Timer timer = Timer.createLoopingTimer(6.0f, false).randomize();
    private final LifeCompBlueprint lifeBlueprint;
    public Health health;
    private Disease diseaseComponent;
    private BreedingComponent breedComponent;
    private EnviroComponent enviroComponent;
    private EatingComponent hungerComponent;
    private Entity entity;
    private float wellbeing = 1.0f;

    protected LifeComponent(LifeCompBlueprint lifeBlueprint) {
        super(lifeBlueprint);
        this.lifeBlueprint = lifeBlueprint;
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.entity = bundle.getEntity();
        Transformation transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        InformationComponent info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        this.health = new Health(this.entity, this, this.lifeBlueprint.averagePopulation, this.lifeBlueprint.averageLifeLength, info);
        boolean isAnimal = this.entity.getBlueprint().getSpeciesClassification().isTypeOf(Classifier.getAnimalClassification());
        if (isAnimal) {
            this.diseaseComponent = new Disease(this.entity, transform, this, (FloatTrait)this.getTrait(0));
        }
        this.breedComponent = this.lifeBlueprint.breedInfo.createInstance(this.entity, this);
        Vector3f position = isAnimal ? info.getBasePosition() : transform.getPosition();
        this.enviroComponent = this.lifeBlueprint.getEnviroBlueprint().createInstance(position, info.getRoamingRange());
        this.addRemoveListener();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.entity = bundle.getEntity();
        Transformation transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        InformationComponent info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        boolean isAnimal = this.entity.getBlueprint().getSpeciesClassification().isTypeOf(Classifier.getAnimalClassification());
        Vector3f position = isAnimal ? info.getBasePosition() : transform.getPosition();
        this.enviroComponent = this.lifeBlueprint.getEnviroBlueprint().loadInstance(reader, position, info.getRoamingRange());
        this.wellbeing = reader.readFloat();
        this.health = new Health(this.entity, reader, this.lifeBlueprint.averagePopulation, this, info);
        if (isAnimal) {
            this.diseaseComponent = new Disease(this.entity, transform, this, reader, (FloatTrait)this.getTrait(0));
        }
        this.breedComponent = this.lifeBlueprint.breedInfo.loadInstance(reader, this.entity, this);
        this.addRemoveListener();
    }

    public LifeCompBlueprint getLifeCompBlueprint() {
        return this.lifeBlueprint;
    }

    public Health getHealth() {
        return this.health;
    }

    public void setHungerComponent(EatingComponent hungerComp) {
        this.hungerComponent = hungerComp;
    }

    public float getWellbeing() {
        return this.wellbeing;
    }

    public BreedingComponent getBreedComponent() {
        return this.breedComponent;
    }

    public Disease getDiseaseComponent() {
        return this.diseaseComponent;
    }

    public EnviroComponent getEnviroComponent() {
        return this.enviroComponent;
    }

    public float getEnvironmentalSatisfaction() {
        return this.enviroComponent.getEnvironmentSatisfaction();
    }

    private void addRemoveListener() {
        this.entity.getNotifier().addRemoveListener(new EntityListener(){

            @Override
            public void execute() {
                LifeComponent.this.breedComponent.notifyRemoved();
                if (LifeComponent.this.diseaseComponent != null) {
                    LifeComponent.this.diseaseComponent.notifyDead();
                }
            }
        });
    }

    @Override
    public void getPerformanceBuffsInfo(List<TextStatInfo> info) {
        if (this.diseaseComponent != null) {
            this.diseaseComponent.getLiklihoodInfo(info);
        }
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
        this.fullyRecalculateWellbeing();
        info.add(new ProgressInfo(NAME, EntityInfoGui.FONT_SIZE, true){

            @Override
            protected GuiComponent addMouseOver() {
                return new HealthPopUp(LifeComponent.this, LifeComponent.this.hungerComponent);
            }

            @Override
            protected float getValue() {
                LifeComponent.this.fullyRecalculateWellbeing();
                return LifeComponent.this.wellbeing;
            }
        });
        info.add(new TextInfo(AGE_STAT, EntityInfoGui.FONT_SIZE){

            @Override
            public String getValue() {
                return Calendar.formatTimeHours(LifeComponent.this.health.getAge());
            }
        });
        info.add(new TextInfo(GENERATIONS, EntityInfoGui.FONT_SIZE){

            @Override
            public String getValue() {
                return Maths.formatNumber(LifeComponent.this.breedComponent.getGeneration());
            }
        });
        this.enviroComponent.getStatusInfo(info);
    }

    @Override
    public void update() {
        this.health.update();
        if (this.diseaseComponent != null && GameManager.getGameMode() != GameMode.BUILD) {
            this.diseaseComponent.update();
        }
        if (GameManager.getGameMode() != GameMode.BUILD) {
            this.breedComponent.update();
        }
        this.updateDp();
        if (this.ticker.isActive()) {
            this.fullyRecalculateWellbeing();
        }
        if (this.wellbeing < 0.001f) {
            this.naturalDie();
        }
    }

    public void naturalDie() {
        this.entity.die(this.lifeBlueprint.getDeathAiInstance(this.entity), false);
    }

    public DeathAi getDeathAi() {
        return this.lifeBlueprint.getDeathAiInstance(this.entity);
    }

    private void updateDp() {
        if (this.timer.check()) {
            float modifier = 0.2f + this.enviroComponent.getEnvironmentSatisfaction();
            this.entity.getDpCounter().registerModifier(this.getType(), modifier);
        }
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        this.enviroComponent.export(writer);
        writer.writeFloat(this.wellbeing);
        this.health.export(writer);
        if (this.diseaseComponent != null) {
            this.diseaseComponent.export(writer);
        }
        this.breedComponent.export(writer);
    }

    protected void fullyRecalculateWellbeing() {
        this.enviroComponent.recalculate();
        this.health.fullyRecalculateHealth();
        this.updateWellbeingValue();
    }

    protected void reAddWellbeing() {
        this.health.reAddHealth();
        this.updateWellbeingValue();
    }

    private void updateWellbeingValue() {
        if (GameManager.getGameMode() == GameMode.BUILD) {
            this.wellbeing = 1.0f;
            return;
        }
        this.wellbeing = this.getEnvironmentalSatisfaction() * this.getHungerValue() - (1.0f - this.health.getHealth());
        this.wellbeing = Math.max(0.0f, this.wellbeing);
    }

    public float getHungerValue() {
        if (this.hungerComponent == null || this.hungerComponent.getHunger() >= 0.7f) {
            return 1.0f;
        }
        float hunger = (0.7f - this.hungerComponent.getHunger()) / 0.7f;
        hunger *= hunger;
        return 1.0f - hunger;
    }
}

