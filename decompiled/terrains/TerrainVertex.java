/*
 * Decompiled with CFR 0.152.
 */
package terrains;

import basics.DisplayManager;
import biomes.Biome;
import environment.EnvironmentVariables;
import java.util.HashMap;
import java.util.Map;
import toolbox.Colour;

public class TerrainVertex {
    private static final float CHANGE_PER_SECOND = 0.04f;
    private static final int MAX_COUNT = 100;
    private final Map<Biome, Integer> biomeAmounts = new HashMap<Biome, Integer>();
    private final Map<Colour, Integer> colourWeights = new HashMap<Colour, Integer>();
    private final Colour background;
    private final boolean empty;
    private int totalBiomeWeight = 0;
    private Colour targetColour = new Colour();
    private Colour currentColour = new Colour();
    private float lastUpdateTime = 0.0f;

    protected TerrainVertex(Colour background, boolean empty) {
        this.empty = empty;
        this.background = background;
        this.updateTargetColour();
        this.currentColour.setColour(background);
    }

    public void addBiomeWeights(Biome biome, int count) {
        this.addBiomeWeight(biome, count);
        this.updateCurrentColour();
        this.updateTargetColour();
    }

    public void removeBiomeWeights(Biome biome, int count) {
        this.removeBiomeWeight(biome, count);
        this.updateCurrentColour();
        this.updateTargetColour();
    }

    public Map<Biome, Integer> getBiomeAmounts() {
        return this.biomeAmounts;
    }

    public int getBiomeWeight(Biome biome) {
        if (this.biomeAmounts.isEmpty()) {
            return 0;
        }
        Integer amount = this.biomeAmounts.get((Object)biome);
        return amount == null ? 0 : amount;
    }

    public float getBiomeDecimal(Biome biome) {
        float totalWeight = Math.max(this.totalBiomeWeight, 100);
        int weight = this.getBiomeWeight(biome);
        return (float)weight / totalWeight;
    }

    public float getBiomePercent(Biome biome) {
        return this.getBiomeDecimal(biome) * 100.0f;
    }

    public int getTotalWeights() {
        return this.totalBiomeWeight;
    }

    protected Colour getTargetColour() {
        return this.empty ? EnvironmentVariables.VOID_COLOUR : this.targetColour;
    }

    protected Colour getLastKnownColour() {
        return this.empty ? EnvironmentVariables.VOID_COLOUR : this.currentColour;
    }

    protected float getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    private void addBiomeWeight(Biome biome, int count) {
        Integer currentNumber = this.biomeAmounts.get((Object)biome);
        if (currentNumber == null) {
            currentNumber = 0;
        }
        currentNumber = currentNumber + count;
        this.totalBiomeWeight += count;
        this.biomeAmounts.put(biome, currentNumber);
    }

    private void removeBiomeWeight(Biome biome, int count) {
        Integer currentNumber = this.biomeAmounts.get((Object)biome);
        if (currentNumber == null) {
            return;
        }
        currentNumber = currentNumber - count;
        this.totalBiomeWeight -= count;
        if (currentNumber <= 0) {
            this.biomeAmounts.remove((Object)biome);
        } else {
            this.biomeAmounts.put(biome, currentNumber);
        }
    }

    private void updateTargetColour() {
        Colour totalBiomeColour = new Colour(0.0f, 0.0f, 0.0f);
        for (Biome biome : this.biomeAmounts.keySet()) {
            this.addBiomeColour(biome, totalBiomeColour);
        }
        Colour backgroundColour = this.calculateBackgroundColour();
        Colour.add(totalBiomeColour, backgroundColour, this.targetColour);
    }

    private void addBiomeColour(Biome biome, Colour totalColour) {
        float saturation = this.getBiomeDecimal(biome);
        Colour scaledColour = biome.getColour().duplicate();
        scaledColour.scale(saturation);
        Colour.add(totalColour, scaledColour, totalColour);
    }

    private Colour calculateBackgroundColour() {
        float totalBiome = Math.min(this.totalBiomeWeight, 100);
        float backgroundAmount = 1.0f - totalBiome / 100.0f;
        Colour back = this.background.duplicate();
        return back.scale(backgroundAmount);
    }

    private void updateCurrentColour() {
        Colour toTargetColour = Colour.sub(this.targetColour, this.currentColour, null);
        float distanceSquaredToTarget = toTargetColour.lengthSquared();
        if (distanceSquaredToTarget > 0.0f) {
            float changeAmount = 0.04f * (DisplayManager.getTime() - this.lastUpdateTime);
            this.moveCurrentColourToTarget(changeAmount, distanceSquaredToTarget, toTargetColour);
        }
        this.lastUpdateTime = DisplayManager.getTime();
    }

    private void moveCurrentColourToTarget(float changeAmount, float distanceSquaredToTarget, Colour toTargetColour) {
        float distanceToTarget = (float)Math.sqrt(distanceSquaredToTarget);
        if (changeAmount >= distanceToTarget) {
            this.currentColour.setColour(this.targetColour);
        } else {
            toTargetColour.scale(changeAmount / distanceToTarget);
            Colour.add(this.currentColour, toTargetColour, this.currentColour);
        }
    }
}

