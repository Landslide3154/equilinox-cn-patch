/*
 * Decompiled with CFR 0.152.
 */
package entityInfoGui;

import blueprints.Blueprint;
import entityInfoGui.TabSetting;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TabController {
    private static final float MAX_TIME = 60.0f;
    private static Map<Blueprint, TabSetting> tabSettings = new HashMap<Blueprint, TabSetting>();

    public static void reset() {
        tabSettings.clear();
    }

    public static int getTabIndex(Blueprint blueprint) {
        TabSetting setting = tabSettings.get(blueprint);
        if (setting == null) {
            return 0;
        }
        setting.resetTime();
        return setting.getTabNumber();
    }

    public static void update(float delta) {
        Iterator<Map.Entry<Blueprint, TabSetting>> iterator = tabSettings.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Blueprint, TabSetting> entry = iterator.next();
            float time = entry.getValue().updateTime(delta);
            if (!(time > 60.0f)) continue;
            iterator.remove();
        }
    }

    public static void indicateTabSelected(Blueprint blueprint, int tabId) {
        if (tabId == 0) {
            tabSettings.remove(blueprint);
        } else {
            TabSetting setting = tabSettings.get(blueprint);
            if (setting == null) {
                setting = new TabSetting();
                tabSettings.put(blueprint, setting);
            }
            setting.setTabOpen(tabId);
        }
    }
}

