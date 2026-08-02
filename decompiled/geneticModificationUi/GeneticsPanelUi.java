/*
 * Decompiled with CFR 0.152.
 */
package geneticModificationUi;

import breedingTraits.Trait;
import entityInfoGui.EntityInfoGui;
import geneticModificationUi.TraitModificationManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import instances.Entity;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;
import userInterfaces.GuiClickableGroup;
import userInterfaces.Tab2ButtonUi;

public class GeneticsPanelUi
extends GuiComponent {
    private static final float DISPLAY_WIDTH = 0.7f;
    private static final float DISPLAY_HEIGHT = 0.8f;
    private static final float BUTTON_X = 0.85f;
    private final EntityInfoGui mainGui;
    private final GuiClickableGroup group = new GuiClickableGroup();
    private final List<TraitModificationManager> traitManagers = new ArrayList<TraitModificationManager>();
    private float yGap;
    private boolean block = false;

    public GeneticsPanelUi(EntityInfoGui mainGui, List<Trait> traits, float yGap, int lineCount) {
        this.mainGui = mainGui;
        for (Trait trait : traits) {
            this.traitManagers.add(trait.getModificationManager());
        }
        this.yGap = yGap;
    }

    public void block(boolean blocked) {
        if (blocked && !super.isInitialized()) {
            this.block = true;
            return;
        }
        if (blocked) {
            this.turnOffButtons();
        }
        for (TraitModificationManager trait : this.traitManagers) {
            trait.block(blocked);
        }
        for (GuiClickable button : this.group.getButtons()) {
            button.block(blocked);
        }
    }

    public void turnOffButtons() {
        this.group.turnOffCurrentlyActive();
    }

    public Entity getEntity() {
        return this.mainGui.getEntity();
    }

    @Override
    protected void init() {
        float yPos = 0.0f;
        for (TraitModificationManager trait : this.traitManagers) {
            this.addTraitUi(trait, yPos);
            yPos += this.yGap;
        }
        if (this.block) {
            this.block(true);
        }
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

    private void addTraitUi(TraitModificationManager traitManager, float yPos) {
        this.addModifierDisplay(traitManager.createModifierDisplay(), yPos);
        this.addButton(traitManager, yPos);
    }

    private void addModifierDisplay(GuiComponent display, float yPos) {
        super.addComponent(display, 0.0f, yPos + this.yGap * 0.19999999f / 2.0f, 0.7f, this.yGap * 0.8f);
    }

    private void addButton(final TraitModificationManager traitManager, float yPos) {
        Tab2ButtonUi tab = new Tab2ButtonUi(GuiRepository.SPANNER_ONLY, GuiRepository.SPANNER_OFF);
        tab.setBlockColour(ColourPalette.DARK_GREY, true);
        tab.setPreferredPixelSize(18);
        super.addPixelCompCenterY(tab, 0.85f, yPos + this.yGap / 2.0f);
        this.group.addButton(tab);
        tab.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    GeneticsPanelUi.this.mainGui.showSecondPanel(traitManager.createModifierUi(GeneticsPanelUi.this));
                } else if (event.isToggleOff()) {
                    GeneticsPanelUi.this.mainGui.removeSecondPanel();
                }
            }
        });
    }
}

