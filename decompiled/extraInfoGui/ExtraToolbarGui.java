/*
 * Decompiled with CFR 0.152.
 */
package extraInfoGui;

import basics.DisplayManager;
import extraInfoGui.ExtraInfoGui;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import guis.GuiTexture;
import java.util.ArrayList;
import java.util.List;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import toolTips.ToolTipInfo;
import userInterfaces.ClickListener;
import userInterfaces.GuiButton;
import userInterfaces.GuiClickableGroup;
import userInterfaces.Listener;
import userInterfaces.TabButtonUi;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SlideDriver;
import visualFxDrivers.ValueDriver;

public class ExtraToolbarGui
extends GuiComponent {
    private static final float BAR_WIDTH = 15.0f / (ExtraInfoGui.WIDTH * (float)DisplayManager.getUiWidth());
    private static final float TEXT_PAD = 0.02f;
    public static final float X_START = BAR_WIDTH + 0.02f;
    private static final float NORMAL_BUTTON_WIDTH = 0.07f;
    private static final float CLOSE_BUTTON_WIDTH = 0.1f;
    private static final float SEPARATOR_WIDTH = 1.0f / (float)DisplayManager.getUiWidth();
    private Text nameText;
    private List<TabButtonUi> tabs = new ArrayList<TabButtonUi>();
    private boolean displayed = false;
    private ValueDriver yDriver;
    private GuiTexture bar;
    private GuiTexture background;
    private GuiTexture separator1;
    private GuiTexture separator2;
    private float yScale = 36.0f / (float)DisplayManager.getUiHeight();

    protected ExtraToolbarGui(ExtraInfoGui gui) {
        this.yDriver = new ConstantDriver(-this.yScale);
        GuiMaster.addComponent(this, ExtraInfoGui.X_POS, -this.yScale, ExtraInfoGui.WIDTH, this.yScale);
        this.bar = new GuiTexture(GuiRepository.BLOCK);
        this.bar.setOverrideColour(ColourPalette.GREEN);
        this.background = new GuiTexture(GuiRepository.BLOCK);
        this.background.setBlurry(true);
        this.background.setOverrideColour(ColourPalette.DARK_GREY);
        this.background.setAlphaDriver(new ConstantDriver(0.75f));
        this.separator1 = new GuiTexture(GuiRepository.BLOCK);
        this.separator1.setOverrideColour(ColourPalette.MIDDLE_GREY);
        this.separator2 = new GuiTexture(GuiRepository.BLOCK);
        this.separator2.setOverrideColour(ColourPalette.MIDDLE_GREY);
        this.addExitButton(gui);
        this.show(false);
    }

    public void addTabListener(int tabId, ClickListener listener) {
        this.tabs.get(tabId).addListener(listener);
    }

    public void display(String name, List<Texture> icons, List<ToolTipInfo> toolTips) {
        if (!this.displayed) {
            this.show(true);
            this.slide(true);
            this.displayed = true;
        }
        this.setName(name);
        this.setTabs(icons, toolTips);
        this.separator2.setPosition(this.calcSeparator2PosX(), super.getPosition().y, SEPARATOR_WIDTH, super.getScale().y);
    }

    protected void undisplay() {
        if (this.displayed) {
            this.displayed = false;
            this.slide(false);
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.bar.setPosition(position.x, position.y, scale.x * BAR_WIDTH, scale.y);
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
        this.separator1.setPosition(position.x + 0.9f * scale.x, position.y, SEPARATOR_WIDTH, scale.y);
        this.separator2.setPosition(this.calcSeparator2PosX(), position.y, SEPARATOR_WIDTH, scale.y);
    }

    @Override
    protected void updateSelf() {
        this.background.update();
        float yPos = this.yDriver.update(DisplayManager.getDeltaSeconds());
        super.setRelativeY(yPos);
        this.checkOffScreen();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
        data.addTexture(this.getLevel(), this.bar);
        data.addTexture(this.getLevel(), this.separator1);
        if (this.tabs.size() > 0) {
            data.addTexture(this.getLevel(), this.separator2);
        }
    }

    private float calcSeparator2PosX() {
        float xPos = 1.0f - (0.1f + (float)this.tabs.size() * 0.07f + super.pixelsToRelativeX(1.0f));
        return super.getPosition().x + xPos * super.getScale().x;
    }

    private void setName(String name) {
        if (this.nameText == null) {
            this.nameText = Text.newText(name).setFontSize(UiSettings.LARGE_FONT).create();
            this.nameText.setColour(ColourPalette.WHITE);
            super.addText(this.nameText, X_START, 0.19f, 1.0f);
        } else {
            this.nameText.setText(name);
        }
    }

    private void setTabs(List<Texture> tabIcons, List<ToolTipInfo> toolTips) {
        this.removeTabs();
        GuiClickableGroup group = new GuiClickableGroup(true);
        float buttonsStart = 1.0f - (0.1f + (float)tabIcons.size() * 0.07f);
        int i = 0;
        while (i < tabIcons.size()) {
            TabButtonUi button = new TabButtonUi(tabIcons.get(i), 20);
            super.addComponent(button, buttonsStart + (float)i * 0.07f, 0.0f, 0.07f, 1.0f);
            group.addButton(button, i == 0);
            button.setToolTip(toolTips.get(i));
            this.tabs.add(button);
            ++i;
        }
    }

    private void addExitButton(final ExtraInfoGui gui) {
        GuiButton exit = new GuiButton(GuiRepository.EXIT);
        exit.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                gui.close();
            }
        });
        super.addCenteredComponentX(exit, 0.95f, 0.19999999f, 0.6f);
    }

    private void checkOffScreen() {
        if (super.getRelativeY() <= -this.yScale) {
            this.removeTabs();
            super.show(false);
        }
    }

    private void removeTabs() {
        for (TabButtonUi tab : this.tabs) {
            tab.remove();
        }
        this.tabs.clear();
    }

    private void slide(boolean slideIn) {
        this.yDriver = new SlideDriver(super.getRelativeY(), slideIn ? 0.0f : -this.yScale, 0.2f);
    }
}

