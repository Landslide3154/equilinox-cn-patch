/*
 * Decompiled with CFR 0.152.
 */
package baseMovement;

import org.lwjgl.util.vector.Vector3f;
import toolbox.Transformation;

public interface MovementComp {
    public void walkForward();

    public void run();

    public void turn(float var1);

    public boolean goToTargetAndFace(Vector3f var1, boolean var2, float var3);

    public void increaseTurn(float var1);

    public boolean goToTarget(Vector3f var1, boolean var2, float var3);

    public boolean land(Vector3f var1);

    public void goFromTarget(Vector3f var1, boolean var2);

    public Transformation getTransform();

    public boolean normalize();

    public boolean isSwimming();

    public void block(boolean var1);

    public float getHeadingRotation();
}

