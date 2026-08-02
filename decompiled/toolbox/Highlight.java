/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import gameManaging.GameManager;
import instances.Entity;
import org.lwjgl.util.vector.Vector3f;
import picking.EntityBox;
import toolbox.Colour;
import toolbox.MyKeyboard;
import toolbox.Transformation;

public class Highlight {
    private static final float SMALLEST = 0.1f;
    private static final float SCALE = 0.2f;
    private Vector3f info = new Vector3f();
    private Colour colour = new Colour(1.0f, 1.0f, 1.0f);
    private boolean show = false;
    private Transformation targetTransform;
    private boolean followMouse = false;

    public Vector3f getInfo() {
        return this.info;
    }

    public boolean isShown() {
        return this.show && !MyKeyboard.getKeyboard().isKeyDown(35);
    }

    public void hide() {
        this.show = false;
        this.targetTransform = null;
        this.followMouse = false;
    }

    public void followEntity(Entity entity, Colour newColour) {
        this.followMouse = false;
        this.show = true;
        this.targetTransform = entity.getTransform();
        this.info.z = 0.1f + this.calcuateHighlightWidth(entity.getBoundingBox()) * 0.2f;
        this.colour.setColour(newColour);
        this.update();
    }

    public void followMouse(Colour newColour, float radius) {
        this.info.z = radius;
        this.show = true;
        this.followMouse = true;
        this.colour.setColour(newColour);
        this.targetTransform = null;
        this.update();
    }

    public void setColour(Colour newColour) {
        this.colour.setColour(newColour);
    }

    public void update() {
        Vector3f point;
        if (this.targetTransform != null) {
            Vector3f position = this.targetTransform.getPosition();
            this.info.x = position.x;
            this.info.y = position.z;
        } else if (this.followMouse && (point = GameManager.getTerrainPicker().getCurrentTerrainPoint()) != null) {
            this.info.x = point.x;
            this.info.y = point.z;
        }
    }

    public Colour getColour() {
        return this.colour;
    }

    private float calcuateHighlightWidth(EntityBox box) {
        return (box.getSizes().x + box.getSizes().z) / 4.0f;
    }
}

