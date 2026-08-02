/*
 * Decompiled with CFR 0.152.
 */
package fontRendering;

import fontRendering.FontVariablesCalculator;
import fontRendering.GillCalculator;
import fontRendering.SegoeCalculator;
import fontRendering.Text;
import fontRendering.TextLoader;
import guis.GuiMaster;
import utils.MyFile;

public enum FontType {
    SEGOE_UI(new MyFile(FontType.getFontsLoc(), "segoeUI.png"), new MyFile(FontType.getFontsLoc(), "segoeUI.fnt"), new SegoeCalculator()),
    GILL(new MyFile(FontType.getFontsLoc(), "gill3.png"), new MyFile(FontType.getFontsLoc(), "gill3.fnt"), new GillCalculator());

    private TextLoader loader;
    private FontVariablesCalculator calculator;

    private FontType(MyFile textureAtlas, MyFile fontFile, FontVariablesCalculator calculator) {
        this.loader = new TextLoader(textureAtlas, fontFile);
        this.calculator = calculator;
    }

    public FontVariablesCalculator getCalculator() {
        return this.calculator;
    }

    protected void loadText(Text text) {
        this.loader.loadTextIntoMemory(text);
    }

    protected int getTextureAtlas() {
        return this.loader.getFontTextureAtlas();
    }

    private static MyFile getFontsLoc() {
        return new MyFile(GuiMaster.GUIS_LOC, "fonts");
    }
}

