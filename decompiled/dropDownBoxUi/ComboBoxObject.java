/*
 * Decompiled with CFR 0.152.
 */
package dropDownBoxUi;

import dropDownBoxUi.UiProvider;
import guis.GuiComponent;

public class ComboBoxObject {
    private final Object object;
    private final Object[] subObjects;
    private boolean selectable = true;
    private String extraPrefix = "";
    private final UiProvider uiProvider;

    public ComboBoxObject(Object object) {
        this.object = object;
        this.subObjects = null;
        this.uiProvider = null;
    }

    public ComboBoxObject(Object object, UiProvider uiProvider) {
        this.object = object;
        this.subObjects = null;
        this.uiProvider = uiProvider;
    }

    public ComboBoxObject(Object object, Object[] subObjects) {
        this.object = object;
        this.subObjects = subObjects;
        this.uiProvider = null;
    }

    public String toString() {
        return this.object.toString();
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public void setExtraPrefix(String prefix) {
        this.extraPrefix = prefix;
    }

    public String getExtraPrefix() {
        return this.extraPrefix;
    }

    public boolean isSelectable() {
        return this.selectable;
    }

    public boolean hasUiComponent() {
        return this.uiProvider != null;
    }

    public GuiComponent createUiComponent() {
        return this.uiProvider.createUi();
    }

    public Object getObject() {
        return this.object;
    }

    protected Object[] getSubObjects() {
        return this.subObjects;
    }

    protected boolean hasSubObjects() {
        return this.subObjects != null && this.subObjects.length > 0;
    }
}

