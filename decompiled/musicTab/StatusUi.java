/*
 * Decompiled with CFR 0.152.
 */
package musicTab;

import audio.SoundMaestro;
import guiRendering.GuiRenderData;
import languages.GameText;
import mainGuis.ColourPalette;
import music.MusicPlayer;
import musicTab.CurrentPlayingUi;
import musicTab.VolumeChooserUi;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ChangeListener;
import userInterfaces.GuiPanel;

public class StatusUi
extends GuiPanel {
    private static final String MUSIC_VOL = GameText.getText(84);
    private static final String SOUND_VOL = GameText.getText(85);
    private static final float TITLE_Y = 0.07f;
    private static final float GAP = 0.12f;
    private static final float HEIGHT = 0.17333333f;

    public StatusUi() {
        super(ColourPalette.BASE_BLUE, 0.2f);
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        super.updateGuiTexturePositions(position, scale);
    }

    @Override
    protected void init() {
        super.init();
        this.addCurrentlyPlayingPanel();
        this.addMusicVolumePanel();
        this.addSoundVolumePanel();
    }

    @Override
    protected void updateSelf() {
        super.updateSelf();
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        super.getGuiTextures(data);
    }

    private void addCurrentlyPlayingPanel() {
        super.addComponent(new CurrentPlayingUi(), 0.0f, 0.07f, 1.0f, 0.2f);
    }

    private void addMusicVolumePanel() {
        final MusicPlayer player = SoundMaestro.getMusicPlayer();
        super.addComponent(new VolumeChooserUi(MUSIC_VOL, player.getVolume(), new ChangeListener(){

            @Override
            public void eventOccurred(float value) {
                player.setVolume(value);
            }
        }), 0.0f, 0.41333333f, 1.0f, 0.17333333f);
    }

    private void addSoundVolumePanel() {
        super.addComponent(new VolumeChooserUi(SOUND_VOL, SoundMaestro.SOUND_VOLUME, new ChangeListener(){

            @Override
            public void eventOccurred(float value) {
                SoundMaestro.SOUND_VOLUME = value;
            }
        }), 0.0f, 0.70666665f, 1.0f, 0.17333333f);
    }
}

