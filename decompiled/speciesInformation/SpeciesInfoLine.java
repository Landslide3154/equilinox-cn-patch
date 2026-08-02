/*
 * Decompiled with CFR 0.152.
 */
package speciesInformation;

import guis.GuiComponent;
import toolbox.Colour;

public class SpeciesInfoLine {
    private String name;
    private Colour overrideColour = null;
    private String valueText;
    private GuiComponent valueComponent;
    private boolean isValueText;

    public SpeciesInfoLine(String name, String value) {
        this.name = name;
        this.valueText = value;
        this.isValueText = true;
    }

    public SpeciesInfoLine(String name, GuiComponent value) {
        this.name = name;
        this.valueComponent = value;
        this.isValueText = false;
    }

    public void setOverrideColour(Colour colour) {
        this.overrideColour = colour;
    }

    protected String getName() {
        return this.name;
    }

    protected Colour getOverrideColour() {
        return this.overrideColour;
    }

    protected String getValueText() {
        return this.valueText;
    }

    protected GuiComponent getValueComponent() {
        return this.valueComponent;
    }

    protected boolean usesTextValue() {
        return this.isValueText;
    }
}

