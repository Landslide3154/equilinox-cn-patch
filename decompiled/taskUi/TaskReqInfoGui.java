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
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import taskUi.RequirementProgressGui;
import tasks.TaskRequirement;

public class TaskReqInfoGui
extends GuiComponent {
    private static final float TEXT_WIDTH = 0.5f;
    private static final float CENTER_PAD = 0.05f;
    private static final float BAR_START = 0.55f;
    private static final float BAR_WIDTH = 0.39999998f;
    private static final float TEXT_WIDTH_S = 0.55f;
    private static final float CENTER_PAD_S = 0.02f;
    private static final float BAR_START_S = 0.57f;
    private static final float BAR_WIDTH_S = 0.41000003f;
    private List<TaskRequirement> requirements;

    protected TaskReqInfoGui(List<TaskRequirement> requirements) {
        this.requirements = requirements;
    }

    @Override
    protected void init() {
        float yPos = 0.0f;
        float textGap = 8.0f / (super.getScale().y * (float)DisplayManager.getUiHeight());
        for (TaskRequirement req : this.requirements) {
            float height = this.addRequirement(req, yPos);
            yPos += height + textGap;
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

    private float addRequirement(TaskRequirement req, float yPos) {
        boolean small = DisplayManager.getWidth() < 1200;
        Text text = Text.newText("- " + req.getDescription()).indent().setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, yPos, small ? 0.55f : 0.5f);
        float barStart = small ? 0.57f : 0.55f;
        float barWidth = small ? 0.41000003f : 0.39999998f;
        super.addComponent(new RequirementProgressGui(req), barStart, yPos, barWidth, 0.16f);
        return text.getHeight() / super.getScale().y;
    }
}

