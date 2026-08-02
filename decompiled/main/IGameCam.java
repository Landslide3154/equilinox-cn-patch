/*
 * Decompiled with CFR 0.152.
 */
package main;

import basics.CameraInterface;
import java.io.IOException;
import org.lwjgl.util.vector.Vector3f;
import toolbox.MousePicker;
import utils.BinaryReader;
import utils.BinaryWriter;

public interface IGameCam
extends CameraInterface {
    public void loadState(BinaryReader var1) throws Exception;

    public void saveState(BinaryWriter var1) throws IOException;

    public void resetPosition();

    public void focusOn(Vector3f var1);

    public void enable(boolean var1);

    public void setTargetEntity(Vector3f var1);

    public MousePicker getCameraPicker();
}

