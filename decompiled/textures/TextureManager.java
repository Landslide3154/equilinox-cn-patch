/*
 * Decompiled with CFR 0.152.
 */
package textures;

import de.matthiasmann.twl.utils.PNGDecoder;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import textures.TextureBuilder;
import textures.TextureData;
import utils.MyFile;

public class TextureManager {
    private static List<Integer> textureCache = new ArrayList<Integer>();

    public static void cleanUp() {
        for (Integer textureID : textureCache) {
            GL11.glDeleteTextures(textureID);
        }
    }

    public static int loadCubeMapSameImage(MyFile textureFile) {
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(33984);
        GL11.glBindTexture(34067, texID);
        GL11.glPixelStorei(3317, 1);
        TextureData data = TextureManager.decodeTextureFile(textureFile);
        int i = 0;
        while (i < 6) {
            GL11.glTexImage2D(34069 + i, 0, 6408, data.getWidth(), data.getHeight(), 0, 32993, 5121, data.getBuffer());
            ++i;
        }
        GL11.glTexParameteri(34067, 10240, 9729);
        GL11.glTexParameteri(34067, 10241, 9729);
        GL11.glTexParameteri(34067, 10242, 33071);
        GL11.glTexParameteri(34067, 10243, 33071);
        GL11.glBindTexture(34067, 0);
        textureCache.add(texID);
        return texID;
    }

    protected static TextureData decodeTextureFile(MyFile file) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            InputStream in = file.getInputStream();
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.BGRA);
            buffer.flip();
            in.close();
        }
        catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + file.getName() + " , didn't work");
            System.exit(-1);
        }
        return new TextureData(buffer, width, height);
    }

    protected static int loadTextureToOpenGL(TextureData data, TextureBuilder builder) {
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(33984);
        GL11.glBindTexture(3553, texID);
        GL11.glPixelStorei(3317, 1);
        GL11.glTexImage2D(3553, 0, 6408, data.getWidth(), data.getHeight(), 0, 32993, 5121, data.getBuffer());
        if (builder.isMipmap()) {
            GL30.glGenerateMipmap(3553);
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10241, 9987);
            if (builder.isAnisotropic()) {
                GL11.glTexParameterf(3553, 34049, 0.0f);
                GL11.glTexParameterf(3553, 34046, 4.0f);
            }
        } else if (builder.isNearest()) {
            GL11.glTexParameteri(3553, 10240, 9728);
            GL11.glTexParameteri(3553, 10241, 9728);
        } else {
            GL11.glTexParameteri(3553, 10240, 9729);
            GL11.glTexParameteri(3553, 10241, 9729);
        }
        if (builder.isClampEdges()) {
            GL11.glTexParameteri(3553, 10242, 33071);
            GL11.glTexParameteri(3553, 10243, 33071);
        } else if (builder.isClampToBorder()) {
            GL11.glTexParameteri(3553, 10242, 33069);
            GL11.glTexParameteri(3553, 10243, 33069);
            GL11.glTexParameter(3553, 4100, builder.getBorderColour().getAsFloatBuffer());
        } else {
            GL11.glTexParameteri(3553, 10242, 10497);
            GL11.glTexParameteri(3553, 10243, 10497);
        }
        textureCache.add(texID);
        return texID;
    }

    protected static void deleteTexture(Integer textureID) {
        textureCache.remove(textureID);
        GL11.glDeleteTextures(textureID);
    }
}

