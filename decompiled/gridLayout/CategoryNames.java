/*
 * Decompiled with CFR 0.152.
 */
package gridLayout;

import dropDownBoxUi.ComboBoxObject;
import java.util.Map;

public class CategoryNames {
    private final String[] categories;
    private final String[][] subCats;
    private final String noneChosen;
    private final boolean mainCatsSelectable;
    private String[] prefixes = null;

    public CategoryNames(String noneChosenString, String[] categories, boolean mainCatsSelectable) {
        this.categories = categories;
        this.subCats = null;
        this.noneChosen = noneChosenString;
        this.mainCatsSelectable = mainCatsSelectable;
    }

    public CategoryNames(String noneChosenString, Map<String, String[]> categories, boolean mainCatsSelectable) {
        this.noneChosen = noneChosenString;
        this.categories = categories.keySet().toArray(new String[categories.size()]);
        this.subCats = new String[categories.size()][];
        int i = 0;
        for (String[] sub : categories.values()) {
            this.subCats[i++] = sub;
        }
        this.mainCatsSelectable = mainCatsSelectable;
    }

    public void setPrefixes(String[] prefixes) {
        this.prefixes = prefixes;
    }

    public ComboBoxObject[] getCategoryObjects() {
        ComboBoxObject[] objects = new ComboBoxObject[this.categories.length + 1];
        objects[0] = new ComboBoxObject(this.noneChosen);
        int i = 0;
        while (i < this.categories.length) {
            if (this.subCats != null) {
                Object[] subCategories = this.subCats[i];
                ComboBoxObject newObject = new ComboBoxObject((Object)this.categories[i], subCategories);
                newObject.setSelectable(this.mainCatsSelectable);
                if (this.prefixes != null) {
                    newObject.setExtraPrefix(this.prefixes[i]);
                }
                objects[i + 1] = newObject;
            } else {
                objects[i + 1] = new ComboBoxObject(this.categories[i]);
            }
            ++i;
        }
        return objects;
    }
}

