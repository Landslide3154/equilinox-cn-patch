/*
 * Decompiled with CFR 0.152.
 */
package eating;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import classification.Classification;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import eating.DietOption;
import eating.DietTracker;
import eating.EatingCompBlueprint;
import eating.FoodFinder;
import entityInfoGui.BarMouseOverGui;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.PopUpInfoGui;
import entityInfoGui.ProgressInfo;
import entityInfoGui.TextInfo;
import events.EventData;
import events.EventManager;
import food.FoodComponent;
import food.FoodSectionType;
import gameManaging.GameManager;
import growth.GrowthComponent;
import guis.GuiComponent;
import health.LifeComponent;
import instances.Entity;
import instances.EntityListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import mainGuis.EquilinoxGuis;
import resourceManagement.ParticleAtlasCache;
import session.GameMode;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class EatingComponent
extends Component
implements AiProvidingComponent {
    private static final String HUNGER = GameText.getText(620);
    private static final String DIET = GameText.getText(621);
    private static final String MIXED = GameText.getText(623);
    private static final int TRACKED_DIET = 5;
    public static final float HUNGRY = 0.7f;
    public static final float STARVING = 0.35f;
    public static final float START_HUNGER = 0.75f;
    private static final int STANDARD_FOOD = 10;
    private static final float SMALL_HUNGER = 0.5f;
    private static final float HUNGER_STEPS = 0.2f;
    private static final float ATTEMPT_COOLDOWN = 4.0f;
    private final EatingCompBlueprint eatingBlueprint;
    private AiComponent aiComponent;
    private GrowthComponent growthComponent;
    private Transformation transform;
    private MovementComp mover;
    private Entity entity;
    private float hungerPoints;
    private boolean gettingFood = false;
    private boolean gettingFoodUrgently = false;
    private boolean cooling = false;
    private float cooldown = 0.0f;
    private DietTracker dietTracker = new DietTracker(5);
    private boolean showingIcon = false;

    public EatingComponent(EatingCompBlueprint eatingBlueprint) {
        super(eatingBlueprint);
        this.eatingBlueprint = eatingBlueprint;
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        this.aiComponent = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.entity = bundle.getEntity();
        this.hungerPoints = (float)this.eatingBlueprint.getMaxHungerPoints() * 0.75f;
        this.growthComponent = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        ((LifeComponent)bundle.getComponent(ComponentType.LIFE)).setHungerComponent(this);
        this.entity.getNotifier().addDeadListener(new EntityListener(){

            @Override
            public void execute() {
                if (EatingComponent.this.showingIcon) {
                    EquilinoxGuis.getToolBar().getHungerCounter().decrementCount(EatingComponent.this.transform);
                }
            }
        });
    }

    public boolean runsToFood() {
        return this.eatingBlueprint.runsToFood();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
        this.hungerPoints = reader.readFloat();
        this.dietTracker = DietTracker.load(5, reader);
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeFloat(this.hungerPoints);
        this.dietTracker.export(writer);
    }

    public float getHunger() {
        return this.hungerPoints / (float)this.eatingBlueprint.getMaxHungerPoints();
    }

    public String getHungerString() {
        return String.valueOf(Math.round(this.hungerPoints)) + "/" + this.eatingBlueprint.getMaxHungerPoints();
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
        this.addHungerBar(info);
        this.addDietInfo(info);
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void update() {
        if (this.eatingBlueprint.hasEggStage() && this.growthComponent.getStageNumber() == 0) {
            return;
        }
        if (GameManager.getGameMode() == GameMode.BUILD) {
            return;
        }
        this.decreaseHungerPoints();
        this.updateCoolDown();
        if (!this.cooling && this.isUrgentlyHungry() && !this.gettingFoodUrgently) {
            FoodFinder foodFinder = new FoodFinder(this.transform, this);
            if (foodFinder.chooseTarget()) {
                this.gettingFood = true;
                this.gettingFoodUrgently = true;
                this.aiComponent.queueAiProgram(this.eatingBlueprint.getEatingAi(foodFinder, this.transform, this.mover, this.growthComponent, this, true));
            } else {
                this.cooldown();
            }
        } else if (!this.gettingFood && !this.cooling && this.isHungry()) {
            FoodFinder foodFinder = new FoodFinder(this.transform, this);
            if (foodFinder.chooseTarget()) {
                this.gettingFood = true;
                this.aiComponent.queueAiProgram(this.eatingBlueprint.getEatingAi(foodFinder, this.transform, this.mover, this.growthComponent, this, false));
            } else {
                this.cooldown();
            }
        }
    }

    @Override
    public void notifyAiFinished() {
        this.gettingFood = false;
        this.gettingFoodUrgently = false;
        this.cooldown();
    }

    public int getSearchRange() {
        float hungerValue = this.hungerPoints / (float)this.eatingBlueprint.getMaxHungerPoints();
        if (hungerValue > 0.5f) {
            return 1;
        }
        return (int)((0.5f - hungerValue) / 0.2f + 2.0f);
    }

    public DietTracker getCurrentDiet() {
        return this.dietTracker;
    }

    protected void eat(FoodComponent food, FoodSectionType sectionType) {
        if (food == null) {
            this.hungerPoints += 10.0f;
        } else {
            this.hungerPoints += (float)food.eat(sectionType);
            EventManager.EAT.registerEvent(new EventData(food.getEntity()), food.getEntity().getBlueprint().getSpeciesClassification().getKey(), this.entity.getBlueprint().getSpeciesClassification().getKey());
            this.dietTracker.registerMeal(food.getEntity().getBlueprint().getSpeciesClassification());
            if (food.hasEffect()) {
                food.getEffect().apply(this.entity);
            }
        }
        this.hungerPoints = Math.min((float)this.eatingBlueprint.getMaxHungerPoints(), this.hungerPoints);
        if (this.showingIcon && !this.isUrgentlyHungry()) {
            this.showingIcon = false;
            this.entity.iconDisplay.removeStatusIcon(ParticleAtlasCache.getAtlas(15));
            EquilinoxGuis.getToolBar().getHungerCounter().decrementCount(this.transform);
        }
    }

    protected DietOption[] getDiet() {
        return this.eatingBlueprint.getDiet();
    }

    private void addDietInfo(List<PopUpInfoGui> info) {
        info.add(new TextInfo(DIET, EntityInfoGui.FONT_SIZE){

            @Override
            public String getValue() {
                Classification currentDiet = EatingComponent.this.dietTracker.getCurrentMainDiet();
                if (currentDiet == null) {
                    return MIXED;
                }
                return currentDiet.getName();
            }
        });
    }

    private void addHungerBar(List<PopUpInfoGui> info) {
        info.add(new ProgressInfo(HUNGER, EntityInfoGui.FONT_SIZE, true){

            @Override
            protected GuiComponent addMouseOver() {
                String[] headers = new String[]{HUNGER};
                return new BarMouseOverGui(headers){

                    @Override
                    public List<BarMouseOverGui.StatData> getData() {
                        ArrayList<BarMouseOverGui.StatData> data = new ArrayList<BarMouseOverGui.StatData>();
                        data.add(new BarMouseOverGui.StatData(EatingComponent.this.getHungerString(), BarMouseOverGui.HEADING_COLOUR));
                        return data;
                    }
                };
            }

            @Override
            protected float getValue() {
                return EatingComponent.this.hungerPoints / (float)EatingComponent.this.eatingBlueprint.getMaxHungerPoints();
            }
        });
    }

    private boolean isHungry() {
        return this.hungerPoints < (float)this.eatingBlueprint.getMaxHungerPoints() * 0.7f;
    }

    private boolean isUrgentlyHungry() {
        return this.hungerPoints < (float)this.eatingBlueprint.getMaxHungerPoints() * 0.35f;
    }

    private void cooldown() {
        this.cooling = true;
        this.cooldown = 4.0f;
    }

    private void decreaseHungerPoints() {
        this.hungerPoints -= this.eatingBlueprint.getHungerPerHour() * GameManager.getDeltaHours();
        if (this.isUrgentlyHungry() && !this.showingIcon && !this.entity.isDead()) {
            this.entity.iconDisplay.showStatusIcon(ParticleAtlasCache.getAtlas(15), 0.1f);
            this.showingIcon = true;
            EquilinoxGuis.getToolBar().getHungerCounter().incrementCount(this.transform);
        }
        this.hungerPoints = Math.max(0.0f, this.hungerPoints);
    }

    private void updateCoolDown() {
        if (this.cooling) {
            this.cooldown -= GameManager.getGameSeconds();
            if (this.cooldown <= 0.0f) {
                this.cooldown = 0.0f;
                this.cooling = false;
            }
        }
    }
}

