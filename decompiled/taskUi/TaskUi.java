/*
 * Decompiled with CFR 0.152.
 */
package taskUi;

import audio.SoundMaestro;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiTexture;
import languages.ComplexString;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import taskUi.TaskInformationGui;
import taskUi.TaskReqUi;
import tasks.Task;
import tasks.TaskRequirement;
import tasks.TaskState;
import toolbox.Colour;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;
import userInterfaces.GuiImage;
import visualFxDrivers.ConstantDriver;

public class TaskUi
extends GuiClickable {
    private static final ComplexString MORE_TEXT = GameText.getComplexText(151);
    private static final int SHOWN_REQ_COUNT = 2;
    private static final Colour HL_COLOUR = ColourPalette.BEIGE.duplicate().scale(1.2f);
    private static final Colour HL_LOCK_COLOUR = ColourPalette.BRIGHT_RED.duplicate().scale(1.2f);
    private static final float WOBBLE_FACTOR = 1.1f;
    private static final float WOBBLE_TIME = 0.5f;
    private static final float BOUNCE_TIME = 0.35f;
    private static final float BAR_SECTION_WEIGHT = 0.15f;
    private static final float LOCK_ICON_SIZE = 0.3f;
    private static final float REQ_WEIGHT = 0.25f;
    private static final float MORE_TEXT_WEIGHT = 0.1f;
    private static final float Y_PAD_WEIGHT = 0.025f;
    private static final float Y_PAD_B_WEIGHT = 0.03f;
    public static final float FULL_WEIGHT = 0.80499995f;
    public static final float ASPECT_RATIO = 1.2422361f;
    public static final int MIN_PIXELS_WIDE = 170;
    private static final float PIN_WIDTH = 0.15f;
    private static final float PIN_OFFSET = 0.05f;
    private static final float REPEAT_HEIGHT = 0.6f;
    private static final float REPEAT_Y = 0.19999999f;
    private static final float REPEAT_X = 0.88f;
    private static final float TITLE_Y = 0.01863354f;
    private static final float FIRST_SECTION_Y = 0.18633543f;
    private static final float CONTENT_Y = 0.21739134f;
    private static final float REQ_HEIGHT = 0.31055903f;
    private static final float MORE_TEXT_Y = 0.8509318f;
    public static final float X_PAD = 0.05f;
    private static final Colour REQ_SECTION_COL = ColourPalette.BEIGE;
    private static final float FONT_CONSTANT = 0.0025424f;
    private static final float SCALE_UP_FACTOR = 1.08f;
    private float fontSize;
    private Task task;
    private TaskState displayedState;
    private GuiTexture top;
    private GuiTexture name;
    private GuiImage pin;
    private boolean displayingLocked = false;
    private GuiImage lockedIcon;
    private Text nameText;
    private GuiTexture repeatTexture;

    public TaskUi(Task task) {
        super(1.08f);
        this.task = task;
        if (task.needsChecking()) {
            task.check();
        }
        this.displayedState = task.getState();
        this.initTextures();
    }

    @Override
    protected void delete() {
        this.task.linkUi(null);
        super.delete();
    }

    @Override
    public void release() {
        super.release();
        this.top.setOverrideColour(this.task.isLocked() ? ColourPalette.FLAT_RED : REQ_SECTION_COL);
    }

    @Override
    protected void init() {
        this.fontSize = UiSettings.NORM_FONT;
        this.addNameText();
        this.addRepeatIcon();
        this.addPinIcon();
        if (this.task.isLocked()) {
            this.addLockedIcon();
            this.displayingLocked = true;
        } else {
            this.addRequirementGuis();
            this.addMoreText();
            if (this.task.getState() == TaskState.CLAIM_REWARD) {
                super.wobble(1.1f, 0.5f);
            }
        }
        this.addClickListener();
        this.task.linkUi(this);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.name.setPosition(position.x, position.y, scale.x, scale.y * 0.18633543f);
        this.top.setPosition(position.x, position.y + 0.18633543f * scale.y, scale.x, 0.81366456f * scale.y);
    }

    @Override
    protected void updateSelf() {
        this.top.update();
        this.name.update();
        if (this.displayingLocked && !this.task.isLocked()) {
            this.switchToUnlocked();
        }
        this.task.updateState();
        if (this.task.alreadyCompleted()) {
            this.nameText.setColour(ColourPalette.BEIGE);
            this.repeatTexture.setOverrideColour(ColourPalette.BEIGE);
        }
        this.updateStateAppearance();
        super.updateSelf();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.name);
        data.addTexture(this.getLevel(), this.top);
    }

    @Override
    protected void setOn() {
        super.setOn();
        this.top.setOverrideColour(this.task.isLocked() ? HL_LOCK_COLOUR : HL_COLOUR);
    }

    private void switchToUnlocked() {
        this.lockedIcon.remove();
        this.displayingLocked = false;
        this.top.setOverrideColour(REQ_SECTION_COL);
        this.addRequirementGuis();
        this.addMoreText();
        super.bounce(0.35f, 1.1f);
    }

    private void addLockedIcon() {
        this.lockedIcon = new GuiImage(GuiRepository.LOCKED);
        super.addCenteredComponent(this.lockedIcon, 0.5f, 0.55f, 0.3f);
    }

    private void initTextures() {
        this.name = new GuiTexture(GuiRepository.BLOCK);
        this.name.setOverrideColour(this.displayedState.colour);
        this.top = new GuiTexture(GuiRepository.BLOCK);
        this.top.setOverrideColour(this.task.isLocked() ? ColourPalette.FLAT_RED : REQ_SECTION_COL);
        this.top.setAlphaDriver(new ConstantDriver(0.2f));
    }

    private void addNameText() {
        this.nameText = Text.newText(this.task.name).center().setFontSize(UiSettings.NORM_FONT).create();
        this.nameText.setColour(this.task.alreadyCompleted() ? ColourPalette.BEIGE : ColourPalette.WHITE);
        super.addText(this.nameText, 0.0f, 0.01863354f, 1.0f);
    }

    private void addRequirementGuis() {
        float yPos = 0.21739134f;
        int placesLeft = 2;
        for (TaskRequirement req : this.task.getRequirements(this.task.getRequirementCount() > 2)) {
            TaskReqUi reqGui = new TaskReqUi(req, this.fontSize);
            super.addComponent(reqGui, 0.05f, yPos, 0.9f, 0.31055903f);
            yPos += 0.31055903f;
            if (--placesLeft == 0) break;
        }
    }

    private void addMoreText() {
        int count = this.task.getRequirementCount() - 2;
        if (count > 0) {
            Text text = Text.newText(MORE_TEXT.getString(Integer.toString(count))).center().setFontSize(this.fontSize).create();
            text.setColour(ColourPalette.BEIGE);
            super.addText(text, 0.0f, 0.8509318f, 1.0f);
        }
    }

    private void addPinIcon() {
        if (this.task.isPinned()) {
            this.pin = new GuiImage(GuiRepository.PIN);
            super.addComponentX(this.pin, -0.05f, -0.05f, 0.15f);
        }
    }

    private void addRepeatIcon() {
        if (this.task.isRepeatable()) {
            GuiImage repeatIcon = new GuiImage(GuiRepository.REPEAT);
            this.repeatTexture = repeatIcon.getTexture();
            this.repeatTexture.setOverrideColour(this.task.isAutoCollect() ? ColourPalette.BEIGE : ColourPalette.WHITE);
            super.addComponentY(repeatIcon, 0.88f, 0.037267085f, 0.11180126f);
        }
    }

    private void addClickListener() {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    SoundMaestro.playSystemSound(GuiSounds.SELECT);
                    TaskInformationGui.openTaskInfo(TaskUi.this.task);
                }
            }
        });
    }

    private void pinTask() {
        this.task.pin(!this.task.isPinned());
        if (this.task.isPinned()) {
            this.addPinIcon();
        } else {
            this.pin.remove();
        }
    }

    private void updateStateAppearance() {
        if (this.task.getState() != this.displayedState) {
            this.displayedState = this.task.getState();
            this.name.setOverrideColour(this.displayedState.colour);
            if (this.task.getState() == TaskState.CLAIM_REWARD) {
                super.wobble(1.1f, 0.5f);
            }
        }
    }
}

