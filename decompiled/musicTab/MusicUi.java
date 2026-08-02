/*
 * Decompiled with CFR 0.152.
 */
package musicTab;

import audio.SoundMaestro;
import basics.DisplayManager;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import java.util.ArrayList;
import java.util.Collection;
import languages.GameText;
import music.MusicTrack;
import musicTab.ListUi;
import musicTab.StatusUi;
import musicTab.TrackUi;
import org.lwjgl.util.vector.Vector2f;

public class MusicUi
extends GuiComponent {
    private static final String ALL_TRACKS = GameText.getText(83);
    public static final int ITEM_HEIGHT = 24;
    public static final int GAP_HEIGHT = DisplayManager.isMinitureHeight() ? 2 : 4;
    private static final int OUTER_PADDING = 20;
    private static final int INNER_PADDING = 8;
    private static final int BOTTOM_START_PIXELS = 230;
    private ListUi tracksList;

    @Override
    protected void init() {
        super.init();
        float xInnerPad = super.pixelsToRelativeX(8.0f);
        float yInnerPad = super.pixelsToRelativeY(8.0f);
        float xOuterPad = super.pixelsToRelativeX(20.0f);
        float yOuterPad = super.pixelsToRelativeY(20.0f);
        this.addStatusPanel(xInnerPad, yInnerPad, xOuterPad, yOuterPad);
        this.addQueueList(xInnerPad, yInnerPad, xOuterPad, yOuterPad);
        this.addTracksList(xInnerPad, yInnerPad, xOuterPad, yOuterPad);
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

    private void addStatusPanel(float xInnerPad, float yInnerPad, float xOuterPad, float yOuterPad) {
        float xStart = 0.5f + xInnerPad * 0.5f;
        float bottomStart = super.pixelsToRelativeY(230.0f);
        super.addComponent(new StatusUi(), xStart, yOuterPad, 1.0f - (xStart + xOuterPad), bottomStart - (yOuterPad + yInnerPad));
    }

    private void addQueueList(float xInnerPad, float yInnerPad, float xOuterPad, float yOuterPad) {
        float bottomStart = super.pixelsToRelativeY(230.0f);
        float xStart = 0.5f + xInnerPad * 0.5f;
        ListUi queueUi = SoundMaestro.getMusicPlayer().getQueueController().createQueueUi();
        super.addComponent(queueUi, xStart, bottomStart, 1.0f - (xStart + xOuterPad), 1.0f - (bottomStart + yOuterPad));
    }

    private void addTracksList(float xInnerPad, float yInnerPad, float xOuterPad, float yOuterPad) {
        float width = 0.5f - (xOuterPad + xInnerPad * 0.5f);
        ArrayList<GuiComponent> components = new ArrayList<GuiComponent>();
        Collection<MusicTrack> tracks = SoundMaestro.getMusicPlayer().getPlayList().getOrderedTracks();
        for (MusicTrack track : tracks) {
            components.add(new TrackUi(track, SoundMaestro.getMusicPlayer().getQueueController()));
        }
        this.tracksList = new ListUi(String.valueOf(ALL_TRACKS) + " (" + tracks.size() + ")", 24, GAP_HEIGHT, components, true);
        super.addComponent(this.tracksList, xOuterPad, yOuterPad, width, 1.0f - 2.0f * yOuterPad);
    }
}

