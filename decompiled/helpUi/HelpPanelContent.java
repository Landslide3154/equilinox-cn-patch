/*
 * Decompiled with CFR 0.152.
 */
package helpUi;

import helpUi.ContentSection;

public class HelpPanelContent {
    protected final String title;
    protected final String intro;
    protected final ContentSection[] sections;
    protected final float scale;

    protected HelpPanelContent(String title, String intro, ContentSection[] sections, float scale) {
        this.title = title;
        this.intro = intro;
        this.sections = sections;
        this.scale = scale;
    }
}

