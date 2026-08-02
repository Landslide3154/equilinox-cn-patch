/*
 * Decompiled with CFR 0.152.
 */
package musicTab;

import audio.SoundMaestro;
import fontRendering.Text;
import mainGuis.ColourPalette;
import mainGuis.GuiRepository;
import music.MusicTrack;
import music.QueueController;
import speciesInformation.SpeciesInfoGui;
import toolbox.Colour;
import userInterfaces.GuiButton;
import userInterfaces.GuiImage;
import userInterfaces.GuiPanel;
import userInterfaces.Listener;

public class TrackUi
extends GuiPanel {
    private static final Colour GREEN_COL = new Colour(96.0f, 147.0f, 111.0f, true);
    private static final float ICON_X = 0.02f;
    private static final float TEXT_X = 0.1f;
    private static final float TEXT_Y = 0.06f;
    private static final float PLAY_X_POS = 0.92f;
    private static final float MOVE_X_POS = 0.83f;
    private static final float BUTTON_SIZE = 0.6f;
    private MusicTrack track;
    private QueueController queue;
    private GuiImage lockIcon;

    public TrackUi(MusicTrack track, QueueController queue) {
        super(ColourPalette.MIDDLE_GREY);
        this.track = track;
        this.queue = queue;
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
        if (this.track.needsUiUpdate()) {
            this.setUnlocked();
        }
        if (SoundMaestro.getMusicPlayer().getCurrentlyPlaying() == this.track) {
            super.setColour(GREEN_COL);
        } else {
            this.setBackColour();
        }
    }

    @Override
    protected void init() {
        super.init();
        this.addMusicIcon();
        this.setBackColour();
        this.addName();
        if (!this.track.isLocked()) {
            this.addQueueAddButton();
            this.addPlayButton();
        } else {
            this.addLockIcon();
        }
        this.track.notifyUiUpdated();
    }

    private void setUnlocked() {
        if (this.lockIcon != null && !this.track.isLocked()) {
            this.addQueueAddButton();
            this.addPlayButton();
            this.lockIcon.remove();
            this.lockIcon = null;
        }
        this.track.notifyUiUpdated();
    }

    private void addName() {
        Text text = Text.newText(this.track.getName()).setFontSize(SpeciesInfoGui.FONT_SIZE).create();
        text.setColour(ColourPalette.BEIGE);
        super.addText(text, 0.1f, 0.06f, 1.0f);
    }

    private void addPlayButton() {
        GuiButton button = new GuiButton(GuiRepository.PLAY);
        super.addCenteredComponentYScaleY(button, 0.5f, 0.92f, 0.6f);
        button.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                SoundMaestro.getMusicPlayer().forcePlay(TrackUi.this.track, true);
            }
        });
    }

    private void addQueueAddButton() {
        GuiButton button = new GuiButton(GuiRepository.WAIT);
        super.addCenteredComponentYScaleY(button, 0.5f, 0.83f, 0.6f);
        button.addListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                TrackUi.this.queue.add(TrackUi.this.track);
            }
        });
    }

    private void addMusicIcon() {
        GuiImage icon = new GuiImage(GuiRepository.MUSIC);
        icon.getTexture().setOverrideColour(ColourPalette.BEIGE);
        super.addCenteredComponentYScaleY(icon, 0.5f, 0.02f, 0.6f);
    }

    private void addLockIcon() {
        this.lockIcon = new GuiImage(GuiRepository.LOCKED);
        super.addCenteredComponentYScaleY(this.lockIcon, 0.5f, 0.92f, 0.6f);
    }

    private void setBackColour() {
        super.setColour(this.track.isLocked() ? ColourPalette.LOCKED_BACKGROUND : ColourPalette.MIDDLE_GREY);
    }
}

