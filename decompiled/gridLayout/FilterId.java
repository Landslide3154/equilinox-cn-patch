/*
 * Decompiled with CFR 0.152.
 */
package gridLayout;

import java.util.HashMap;
import java.util.Map;

public class FilterId {
    public final int id;
    private Map<Integer, FilterId> subFilters = null;

    public FilterId(int id) {
        this.id = id;
    }

    public FilterId get(int subFilterId) {
        if (this.subFilters == null) {
            return null;
        }
        return this.subFilters.get(subFilterId);
    }

    public boolean has(int subFilterId) {
        if (this.subFilters == null) {
            return false;
        }
        return this.subFilters.get(subFilterId) != null;
    }

    public void add(int ... filterIds) {
        FilterId subFilter;
        if (this.subFilters == null) {
            this.subFilters = new HashMap<Integer, FilterId>();
        }
        if ((subFilter = this.subFilters.get(filterIds[0])) == null) {
            subFilter = new FilterId(filterIds[0]);
            this.subFilters.put(filterIds[0], subFilter);
        }
        if (filterIds.length == 1) {
            return;
        }
        int[] subFilterIds = new int[filterIds.length - 1];
        int i = 0;
        while (i < subFilterIds.length) {
            subFilterIds[i] = filterIds[i + 1];
            ++i;
        }
        subFilter.add(subFilterIds);
    }
}

