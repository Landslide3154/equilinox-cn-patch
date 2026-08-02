/*
 * Decompiled with CFR 0.152.
 */
package helpUi;

import checkList.DataHeaderUi;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import helpUi.HelpDisplayUi;
import helpUi.HelpPanelContent;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickableGroup;

public class TabbedScrollPanel
extends GuiComponent {
    protected static final int GAP = 3;
    protected static final int HEADER_HEIGHT = 25;
    private static final float HEADER_WIDTH = 0.25f;
    private final List<HelpPanelContent> content;
    private HelpDisplayUi displayUi;
    private int startTab;

    public TabbedScrollPanel(List<HelpPanelContent> content, int startTab) {
        this.content = content;
        this.startTab = startTab;
    }

    @Override
    protected void init() {
        super.init();
        this.addDisplayPanel();
        this.addHeaders();
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

    private void addHeaders() {
        float headerSpacing = 28.0f / super.getPixelHeight();
        float yPos = 0.0f;
        GuiClickableGroup group = new GuiClickableGroup(true);
        int i = 0;
        while (i < this.content.size()) {
            HelpPanelContent helpInfo = this.content.get(i);
            this.addHeader(group, helpInfo.title, yPos, helpInfo, i == this.startTab);
            yPos += headerSpacing;
            ++i;
        }
    }

    private void addHeader(GuiClickableGroup group, String header, float yPos, final HelpPanelContent panelContents, boolean first) {
        DataHeaderUi headerUi = new DataHeaderUi(header);
        float height = 25.0f / super.getPixelHeight();
        super.addComponent(headerUi, 0.0f, yPos, 0.25f, height);
        group.addButton(headerUi, first);
        headerUi.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    TabbedScrollPanel.this.displayUi.display(panelContents);
                }
            }
        });
    }

    private void addDisplayPanel() {
        this.displayUi = new HelpDisplayUi(this.content.get(this.startTab));
        float xPos = 0.25f + 3.0f / super.getPixelWidth();
        super.addComponent(this.displayUi, xPos, 0.0f, 1.0f - xPos, 1.0f);
    }
}

