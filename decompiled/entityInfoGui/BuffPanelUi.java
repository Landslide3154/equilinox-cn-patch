/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import entityInfoGui.BuffDescriptionUi;
import entityInfoGui.BuffUi;
import entityInfoGui.EntityInfoGui;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.List;
import languages.GameText;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.TextStatInfo;

public class BuffPanelUi
extends GuiComponent {
    private static final String NO_BUFF = GameText.getText(36);
    private final List<TextStatInfo> buffs;
    private final float yGap;
    private final EntityInfoGui entityInfoUi;
    private BuffDescriptionUi extraPanel;
    private TextStatInfo newInfo;
    private TextStatInfo oldInfo;

    protected BuffPanelUi(List<TextStatInfo> buffs, int numberOfLines, EntityInfoGui entityInfoUi) {
        this.buffs = buffs;
        this.yGap = 1.0f / (float)numberOfLines;
        this.entityInfoUi = entityInfoUi;
    }

    @Override
    protected void init() {
        super.init();
        if (this.buffs.isEmpty()) {
            this.addNoBuffMessage();
        } else {
            this.addBuffs();
        }
    }

    protected void notifyMouseOver(TextStatInfo extraInfo) {
        this.newInfo = extraInfo;
    }

    @Override
    public void remove() {
        super.remove();
        this.entityInfoUi.removeSecondPanel();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        if (this.extraPanel != null && this.newInfo != this.oldInfo) {
            this.updateInfo();
        } else if (this.extraPanel == null && this.newInfo != null) {
            this.showNewInfo();
        }
        this.oldInfo = this.newInfo;
        this.newInfo = null;
    }

    private void showNewInfo() {
        this.extraPanel = new BuffDescriptionUi(this.newInfo.description);
        this.entityInfoUi.showSecondPanel(this.extraPanel);
    }

    private void updateInfo() {
        if (this.newInfo == null) {
            this.entityInfoUi.removeSecondPanel();
            this.extraPanel = null;
        } else {
            this.extraPanel.setDescription(this.newInfo.description);
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addNoBuffMessage() {
        Text text = Text.newText(NO_BUFF).center().setFontSize(EntityInfoGui.FONT_SIZE).create();
        text.setColour(ColourPalette.BEIGE);
        super.addText(text, 0.0f, 0.0f, 1.0f);
    }

    private void addBuffs() {
        float y = 0.0f;
        for (TextStatInfo buff : this.buffs) {
            super.addComponent(new BuffUi(buff, this), 0.0f, y, 1.0f, this.yGap);
            y += this.yGap;
        }
    }
}

