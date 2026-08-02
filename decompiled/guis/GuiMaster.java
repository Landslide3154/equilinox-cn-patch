/*
 * Decompiled with CFR 0.152.
 */
package guis;

import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiScreenContainer;
import toolbox.MyMouse;
import utils.FileUtils;
import utils.MyFile;

public class GuiMaster {
    public static final MyFile GUIS_LOC = new MyFile(FileUtils.RES_FOLDER, "guis");
    protected static final GuiScreenContainer CONTAINER = new GuiScreenContainer();
    private static final GuiRenderData renderData = new GuiRenderData();
    private static boolean mouseInteraction = true;
    private static GuiComponent currentFocus = null;
    private static boolean removeFocus = false;

    public static void updateGuis() {
        if (removeFocus) {
            GuiMaster.removeFocus();
        }
        renderData.clear();
        CONTAINER.update(renderData);
    }

    public static boolean isMouseInGui() {
        return CONTAINER.isMouseInGui();
    }

    public static void focusOn(GuiComponent newFocus) {
        if (currentFocus != null) {
            GuiMaster.removeFocus();
        }
        newFocus.setFocus(true);
        currentFocus = newFocus;
    }

    public static void releaseFocus(GuiComponent oldFocus) {
        if (currentFocus == oldFocus) {
            removeFocus = true;
        }
    }

    private static void removeFocus() {
        currentFocus.setFocus(false);
        currentFocus = null;
        removeFocus = false;
    }

    public static boolean clickedOffGui() {
        MyMouse mouse;
        return !GuiMaster.isMouseInGui() && (mouse = MyMouse.getActiveMouse()).isLeftClick();
    }

    public static boolean isInFocus(GuiComponent component) {
        return currentFocus == null || component.inFocus();
    }

    public static void enableMouseInteraction(boolean enable) {
        mouseInteraction = enable;
    }

    public static boolean isMouseInteractionEnabled() {
        return mouseInteraction;
    }

    public static GuiRenderData getRenderData() {
        return renderData;
    }

    public static void addComponent(GuiComponent component, float relX, float relY, float relScaleX, float relScaleY) {
        CONTAINER.addComponent(component, relX, relY, relScaleX, relScaleY);
    }

    public static void addResizingComponent(GuiComponent component) {
        CONTAINER.addComponent(component, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    public static void addText(Text text, float relX, float relY, float relLineWidth) {
        text.setRenderLevel(-1);
        CONTAINER.addText(text, relX, relY, relLineWidth);
    }

    public static void removeText(Text text) {
        CONTAINER.deleteText(text);
    }
}

