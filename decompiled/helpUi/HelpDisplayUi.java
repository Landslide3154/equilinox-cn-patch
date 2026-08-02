/*
 * Decompiled with CFR 0.152.
 */
package helpUi;

import helpUi.HelpPanelContent;
import helpUi.TutorialPageUi;
import mainGuis.ColourPalette;
import userInterfaces.GuiScrollPanel;

public class HelpDisplayUi
extends GuiScrollPanel {
    private TutorialPageUi listUi;
    private float size;

    public HelpDisplayUi(HelpPanelContent content) {
        super(ColourPalette.LIGHT_GREY, 0.2f);
        this.listUi = new TutorialPageUi(content, this);
        this.size = content.scale;
    }

    @Override
    protected void init() {
        super.init();
        super.setContents(this.listUi, this.size);
    }

    protected void display(HelpPanelContent content) {
        this.listUi.remove();
        this.listUi = new TutorialPageUi(content, this);
        super.setContents(this.listUi, content.scale);
    }
}

