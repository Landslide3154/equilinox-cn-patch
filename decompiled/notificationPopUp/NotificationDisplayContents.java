/*
 * Decompiled with CFR 0.152.
 */
package notificationPopUp;

import checkList.DisplayContents;
import guis.GuiComponent;
import notificationPopUp.NotificationListUi;
import userInterfaces.GuiScrollPanel;

public class NotificationDisplayContents
implements DisplayContents {
    @Override
    public GuiComponent showInPanel(GuiScrollPanel panel) {
        NotificationListUi list = new NotificationListUi();
        panel.setContents(list, (float)list.getTotalPixels() / panel.getPixelHeight());
        return list;
    }
}

