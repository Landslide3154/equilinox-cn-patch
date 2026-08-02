/*
 * Decompiled with CFR 0.152.
 */
package blueprints;

import org.lwjgl.util.vector.Vector3f;
import utils.CSVReader;

public class ModelLoader {
    public static float[] loadModel(CSVReader reader, float size) {
        reader.nextLine();
        int dataCount = reader.getNextInt() * 10;
        int sectionsCount = reader.getNextInt();
        float[] data = new float[dataCount];
        int pointer = 0;
        int k = 0;
        while (k < sectionsCount) {
            reader.nextLine();
            int vertexCount = reader.getNextInt();
            Vector3f colour = reader.getNextVector();
            float wobbleFactor = 0.0f;
            if (!reader.isEndOfLine()) {
                wobbleFactor = reader.getNextFloat();
            }
            reader.nextLine();
            int i = 0;
            while (i < vertexCount) {
                Vector3f position = reader.getNextVector();
                data[pointer++] = position.x * size;
                data[pointer++] = position.y * size;
                data[pointer++] = position.z * size;
                data[pointer++] = position.y * size * wobbleFactor;
                int j = 0;
                while (j < 3) {
                    data[pointer++] = reader.getNextFloat();
                    ++j;
                }
                data[pointer++] = colour.x;
                data[pointer++] = colour.y;
                data[pointer++] = colour.z;
                ++i;
            }
            ++k;
        }
        return data;
    }
}

