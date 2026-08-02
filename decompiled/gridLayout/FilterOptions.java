/*
 * Decompiled with CFR 0.152.
 */
package gridLayout;

import gridLayout.CategoryNames;

public class FilterOptions {
    private final CategoryNames[] filters;

    public FilterOptions(CategoryNames filter) {
        this.filters = new CategoryNames[1];
        this.filters[0] = filter;
    }

    public FilterOptions(CategoryNames ... filters) {
        this.filters = filters;
    }

    public CategoryNames[] getFilterOptions() {
        return this.filters;
    }
}

