/*
 * Decompiled with CFR 0.152.
 */
package gridLayout;

import gridLayout.FilterId;

public class SingleFilterSetting {
    private boolean noFilter;
    private int mainCategory;
    private Integer subCategory;

    public SingleFilterSetting() {
        this.noFilter = true;
        this.mainCategory = -1;
        this.subCategory = null;
    }

    public SingleFilterSetting(int mainCategory, Integer subCategory) {
        this.noFilter = false;
        this.mainCategory = mainCategory;
        this.subCategory = subCategory;
    }

    public void reset() {
        this.mainCategory = -1;
        this.subCategory = null;
        this.noFilter = true;
    }

    public void set(int mainCat, Integer subCat) {
        this.mainCategory = mainCat;
        this.subCategory = subCat;
        this.noFilter = false;
    }

    public boolean isNoFilter() {
        return this.noFilter;
    }

    public int getMainCategory() {
        return this.mainCategory;
    }

    public Integer getSubCategory() {
        return this.subCategory;
    }

    public boolean check(FilterId filterId) {
        if (this.noFilter) {
            return true;
        }
        if (filterId == null) {
            return false;
        }
        FilterId mainCatFilter = filterId.get(this.mainCategory);
        if (mainCatFilter == null) {
            return false;
        }
        if (this.subCategory == null) {
            return true;
        }
        return mainCatFilter.has(this.subCategory);
    }
}

