/*
 * Decompiled with CFR 0.152.
 */
package colourSelector;

import guiRendering.GuiRenderData;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import toolbox.Colour;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickable;
import userInterfaces.GuiPanel;
import visualFxDrivers.ValueDriver;

public class ColourDisplayGui
extends GuiClickable {
    private GuiPanel panel;

    public ColourDisplayGui(Colour startColour) {
        super(1.0f);
        this.panel = new GuiPanel(GuiRepository.BLOCK, startColour, 1, ColourPalette.BRIGHT_GREY);
    }

    public ColourDisplayGui(Colour startColour, Colour borderColour) {
        super(1.0f);
        this.panel = new GuiPanel(GuiRepository.BLOCK, startColour, 1, borderColour);
    }

    public ColourDisplayGui(Colour startColour, Colour borderColour, int borderSize) {
        super(1.0f);
        this.panel = new GuiPanel(GuiRepository.BLOCK, startColour, borderSize, borderColour);
    }

    public ColourDisplayGui(Colour startColour, int borderSize) {
        super(1.0f);
        this.panel = new GuiPanel(startColour, borderSize);
    }

    public void setAlphaDriver(ValueDriver driver) {
        this.panel.setAlphaDriver(driver);
    }

    public void setBorderColour(Colour borderColour) {
        this.panel.setBorderColour(borderColour);
    }

    public void showBorder(boolean show) {
        this.panel.showBorder(show);
    }

    @Override
    protected void init() {
        super.init();
        super.addComponent(this.panel, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    @Override
    public void addListener(ClickListener listener) {
        super.addListener(listener);
        super.setScaleFactor(1.15f);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }
}

