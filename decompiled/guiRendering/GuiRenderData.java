/*
 * Decompiled with CFR 0.152.
 */
package guiRendering;

import fontRendering.FontType;
import fontRendering.Text;
import guis.GuiTexture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GuiRenderData {
    private GuiRenderLevelData[] levels = new GuiRenderLevelData[1];
    private int startLevel = 0;

    public GuiRenderData() {
        this.levels[0] = new GuiRenderLevelData();
    }

    public void addTexture(int level, GuiTexture texture) {
        this.checkResize(level);
        this.levels[level - this.startLevel].textures.add(texture);
    }

    public void addText(int level, Text text) {
        this.checkResize(level);
        this.levels[level - this.startLevel].addText(text);
    }

    private void checkResize(int level) {
        if (level - this.startLevel >= this.levels.length) {
            this.resize(level - this.startLevel + 1);
        } else if (level - this.startLevel < 0) {
            int currentMax = this.levels.length + this.startLevel;
            this.startLevel = level;
            this.resize(currentMax - this.startLevel);
        }
    }

    public GuiRenderLevelData[] getRenderData() {
        return this.levels;
    }

    public void clear() {
        GuiRenderLevelData[] guiRenderLevelDataArray = this.levels;
        int n = this.levels.length;
        int n2 = 0;
        while (n2 < n) {
            GuiRenderLevelData level = guiRenderLevelDataArray[n2];
            level.clear();
            ++n2;
        }
    }

    private void resize(int size) {
        GuiRenderLevelData[] oldData = this.levels;
        this.levels = new GuiRenderLevelData[size];
        int i = 0;
        while (i < oldData.length) {
            this.levels[i] = oldData[i];
            ++i;
        }
        i = oldData.length;
        while (i < this.levels.length) {
            this.levels[i] = new GuiRenderLevelData();
            ++i;
        }
    }

    public static class GuiRenderLevelData {
        private List<GuiTexture> textures = new ArrayList<GuiTexture>();
        private Map<FontType, List<Text>> texts = new HashMap<FontType, List<Text>>();

        public boolean isEmpty() {
            return this.textures.isEmpty() && this.texts.isEmpty();
        }

        public List<GuiTexture> getTextures() {
            return this.textures;
        }

        public Map<FontType, List<Text>> getTexts() {
            return this.texts;
        }

        private void clear() {
            this.textures.clear();
            this.texts.clear();
        }

        private void addText(Text text) {
            FontType font = text.getFontType();
            List<Text> textBatch = this.texts.get((Object)font);
            if (textBatch == null) {
                textBatch = new ArrayList<Text>();
                this.texts.put(font, textBatch);
            }
            textBatch.add(text);
        }
    }
}

