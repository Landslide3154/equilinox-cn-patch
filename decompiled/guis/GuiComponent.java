/*
 * Decompiled with CFR 0.152.
 */
package guis;

import basics.DisplayManager;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiMaster;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.MyMouse;

public abstract class GuiComponent {
    private Vector2f position = new Vector2f();
    private Vector2f scale = new Vector2f();
    private Vector2f relativePosition = new Vector2f();
    private Vector2f relativeScale = new Vector2f();
    private GuiComponent parent;
    private boolean visible = true;
    private int[] clippingBounds;
    private List<GuiComponent> childComponents = new ArrayList<GuiComponent>();
    private Map<Text, Vector3f> componentTexts = new HashMap<Text, Vector3f>();
    private List<GuiComponent> componentsToRemove = new ArrayList<GuiComponent>();
    private List<GuiComponent> componentsToAdd = new ArrayList<GuiComponent>();
    private List<Text> textsToRemove = new ArrayList<Text>();
    private boolean initialized = false;
    private boolean hasFocus = false;
    private boolean useCenteringX = false;
    private boolean useCenteringY = false;
    private boolean usePreferredAspectFixedX = false;
    private boolean usePreferredAspectFixedY = false;
    private float preferredAspect = 1.0f;
    private boolean specificLevelSet = false;
    private int level = 0;
    private int preferredPixelSize = 1;

    public void show(boolean visible) {
        this.visible = visible;
    }

    public void addComponent(GuiComponent component, float relX, float relY, float relScaleX, float relScaleY) {
        component.relativePosition.set(relX, relY);
        component.relativeScale.set(relScaleX, relScaleY);
        component.parent = this;
        this.componentsToAdd.add(component);
    }

    public void addComponentX(GuiComponent component, float relX, float relY, float relScaleX) {
        component.usePreferredAspectFixedX = true;
        component.relativePosition.set(relX, relY);
        component.relativeScale.x = relScaleX;
        this.componentsToAdd.add(component);
        component.parent = this;
    }

    public void addComponentY(GuiComponent component, float relX, float relY, float relScaleY) {
        component.usePreferredAspectFixedY = true;
        component.relativePosition.set(relX, relY);
        component.relativeScale.y = relScaleY;
        this.componentsToAdd.add(component);
        component.parent = this;
    }

    public void addPixelComp(GuiComponent component, float relX, float relY) {
        if (!this.initialized) {
            System.err.println("UI Component must be initialized before adding PP component!");
        }
        float width = (float)component.preferredPixelSize / (this.scale.x * (float)DisplayManager.getUiWidth());
        float height = (float)component.preferredPixelSize / (this.scale.y * (float)DisplayManager.getUiHeight());
        this.addComponent(component, relX, relY, width, height);
    }

    public void addPixelCompCenterX(GuiComponent component, float centerX, float relY) {
        if (!this.initialized) {
            System.err.println("UI Component must be initialized before adding PP component!");
        }
        float width = (float)component.preferredPixelSize / (this.scale.x * (float)DisplayManager.getUiWidth());
        float height = (float)component.preferredPixelSize / (this.scale.y * (float)DisplayManager.getUiHeight());
        this.addComponent(component, centerX - width * 0.5f, relY, width, height);
    }

    public void addPixelCompCenterY(GuiComponent component, float relX, float centerY) {
        if (!this.initialized) {
            System.err.println("UI Component must be initialized before adding PP component!");
        }
        float width = (float)component.preferredPixelSize / (this.scale.x * (float)DisplayManager.getUiWidth());
        float height = (float)component.preferredPixelSize / (this.scale.y * (float)DisplayManager.getUiHeight());
        this.addComponent(component, relX, centerY - height * 0.5f, width, height);
    }

    public void addPixelCompCenter(GuiComponent component, float centerX, float centerY) {
        if (!this.initialized) {
            System.err.println("UI Component must be initialized before adding PP component!");
        }
        float width = (float)component.preferredPixelSize / (this.scale.x * (float)DisplayManager.getUiWidth());
        float height = (float)component.preferredPixelSize / (this.scale.y * (float)DisplayManager.getUiHeight());
        this.addComponent(component, centerX - width * 0.5f, centerY - height * 0.5f, width, height);
    }

    public void addCenteredComponentX(GuiComponent component, float centerX, float relY, float relScaleY) {
        component.usePreferredAspectFixedY = true;
        component.useCenteringX = true;
        component.relativePosition.set(centerX, relY);
        component.relativeScale.y = relScaleY;
        this.componentsToAdd.add(component);
        component.parent = this;
    }

    public void addCenteredComponent(GuiComponent component, float centerX, float centerY, float relScaleX) {
        component.usePreferredAspectFixedX = true;
        component.useCenteringX = true;
        component.useCenteringY = true;
        component.relativePosition.set(centerX, centerY);
        component.relativeScale.x = relScaleX;
        this.componentsToAdd.add(component);
        component.parent = this;
    }

    public void addCenteredComponentY(GuiComponent component, float centerY, float relX, float relScaleX) {
        component.usePreferredAspectFixedX = true;
        component.useCenteringY = true;
        component.relativePosition.set(relX, centerY);
        component.relativeScale.x = relScaleX;
        this.componentsToAdd.add(component);
        component.parent = this;
    }

    public void addCenteredComponentYScaleY(GuiComponent component, float centerY, float relX, float relScaleY) {
        component.usePreferredAspectFixedY = true;
        component.useCenteringY = true;
        component.relativePosition.set(relX, centerY);
        component.relativeScale.y = relScaleY;
        this.componentsToAdd.add(component);
        component.parent = this;
    }

    public void setPreferredPixelSize(int size) {
        this.preferredPixelSize = size;
    }

    public void setFocus(boolean focus) {
        this.hasFocus = focus;
        this.setChildrenFocus();
    }

    private void setChildrenFocus() {
        for (GuiComponent child : this.childComponents) {
            child.hasFocus = this.hasFocus;
            child.setChildrenRenderLevel();
        }
    }

    public void setRenderLevel(int level) {
        this.level = level;
        this.specificLevelSet = true;
        this.setChildrenRenderLevel();
    }

    public void removeComponent(GuiComponent component) {
        this.componentsToRemove.add(component);
    }

    public void clear() {
        this.componentsToRemove.addAll(this.childComponents);
        this.deleteTexts();
    }

    public void addText(Text text, float relX, float relY, float relLineWidth) {
        Vector3f relativePosition = new Vector3f(relX, relY, relLineWidth);
        text.setParentInfo(this, relativePosition);
        text.setClippingBounds(this.clippingBounds);
        this.componentTexts.put(text, relativePosition);
        if (this.initialized) {
            this.setTextScreenSpacePosition(text, relativePosition);
        }
    }

    protected boolean isInitialized() {
        return this.initialized;
    }

    public boolean isShown() {
        return this.visible;
    }

    public void deleteText(Text text) {
        this.textsToRemove.add(text);
    }

    public float getRelativeX() {
        return this.relativePosition.x;
    }

    public float getRelativeY() {
        return this.relativePosition.y;
    }

    public float getRelativeScaleY() {
        return this.relativeScale.y;
    }

    public float getRelativeScaleX() {
        return this.relativeScale.x;
    }

    public void setRelativeX(float x) {
        if (this.relativePosition.x != x) {
            this.relativePosition.x = x;
            this.updateScreenSpacePosition();
        }
    }

    public void setRelativeY(float y) {
        if (this.relativePosition.y != y) {
            this.relativePosition.y = y;
            this.updateScreenSpacePosition();
        }
    }

    public void setRelativeScaleX(float relScaleX) {
        if (this.relativeScale.x != relScaleX) {
            this.relativeScale.x = relScaleX;
            this.updateScreenSpacePosition();
        }
    }

    public void setRelativeScaleY(float relScaleY) {
        if (this.relativeScale.y != relScaleY) {
            this.relativeScale.y = relScaleY;
            this.updateScreenSpacePosition();
        }
    }

    public void setRelativeScale(float relScaleX, float relScaleY) {
        if (this.relativeScale.x != relScaleX || this.relativeScale.y != relScaleY) {
            this.relativeScale.x = relScaleX;
            this.relativeScale.y = relScaleY;
            this.updateScreenSpacePosition();
        }
    }

    public void remove() {
        this.parent.removeComponent(this);
    }

    public void increaseRelativePosition(float dX, float dY) {
        this.relativePosition.x += dX;
        this.relativePosition.y += dY;
        this.updateScreenSpacePosition();
    }

    public float getRelativeAspectRatio() {
        return this.relativeScale.x / this.relativeScale.y;
    }

    public void setRelativePosition(float x, float y) {
        if (this.relativePosition.x != x || this.relativePosition.y != y) {
            this.relativePosition.x = x;
            this.relativePosition.y = y;
            this.updateScreenSpacePosition();
        }
    }

    public void setPreferredAspectRatio(float ratio) {
        this.preferredAspect = ratio;
    }

    public void updateTextAbsPos(Text text) {
        this.setTextScreenSpacePosition(text, this.componentTexts.get(text));
    }

    public Vector2f getScale() {
        return this.scale;
    }

    public float getPixelHeight() {
        return this.scale.y * (float)DisplayManager.getUiHeight();
    }

    public float getPixelWidth() {
        return this.scale.x * (float)DisplayManager.getUiWidth();
    }

    public final boolean isMouseOver() {
        if (!GuiMaster.isInFocus(this)) {
            return false;
        }
        return this.isMouseOverFocusIrrelevant();
    }

    public boolean isMouseOverFocusIrrelevant() {
        if (!GuiMaster.isMouseInteractionEnabled()) {
            return false;
        }
        MyMouse mouse = MyMouse.getActiveMouse();
        return mouse.getX() >= this.position.x && mouse.getX() <= this.position.x + this.scale.x && mouse.getY() >= this.position.y && mouse.getY() <= this.position.y + this.scale.y;
    }

    protected boolean inFocus() {
        return this.hasFocus;
    }

    public int getLevel() {
        return this.level;
    }

    public float getRelativeMouseX() {
        MyMouse mouse = MyMouse.getActiveMouse();
        return (mouse.getX() - this.position.x) / this.scale.x;
    }

    public float getRelativeMouseY() {
        MyMouse mouse = MyMouse.getActiveMouse();
        return (mouse.getY() - this.position.y) / this.scale.y;
    }

    protected void setClippingBounds(float x, float y, float width, float height) {
        int xPixels = Math.round(x * (float)Display.getWidth());
        int yPixels = Display.getHeight() - Math.round((y + height) * (float)Display.getHeight());
        int widthPixels = Math.round(width * (float)Display.getWidth());
        int heightPixels = Math.round(height * (float)Display.getHeight());
        if (this.clippingBounds == null) {
            int[] bounds = new int[]{xPixels, yPixels, widthPixels, heightPixels};
            this.setChildrenClippingBounds(bounds);
        } else {
            this.clippingBounds[0] = xPixels;
            this.clippingBounds[1] = yPixels;
            this.clippingBounds[2] = widthPixels;
            this.clippingBounds[3] = heightPixels;
        }
    }

    protected void setTextureClippingBounds(int[] bounds) {
    }

    protected float pixelsToRelativeX(float pixels) {
        float pixelsWide = (float)DisplayManager.getUiWidth() * this.scale.x;
        return pixels / pixelsWide;
    }

    protected float pixelsToRelativeY(float pixels) {
        float pixelsHigh = (float)DisplayManager.getUiHeight() * this.scale.y;
        return pixels / pixelsHigh;
    }

    public Vector2f getPosition() {
        return this.position;
    }

    protected void forceInitialization(float absX, float absY, float absScaleX, float absScaleY) {
        this.position.x = absX;
        this.position.y = absY;
        this.scale.x = absScaleX;
        this.scale.y = absScaleY;
        this.initialized = true;
    }

    protected final void update(GuiRenderData data) {
        if (!this.visible) {
            return;
        }
        this.updateSelf();
        this.updateTexts();
        this.getGuiTextures(data);
        this.addTextsToRenderBatch(data);
        this.addNewChildren();
        this.removeOldComponents();
        for (GuiComponent childComponent : this.childComponents) {
            childComponent.update(data);
        }
    }

    protected List<GuiComponent> getComponents() {
        return this.childComponents;
    }

    protected float getRelativeHeightCoords(float relativeWidth) {
        relativeWidth *= (float)DisplayManager.getUiWidth() / (float)DisplayManager.getUiHeight();
        return relativeWidth *= this.scale.x / this.scale.y;
    }

    protected float getRelativeWidthCoords(float relativeHeight) {
        relativeHeight /= (float)DisplayManager.getUiWidth() / (float)DisplayManager.getUiHeight();
        return relativeHeight /= this.scale.x / this.scale.y;
    }

    protected void init() {
    }

    protected void updateScreenSpacePosition() {
        this.calculateAbsPositionAndScale();
        this.updateGuiTexturePositions(this.position, this.scale);
        for (GuiComponent component : this.childComponents) {
            component.updateScreenSpacePosition();
        }
        for (Text text : this.componentTexts.keySet()) {
            this.setTextScreenSpacePosition(text, this.componentTexts.get(text));
        }
    }

    protected abstract void updateGuiTexturePositions(Vector2f var1, Vector2f var2);

    protected abstract void updateSelf();

    protected abstract void getGuiTextures(GuiRenderData var1);

    protected void delete() {
        this.deleteTexts();
        for (GuiComponent component : this.componentsToRemove) {
            component.delete();
        }
        for (GuiComponent component : this.componentsToAdd) {
            component.delete();
        }
        for (GuiComponent component : this.childComponents) {
            component.delete();
        }
    }

    private void setChildrenClippingBounds(int[] bounds) {
        this.clippingBounds = bounds;
        this.setTextureClippingBounds(bounds);
        for (Text text : this.componentTexts.keySet()) {
            text.setClippingBounds(this.clippingBounds);
        }
        for (GuiComponent child : this.childComponents) {
            child.setChildrenClippingBounds(this.clippingBounds);
        }
    }

    private void setChildrenRenderLevel() {
        for (GuiComponent child : this.childComponents) {
            if (child.specificLevelSet) continue;
            child.level = this.level;
            child.setChildrenRenderLevel();
        }
    }

    private void addTextsToRenderBatch(GuiRenderData data) {
        for (Text text : this.componentTexts.keySet()) {
            data.addText(text.getRenderLevel(), text);
        }
    }

    private void deleteTexts() {
        for (Text text : this.componentTexts.keySet()) {
            text.deleteFromMemory();
        }
        this.componentTexts.clear();
    }

    private void setTextScreenSpacePosition(Text text, Vector3f relativePosition) {
        float x = this.position.x + this.scale.x * relativePosition.x;
        float y = this.position.y + this.scale.y * relativePosition.y;
        float lineWidth = relativePosition.z * this.scale.x;
        text.initialise(x, y, lineWidth);
    }

    private void updateTexts() {
        for (Text text : this.textsToRemove) {
            this.componentTexts.remove(text);
            text.deleteFromMemory();
        }
        this.textsToRemove.clear();
        for (Text text : this.componentTexts.keySet()) {
            text.update(DisplayManager.getDeltaSeconds());
        }
    }

    private void removeOldComponents() {
        while (!this.componentsToRemove.isEmpty()) {
            GuiComponent component = this.componentsToRemove.remove(0);
            this.childComponents.remove(component);
            component.delete();
        }
    }

    private void addNewChildren() {
        int index = 0;
        while (index < this.componentsToAdd.size()) {
            GuiComponent component = this.componentsToAdd.get(index++);
            this.childComponents.add(component);
            component.clippingBounds = this.clippingBounds;
            component.hasFocus = this.hasFocus;
            if (!component.specificLevelSet) {
                component.level = this.level;
            }
            component.updateScreenSpacePosition();
            component.init();
            component.setTextureClippingBounds(this.clippingBounds);
            component.addNewChildren();
        }
        this.componentsToAdd.clear();
    }

    private void calculateAbsPositionAndScale() {
        this.position.x = this.parent.position.x + this.parent.scale.x * this.relativePosition.x;
        this.position.y = this.parent.position.y + this.parent.scale.y * this.relativePosition.y;
        this.scale.x = this.relativeScale.x * this.parent.scale.x;
        this.scale.y = this.relativeScale.y * this.parent.scale.y;
        if (this.usePreferredAspectFixedY) {
            this.scale.x = this.convertToScreenWidthCoords(this.scale.y);
            this.relativeScale.x = this.scale.x / this.parent.scale.x;
            this.usePreferredAspectFixedY = false;
        } else if (this.usePreferredAspectFixedX) {
            this.scale.y = this.convertToScreenHeightCoords(this.scale.x);
            this.relativeScale.y = this.scale.y / this.parent.scale.y;
            this.usePreferredAspectFixedX = false;
        }
        if (this.useCenteringX) {
            this.position.x -= this.scale.x / 2.0f;
            this.relativePosition.x = (this.position.x - this.parent.position.x) / this.parent.scale.x;
            this.useCenteringX = false;
        }
        if (this.useCenteringY) {
            this.position.y -= this.scale.y / 2.0f;
            this.relativePosition.y = (this.position.y - this.parent.position.y) / this.parent.scale.y;
            this.useCenteringY = false;
        }
        this.initialized = true;
    }

    private float convertToScreenWidthCoords(float heightCoord) {
        heightCoord /= DisplayManager.getAspectRatio();
        return heightCoord *= this.preferredAspect;
    }

    private float convertToScreenHeightCoords(float widthCoord) {
        widthCoord *= DisplayManager.getAspectRatio();
        return widthCoord /= this.preferredAspect;
    }
}

