/*
 * Decompiled with CFR 0.152.
 */
package fbos;

public abstract class Attachment {
    private int bufferId;
    private boolean isDepthAttach = false;

    public int getBufferId() {
        return this.bufferId;
    }

    public abstract void init(int var1, int var2, int var3, int var4);

    public abstract void delete();

    protected void setBufferId(int id) {
        this.bufferId = id;
    }

    protected void setAsDepthAttachment() {
        this.isDepthAttach = true;
    }

    protected boolean isDepthAttachment() {
        return this.isDepthAttach;
    }
}

