/*
 * Decompiled with CFR 0.152.
 */
package animator;

import animator.KeyFrame;
import toolbox.Maths;

public class ValueAnimator {
    private final KeyFrame[] frames;
    private final float length;
    private float time = 0.0f;
    private float value;

    public ValueAnimator(KeyFrame[] frames) {
        this.frames = frames;
        this.length = frames[frames.length - 1].time;
    }

    public void reset() {
        this.time = 0.0f;
    }

    public boolean updateAnimation(float delta) {
        this.time += delta;
        if (this.time >= this.length) {
            this.value = this.frames[this.frames.length - 1].value;
            return true;
        }
        KeyFrame[] twoFrames = this.getCloseFrames(this.time);
        this.interpolate(twoFrames[0], twoFrames[1], this.time);
        return false;
    }

    public float getValue() {
        return this.value;
    }

    public float getTime() {
        return this.time;
    }

    private void interpolate(KeyFrame frame0, KeyFrame frame1, float time) {
        float blend = (time - frame0.time) / (frame1.time - frame0.time);
        float smoothTime = Maths.cosify(blend);
        this.value = Maths.interpolate(frame0.value, frame1.value, smoothTime);
    }

    private KeyFrame[] getCloseFrames(float time) {
        KeyFrame[] closeFrames = new KeyFrame[]{this.frames[0], this.frames[1]};
        int i = 1;
        while (i < this.frames.length) {
            if (this.frames[i].time > time) {
                return closeFrames;
            }
            closeFrames[0] = this.frames[i];
            closeFrames[1] = this.frames[i + 1];
            ++i;
        }
        return closeFrames;
    }
}

