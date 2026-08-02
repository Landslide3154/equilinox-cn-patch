/*
 * Decompiled with CFR 0.152.
 */
package baseMovement;

import baseMovement.BaseMovement;
import baseMovement.MovementComp;
import gameManaging.GameManager;
import objectPools.Vec2Pool;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import toolbox.Transformation;

public class MoveUtils {
    public static boolean goToTarget(MovementComp movement, Vector3f target, boolean run, float radius) {
        Vector2f thisPos;
        Vector3f pos = movement.getTransform().getPosition();
        Vector2f targetPos = Vec2Pool.get(target.x, target.z);
        Vector2f toTarget = Vector2f.sub(targetPos, thisPos = Vec2Pool.get(pos.x, pos.z), Vec2Pool.get());
        float disSquared = toTarget.lengthSquared();
        if (disSquared < radius * radius) {
            return true;
        }
        float aimRotation = Maths.calculateVectorRotationY(toTarget);
        Vec2Pool.release(targetPos, thisPos, toTarget);
        movement.turn(aimRotation);
        if (disSquared > 1.0f || Math.abs(aimRotation - movement.getHeadingRotation()) < 1.0f) {
            if (run) {
                movement.run();
            } else {
                movement.walkForward();
            }
        }
        return false;
    }

    public static boolean goToTargetAndFace(MovementComp movement, Vector3f target, boolean run, float radius) {
        Vector3f pos = movement.getTransform().getPosition();
        Vector2f toTarget = Vector2f.sub(new Vector2f(target.x, target.z), new Vector2f(pos.x, pos.z), null);
        float disSquared = toTarget.lengthSquared();
        float aimRotation = 0.0f;
        try {
            aimRotation = Maths.calculateVectorRotationY(toTarget);
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println();
            System.err.println("ERROR WITH GOING TO TARGET AND FACING");
        }
        movement.turn(aimRotation);
        if (disSquared < radius * radius) {
            return Math.abs(aimRotation - movement.getHeadingRotation()) < 1.0f;
        }
        if (disSquared > 1.0f || Math.abs(aimRotation - movement.getHeadingRotation()) < 1.0f) {
            if (run) {
                movement.run();
            } else {
                movement.walkForward();
            }
        }
        return false;
    }

    public static Vector2f getVectorToTarget(MovementComp movement, float targetX, float targetZ) {
        Vector3f pos = movement.getTransform().getPosition();
        Vector2f targetPos = Vec2Pool.get(targetX, targetZ);
        Vector2f thisPos = Vec2Pool.get(pos.x, pos.z);
        Vector2f vec = Vector2f.sub(targetPos, thisPos, null);
        Vec2Pool.release(thisPos, targetPos);
        return vec;
    }

    public static float goInDirection(MovementComp movement, Vector2f direction) {
        float angle = Maths.calculateVectorRotationY(direction);
        movement.turn(angle);
        movement.walkForward();
        return Math.abs(angle - movement.getHeadingRotation());
    }

    public static void goFromTarget(MovementComp movement, Vector3f target, boolean run) {
        Vector3f pos = movement.getTransform().getPosition();
        Vector2f toTarget = Vector2f.sub(new Vector2f(pos.x, pos.z), new Vector2f(target.x, target.z), null);
        movement.turn(Maths.calculateVectorRotationY(toTarget));
        if (run) {
            movement.run();
        } else {
            movement.walkForward();
        }
    }

    public static void applyVelocityWithGravity(Vector3f velocity, Transformation transform, float delta) {
        MoveUtils.applyVelocity(velocity, transform, delta);
        velocity.y -= 10.0f * GameManager.getGameSeconds();
    }

    public static void applyVelocityWithGravity(Vector3f velocity, Transformation transform, float gravityFactor, float delta) {
        velocity.y -= 10.0f * GameManager.getGameSeconds() * gravityFactor;
        MoveUtils.applyVelocity(velocity, transform, delta);
    }

    public static void applyVelocity(Vector3f velocity, Transformation transform, float delta) {
        Vector3f temp = Maths.VEC3.set(velocity);
        temp.scale(delta);
        transform.increasePosition(temp);
    }

    public static void clampToWorld(Vector3f pos) {
        float worldSize = GameManager.getWorld().getSize();
        if (pos.x < 1.0f) {
            pos.x = 1.0f;
        } else if (pos.x > worldSize - 1.0f) {
            pos.x = worldSize - 1.0f;
        }
        if (pos.z < 1.0f) {
            pos.z = 1.0f;
        } else if (pos.z > worldSize - 1.0f) {
            pos.z = worldSize - 1.0f;
        }
    }

    public static boolean updateRotation(MovementComp movement, float targetRotation, float rotSpeed) {
        float currentRot = movement.getTransform().getRotY();
        float toTarget = targetRotation - currentRot;
        if (Math.abs(toTarget) > 180.0f) {
            toTarget -= Math.signum(toTarget) * 360.0f;
        }
        float change = GameManager.getGameSeconds() * rotSpeed;
        if (Math.abs(toTarget) <= change) {
            movement.getTransform().setYRotation(targetRotation);
            return true;
        }
        float newRot = currentRot += Math.signum(toTarget) * change;
        if (newRot < 0.0f) {
            newRot += 360.0f;
        }
        movement.getTransform().setYRotation(newRot % 360.0f);
        return false;
    }

    public static boolean updateBaseRotation(BaseMovement movement, float targetRotation, float rotSpeed) {
        float currentRot = movement.getActualRotY();
        float toTarget = targetRotation - currentRot;
        if (Math.abs(toTarget) > 180.0f) {
            toTarget -= Math.signum(toTarget) * 360.0f;
        }
        float change = GameManager.getGameSeconds() * rotSpeed;
        if (Math.abs(toTarget) <= change) {
            movement.setActualRotY(targetRotation);
            return true;
        }
        float newRot = currentRot += Math.signum(toTarget) * change;
        if (newRot < 0.0f) {
            newRot += 360.0f;
        }
        movement.setActualRotY(newRot % 360.0f);
        return false;
    }

    public static boolean updateRotation(MovementComp movement, float currentRot, float targetRotation, float rotSpeed, float extraRot) {
        float toTarget = targetRotation - currentRot;
        if (Math.abs(toTarget) > 180.0f) {
            toTarget -= Math.signum(toTarget) * 360.0f;
        }
        float change = GameManager.getGameSeconds() * rotSpeed;
        if (Math.abs(toTarget) <= change) {
            movement.getTransform().setYRotation(targetRotation + extraRot);
            return true;
        }
        float newRot = currentRot += Math.signum(toTarget) * change + extraRot;
        if (newRot < 0.0f) {
            newRot += 360.0f;
        }
        movement.getTransform().setYRotation(newRot % 360.0f);
        return false;
    }
}

