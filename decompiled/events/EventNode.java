/*
 * Decompiled with CFR 0.152.
 */
package events;

import events.EventData;
import events.EventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventNode {
    public final EventNode parent;
    private List<EventListener> listeners = new ArrayList<EventListener>();
    private Map<String, EventNode> leafChildren = new HashMap<String, EventNode>();

    public EventNode(EventNode parent) {
        this.parent = parent;
    }

    public void registerEvent(EventData data, String ... eventKeys) {
        this.registerEvent(0, eventKeys, data);
    }

    public void addListener(EventListener listener, String ... eventKeys) {
        this.addListener(0, eventKeys, listener);
    }

    private void registerEvent(int index, String[] eventKey, EventData data) {
        if (index == eventKey.length) {
            this.notifyAllListeners(data);
            return;
        }
        EventNode child = this.leafChildren.get(eventKey[index]);
        if (child == null) {
            this.notifyAllListeners(data);
        } else {
            child.registerEvent(index + 1, eventKey, data);
        }
    }

    private void addListener(int index, String[] childEventKey, EventListener listener) {
        if (index == childEventKey.length) {
            this.listeners.add(listener);
            return;
        }
        EventNode child = this.leafChildren.get(childEventKey[index]);
        if (child == null) {
            child = new EventNode(this);
            this.leafChildren.put(childEventKey[index], child);
        }
        child.addListener(index + 1, childEventKey, listener);
    }

    private void notifyAllListeners(EventData data) {
        this.notifyListeners(data);
        if (this.parent != null) {
            this.parent.notifyAllListeners(data);
        }
    }

    private void notifyListeners(EventData data) {
        for (EventListener listener : this.listeners) {
            listener.eventOccurred(data);
        }
    }
}

