/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import graphicsOptions.GraphicsOptions;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import toolbox.Colour;

public class OpenglUtils {
    private static boolean cullingBackFace = false;
    private static boolean inWireframe = false;
    private static boolean isAlphaBlending = false;
    private static boolean additiveBlending = false;
    private static boolean antialiasing = false;
    private static boolean scissorTest = false;
    private static int[] scissorBounds = new int[4];

    public static void prepareNewRenderPass(Colour skyColour) {
        OpenglUtils.prepareNewRenderPass(skyColour, 1.0f);
    }

    public static void prepareNewRenderPass(Colour skyColour, float alpha) {
        GL11.glClearColor(skyColour.getR(), skyColour.getG(), skyColour.getB(), alpha);
        GL11.glClear(16640);
        OpenglUtils.disableBlending();
        OpenglUtils.cullBackFaces(true);
        OpenglUtils.enableDepthTesting();
    }

    public static void prepareNewRenderPassOnlyDepth() {
        GL11.glClear(256);
        OpenglUtils.disableBlending();
        OpenglUtils.cullBackFaces(true);
        OpenglUtils.enableDepthTesting();
    }

    public static void enableScissorTest(int x, int y, int width, int height) {
        if (!scissorTest) {
            scissorTest = true;
            GL11.glEnable(3089);
        }
        if (x != scissorBounds[0] || y != scissorBounds[1] || width != scissorBounds[2] || height != scissorBounds[3]) {
            GL11.glScissor(x, y, width, height);
            OpenglUtils.scissorBounds[0] = x;
            OpenglUtils.scissorBounds[1] = y;
            OpenglUtils.scissorBounds[2] = width;
            OpenglUtils.scissorBounds[3] = height;
        }
    }

    public static void disableScissorTest() {
        if (scissorTest) {
            scissorTest = false;
            GL11.glDisable(3089);
        }
    }

    public static void prepareNewRenderParse(Colour skyColour, float alpha) {
        GL11.glClearColor(skyColour.getR(), skyColour.getG(), skyColour.getB(), alpha);
        GL11.glClear(16640);
        OpenglUtils.disableBlending();
        OpenglUtils.cullBackFaces(true);
        OpenglUtils.enableDepthTesting();
    }

    public static void antialias(boolean enable) {
        if (!GraphicsOptions.ANTI_ALIASING) {
            return;
        }
        if (enable && !antialiasing) {
            GL11.glEnable(32925);
            antialiasing = true;
        } else if (!enable && antialiasing) {
            GL11.glDisable(32925);
            antialiasing = false;
        }
    }

    public static void enableAlphaBlending() {
        if (!isAlphaBlending) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            isAlphaBlending = true;
            additiveBlending = false;
        }
    }

    public static void enableAdditiveBlending() {
        if (!additiveBlending) {
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 1);
            additiveBlending = true;
            isAlphaBlending = false;
        }
    }

    public static void disableBlending() {
        if (isAlphaBlending || additiveBlending) {
            GL11.glDisable(3042);
            isAlphaBlending = false;
            additiveBlending = false;
        }
    }

    public static void disableDepthTesting() {
        GL11.glDisable(2929);
    }

    public static void enableDepthTesting() {
        GL11.glEnable(2929);
    }

    public static void bindVAO(int vaoID, int ... attributes) {
        GL30.glBindVertexArray(vaoID);
        int[] nArray = attributes;
        int n = attributes.length;
        int n2 = 0;
        while (n2 < n) {
            int i = nArray[n2];
            GL20.glEnableVertexAttribArray(i);
            ++n2;
        }
    }

    public static void unbindVAO(int ... attributes) {
        int[] nArray = attributes;
        int n = attributes.length;
        int n2 = 0;
        while (n2 < n) {
            int i = nArray[n2];
            GL20.glDisableVertexAttribArray(i);
            ++n2;
        }
        GL30.glBindVertexArray(0);
    }

    public static void bindTextureToBank(int textureID, int bankID) {
        GL13.glActiveTexture(33984 + bankID);
        GL11.glBindTexture(3553, textureID);
    }

    public static void bindTextureToBank(int textureID, int bankID, int lodBias) {
        GL13.glActiveTexture(33984 + bankID);
        GL11.glBindTexture(3553, textureID);
        GL11.glTexParameteri(3553, 33084, lodBias);
        GL13.glActiveTexture(0);
    }

    public static void cullBackFaces(boolean cull) {
        if (cull && !cullingBackFace) {
            GL11.glEnable(2884);
            GL11.glCullFace(1029);
            cullingBackFace = true;
        } else if (!cull && cullingBackFace) {
            GL11.glDisable(2884);
            cullingBackFace = false;
        }
    }

    public static void goWireframe(boolean goWireframe) {
        if (goWireframe && !inWireframe) {
            GL11.glPolygonMode(1032, 6913);
            inWireframe = true;
        } else if (!goWireframe && inWireframe) {
            GL11.glPolygonMode(1032, 6914);
            inWireframe = false;
        }
    }
}

