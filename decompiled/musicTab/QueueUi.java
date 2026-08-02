/*
 * Decompiled with CFR 0.152.
 */
package musicTab;

import guis.GuiComponent;
import java.util.List;
import languages.GameText;
import mainGuis.GuiRepository;
import music.QueueController;
import musicTab.ListUi;
import toolTips.ToolTipInfo;
import userInterfaces.GuiButton;
import userInterfaces.Listener;

public class QueueUi
extends ListUi {
    private static final String TIP_TITLE = GameText.getText(997);
    private static final String TIP_DESC = GameText.getText(998);
    private static final float BUTTON_X = 0.8f;
    private QueueController controller;

    public QueueUi(QueueController controller, String title, int pixelsHigh, int gap, List<GuiComponent> components) {
        super(title, pixelsHigh, gap, components, false);
        this.controller = controller;
    }

    @Override
    protected void delete() {
        this.controller.unregisterUi(this);
        super.delete();
    }

    @Override
    protected void init() {
        super.init();
        this.addIcon();
    }

    private void addIcon() {
        GuiButton button = new GuiButton(GuiRepository.PLAYLIST, true);
        button.setToolTip(ToolTipInfo.newInfo(TIP_TITLE, TIP_DESC));
        button.setPreferredPixelSize(24);
        super.addPixelComp(button, 0.8f, super.pixelsToRelativeY(5.0f));
        if (this.controller.isRepeating()) {
            button.toggle();
        }
        button.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                QueueUi.this.controller.setRepeat(on);
            }
        });
    }
}

