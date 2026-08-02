/*
 * Decompiled with CFR 0.152.
 */
package checkList;

import blueprints.Blueprint;
import checkList.ListElement;
import fontRendering.Text;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.Collections;
import java.util.List;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ClickListener;
import userInterfaces.GuiClickEvent;
import userInterfaces.GuiImage;
import userInterfaces.IconButtonUi;
import userInterfaces.Listener;

public class ListUi
extends GuiComponent {
    private static final String IN_WORLD = GameText.getText(884);
    private static final int TOP_PAD_PIXELS = 4;
    private static final int TITLE_HEIGHT_PIXELS = 40;
    private static final int PROGRESS_HEIGHT_PIXELS = 25;
    private static final int LINE_PAD_PIXELS = 15;
    protected static final int TOP_SECTION = 84;
    private static final int LINE_HEIGHT = 1;
    private static final float LINE_WIDTH = 0.9f;
    private static final int ICON_PAD_PIXELS = 1;
    protected static final int BOTTOM_PAD_PIXELS = 20;
    protected static final int ENTRY_HEIGHT_PIXELS = 28;
    private static final float TEXT_X = 0.15f;
    private static final float COUNT_TEXT_X = 0.67f;
    private static final float GAP_X = 0.025f;
    private static final float WORD_TEXT_X = 0.695f;
    private static final float BUTTON_X = 0.895f;
    private static final int BUTTON_Y_OFF = 2;
    private static final float ICON_X = 0.03f;
    private final List<ListElement> data;
    private final String title;

    public ListUi(String title, List<ListElement> data) {
        Collections.sort(data);
        this.data = data;
        this.title = title;
    }

    @Override
    protected void init() {
        super.init();
        float yPos = super.pixelsToRelativeY(4.0f);
        this.addTitle(yPos);
        this.addLine(yPos += super.pixelsToRelativeY(65.0f));
        yPos += super.pixelsToRelativeY(15.0f);
        float gap = 28.0f / super.getPixelHeight();
        int unlockedCount = 0;
        for (ListElement element : this.data) {
            this.addEntry(element, yPos);
            unlockedCount += element.isChecked() ? 1 : 0;
            yPos += gap;
        }
        yPos = super.pixelsToRelativeY(44.0f);
        this.addProgressText(yPos, unlockedCount, this.data.size());
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

    private void addTitle(float yStart) {
        Text titleText = Text.newText(this.title).center().setFontSize(UiSettings.V_BIG_FONT).create();
        titleText.setColour(ColourPalette.WHITE);
        super.addText(titleText, 0.0f, yStart, 1.0f);
    }

    private void addProgressText(float yStart, int unlocked, int total) {
        String progress = ListUi.getProgressString(unlocked, total);
        Text progressText = Text.newText(progress).center().setFontSize(UiSettings.NORM_FONT).create();
        progressText.setColour(ColourPalette.BEIGE);
        super.addText(progressText, 0.0f, yStart, 1.0f);
    }

    private void addLine(float yStart) {
        GuiImage image = new GuiImage(GuiRepository.BLOCK);
        image.getTexture().setOverrideColour(ColourPalette.LIGHT_GREY);
        super.addComponent(image, 0.050000012f, yStart, 0.9f, super.pixelsToRelativeY(1.0f));
    }

    private void addEntry(ListElement element, float yPos) {
        this.addText(element.getName(), element.isChecked(), yPos);
        this.addIcon(element.isChecked(), yPos);
        this.addCountText(element.getCount(), element.getSpecies(), yPos, element.getListener());
    }

    private void addText(String stringText, boolean complete, float yPos) {
        Text text = Text.newText(stringText).setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(complete ? ColourPalette.GREEN : ColourPalette.WHITE);
        super.addText(text, 0.15f, yPos, 1.0f);
    }

    private void addIcon(boolean complete, float yPos) {
        GuiImage image = new GuiImage(complete ? GuiRepository.TICK_FILL : GuiRepository.TICK_EMPTY);
        image.setPreferredPixelSize(18);
        image.getTexture().setOverrideColour(complete ? ColourPalette.GREEN : ColourPalette.WHITE);
        float offset = 1.0f / super.getPixelHeight();
        super.addPixelComp(image, 0.03f, yPos + offset);
    }

    private void addCountText(int count, Blueprint species, float yPos, Listener listener) {
        if (count <= 0) {
            return;
        }
        this.addButton(species, yPos, listener);
        Text text = Text.newText(Integer.toString(count)).rightAlign().setFontSize(UiSettings.NORM_FONT).create();
        text.setColour(ColourPalette.BEIGE);
        super.addText(text, 0.0f, yPos, 0.67f);
        Text words = Text.newText(IN_WORLD).setFontSize(UiSettings.NORM_FONT).create();
        words.setColour(ColourPalette.LIGHT_GREY);
        super.addText(words, 0.695f, yPos, 1.0f);
    }

    private void addButton(Blueprint species, float yPos, final Listener listener) {
        if (species == null) {
            return;
        }
        IconButtonUi button = new IconButtonUi(GuiRepository.ARROW_OFF_2);
        button.setPreferredPixelSize(18);
        button.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.isLeftClick()) {
                    listener.eventOccurred(true);
                }
            }
        });
        super.addPixelComp(button, 0.895f, yPos + super.pixelsToRelativeY(2.0f));
    }

    private static String getProgressString(int unlocked, int total) {
        return "(" + unlocked + "/" + total + ")";
    }
}

