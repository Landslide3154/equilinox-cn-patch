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

public class HungerCounterUi
extends GuiComponent {
    private static final float COUNTER_WIDTH = 0.7f;
    private GuiButton hungerButton;
    private CountGui counter;
    private int nextView = 0;
    private List<Transformation> hungryEntities = new ArrayList<Transformation>();

    public HungerCounterUi() {
        this.counter = new CountGui(0, 420, -0.1f);
        this.hungerButton = new GuiButton(GuiRepository.HUNGER);
    }

    public void incrementCount(Transformation transform) {
        this.hungryEntities.add(transform);
        this.counter.setCount(this.hungryEntities.size());
        this.show(true);
    }

    public void decrementCount(Transformation transform) {
        int index = this.hungryEntities.indexOf(transform);
        if (index == -1) {
            System.err.println("Tried to remove hunger counter for non-hungry entity");
            return;
        }
        this.hungryEntities.remove(index);
        if (index < this.nextView) {
            --this.nextView;
        }
        this.counter.setCount(this.hungryEntities.size());
    }

    public void reset() {
        this.hungryEntities.clear();
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
        super.show(!this.hungryEntities.isEmpty());
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addDiseaseButton() {
        super.addComponent(this.hungerButton, 0.0f, 0.0f, 1.0f, 1.0f);
        this.hungerButton.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                if (HungerCounterUi.this.hungryEntities.isEmpty()) {
                    return;
                }
                HungerCounterUi hungerCounterUi = HungerCounterUi.this;
                hungerCounterUi.nextView = hungerCounterUi.nextView % HungerCounterUi.this.hungryEntities.size();
                Camera.getCamera().focusOn(((Transformation)HungerCounterUi.this.hungryEntities.get(HungerCounterUi.this.nextView)).getPosition());
                HungerCounterUi hungerCounterUi2 = HungerCounterUi.this;
                hungerCounterUi2.nextView = hungerCounterUi2.nextView + 1;
            }
        });
    }

    private void addCounter() {
        super.addCenteredComponent(this.counter, 1.0f, 1.0f, 0.7f);
    }
}

