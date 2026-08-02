/*
 * Decompiled with CFR 0.152.
 */
package unlockGuide;

import blueprints.Blueprint;
import componentArchitecture.ComponentType;
import extraInfoGui.ExtraInfoContent;
import extraInfoGui.ExtraInfoGui;
import fontRendering.Text;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import health.LifeCompBlueprint;
import java.util.ArrayList;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import shopping.BlueprintShopItem;
import taskUi.RequiredTaskListUi;
import tasks.Task;
import textures.Texture;
import toolTips.ToolTipInfo;
import unlockGuide.EvolveChainUi;
import userInterfaces.GuiImage;

public class UnlockGuideUi
extends ExtraInfoContent {
    private static final int SHORT_LIST_LENGTH = 4;
    private static final int LONG_LIST_LENGTH = 11;
    private static final int TOP_PAD = 25;
    private static final int IMAGE_HEIGHT = 55;
    private static final int PAD = 10;
    private static final int TITLE_HEIGHT = 35;
    private static final int TASKS_HEIGHT = 170;
    private static final float TASK_GAP_VAL = 0.02f;
    private static final String TITLE = GameText.getText(1120);
    private static final String TASK_TITLE_BASE = GameText.getText(1118);
    private static final String TASK_TITLE = GameText.getText(1119);
    private static final String EVOLVE_ONLY = GameText.getText(1121);
    private static final String EVOLVE_SECOND = GameText.getText(1122);
    private final BlueprintShopItem item;
    private float yPos = 0.0f;

    public static void openUnlockGuidePanel(BlueprintShopItem item) {
        ExtraInfoGui extraInfoGui = EquilinoxGuis.getExtraInfoGui();
        extraInfoGui.display(String.valueOf(TITLE) + ": " + item.getName(), new ArrayList<Texture>(), new ArrayList<ToolTipInfo>(), new UnlockGuideUi(item));
    }

    private UnlockGuideUi(BlueprintShopItem item) {
        this.item = item;
    }

    @Override
    protected void init() {
        super.init();
        this.yPos += super.pixelsToRelativeY(25.0f);
        this.addLockImage();
        if (this.item.hasLinkedTask()) {
            this.addTaskSection(true, this.item.getRequiredTask());
            return;
        }
        Blueprint baseSpecies = this.getBaseSpecies(this.item.getBlueprint());
        BlueprintShopItem baseItem = (BlueprintShopItem)GameManager.getShops().getItem(baseSpecies.getId());
        if (baseItem.isLocked()) {
            this.addTaskSection(false, baseItem.getRequiredTask());
        }
        this.addEvolveSection(!baseItem.isLocked());
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

    private void addLockImage() {
        GuiImage lockImage = new GuiImage(GuiRepository.LOCK_ICON);
        float height = super.pixelsToRelativeY(55.0f);
        super.addCenteredComponentX(lockImage, 0.5f, this.yPos, height);
        this.yPos += height + super.pixelsToRelativeY(10.0f);
    }

    private Blueprint getBaseSpecies(Blueprint blueprint) {
        LifeCompBlueprint lifeComp = (LifeCompBlueprint)blueprint.getComponent(ComponentType.LIFE);
        Blueprint parent = lifeComp.breedInfo.getParent();
        if (parent != null) {
            return this.getBaseSpecies(parent);
        }
        return blueprint;
    }

    private void addTaskSection(boolean baseSpecies, Task linkedTask) {
        this.addTaskSectionTitle(baseSpecies);
        this.addRequiredTasksList(linkedTask, !baseSpecies);
    }

    private void addTaskSectionTitle(boolean baseSpecies) {
        Text text = Text.newText(baseSpecies ? TASK_TITLE_BASE : TASK_TITLE).center().setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, this.yPos, 1.0f);
        this.yPos += super.pixelsToRelativeY(35.0f);
    }

    private void addRequiredTasksList(Task task, boolean shorten) {
        RequiredTaskListUi listUi = new RequiredTaskListUi(task, true, shorten ? 4 : 11, 0.02f);
        super.addComponent(listUi, 0.0f, this.yPos, 1.0f, 0.5f);
        this.yPos += super.pixelsToRelativeY(170.0f);
    }

    private void addEvolveSection(boolean onlyEvolve) {
        this.addEvolveSectionTitle(onlyEvolve);
        EvolveChainUi chainUi = new EvolveChainUi(this.item.getBlueprint());
        super.addComponent(chainUi, 0.0f, this.yPos, 1.0f, 1.0f - this.yPos);
    }

    private void addEvolveSectionTitle(boolean onlyEvolve) {
        Text text = Text.newText(onlyEvolve ? EVOLVE_ONLY : EVOLVE_SECOND).center().setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, this.yPos, 1.0f);
        this.yPos += super.pixelsToRelativeY(35.0f);
    }
}

