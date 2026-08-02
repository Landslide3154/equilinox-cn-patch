/*
 * Decompiled with CFR 0.152.
 */
package checkList;

import checkList.DisplayContents;
import checkList.ListElement;
import checkList.ListUi;
import guis.GuiComponent;
import java.util.List;
import userInterfaces.GuiScrollPanel;

public class ListDisplayContents
implements DisplayContents {
    private final String title;
    private final List<ListElement> listElements;

    public ListDisplayContents(String title, List<ListElement> listElements) {
        this.title = title;
        this.listElements = listElements;
    }

    @Override
    public GuiComponent showInPanel(GuiScrollPanel panel) {
        ListUi listUi = new ListUi(this.title, this.listElements);
        int size = this.listElements.size();
        int reqPixelSize = 84 + size * 28 + 20;
        panel.setContents(listUi, (float)reqPixelSize / panel.getPixelHeight());
        return listUi;
    }
}

