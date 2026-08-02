/*
 * Decompiled with CFR 0.152.
 */
package colourSelector;

import colourSelector.BrightnessSelectorGui;
import colourSelector.ColourWheelGui;
import colourSelector.RecentColoursUi;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Colour;
import toolbox.HsvColour;
import userInterfaces.Listener;

public class ColourSelectorGui
extends GuiComponent {
    private static final float COL_SIDE_WIDTH = 0.1f;
    private static final float GAP = 0.04f;
    private static final float CIRCLE_WIDTH = 0.72f;
    private ColourWheelGui colourWheel;
    private BrightnessSelectorGui brightnessSelector;
    private RecentColoursUi recentListUi;
    private Colour currentColour;
    private List<Listener> listeners = new ArrayList<Listener>();

    public ColourSelectorGui(Colour colourToManipulate, Colour originalColour) {
        this.setPreferredAspectRatio(0.9458128f);
        HsvColour originalHsv = originalColour.getHsvColour();
        HsvColour startHsv = colourToManipulate.getHsvColour();
        this.currentColour = colourToManipulate;
        this.colourWheel = new ColourWheelGui(originalHsv, startHsv);
        this.brightnessSelector = new BrightnessSelectorGui(this.colourWheel.getRgbColour(), startHsv.getValue(), originalHsv.getValue());
        this.recentListUi = new RecentColoursUi(this, GameManager.getSession().getStats().getRecentColours());
        this.addColourWheelListener();
        this.addBrightnessSelectorListener();
    }

    public void setColour(Colour newColour) {
        HsvColour newCol = newColour.getHsvColour();
        this.colourWheel.set(newCol);
        this.brightnessSelector.set(newCol.getValue());
    }

    public void addChangeListener(Listener listener) {
        this.listeners.add(listener);
    }

    @Override
    protected void init() {
        super.addComponentX(this.colourWheel, 0.14f, 0.0f, 0.72f);
        float height = super.getRelativeHeightCoords(0.72f);
        super.addComponent(this.brightnessSelector, 0.0f, 0.0f, 0.1f, height);
        super.addComponent(this.recentListUi, 0.90000004f, 0.0f, 0.1f, height);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void updateColour() {
        HsvColour hueSat = this.colourWheel.getHsvColour();
        float bright = this.brightnessSelector.getBrightness();
        this.currentColour.setHsvColour(hueSat.getHue(), hueSat.getSaturation(), bright);
        this.notifyChangeListeners();
    }

    private void addColourWheelListener() {
        this.colourWheel.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                ColourSelectorGui.this.brightnessSelector.setColour(ColourSelectorGui.this.colourWheel.getRgbColour());
                ColourSelectorGui.this.updateColour();
            }
        });
    }

    private void addBrightnessSelectorListener() {
        this.brightnessSelector.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                ColourSelectorGui.this.updateColour();
            }
        });
    }

    private void notifyChangeListeners() {
        for (Listener listener : this.listeners) {
            listener.eventOccurred(true);
        }
    }
}

