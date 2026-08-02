/*
 * Decompiled with CFR 0.152.
 */
package taskUi;

import basics.DisplayManager;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import tasks.Reward;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.IconButtonUi;

public class TaskRewardGui
extends GuiComponent {
    private static final float CENTER = 0.5f;
    private static final float TOO_LOW = 0.8f;
    private static final float BUTTON_WIDTH = 0.044f;
    private static final float BUTTON_OFFSET = 0.3f;
    private final List<Reward> rewards;
    private GuiComponent component;

    protected TaskRewardGui(List<Reward> rewards) {
        this.rewards = rewards;
    }

    @Override
    protected void init() {
        float yPos = 0.0f;
        float xPos = 0.0f;
        float textGap = 8.0f / (super.getScale().y * (float)DisplayManager.getUiHeight());
        for (Reward reward : this.rewards) {
            Text text = Text.newText("- " + reward.getInfo()).setFontSize(UiSettings.NORM_FONT).indent().create();
            text.setColour(ColourPalette.WHITE);
            super.addText(text, xPos, yPos, 1.0f);
            if (reward.hasExtraInfo()) {
                this.addExtraInfoButton(xPos, yPos, reward);
            }
            if (!((yPos += textGap + text.getHeight() / super.getScale().y) > 0.8f)) continue;
            yPos = 0.0f;
            xPos = 0.5f;
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

    private void addExtraInfoButton(final float xPos, final float yPos, final Reward reward) {
        IconButtonUi button = new IconButtonUi(GuiRepository.INFO);
        button.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isMouseOver()) {
                    TaskRewardGui.this.createPopUp(xPos, yPos, reward);
                } else if (event.isMouseOff()) {
                    TaskRewardGui.this.deletePopUp();
                }
            }
        });
        super.addComponentX(button, xPos + 0.3f, yPos, 0.044f);
    }

    private void deletePopUp() {
        if (this.component != null) {
            this.component.remove();
            this.component = null;
        }
    }

    private void createPopUp(float xPos, float yPos, Reward reward) {
        this.deletePopUp();
        this.component = reward.addExtraInfo(this, xPos + 0.3f + 0.044f, yPos);
    }
}

