/*
 * Decompiled with CFR 0.152.
 */
package health;

import classification.Classification;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import gameManaging.GameManager;
import health.DamageListener;
import health.Disease;
import health.LifeComponent;
import instances.Entity;
import interpolation.SmoothFloat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import loot.DropComponent;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Colour;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;
import world.World;

public class Health {
    private static final double LIFE_DELTA_STD = 0.25;
    private static final double LIFE_EXP_BIAS = 1.25;
    private static final float HEALTH_VAL_AGILITY = 0.4f;
    private static final float EFFECT_INTENSE = 0.6f;
    private static final float EFFECT_TIME = 0.3f;
    private static final Colour EFFECT_COL = new Colour(1.0f, 0.0f, 0.0f);
    private final float averagePopulationFactor;
    private final float lifeExpectancy;
    private final Entity entity;
    private final LifeComponent wellbeing;
    private short damagePoints;
    private float age = 0.0f;
    private float health = 1.0f;
    private SmoothFloat ageFactor = new SmoothFloat(1.0f, 0.4f);
    private float localPopualtion;
    private InformationComponent info;
    private List<DamageListener> damageListeners;

    protected Health(Entity entity, BinaryReader reader, float averagePopulation, LifeComponent wellbeing, InformationComponent info) throws Exception {
        this.health = reader.readFloat();
        this.age = reader.readFloat();
        this.damagePoints = reader.readShort();
        this.lifeExpectancy = reader.readFloat();
        this.averagePopulationFactor = averagePopulation;
        this.localPopualtion = averagePopulation;
        this.entity = entity;
        this.wellbeing = wellbeing;
        this.info = info;
    }

    protected Health(Entity entity, LifeComponent wellbeing, float averagePopulation, float averageLife, InformationComponent info) {
        this.lifeExpectancy = this.generateLifeExpectancy(averageLife);
        this.averagePopulationFactor = averagePopulation;
        this.localPopualtion = averagePopulation;
        this.entity = entity;
        this.wellbeing = wellbeing;
        this.info = info;
    }

    protected void export(BinaryWriter writer) throws IOException {
        writer.writeFloat(this.health);
        writer.writeFloat(this.age);
        writer.writeShort(this.damagePoints);
        writer.writeFloat(this.lifeExpectancy);
    }

    public void addDamageListener(DamageListener listener) {
        if (this.damageListeners == null) {
            this.damageListeners = new ArrayList<DamageListener>();
        }
        this.damageListeners.add(listener);
    }

    public boolean takeDamage(int amount, Entity attacker) {
        this.damagePoints = (short)(this.damagePoints + amount);
        this.entity.getTinter().pulse(EFFECT_COL, 0.6f, 0.3f);
        this.wellbeing.reAddWellbeing();
        if (this.wellbeing.getWellbeing() <= 0.0f && attacker != null) {
            this.dropItem();
        }
        this.notifyListeners(attacker);
        return this.wellbeing.getWellbeing() <= 0.0f;
    }

    private void dropItem() {
        DropComponent dropComp = (DropComponent)this.entity.getComponent(ComponentType.DROP);
        if (dropComp != null) {
            dropComp.indicateHunted();
        }
    }

    private void notifyListeners(Entity attacker) {
        if (this.damageListeners == null) {
            return;
        }
        for (DamageListener listener : this.damageListeners) {
            listener.damageOccurred(attacker);
        }
    }

    protected float getHealth() {
        return this.health;
    }

    protected float getAge() {
        return this.age;
    }

    protected float getPhysicalDamage() {
        return (float)this.damagePoints / (float)this.wellbeing.getLifeCompBlueprint().defencePoints;
    }

    protected float getLifeExpectancy() {
        return this.lifeExpectancy;
    }

    protected float getLifeExpectancyFactor() {
        return this.lifeExpectancy / this.wellbeing.getLifeCompBlueprint().getAverageLifeLength();
    }

    protected float getLocalPopulation() {
        return this.localPopualtion;
    }

    public float getLocalPopulationFactor() {
        return this.localPopualtion / this.averagePopulationFactor;
    }

    protected int getPopulationPercentage() {
        return Math.round(100.0f * this.localPopualtion / this.averagePopulationFactor);
    }

    protected void update() {
        this.age += GameManager.getDeltaHours();
        this.ageFactor.update(GameManager.getGameSeconds());
    }

    protected void fullyRecalculateHealth() {
        this.reevaluateAgeFactor();
        this.reAddHealth();
    }

    protected void reAddHealth() {
        this.health = Math.max(this.ageFactor.get(), 0.0f);
        this.health -= (float)this.damagePoints / (float)this.wellbeing.getLifeCompBlueprint().defencePoints;
        Disease disease = this.wellbeing.getDiseaseComponent();
        if (disease != null) {
            this.health -= this.wellbeing.getDiseaseComponent().getDiseaseDamage();
        }
        this.health = Math.max(0.0f, this.health);
    }

    private void reevaluateAgeFactor() {
        double lifeExpFactor = (double)this.lifeExpectancy * this.calculatePopulationFactor();
        this.ageFactor.setTarget((float)this.calculateAgeFactor(lifeExpFactor));
    }

    private double calculatePopulationFactor() {
        float[] popFactors;
        Vector3f pos = this.info.getBasePosition();
        World world = GameManager.getWorld();
        Classification classification = this.entity.getBlueprint().getSpeciesClassification();
        int totalPopulation = world.getPopulation(classification, this.info.getRoamingRange(), pos.x, pos.z);
        this.localPopualtion = totalPopulation;
        float[] fArray = popFactors = this.wellbeing.getLifeCompBlueprint().getPopulationFactors();
        int n = popFactors.length;
        int n2 = 0;
        while (n2 < n) {
            float popFactor = fArray[n2];
            classification = classification.getParent();
            if (popFactor != 0.0f) {
                int newPop = world.getPopulation(classification, this.info.getRoamingRange(), pos.x, pos.z);
                this.localPopualtion += (float)(newPop - totalPopulation) * popFactor;
                totalPopulation = newPop;
            }
            ++n2;
        }
        return this.averagePopulationFactor / this.localPopualtion;
    }

    private double calculateAgeFactor(double lifeFactor) {
        return 1.0 - (double)this.age / (lifeFactor * 1.25);
    }

    private float generateLifeExpectancy(float averageLife) {
        return Math.max((float)((double)averageLife * (Maths.RANDOM.nextGaussian() * 0.25 + 1.0)), 1.0f);
    }
}

