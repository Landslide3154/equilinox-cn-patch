package fontRendering;

import java.util.List;

public class SpacingTest {
    static double SPACE = 39.0 / 4096.0;  // arbitrary normalized space width

    static void addChar(Word w, double adv) {
        w.addCharacter(new Character(0, 0, 0, 0, 0, 0, 0, adv, 0, adv));
    }

    public static void main(String[] args) {
        // line 1: CJK words "植物", "的所有统", "计数据" — split by char-wrap, no real spaces
        Line line = new Line(SPACE, 1.0, 100.0);
        Word w1 = new Word(1.0);
        w1.setSpaceBefore(false);
        addChar(w1, 10); addChar(w1, 10);
        line.attemptToAddWord(w1);

        Word w2 = new Word(1.0);
        w2.setSpaceBefore(false);
        addChar(w2, 10); addChar(w2, 10); addChar(w2, 10);
        boolean ok2 = line.attemptToAddWord(w2);
        System.out.println("w2 (spaceBefore=false) added=" + ok2 + " lineLen=" + line.getLineLength());

        // mixed: "或 W,A,S,D" — second word has a real space before
        Line line2 = new Line(SPACE, 1.0, 100.0);
        Word m1 = new Word(1.0);
        m1.setSpaceBefore(false);
        addChar(m1, 10);
        line2.attemptToAddWord(m1);
        Word m2 = new Word(1.0);
        m2.setSpaceBefore(true);
        addChar(m2, 10);
        boolean okm = line2.attemptToAddWord(m2);
        System.out.println("mixed m2 (spaceBefore=true) added=" + okm + " lineLen=" + line2.getLineLength() + " (expect 10+SPACE+10)");

        // renderer advance logic: CJK boundary should advance 0, space boundary advances SPACE
        double curser = 0;
        List<Word> words = line.getWords();
        for (int i = 0; i < words.size() - 1; i++) {
            Word cur = words.get(i);
            Word next = words.get(i + 1);
            for (Character c : cur.getCharacters()) curser += c.getxAdvance();
            if (next.isSpaceBefore()) curser += SPACE;
        }
        for (Character c : words.get(words.size() - 1).getCharacters()) curser += c.getxAdvance();
        System.out.println("render advance for CJK-wrapped line: " + curser + " (expect 50 = 4+5 chars * 10, no space)");

        double curser2 = 0;
        List<Word> words2 = line2.getWords();
        for (int i = 0; i < words2.size() - 1; i++) {
            Word cur = words2.get(i);
            Word next = words2.get(i + 1);
            for (Character c : cur.getCharacters()) curser2 += c.getxAdvance();
            if (next.isSpaceBefore()) curser2 += SPACE;
        }
        for (Character c : words2.get(words2.size() - 1).getCharacters()) curser2 += c.getxAdvance();
        System.out.println("render advance for mixed line: " + curser2 + " (expect 20 + SPACE)");
    }
}
