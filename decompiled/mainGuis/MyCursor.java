/*
 * Decompiled with CFR 0.152.
 */
package mainGuis;

import guis.GuiMaster;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import utils.MyFile;

public class MyCursor {
    public static final Cursor NORMAL = MyCursor.loadCursor(new MyFile(GuiMaster.GUIS_LOC, "curse.png"), 1);
    public static final Cursor GRABBED = MyCursor.loadCursor(new MyFile(GuiMaster.GUIS_LOC, "cursorGrab.png"), 8);
    public static final Cursor GRABBED_LIGHT = MyCursor.loadCursor(new MyFile(GuiMaster.GUIS_LOC, "cursorGrab2.png"), 8);
    private static Cursor currentCursor = null;

    public static void setCursor(Cursor cursor) {
        if (currentCursor == cursor) {
            return;
        }
        currentCursor = cursor;
        try {
            Mouse.setGrabbed(false);
            Mouse.setNativeCursor(cursor);
        }
        catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    private static Cursor loadCursor(MyFile file, int hot) {
        try {
            InputStream stream = file.getInputStream();
            BufferedImage img = ImageIO.read(stream);
            int w = img.getWidth();
            int h = img.getHeight();
            int[] rgbData = new int[w * h];
            int i = 0;
            while (i < rgbData.length) {
                int x = i % w;
                int y = h - 1 - i / w;
                rgbData[i] = img.getRGB(x, y);
                ++i;
            }
            IntBuffer buffer = BufferUtils.createIntBuffer(w * h);
            buffer.put(rgbData);
            buffer.flip();
            stream.close();
            return new Cursor(w, h, hot, h - hot, 1, buffer, null);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

