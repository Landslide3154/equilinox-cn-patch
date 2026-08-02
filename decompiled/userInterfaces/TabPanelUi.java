/*
 * Decompiled with CFR 0.152.
 */
package userInterfaces;

import basics.DisplayManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickableGroup;
import userInterfaces.GuiPanel;
import userInterfaces.TabContent;
import userInterfaces.TextButtonUi;

public class TabPanelUi
extends GuiComponent {
    private static final float FONT_SIZE = UiSettings.LARGE_FONT;
    private static final int TITLE_HEIGHT_PIXELS = 25;
    private static final int CONTENT_TOP_GAP = 4;
    private static final int CONTENT_Y_PIXELS = 29;
    private static final int TAB_GAP = 2;
    private final TextButtonUi[] tabs;
    private final TabContent[] contents;
    private GuiComponent[] shownContents;

    public TabPanelUi(TabContent[] tabContents) {
        this.contents = tabContents;
        this.tabs = new TextButtonUi[tabContents.length];
        int i = 0;
        while (i < this.tabs.length) {
            this.tabs[i] = new TextButtonUi(tabContents[i].name, ColourPalette.LIGHT_GREY, FONT_SIZE, ColourPalette.GREEN, 0.2f, 1.0f);
            ++i;
        }
    }

    @Override
    protected void init() {
        super.init();
        this.addTabs();
        this.addContents();
        this.addListeners();
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

    public TextButtonUi getTab(int index) {
        return this.tabs[index];
    }

    private void addTabs() {
        float xPos = 0.0f;
        float pixelWidth = 2.0f / super.getPixelWidth();
        float width = (1.0f - (float)(this.tabs.length - 1) * pixelWidth) / (float)this.tabs.length;
        GuiClickableGroup group = new GuiClickableGroup(true);
        boolean first = true;
        TextButtonUi[] textButtonUiArray = this.tabs;
        int n = this.tabs.length;
        int n2 = 0;
        while (n2 < n) {
            TextButtonUi button = textButtonUiArray[n2];
            super.addComponent(button, xPos, 0.0f, width, 25.0f / super.getPixelHeight());
            group.addButton(button, first);
            first = false;
            xPos += width + pixelWidth;
            ++n2;
        }
    }

    private void addListeners() {
        int i = 0;
        while (i < this.tabs.length) {
            final int index = i;
            this.tabs[i].addListener(new ClickListener(){

                @Override
                public void eventOccurred(GuiClickEvent event) {
                    if (event.toggleChange) {
                        TabPanelUi.this.shownContents[index].show(event.isToggleOn());
                    }
                }
            });
            ++i;
        }
    }

    private void addContents() {
        float yPos = 29.0f / ((float)DisplayManager.getUiHeight() * super.getScale().y);
        boolean first = true;
        this.shownContents = new GuiComponent[this.contents.length];
        int i = 0;
        while (i < this.contents.length) {
            GuiPanel panel = new GuiPanel(ColourPalette.LIGHT_GREY, 0.2f);
            panel.addComponent(this.contents[i].content, 0.0f, 0.0f, 1.0f, 1.0f);
            super.addComponent(panel, 0.0f, yPos, 1.0f, 1.0f - yPos);
            panel.show(first);
            this.shownContents[i] = panel;
            first = false;
            ++i;
        }
    }
}

