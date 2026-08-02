/*
 * Decompiled with CFR 0.152.
 */
package visualFxDrivers;

import visualFxDrivers.KeyFrame;
import visualFxDrivers.ValueDriver;

public class KeyFrameDriver
extends ValueDriver {
    private KeyFrame[] keyFrames;

    public KeyFrameDriver(KeyFrame[] keyFrames, float length) {
        super(length);
        this.keyFrames = keyFrames;
    }

    @Override
    protected float calculateValue(float time) {
        int index = this.findNextFrameIndex(time, 0, this.keyFrames.length - 1);
        KeyFrame previous = this.keyFrames[index - 1];
        KeyFrame next = this.keyFrames[index];
        float factor = (time - previous.getTime()) / (next.getTime() - previous.getTime());
        float difference = next.getValue() - previous.getValue();
        return previous.getValue() + factor * difference;
    }

    private int findNextFrameIndex(float time, int firstIndex, int lastIndex) {
        if (firstIndex == lastIndex) {
            return lastIndex + 1;
        }
        float length = 1 + (lastIndex - firstIndex);
        int check = (int)Math.floor(length / 2.0f) + firstIndex - 1;
        float number1 = this.keyFrames[check].getTime();
        float number2 = this.keyFrames[check + 1].getTime();
        if (number1 > time) {
            return this.findNextFrameIndex(time, firstIndex, check);
        }
        if (number2 > time) {
            return check + 1;
        }
        return this.findNextFrameIndex(time, check + 1, lastIndex);
    }
}

