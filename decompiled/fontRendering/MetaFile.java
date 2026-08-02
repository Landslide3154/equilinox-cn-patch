/*
 * Decompiled with CFR 0.152.
 */
package fontRendering;

import basics.DisplayManager;
import fontRendering.Character;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import utils.MyFile;

public class MetaFile {
    private static final char QUOTE_REPLACEMENT = '^';
    private static final int QUOTE_REPLACE = 94;
    private static final int PAD_TOP = 0;
    private static final int PAD_LEFT = 1;
    private static final int PAD_BOTTOM = 2;
    private static final int PAD_RIGHT = 3;
    private static final int DESIRED_PADDING = 10;
    private static final String SPLITTER = " ";
    private static final String NUMBER_SEPARATOR = ",";
    private double verticalPerPixelSize;
    private double horizontalPerPixelSize;
    private double spaceWidth;
    private int[] padding;
    private int paddingWidth;
    private int paddingHeight;
    private Map<Integer, Character> metaData = new HashMap<Integer, Character>();
    private BufferedReader reader;
    private Map<String, String> values = new HashMap<String, String>();

    protected MetaFile(MyFile file) {
        this.openFile(file);
        this.loadPaddingData();
        this.loadLineSizes();
        int imageWidth = this.getValueOfVariable("scaleW");
        this.loadCharacterData(imageWidth);
        this.close();
    }

    protected double getSpaceWidth() {
        return this.spaceWidth;
    }

    protected Character getCharacter(int ascii) {
        return this.metaData.get(ascii);
    }

    private boolean processNextLine() {
        this.values.clear();
        String line = null;
        try {
            line = this.reader.readLine();
        }
        catch (IOException iOException) {
            // empty catch block
        }
        if (line == null) {
            return false;
        }
        String[] stringArray = line.split(SPLITTER);
        int n = stringArray.length;
        int n2 = 0;
        while (n2 < n) {
            String part = stringArray[n2];
            String[] valuePairs = part.split("=");
            if (valuePairs.length == 2) {
                this.values.put(valuePairs[0], valuePairs[1]);
            }
            ++n2;
        }
        return true;
    }

    private int getValueOfVariable(String variable) {
        return Integer.parseInt(this.values.get(variable));
    }

    private int[] getValuesOfVariable(String variable) {
        String[] numbers = this.values.get(variable).split(NUMBER_SEPARATOR);
        int[] actualValues = new int[numbers.length];
        int i = 0;
        while (i < actualValues.length) {
            actualValues[i] = Integer.parseInt(numbers[i]);
            ++i;
        }
        return actualValues;
    }

    private void close() {
        try {
            this.reader.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openFile(MyFile file) {
        try {
            this.reader = file.getReader();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Couldn't read font meta file!");
        }
    }

    private void loadPaddingData() {
        this.processNextLine();
        this.padding = this.getValuesOfVariable("padding");
        this.paddingWidth = this.padding[1] + this.padding[3];
        this.paddingHeight = this.padding[0] + this.padding[2];
    }

    private void loadLineSizes() {
        this.processNextLine();
        int lineHeightPixels = this.getValueOfVariable("lineHeight") - this.paddingHeight;
        this.verticalPerPixelSize = (double)0.04f / (double)lineHeightPixels;
        this.horizontalPerPixelSize = this.verticalPerPixelSize / (double)DisplayManager.getAspectRatio();
    }

    private void loadCharacterData(int imageWidth) {
        this.processNextLine();
        this.processNextLine();
        while (this.processNextLine()) {
            Character c = this.loadCharacter(imageWidth);
            if (c == null || c.getId() == 94) continue;
            if (c.getId() == 34) {
                this.metaData.put(94, c);
            }
            this.metaData.put(c.getId(), c);
        }
    }

    private Character loadCharacter(int imageSize) {
        int id = this.getValueOfVariable("id");
        if (id == 32) {
            this.spaceWidth = (double)(this.getValueOfVariable("xadvance") - this.paddingWidth) * this.horizontalPerPixelSize;
            return null;
        }
        double xTex = ((double)this.getValueOfVariable("x") + (double)(this.padding[1] - 10)) / (double)imageSize;
        double yTex = ((double)this.getValueOfVariable("y") + (double)(this.padding[0] - 10)) / (double)imageSize;
        int width = this.getValueOfVariable("width") - (this.paddingWidth - 20);
        int height = this.getValueOfVariable("height") - (this.paddingHeight - 20);
        double quadWidth = (double)width * this.horizontalPerPixelSize;
        double quadHeight = (double)height * this.verticalPerPixelSize;
        double xTexSize = (double)width / (double)imageSize;
        double yTexSize = (double)height / (double)imageSize;
        double xOff = (double)(this.getValueOfVariable("xoffset") + this.padding[1] - 10) * this.horizontalPerPixelSize;
        double yOff = (double)(this.getValueOfVariable("yoffset") + (this.padding[0] - 10)) * this.verticalPerPixelSize;
        double xAdvance = (double)(this.getValueOfVariable("xadvance") - this.paddingWidth) * this.horizontalPerPixelSize;
        return new Character(id, xTex, yTex, xTexSize, yTexSize, xOff, yOff, quadWidth, quadHeight, xAdvance);
    }
}

