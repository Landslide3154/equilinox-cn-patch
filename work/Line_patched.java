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
        if (this.words.isEmpty()) {
            // Always accept a word on an empty line so that unbreakable
            // (e.g. CJK) words can never be dropped entirely.
            this.words.add(word);
            this.currentLineLength += additionalLength;
            return true;
        }
        if (this.currentLineLength + (additionalLength += this.spaceSize) <= this.maxLength) {
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
