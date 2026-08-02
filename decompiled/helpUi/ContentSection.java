/*
 * Decompiled with CFR 0.152.
 */
package helpUi;

import textures.Texture;

public class ContentSection {
    protected final String title;
    protected final Texture image;
    protected final String description;

    public ContentSection(String title, Texture image, String desc) {
        this.title = title;
        this.image = image;
        this.description = desc;
    }
}

