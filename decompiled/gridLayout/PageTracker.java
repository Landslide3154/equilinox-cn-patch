/*
 * Decompiled with CFR 0.152.
 */
package gridLayout;

import gridLayout.CurrentFilterSettings;
import gridLayout.SingleFilterSetting;

public class PageTracker {
    protected CurrentFilterSettings filterSettings;
    protected int page = 0;
    protected String searchTerm;

    public PageTracker(int filterCount) {
        SingleFilterSetting[] filters = new SingleFilterSetting[filterCount];
        int i = 0;
        while (i < filters.length) {
            filters[i] = new SingleFilterSetting();
            ++i;
        }
        this.filterSettings = new CurrentFilterSettings(filters);
    }

    public void reset() {
        this.filterSettings.reset();
        this.page = 0;
        this.searchTerm = null;
    }
}

