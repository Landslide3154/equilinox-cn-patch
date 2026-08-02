/*
 * Decompiled with CFR 0.152.
 */
package fontRendering;

import basics.Loader;
import fontRendering.Character;
import fontRendering.Line;
import fontRendering.MetaFile;
import fontRendering.Text;
import fontRendering.Word;
import java.util.ArrayList;
import java.util.List;
import textures.Texture;
import utils.MyFile;

public class TextLoader {
    protected static final double LINE_HEIGHT = (double)0.04f;
    protected static final int SPACE_ASCII = 32;
    private Texture fontTexture;
    private MetaFile metaData;

    public TextLoader(MyFile fontSheet, MyFile metaFile) {
        this.fontTexture = Texture.newTexture(fontSheet).noFiltering().create();
        this.metaData = new MetaFile(metaFile);
    }

    public int getFontTextureAtlas() {
        return this.fontTexture.getID();
    }

    public void loadTextIntoMemory(Text text) {
        List<Line> lines = this.createStructure(text);
        this.loadStructureToOpenGL(text, lines);
    }

    private List<Line> createStructure(Text text) {
        char[] chars = text.getTextString().toCharArray();
        ArrayList<Line> lines = new ArrayList<Line>();
        Line currentLine = new Line(this.metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
        Word currentWord = new Word(text.getFontSize());
        char[] cArray = chars;
        int n = chars.length;
        int n2 = 0;
        while (n2 < n) {
            char c = cArray[n2];
            char ascii = c;
            if (ascii == ' ') {
                boolean added = currentLine.attemptToAddWord(currentWord);
                if (!added) {
                    lines.add(currentLine);
                    currentLine = new Line(this.metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
                    currentLine.attemptToAddWord(currentWord);
                }
                currentWord = new Word(text.getFontSize());
            } else {
                Character character = this.metaData.getCharacter(ascii);
                if (character != null) {
                    currentWord.addCharacter(character);
                } else {
                    System.err.println("ERROR CHAR " + ascii);
                }
            }
            ++n2;
        }
        this.completeStructure(lines, currentLine, currentWord, text);
        return lines;
    }

    private void completeStructure(List<Line> lines, Line currentLine, Word currentWord, Text text) {
        boolean added = currentLine.attemptToAddWord(currentWord);
        if (!added) {
            lines.add(currentLine);
            currentLine = new Line(this.metaData.getSpaceWidth(), text.getFontSize(), text.getMaxLineSize());
            currentLine.attemptToAddWord(currentWord);
        }
        lines.add(currentLine);
    }

    private void loadStructureToOpenGL(Text text, List<Line> lines) {
        this.setTextSettings(text, lines);
        boolean first = true;
        double indent = 0.0;
        double curserX = 0.0;
        double curserY = 0.0;
        ArrayList<Float> vertices = new ArrayList<Float>();
        ArrayList<Float> textureCoords = new ArrayList<Float>();
        int i = 0;
        while (i < lines.size()) {
            Line line = lines.get(i);
            curserX = indent;
            if (text.isCentered()) {
                curserX = (line.getMaxLength() - line.getLineLength()) / 2.0;
            } else if (text.isRightAligned()) {
                curserX = line.getMaxLength() - line.getLineLength();
            }
            double extraSpace = !text.isJustified() || i == lines.size() - 1 ? 0.0 : (line.getMaxLength() - line.getLineLength()) / (double)(line.getWords().size() - 1);
            for (Word word : line.getWords()) {
                for (Character letter : word.getCharacters()) {
                    this.addVerticesForCharacter(curserX, curserY, letter, text.getFontSize(), vertices);
                    TextLoader.addTextCoords(textureCoords, letter.getxTextureCoord(), letter.getyTextureCoord(), letter.getXMaxTextureCoord(), letter.getYMaxTextureCoord());
                    curserX += letter.getxAdvance() * (double)text.getFontSize();
                }
                curserX += this.metaData.getSpaceWidth() * (double)text.getFontSize() + extraSpace;
                if (!first || !text.isIndented()) continue;
                indent = curserX;
                first = false;
            }
            curserY += (double)0.04f * (double)text.getFontSize();
            ++i;
        }
        float[] verticesArray = this.listToArray(vertices);
        float[] textureArray = this.listToArray(textureCoords);
        int vao = Loader.createInterleavedVAO(vertices.size() / 2, verticesArray, textureArray);
        text.setMeshInfo(vao, vertices.size() / 2, (float)lines.get(0).getLineLength(), (float)curserY);
    }

    private void addVerticesForCharacter(double curserX, double curserY, Character character, double fontSize, List<Float> vertices) {
        double x = curserX + character.getxOffset() * fontSize;
        double y = curserY + character.getyOffset() * fontSize;
        double maxX = x + character.getSizeX() * fontSize;
        double maxY = y + character.getSizeY() * fontSize;
        TextLoader.addVertices(vertices, x, y, maxX, maxY);
    }

    private static void addVertices(List<Float> vertices, double x, double y, double maxX, double maxY) {
        vertices.add(Float.valueOf((float)x));
        vertices.add(Float.valueOf((float)y));
        vertices.add(Float.valueOf((float)x));
        vertices.add(Float.valueOf((float)maxY));
        vertices.add(Float.valueOf((float)maxX));
        vertices.add(Float.valueOf((float)maxY));
        vertices.add(Float.valueOf((float)maxX));
        vertices.add(Float.valueOf((float)maxY));
        vertices.add(Float.valueOf((float)maxX));
        vertices.add(Float.valueOf((float)y));
        vertices.add(Float.valueOf((float)x));
        vertices.add(Float.valueOf((float)y));
    }

    private static void addTextCoords(List<Float> texCoords, double x, double y, double maxX, double maxY) {
        texCoords.add(Float.valueOf((float)x));
        texCoords.add(Float.valueOf((float)y));
        texCoords.add(Float.valueOf((float)x));
        texCoords.add(Float.valueOf((float)maxY));
        texCoords.add(Float.valueOf((float)maxX));
        texCoords.add(Float.valueOf((float)maxY));
        texCoords.add(Float.valueOf((float)maxX));
        texCoords.add(Float.valueOf((float)maxY));
        texCoords.add(Float.valueOf((float)maxX));
        texCoords.add(Float.valueOf((float)y));
        texCoords.add(Float.valueOf((float)x));
        texCoords.add(Float.valueOf((float)y));
    }

    private void setTextSettings(Text text, List<Line> lines) {
        text.setNumberOfLines(lines.size());
        if (text.isCentered() || lines.size() > 1) {
            text.setOriginalWidth((float)lines.get(0).getMaxLength());
        } else {
            text.setOriginalWidth((float)lines.get(0).getLineLength());
        }
    }

    private float[] listToArray(List<Float> list) {
        float[] array = new float[list.size()];
        int i = 0;
        while (i < array.length) {
            array[i] = list.get(i).floatValue();
            ++i;
        }
        return array;
    }
}

