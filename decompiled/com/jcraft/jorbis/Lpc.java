/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

import com.jcraft.jorbis.Drft;

class Lpc {
    Drft fft = new Drft();
    int ln;
    int m;

    Lpc() {
    }

    static float lpc_from_data(float[] data, float[] lpc, int n, int m) {
        int i;
        float[] aut = new float[m + 1];
        int j = m + 1;
        while (j-- != 0) {
            float d = 0.0f;
            i = j;
            while (i < n) {
                d += data[i] * data[i - j];
                ++i;
            }
            aut[j] = d;
        }
        float error = aut[0];
        i = 0;
        while (i < m) {
            float r = -aut[i + 1];
            if (error == 0.0f) {
                int k = 0;
                while (k < m) {
                    lpc[k] = 0.0f;
                    ++k;
                }
                return 0.0f;
            }
            j = 0;
            while (j < i) {
                r -= lpc[j] * aut[i - j];
                ++j;
            }
            lpc[i] = r /= error;
            j = 0;
            while (j < i / 2) {
                float tmp = lpc[j];
                int n2 = j;
                lpc[n2] = lpc[n2] + r * lpc[i - 1 - j];
                int n3 = i - 1 - j;
                lpc[n3] = lpc[n3] + r * tmp;
                ++j;
            }
            if (i % 2 != 0) {
                int n4 = j;
                lpc[n4] = lpc[n4] + lpc[j] * r;
            }
            error = (float)((double)error * (1.0 - (double)(r * r)));
            ++i;
        }
        return error;
    }

    float lpc_from_curve(float[] curve, float[] lpc) {
        int n = this.ln;
        float[] work = new float[n + n];
        float fscale = (float)(0.5 / (double)n);
        int i = 0;
        while (i < n) {
            work[i * 2] = curve[i] * fscale;
            work[i * 2 + 1] = 0.0f;
            ++i;
        }
        work[n * 2 - 1] = curve[n - 1] * fscale;
        this.fft.backward(work);
        i = 0;
        int j = (n *= 2) / 2;
        while (i < n / 2) {
            float temp = work[i];
            work[i++] = work[j];
            work[j++] = temp;
        }
        return Lpc.lpc_from_data(work, lpc, n, this.m);
    }

    void init(int mapped, int m) {
        this.ln = mapped;
        this.m = m;
        this.fft.init(mapped * 2);
    }

    void clear() {
        this.fft.clear();
    }

    static float FAST_HYPOT(float a, float b) {
        return (float)Math.sqrt(a * a + b * b);
    }

    void lpc_to_curve(float[] curve, float[] lpc, float amp) {
        int i = 0;
        while (i < this.ln * 2) {
            curve[i] = 0.0f;
            ++i;
        }
        if (amp == 0.0f) {
            return;
        }
        i = 0;
        while (i < this.m) {
            curve[i * 2 + 1] = lpc[i] / (4.0f * amp);
            curve[i * 2 + 2] = -lpc[i] / (4.0f * amp);
            ++i;
        }
        this.fft.backward(curve);
        int l2 = this.ln * 2;
        float unit = (float)(1.0 / (double)amp);
        curve[0] = (float)(1.0 / (double)(curve[0] * 2.0f + unit));
        int i2 = 1;
        while (i2 < this.ln) {
            float real = curve[i2] + curve[l2 - i2];
            float imag = curve[i2] - curve[l2 - i2];
            float a = real + unit;
            curve[i2] = (float)(1.0 / (double)Lpc.FAST_HYPOT(a, imag));
            ++i2;
        }
    }
}

