/*
 * Decompiled with CFR 0.152.
 */
package fontRendering;

import fontRendering.Word;
import java.util.ArrayList;
import java.util.List;

public class Line {
    private double maxLength;
    private double spaceSize;
    private List<Word> words = new ArrayList<Word>();
    private double currentLineLength = 0.0;

    protected Line(double spaceWidth, double fontSize, double maxLength) {
        this.spaceSize = spaceWidth * fontSize;
        this.maxLength = maxLength;
    }

    protected boolean attemptToAddWord(Word word) {
        double additionalLength = word.getWordWidth();
        if (this.currentLineLength + (additionalLength += !this.words.isEmpty() ? this.spaceSize : 0.0) <= this.maxLength) {
            this.words.add(word);
            this.currentLineLength += additionalLength;
            return true;
        }
        return false;
    }

    protected double getMaxLength() {
        return this.maxLength;
    }

    protected double getLineLength() {
        return this.currentLineLength;
    }

    protected List<Word> getWords() {
        return this.words;
    }
}

