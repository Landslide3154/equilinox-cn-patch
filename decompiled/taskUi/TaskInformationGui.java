/*
 * Decompiled with CFR 0.152.
 */
package taskUi;

import extraInfoGui.ExtraInfoGui;
import java.util.ArrayList;
import mainGuis.EquilinoxGuis;
import taskUi.TaskInfoFrameUi;
import tasks.Task;
import textures.Texture;
import toolTips.ToolTipInfo;

public class TaskInformationGui {
    public static void openTaskInfo(Task task) {
        ExtraInfoGui extraInfoGui = EquilinoxGuis.getExtraInfoGui();
        String title = task.isRepeatable() ? String.valueOf(task.name) + " (Repeatable)" : task.name;
        extraInfoGui.display(title, new ArrayList<Texture>(), new ArrayList<ToolTipInfo>(), new TaskInfoFrameUi(task));
    }
}

