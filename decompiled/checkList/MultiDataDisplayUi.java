/*
 * Decompiled with CFR 0.152.
 */
package checkList;

import checkList.DataDisplayUi;
import checkList.DataHeaderUi;
import checkList.DisplayContents;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.Map;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiClickableGroup;

public class MultiDataDisplayUi
extends GuiComponent {
    protected static final int GAP = 3;
    protected static final int HEADER_HEIGHT = 25;
    private static final float HEADER_WIDTH = 0.3f;
    private final Map<String, DisplayContents> data;
    private DataDisplayUi displayUi;

    public MultiDataDisplayUi(Map<String, DisplayContents> data) {
        this.data = data;
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
        boolean first = true;
        for (String header : this.data.keySet()) {
            this.addHeader(group, header, yPos, this.data.get(header), first);
            first = false;
            yPos += headerSpacing;
        }
    }

    private void addHeader(GuiClickableGroup group, String header, float yPos, final DisplayContents contents, boolean first) {
        DataHeaderUi headerUi = new DataHeaderUi(header);
        float height = 25.0f / super.getPixelHeight();
        super.addComponent(headerUi, 0.0f, yPos, 0.3f, height);
        group.addButton(headerUi, first);
        headerUi.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isToggleOn()) {
                    MultiDataDisplayUi.this.displayUi.display(contents);
                }
            }
        });
    }

    private void addDisplayPanel() {
        Map.Entry<String, DisplayContents> entry = this.data.entrySet().iterator().next();
        this.displayUi = new DataDisplayUi(entry.getValue());
        float xPos = 0.3f + 3.0f / super.getPixelWidth();
        super.addComponent(this.displayUi, xPos, 0.0f, 1.0f - xPos, 1.0f);
    }
}

