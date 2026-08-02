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
import objectPools.Vec2Pool;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import toolbox.Transformation;

public class DolphinAi
implements Ai {
    private static final String DESC = GameText.getText(185);
    private static final float TARGET_SIZE = 0.2f;
    private static final float TOO_SHALLOW_START = 0.5f;
    private static final float TOO_SHALLOW_PATH = 0.25f;
    private static final float CLOSE_RANGE = 2.0f;
    private static final float RUN_RANGE = 5.0f;
    private final InformationComponent info;
    private final MovementComp mover;
    private final Transformation transform;
    private Vector3f target = new Vector3f();
    private float initialDistance = 0.0f;
    private boolean reachedTarget = true;
    private boolean run = false;
    private float currentTargetScore = 0.0f;
    private boolean anchored = false;

    public DolphinAi(InformationComponent info, MovementComp mover, Transformation transform) {
        this.info = info;
        this.mover = mover;
        this.transform = transform;
    }

    @Override
    public boolean carryOut() {
        if (this.reachedTarget) {
            this.chooseTargetPoint();
            this.reachedTarget = false;
        }
        float dis = Maths.getDistanceBetweenPoints(this.target.x, this.target.z, this.transform.getPosition().x, this.transform.getPosition().z);
        float disTravelled = this.initialDistance - dis;
        this.reachedTarget = this.mover.goToTarget(this.target, this.run && dis > 2.0f && disTravelled > 2.0f, 0.2f);
        return false;
    }

    private void chooseTargetPoint() {
        int requiredScore = 10;
        do {
            this.generateNewPath(requiredScore);
        } while (this.currentTargetScore < (float)requiredScore--);
        this.initialDistance = Maths.getDistanceBetweenPoints(this.target.x, this.target.z, this.transform.getPosition().x, this.transform.getPosition().z);
    }

    private void generateNewPath(int requiredScore) {
        Vector2f startPoint = Vec2Pool.getXZ(this.info.getRandomInRangePoint());
        Vector2f dolphinPos = Vec2Pool.getXZ(this.transform.getPosition());
        Vector2f toDolphin = Vector2f.sub(dolphinPos, startPoint, Vec2Pool.get());
        this.anchored = false;
        this.run = false;
        this.currentTargetScore = 0.0f;
        float length = toDolphin.length();
        if (length < (float)requiredScore) {
            this.currentTargetScore = -1000.0f;
            return;
        }
        int i = 0;
        while ((double)i <= Math.floor(length)) {
            Vector2f currentPoint = this.getCurrentTestPoint(toDolphin, i, length, startPoint);
            this.checkPoint(currentPoint, i, length);
            Vec2Pool.release(currentPoint);
            ++i;
        }
        Vec2Pool.release(startPoint, dolphinPos, toDolphin);
    }

    private Vector2f getCurrentTestPoint(Vector2f toDolphin, int index, float length, Vector2f startPoint) {
        if (index == 0) {
            return Vec2Pool.get(startPoint);
        }
        Vector2f toPoint = Vec2Pool.get(toDolphin);
        toPoint.scale((float)index / length);
        return Vector2f.add(startPoint, toPoint, Vec2Pool.get());
    }

    private void checkPoint(Vector2f currentPoint, int pointIndex, float length) {
        float waterDepth = GameManager.getWorld().getWaterHeight() - GameManager.getWorld().getHeightOfTerrain(currentPoint.x, currentPoint.y);
        if (!this.anchored) {
            this.testStartPoint(currentPoint, waterDepth, length - (float)pointIndex);
        } else if (waterDepth < 0.25f) {
            this.currentTargetScore = 0.0f;
            this.run = false;
            this.anchored = false;
        }
    }

    private void testStartPoint(Vector2f currentPoint, float waterDepth, float score) {
        if (waterDepth > 0.5f) {
            this.anchored = true;
            this.currentTargetScore = score;
            this.target.set(currentPoint.x, 0.0f, currentPoint.y);
            this.run = this.currentTargetScore > 5.0f;
        } else {
            this.run = false;
            this.currentTargetScore = 0.0f;
        }
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
        this.reachedTarget = true;
        this.run = false;
    }
}

