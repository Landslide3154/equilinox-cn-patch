/*
 * Decompiled with CFR 0.152.
 */
package musicTab;

import audio.SoundMaestro;
import fontRendering.Text;
import gridLayout.GridGui;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import languages.GameText;
import mainGuis.ColourPalette;
import music.MusicTrack;
import org.lwjgl.util.vector.Vector2f;
import speciesInformation.SpeciesInfoGui;

public class CurrentPlayingUi
extends GuiComponent {
    private static final float NAME_Y = 0.5f;
    private static final float NAME_FONT_FACTOR = 1.25f;
    private static final String CURRENT_PLAYING = GameText.getText(82);
    private MusicTrack currentTrack;
    private Text name;

    @Override
    protected void init() {
        super.init();
        this.addTitle();
        this.addName();
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
        MusicTrack actualTrack = SoundMaestro.getMusicPlayer().getCurrentlyPlaying();
        if (this.currentTrack != actualTrack) {
            this.currentTrack = actualTrack;
            this.name.setText(this.currentTrack.getName());
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addTitle() {
        Text text = Text.newText(CURRENT_PLAYING).setFontSize(GridGui.FONT_SIZE).center().create();
        text.setColour(ColourPalette.WHITE);
        super.addText(text, 0.0f, 0.0f, 1.0f);
    }

    private void addName() {
        this.currentTrack = SoundMaestro.getMusicPlayer().getCurrentlyPlaying();
        this.name = Text.newText(this.currentTrack.getName()).setFontSize(SpeciesInfoGui.FONT_SIZE * 1.25f).center().create();
        this.name.setColour(ColourPalette.BASE_BLUE);
        super.addText(this.name, 0.0f, 0.5f, 1.0f);
    }
}

