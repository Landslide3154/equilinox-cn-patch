/*
 * Decompiled with CFR 0.152.
 */
package componentArchitecture;

import aiComponent.AiCompLoader;
import baseMovement.MovementCompLoader;
import beavers.BeaverCompLoader;
import beavers.WoodCompLoader;
import biomes.SpreaderCompLoader;
import birdHunt.BirdHuntCompLoader;
import blueprints.Blueprint;
import building.BuildCompLoader;
import building.BuilderCompLoader;
import building.DecomposeCompLoader;
import carnivorePlant.FlyTrapCompLoader;
import carnivorePlant.TongueShootCompLoader;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.Requirement;
import components.InformationComponent;
import components.LilyComponent;
import components.NameComponent;
import eating.EatingCompLoader;
import equipping.EquipCompLoader;
import equipping.ItemCompLoader;
import fighting.FightCompLoader;
import fighting.HostileCompLoader;
import fishHunt.FishHuntCompLoader;
import flinging.FlingingCompLoader;
import food.FoodCompLoader;
import fruit.DecayCompLoader;
import fruit.FruitFallLoader;
import fruit.FruiterCompLoader;
import growth.GrowthCompLoader;
import healer.HealerCompLoader;
import health.LifeCompLoader;
import hiveComponents.BeeCompLoader;
import hiveComponents.HiveCompLoader;
import hunting.FleeCompLoader;
import hunting.HuntCompLoader;
import loot.DropCompLoader;
import materials.MaterialComponent;
import meerkats.BurrowCompLoader;
import meerkats.TimeOutCompLoader;
import monkeys.ClingCompLoader;
import monkeys.TreeSwingCompLoader;
import nesting.NestingCompLoader;
import nightBloom.BloomCompLoader;
import panic.PanicCompLoader;
import particleComponent.ParticleCompLoader;
import peacock.PeacockCompLoader;
import perching.PerchCompLoader;
import perching.PercherCompLoader;
import simpleAnimations.AnimationCompLoader;
import sleeping.SleepCompLoader;
import sound.RandomSounderLoader;
import sound.SoundCompLoader;
import sound.SoundLooperLoader;
import spitting.ProjectileCompLoader;
import spitting.SpitCompLoader;
import stinging.StingingCompLoader;
import sunFacer.SunFaceCompLoader;
import toolbox.Transformation;
import treeCharging.TreeChargeCompLoader;
import utils.CSVReader;

public enum ComponentType {
    TRANSFORM(new Transformation.TransformLoader(), false, false),
    MESH(null, false, false),
    MATERIAL(new MaterialComponent.MaterialCompLoader(), false, false),
    SOUND(new SoundCompLoader(), false, true),
    NAME(new NameComponent.NameCompLoader(), false, false),
    INFO(new InformationComponent.InformationCompLoader(), false, false),
    LIFE(new LifeCompLoader(), false, true),
    ANIMATION(new AnimationCompLoader(), true, true),
    PARTICLES(new ParticleCompLoader(), false, true),
    HEALER(new HealerCompLoader(), false, false),
    SPREADER(new SpreaderCompLoader(), false, false),
    MOVEMENT(new MovementCompLoader(), true, true),
    AI(new AiCompLoader(), false, true),
    EATING(new EatingCompLoader(), false, true),
    FOOD(new FoodCompLoader(), false, false),
    FRUITER(new FruiterCompLoader(), false, true),
    GROWTH(new GrowthCompLoader(), false, true),
    LILY(new LilyComponent.LilyCompLoader(), true, true),
    RANDOM_SOUNDER(new RandomSounderLoader(), false, true),
    FLEE(new FleeCompLoader(), true, false),
    FRUIT_FALL(new FruitFallLoader(), false, true),
    DECAY(new DecayCompLoader(), true, true),
    BUILD(new BuildCompLoader(), false, false),
    BUILDER(new BuilderCompLoader(), false, true),
    HIVE(new HiveCompLoader(), false, true),
    PERCH(new PerchCompLoader(), false, false),
    BEAVER(new BeaverCompLoader(), true, true),
    EQUIP(new EquipCompLoader(), false, true),
    WOOD(new WoodCompLoader(), false, false),
    SLEEP(new SleepCompLoader(), true, true),
    CHARGE(new TreeChargeCompLoader(), true, true),
    PANIC(new PanicCompLoader(), true, true),
    HUNT(new HuntCompLoader(), true, true),
    FIGHT(new FightCompLoader(), true, false),
    DROP(new DropCompLoader(), false, false),
    ITEM(new ItemCompLoader(), true, true),
    NESTING(new NestingCompLoader(), false, false),
    DECOMPOSE(new DecomposeCompLoader(), false, true),
    PERCHER(new PercherCompLoader(), false, false),
    FLINGING(new FlingingCompLoader(), true, true),
    CLING(new ClingCompLoader(), true, true),
    TREE_SWING(new TreeSwingCompLoader(), true, true),
    HOSTILE(new HostileCompLoader(), true, true),
    BEE(new BeeCompLoader(), false, false),
    FISH_HUNT(new FishHuntCompLoader(), true, true),
    PEACOCK(new PeacockCompLoader(), true, true),
    SOUND_LOOPER(new SoundLooperLoader(), false, true),
    SUN_FACER(new SunFaceCompLoader(), true, true),
    SPITTING(new SpitCompLoader(), true, true),
    PROJECTILE(new ProjectileCompLoader(), true, true),
    STINGING(new StingingCompLoader(), true, true),
    BLOOM(new BloomCompLoader(), false, true),
    BIRD_HUNT(new BirdHuntCompLoader(), true, true),
    TONGUE_SHOOT(new TongueShootCompLoader(), true, true),
    FLY_TRAP(new FlyTrapCompLoader(), false, true),
    BURROW(new BurrowCompLoader(), true, true),
    TIME_OUT(new TimeOutCompLoader(), false, true);

    private ComponentLoader loader;
    private boolean dynamic;
    private boolean active;

    private ComponentType(ComponentLoader loader, boolean dynamic, boolean active) {
        this.loader = loader;
        this.dynamic = dynamic;
        this.active = active;
    }

    public ComponentBlueprint loadComponent(CSVReader reader, Blueprint blueprint) {
        return this.loader.load(reader, blueprint);
    }

    public Requirement loadRequirement(CSVReader reader) {
        return this.loader.loadRequirement(reader);
    }

    protected boolean isDynamic() {
        return this.dynamic;
    }

    public boolean isActive() {
        return this.active;
    }

    public static ComponentType getType(int id) {
        return ComponentType.values()[id];
    }
}

