/*
 * Decompiled with CFR 0.152.
 */
package bottomBar;

import bottomBar.ArrowButtonUi;
import gameManaging.GameManager;
import guiRendering.GuiRenderData;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiPanel;
import userInterfaces.Tab2ButtonUi;

public class DayNightControlUi
extends GuiPanel {
    private static final int ICON_SIZE = 18;
    private static final int PADDING = 4;
    private static final float ICON_HEIGHT = 0.7f;
    private Tab2ButtonUi sunButton;

    public DayNightControlUi() {
        super(ColourPalette.DARK_GREY, 0.75f);
    }

    @Override
    protected void init() {
        super.init();
        this.addSunButton();
        this.addLeftButton();
        this.addRightButton();
    }

    public void reset() {
        if (this.sunButton != null && this.sunButton.isToggledOn()) {
            this.sunButton.toggle();
        }
    }

    private void addSunButton() {
        this.sunButton = new Tab2ButtonUi(GuiRepository.DAY_ICON, GuiRepository.DAY_ICON_PAUSE);
        this.sunButton.setMouseOverColour(ColourPalette.LIGHT_GREY);
        this.sunButton.setOnColour(ColourPalette.WHITE);
        this.sunButton.setPreferredPixelSize(18);
        float posX = 0.5f - 0.5f * super.pixelsToRelativeX(18.0f);
        float yPos = 0.5f * (1.0f - super.pixelsToRelativeY(16.0f));
        super.addPixelComp(this.sunButton, posX, yPos);
        this.sunButton.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    GameManager.getSession().getStats().getCalendar().pauseTime(true);
                } else if (event.isToggleOff()) {
                    GameManager.getSession().getStats().getCalendar().pauseTime(false);
                }
            }
        });
    }

    private void addLeftButton() {
        ArrowButtonUi leftArrow = new ArrowButtonUi(GuiRepository.FAST_FORWARD2, false);
        leftArrow.setPreferredPixelSize(18);
        float xPos = super.pixelsToRelativeX(4.0f);
        super.addCenteredComponentYScaleY(leftArrow, 0.5f, xPos, 0.7f);
    }

    private void addRightButton() {
        ArrowButtonUi leftArrow = new ArrowButtonUi(GuiRepository.FAST_FORWARD, true);
        leftArrow.setPreferredPixelSize(18);
        float xPos = 1.0f - super.getRelativeWidthCoords(0.7f) - super.pixelsToRelativeX(4.0f);
        super.addCenteredComponentYScaleY(leftArrow, 0.5f, xPos, 0.7f);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        super.getGuiTextures(data);
    }
}

