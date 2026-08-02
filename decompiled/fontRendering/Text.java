/*
 * Decompiled with CFR 0.152.
 */
package fontRendering;

import basics.DisplayManager;
import basics.Loader;
import fontRendering.FontType;
import fontRendering.TextBuilder;
import guis.GuiComponent;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Colour;
import visualFxDrivers.ColourBounceDriver;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.ValueDriver;

public class Text {
    private static final float BOLD_FACTOR = 0.017f;
    private static final float SCREEN_FACTOR = (float)DisplayManager.getUiHeight() / 720.0f;
    private String textString;
    private final float fontSize;
    private final FontType fontType;
    private final boolean centerText;
    private final boolean rightAlignText;
    private final boolean justify;
    private final boolean indent;
    private int textMesh;
    private int vertexCount;
    private float lineMaxSize;
    private int numberOfLines;
    private float originalWidth;
    private int[] scissorTestInfo = null;
    private ValueDriver alphaDriver = new ConstantDriver(1.0f);
    private ValueDriver scaleDriver = new ConstantDriver(1.0f);
    private ValueDriver glowDriver = new ConstantDriver(0.0f);
    private ValueDriver borderDriver = new ConstantDriver(0.0f);
    private ColourBounceDriver colourDriver = null;
    private Colour colour = new Colour(0.0f, 0.0f, 0.0f);
    private Colour borderColour = new Colour(1.0f, 1.0f, 1.0f);
    private boolean solidBorder = false;
    private boolean glowBorder = false;
    private float effectScale = 1.0f;
    private float parentScaleFactor = 1.0f;
    private float currentX;
    private float currentY;
    private float currentAlpha;
    private float glowSize = 0.0f;
    private float borderSize = 0.0f;
    private float originalLineWidth;
    private Vector3f relativePosition;
    private GuiComponent parent;
    private float originalHeight;
    private float actualTextWidth;
    private boolean empty = false;
    private boolean expandCenter = true;
    private boolean loaded = false;
    private int level;
    private boolean hasSpecificLevel = false;

    protected Text(String text, FontType font, float fontSize, boolean centered, boolean rightAligned, boolean justified, boolean indent) {
        this.textString = text;
        this.fontSize = fontSize;
        this.fontType = font;
        this.justify = justified;
        this.centerText = centered;
        this.rightAlignText = rightAligned;
        this.indent = indent;
        boolean bl = this.empty = this.textString.length() == 0;
        if (this.empty) {
            this.textString = ".";
        }
    }

    public static TextBuilder newText(String text) {
        return new TextBuilder(text);
    }

    public FontType getFontType() {
        return this.fontType;
    }

    public void setRenderLevel(int level) {
        this.level = level;
        this.hasSpecificLevel = true;
    }

    public int getRenderLevel() {
        return this.level;
    }

    public void initialise(float absX, float absY, float maxXLength) {
        this.currentX = absX;
        this.currentY = absY;
        this.lineMaxSize = maxXLength;
        if (!this.loaded) {
            this.originalLineWidth = maxXLength;
            this.fontType.loadText(this);
            this.loaded = true;
            if (this.empty) {
                this.actualTextWidth = 0.0f;
            }
        } else {
            this.parentScaleFactor = maxXLength / this.originalLineWidth;
        }
    }

    public void setParentInfo(GuiComponent parent, Vector3f relPos) {
        this.relativePosition = relPos;
        this.parent = parent;
        if (!this.hasSpecificLevel) {
            this.level = parent.getLevel();
        }
    }

    public void expandCenter(boolean center) {
        this.expandCenter = center;
    }

    public void setAbsPosition(float x, float y) {
        this.currentX = x;
        this.currentY = y;
    }

    public void setAbsX(float x) {
        this.currentX = x;
    }

    public boolean isJustified() {
        return this.justify;
    }

    public void setScaleDriver(ValueDriver scaleDriver) {
        this.scaleDriver = scaleDriver;
    }

    public float getRelativeX() {
        return this.relativePosition.x;
    }

    public float getRelativeY() {
        return this.relativePosition.y;
    }

    public void setRelativeX(float x) {
        this.relativePosition.x = x;
        this.parent.updateTextAbsPos(this);
    }

    public void setRelativeY(float y) {
        this.relativePosition.y = y;
        this.parent.updateTextAbsPos(this);
    }

    public void increaseRelativeX(float dX) {
        this.relativePosition.x += dX;
        this.parent.updateTextAbsPos(this);
    }

    public void setBorder(ValueDriver driver) {
        this.borderDriver = driver;
        this.solidBorder = true;
        this.glowBorder = false;
    }

    public void makeBold() {
        this.setBorder(new ConstantDriver(this.fontSize * 0.017f));
        this.setOutlineColour(this.colour);
    }

    public void setGlowing(ValueDriver driver) {
        this.solidBorder = false;
        this.glowBorder = true;
        this.glowDriver = driver;
    }

    public void removeBorder() {
        this.solidBorder = false;
        this.glowBorder = false;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public void setAlphaDriver(ValueDriver driver) {
        this.alphaDriver = driver;
    }

    public void setText(String newText) {
        if (newText.equals(this.textString)) {
            return;
        }
        this.textString = newText;
        if (this.loaded) {
            this.deleteFromMemory();
            boolean bl = this.empty = newText.length() == 0;
            if (this.empty) {
                this.actualTextWidth = 0.0f;
            } else {
                this.fontType.loadText(this);
            }
        }
    }

    public void deleteFromMemory() {
        if (!this.empty) {
            Loader.deleteVaoFromCache(this.textMesh);
        }
    }

    public void setColourDriver(Colour peak, Colour end, float length) {
        this.colourDriver = new ColourBounceDriver(this.colour, peak, end, length);
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public void setOutlineColour(Colour colour) {
        this.borderColour.setColour(colour);
    }

    public Colour getColour() {
        return this.colour;
    }

    public float getScale() {
        return this.effectScale * this.parentScaleFactor;
    }

    public int getNumberOfLines() {
        return this.numberOfLines;
    }

    public float getHeight() {
        return this.originalHeight * this.getScale();
    }

    public float getActualWidth() {
        return this.actualTextWidth * this.getScale();
    }

    public float getBorderSize() {
        return this.borderSize;
    }

    public float getGlowSize() {
        if (this.solidBorder) {
            return this.calculateAntialiasSize();
        }
        if (this.glowBorder) {
            return this.glowSize;
        }
        return 0.0f;
    }

    public float getCurrentWidth() {
        return this.originalWidth * this.effectScale;
    }

    public void update(float delta) {
        this.effectScale = this.scaleDriver.update(delta);
        this.currentAlpha = this.alphaDriver.update(delta);
        this.glowSize = this.glowDriver.update(delta);
        this.borderSize = this.borderDriver.update(delta);
        if (this.colourDriver != null) {
            this.colourDriver.update(delta);
        }
    }

    public String getTextString() {
        return this.textString;
    }

    public void setClippingBounds(int[] clippingBounds) {
        this.scissorTestInfo = clippingBounds;
    }

    public int[] getClippingBounds() {
        return this.scissorTestInfo;
    }

    protected float calculateAntialiasSize() {
        float size = this.fontSize * this.getScale() * SCREEN_FACTOR;
        return this.fontType.getCalculator().calculateAntialiasValue(size);
    }

    protected float calculateEdgeStart() {
        float size = this.fontSize * this.getScale() * SCREEN_FACTOR;
        return this.fontType.getCalculator().calculateEdgeValue(size);
    }

    protected float getTransparency() {
        return this.currentAlpha;
    }

    public float getCurrentX() {
        return this.currentX;
    }

    public float getCurrentEffectScale() {
        return this.effectScale;
    }

    protected Vector2f getPosition() {
        float scaleFactor = (this.effectScale - 1.0f) / 2.0f;
        float xChange = this.expandCenter ? scaleFactor * this.originalWidth : 0.0f;
        float yChange = scaleFactor * 0.04f * this.fontSize * (float)this.numberOfLines * 1.0f;
        return new Vector2f(this.currentX - xChange, this.currentY - yChange);
    }

    protected float getTotalBorderSize() {
        if (this.solidBorder) {
            if (this.borderSize == 0.0f) {
                return 0.0f;
            }
            return this.calculateEdgeStart() + this.borderSize;
        }
        if (this.glowBorder) {
            return this.calculateEdgeStart();
        }
        return 0.0f;
    }

    protected int getMesh() {
        return this.textMesh;
    }

    protected int getVertexCount() {
        return this.vertexCount;
    }

    protected float getFontSize() {
        return this.fontSize;
    }

    protected Colour getBorderColour() {
        return this.borderColour;
    }

    protected void setMeshInfo(int vao, int verticesCount, float width, float height) {
        this.textMesh = vao;
        this.vertexCount = verticesCount;
        this.actualTextWidth = width;
        this.originalHeight = height;
    }

    protected void setNumberOfLines(int number) {
        this.numberOfLines = number;
    }

    protected void setOriginalWidth(float width) {
        this.originalWidth = width;
    }

    protected boolean isCentered() {
        return this.centerText;
    }

    protected boolean isIndented() {
        return this.indent;
    }

    protected boolean isRightAligned() {
        return this.rightAlignText;
    }

    protected float getMaxLineSize() {
        return this.originalLineWidth;
    }
}

