/*
 * Decompiled with CFR 0.152.
 */
package taskUi;

import extraInfoGui.ExtraInfoContent;
import extraInfoGui.ExtraToolbarGui;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import languages.ComplexString;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import taskUi.ClaimButtonGui;
import taskUi.NotifyBellUi;
import taskUi.RequiredTaskListUi;
import taskUi.TaskDescriptionGui;
import taskUi.TaskReqInfoGui;
import taskUi.TaskRewardGui;
import taskUi.TaskUi;
import tasks.Task;
import userInterfaces.ClickListener;
import userInterfaces.GuiButton;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiImage;
import userInterfaces.Listener;
import userInterfaces.TitlePanelUi;

public class TaskInfoFrameUi
extends ExtraInfoContent {
    private static final String DESCRIPTION = GameText.getText(147);
    private static final String REQUIREMENTS = GameText.getText(148);
    private static final String REWARDS = GameText.getText(149);
    private static final ComplexString LOCK_MESSAGE = GameText.getComplexText(150);
    private static final float X_PAD = ExtraToolbarGui.X_START;
    private static final float Y_GAP = 0.03f;
    private static final float Y_PAD = 0.03f;
    private static final float Y_PAD_BOTTOM = 0.04f;
    protected static final int TEXT_GAP_PIXELS = 8;
    private static final float DESC_WEIGHT = 1.02f;
    private static final float REQS_WEIGHT = 1.3f;
    private static final float REWARDS_WEIGHT = 1.0f;
    private static final float BUTTON_WEIGHT = 0.25f;
    private static final float TOTAL_WEIGHT = 3.57f;
    private static final float WEIGHT_FACTOR = 0.23529413f;
    private static final float DESC_HEIGHT = 0.24000001f;
    private static final float REQS_HEIGHT = 0.30588236f;
    private static final float REWARDS_HEIGHT = 0.23529413f;
    private static final float BUTTON_HEIGHT = 0.058823533f;
    private static final float CONTENT_WIDTH = 1.0f - 2.0f * X_PAD;
    private static final float DESC_Y = 0.03f;
    private static final float REQS_Y = 0.3f;
    private static final float REWARDS_Y = 0.6358824f;
    private static final float BUTTON_Y = 0.90117645f;
    private Task task;

    public TaskInfoFrameUi(Task task) {
        this.task = task;
    }

    @Override
    protected void init() {
        super.init();
        if (this.task.isLocked()) {
            this.addLockedMessage();
            this.addLockedIcon();
            this.addRequiredTasksList();
        } else {
            this.addDescriptionPanel();
            this.addRequirementsPanel();
            this.addRewardsPanel();
            this.addButton();
            if (this.task.isAutoCollect()) {
                this.addNotifyBell();
            }
        }
        this.turnOnTaskUi();
    }

    @Override
    public void close() {
        TaskUi taskUi = this.task.getCurrentUi();
        if (taskUi != null) {
            taskUi.release();
        }
    }

    private void turnOnTaskUi() {
        TaskUi taskUi = this.task.getCurrentUi();
        if (taskUi != null) {
            taskUi.setOn();
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

    private void addLockedIcon() {
        GuiImage icon = new GuiImage(GuiRepository.LOCKED);
        icon.getTexture().setOverrideColour(ColourPalette.BEIGE);
        super.addCenteredComponentX(icon, 0.5f, 0.07f, 0.1f);
    }

    private void addLockedMessage() {
        Text lockedText = Text.newText(LOCK_MESSAGE.getString("\"" + this.task.name + "\"")).setFontSize(UiSettings.NORM_FONT).center().create();
        lockedText.setColour(ColourPalette.BEIGE);
        super.addText(lockedText, 0.05f, 0.2f, 0.9f);
    }

    private void addRequiredTasksList() {
        RequiredTaskListUi listUi = new RequiredTaskListUi(this.task);
        super.addComponent(listUi, 0.0f, 0.35f, 1.0f, 0.5f);
    }

    private void addDescriptionPanel() {
        TitlePanelUi descPanel = new TitlePanelUi(DESCRIPTION, new TaskDescriptionGui(this.task.description));
        super.addComponent(descPanel, X_PAD, 0.03f, CONTENT_WIDTH, 0.24000001f);
        if (this.task.getLinkedHelpTab() != null) {
            GuiButton button = new GuiButton(GuiRepository.HELP_LIL_ICON);
            button.getGuiTexture().setOverrideColour(ColourPalette.BEIGE);
            button.setPreferredPixelSize(20);
            button.addListener(new Listener(){

                @Override
                public void eventOccurred(boolean on) {
                    if (on) {
                        EquilinoxGuis.getToolBar().openHelpUi(TaskInfoFrameUi.this.task.getLinkedHelpTab());
                    }
                }
            });
            super.addPixelComp(button, X_PAD + CONTENT_WIDTH - super.pixelsToRelativeX(20.0f), 0.035f);
        }
    }

    private void addRequirementsPanel() {
        TitlePanelUi reqPanel = new TitlePanelUi(REQUIREMENTS, new TaskReqInfoGui(this.task.getRequirements(false)));
        super.addComponent(reqPanel, X_PAD, 0.3f, CONTENT_WIDTH, 0.30588236f);
    }

    private void addRewardsPanel() {
        TitlePanelUi rewardPanel = new TitlePanelUi(REWARDS, new TaskRewardGui(this.task.getRewards()));
        super.addComponent(rewardPanel, X_PAD, 0.6358824f, CONTENT_WIDTH, 0.23529413f);
    }

    private void addButton() {
        ClaimButtonGui claimButton = new ClaimButtonGui(this.task, this);
        claimButton.setPreferredAspectRatio(6.0f);
        super.addCenteredComponentX(claimButton, 0.5f, 0.90117645f, 0.058823533f);
    }

    protected void addNotifyBell() {
        NotifyBellUi bellUi = new NotifyBellUi(this.task.notificationsOn());
        bellUi.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    TaskInfoFrameUi.this.task.setNotify(true);
                } else if (event.isToggleOff()) {
                    TaskInfoFrameUi.this.task.setNotify(false);
                }
            }
        });
        super.addComponentY(bellUi, 0.7f, 0.90117645f, 0.058823533f);
    }
}

