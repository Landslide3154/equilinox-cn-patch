/*
 * Decompiled with CFR 0.152.
 */
package profile3d;

import basics.DisplayManager;
import blueprints.Blueprint;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import mainGuis.MyCursor;
import org.lwjgl.util.vector.Vector2f;
import profile3d.ProfileImageRenderer;
import textures.Texture;
import toolbox.MyMouse;
import userInterfaces.GuiImage;

public class Profile3D
extends GuiComponent {
    public static final int SIZE = 256;
    private static final float ROT_SPEED = 41.0f;
    private static final float MOUSE_EFFECT = 100.0f;
    private static ProfileImageRenderer renderer;
    private boolean grabbed = false;
    private boolean mousedOver = false;

    public static void initProfile3D() {
        renderer = new ProfileImageRenderer(256, 256);
    }

    public static void cleanUp() {
        renderer.cleanUp();
    }

    public Profile3D(Blueprint blueprint) {
        renderer.changeModel(blueprint);
        Texture texture = Texture.getEmptyTexture();
        texture.setTextureID(renderer.getTexture());
        GuiImage image = new GuiImage(new GuiTexture(texture, true));
        super.addComponent(image, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    @Override
    protected void delete() {
        super.delete();
        MyCursor.setCursor(MyCursor.NORMAL);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        MyMouse mouse = MyMouse.getActiveMouse();
        this.checkMouseOver();
        this.checkGrabbed(mouse);
        this.rotateObject(mouse);
        renderer.update();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void checkMouseOver() {
        if (!this.mousedOver && super.isMouseOver()) {
            MyCursor.setCursor(MyCursor.GRABBED_LIGHT);
            this.mousedOver = true;
        } else if (this.mousedOver && !super.isMouseOver()) {
            if (!this.grabbed) {
                MyCursor.setCursor(MyCursor.NORMAL);
            }
            this.mousedOver = false;
        }
    }

    private void checkGrabbed(MyMouse mouse) {
        if (this.mousedOver && mouse.isLeftClick()) {
            this.grabbed = true;
        } else if (mouse.isLeftClickRelease()) {
            if (!this.mousedOver) {
                MyCursor.setCursor(MyCursor.NORMAL);
            }
            this.grabbed = false;
        }
    }

    private void rotateObject(MyMouse mouse) {
        if (this.grabbed) {
            renderer.increaseRotation(this.getRotChange(mouse));
        } else {
            renderer.increaseRotation(DisplayManager.getDeltaSeconds() * 41.0f);
        }
    }

    private float getRotChange(MyMouse mouse) {
        return (float)mouse.getDX() * DisplayManager.getDeltaSeconds() * 100.0f;
    }
}

