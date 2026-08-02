/*
 * Decompiled with CFR 0.152.
 */
package checkList;

import checkList.DisplayContents;
import guis.GuiComponent;
import mainGuis.ColourPalette;
import userInterfaces.GuiScrollPanel;

public class DataDisplayUi
extends GuiScrollPanel {
    private final DisplayContents initialContents;
    private GuiComponent currentContents;

    public DataDisplayUi(DisplayContents initialContents) {
        super(ColourPalette.LIGHT_GREY, 0.2f);
        this.initialContents = initialContents;
    }

    @Override
    protected void init() {
        super.init();
        this.currentContents = this.initialContents.showInPanel(this);
    }

    protected void display(DisplayContents contents) {
        this.currentContents.remove();
        this.currentContents = contents.showInPanel(this);
    }
}

