/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import org.lwjgl.opengl.GL15;

public class Query {
    private final int id = GL15.glGenQueries();
    private final int type;
    private boolean inUse = false;

    public Query(int type) {
        this.type = type;
    }

    public void start() {
        this.inUse = true;
        GL15.glBeginQuery(this.type, this.id);
    }

    public void end() {
        GL15.glEndQuery(this.type);
    }

    public boolean isResultReady() {
        return GL15.glGetQueryObjecti(this.id, 34919) == 1;
    }

    public boolean isInUse() {
        return this.inUse;
    }

    public int getResult() {
        this.inUse = false;
        return GL15.glGetQueryObjecti(this.id, 34918);
    }

    public void delete() {
        GL15.glDeleteQueries(this.id);
    }
}

