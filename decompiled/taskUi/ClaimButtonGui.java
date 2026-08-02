/*
 * Decompiled with CFR 0.152.
 */
package taskUi;

import basics.DisplayManager;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiTexture;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import taskUi.TaskInfoFrameUi;
import tasks.Task;
import tasks.TaskState;
import toolbox.Colour;
import toolbox.Maths;
import toolbox.MyKeyboard;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickable;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SinWaveDriver;
import visualFxDrivers.SlideDriver;
import visualFxDrivers.ValueDriver;

public class ClaimButtonGui
extends GuiClickable {
    private static final Colour BASE = ColourPalette.BASE_BLUE;
    private static final Colour BRIGHT = ColourPalette.BASE_BLUE.duplicate().scale(1.4f);
    private static final float TEXT_Y = 0.11f;
    private static final float WOBBLE_TIME = 0.8f;
    private static final float ANIM_TIME = 0.25f;
    private static final String COMPLETE = GameText.getText(144);
    private static final String AUTO = GameText.getText(145);
    private static final String CLAIM = GameText.getText(146);
    private final Task task;
    private final TaskInfoFrameUi infoUi;
    private GuiTexture background;
    private Text text;
    private boolean unfinished;
    private Colour collectColour = ColourPalette.BASE_BLUE.duplicate();
    private ValueDriver waver = new SinWaveDriver(0.0f, 1.0f, 0.8f);
    private ValueDriver stretcher = new ConstantDriver(0.0f);
    private boolean showBackground = true;

    public ClaimButtonGui(Task task, TaskInfoFrameUi infoUi) {
        super(1.07f);
        this.task = task;
        this.infoUi = infoUi;
        this.unfinished = task.getState() == TaskState.IN_PROGRESS || task.getState() == TaskState.UNSTARTED;
        this.initTexture();
        this.initButton();
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        this.checkCheat();
        this.updateColour();
        if (this.unfinished && this.task.getState() == TaskState.CLAIM_REWARD) {
            this.switchToClaimState();
        }
        float value = this.stretcher.update(DisplayManager.getDeltaSeconds());
        float xPos = Maths.interpolate(super.getPosition().x, this.infoUi.getPosition().x, value);
        float yPos = Maths.interpolate(super.getPosition().y, super.getPosition().y + super.getScale().y * 0.5f, value);
        float xScale = Maths.interpolate(super.getScale().x, this.infoUi.getScale().x, value);
        float yScale = Maths.interpolate(super.getScale().y, 0.0f, value);
        this.background.setPosition(xPos, yPos, xScale, yScale);
    }

    private void checkCheat() {
        MyKeyboard keyboard = MyKeyboard.getKeyboard();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void init() {
        super.init();
        this.initText();
        if (this.task.getState() == TaskState.COMPLETE) {
            this.showBackground = false;
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        if (this.showBackground) {
            data.addTexture(this.getLevel(), this.background);
        }
    }

    private void switchToClaimState() {
        this.unfinished = false;
        super.block(false);
        this.background.setOverrideColour(this.collectColour);
        this.text.setColour(ColourPalette.WHITE);
    }

    private void initButton() {
        this.addListener();
        if (this.task.getState() != TaskState.CLAIM_REWARD) {
            super.block(true);
        }
    }

    private void initText() {
        String message = this.task.getState() == TaskState.COMPLETE ? COMPLETE : CLAIM;
        message = this.task.isAutoCollect() ? AUTO : message;
        this.text = Text.newText(message).center().setFontSize(UiSettings.NORM_FONT).create();
        if (this.task.isAutoCollect()) {
            this.text.setColour(ColourPalette.BEIGE);
        } else if (this.task.getState() == TaskState.CLAIM_REWARD) {
            this.text.setColour(ColourPalette.WHITE);
        } else if (this.task.getState() == TaskState.COMPLETE) {
            this.text.setColour(ColourPalette.GREEN);
        } else {
            this.text.setColour(ColourPalette.LIGHT_GREY);
        }
        super.addText(this.text, 0.0f, 0.11f, 1.0f);
    }

    private void initTexture() {
        this.background = new GuiTexture(GuiRepository.BLOCK);
        if (this.task.getState() == TaskState.COMPLETE) {
            this.background.setOverrideColour(ColourPalette.LIGHT_GREEN);
        } else if (this.task.getState() == TaskState.CLAIM_REWARD) {
            this.background.setOverrideColour(this.collectColour);
        } else {
            this.background.setOverrideColour(ColourPalette.MIDDLE_GREY);
        }
    }

    private void addListener() {
        super.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    ClaimButtonGui.this.complete();
                } else if (event.isMouseOver()) {
                    ClaimButtonGui.this.background.setOverrideColour(BRIGHT);
                } else if (event.isMouseOff()) {
                    ClaimButtonGui.this.background.setOverrideColour(ClaimButtonGui.this.collectColour);
                }
            }
        });
    }

    private void complete() {
        this.task.complete();
        super.block(true);
        if (this.task.isAutoCollect()) {
            this.background.setOverrideColour(ColourPalette.MIDDLE_GREY);
            this.text.setText(AUTO);
            this.text.setColour(ColourPalette.BEIGE);
            this.infoUi.addNotifyBell();
        } else {
            this.stretcher = new SlideDriver(0.0f, 1.0f, 0.25f);
            this.background.setOverrideColour(ColourPalette.LIGHT_GREEN);
            this.text.setText(COMPLETE);
            this.text.setColour(ColourPalette.GREEN);
        }
    }

    private void updateColour() {
        Colour.interpolateColours(BASE, BRIGHT, this.waver.update(DisplayManager.getDeltaSeconds()), this.collectColour);
    }
}

