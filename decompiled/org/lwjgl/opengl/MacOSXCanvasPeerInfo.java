/*
 * Decompiled with CFR 0.152.
 */
package org.lwjgl.opengl;

import java.applet.Applet;
import java.awt.Canvas;
import java.awt.Container;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.nio.ByteBuffer;
import javax.swing.SwingUtilities;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.AWTSurfaceLock;
import org.lwjgl.opengl.ContextAttribs;
import org.lwjgl.opengl.MacOSXPeerInfo;
import org.lwjgl.opengl.PixelFormat;

abstract class MacOSXCanvasPeerInfo
extends MacOSXPeerInfo {
    private final AWTSurfaceLock awt_surface = new AWTSurfaceLock();
    public ByteBuffer window_handle;

    protected MacOSXCanvasPeerInfo(PixelFormat pixel_format, ContextAttribs attribs, boolean support_pbuffer) throws LWJGLException {
        super(pixel_format, attribs, true, true, support_pbuffer, true);
    }

    protected void initHandle(Canvas component) throws LWJGLException {
        boolean forceCALayer = true;
        boolean autoResizable = true;
        String javaVersion = System.getProperty("java.version");
        if (javaVersion.startsWith("1.5") || javaVersion.startsWith("1.6")) {
            forceCALayer = false;
        } else if (javaVersion.startsWith("1.7")) {
            autoResizable = false;
        }
        Insets insets = this.getInsets(component);
        int top = insets != null ? insets.top : 0;
        int left = insets != null ? insets.left : 0;
        this.window_handle = MacOSXCanvasPeerInfo.nInitHandle(this.awt_surface.lockAndGetHandle(component), this.getHandle(), this.window_handle, forceCALayer, autoResizable, component.getX() - left, component.getY() - top);
        if (javaVersion.startsWith("1.7")) {
            this.addComponentListener(component);
            if (SwingUtilities.getWindowAncestor(component.getParent()) != null) {
                Point componentPosition = SwingUtilities.convertPoint(component, component.getLocation(), null);
                Point parentPosition = SwingUtilities.convertPoint(component.getParent(), component.getLocation(), null);
                if (componentPosition.getX() == parentPosition.getX() && componentPosition.getY() == parentPosition.getY()) {
                    insets = this.getWindowInsets(component);
                    top = insets != null ? insets.top : 0;
                    left = insets != null ? insets.left : 0;
                    int x = (int)componentPosition.getX() - left;
                    int y = (int)(-componentPosition.getY()) + top - component.getHeight();
                    int width = component.getWidth();
                    int height = component.getHeight();
                    MacOSXCanvasPeerInfo.nSetLayerBounds(this.getHandle(), x, y, width, height);
                }
            }
        }
    }

    private void addComponentListener(final Canvas component) {
        ComponentListener[] components = component.getComponentListeners();
        for (int i = 0; i < components.length; ++i) {
            ComponentListener c = components[i];
            if (c.toString() != "CanvasPeerInfoListener") continue;
            return;
        }
        ComponentListener comp = new ComponentListener(){

            public void componentHidden(ComponentEvent e) {
            }

            public void componentMoved(ComponentEvent e) {
                Insets insets;
                if (SwingUtilities.getWindowAncestor(component.getParent()) != null) {
                    Point componentPosition = SwingUtilities.convertPoint(component, component.getLocation(), null);
                    Point parentPosition = SwingUtilities.convertPoint(component.getParent(), component.getLocation(), null);
                    if (componentPosition.getX() == parentPosition.getX() && componentPosition.getY() == parentPosition.getY()) {
                        Insets insets2 = MacOSXCanvasPeerInfo.this.getWindowInsets(component);
                        int top = insets2 != null ? insets2.top : 0;
                        int left = insets2 != null ? insets2.left : 0;
                        MacOSXCanvasPeerInfo.nSetLayerBounds(MacOSXCanvasPeerInfo.this.getHandle(), (int)componentPosition.getX() - left, (int)componentPosition.getY() - top, component.getWidth(), component.getHeight());
                        return;
                    }
                }
                int top = (insets = MacOSXCanvasPeerInfo.this.getInsets(component)) != null ? insets.top : 0;
                int left = insets != null ? insets.left : 0;
                MacOSXCanvasPeerInfo.nSetLayerBounds(MacOSXCanvasPeerInfo.this.getHandle(), component.getX() - left, component.getY() - top, component.getWidth(), component.getHeight());
            }

            public void componentResized(ComponentEvent e) {
                Insets insets;
                if (SwingUtilities.getWindowAncestor(component.getParent()) != null) {
                    Point componentPosition = SwingUtilities.convertPoint(component, component.getLocation(), null);
                    Point parentPosition = SwingUtilities.convertPoint(component.getParent(), component.getLocation(), null);
                    if (componentPosition.getX() == parentPosition.getX() && componentPosition.getY() == parentPosition.getY()) {
                        Insets insets2 = MacOSXCanvasPeerInfo.this.getWindowInsets(component);
                        int top = insets2 != null ? insets2.top : 0;
                        int left = insets2 != null ? insets2.left : 0;
                        MacOSXCanvasPeerInfo.nSetLayerBounds(MacOSXCanvasPeerInfo.this.getHandle(), (int)componentPosition.getX() - left, (int)componentPosition.getY() - top, component.getWidth(), component.getHeight());
                        return;
                    }
                }
                int top = (insets = MacOSXCanvasPeerInfo.this.getInsets(component)) != null ? insets.top : 0;
                int left = insets != null ? insets.left : 0;
                MacOSXCanvasPeerInfo.nSetLayerBounds(MacOSXCanvasPeerInfo.this.getHandle(), component.getX() - left, component.getY() - top, component.getWidth(), component.getHeight());
            }

            public void componentShown(ComponentEvent e) {
            }

            public String toString() {
                return "CanvasPeerInfoListener";
            }
        };
        component.addComponentListener(comp);
    }

    private static native ByteBuffer nInitHandle(ByteBuffer var0, ByteBuffer var1, ByteBuffer var2, boolean var3, boolean var4, int var5, int var6) throws LWJGLException;

    private static native void nSetLayerPosition(ByteBuffer var0, int var1, int var2);

    private static native void nSetLayerBounds(ByteBuffer var0, int var1, int var2, int var3, int var4);

    protected void doUnlock() throws LWJGLException {
        this.awt_surface.unlock();
    }

    private Insets getWindowInsets(Canvas canvas) {
        for (Container parent = canvas.getParent(); parent != null; parent = parent.getParent()) {
            if (!(parent instanceof Window) && !(parent instanceof Applet)) continue;
            return parent.getInsets();
        }
        return null;
    }

    private Insets getInsets(Canvas component) {
        for (Container parent = component.getParent(); parent != null; parent = parent.getParent()) {
            if (!(parent instanceof Container)) continue;
            return parent.getInsets();
        }
        return null;
    }
}

