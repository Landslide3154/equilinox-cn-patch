/*
 * Decompiled with CFR 0.152.
 */
package breeding;

import breeding.BreedingComponent;
import entityInfoGui.EntityInfoGui;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.GuiCheckBox;
import userInterfaces.Listener;

public class BreedingSwitchGui
extends GuiComponent {
    private static final float SWITCH_HEIGHT = 1.7f;
    private static final String NAME = "Breeding boost";
    private GuiCheckBox breedSwitch;
    private BreedingComponent breedComponent;

    protected BreedingSwitchGui(BreedingComponent breedComp) {
        this.breedComponent = breedComp;
        this.breedSwitch = new GuiCheckBox(this.breedComponent.isBreedingBoosted());
        this.breedSwitch.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                BreedingSwitchGui.this.breedComponent.setBreedingBoost(on);
            }
        });
    }

    @Override
    protected void init() {
        float width = super.getRelativeWidthCoords(1.7f);
        super.addComponentY(this.breedSwitch, 1.0f - width, -0.35000002f, 1.7f);
        this.addText();
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

    private void addText() {
        Text text = Text.newText(NAME).setFontSize(EntityInfoGui.FONT_SIZE).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, 0.0f, 1.0f);
    }
}

