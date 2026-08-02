/*
 * Decompiled with CFR 0.152.
 */
package checkList;

import blueprints.Blueprint;
import userInterfaces.Listener;

public class ListElement
implements Comparable<ListElement> {
    private final String name;
    private final boolean checked;
    private final int count;
    private final Listener listener;
    private final Blueprint species;

    public ListElement(String name, boolean checked, Blueprint species, int count, Listener listener) {
        this.name = name;
        this.checked = checked;
        this.count = count;
        this.species = species;
        this.listener = listener;
    }

    public ListElement(String name, boolean checked) {
        this.name = name;
        this.checked = checked;
        this.count = 0;
        this.species = null;
        this.listener = null;
    }

    public Blueprint getSpecies() {
        return this.species;
    }

    public String getName() {
        return this.name;
    }

    public Listener getListener() {
        return this.listener;
    }

    public boolean isChecked() {
        return this.checked;
    }

    public int getCount() {
        return this.count;
    }

    @Override
    public int compareTo(ListElement o) {
        return this.name.compareTo(o.name);
    }
}

