/*
 * Decompiled with CFR 0.152.
 */
package shellHide;

import aiComponent.Ai;
import aiComponent.AiProvidingComponent;
import instances.Entity;
import java.util.Iterator;
import languages.GameText;
import movementUtils.ProjectileBounce;
import org.lwjgl.util.vector.Vector3f;
import shellHide.ShellHideComponent;
import toolbox.Maths;
import toolbox.Transformation;

public class HidingAi
implements Ai {
    private static final float BOUNCINESS = 0.5f;
    private static final float BOUNCE_POWER = 1.5f;
    private static final int BOUNCE_COUNT = 3;
    private static final String DESC = GameText.getText(643);
    private final Transformation transform;
    private final ShellHideComponent shellHideComp;
    private ProjectileBounce bouncer;
    private boolean unhide = false;

    public HidingAi(ShellHideComponent component, Transformation transform) {
        this.shellHideComp = component;
        this.transform = transform;
        this.initBounce();
    }

    public void unhide() {
        this.unhide = true;
    }

    @Override
    public boolean carryOut() {
        this.updatePredatorList();
        if (this.shellHideComp.getPredatorList().isEmpty()) {
            this.shellHideComp.unhide();
        }
        if (this.bouncer == null) {
            if (this.unhide) {
                this.initBounce();
            } else {
                return false;
            }
        }
        if (this.bouncer.update()) {
            if (this.unhide) {
                return true;
            }
            this.bouncer = null;
        }
        return false;
    }

    private void updatePredatorList() {
        Iterator<Entity> iterator = this.shellHideComp.getPredatorList().iterator();
        while (iterator.hasNext()) {
            this.testPredatorPosition(iterator);
        }
    }

    @Override
    public float getPriority() {
        return 10000.0f;
    }

    @Override
    public AiProvidingComponent getComponent() {
        return this.shellHideComp;
    }

    @Override
    public void interrupt() {
    }

    @Override
    public String getDescription() {
        return DESC;
    }

    private void initBounce() {
        this.bouncer = new ProjectileBounce(this.transform, new Vector3f(0.0f, 1.5f, 0.0f), 3, 0.5f);
    }

    private void testPredatorPosition(Iterator<Entity> iterator) {
        Entity predator = iterator.next();
        if (this.outOfRange(predator)) {
            iterator.remove();
        }
    }

    private boolean outOfRange(Entity predator) {
        if (predator.isDead() || predator.isGrabbed()) {
            return true;
        }
        Vector3f predatorPos = predator.getTransform().getPosition();
        Vector3f.sub(predatorPos, this.transform.getPosition(), Maths.VEC3);
        return Maths.VEC3.lengthSquared() > this.shellHideComp.getSafeRangeSquared();
    }
}

