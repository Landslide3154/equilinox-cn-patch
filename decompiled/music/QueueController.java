/*
 * Decompiled with CFR 0.152.
 */
package music;

import guis.GuiComponent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import main.EquilinoxMusic;
import music.MusicTrack;
import music.Queue;
import musicTab.ListUi;
import musicTab.MusicUi;
import musicTab.QueueItemUi;
import musicTab.QueueUi;
import utils.BinaryReader;
import utils.BinaryWriter;

public class QueueController
extends Queue {
    private static final int MAX_CAPACITY = 10;
    private ListUi ui;
    private List<QueueItemUi> uiItems;
    private boolean repeat = false;

    public void unregisterUi(ListUi ui) {
        if (this.ui == ui) {
            this.ui = null;
            this.uiItems = null;
        }
    }

    public void export(BinaryWriter writer) throws IOException {
        writer.writeBoolean(this.repeat);
        writer.writeInt(super.getTracks().size());
        for (MusicTrack track : this.getTracks()) {
            writer.writeInt(track.getId());
        }
    }

    public void load(BinaryReader reader) throws Exception {
        this.repeat = reader.readBoolean();
        int count = reader.readInt();
        int i = 0;
        while (i < count) {
            this.add(EquilinoxMusic.getTrack(reader.readInt()));
            ++i;
        }
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public ListUi createQueueUi() {
        ArrayList<GuiComponent> components = new ArrayList<GuiComponent>();
        this.uiItems = new ArrayList<QueueItemUi>();
        int i = 0;
        while (i < this.getTracks().size()) {
            QueueItemUi item = new QueueItemUi(i, this.getTracks().get(i), this);
            this.uiItems.add(item);
            components.add(item);
            ++i;
        }
        this.ui = new QueueUi(this, this.getName(), 24, MusicUi.GAP_HEIGHT, components);
        return this.ui;
    }

    public boolean isRepeating() {
        return this.repeat;
    }

    @Override
    public void add(MusicTrack track) {
        if (this.ui != null && this.uiItems.size() == 10) {
            return;
        }
        if (this.ui != null) {
            QueueItemUi item = new QueueItemUi(this.uiItems.size(), track, this);
            this.ui.addElement(item);
            this.uiItems.add(item);
            this.ui.setTitle(this.getName());
        }
        super.add(track);
    }

    @Override
    public MusicTrack remove(int index) {
        if (this.ui != null) {
            this.removeElementFromUi(index);
            this.ui.setTitle(this.getName());
        }
        return super.remove(index);
    }

    public MusicTrack getNext() {
        MusicTrack track = this.remove(0);
        if (this.repeat) {
            this.add(track);
        }
        return track;
    }

    private void removeElementFromUi(int index) {
        this.ui.removeElement(index);
        this.uiItems.remove(index);
        int i = index;
        while (i < this.uiItems.size()) {
            this.uiItems.get(i).decrementIndex();
            ++i;
        }
    }

    private String getName() {
        return String.valueOf(GameText.getText(655)) + " (" + this.uiItems.size() + ")";
    }
}

