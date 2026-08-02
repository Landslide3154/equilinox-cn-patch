/*
 * Decompiled with CFR 0.152.
 */
package hunting;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import components.InformationComponent;
import entityBundle.EntityBundle;
import gameManaging.GameManager;
import hunting.FleeComponent;
import instances.Entity;
import java.util.Iterator;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import toolbox.Transformation;

public class SimpleFleeAi
implements Ai {
    private static final String DESC = GameText.getText(183);
    private static final float TOO_SHALLOW = -0.5f;
    private static final float TOO_SHALLOW_FLEE = -0.2f;
    private static final float TARGET_SIZE = 0.2f;
    private static final float EDGE = 1.5f;
    private static final float PRIORITY = 15.0f;
    private Transformation transform;
    private MovementComp mover;
    private FleeComponent fleeComp;
    private InformationComponent info;
    private Vector3f dangerPosition = new Vector3f();
    private boolean correcting = false;
    private Vector3f target;
    private Entity hidingSpot = null;

    public SimpleFleeAi(FleeComponent fleeComp, MovementComp mover, Transformation transform, InformationComponent info) {
        this.info = info;
        this.mover = mover;
        this.transform = transform;
        this.fleeComp = fleeComp;
    }

    @Override
    public boolean carryOut() {
        this.dangerPosition.set(0.0f, 0.0f, 0.0f);
        Iterator<Entity> iterator = this.fleeComp.getPredatorList().iterator();
        while (iterator.hasNext()) {
            this.testPredatorPosition(iterator);
        }
        if (this.fleeComp.getPredatorList().isEmpty()) {
            this.hidingSpot = null;
            this.fleeComp.setHiding(false);
            this.correcting = false;
            return true;
        }
        this.dangerPosition.scale(1.0f / (float)this.fleeComp.getPredatorList().size());
        this.flee();
        return false;
    }

    private void flee() {
        this.testAltitude();
        if (this.target != null && this.correcting) {
            boolean reached = this.mover.goToTarget(this.target, true, 0.2f);
            if (reached) {
                this.correcting = false;
                this.target = null;
            }
        } else if (this.checkHidingSpotAvailable()) {
            boolean reached;
            if (!this.fleeComp.isInvulnerable() && (reached = this.mover.goToTarget(this.hidingSpot.getTransform().getPosition(), true, 0.2f))) {
                this.fleeComp.setHiding(true);
            }
        } else {
            this.searchForHidingSpot();
            this.mover.goFromTarget(this.dangerPosition, true);
        }
    }

    private void searchForHidingSpot() {
        if (this.fleeComp.fleeCompBlueprint.hidingSpot == null) {
            return;
        }
        Vector3f pos = this.transform.getPosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(this.fleeComp.fleeCompBlueprint.hidingSpot, 1, pos.x, pos.z);
        if (!bundle.isEmpty()) {
            this.hidingSpot = bundle.getRandomEntity();
        }
    }

    private boolean checkHidingSpotAvailable() {
        if (this.hidingSpot == null) {
            return false;
        }
        if (this.hidingSpot.isDead() || this.hidingSpot.isGrabbed()) {
            this.hidingSpot = null;
            this.fleeComp.setHiding(false);
            return false;
        }
        return true;
    }

    private void testAltitude() {
        if (this.correcting) {
            return;
        }
        if (!this.isAcceptableHeight(this.transform.getTerrainHeight(), -0.5f)) {
            Vector3f point = this.info.getRandomInRangePoint();
            float height = GameManager.getWorld().getHeightOfTerrain(point.x, point.z);
            if (this.isAcceptableHeight(height, -0.2f)) {
                this.correcting = true;
                this.target = point;
            }
        }
    }

    private boolean isAcceptableHeight(float terrainHeight, float tooShallow) {
        if (!this.fleeComp.fleeCompBlueprint.canGoOnLand) {
            return terrainHeight - GameManager.getWorld().getWaterHeight() < tooShallow;
        }
        if (!this.fleeComp.fleeCompBlueprint.canSwim) {
            return terrainHeight - GameManager.getWorld().getWaterHeight() > 0.0f;
        }
        return true;
    }

    private void testPredatorPosition(Iterator<Entity> iterator) {
        Entity predator = iterator.next();
        if (this.outOfRange(predator) || predator.getTransform() == this.transform) {
            iterator.remove();
        } else {
            Vector3f.add(this.dangerPosition, predator.getTransform().getPosition(), this.dangerPosition);
        }
        this.setEdgeDangerPosition();
    }

    private void setEdgeDangerPosition() {
        Vector3f pos = this.transform.getPosition();
        float worldSize = GameManager.getWorld().getSize();
        boolean xEdge = false;
        boolean zEdge = false;
        if (pos.x < 1.5f || pos.x > worldSize - 1.5f) {
            xEdge = true;
        }
        if (pos.z < 1.5f || pos.z > worldSize - 1.5f) {
            zEdge = true;
        }
        if (xEdge && zEdge) {
            this.dangerPosition.x = pos.x > worldSize * 0.5f ? worldSize : 0.0f;
            this.dangerPosition.z = pos.z > worldSize * 0.5f ? worldSize : 0.0f;
        } else if (xEdge) {
            this.dangerPosition.x = pos.x;
            this.dangerPosition.z = this.dangerPosition.z > pos.z ? pos.z + 1.0f : pos.z - 1.0f;
        } else if (zEdge) {
            this.dangerPosition.x = this.dangerPosition.x > pos.x ? pos.x + 1.0f : pos.x - 1.0f;
            this.dangerPosition.z = pos.z;
        }
    }

    private boolean outOfRange(Entity predator) {
        if (predator.isDead() || predator.isGrabbed()) {
            return true;
        }
        Vector3f predatorPos = predator.getTransform().getPosition();
        Vector3f.sub(predatorPos, this.transform.getPosition(), Maths.VEC3);
        return Maths.VEC3.lengthSquared() > this.fleeComp.fleeCompBlueprint.safeRangeSquared;
    }

    @Override
    public float getPriority() {
        return 15.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.fleeComp;
    }

    @Override
    public void interrupt() {
        this.target = null;
        this.correcting = false;
        this.fleeComp.setHiding(false);
        this.hidingSpot = null;
    }

    @Override
    public String getDescription() {
        return DESC;
    }
}

