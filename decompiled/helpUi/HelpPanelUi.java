/*
 * Decompiled with CFR 0.152.
 */
package helpUi;

import basics.DisplayManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import helpUi.ContentSection;
import helpUi.HelpPanelContent;
import helpUi.TabbedScrollPanel;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;

public class HelpPanelUi
extends GuiComponent {
    public static int START_PAGE = 0;
    private final TabbedScrollPanel displayUi = new TabbedScrollPanel(this.initData(), START_PAGE);

    @Override
    protected void init() {
        super.init();
        float sidePad = 30.0f / (this.getScale().x * (float)DisplayManager.getUiWidth());
        this.addDisplayUi(sidePad);
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

    private void addDisplayUi(float sidePad) {
        float pixelHeight = super.getPixelHeight();
        float yStart = 30.0f / pixelHeight;
        float gapHeight = 10.0f / pixelHeight;
        float height = 1.0f - (yStart + gapHeight);
        float width = 1.0f - 2.0f * sidePad;
        super.addComponent(this.displayUi, sidePad, yStart, width, height);
    }

    private List<HelpPanelContent> initData() {
        ArrayList<HelpPanelContent> data = new ArrayList<HelpPanelContent>();
        ContentSection[] sections = new ContentSection[]{new ContentSection(GameText.getText(1026), GuiRepository.GS1, GameText.getText(1027)), new ContentSection(GameText.getText(1028), GuiRepository.GS2, GameText.getText(1029)), new ContentSection(GameText.getText(1030), GuiRepository.GS3, GameText.getText(1031)), new ContentSection(GameText.getText(1032), GuiRepository.GS5, GameText.getText(1033)), new ContentSection(GameText.getText(1034), GuiRepository.GS4, GameText.getText(1035))};
        ContentSection[] wildlifeSections = new ContentSection[]{new ContentSection(GameText.getText(710), GuiRepository.WB1, GameText.getText(711)), new ContentSection(GameText.getText(712), GuiRepository.WB4, GameText.getText(713)), new ContentSection(GameText.getText(714), null, GameText.getText(715)), new ContentSection(GameText.getText(716), GuiRepository.WB2, GameText.getText(717)), new ContentSection(GameText.getText(718), GuiRepository.WB3, GameText.getText(719))};
        ContentSection[] dpSections = new ContentSection[]{new ContentSection(GameText.getText(722), GuiRepository.DP1, GameText.getText(723)), new ContentSection(GameText.getText(724), null, GameText.getText(725)), new ContentSection(GameText.getText(726), GuiRepository.GS2, GameText.getText(727)), new ContentSection(GameText.getText(728), null, GameText.getText(729)), new ContentSection(GameText.getText(730), null, GameText.getText(731))};
        ContentSection[] evolveSections = new ContentSection[]{new ContentSection(GameText.getText(762), GuiRepository.EVO1, GameText.getText(763)), new ContentSection(GameText.getText(764), null, GameText.getText(765)), new ContentSection(GameText.getText(766), GuiRepository.EVO2, GameText.getText(767)), new ContentSection(GameText.getText(768), GuiRepository.EVO3, GameText.getText(769)), new ContentSection(GameText.getText(770), GuiRepository.EVO4, GameText.getText(771))};
        ContentSection[] statusSections = new ContentSection[]{new ContentSection(GameText.getText(774), GuiRepository.GS3, GameText.getText(775)), new ContentSection(GameText.getText(776), GuiRepository.STATUS1, GameText.getText(777)), new ContentSection(GameText.getText(778), null, GameText.getText(779)), new ContentSection(GameText.getText(780), null, GameText.getText(781)), new ContentSection(GameText.getText(782), null, GameText.getText(783)), new ContentSection(GameText.getText(784), GuiRepository.STATUS2, GameText.getText(785))};
        ContentSection[] biomeSections = new ContentSection[]{new ContentSection(GameText.getText(734), GuiRepository.B1, GameText.getText(735)), new ContentSection(GameText.getText(736), null, GameText.getText(737)), new ContentSection(GameText.getText(738), GuiRepository.B2, GameText.getText(739))};
        ContentSection[] geneticsSection = new ContentSection[]{new ContentSection(GameText.getText(742), GuiRepository.STATUS1, GameText.getText(743)), new ContentSection(GameText.getText(744), null, GameText.getText(745)), new ContentSection(GameText.getText(746), GuiRepository.GEN1, GameText.getText(747)), new ContentSection(GameText.getText(748), GuiRepository.GEN2, GameText.getText(749))};
        ContentSection[] diseaseSections = new ContentSection[]{new ContentSection(GameText.getText(1020), GuiRepository.D1, GameText.getText(1021)), new ContentSection(GameText.getText(1022), null, GameText.getText(1023)), new ContentSection(GameText.getText(1024), GuiRepository.D2, GameText.getText(1025))};
        data.add(new HelpPanelContent(GameText.getText(750), GameText.getText(751), sections, 1.0f));
        data.add(new HelpPanelContent(GameText.getText(708), GameText.getText(709), wildlifeSections, 1.0f));
        data.add(new HelpPanelContent(GameText.getText(772), GameText.getText(773), statusSections, 1.0f));
        data.add(new HelpPanelContent(GameText.getText(720), GameText.getText(721), dpSections, 1.0f));
        data.add(new HelpPanelContent(GameText.getText(732), GameText.getText(733), biomeSections, 1.0f));
        data.add(new HelpPanelContent(GameText.getText(740), GameText.getText(741), geneticsSection, 1.0f));
        data.add(new HelpPanelContent(GameText.getText(760), GameText.getText(761), evolveSections, 1.0f));
        data.add(new HelpPanelContent(GameText.getText(1018), GameText.getText(1019), diseaseSections, 1.0f));
        return data;
    }
}

