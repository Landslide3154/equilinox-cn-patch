/*
 * Decompiled with CFR 0.152.
 */
package basics;

import audio.AudioListener;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public interface CameraInterface
extends AudioListener {
    public Vector3f getPosition();

    public float getNearPlane();

    public float getFarPlane();

    public float getFOV();

    public Matrix4f getViewMatrix();

    public void reflect(float var1);

    public void moveCamera();

    public float getPitch();

    public float getYaw();

    public float getAimDistance();
}

