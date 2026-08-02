/*
 * Decompiled with CFR 0.152.
 */
package gridLayout;

import gridLayout.FilterId;
import gridLayout.SingleFilterSetting;

public class CurrentFilterSettings {
    private final SingleFilterSetting[] filterValues;

    public CurrentFilterSettings(SingleFilterSetting[] filterValues) {
        this.filterValues = filterValues;
    }

    public void reset() {
        SingleFilterSetting[] singleFilterSettingArray = this.filterValues;
        int n = this.filterValues.length;
        int n2 = 0;
        while (n2 < n) {
            SingleFilterSetting filter = singleFilterSettingArray[n2];
            filter.reset();
            ++n2;
        }
    }

    public SingleFilterSetting getFilter(int number) {
        return this.filterValues[number];
    }

    public boolean check(FilterId filterId) {
        int i = 0;
        while (i < this.filterValues.length) {
            boolean passed = this.filterValues[i].check(filterId.get(i));
            if (!passed) {
                return false;
            }
            ++i;
        }
        return true;
    }

    public boolean isFiltering() {
        SingleFilterSetting[] singleFilterSettingArray = this.filterValues;
        int n = this.filterValues.length;
        int n2 = 0;
        while (n2 < n) {
            SingleFilterSetting filter = singleFilterSettingArray[n2];
            if (!filter.isNoFilter()) {
                return true;
            }
            ++n2;
        }
        return false;
    }
}

