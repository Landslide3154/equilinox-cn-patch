/*
 * Decompiled with CFR 0.152.
 */
package ai;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import components.InformationComponent;
import gameManaging.GameManager;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Transformation;

public class SwimAi
implements Ai {
    private static final String DESC = GameText.getText(185);
    private static final float TARGET_SIZE = 0.15f;
    private static final float TOO_SHALLOW = 0.5f;
    private static final float SUPER_SHALLOW = 0.15f;
    private final InformationComponent info;
    private final MovementComp mover;
    private final Transformation transform;
    private boolean correcting = false;
    private Vector3f target;
    private Vector3f safePoint = null;

    public SwimAi(InformationComponent info, MovementComp mover, Transformation transform) {
        this.info = info;
        this.mover = mover;
        this.transform = transform;
    }

    @Override
    public boolean carryOut() {
        if (this.target == null) {
            this.target = this.info.getRandomInRangePoint();
        }
        this.testWaterDepth();
        boolean reached = this.mover.goToTarget(this.target, false, 0.15f);
        if (reached) {
            this.correcting = false;
            this.target = null;
        }
        return false;
    }

    private void testWaterDepth() {
        if (this.correcting) {
            return;
        }
        float waterDepth = GameManager.getWorld().getWaterHeight() - this.transform.getTerrainHeight();
        if (waterDepth < 0.5f) {
            int i = 0;
            while (i < 2) {
                if (this.checkPoint(0.5f)) {
                    return;
                }
                ++i;
            }
            if (waterDepth < 0.15f) {
                if (this.safePoint != null) {
                    this.correcting = true;
                    this.target = this.safePoint;
                    return;
                }
                i = 0;
                while (i < 5) {
                    if (this.checkPoint(0.15f)) {
                        return;
                    }
                    ++i;
                }
            }
        }
    }

    private boolean checkPoint(float suiatbleHeight) {
        Vector3f point = this.info.getRandomInRangePoint();
        float height = GameManager.getWorld().getHeightOfTerrain(point.x, point.z);
        if (GameManager.getWorld().getWaterHeight() - height > suiatbleHeight) {
            this.correcting = true;
            this.target = point;
            this.safePoint = point;
            return true;
        }
        return false;
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    @Override
    public float getPriority() {
        return 0.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return null;
    }

    @Override
    public void interrupt() {
        this.target = null;
        this.correcting = false;
        this.safePoint = null;
    }
}

