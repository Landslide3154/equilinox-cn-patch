/*
 * Decompiled with CFR 0.152.
 */
package evolveStatusOverview;

import checkList.DisplayContents;
import evolveStatusOverview.EvolveStatusList;
import guis.GuiComponent;
import userInterfaces.GuiScrollPanel;

public class EvolveDisplayContents
implements DisplayContents {
    @Override
    public GuiComponent showInPanel(GuiScrollPanel panel) {
        EvolveStatusList evolveStatusList = new EvolveStatusList();
        panel.setContents(evolveStatusList, (float)evolveStatusList.getHeightInPixels() / panel.getPixelHeight());
        return evolveStatusList;
    }
}

