/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import java.util.Map;
import session.Session;
import tasks.Task;

public class NewTaskUpdater {
    private final int saveVersion;
    private final Session loadingSession;
    private final Map<Integer, Task> tasks;

    protected NewTaskUpdater(int saveVersion, Session loadingSession, Map<Integer, Task> tasks) {
        this.saveVersion = saveVersion;
        this.loadingSession = loadingSession;
        this.tasks = tasks;
    }

    protected void updateTask(int taskId, int versionAdded) {
        if (this.saveVersion < versionAdded) {
            this.tasks.get(taskId).resetLight(this.loadingSession);
        }
    }
}

