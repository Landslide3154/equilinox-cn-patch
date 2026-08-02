/*
 * Decompiled with CFR 0.152.
 */
package environment;

import entityInfoGui.EntityInfoGui;
import entityInfoGui.PopUpInfoGui;
import entityInfoGui.ProgressInfo;
import environment.EnviroFactor;
import environment.EnviroPopUp;
import gameManaging.GameManager;
import guis.GuiComponent;
import java.io.IOException;
import java.util.List;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import session.GameMode;
import terrains.TerrainVertex;
import utils.BinaryWriter;

public class EnviroComponent {
    private static final String ENVIRO = GameText.getText(876);
    public static final float STANDARD = 0.8f;
    private final Vector3f basePosition;
    private final int range;
    private final List<EnviroFactor> enviroFactors;
    private float environmentSatisfaction;
    private float boost = 1.0f;

    public EnviroComponent(List<EnviroFactor> factors, float environmentSatisfaction, Vector3f basePosition, int range) {
        this.enviroFactors = factors;
        this.range = range;
        this.environmentSatisfaction = environmentSatisfaction;
        this.basePosition = basePosition;
        this.calcBoost();
    }

    public EnviroComponent(List<EnviroFactor> factors, Vector3f basePosition, int range) {
        this.enviroFactors = factors;
        this.range = range;
        this.basePosition = basePosition;
        this.recalculate();
    }

    public void export(BinaryWriter writer) throws IOException {
        writer.writeFloat(this.environmentSatisfaction);
    }

    public List<EnviroFactor> getFactors() {
        return this.enviroFactors;
    }

    public float getEnvironmentSatisfaction() {
        if (GameManager.getGameMode() == GameMode.BUILD) {
            return 1.0f;
        }
        return this.environmentSatisfaction;
    }

    public float getBoost() {
        return this.boost;
    }

    public float recalculate() {
        this.environmentSatisfaction = 1.0f;
        TerrainVertex vertex = GameManager.getWorld().getTerrainVertex(this.basePosition.x, this.basePosition.z);
        if (vertex == null) {
            return 0.0f;
        }
        for (EnviroFactor factor : this.enviroFactors) {
            float multiplier = factor.recalculate(this.basePosition, vertex, this.range);
            float value = 1.0f - factor.getInfluence() + multiplier * factor.getInfluence();
            this.environmentSatisfaction *= value;
        }
        this.calcBoost();
        return this.environmentSatisfaction;
    }

    private void calcBoost() {
        this.boost = 0.35f + 0.65f * this.environmentSatisfaction / 0.8f;
    }

    public void getStatusInfo(List<PopUpInfoGui> info) {
        info.add(new ProgressInfo(ENVIRO, EntityInfoGui.FONT_SIZE, false){

            @Override
            protected GuiComponent addMouseOver() {
                return new EnviroPopUp(EnviroComponent.this);
            }

            @Override
            protected float getValue() {
                return EnviroComponent.this.environmentSatisfaction;
            }
        });
    }
}

