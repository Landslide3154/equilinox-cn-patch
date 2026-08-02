/*
 * Decompiled with CFR 0.152.
 */
package notificationPopUp;

import audio.Sound;
import audio.SoundMaestro;
import basics.DisplayManager;
import interpolation.SmoothFloat;
import java.util.ArrayList;
import java.util.List;
import mainGuis.EquilinoxGuis;
import notificationPopUp.Notification;
import notificationPopUp.NotificationGui;
import textures.Texture;
import userInterfaces.Listener;

public class Notifier {
    private static final float SLIDE_AGILITY = 10.0f;
    private static final int GUI_HEIGHT_PIXELS = 70;
    protected static final float GUI_HEIGHT = 70.0f / (float)DisplayManager.getUiHeight();
    private static final int GAP_HEIGHT_PIXELS = 7;
    protected static final float GAP_HEIGHT = 7.0f / (float)DisplayManager.getUiHeight();
    private SmoothFloat topPosition = new SmoothFloat(-GAP_HEIGHT, 10.0f);
    private List<NotificationGui> guis = new ArrayList<NotificationGui>();

    public Notifier() {
        this.recalculateTopTarget();
    }

    public synchronized void update() {
        this.topPosition.update(DisplayManager.getDeltaSeconds());
        int index = 0;
        while (this.guis.size() > index) {
            boolean alive = this.guis.get(index).updateNotification(index);
            if (!alive) {
                NotificationGui gui = this.guis.remove(index);
                gui.remove();
                this.recalculateTopTarget();
                this.topPosition.instantIncrease(GUI_HEIGHT + GAP_HEIGHT);
                continue;
            }
            ++index;
        }
    }

    public synchronized void notify(String title, String text, Texture icon, Sound sound, Listener listener) {
        this.playNotifySound(sound);
        NotificationGui gui = new NotificationGui(this.guis.size(), this.topPosition, icon, title, text);
        gui.setListener(listener);
        this.guis.add(gui);
        this.recalculateTopTarget();
        EquilinoxGuis.getNotificationLog().addNotification(new Notification(title, text, icon, listener));
    }

    public synchronized void notify(String title, String text, Texture icon, Sound sound) {
        this.notify(title, text, icon, sound, null);
    }

    private void playNotifySound(Sound sound) {
        if (sound != null) {
            SoundMaestro.playSystemSound(sound);
        }
    }

    private void recalculateTopTarget() {
        int count = this.guis.size();
        this.topPosition.setTarget(1.0f - ((float)count * GUI_HEIGHT + (float)(count - 1) * GAP_HEIGHT));
    }
}

