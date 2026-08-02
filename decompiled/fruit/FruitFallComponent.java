/*
 * Decompiled with CFR 0.152.
 */
package fruit;

import blueprints.Blueprint;
import breedingTraits.FloatTrait;
import classification.Classifier;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import fruit.DecayParams;
import fruit.FruitFallCompBlueprint;
import fruit.FruiterComponent;
import gameManaging.GameManager;
import health.LifeComponent;
import instances.Entity;
import interpolation.Timer;
import java.util.List;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import resourceManagement.BlueprintRepository;
import toolbox.Maths;
import toolbox.Transformation;
import userInterfaces.TextStatInfo;
import utils.BinaryReader;
import utils.BinaryWriter;

public class FruitFallComponent
extends Component {
    private static final float BOOST_AMOUNT = 0.3f;
    private static final String FRUIT_BUFF_NAME = GameText.getText(255);
    private static final String INSECT_BUFF_NAME = GameText.getText(258);
    private static final String ENVIRO_BOOST = GameText.getText(256);
    private static final String INSECT_BOOST = GameText.getText(257);
    private static final int FRUIT_EXPLODE_COUNT = 12;
    private static final float NORMAL_SPEED = 0.5f;
    private static final float EXPLODE_SPEED = 2.5f;
    private static final float BAD_ENV_BOOST = 0.2f;
    private final FruitFallCompBlueprint blueprint;
    private Transformation transform;
    private FruiterComponent fruiter;
    private LifeComponent lifeComp;
    private final Timer insectCheck = Timer.createLoopingTimer(5.0f, false);
    private boolean boosted = false;

    protected FruitFallComponent(FruitFallCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void update() {
        float boost;
        if (this.fruiter != null && !this.fruiter.hasFruit()) {
            return;
        }
        this.checkForInsects();
        float chance = GameManager.getDeltaHours() / this.blueprint.averageFruitTime * ((FloatTrait)super.getTrait(0)).getValue();
        float boostValue = 1.3f;
        float f = boost = this.boosted ? boostValue : 1.0f;
        if (Maths.chance(chance * boost * this.calcEnvironmentalFactor())) {
            this.spawnFruit(false);
        }
    }

    @Override
    public void getPerformanceBuffsInfo(List<TextStatInfo> info) {
        int enviroPercent = (int)(this.calcEnvironmentalFactor() * 100.0f);
        info.add(new TextStatInfo(FRUIT_BUFF_NAME, String.valueOf(enviroPercent) + "%", ENVIRO_BOOST));
        float boostAmount = 30.000002f;
        if (this.boosted) {
            info.add(new TextStatInfo(INSECT_BUFF_NAME, "+" + (int)boostAmount + "%", INSECT_BOOST));
        }
    }

    @Override
    public void getActions(List<Action> actions) {
        actions.add(new Action("Drop All Fruit", 10){

            @Override
            public void carryOut() {
                FruitFallComponent.this.dropAll();
            }
        });
    }

    public void dropAll() {
        if (this.fruiter != null) {
            if (!this.fruiter.hasFruit()) {
                return;
            }
            this.fruiter.removeAllFruit();
        }
        int i = 0;
        while (i < 12) {
            this.spawnFruit(true);
            ++i;
        }
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.fruiter = (FruiterComponent)bundle.getComponent(ComponentType.FRUITER);
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.lifeComp = (LifeComponent)bundle.getComponent(ComponentType.LIFE);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    private void checkForInsects() {
        if (this.insectCheck.check()) {
            Vector3f pos = this.transform.getPosition();
            int pop = GameManager.getWorld().getPopulation(Classifier.getClassification("ai"), 2, pos.x, pos.z);
            this.boosted = pop > 0;
        }
    }

    private float calcEnvironmentalFactor() {
        float enviro = this.lifeComp.getEnvironmentalSatisfaction();
        return 0.2f + enviro * 0.8f;
    }

    private void spawnFruit(boolean explode) {
        Transformation.TransformBlueprint transformBlueprint = this.transform.getBlueprint();
        Vector3f treePos = this.transform.getPosition();
        Vector3f rand = Maths.randomPointOnCircle(Maths.UP, this.blueprint.spawnRadius * this.transform.getScaleTrait().getValue());
        Vector3f pos = Vector3f.add(treePos, rand, null);
        float height = GameManager.getWorld().getHeightOfTerrain(pos.x, pos.z);
        pos.y = height + this.blueprint.spawnHeight * this.transform.getScaleTrait().getValue();
        Transformation.TransformParams params = new Transformation.TransformParams(new Vector3f(pos), Maths.RANDOM.nextFloat() * 360.0f, transformBlueprint.generateRandomScale());
        Vector3f velocity = Maths.generateRandomUnitVector();
        if (explode) {
            velocity.scale(explode ? 2.5f : 0.5f);
        }
        DecayParams params2 = new DecayParams(velocity);
        Blueprint fruitBlueprint = BlueprintRepository.getBlueprint(this.blueprint.fruitModelId);
        Entity entity = fruitBlueprint.createInstance(params, params2);
        GameManager.getSession().getWorld().addInstance(entity, false);
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }
}

