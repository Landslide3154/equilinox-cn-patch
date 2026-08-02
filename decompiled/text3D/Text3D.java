/*
 * Decompiled with CFR 0.152.
 */
package text3D;

import basics.MasterRenderer;
import fontRendering.Text;
import guis.GuiMaster;
import instances.Entity;
import main.Camera;
import main.IGameCam;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import visualFxDrivers.ConstantDriver;

public class Text3D {
    private final Entity entity;
    private boolean active;
    private Text text;
    private Vector3f position = new Vector3f();

    public Text3D(Entity entity, String stringText) {
        this.entity = entity;
        this.text = this.initText(stringText);
        this.active = true;
        entity.set3dTextDisplay(this);
    }

    private Text initText(String stringText) {
        Text text = Text.newText(stringText).setFontSize(UiSettings.NORM_FONT).center().create();
        text.setColour(ColourPalette.WHITE);
        Vector3f screenCoords = this.calculateScreenCoordPosition();
        GuiMaster.addText(text, screenCoords.x - 0.5f, screenCoords.y, 1.0f);
        return text;
    }

    public void setActive() {
        this.active = true;
    }

    public void setAlpha(float alpha) {
        this.text.setAlphaDriver(new ConstantDriver(alpha));
    }

    public boolean update() {
        if (!this.active || this.entity.isDead() || this.entity.isGrabbed()) {
            return false;
        }
        this.updatePosition();
        this.active = false;
        return true;
    }

    public void destroy() {
        this.entity.set3dTextDisplay(null);
        GuiMaster.removeText(this.text);
    }

    private void updatePosition() {
        Vector3f screenCoords = this.calculateScreenCoordPosition();
        this.text.setAbsPosition(screenCoords.x - 0.5f, screenCoords.y);
    }

    private Vector3f calculateScreenCoordPosition() {
        IGameCam camera = Camera.getCamera();
        this.position.set(this.entity.getTransform().getPosition());
        this.position.y += this.entity.getBoundingBox().getHeight();
        Vector3f screenCoords = Maths.convertToScreenSpace(this.position, camera.getViewMatrix(), MasterRenderer.getProjectionMatrix());
        if (screenCoords == null) {
            return new Vector3f(-1.0f, -1.0f, 0.0f);
        }
        return screenCoords;
    }
}

