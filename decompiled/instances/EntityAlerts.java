/*
 * Decompiled with CFR 0.152.
 */
package instances;

import instances.EntityListener;
import java.util.ArrayList;
import java.util.List;

public class EntityAlerts {
    private List<EntityListener> incapacitatedListeners;
    private List<EntityListener> removeListeners;
    private List<EntityListener> deadListeners;

    public void addIncapacitatedListener(EntityListener listener) {
        if (this.incapacitatedListeners == null) {
            this.incapacitatedListeners = new ArrayList<EntityListener>();
        }
        this.incapacitatedListeners.add(listener);
    }

    public void addDeadListener(EntityListener listener) {
        if (this.deadListeners == null) {
            this.deadListeners = new ArrayList<EntityListener>();
        }
        this.deadListeners.add(listener);
    }

    public void addRemoveListener(EntityListener listener) {
        if (this.removeListeners == null) {
            this.removeListeners = new ArrayList<EntityListener>();
        }
        this.removeListeners.add(listener);
    }

    protected void notifyDied() {
        this.notifyIncapacitated();
        if (this.deadListeners == null) {
            return;
        }
        for (EntityListener listener : this.deadListeners) {
            listener.execute();
        }
    }

    protected void notifyIncapacitated() {
        if (this.incapacitatedListeners == null) {
            return;
        }
        for (EntityListener listener : this.incapacitatedListeners) {
            listener.execute();
        }
    }

    protected void notifyRemoved() {
        if (this.removeListeners == null) {
            return;
        }
        for (EntityListener listener : this.removeListeners) {
            listener.execute();
        }
    }
}

