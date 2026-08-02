/*
 * Decompiled with CFR 0.152.
 */
package events;

public class EventData {
    public final Object object;
    public final int count;

    public EventData() {
        this.count = 0;
        this.object = null;
    }

    public EventData(Object object) {
        this.count = 0;
        this.object = object;
    }

    public EventData(Object object, int count) {
        this.count = count;
        this.object = object;
    }
}

