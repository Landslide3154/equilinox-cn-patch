package fontRendering;

import basics.DisplayManager;
import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import utils.MyFile;

/**
 * Headless layout test: parses the patched gill3 font with the game's own
 * MetaFile parser and runs the patched CJK-safe word-wrap algorithm over
 * every string, verifying that no string produces zero glyphs.
 */
public class LayoutTest {

    private static MetaFile meta;

    private static List<Line> simulate(String s, double fontSize, double maxLineSize) {
        char[] chars = s.toCharArray();
        List<Line> lines = new ArrayList<Line>();
        Line currentLine = new Line(meta.getSpaceWidth(), fontSize, maxLineSize);
        Word currentWord = new Word(fontSize);
        for (char c : chars) {
            if (c == ' ') {
                if (!currentWord.getCharacters().isEmpty()) {
                    boolean added = currentLine.attemptToAddWord(currentWord);
                    if (!added) {
                        lines.add(currentLine);
                        currentLine = new Line(meta.getSpaceWidth(), fontSize, maxLineSize);
                        currentLine.attemptToAddWord(currentWord);
                    }
                    currentWord = new Word(fontSize);
                }
            } else {
                Character character = meta.getCharacter(c);
                if (character != null) {
                    if (!currentWord.getCharacters().isEmpty() && !wordFits(currentLine, currentWord, character, fontSize)) {
                        boolean added = currentLine.attemptToAddWord(currentWord);
                        if (!added) {
                            lines.add(currentLine);
                            currentLine = new Line(meta.getSpaceWidth(), fontSize, maxLineSize);
                            currentLine.attemptToAddWord(currentWord);
                        }
                        currentWord = new Word(fontSize);
                    }
                    currentWord.addCharacter(character);
                } else {
                    System.out.println("MISSING GLYPH: " + (int) c + " in string: " + s);
                }
            }
        }
        boolean added = currentLine.attemptToAddWord(currentWord);
        if (!added) {
            lines.add(currentLine);
            currentLine = new Line(meta.getSpaceWidth(), fontSize, maxLineSize);
            currentLine.attemptToAddWord(currentWord);
        }
        lines.add(currentLine);
        return lines;
    }

    private static boolean wordFits(Line line, Word word, Character character, double fontSize) {
        double wordWidth = word.getWordWidth() + character.getxAdvance() * fontSize;
        double space = line.getWords().isEmpty() ? 0.0 : meta.getSpaceWidth() * fontSize;
        return line.getLineLength() + space + wordWidth <= line.getMaxLength();
    }

    private static int glyphCount(List<Line> lines) {
        int total = 0;
        for (Line line : lines) {
            for (Word word : line.getWords()) {
                total += word.getCharacters().size();
            }
        }
        return total;
    }

    public static void main(String[] args) throws Exception {
        Field f = DisplayManager.class.getDeclaredField("aspectRatio");
        f.setAccessible(true);
        f.setFloat(null, 1600.0f / 900.0f);

        meta = new MetaFile(new MyFile("res/guis/fonts/gill3.fnt"));

        List<String> texts = new ArrayList<String>();
        File csv = new File("res/languageSheet.csv");
        for (String line : Files.readAllLines(csv.toPath(), StandardCharsets.UTF_8)) {
            String[] parts = line.split(";");
            if (parts.length >= 3 && parts[0].matches("\\d+")) {
                texts.add(parts[2]);
            }
        }
        texts.add("景深效果（略有瑕疵）");
        texts.add("垂直同步");
        texts.add("无边框窗口");
        texts.add("阴影: 开");
        texts.add("完成任务以获取多样性点数，并解锁新物种和新物品！");

        int failures = 0;
        int totalGlyphs = 0;
        for (String s : texts) {
            // CheckOptionUi-like narrow box (0.18) and tooltip-like box (0.15) and wide box (0.9)
            for (double box : new double[]{0.18, 0.15, 0.9}) {
                List<Line> lines = simulate(s, 1.11111, box);
                int g = glyphCount(lines);
                totalGlyphs += g;
                if (g == 0) {
                    System.out.println("FAIL(0 glyphs) box=" + box + " text=" + s);
                    failures++;
                }
            }
        }
        System.out.println("Tested " + texts.size() + " strings x3 boxes, total glyphs " + totalGlyphs + ", failures " + failures);
        // sanity: verify some specific chars resolve
        for (char c : "景深效果（略有瑕疵）垂直同步无边框窗口完成任务".toCharArray()) {
            if (meta.getCharacter(c) == null) {
                System.out.println("MISSING " + (int) c);
            }
        }
        if (failures > 0) {
            System.exit(1);
        }
    }
}
