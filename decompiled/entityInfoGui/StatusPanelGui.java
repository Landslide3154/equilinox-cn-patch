/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import entityInfoGui.InfoType;
import entityInfoGui.PopUpInfoGui;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;

public class StatusPanelGui
extends GuiComponent {
    private Map<InfoType, List<PopUpInfoGui>> sortedInfo = this.createEmptyMap();
    private float yGap;
    private boolean empty = false;

    public StatusPanelGui(List<PopUpInfoGui> info, int lineCount) {
        this.sortInfo(info);
        this.yGap = 1.0f / (float)lineCount;
    }

    public StatusPanelGui(List<PopUpInfoGui> info, int lineCount, boolean buildMode) {
        if (buildMode) {
            this.yGap = 0.2f;
            this.empty = true;
        } else {
            this.sortInfo(info);
            this.yGap = 1.0f / (float)lineCount;
        }
    }

    @Override
    protected void init() {
        if (this.empty) {
            this.addEmptyText();
            return;
        }
        float yPos = 0.0f;
        for (InfoType type : this.sortedInfo.keySet()) {
            for (PopUpInfoGui info : this.sortedInfo.get((Object)type)) {
                super.addComponent(info, 0.0f, yPos, 1.0f, this.yGap);
                yPos += this.yGap;
            }
        }
    }

    private void addEmptyText() {
        Text text = Text.newText("No stats to show.").center().setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.BEIGE);
        super.addText(text, 0.0f, 0.0f, 1.0f);
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

    private Map<InfoType, List<PopUpInfoGui>> createEmptyMap() {
        LinkedHashMap<InfoType, List<PopUpInfoGui>> infoMap = new LinkedHashMap<InfoType, List<PopUpInfoGui>>();
        InfoType[] infoTypeArray = InfoType.values();
        int n = infoTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            InfoType type = infoTypeArray[n2];
            infoMap.put(type, new ArrayList());
            ++n2;
        }
        return infoMap;
    }

    private void sortInfo(List<PopUpInfoGui> infoGuis) {
        for (PopUpInfoGui info : infoGuis) {
            this.sortedInfo.get((Object)info.getType()).add(info);
        }
    }
}

