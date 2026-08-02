/*
 * Decompiled with CFR 0.152.
 */
package speciesInformation;

import basics.DisplayManager;
import blueprints.Blueprint;
import breedingTrees.BreedingTreeGui;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import extraInfoGui.ExtraInfoGui;
import gameManaging.GameManager;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import session.GameMode;
import speciesInformation.InfoPanelGui;
import textures.Texture;
import toolTips.ToolTipInfo;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;

public class SpeciesInfoGui {
    private static final String INFO_TITLE = GameText.getText(534);
    private static final String INFO_INFO = GameText.getText(535);
    private static final String EVOLVE_TITLE = GameText.getText(530);
    private static final String EVOLVE_INFO = GameText.getText(531);
    private static final String BREED_TITLE = GameText.getText(532);
    private static final String BREED_INFO = GameText.getText(533);
    public static final float FONT_SIZE = 500.0f / (float)DisplayManager.getUiHeight();

    public static void createSpeciesInfoGui(Blueprint blueprint) {
        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)blueprint.getComponent(ComponentType.INFO);
        ExtraInfoGui gui = EquilinoxGuis.getExtraInfoGui();
        boolean breeder = GameManager.getGameMode() == GameMode.NORMAL && blueprint.getComponent(ComponentType.LIFE) != null;
        boolean base = breeder && GameManager.BREED_TREES.isBaseSpecies(blueprint);
        ArrayList<ToolTipInfo> tips = new ArrayList<ToolTipInfo>();
        List<Texture> icons = SpeciesInfoGui.getIconList(breeder, base, tips);
        gui.display(info.getName(), icons, tips, new InfoPanelGui(blueprint));
        SpeciesInfoGui.addTabListeners(gui, blueprint, breeder, base);
    }

    private static void addTabListeners(ExtraInfoGui gui, Blueprint blueprint, boolean breeder, boolean base) {
        SpeciesInfoGui.addInfoTabListener(gui, blueprint);
        if (breeder) {
            if (!base) {
                SpeciesInfoGui.addBreedUpTabListener(gui, blueprint);
            }
            SpeciesInfoGui.addBreedDownTabListener(gui, blueprint);
        }
    }

    private static void addInfoTabListener(final ExtraInfoGui gui, final Blueprint blueprint) {
        gui.getToolbarGui().addTabListener(0, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    gui.getContentFrame().display(new InfoPanelGui(blueprint));
                }
            }
        });
    }

    private static void addBreedUpTabListener(final ExtraInfoGui gui, final Blueprint blueprint) {
        gui.getToolbarGui().addTabListener(2, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    gui.getContentFrame().display(new BreedingTreeGui(GameManager.BREED_TREES.getNode(blueprint), true));
                }
            }
        });
    }

    private static void addBreedDownTabListener(final ExtraInfoGui gui, final Blueprint blueprint) {
        gui.getToolbarGui().addTabListener(1, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    gui.getContentFrame().display(new BreedingTreeGui(GameManager.BREED_TREES.getNode(blueprint), false));
                }
            }
        });
    }

    private static List<Texture> getIconList(boolean breeder, boolean base, List<ToolTipInfo> toolTips) {
        ArrayList<Texture> icons = new ArrayList<Texture>();
        icons.add(GuiRepository.INFO_ICON_20);
        toolTips.add(ToolTipInfo.newInfo(INFO_TITLE, INFO_INFO, true, false));
        if (breeder) {
            toolTips.add(ToolTipInfo.newInfo(EVOLVE_TITLE, EVOLVE_INFO, true, false));
            icons.add(GuiRepository.BREED20);
            if (!base) {
                toolTips.add(ToolTipInfo.newInfo(BREED_TITLE, BREED_INFO, true, false));
                icons.add(GuiRepository.BREED_UP_UP);
            }
        }
        return icons;
    }
}

