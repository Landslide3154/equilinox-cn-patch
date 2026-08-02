/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import utils.MyFile;

public class IconLoader {
    public static ByteBuffer[] load(MyFile file16, MyFile file32, MyFile file128) {
        ByteBuffer[] buffers = null;
        try {
            InputStream stream16 = file16.getInputStream();
            InputStream stream32 = file32.getInputStream();
            InputStream stream128 = file128.getInputStream();
            BufferedImage image16 = ImageIO.read(stream16);
            BufferedImage image32 = ImageIO.read(stream32);
            BufferedImage image128 = ImageIO.read(stream128);
            String OS = System.getProperty("os.name").toUpperCase();
            buffers = OS.contains("WIN") ? new ByteBuffer[]{IconLoader.loadInstance(image16, 16), IconLoader.loadInstance(image32, 32)} : (OS.contains("MAC") ? new ByteBuffer[]{IconLoader.loadInstance(image128, 128)} : new ByteBuffer[]{IconLoader.loadInstance(image32, 32)});
            stream16.close();
            stream32.close();
            stream128.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return buffers;
    }

    private static ByteBuffer loadInstance(BufferedImage image, int dimension) {
        BufferedImage scaledIcon = new BufferedImage(dimension, dimension, 3);
        Graphics2D g = scaledIcon.createGraphics();
        double ratio = IconLoader.getIconRatio(image, scaledIcon);
        double width = (double)image.getWidth() * ratio;
        double height = (double)image.getHeight() * ratio;
        g.drawImage(image, (int)(((double)scaledIcon.getWidth() - width) / 2.0), (int)(((double)scaledIcon.getHeight() - height) / 2.0), (int)width, (int)height, null);
        g.dispose();
        return IconLoader.convertToByteBuffer(scaledIcon);
    }

    private static double getIconRatio(BufferedImage src, BufferedImage icon) {
        double ratio = 1.0;
        ratio = src.getWidth() > icon.getWidth() ? (double)icon.getWidth() / (double)src.getWidth() : (double)(icon.getWidth() / src.getWidth());
        if (src.getHeight() > icon.getHeight()) {
            double r2 = (double)icon.getHeight() / (double)src.getHeight();
            if (r2 < ratio) {
                ratio = r2;
            }
        } else {
            double r2 = icon.getHeight() / src.getHeight();
            if (r2 < ratio) {
                ratio = r2;
            }
        }
        return ratio;
    }

    public static ByteBuffer convertToByteBuffer(BufferedImage image) {
        byte[] buffer = new byte[image.getWidth() * image.getHeight() * 4];
        int counter = 0;
        int i = 0;
        while (i < image.getHeight()) {
            int j = 0;
            while (j < image.getWidth()) {
                int colorSpace = image.getRGB(j, i);
                buffer[counter + 0] = (byte)(colorSpace << 8 >> 24);
                buffer[counter + 1] = (byte)(colorSpace << 16 >> 24);
                buffer[counter + 2] = (byte)(colorSpace << 24 >> 24);
                buffer[counter + 3] = (byte)(colorSpace >> 24);
                counter += 4;
                ++j;
            }
            ++i;
        }
        return ByteBuffer.wrap(buffer);
    }
}

