/*
 * Decompiled with CFR 0.152.
 */
package environment;

import org.lwjgl.util.vector.Vector3f;
import terrains.TerrainVertex;
import toolbox.Colour;

public interface EnviroFactor {
    public String getName();

    public String getValue();

    public Colour getColour();

    public float getInfluence();

    public float recalculate(Vector3f var1, TerrainVertex var2, int var3);
}

