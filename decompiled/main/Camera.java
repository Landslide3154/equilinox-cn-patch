/*
 * Decompiled with CFR 0.152.
 */
package main;

import basics.DisplayManager;
import gameManaging.GameManager;
import gameManaging.GameState;
import guis.GuiMaster;
import interpolation.SmoothFloat;
import java.io.IOException;
import main.Camera2;
import main.IGameCam;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import toolbox.MousePicker;
import toolbox.MyKeyboard;
import toolbox.MyMouse;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class Camera
implements IGameCam {
    private static final float STANDARD_PITCH = 0.6f;
    private static final float STANDARD_ZOOM = 15.0f;
    private static final Vector3f STANDARD_POS = new Vector3f(50.0f, 0.0f, 50.0f);
    private static final float PITCH_AGILITY = 8.0f;
    private static final float CONTROL_DIS = 8.0f;
    private static final float ZOOM_AGILITY = 4.0f;
    private static final float ZOOM_DISTANCE_FACTOR = 0.05f;
    private static final float ROTATE_AGILITY = 6.0f;
    public static final float FIELD_OF_VIEW = 35.0f;
    private final float NEAR_PLANE = 0.15f;
    private final float FAR_PLANE = 2000.0f;
    private static final int INFLUENCE_OF_MOUSEDY = 400;
    private static final int INFLUENCE_OF_MOUSEDX = 3;
    private static final float INFLUENCE_OF_MOUSE_WHEEL = 0.6f;
    private static final float MAX_ANGLE_OF_ELEVATION = 1.5f;
    private static final float PITCH_OFFSET = 0.0f;
    private static final float MINIMUM_ZOOM = 0.0f;
    private static final float MAXIMUM_ZOOM = 300.0f;
    private static final float MAX_HORIZONTAL_CHANGE = 500.0f;
    private static final float MAX_VERTICAL_CHANGE = 5.0f;
    private static final float MIN_SCROLL = 1.7f;
    private static final float SCROLL_SPEED = 0.18f;
    private static final float SCROLL_AGILITY = 10.0f;
    private static final float FOCUS_AGILITY = 2.0f;
    private MyMouse mouse;
    private MyKeyboard keyboard;
    private Matrix4f viewMatrix = new Matrix4f();
    private Vector3f position = new Vector3f();
    private float horizontalDistanceFromPlayer;
    private float verticalDistanceFromPlayer;
    private float pitch;
    private float yaw;
    private boolean normalMode = true;
    private SmoothFloat aimDistance = new SmoothFloat(15.0f, 2.0f);
    private Vector3f target = new Vector3f(STANDARD_POS);
    private Vector3f aimingAt = new Vector3f(this.target);
    private Transformation targetEntity = null;
    private float actualDistanceFromPoint;
    private float targetZoom = this.actualDistanceFromPoint = 15.0f;
    private float targetElevation;
    private float angleOfElevation = this.targetElevation = 0.6f;
    private float targetRotationAngle;
    private float angleAroundPlayer = this.targetRotationAngle = 0.0f;
    private float zoomChange;
    private MousePicker cameraAimer;
    private boolean enabled = true;
    private static final IGameCam currentCamera = new Camera2();

    private Camera() {
        this.calculateDistances();
        this.mouse = MyMouse.getActiveMouse();
        this.keyboard = MyKeyboard.getKeyboard();
        this.cameraAimer = new MousePicker(this, true);
    }

    public static IGameCam getCamera() {
        return currentCamera;
    }

    @Override
    public void loadState(BinaryReader reader) throws Exception {
        this.target.set(reader.readVector());
        this.targetZoom = reader.readFloat();
        this.targetElevation = reader.readFloat();
        this.targetRotationAngle = reader.readFloat();
        this.setToTarget();
    }

    @Override
    public void saveState(BinaryWriter writer) throws IOException {
        writer.writeVector(this.target);
        writer.writeFloat(this.targetZoom);
        writer.writeFloat(this.targetElevation);
        writer.writeFloat(this.targetRotationAngle);
    }

    @Override
    public void resetPosition() {
        this.target.set(STANDARD_POS);
        this.targetZoom = 15.0f;
        this.targetElevation = 0.6f;
        this.targetRotationAngle = 0.0f;
        this.setToTarget();
    }

    @Override
    public void focusOn(Vector3f point) {
        float offset = (float)((double)point.y / Math.tan(this.angleOfElevation));
        double radians = Math.toRadians(this.yaw);
        float dX = (float)((double)offset * Math.sin(radians));
        float dZ = (float)((double)offset * Math.cos(radians));
        this.target.set(point.x - dX, 0.0f, point.z - dZ);
    }

    @Override
    public void enable(boolean enabled) {
        this.enabled = enabled;
    }

    private float getZoom() {
        return this.actualDistanceFromPoint;
    }

    @Override
    public float getAimDistance() {
        return this.aimDistance.get();
    }

    @Override
    public void moveCamera() {
        this.checkMouseState();
        this.moveTarget();
        if (this.enabled && !GuiMaster.isMouseInGui()) {
            this.calculateHorizontalAngle();
            this.calculateVerticalAngle();
            this.calculateZoom();
        }
        this.updateTargetPosition();
        this.updateActualZoom();
        this.updateHorizontalAngle();
        this.updatePitchAngle();
        this.calculateDistances();
        this.calculatePosition();
        Camera.createViewMatrix(this.viewMatrix, this.position, this.pitch, this.yaw);
        this.cameraAimer.update();
        this.updateAimDistance();
    }

    @Override
    public float getPitch() {
        return this.pitch;
    }

    @Override
    public float getYaw() {
        return this.yaw;
    }

    @Override
    public Vector3f getPosition() {
        return this.position;
    }

    @Override
    public float getFOV() {
        return 35.0f;
    }

    @Override
    public float getNearPlane() {
        return 0.15f;
    }

    @Override
    public float getFarPlane() {
        return 2000.0f;
    }

    @Override
    public Matrix4f getViewMatrix() {
        return this.viewMatrix;
    }

    @Override
    public void reflect(float waterHeight) {
        this.position.y -= 2.0f * (this.position.y - waterHeight);
        this.pitch = -this.pitch;
        Camera.createViewMatrix(this.viewMatrix, this.position, this.pitch, this.yaw);
    }

    public void setTargetEntity(Transformation transform) {
        this.targetEntity = transform;
        this.targetZoom = transform != null ? 8.0f : 15.0f;
    }

    private void setToTarget() {
        this.aimingAt.set(this.target);
        this.actualDistanceFromPoint = this.targetZoom;
        this.angleOfElevation = this.targetElevation;
        this.angleAroundPlayer = this.targetRotationAngle;
    }

    private void updateAimDistance() {
        Vector3f aimPoint = this.cameraAimer.getCurrentTerrainPoint();
        if (aimPoint != null) {
            this.aimDistance.setTarget(Vector3f.sub(aimPoint, this.position, null).length());
        } else {
            this.aimDistance.setTarget(this.actualDistanceFromPoint);
        }
        this.aimDistance.update(DisplayManager.getDeltaSeconds());
        this.aimDistance.instantIncrease(this.zoomChange);
    }

    private void moveTarget() {
        if (!this.normalMode) {
            return;
        }
        if (this.targetEntity != null) {
            Vector3f entityPos = this.targetEntity.getPosition();
            float height = GameManager.getWorld().getHeightOfTerrain(entityPos.x, entityPos.z);
            float altitude = entityPos.y - height;
            float value = Maths.smoothStep(0.2f, 0.85f, altitude);
            float targetY = Maths.interpolate(height, entityPos.y, value);
            this.target.set(entityPos.x, targetY, entityPos.z);
        } else {
            float speed = 0.0f;
            float sideSpeed = 0.0f;
            float actualDis = Math.max(this.actualDistanceFromPoint, 1.7f);
            if (this.mouse.isRightButtonDown()) {
                speed = (float)MyMouse.getActiveMouse().getDY() * actualDis * 0.18f;
                sideSpeed = (float)(-MyMouse.getActiveMouse().getDX()) * actualDis * 0.18f;
            }
            float zoomAmount = Math.max(this.getZoom(), 1.7f);
            if (MyKeyboard.getKeyboard().isKeyDown(205) || MyKeyboard.getKeyboard().isKeyDown(32)) {
                sideSpeed = zoomAmount * 0.18f * 2.5f;
            } else if (MyKeyboard.getKeyboard().isKeyDown(203) || MyKeyboard.getKeyboard().isKeyDown(30)) {
                sideSpeed = -zoomAmount * 0.18f * 2.5f;
            }
            if (MyKeyboard.getKeyboard().isKeyDown(200) || MyKeyboard.getKeyboard().isKeyDown(17)) {
                speed = -zoomAmount * 0.18f * 2.5f;
            } else if (MyKeyboard.getKeyboard().isKeyDown(208) || MyKeyboard.getKeyboard().isKeyDown(31)) {
                speed = zoomAmount * 0.18f * 2.5f;
            }
            float distance = speed * DisplayManager.getDeltaSeconds();
            float sideDistance = sideSpeed * DisplayManager.getDeltaSeconds();
            float dx = (float)((double)distance * Math.sin(Math.toRadians(this.yaw)));
            float dz = (float)((double)distance * Math.cos(Math.toRadians(this.yaw)));
            float sideDx = (float)((double)sideDistance * Math.sin(Math.toRadians(this.yaw + 90.0f)));
            float sideDz = (float)((double)sideDistance * Math.cos(Math.toRadians(this.yaw + 90.0f)));
            this.target.x += dx + sideDx;
            this.target.y = 0.0f;
            this.target.z += dz + sideDz;
        }
    }

    private void updateTargetPosition() {
        Vector3f offset = Vector3f.sub(this.target, this.aimingAt, null);
        offset.scale(DisplayManager.getDeltaSeconds() * 10.0f);
        Vector3f.add(this.aimingAt, offset, this.aimingAt);
    }

    private void calculatePosition() {
        this.position.x = this.aimingAt.x - (float)((double)this.horizontalDistanceFromPlayer * Math.sin(Math.toRadians(this.angleAroundPlayer)));
        this.position.z = this.aimingAt.z - (float)((double)this.horizontalDistanceFromPlayer * Math.cos(Math.toRadians(this.angleAroundPlayer)));
        this.position.y = this.verticalDistanceFromPlayer + this.aimingAt.y;
        this.yaw = 180.0f + this.angleAroundPlayer;
        this.pitch = (float)Math.toDegrees(this.angleOfElevation) - 0.0f;
    }

    private void calculateHorizontalAngle() {
        float delta = DisplayManager.getDeltaSeconds();
        if (this.mouse.isMouseWheelDown() && !this.keyboard.isKeyDown(42) || this.keyboard.isKeyDown(46)) {
            float angleChange = (float)this.mouse.getDX() / 3.0f;
            if (this.keyboard.isKeyDown(46)) {
                angleChange = DisplayManager.getDeltaSeconds() * 10.0f;
            }
            if (angleChange > 500.0f * delta) {
                angleChange = 500.0f * delta;
            } else if (angleChange < -500.0f * delta) {
                angleChange = -500.0f * delta;
            }
            this.targetRotationAngle -= angleChange;
            if (this.targetRotationAngle >= 180.0f) {
                this.targetRotationAngle -= 360.0f;
            } else if (this.targetRotationAngle <= -180.0f) {
                this.targetRotationAngle += 360.0f;
            }
        }
    }

    private void updateHorizontalAngle() {
        float offset = this.targetRotationAngle - this.angleAroundPlayer;
        if (Math.abs(offset) > 180.0f) {
            offset = offset < 0.0f ? this.targetRotationAngle + 360.0f - this.angleAroundPlayer : this.targetRotationAngle - 360.0f - this.angleAroundPlayer;
        }
        float change = offset * DisplayManager.getDeltaSeconds() * 6.0f;
        this.angleAroundPlayer += change;
        if (this.angleAroundPlayer >= 180.0f) {
            this.angleAroundPlayer -= 360.0f;
        } else if (this.angleAroundPlayer <= -180.0f) {
            this.angleAroundPlayer += 360.0f;
        }
    }

    private void calculateVerticalAngle() {
        float delta = DisplayManager.getDeltaSeconds();
        if (this.mouse.isMouseWheelDown() && !this.keyboard.isKeyDown(42)) {
            float angleChange = (float)this.mouse.getDY() / 400.0f;
            if (angleChange > 5.0f * delta) {
                angleChange = 5.0f * delta;
            } else if (angleChange < -5.0f * delta) {
                angleChange = -5.0f * delta;
            }
            this.targetElevation -= angleChange;
            if (this.targetElevation >= 1.5f) {
                this.targetElevation = 1.5f;
            } else if (this.targetElevation <= 0.0f) {
                this.targetElevation = 0.0f;
            }
        }
    }

    private void updatePitchAngle() {
        float offset = this.targetElevation - this.angleOfElevation;
        float change = offset * DisplayManager.getDeltaSeconds() * 8.0f;
        this.angleOfElevation += change;
    }

    private void calculateZoom() {
        if (!this.normalMode) {
            return;
        }
        float wheel = this.mouse.getDWheelSigned();
        float zoomLevel = wheel * 0.6f;
        float extra = this.targetZoom * wheel * 0.05f;
        zoomLevel += extra;
        if (this.keyboard.isKeyDown(47)) {
            zoomLevel = DisplayManager.getDeltaSeconds() * 1.0f;
        }
        if (zoomLevel != 0.0f) {
            float maxZoom = 300.0f;
            this.targetZoom -= zoomLevel;
            if (this.targetZoom < 0.0f) {
                this.targetZoom = 0.0f;
            } else if (this.targetZoom > maxZoom) {
                this.targetZoom = maxZoom;
            }
        }
    }

    private static void createViewMatrix(Matrix4f viewMatrix, Vector3f position, float pitch, float yaw) {
        viewMatrix.setIdentity();
        Vector3f cameraPos = new Vector3f(-position.x, -position.y, -position.z);
        Matrix4f.rotate(Maths.degreesToRadians(pitch), new Vector3f(1.0f, 0.0f, 0.0f), viewMatrix, viewMatrix);
        Matrix4f.rotate(Maths.degreesToRadians(-yaw), new Vector3f(0.0f, 1.0f, 0.0f), viewMatrix, viewMatrix);
        Matrix4f.translate(cameraPos, viewMatrix, viewMatrix);
    }

    private void updateActualZoom() {
        float offset = this.targetZoom - this.actualDistanceFromPoint;
        this.zoomChange = offset * DisplayManager.getDeltaSeconds() * 4.0f;
        this.actualDistanceFromPoint += this.zoomChange;
    }

    private void calculateDistances() {
        this.horizontalDistanceFromPlayer = (float)((double)this.actualDistanceFromPoint * Math.cos(this.angleOfElevation));
        this.verticalDistanceFromPlayer = (float)((double)this.actualDistanceFromPoint * Math.sin(this.angleOfElevation));
    }

    private void checkMouseState() {
        if (this.mouse.isMiddleClick() || this.mouse.isRightClick()) {
            GameManager.gameState.suggestState(GameState.CAMERA);
        } else if (this.mouse.isMiddleClickRelease() || this.mouse.isRightClickRelease()) {
            GameManager.gameState.endState(GameState.CAMERA);
        }
    }

    @Override
    public void setTargetEntity(Vector3f entityPos) {
    }

    @Override
    public Vector3f getListenerPosition() {
        return null;
    }

    @Override
    public MousePicker getCameraPicker() {
        return this.cameraAimer;
    }
}

