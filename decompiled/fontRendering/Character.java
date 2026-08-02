/*
 * Decompiled with CFR 0.152.
 */
package fontRendering;

public class Character {
    private int id;
    private double xTextureCoord;
    private double yTextureCoord;
    private double xMaxTextureCoord;
    private double yMaxTextureCoord;
    private double xOffset;
    private double yOffset;
    private double sizeX;
    private double sizeY;
    private double xAdvance;

    protected Character(int id, double xTextureCoord, double yTextureCoord, double xTexSize, double yTexSize, double xOffset, double yOffset, double sizeX, double sizeY, double xAdvance) {
        this.id = id;
        this.xTextureCoord = xTextureCoord;
        this.yTextureCoord = yTextureCoord;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.xMaxTextureCoord = xTexSize + xTextureCoord;
        this.yMaxTextureCoord = yTexSize + yTextureCoord;
        this.xAdvance = xAdvance;
    }

    protected int getId() {
        return this.id;
    }

    protected double getxTextureCoord() {
        return this.xTextureCoord;
    }

    protected double getyTextureCoord() {
        return this.yTextureCoord;
    }

    protected double getXMaxTextureCoord() {
        return this.xMaxTextureCoord;
    }

    protected double getYMaxTextureCoord() {
        return this.yMaxTextureCoord;
    }

    protected double getxOffset() {
        return this.xOffset;
    }

    protected double getyOffset() {
        return this.yOffset;
    }

    protected double getSizeX() {
        return this.sizeX;
    }

    protected double getSizeY() {
        return this.sizeY;
    }

    protected double getxAdvance() {
        return this.xAdvance;
    }
}

