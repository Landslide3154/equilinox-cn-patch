/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import entityInfoGui.InfoType;
import entityInfoGui.PopUpInfoGui;
import userInterfaces.Listener;
import userInterfaces.TextFieldGui;

public abstract class EditableTextInfo
extends PopUpInfoGui {
    private TextFieldGui textField;

    public EditableTextInfo(String name, float font, int maxChars) {
        super(name, InfoType.HEADER, font);
        this.textField = new TextFieldGui(this.getValue(), super.getFontSize(), maxChars, true);
    }

    public void addChangeListener(Listener listener) {
        this.textField.addChangeListener(listener);
    }

    public String getCurrentText() {
        return this.textField.getCurrentText();
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
    }

    @Override
    protected void initValueGui() {
        super.addComponent(this.textField, 0.54f, 0.0f, 0.42f, 1.0f);
    }

    public abstract String getValue();
}

