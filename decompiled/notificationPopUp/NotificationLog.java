/*
 * Decompiled with CFR 0.152.
 */
package notificationPopUp;

import java.util.ArrayList;
import java.util.List;
import notificationPopUp.Notification;

public class NotificationLog {
    private static final int MAX_SIZE = 100;
    private List<Notification> notifications = new ArrayList<Notification>();

    public void addNotification(Notification notification) {
        this.notifications.add(notification);
        if (this.notifications.size() > 100) {
            this.notifications.remove(0);
        }
    }

    public void clear() {
        this.notifications.clear();
    }

    public List<Notification> getNotifications() {
        return this.notifications;
    }
}

