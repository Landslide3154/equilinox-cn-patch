/*
 * Decompiled with CFR 0.152.
 */
package fontRendering;

import fontRendering.Character;
import java.util.ArrayList;
import java.util.List;

public class Word {
    private List<Character> characters = new ArrayList<Character>();
    private double width = 0.0;
    private double fontSize;

    protected Word(double fontSize) {
        this.fontSize = fontSize;
    }

    protected void addCharacter(Character character) {
        this.characters.add(character);
        this.width += character.getxAdvance() * this.fontSize;
    }

    protected List<Character> getCharacters() {
        return this.characters;
    }

    protected double getWordWidth() {
        return this.width;
    }
}

