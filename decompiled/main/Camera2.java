/*
 * Decompiled with CFR 0.152.
 */
package main;

import basics.DisplayManager;
import gameManaging.GameManager;
import gameManaging.GameState;
import guis.GuiMaster;
import interpolation.SmoothFloat;
import interpolation.SmoothVector;
import java.io.IOException;
import main.IGameCam;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import toolbox.MousePicker;
import toolbox.MyKeyboard;
import toolbox.MyMouse;
import utils.BinaryReader;
import utils.BinaryWriter;

public class Camera2
implements IGameCam {
    private static final Vector3f STANDARD_POS = new Vector3f(101.0f, 7.76f, 1.0f);
    private static final float STANDARD_PITCH = 45.0f;
    private static final float MIN_ZOOM = 1.0f;
    private static final float MAX_ZOOM = 140.0f;
    private static final float HEIGHT_OFFSET = 0.4f;
    private static final float MIN_PITCH = 5.0f;
    private static final float MAX_PITCH = 85.0f;
    private static final float TO_TARGET_AGILITY = 8.0f;
    private static final float LOW_ALTITUDE = 2.3f;
    private static final float HIGH_ALTITUDE = 2.9f;
    private static final float ENTITY_ZOOM = 5.0f;
    private static final float RELEASE_ZOOM = 4.0f;
    private static final float ZOOM_DISTANCE_FACTOR = 0.07f;
    private static final float MAX_YAW_CHANGE = 12.0f;
    private static final float MAX_PITCH_CHANGE = 7.0f;
    private static final float SCROLL_SPEED = 0.25f;
    private static final float KEY_SCROLL_FACTOR = 4.0f;
    private static final float ROTATION_FACTOR = 33.33333f;
    private static final float ZOOM_SPEED = 0.4f;
    private static final float LISTENER_OFFSET = 4.0f;
    public static final float FIELD_OF_VIEW = 35.0f;
    private final float NEAR_PLANE = 0.15f;
    private final float FAR_PLANE = 2000.0f;
    private SmoothVector position = new SmoothVector(new Vector3f(), 12.0f);
    private SmoothFloat yaw = new SmoothFloat(0.0f, 12.0f);
    private SmoothFloat pitch = new SmoothFloat(0.0f, 10.0f);
    private Vector3f target = new Vector3f();
    private SmoothFloat distanceFromTarget = new SmoothFloat(0.0f, 8.0f);
    private Vector3f entityPos = null;
    private Vector3f followTarget = new Vector3f();
    private boolean oneOffTarget = false;
    private MousePicker cameraAimer;
    private boolean doingStartAnimation = false;
    private boolean animationStarted = false;
    private float animProgress = 0.0f;
    private boolean updatePosition = false;
    private boolean newStart = true;
    private boolean targetMoved = false;
    private boolean zoomOccurred = false;
    private boolean enabled = true;
    private float distanceBeforeScroll = 5.0f;
    private boolean reflected = false;
    private Matrix4f viewMatrix = new Matrix4f();
    private Vector3f reflectedPosition = new Vector3f();
    private Matrix4f reflectedViewMatrix = new Matrix4f();
    private static final float ANIM_TIME = 4.0f;

    public Camera2() {
        this.cameraAimer = new MousePicker(this, true, true);
    }

    @Override
    public void moveCamera() {
        this.updatePosition = false;
        if (MyKeyboard.getKeyboard().keyDownEventOccurred(19) && this.entityPos == null) {
            if (Float.isNaN(this.position.get().x) || Float.isNaN(this.position.get().y) || Float.isNaN(this.position.get().z)) {
                this.target = new Vector3f(50.0f, 5.0f, 50.0f);
                this.position = new SmoothVector(new Vector3f(), 12.0f);
                this.distanceFromTarget = new SmoothFloat(0.0f, 8.0f);
                this.pitch = new SmoothFloat(0.0f, 10.0f);
                this.yaw = new SmoothFloat(0.0f, 12.0f);
                this.recalculatePosition();
            } else {
                this.focusOn(new Vector3f(50.0f, 0.0f, 50.0f));
            }
        }
        this.checkMouseState();
        this.checkZoomInput();
        this.checkRotationInput();
        if (this.entityPos != null) {
            this.followEntity();
        }
        if (this.updatePosition) {
            this.recalculatePosition();
        }
        this.cameraAimer.update();
        if (this.entityPos == null || this.oneOffTarget) {
            this.translateCamera();
            if (this.targetMoved || !this.position.reached()) {
                if (this.oneOffTarget) {
                    this.oneOffTarget = false;
                    this.entityPos = null;
                }
                this.updateTargetPoint();
            }
        }
        if (this.oneOffTarget && (MyMouse.getActiveMouse().isLeftClick() || MyMouse.getActiveMouse().isRightClick())) {
            this.oneOffTarget = false;
            this.entityPos = null;
        }
        if (!this.targetMoved || this.zoomOccurred) {
            this.distanceBeforeScroll = this.distanceFromTarget.get();
        }
        if (this.targetMoved || this.updatePosition) {
            this.newStart = false;
            this.doingStartAnimation = false;
        }
        if (this.newStart && !this.doingStartAnimation) {
            this.updateTargetPoint();
        }
        if (this.doingStartAnimation && !this.animationStarted && GameManager.sessionManager.hasWorldReady() && !GameManager.sessionManager.isLoading()) {
            this.animationStarted = true;
            this.updateTargetPoint();
        }
        this.recalculateViewMatrix();
    }

    @Override
    public Vector3f getPosition() {
        if (this.reflected) {
            return this.reflectedPosition;
        }
        return this.position.get();
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
    public float getFOV() {
        return 35.0f;
    }

    @Override
    public Matrix4f getViewMatrix() {
        if (this.reflected) {
            return this.reflectedViewMatrix;
        }
        return this.viewMatrix;
    }

    @Override
    public void reflect(float waterHeight) {
        boolean bl = this.reflected = !this.reflected;
        if (this.reflected) {
            this.reflectedPosition.set(this.position.get());
            this.reflectedPosition.y -= 2.0f * (this.reflectedPosition.y - waterHeight);
            this.recalculateReflectedViewMatrix();
        }
    }

    @Override
    public float getPitch() {
        if (this.reflected) {
            return -this.pitch.get();
        }
        return this.pitch.get();
    }

    @Override
    public float getYaw() {
        return this.yaw.get();
    }

    @Override
    public float getAimDistance() {
        return this.distanceFromTarget.get();
    }

    @Override
    public void loadState(BinaryReader reader) throws Exception {
        Vector3f pos = reader.readVector();
        this.fixNaN(pos, 50.0f, 6.0f, 50.0f);
        this.position.force(pos);
        this.yaw.force(reader.readFloat());
        this.pitch.force(reader.readFloat());
        this.newStart = true;
    }

    private void fixNaN(Vector3f vec, float x, float y, float z) {
        if (Float.isNaN(vec.x)) {
            vec.x = x;
        }
        if (Float.isNaN(vec.y)) {
            vec.y = y;
        }
        if (Float.isNaN(vec.z)) {
            vec.z = z;
        }
    }

    @Override
    public void saveState(BinaryWriter writer) throws IOException {
        Vector3f pos = this.position.get();
        this.fixNaN(pos, 50.0f, 6.0f, 50.0f);
        writer.writeVector(pos);
        writer.writeFloat(this.yaw.get() % 360.0f);
        writer.writeFloat(this.pitch.get());
    }

    @Override
    public void resetPosition() {
        this.position.force(STANDARD_POS);
        this.pitch.force(7.2f);
        this.yaw.force(135.0f);
        this.doingStartAnimation = true;
        this.animationStarted = false;
        this.animProgress = 0.0f;
        this.newStart = true;
        this.recalculateViewMatrix();
        this.cameraAimer.update();
    }

    @Override
    public void focusOn(Vector3f point) {
        this.entityPos = point;
        this.distanceFromTarget.setTarget(5.0f);
        this.oneOffTarget = true;
        this.position.cancelTarget();
    }

    @Override
    public void enable(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void setTargetEntity(Vector3f targetPosition) {
        this.entityPos = targetPosition;
        if (targetPosition != null) {
            this.distanceFromTarget.setTarget(5.0f);
            this.oneOffTarget = false;
        } else {
            this.distanceFromTarget.increaseTarget(4.0f);
        }
    }

    private void followEntity() {
        float height = GameManager.getWorld().getHeightOfTerrain(this.entityPos.x, this.entityPos.z);
        float altitude = this.entityPos.y - height;
        float value = Maths.smoothStep(2.3f, 2.9f, altitude);
        float targetY = Maths.interpolate(height, this.entityPos.y, value);
        this.followTarget.set(this.entityPos.x, targetY, this.entityPos.z);
        this.updateTargetPosition();
        this.updatePosition = true;
    }

    private void translateCamera() {
        this.targetMoved = false;
        Vector2f speeds = this.getSpeedFromInputs();
        if (this.targetMoved) {
            float forwardDistance = speeds.x * DisplayManager.getDeltaSeconds();
            float sideDistance = speeds.y * DisplayManager.getDeltaSeconds();
            float dx = (float)((double)forwardDistance * Math.sin(Math.toRadians(this.yaw.get())));
            float dz = (float)((double)forwardDistance * Math.cos(Math.toRadians(this.yaw.get())));
            float sideDx = (float)((double)sideDistance * Math.sin(Math.toRadians(this.yaw.get() + 90.0f)));
            float sideDz = (float)((double)sideDistance * Math.cos(Math.toRadians(this.yaw.get() + 90.0f)));
            this.position.increaseTarget(dx + sideDx, 0.0f, dz + sideDz);
            Vector3f posTarget = this.position.getTarget();
            float height = GameManager.getWorld().getHeightOfTerrain(posTarget.x, posTarget.z);
            if (posTarget.y < height + 0.4f) {
                posTarget.y = height + 0.4f;
            }
        }
        this.position.update(DisplayManager.getDeltaSeconds());
    }

    private void updateTargetPoint() {
        Vector3f terrainPoint = this.cameraAimer.getCurrentTerrainPoint();
        if (terrainPoint == null && GameManager.sessionManager.hasWorldReady() && !GameManager.sessionManager.isLoading()) {
            terrainPoint = this.cameraAimer.getIntersectionWithPlane(0.0f);
        }
        if (terrainPoint != null) {
            this.target.set(terrainPoint);
            this.distanceFromTarget.forceOnlyActualValue(Vector3f.sub(this.target, this.position.get(), null).length());
        }
    }

    private void updateTargetPosition() {
        Vector3f offset = Vector3f.sub(this.followTarget, this.target, null);
        offset.scale(DisplayManager.getDeltaSeconds() * 8.0f);
        Vector3f.add(this.target, offset, this.target);
    }

    private void checkZoomInput() {
        this.zoomOccurred = false;
        MyMouse mouse = MyMouse.getActiveMouse();
        float wheel = 0.0f;
        if (this.inputsActive() && (wheel = mouse.getDWheelSigned()) != 0.0f) {
            this.animationStarted = false;
        }
        if (MyKeyboard.getKeyboard().isKeyDown(45)) {
            wheel = 0.08f;
        } else if (MyKeyboard.getKeyboard().isKeyDown(44)) {
            wheel = -0.08f;
        }
        if (this.animationStarted) {
            this.animProgress += DisplayManager.getDeltaSeconds();
            float factor = 1.0f - this.animProgress / 4.0f;
            if (GameManager.getGameState() == GameState.GAME_MENU) {
                this.animProgress = 0.0f;
            }
            wheel = 0.008f * factor;
            if (this.distanceFromTarget.get() < 15.0f || this.animProgress >= 4.0f) {
                this.animationStarted = false;
            }
        }
        float zoomLevel = wheel * 0.4f;
        float extra = this.distanceFromTarget.get() * wheel * 0.07f;
        if ((zoomLevel += extra) != 0.0f) {
            this.zoomOccurred = true;
            this.updatePosition = true;
            this.distanceFromTarget.increaseTarget(-zoomLevel);
            this.distanceFromTarget.clampTarget(1.0f, 140.0f);
        }
        this.distanceFromTarget.update(DisplayManager.getDeltaSeconds());
        if (!this.distanceFromTarget.reached()) {
            this.updatePosition = true;
        }
    }

    private void checkRotationInput() {
        MyMouse mouse = MyMouse.getActiveMouse();
        if ((mouse.isMouseWheelDown() || MyKeyboard.getKeyboard().isKeyDown(42)) && !this.targetMoved) {
            this.updatePosition = true;
            this.animationStarted = false;
            this.position.cancelTarget();
            float pitchChange = 0.0f;
            if (this.inputsActive()) {
                pitchChange = -((float)mouse.getDY()) * DisplayManager.getDeltaSeconds() * 33.33333f;
            }
            pitchChange = Maths.clamp(pitchChange, -7.0f, 7.0f);
            this.pitch.increaseTarget(pitchChange);
            this.pitch.clampTarget(5.0f, 85.0f);
            float yawChange = 0.0f;
            if (this.inputsActive()) {
                yawChange = -((float)mouse.getDX()) * DisplayManager.getDeltaSeconds() * 33.33333f;
            }
            yawChange = Maths.clamp(yawChange, -12.0f, 12.0f);
            this.yaw.increaseTarget(yawChange);
        } else if (this.targetMoved) {
            this.yaw.cancelTarget();
            this.pitch.cancelTarget();
        }
        this.yaw.update(DisplayManager.getDeltaSeconds());
        this.pitch.update(DisplayManager.getDeltaSeconds());
        if (!this.yaw.reached() || !this.pitch.reached()) {
            this.updatePosition = true;
        }
    }

    private void recalculatePosition() {
        Vector3f offset = new Vector3f();
        float pitchRadians = (float)Math.toRadians(this.pitch.get());
        offset.y = (float)((double)this.distanceFromTarget.get() * Math.sin(pitchRadians));
        float horizDistance = (float)((double)this.distanceFromTarget.get() * Math.cos(pitchRadians));
        float yawRadians = (float)Math.toRadians(this.yaw.get());
        offset.x = (float)((double)horizDistance * Math.sin(yawRadians));
        offset.z = (float)((double)horizDistance * Math.cos(yawRadians));
        Vector3f pos = Vector3f.add(this.target, offset, null);
        float height = GameManager.getWorld().getHeightOfTerrain(pos.x, pos.z);
        if (pos.y < (height = Math.max(height, GameManager.getWorld().getWaterHeight())) + 0.4f) {
            pos.y = height + 0.4f;
        }
        this.position.forceOnlyActualValue(pos);
    }

    private Vector2f getSpeedFromInputs() {
        Vector2f speeds = new Vector2f();
        MyKeyboard input = MyKeyboard.getKeyboard();
        if (MyMouse.getActiveMouse().isRightButtonDown() && !input.isKeyDown(42) || input.isKeyDown(29) && this.inputsActive()) {
            speeds.x = (float)MyMouse.getActiveMouse().getDY() * 0.25f;
            speeds.y = (float)(-MyMouse.getActiveMouse().getDX()) * 0.25f;
            this.targetMoved = true;
            this.animationStarted = false;
        }
        if (input.isKeyDown(205) || input.isKeyDown(32)) {
            this.targetMoved = true;
            this.animationStarted = false;
            speeds.y = 1.0f;
        } else if (input.isKeyDown(203) || input.isKeyDown(30)) {
            speeds.y = -1.0f;
            this.targetMoved = true;
            this.animationStarted = false;
        }
        if (input.isKeyDown(200) || input.isKeyDown(17)) {
            this.targetMoved = true;
            this.animationStarted = false;
            speeds.x = -1.0f;
        } else if (input.isKeyDown(208) || input.isKeyDown(31)) {
            this.targetMoved = true;
            this.animationStarted = false;
            speeds.x = 1.0f;
        }
        float actualDis = 0.5f + this.distanceBeforeScroll / 2.0f;
        speeds.x *= actualDis;
        speeds.y *= actualDis;
        return speeds;
    }

    private void checkMouseState() {
        MyMouse mouse = MyMouse.getActiveMouse();
        if (mouse.isMiddleClick() || mouse.isRightClick() || MyKeyboard.getKeyboard().keyDownEventOccurred(42) || MyKeyboard.getKeyboard().keyDownEventOccurred(29)) {
            GameManager.gameState.suggestState(GameState.CAMERA);
        } else if ((mouse.isMiddleClickRelease() || mouse.isRightClickRelease() || MyKeyboard.getKeyboard().keyUpEventOccurred(42) || MyKeyboard.getKeyboard().keyUpEventOccurred(29)) && !this.cameraControlHeld()) {
            GameManager.gameState.endState(GameState.CAMERA);
        }
    }

    private boolean cameraControlHeld() {
        MyMouse mouse = MyMouse.getActiveMouse();
        return mouse.isRightButtonDown() || mouse.isMouseWheelDown() || MyKeyboard.getKeyboard().isKeyDown(42) || MyKeyboard.getKeyboard().isKeyDown(29);
    }

    private void recalculateViewMatrix() {
        this.viewMatrix.setIdentity();
        Vector3f pos = this.position.get();
        Vector3f cameraPos = new Vector3f(-pos.x, -pos.y, -pos.z);
        Matrix4f.rotate(Maths.degreesToRadians(this.pitch.get()), new Vector3f(1.0f, 0.0f, 0.0f), this.viewMatrix, this.viewMatrix);
        Matrix4f.rotate(Maths.degreesToRadians(-this.yaw.get()), new Vector3f(0.0f, 1.0f, 0.0f), this.viewMatrix, this.viewMatrix);
        Matrix4f.translate(cameraPos, this.viewMatrix, this.viewMatrix);
    }

    private void recalculateReflectedViewMatrix() {
        this.reflectedViewMatrix.setIdentity();
        Vector3f cameraPos = new Vector3f(-this.reflectedPosition.x, -this.reflectedPosition.y, -this.reflectedPosition.z);
        Matrix4f.rotate(Maths.degreesToRadians(-this.pitch.get()), new Vector3f(1.0f, 0.0f, 0.0f), this.reflectedViewMatrix, this.reflectedViewMatrix);
        Matrix4f.rotate(Maths.degreesToRadians(-this.yaw.get()), new Vector3f(0.0f, 1.0f, 0.0f), this.reflectedViewMatrix, this.reflectedViewMatrix);
        Matrix4f.translate(cameraPos, this.reflectedViewMatrix, this.reflectedViewMatrix);
    }

    public boolean inputsActive() {
        return this.enabled && !GuiMaster.isMouseInGui() && GameManager.getGameState() != GameState.GAME_MENU && GameManager.getGameState() != GameState.SPLASH_SCREEN;
    }

    @Override
    public Vector3f getListenerPosition() {
        float terrainDis = this.cameraAimer.getTerrainDistance();
        if (terrainDis < 4.0f && this.cameraAimer.getCurrentTerrainPoint() != null) {
            return this.cameraAimer.getCurrentTerrainPoint();
        }
        return this.cameraAimer.getRayPoint(4.0f);
    }

    @Override
    public MousePicker getCameraPicker() {
        return this.cameraAimer;
    }
}

