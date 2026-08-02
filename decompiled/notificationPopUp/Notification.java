/*
 * Decompiled with CFR 0.152.
 */
package notificationPopUp;

import textures.Texture;
import userInterfaces.Listener;

public class Notification {
    protected final String title;
    protected final String description;
    protected final Texture icon;
    protected final Listener listener;

    protected Notification(String title, String description, Texture icon, Listener listener) {
        this.title = title;
        this.description = description;
        this.icon = icon;
        this.listener = listener;
    }
}

