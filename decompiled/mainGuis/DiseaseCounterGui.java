/*
 * Decompiled with CFR 0.152.
 */
package mainGuis;

import guiRendering.GuiRenderData;
import guis.GuiComponent;
import inventory.CountGui;
import java.util.ArrayList;
import java.util.List;
import main.Camera;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import toolbox.Transformation;
import userInterfaces.GuiButton;
import userInterfaces.Listener;

public class DiseaseCounterGui
extends GuiComponent {
    private static final float COUNTER_WIDTH = 0.7f;
    private GuiButton diseaseButton;
    private CountGui counter;
    private int nextView = 0;
    private List<Transformation> diseasedEntities = new ArrayList<Transformation>();

    public DiseaseCounterGui() {
        this.counter = new CountGui(0, 420, -0.1f);
        this.diseaseButton = new GuiButton(GuiRepository.DISEASE);
    }

    public void incrementCount(Transformation transform) {
        this.diseasedEntities.add(transform);
        this.counter.setCount(this.diseasedEntities.size());
        this.show(true);
    }

    public void decrementCount(Transformation transform) {
        int index = this.diseasedEntities.indexOf(transform);
        this.diseasedEntities.remove(index);
        if (index < this.nextView) {
            --this.nextView;
        }
        this.counter.setCount(this.diseasedEntities.size());
    }

    public void reset() {
        this.diseasedEntities.clear();
        this.counter.setCount(0);
    }

    @Override
    protected void init() {
        this.addDiseaseButton();
        this.addCounter();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        super.show(!this.diseasedEntities.isEmpty());
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addDiseaseButton() {
        super.addComponent(this.diseaseButton, 0.0f, 0.0f, 1.0f, 1.0f);
        this.diseaseButton.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                if (DiseaseCounterGui.this.diseasedEntities.isEmpty()) {
                    return;
                }
                DiseaseCounterGui diseaseCounterGui = DiseaseCounterGui.this;
                diseaseCounterGui.nextView = diseaseCounterGui.nextView % DiseaseCounterGui.this.diseasedEntities.size();
                Camera.getCamera().focusOn(((Transformation)DiseaseCounterGui.this.diseasedEntities.get(DiseaseCounterGui.this.nextView)).getPosition());
                DiseaseCounterGui diseaseCounterGui2 = DiseaseCounterGui.this;
                diseaseCounterGui2.nextView = diseaseCounterGui2.nextView + 1;
            }
        });
    }

    private void addCounter() {
        super.addCenteredComponent(this.counter, 1.0f, 1.0f, 0.7f);
    }
}

