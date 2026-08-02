/*
 * Decompiled with CFR 0.152.
 */
package openglObjects;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL33;

public class Attribute {
    protected final int attributeNumber;
    protected final int dataType;
    protected final boolean normalized;
    protected final int componentCount;
    protected final int bytesPerVertex;
    private final boolean instanceData;

    public Attribute(int attrNumber, int dataType, int componentCount) {
        this.attributeNumber = attrNumber;
        this.dataType = dataType;
        this.componentCount = componentCount;
        this.normalized = false;
        this.instanceData = false;
        this.bytesPerVertex = this.calcBytesPerVertex();
    }

    public Attribute(int attrNumber, int dataType, int componentCount, boolean normalized) {
        this.attributeNumber = attrNumber;
        this.dataType = dataType;
        this.componentCount = componentCount;
        this.normalized = normalized;
        this.instanceData = false;
        this.bytesPerVertex = this.calcBytesPerVertex();
    }

    public Attribute(int attrNumber, int dataType, int componentCount, boolean normalized, boolean instanceData) {
        this.attributeNumber = attrNumber;
        this.dataType = dataType;
        this.componentCount = componentCount;
        this.normalized = normalized;
        this.instanceData = instanceData;
        this.bytesPerVertex = this.calcBytesPerVertex();
    }

    protected void enable(boolean enable) {
        if (enable) {
            GL20.glEnableVertexAttribArray(this.attributeNumber);
        } else {
            GL20.glDisableVertexAttribArray(this.attributeNumber);
        }
    }

    protected void link(int offset, int stride) {
        GL20.glVertexAttribPointer(this.attributeNumber, this.componentCount, this.dataType, this.normalized, stride, offset);
        if (this.instanceData) {
            GL33.glVertexAttribDivisor(this.attributeNumber, 1);
        }
    }

    private int calcBytesPerVertex() {
        if (this.dataType == 5126 || this.dataType == 5125 || this.dataType == 5124) {
            return 4 * this.componentCount;
        }
        if (this.dataType == 5122 || this.dataType == 5123) {
            return 2 * this.componentCount;
        }
        if (this.dataType == 5120 || this.dataType == 5121) {
            return 1 * this.componentCount;
        }
        if (this.dataType == 33640) {
            return 4;
        }
        System.err.println("Unsupported data type for VAO attribute: " + this.dataType);
        return 0;
    }
}

