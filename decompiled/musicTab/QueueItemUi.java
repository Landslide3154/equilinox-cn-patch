/*
 * Decompiled with CFR 0.152.
 */
package musicTab;

import fontRendering.Text;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import music.MusicTrack;
import music.QueueController;
import speciesInformation.SpeciesInfoGui;
import userInterfaces.GuiButton;
import userInterfaces.GuiImage;
import userInterfaces.GuiPanel;
import userInterfaces.Listener;

public class QueueItemUi
extends GuiPanel {
    private static final float ICON_X = 0.02f;
    private static final float TEXT_X = 0.1f;
    private static final float TEXT_Y = 0.06f;
    private static final float BUTTON_SIZE = 0.6f;
    private static final float CROSS_X_POS = 0.92f;
    private static final float CROSS_SIZE = 0.6f;
    private final MusicTrack track;
    private final QueueController queue;
    private int index;
    private GuiButton button;

    public QueueItemUi(int index, MusicTrack track, QueueController queue) {
        super(ColourPalette.MIDDLE_GREY);
        this.track = track;
        this.index = index;
        this.queue = queue;
    }

    @Override
    protected void init() {
        super.init();
        this.addMusicIcon();
        this.addTitle();
        this.addButton();
    }

    public void decrementIndex() {
        --this.index;
    }

    public MusicTrack getTrack() {
        return this.track;
    }

    private void addTitle() {
        Text text = Text.newText(this.track.getName()).setFontSize(SpeciesInfoGui.FONT_SIZE).create();
        text.setColour(ColourPalette.BEIGE);
        super.addText(text, 0.1f, 0.06f, 1.0f);
    }

    private void addButton() {
        this.button = new GuiButton(GuiRepository.CROSS);
        super.addCenteredComponentYScaleY(this.button, 0.5f, 0.92f, 0.6f);
        this.button.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                QueueItemUi.this.queue.remove(QueueItemUi.this.index);
            }
        });
    }

    private void addMusicIcon() {
        GuiImage icon = new GuiImage(GuiRepository.MUSIC);
        icon.getTexture().setOverrideColour(ColourPalette.BEIGE);
        super.addCenteredComponentYScaleY(icon, 0.5f, 0.02f, 0.6f);
    }
}

