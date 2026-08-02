/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Calendar;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import postProcessing.Fbo;
import utils.FileUtils;

public class ScreenshotTaker {
    private static final String SCREENSHOT_FOLDER = "screenshots";
    private static final String SCREENSHOT_NAME = "screenshot_";
    private static final String FORMAT = "PNG";
    private static final int BYTES = 4;

    public static void takeScreenshot(Fbo fbo) {
        ByteBuffer imageData = ScreenshotTaker.getImageData(fbo);
        BufferedImage image = ScreenshotTaker.createBufferedImage(imageData, fbo.getWidth(), fbo.getHeight());
        ScreenshotTaker.saveScreenshot(image);
    }

    private static void saveScreenshot(BufferedImage image) {
        File folder = new File(FileUtils.getRootFolder(), SCREENSHOT_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(folder, SCREENSHOT_NAME + Calendar.getInstance().getTime().getTime() + ".png");
        try {
            ImageIO.write((RenderedImage)image, FORMAT, file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedImage createBufferedImage(ByteBuffer imageData, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, 2);
        int x = 0;
        while (x < width) {
            int y = 0;
            while (y < height) {
                int i = (x + width * y) * 4;
                int r = imageData.get(i) & 0xFF;
                int g = imageData.get(i + 1) & 0xFF;
                int b = imageData.get(i + 2) & 0xFF;
                int a = imageData.get(i + 3) & 0xFF;
                image.setRGB(x, height - (y + 1), a << 24 | r << 16 | g << 8 | b);
                ++y;
            }
            ++x;
        }
        return image;
    }

    private static ByteBuffer getImageData(Fbo fbo) {
        fbo.bindToRead();
        int width = fbo.getWidth();
        int height = fbo.getHeight();
        ByteBuffer buffer = BufferUtils.createByteBuffer(fbo.getWidth() * fbo.getHeight() * 4);
        GL11.glReadPixels(0, 0, width, height, 6408, 5121, buffer);
        fbo.unbindFrameBuffer();
        return buffer;
    }
}

