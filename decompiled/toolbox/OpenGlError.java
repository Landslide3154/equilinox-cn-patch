/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import org.lwjgl.opengl.GL11;

public class OpenGlError {
    public static boolean check() {
        return OpenGlError.check("Default");
    }

    public static boolean check(String message) {
        int error = GL11.glGetError();
        if (error == 0) {
            return true;
        }
        switch (error) {
            case 1280: {
                System.err.println("OPENGL ERROR REPORT - " + message + " - Invalid Enum!");
                break;
            }
            case 1281: {
                System.err.println("OPENGL ERROR REPORT - " + message + " - Invalid Value!");
                break;
            }
            case 1282: {
                System.err.println("OPENGL ERROR REPORT - " + message + " - Invalid Operation!");
                break;
            }
            case 1286: {
                System.err.println("OPENGL ERROR REPORT - " + message + " - Invalid Framebuffer operation!");
                break;
            }
            case 1285: {
                System.err.println("OPENGL ERROR REPORT - " + message + " - Out of Memory!");
            }
        }
        return false;
    }
}

