/*
 * Decompiled with CFR 0.152.
 */
package health;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import breedingTraits.FloatTrait;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityBundle.EntityBundle;
import gameManaging.GameManager;
import health.HealSearchAi;
import health.LifeComponent;
import instances.Entity;
import java.io.IOException;
import java.util.List;
import languages.ComplexString;
import languages.GameText;
import main.Camera;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import org.lwjgl.util.vector.Vector3f;
import particles.Particle;
import particles.ParticleTexture;
import resourceManagement.ParticleAtlasCache;
import toolbox.Colour;
import toolbox.Maths;
import toolbox.Transformation;
import userInterfaces.Listener;
import userInterfaces.TextStatInfo;
import utils.BinaryReader;
import utils.BinaryWriter;

public class Disease
implements AiProvidingComponent {
    private static final String NOTIFICATION_TITLE = GameText.getText(1009);
    private static final String DIS_CHANCE = GameText.getText(1014);
    private static final String HEAL_CHANCE = GameText.getText(1015);
    private static final String HEAL_DESC = GameText.getText(1017);
    private static final String DIS_DESC = GameText.getText(1016);
    private static final ComplexString NOTIFICATION_MESSAGE = GameText.getComplexText(1010);
    private static final float MAX_ENVIRO = 0.5f;
    private static final int DISEASE_TEXTURE_ID = 3;
    private static final int DAMAGE_TEXTURE_ID = 4;
    private static final float TEST_PERIOD = 2.0f;
    private static final float DAY_CHANCE = 0.0027777778f;
    private static final float HIGH_AVERAGE_DAYS = 70.0f;
    private static final float LOW_AVERAGE_DAYS = 7.0f;
    private static final float LOW_CURE_CHANCE = 0.02f;
    private static final float HIGH_CURE_CHANCE = 0.15f;
    private static final float INFECT_OTHER_HIGH_CHANCE = 0.3f;
    private static final float INFECT_OTHER_SAFE = 0.5f;
    private static final float DAYS_DIFFERENT = 63.0f;
    private static final float INFECTION_PERIOD = 10.0f;
    private static final float DISEASE_HIT_PERIOD = 2.5f;
    private static final float DISEASE_DAMAGE = 0.01f;
    private static final int PARTICLE_COUNT = 7;
    private static final float EFFECT_INTENSE = 0.6f;
    private static final float EFFECT_TIME = 0.3f;
    private static final Colour EFFECT_COL = new Colour(1.0f, 0.0f, 1.0f);
    private final Entity entity;
    private final Transformation transform;
    private final LifeComponent wellbeing;
    private final FloatTrait diseaseResistance;
    private boolean infected = false;
    private boolean sufferingFromDisease = false;
    private boolean selfInfected = false;
    private float diseaseDamage = 0.0f;
    private boolean searchingForHeal = false;
    private float infectedTime = 0.0f;
    private float diseaseTime = Maths.RANDOM.nextFloat() * 2.5f;
    private float testTime = Maths.RANDOM.nextFloat() * 2.0f;

    protected Disease(Entity entity, Transformation transform, LifeComponent wellbeing, FloatTrait trait) {
        this.entity = entity;
        this.wellbeing = wellbeing;
        this.transform = transform;
        this.diseaseResistance = trait;
    }

    protected Disease(Entity entity, Transformation transform, LifeComponent wellbeing, BinaryReader reader, FloatTrait trait) throws Exception {
        this.entity = entity;
        this.wellbeing = wellbeing;
        this.transform = transform;
        this.diseaseResistance = trait;
        this.loadDiseaseSettings(reader);
    }

    public void getLiklihoodInfo(List<TextStatInfo> info) {
        float val;
        String title = this.sufferingFromDisease ? HEAL_CHANCE : DIS_CHANCE;
        String desc = this.sufferingFromDisease ? HEAL_DESC : DIS_DESC;
        float chanceSafe = 1.0f - this.getSelfInfectedChance();
        float infect = (float)Math.pow(chanceSafe, 360.0);
        float f = val = this.sufferingFromDisease ? this.getCuredChance() : 1.0f - infect;
        if (val <= 0.0f) {
            return;
        }
        int value = Math.round(val * 100.0f);
        info.add(new TextStatInfo(title, String.valueOf(value) + "%", desc));
    }

    public void heal(int amount) {
        if (this.diseaseDamage >= 0.0f) {
            this.diseaseDamage -= (float)amount;
            this.diseaseDamage = Math.max(0.0f, this.diseaseDamage);
            this.wellbeing.reAddWellbeing();
        }
        if (this.sufferingFromDisease && this.diseaseDamage == 0.0f) {
            this.cure();
        }
    }

    protected void export(BinaryWriter writer) throws IOException {
        writer.writeBoolean(this.sufferingFromDisease);
        writer.writeFloat(this.diseaseDamage);
    }

    protected boolean isDiseased() {
        return this.sufferingFromDisease;
    }

    protected float getDiseaseDamage() {
        return this.diseaseDamage;
    }

    protected void notifyDead() {
        this.endDisease();
    }

    protected void update() {
        boolean testTime = this.isTestTime();
        if (this.infected) {
            this.updateInfection(testTime);
        } else if (testTime) {
            this.checkSelfInfected();
        }
    }

    private void infect() {
        if (!this.infected) {
            this.infectedTime = 0.0f;
            this.infected = true;
        }
    }

    private void updateInfection(boolean testTime) {
        AiComponent ai;
        this.attemptToInfectOther(testTime);
        if (this.sufferingFromDisease) {
            this.applyDiseaseDamage();
            this.tryToCure(testTime);
        } else {
            this.checkDiseased();
        }
        if (this.sufferingFromDisease && !this.searchingForHeal && (ai = (AiComponent)this.entity.getComponent(ComponentType.AI)) != null) {
            Vector3f pos = this.transform.getPosition();
            EntityBundle bundle = GameManager.getWorld().getListOfEntities(ComponentType.HEALER, 2, pos.x, pos.z);
            if (bundle.isEmpty()) {
                return;
            }
            MovementComp mover = (MovementComp)((Object)this.entity.getComponent(ComponentType.MOVEMENT));
            if (mover == null) {
                return;
            }
            this.searchingForHeal = true;
            ai.queueAiProgram(new HealSearchAi(bundle.getRandomEntity(), this, mover));
        }
    }

    private void attemptToInfectOther(boolean testTime) {
        if (!testTime) {
            return;
        }
        EntityBundle potentialVictims = GameManager.getWorld().getListOfSimilarNearbySpecies(this.entity, 2);
        if (potentialVictims.getSize() <= 1) {
            return;
        }
        Entity randomVictim = potentialVictims.getRandomEntity();
        LifeComponent victimsWellbeing = (LifeComponent)randomVictim.getComponent(ComponentType.LIFE);
        float victimDiseaseResistance = victimsWellbeing.getDiseaseComponent().diseaseResistance.value;
        float chance = this.getInfectOtherChance(victimsWellbeing.getWellbeing(), victimDiseaseResistance);
        if (Maths.RANDOM.nextFloat() < chance) {
            victimsWellbeing.getDiseaseComponent().infect();
        }
    }

    private void checkSelfInfected() {
        if (!this.entity.getBlueprint().isAnimal()) {
            return;
        }
        float chance = this.getSelfInfectedChance();
        if (Maths.RANDOM.nextFloat() < chance) {
            this.infect();
            this.selfInfected = true;
        }
    }

    private void tryToCure(boolean testTime) {
        if (!testTime) {
            return;
        }
        float chance = this.getCuredChance();
        if (Maths.RANDOM.nextFloat() < chance) {
            this.cure();
        }
    }

    private void cure() {
        this.infected = false;
        this.selfInfected = false;
        this.sufferingFromDisease = false;
        this.infectedTime = 0.0f;
        this.endDisease();
    }

    private void initDisease() {
        this.sufferingFromDisease = true;
        this.displayDiseaseIndicators();
        if (this.selfInfected) {
            InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)this.entity.getBlueprint().getComponent(ComponentType.INFO);
            EquilinoxGuis.notify(NOTIFICATION_TITLE, NOTIFICATION_MESSAGE.getString(info.getName()), GuiRepository.DISEASE, GuiSounds.NOTIFY, new Listener(){

                @Override
                public void eventOccurred(boolean on) {
                    Camera.getCamera().focusOn(Disease.this.entity.getTransform().getPosition());
                }
            });
        }
    }

    private void displayDiseaseIndicators() {
        EquilinoxGuis.getToolBar().getDiseaseCounter().incrementCount(this.transform);
        ParticleTexture diseaseTexture = ParticleAtlasCache.getAtlas(3);
        this.entity.iconDisplay.showStatusIcon(diseaseTexture, 0.15f);
    }

    private void applyDiseaseDamage() {
        this.diseaseTime += GameManager.getGameSeconds();
        if (this.diseaseTime >= 2.5f) {
            this.entity.getTinter().pulse(EFFECT_COL, 0.6f, 0.3f);
            this.diseaseTime = 0.0f;
            this.diseaseDamage += 0.01f;
            this.emitParticles();
            this.wellbeing.reAddWellbeing();
        }
    }

    private void checkDiseased() {
        this.infectedTime += GameManager.getGameSeconds();
        if (this.infectedTime >= 10.0f) {
            this.initDisease();
        }
    }

    private void endDisease() {
        boolean removed = this.entity.iconDisplay.removeStatusIcon(ParticleAtlasCache.getAtlas(3));
        if (removed) {
            EquilinoxGuis.getToolBar().getDiseaseCounter().decrementCount(this.transform);
        }
    }

    private boolean isTestTime() {
        this.testTime += GameManager.getGameSeconds();
        if (this.testTime >= 2.0f) {
            this.testTime %= 2.0f;
            return true;
        }
        return false;
    }

    private void loadDiseaseSettings(BinaryReader reader) throws Exception {
        this.sufferingFromDisease = reader.readBoolean();
        if (this.sufferingFromDisease) {
            this.infected = true;
            this.displayDiseaseIndicators();
        }
        this.diseaseDamage = reader.readFloat();
    }

    private float getInfectOtherChance(float otherWellbeing, float otherDisRes) {
        if (otherWellbeing > 0.5f) {
            return 0.0f;
        }
        float blend = 1.0f - otherWellbeing / 0.5f;
        float chance = blend * 0.3f;
        return chance / otherDisRes;
    }

    private float getSelfInfectedChance() {
        float disRes = this.diseaseResistance.value;
        float enviro = this.wellbeing.getEnvironmentalSatisfaction();
        if (enviro > 0.5f) {
            return 0.0f;
        }
        float averageDays = 7.0f + enviro / 0.5f * 63.0f;
        float chance = 0.0027777778f / (averageDays * disRes);
        return chance;
    }

    private float getCuredChance() {
        float wellbeing = this.wellbeing.getWellbeing();
        if (wellbeing < 0.5f) {
            return 0.01f;
        }
        float factor = (wellbeing - 0.5f) / 0.5f;
        return 0.02f + factor * 0.13000001f;
    }

    private void emitParticles() {
        Vector3f position = new Vector3f(this.transform.getPosition());
        position.y += this.entity.getBoundingBox().getHeight() / 2.0f;
        int i = 0;
        while (i < 7) {
            Vector3f velocity = Maths.generateRandomUnitVectorWithinCone(new Vector3f(0.0f, 1.0f, 0.0f), 30.0f);
            velocity.scale(1.8f);
            float lifeLength = Maths.RANDOM.nextFloat() * 0.2f + 0.4f;
            ParticleTexture damageTexture = ParticleAtlasCache.getAtlas(4);
            new Particle(damageTexture, new Vector3f(position), velocity, 0.4f, lifeLength, Maths.RANDOM.nextFloat() * 360.0f, 0.05f);
            ++i;
        }
    }

    @Override
    public void notifyAiFinished() {
        this.searchingForHeal = false;
    }
}

