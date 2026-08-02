/*
 * Decompiled with CFR 0.152.
 */
package health;

import eating.EatingComponent;
import entityInfoGui.BarMouseOverGui;
import health.Disease;
import health.LifeComponent;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import time.Calendar;
import toolbox.Colour;
import toolbox.Maths;

public class HealthPopUp
extends BarMouseOverGui {
    private static final String HEALTH = GameText.getText(875);
    private static final String ENVIRO = GameText.getText(876);
    private static final String HUNGER = GameText.getText(877);
    private static final String LIFE_EX = GameText.getText(878);
    private static final String AGE = GameText.getText(872);
    private static final String POP = GameText.getText(879);
    private static final String DAMAGE = GameText.getText(880);
    private static final String DISEASE = GameText.getText(881);
    private static final String OKAY = "(" + GameText.getText(990) + ")";
    private static final String TOO_HIGH = "(" + GameText.getText(991) + ")";
    private static final String GOOD = "(" + GameText.getText(992) + ")";
    private LifeComponent lifeComponent;
    private EatingComponent eatingComponent;

    public HealthPopUp(LifeComponent lifeComponent, EatingComponent eatingComponent) {
        super(HealthPopUp.getHeaders(eatingComponent != null));
        this.lifeComponent = lifeComponent;
        this.eatingComponent = eatingComponent;
    }

    @Override
    public List<BarMouseOverGui.StatData> getData() {
        ArrayList<BarMouseOverGui.StatData> data = new ArrayList<BarMouseOverGui.StatData>();
        data.add(new BarMouseOverGui.StatData(String.valueOf(Math.round(this.lifeComponent.getWellbeing() * 100.0f)) + "%", HEADING_COLOUR));
        float enviro = this.lifeComponent.getEnvironmentalSatisfaction();
        float enviFactor = Maths.clamp((enviro - 0.35f) / 0.45f, 0.0f, 1.0f);
        data.add(new BarMouseOverGui.StatData(String.valueOf(Math.round(enviro * 100.0f)) + "%", this.getColour(enviFactor * enviFactor)));
        if (this.eatingComponent != null) {
            float fullness = Maths.clamp((this.eatingComponent.getHunger() - 0.2f) / 0.6f, 0.0f, 1.0f);
            data.add(new BarMouseOverGui.StatData(this.eatingComponent.getHungerString(), this.getColour(fullness)));
        }
        float lifeFactor = Math.min(this.lifeComponent.health.getLifeExpectancyFactor(), 1.0f);
        data.add(new BarMouseOverGui.StatData(Calendar.formatTimeHours(this.lifeComponent.health.getLifeExpectancy()), this.getColour(lifeFactor * lifeFactor)));
        float growness = this.lifeComponent.health.getAge() / this.lifeComponent.health.getLifeExpectancy();
        float ageFactor = Math.max(0.0f, 1.0f - growness);
        data.add(new BarMouseOverGui.StatData(Calendar.formatTimeHours(this.lifeComponent.health.getAge()), this.getColour(ageFactor)));
        int popPercent = this.lifeComponent.health.getPopulationPercentage();
        float popFactor = 1.0f - Maths.clamp((this.lifeComponent.health.getLocalPopulationFactor() - 1.0f) / 0.25f, 0.0f, 1.0f);
        String popString = String.valueOf(popPercent) + "% " + (popPercent > 110 ? TOO_HIGH : (popPercent < 90 ? GOOD : OKAY));
        data.add(new BarMouseOverGui.StatData(popString, this.getColour(popFactor)));
        float damage = this.lifeComponent.health.getPhysicalDamage();
        float healthiness = 1.0f - damage;
        data.add(new BarMouseOverGui.StatData(String.valueOf(Math.round(damage * 100.0f)) + "%", this.getColour(healthiness * healthiness * healthiness)));
        Disease disease = this.lifeComponent.getDiseaseComponent();
        float diseaseAmount = disease == null ? 0.0f : disease.getDiseaseDamage();
        String disString = String.valueOf(Math.round(diseaseAmount * 100.0f)) + "%";
        diseaseAmount = 1.0f - diseaseAmount;
        data.add(new BarMouseOverGui.StatData(disString, this.getColour(diseaseAmount * diseaseAmount * diseaseAmount)));
        return data;
    }

    private Colour getColour(float blend) {
        return Colour.interpolateColours(NEGATIVE_COLOUR, NORMAL_COLOUR, blend, null);
    }

    private static String[] getHeaders(boolean eater) {
        String[] headers = new String[eater ? 8 : 7];
        int pointer = 0;
        headers[pointer++] = HEALTH;
        headers[pointer++] = ENVIRO;
        if (eater) {
            headers[pointer++] = HUNGER;
        }
        headers[pointer++] = LIFE_EX;
        headers[pointer++] = AGE;
        headers[pointer++] = POP;
        headers[pointer++] = DAMAGE;
        headers[pointer++] = DISEASE;
        return headers;
    }
}

