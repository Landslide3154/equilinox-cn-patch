/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

class Drft {
    int n;
    float[] trigcache;
    int[] splitcache;
    static int[] ntryh = new int[]{4, 2, 3, 5};
    static float tpi = (float)Math.PI * 2;
    static float hsqt2 = 0.70710677f;
    static float taui = 0.8660254f;
    static float taur = -0.5f;
    static float sqrt2 = 1.4142135f;

    Drft() {
    }

    void backward(float[] data) {
        if (this.n == 1) {
            return;
        }
        Drft.drftb1(this.n, data, this.trigcache, this.trigcache, this.n, this.splitcache);
    }

    void init(int n) {
        this.n = n;
        this.trigcache = new float[3 * n];
        this.splitcache = new int[32];
        Drft.fdrffti(n, this.trigcache, this.splitcache);
    }

    void clear() {
        if (this.trigcache != null) {
            this.trigcache = null;
        }
        if (this.splitcache != null) {
            this.splitcache = null;
        }
    }

    static void drfti1(int n, float[] wa, int index, int[] ifac) {
        int ntry = 0;
        int j = -1;
        int nl = n;
        int nf = 0;
        int state = 101;
        block5: while (true) {
            switch (state) {
                case 101: {
                    ntry = ++j < 4 ? ntryh[j] : (ntry += 2);
                }
                case 104: {
                    int nq = nl / ntry;
                    int nr = nl - ntry * nq;
                    if (nr != 0) {
                        state = 101;
                        continue block5;
                    }
                    ifac[++nf + 1] = ntry;
                    nl = nq;
                    if (ntry != 2) {
                        state = 107;
                        continue block5;
                    }
                    if (nf == 1) {
                        state = 107;
                        continue block5;
                    }
                    int i = 1;
                    while (i < nf) {
                        int ib = nf - i + 1;
                        ifac[ib + 1] = ifac[ib];
                        ++i;
                    }
                    ifac[2] = 2;
                }
                case 107: {
                    int i;
                    if (nl != 1) {
                        state = 104;
                        continue block5;
                    }
                    ifac[0] = n;
                    ifac[1] = nf;
                    float argh = tpi / (float)n;
                    int is = 0;
                    int nfm1 = nf - 1;
                    int l1 = 1;
                    if (nfm1 == 0) {
                        return;
                    }
                    int k1 = 0;
                    while (k1 < nfm1) {
                        int ip = ifac[k1 + 2];
                        int ld = 0;
                        int l2 = l1 * ip;
                        int ido = n / l2;
                        int ipm = ip - 1;
                        j = 0;
                        while (j < ipm) {
                            i = is;
                            float argld = (float)(ld += l1) * argh;
                            float fi = 0.0f;
                            int ii = 2;
                            while (ii < ido) {
                                float arg = (fi += 1.0f) * argld;
                                wa[index + i++] = (float)Math.cos(arg);
                                wa[index + i++] = (float)Math.sin(arg);
                                ii += 2;
                            }
                            is += ido;
                            ++j;
                        }
                        l1 = l2;
                        ++k1;
                    }
                    break block5;
                }
                default: {
                    continue block5;
                }
            }
            break;
        }
    }

    static void fdrffti(int n, float[] wsave, int[] ifac) {
        if (n == 1) {
            return;
        }
        Drft.drfti1(n, wsave, n, ifac);
    }

    static void dradf2(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index) {
        int t2;
        int t1 = 0;
        int t0 = t2 = l1 * ido;
        int t3 = ido << 1;
        int k = 0;
        while (k < l1) {
            ch[t1 << 1] = cc[t1] + cc[t2];
            ch[(t1 << 1) + t3 - 1] = cc[t1] - cc[t2];
            t1 += ido;
            t2 += ido;
            ++k;
        }
        if (ido < 2) {
            return;
        }
        if (ido != 2) {
            t1 = 0;
            t2 = t0;
            k = 0;
            while (k < l1) {
                t3 = t2;
                int t4 = (t1 << 1) + (ido << 1);
                int t5 = t1;
                int t6 = t1 + t1;
                int i = 2;
                while (i < ido) {
                    float tr2 = wa1[index + i - 2] * cc[(t3 += 2) - 1] + wa1[index + i - 1] * cc[t3];
                    float ti2 = wa1[index + i - 2] * cc[t3] - wa1[index + i - 1] * cc[t3 - 1];
                    ch[t6 += 2] = cc[t5 += 2] + ti2;
                    ch[t4 -= 2] = ti2 - cc[t5];
                    ch[t6 - 1] = cc[t5 - 1] + tr2;
                    ch[t4 - 1] = cc[t5 - 1] - tr2;
                    i += 2;
                }
                t1 += ido;
                t2 += ido;
                ++k;
            }
            if (ido % 2 == 1) {
                return;
            }
        }
        t1 = ido;
        t3 = t2 = t1 - 1;
        t2 += t0;
        k = 0;
        while (k < l1) {
            ch[t1] = -cc[t2];
            ch[t1 - 1] = cc[t3];
            t1 += ido << 1;
            t2 += ido;
            t3 += ido;
            ++k;
        }
    }

    static void dradf4(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index1, float[] wa2, int index2, float[] wa3, int index3) {
        float ti1;
        int t6;
        int t5;
        float tr2;
        float tr1;
        int t0;
        int t1 = t0 = l1 * ido;
        int t4 = t1 << 1;
        int t2 = t1 + (t1 << 1);
        int t3 = 0;
        int k = 0;
        while (k < l1) {
            tr1 = cc[t1] + cc[t2];
            tr2 = cc[t3] + cc[t4];
            t5 = t3 << 2;
            ch[t5] = tr1 + tr2;
            ch[(ido << 2) + t5 - 1] = tr2 - tr1;
            ch[(t5 += ido << 1) - 1] = cc[t3] - cc[t4];
            ch[t5] = cc[t2] - cc[t1];
            t1 += ido;
            t2 += ido;
            t3 += ido;
            t4 += ido;
            ++k;
        }
        if (ido < 2) {
            return;
        }
        if (ido != 2) {
            t1 = 0;
            k = 0;
            while (k < l1) {
                t2 = t1;
                t4 = t1 << 2;
                t6 = ido << 1;
                t5 = t6 + t4;
                int i = 2;
                while (i < ido) {
                    t3 = t2 += 2;
                    t4 += 2;
                    t5 -= 2;
                    float cr2 = wa1[index1 + i - 2] * cc[(t3 += t0) - 1] + wa1[index1 + i - 1] * cc[t3];
                    float ci2 = wa1[index1 + i - 2] * cc[t3] - wa1[index1 + i - 1] * cc[t3 - 1];
                    float cr3 = wa2[index2 + i - 2] * cc[(t3 += t0) - 1] + wa2[index2 + i - 1] * cc[t3];
                    float ci3 = wa2[index2 + i - 2] * cc[t3] - wa2[index2 + i - 1] * cc[t3 - 1];
                    float cr4 = wa3[index3 + i - 2] * cc[(t3 += t0) - 1] + wa3[index3 + i - 1] * cc[t3];
                    float ci4 = wa3[index3 + i - 2] * cc[t3] - wa3[index3 + i - 1] * cc[t3 - 1];
                    tr1 = cr2 + cr4;
                    float tr4 = cr4 - cr2;
                    ti1 = ci2 + ci4;
                    float ti4 = ci2 - ci4;
                    float ti2 = cc[t2] + ci3;
                    float ti3 = cc[t2] - ci3;
                    tr2 = cc[t2 - 1] + cr3;
                    float tr3 = cc[t2 - 1] - cr3;
                    ch[t4 - 1] = tr1 + tr2;
                    ch[t4] = ti1 + ti2;
                    ch[t5 - 1] = tr3 - ti4;
                    ch[t5] = tr4 - ti3;
                    ch[t4 + t6 - 1] = ti4 + tr3;
                    ch[t4 + t6] = tr4 + ti3;
                    ch[t5 + t6 - 1] = tr2 - tr1;
                    ch[t5 + t6] = ti1 - ti2;
                    i += 2;
                }
                t1 += ido;
                ++k;
            }
            if ((ido & 1) != 0) {
                return;
            }
        }
        t1 = t0 + ido - 1;
        t2 = t1 + (t0 << 1);
        t3 = ido << 2;
        t4 = ido;
        t5 = ido << 1;
        t6 = ido;
        k = 0;
        while (k < l1) {
            ti1 = -hsqt2 * (cc[t1] + cc[t2]);
            tr1 = hsqt2 * (cc[t1] - cc[t2]);
            ch[t4 - 1] = tr1 + cc[t6 - 1];
            ch[t4 + t5 - 1] = cc[t6 - 1] - tr1;
            ch[t4] = ti1 - cc[t1 + t0];
            ch[t4 + t5] = ti1 + cc[t1 + t0];
            t1 += ido;
            t2 += ido;
            t4 += t3;
            t6 += ido;
            ++k;
        }
    }

    static void dradfg(int ido, int ip, int l1, int idl1, float[] cc, float[] c1, float[] c2, float[] ch, float[] ch2, float[] wa, int index) {
        int t2 = 0;
        float dcp = 0.0f;
        float dsp = 0.0f;
        float arg = tpi / (float)ip;
        dcp = (float)Math.cos(arg);
        dsp = (float)Math.sin(arg);
        int ipph = ip + 1 >> 1;
        int ipp2 = ip;
        int idp2 = ido;
        int nbd = ido - 1 >> 1;
        int t0 = l1 * ido;
        int t10 = ip * ido;
        int state = 100;
        block7: while (true) {
            switch (state) {
                case 101: {
                    int t6;
                    int t5;
                    int t4;
                    int i;
                    int t3;
                    int idij;
                    int k;
                    if (ido == 1) {
                        state = 119;
                        continue block7;
                    }
                    int ik = 0;
                    while (ik < idl1) {
                        ch2[ik] = c2[ik];
                        ++ik;
                    }
                    int t1 = 0;
                    int j = 1;
                    while (j < ip) {
                        t2 = t1 += t0;
                        k = 0;
                        while (k < l1) {
                            ch[t2] = c1[t2];
                            t2 += ido;
                            ++k;
                        }
                        ++j;
                    }
                    int is = -ido;
                    t1 = 0;
                    if (nbd > l1) {
                        j = 1;
                        while (j < ip) {
                            is += ido;
                            t2 = -ido + (t1 += t0);
                            k = 0;
                            while (k < l1) {
                                idij = is - 1;
                                t3 = t2 += ido;
                                i = 2;
                                while (i < ido) {
                                    ch[(t3 += 2) - 1] = wa[index + (idij += 2) - 1] * c1[t3 - 1] + wa[index + idij] * c1[t3];
                                    ch[t3] = wa[index + idij - 1] * c1[t3] - wa[index + idij] * c1[t3 - 1];
                                    i += 2;
                                }
                                ++k;
                            }
                            ++j;
                        }
                    } else {
                        j = 1;
                        while (j < ip) {
                            idij = (is += ido) - 1;
                            t2 = t1 += t0;
                            i = 2;
                            while (i < ido) {
                                idij += 2;
                                t3 = t2 += 2;
                                k = 0;
                                while (k < l1) {
                                    ch[t3 - 1] = wa[index + idij - 1] * c1[t3 - 1] + wa[index + idij] * c1[t3];
                                    ch[t3] = wa[index + idij - 1] * c1[t3] - wa[index + idij] * c1[t3 - 1];
                                    t3 += ido;
                                    ++k;
                                }
                                i += 2;
                            }
                            ++j;
                        }
                    }
                    t1 = 0;
                    t2 = ipp2 * t0;
                    if (nbd < l1) {
                        j = 1;
                        while (j < ipph) {
                            t3 = t1 += t0;
                            t4 = t2 -= t0;
                            i = 2;
                            while (i < ido) {
                                t5 = (t3 += 2) - ido;
                                t6 = (t4 += 2) - ido;
                                k = 0;
                                while (k < l1) {
                                    c1[(t5 += ido) - 1] = ch[t5 - 1] + ch[(t6 += ido) - 1];
                                    c1[t6 - 1] = ch[t5] - ch[t6];
                                    c1[t5] = ch[t5] + ch[t6];
                                    c1[t6] = ch[t6 - 1] - ch[t5 - 1];
                                    ++k;
                                }
                                i += 2;
                            }
                            ++j;
                        }
                    } else {
                        j = 1;
                        while (j < ipph) {
                            t3 = t1 += t0;
                            t4 = t2 -= t0;
                            k = 0;
                            while (k < l1) {
                                t5 = t3;
                                t6 = t4;
                                i = 2;
                                while (i < ido) {
                                    c1[(t5 += 2) - 1] = ch[t5 - 1] + ch[(t6 += 2) - 1];
                                    c1[t6 - 1] = ch[t5] - ch[t6];
                                    c1[t5] = ch[t5] + ch[t6];
                                    c1[t6] = ch[t6 - 1] - ch[t5 - 1];
                                    i += 2;
                                }
                                t3 += ido;
                                t4 += ido;
                                ++k;
                            }
                            ++j;
                        }
                    }
                }
                case 119: {
                    int t9;
                    int t8;
                    int t7;
                    int t6;
                    int t5;
                    int t4;
                    int i;
                    int t3;
                    int k;
                    int ik = 0;
                    while (ik < idl1) {
                        c2[ik] = ch2[ik];
                        ++ik;
                    }
                    int t1 = 0;
                    t2 = ipp2 * idl1;
                    int j = 1;
                    while (j < ipph) {
                        t3 = (t1 += t0) - ido;
                        t4 = (t2 -= t0) - ido;
                        k = 0;
                        while (k < l1) {
                            c1[t3 += ido] = ch[t3] + ch[t4 += ido];
                            c1[t4] = ch[t4] - ch[t3];
                            ++k;
                        }
                        ++j;
                    }
                    float ar1 = 1.0f;
                    float ai1 = 0.0f;
                    t1 = 0;
                    t2 = ipp2 * idl1;
                    t3 = (ip - 1) * idl1;
                    int l = 1;
                    while (l < ipph) {
                        float ar1h = dcp * ar1 - dsp * ai1;
                        ai1 = dcp * ai1 + dsp * ar1;
                        ar1 = ar1h;
                        t4 = t1 += idl1;
                        t5 = t2 -= idl1;
                        t6 = t3;
                        t7 = idl1;
                        ik = 0;
                        while (ik < idl1) {
                            ch2[t4++] = c2[ik] + ar1 * c2[t7++];
                            ch2[t5++] = ai1 * c2[t6++];
                            ++ik;
                        }
                        float dc2 = ar1;
                        float ds2 = ai1;
                        float ar2 = ar1;
                        float ai2 = ai1;
                        t4 = idl1;
                        t5 = (ipp2 - 1) * idl1;
                        j = 2;
                        while (j < ipph) {
                            t4 += idl1;
                            t5 -= idl1;
                            float ar2h = dc2 * ar2 - ds2 * ai2;
                            ai2 = dc2 * ai2 + ds2 * ar2;
                            ar2 = ar2h;
                            t6 = t1;
                            t7 = t2;
                            t8 = t4;
                            t9 = t5;
                            ik = 0;
                            while (ik < idl1) {
                                int n = t6++;
                                ch2[n] = ch2[n] + ar2 * c2[t8++];
                                int n2 = t7++;
                                ch2[n2] = ch2[n2] + ai2 * c2[t9++];
                                ++ik;
                            }
                            ++j;
                        }
                        ++l;
                    }
                    t1 = 0;
                    j = 1;
                    while (j < ipph) {
                        t2 = t1 += idl1;
                        ik = 0;
                        while (ik < idl1) {
                            int n = ik++;
                            ch2[n] = ch2[n] + c2[t2++];
                        }
                        ++j;
                    }
                    if (ido < l1) {
                        state = 132;
                        continue block7;
                    }
                    t1 = 0;
                    t2 = 0;
                    k = 0;
                    while (k < l1) {
                        t3 = t1;
                        t4 = t2;
                        i = 0;
                        while (i < ido) {
                            cc[t4++] = ch[t3++];
                            ++i;
                        }
                        t1 += ido;
                        t2 += t10;
                        ++k;
                    }
                    state = 135;
                    continue block7;
                }
                case 132: {
                    int k;
                    int t1;
                    int i = 0;
                    while (i < ido) {
                        t1 = i;
                        t2 = i;
                        k = 0;
                        while (k < l1) {
                            cc[t2] = ch[t1];
                            t1 += ido;
                            t2 += t10;
                            ++k;
                        }
                        ++i;
                    }
                }
                case 135: {
                    int t9;
                    int t8;
                    int t7;
                    int t6;
                    int t5;
                    int i;
                    int k;
                    int t1 = 0;
                    t2 = ido << 1;
                    int t3 = 0;
                    int t4 = ipp2 * t0;
                    int j = 1;
                    while (j < ipph) {
                        t5 = t1 += t2;
                        t6 = t3 += t0;
                        t7 = t4 -= t0;
                        k = 0;
                        while (k < l1) {
                            cc[t5 - 1] = ch[t6];
                            cc[t5] = ch[t7];
                            t5 += t10;
                            t6 += ido;
                            t7 += ido;
                            ++k;
                        }
                        ++j;
                    }
                    if (ido == 1) {
                        return;
                    }
                    if (nbd < l1) {
                        state = 141;
                        continue block7;
                    }
                    t1 = -ido;
                    t3 = 0;
                    t4 = 0;
                    t5 = ipp2 * t0;
                    j = 1;
                    while (j < ipph) {
                        t6 = t1 += t2;
                        t7 = t3 += t2;
                        t8 = t4 += t0;
                        t9 = t5 -= t0;
                        k = 0;
                        while (k < l1) {
                            i = 2;
                            while (i < ido) {
                                int ic = idp2 - i;
                                cc[i + t7 - 1] = ch[i + t8 - 1] + ch[i + t9 - 1];
                                cc[ic + t6 - 1] = ch[i + t8 - 1] - ch[i + t9 - 1];
                                cc[i + t7] = ch[i + t8] + ch[i + t9];
                                cc[ic + t6] = ch[i + t9] - ch[i + t8];
                                i += 2;
                            }
                            t6 += t10;
                            t7 += t10;
                            t8 += ido;
                            t9 += ido;
                            ++k;
                        }
                        ++j;
                    }
                    return;
                }
                case 141: {
                    int t9;
                    int t8;
                    int t7;
                    int t6;
                    int i;
                    int k;
                    int t1 = -ido;
                    int t3 = 0;
                    int t4 = 0;
                    int t5 = ipp2 * t0;
                    int j = 1;
                    while (j < ipph) {
                        t1 += t2;
                        t3 += t2;
                        t4 += t0;
                        t5 -= t0;
                        i = 2;
                        while (i < ido) {
                            t6 = idp2 + t1 - i;
                            t7 = i + t3;
                            t8 = i + t4;
                            t9 = i + t5;
                            k = 0;
                            while (k < l1) {
                                cc[t7 - 1] = ch[t8 - 1] + ch[t9 - 1];
                                cc[t6 - 1] = ch[t8 - 1] - ch[t9 - 1];
                                cc[t7] = ch[t8] + ch[t9];
                                cc[t6] = ch[t9] - ch[t8];
                                t6 += t10;
                                t7 += t10;
                                t8 += ido;
                                t9 += ido;
                                ++k;
                            }
                            i += 2;
                        }
                        ++j;
                    }
                    break block7;
                }
                default: {
                    continue block7;
                }
            }
            break;
        }
    }

    static void drftf1(int n, float[] c, float[] ch, float[] wa, int[] ifac) {
        int nf = ifac[1];
        int na = 1;
        int l2 = n;
        int iw = n;
        int k1 = 0;
        while (k1 < nf) {
            int kh = nf - k1;
            int ip = ifac[kh + 1];
            int l1 = l2 / ip;
            int ido = n / l2;
            int idl1 = ido * l1;
            iw -= (ip - 1) * ido;
            na = 1 - na;
            int state = 100;
            block9: while (true) {
                switch (state) {
                    case 100: {
                        if (ip != 4) {
                            state = 102;
                            continue block9;
                        }
                        int ix2 = iw + ido;
                        int ix3 = ix2 + ido;
                        if (na != 0) {
                            Drft.dradf4(ido, l1, ch, c, wa, iw - 1, wa, ix2 - 1, wa, ix3 - 1);
                        } else {
                            Drft.dradf4(ido, l1, c, ch, wa, iw - 1, wa, ix2 - 1, wa, ix3 - 1);
                        }
                        state = 110;
                        continue block9;
                    }
                    case 102: {
                        if (ip != 2) {
                            state = 104;
                            continue block9;
                        }
                        if (na != 0) {
                            state = 103;
                            continue block9;
                        }
                        Drft.dradf2(ido, l1, c, ch, wa, iw - 1);
                        state = 110;
                        continue block9;
                    }
                    case 103: {
                        Drft.dradf2(ido, l1, ch, c, wa, iw - 1);
                    }
                    case 104: {
                        if (ido == 1) {
                            na = 1 - na;
                        }
                        if (na != 0) {
                            state = 109;
                            continue block9;
                        }
                        Drft.dradfg(ido, ip, l1, idl1, c, c, c, ch, ch, wa, iw - 1);
                        na = 1;
                        state = 110;
                        continue block9;
                    }
                    case 109: {
                        Drft.dradfg(ido, ip, l1, idl1, ch, ch, ch, c, c, wa, iw - 1);
                        na = 0;
                    }
                    case 110: {
                        l2 = l1;
                        break block9;
                    }
                    default: {
                        continue block9;
                    }
                }
                break;
            }
            ++k1;
        }
        if (na == 1) {
            return;
        }
        int i = 0;
        while (i < n) {
            c[i] = ch[i];
            ++i;
        }
    }

    static void dradb2(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index) {
        int t0 = l1 * ido;
        int t1 = 0;
        int t2 = 0;
        int t3 = (ido << 1) - 1;
        int k = 0;
        while (k < l1) {
            ch[t1] = cc[t2] + cc[t3 + t2];
            ch[t1 + t0] = cc[t2] - cc[t3 + t2];
            t2 = (t1 += ido) << 1;
            ++k;
        }
        if (ido < 2) {
            return;
        }
        if (ido != 2) {
            t1 = 0;
            t2 = 0;
            k = 0;
            while (k < l1) {
                t3 = t1;
                int t4 = t2;
                int t5 = t4 + (ido << 1);
                int t6 = t0 + t1;
                int i = 2;
                while (i < ido) {
                    ch[(t3 += 2) - 1] = cc[(t4 += 2) - 1] + cc[(t5 -= 2) - 1];
                    float tr2 = cc[t4 - 1] - cc[t5 - 1];
                    ch[t3] = cc[t4] - cc[t5];
                    float ti2 = cc[t4] + cc[t5];
                    ch[(t6 += 2) - 1] = wa1[index + i - 2] * tr2 - wa1[index + i - 1] * ti2;
                    ch[t6] = wa1[index + i - 2] * ti2 + wa1[index + i - 1] * tr2;
                    i += 2;
                }
                t2 = (t1 += ido) << 1;
                ++k;
            }
            if (ido % 2 == 1) {
                return;
            }
        }
        t1 = ido - 1;
        t2 = ido - 1;
        k = 0;
        while (k < l1) {
            ch[t1] = cc[t2] + cc[t2];
            ch[t1 + t0] = -(cc[t2 + 1] + cc[t2 + 1]);
            t1 += ido;
            t2 += ido << 1;
            ++k;
        }
    }

    static void dradb3(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index1, float[] wa2, int index2) {
        float ci3;
        float cr2;
        float tr2;
        int t0 = l1 * ido;
        int t1 = 0;
        int t2 = t0 << 1;
        int t3 = ido << 1;
        int t4 = ido + (ido << 1);
        int t5 = 0;
        int k = 0;
        while (k < l1) {
            tr2 = cc[t3 - 1] + cc[t3 - 1];
            cr2 = cc[t5] + taur * tr2;
            ch[t1] = cc[t5] + tr2;
            ci3 = taui * (cc[t3] + cc[t3]);
            ch[t1 + t0] = cr2 - ci3;
            ch[t1 + t2] = cr2 + ci3;
            t1 += ido;
            t3 += t4;
            t5 += t4;
            ++k;
        }
        if (ido == 1) {
            return;
        }
        t1 = 0;
        t3 = ido << 1;
        k = 0;
        while (k < l1) {
            int t7 = t1 + (t1 << 1);
            int t6 = t5 = t7 + t3;
            int t8 = t1;
            int t9 = t1 + t0;
            int t10 = t9 + t0;
            int i = 2;
            while (i < ido) {
                t9 += 2;
                t10 += 2;
                tr2 = cc[(t5 += 2) - 1] + cc[(t6 -= 2) - 1];
                cr2 = cc[(t7 += 2) - 1] + taur * tr2;
                ch[(t8 += 2) - 1] = cc[t7 - 1] + tr2;
                float ti2 = cc[t5] - cc[t6];
                float ci2 = cc[t7] + taur * ti2;
                ch[t8] = cc[t7] + ti2;
                float cr3 = taui * (cc[t5 - 1] - cc[t6 - 1]);
                ci3 = taui * (cc[t5] + cc[t6]);
                float dr2 = cr2 - ci3;
                float dr3 = cr2 + ci3;
                float di2 = ci2 + cr3;
                float di3 = ci2 - cr3;
                ch[t9 - 1] = wa1[index1 + i - 2] * dr2 - wa1[index1 + i - 1] * di2;
                ch[t9] = wa1[index1 + i - 2] * di2 + wa1[index1 + i - 1] * dr2;
                ch[t10 - 1] = wa2[index2 + i - 2] * dr3 - wa2[index2 + i - 1] * di3;
                ch[t10] = wa2[index2 + i - 2] * di3 + wa2[index2 + i - 1] * dr3;
                i += 2;
            }
            t1 += ido;
            ++k;
        }
    }

    static void dradb4(int ido, int l1, float[] cc, float[] ch, float[] wa1, int index1, float[] wa2, int index2, float[] wa3, int index3) {
        float ti2;
        float ti1;
        float tr2;
        float tr1;
        float tr4;
        float tr3;
        int t5;
        int t4;
        int t0 = l1 * ido;
        int t1 = 0;
        int t2 = ido << 2;
        int t3 = 0;
        int t6 = ido << 1;
        int k = 0;
        while (k < l1) {
            t4 = t3 + t6;
            t5 = t1;
            tr3 = cc[t4 - 1] + cc[t4 - 1];
            tr4 = cc[t4] + cc[t4];
            tr1 = cc[t3] - cc[(t4 += t6) - 1];
            tr2 = cc[t3] + cc[t4 - 1];
            ch[t5] = tr2 + tr3;
            ch[t5 += t0] = tr1 - tr4;
            ch[t5 += t0] = tr2 - tr3;
            ch[t5 += t0] = tr1 + tr4;
            t1 += ido;
            t3 += t2;
            ++k;
        }
        if (ido < 2) {
            return;
        }
        if (ido != 2) {
            t1 = 0;
            k = 0;
            while (k < l1) {
                t2 = t1 << 2;
                t4 = t3 = t2 + t6;
                t5 = t3 + t6;
                int t7 = t1;
                int i = 2;
                while (i < ido) {
                    t7 += 2;
                    ti1 = cc[t2 += 2] + cc[t5 -= 2];
                    ti2 = cc[t2] - cc[t5];
                    float ti3 = cc[t3 += 2] - cc[t4 -= 2];
                    tr4 = cc[t3] + cc[t4];
                    tr1 = cc[t2 - 1] - cc[t5 - 1];
                    tr2 = cc[t2 - 1] + cc[t5 - 1];
                    float ti4 = cc[t3 - 1] - cc[t4 - 1];
                    tr3 = cc[t3 - 1] + cc[t4 - 1];
                    ch[t7 - 1] = tr2 + tr3;
                    float cr3 = tr2 - tr3;
                    ch[t7] = ti2 + ti3;
                    float ci3 = ti2 - ti3;
                    float cr2 = tr1 - tr4;
                    float cr4 = tr1 + tr4;
                    float ci2 = ti1 + ti4;
                    float ci4 = ti1 - ti4;
                    int t8 = t7 + t0;
                    ch[t8 - 1] = wa1[index1 + i - 2] * cr2 - wa1[index1 + i - 1] * ci2;
                    ch[t8] = wa1[index1 + i - 2] * ci2 + wa1[index1 + i - 1] * cr2;
                    ch[(t8 += t0) - 1] = wa2[index2 + i - 2] * cr3 - wa2[index2 + i - 1] * ci3;
                    ch[t8] = wa2[index2 + i - 2] * ci3 + wa2[index2 + i - 1] * cr3;
                    ch[(t8 += t0) - 1] = wa3[index3 + i - 2] * cr4 - wa3[index3 + i - 1] * ci4;
                    ch[t8] = wa3[index3 + i - 2] * ci4 + wa3[index3 + i - 1] * cr4;
                    i += 2;
                }
                t1 += ido;
                ++k;
            }
            if (ido % 2 == 1) {
                return;
            }
        }
        t1 = ido;
        t2 = ido << 2;
        t3 = ido - 1;
        t4 = ido + (ido << 1);
        k = 0;
        while (k < l1) {
            t5 = t3;
            ti1 = cc[t1] + cc[t4];
            ti2 = cc[t4] - cc[t1];
            tr1 = cc[t1 - 1] - cc[t4 - 1];
            tr2 = cc[t1 - 1] + cc[t4 - 1];
            ch[t5] = tr2 + tr2;
            ch[t5 += t0] = sqrt2 * (tr1 - ti1);
            ch[t5 += t0] = ti2 + ti2;
            ch[t5 += t0] = -sqrt2 * (tr1 + ti1);
            t3 += ido;
            t1 += t2;
            t4 += t2;
            ++k;
        }
    }

    static void dradbg(int ido, int ip, int l1, int idl1, float[] cc, float[] c1, float[] c2, float[] ch, float[] ch2, float[] wa, int index) {
        int ipph = 0;
        int t0 = 0;
        int t10 = 0;
        int nbd = 0;
        float dcp = 0.0f;
        float dsp = 0.0f;
        int ipp2 = 0;
        int state = 100;
        block10: while (true) {
            switch (state) {
                case 100: {
                    int i;
                    int t4;
                    int t3;
                    t10 = ip * ido;
                    t0 = l1 * ido;
                    float arg = tpi / (float)ip;
                    dcp = (float)Math.cos(arg);
                    dsp = (float)Math.sin(arg);
                    nbd = ido - 1 >>> 1;
                    ipp2 = ip;
                    ipph = ip + 1 >>> 1;
                    if (ido < l1) {
                        state = 103;
                        continue block10;
                    }
                    int t1 = 0;
                    int t2 = 0;
                    int k = 0;
                    while (k < l1) {
                        t3 = t1;
                        t4 = t2;
                        i = 0;
                        while (i < ido) {
                            ch[t3] = cc[t4];
                            ++t3;
                            ++t4;
                            ++i;
                        }
                        t1 += ido;
                        t2 += t10;
                        ++k;
                    }
                    state = 106;
                    continue block10;
                }
                case 103: {
                    int t3;
                    int k;
                    int t2;
                    int t1 = 0;
                    int i = 0;
                    while (i < ido) {
                        t2 = t1;
                        t3 = t1;
                        k = 0;
                        while (k < l1) {
                            ch[t2] = cc[t3];
                            t2 += ido;
                            t3 += t10;
                            ++k;
                        }
                        ++t1;
                        ++i;
                    }
                }
                case 106: {
                    int t11;
                    int t9;
                    int t8;
                    int t6;
                    int t5;
                    int i;
                    int t4;
                    int t3;
                    int k;
                    int t1 = 0;
                    int t2 = ipp2 * t0;
                    int t7 = t5 = ido << 1;
                    int j = 1;
                    while (j < ipph) {
                        t3 = t1 += t0;
                        t4 = t2 -= t0;
                        t6 = t5;
                        k = 0;
                        while (k < l1) {
                            ch[t3] = cc[t6 - 1] + cc[t6 - 1];
                            ch[t4] = cc[t6] + cc[t6];
                            t3 += ido;
                            t4 += ido;
                            t6 += t10;
                            ++k;
                        }
                        t5 += t7;
                        ++j;
                    }
                    if (ido == 1) {
                        state = 116;
                        continue block10;
                    }
                    if (nbd < l1) {
                        state = 112;
                        continue block10;
                    }
                    t1 = 0;
                    t2 = ipp2 * t0;
                    t7 = 0;
                    j = 1;
                    while (j < ipph) {
                        t3 = t1 += t0;
                        t4 = t2 -= t0;
                        t8 = t7 += ido << 1;
                        k = 0;
                        while (k < l1) {
                            t5 = t3;
                            t6 = t4;
                            t9 = t8;
                            t11 = t8;
                            i = 2;
                            while (i < ido) {
                                ch[(t5 += 2) - 1] = cc[(t9 += 2) - 1] + cc[(t11 -= 2) - 1];
                                ch[(t6 += 2) - 1] = cc[t9 - 1] - cc[t11 - 1];
                                ch[t5] = cc[t9] - cc[t11];
                                ch[t6] = cc[t9] + cc[t11];
                                i += 2;
                            }
                            t3 += ido;
                            t4 += ido;
                            t8 += t10;
                            ++k;
                        }
                        ++j;
                    }
                    state = 116;
                    continue block10;
                }
                case 112: {
                    int t12;
                    int t11;
                    int t9;
                    int t8;
                    int t6;
                    int t5;
                    int i;
                    int t4;
                    int t3;
                    int k;
                    int t1 = 0;
                    int t2 = ipp2 * t0;
                    int t7 = 0;
                    int j = 1;
                    while (j < ipph) {
                        t3 = t1 += t0;
                        t4 = t2 -= t0;
                        t8 = t7 += ido << 1;
                        t9 = t7;
                        i = 2;
                        while (i < ido) {
                            t5 = t3 += 2;
                            t6 = t4 += 2;
                            t11 = t8 += 2;
                            t12 = t9 -= 2;
                            k = 0;
                            while (k < l1) {
                                ch[t5 - 1] = cc[t11 - 1] + cc[t12 - 1];
                                ch[t6 - 1] = cc[t11 - 1] - cc[t12 - 1];
                                ch[t5] = cc[t11] - cc[t12];
                                ch[t6] = cc[t11] + cc[t12];
                                t5 += ido;
                                t6 += ido;
                                t11 += t10;
                                t12 += t10;
                                ++k;
                            }
                            i += 2;
                        }
                        ++j;
                    }
                }
                case 116: {
                    int ik;
                    int t12;
                    int t11;
                    int t8;
                    int t6;
                    int j;
                    int t7;
                    int t5;
                    int i;
                    int t4;
                    int k;
                    int t2;
                    float ar1 = 1.0f;
                    float ai1 = 0.0f;
                    int t1 = 0;
                    int t9 = t2 = ipp2 * idl1;
                    int t3 = (ip - 1) * idl1;
                    int l = 1;
                    while (l < ipph) {
                        float ar1h = dcp * ar1 - dsp * ai1;
                        ai1 = dcp * ai1 + dsp * ar1;
                        ar1 = ar1h;
                        t4 = t1 += idl1;
                        t5 = t2 -= idl1;
                        t6 = 0;
                        t7 = idl1;
                        t8 = t3;
                        ik = 0;
                        while (ik < idl1) {
                            c2[t4++] = ch2[t6++] + ar1 * ch2[t7++];
                            c2[t5++] = ai1 * ch2[t8++];
                            ++ik;
                        }
                        float dc2 = ar1;
                        float ds2 = ai1;
                        float ar2 = ar1;
                        float ai2 = ai1;
                        t6 = idl1;
                        t7 = t9 - idl1;
                        j = 2;
                        while (j < ipph) {
                            t6 += idl1;
                            t7 -= idl1;
                            float ar2h = dc2 * ar2 - ds2 * ai2;
                            ai2 = dc2 * ai2 + ds2 * ar2;
                            ar2 = ar2h;
                            t4 = t1;
                            t5 = t2;
                            t11 = t6;
                            t12 = t7;
                            ik = 0;
                            while (ik < idl1) {
                                int n = t4++;
                                c2[n] = c2[n] + ar2 * ch2[t11++];
                                int n2 = t5++;
                                c2[n2] = c2[n2] + ai2 * ch2[t12++];
                                ++ik;
                            }
                            ++j;
                        }
                        ++l;
                    }
                    t1 = 0;
                    j = 1;
                    while (j < ipph) {
                        t2 = t1 += idl1;
                        ik = 0;
                        while (ik < idl1) {
                            int n = ik++;
                            ch2[n] = ch2[n] + ch2[t2++];
                        }
                        ++j;
                    }
                    t1 = 0;
                    t2 = ipp2 * t0;
                    j = 1;
                    while (j < ipph) {
                        t3 = t1 += t0;
                        t4 = t2 -= t0;
                        k = 0;
                        while (k < l1) {
                            ch[t3] = c1[t3] - c1[t4];
                            ch[t4] = c1[t3] + c1[t4];
                            t3 += ido;
                            t4 += ido;
                            ++k;
                        }
                        ++j;
                    }
                    if (ido == 1) {
                        state = 132;
                        continue block10;
                    }
                    if (nbd < l1) {
                        state = 128;
                        continue block10;
                    }
                    t1 = 0;
                    t2 = ipp2 * t0;
                    j = 1;
                    while (j < ipph) {
                        t3 = t1 += t0;
                        t4 = t2 -= t0;
                        k = 0;
                        while (k < l1) {
                            t5 = t3;
                            t6 = t4;
                            i = 2;
                            while (i < ido) {
                                ch[(t5 += 2) - 1] = c1[t5 - 1] - c1[t6 += 2];
                                ch[t6 - 1] = c1[t5 - 1] + c1[t6];
                                ch[t5] = c1[t5] + c1[t6 - 1];
                                ch[t6] = c1[t5] - c1[t6 - 1];
                                i += 2;
                            }
                            t3 += ido;
                            t4 += ido;
                            ++k;
                        }
                        ++j;
                    }
                    state = 132;
                    continue block10;
                }
                case 128: {
                    int t6;
                    int t5;
                    int i;
                    int t4;
                    int t3;
                    int k;
                    int t1 = 0;
                    int t2 = ipp2 * t0;
                    int j = 1;
                    while (j < ipph) {
                        t3 = t1 += t0;
                        t4 = t2 -= t0;
                        i = 2;
                        while (i < ido) {
                            t5 = t3 += 2;
                            t6 = t4 += 2;
                            k = 0;
                            while (k < l1) {
                                ch[t5 - 1] = c1[t5 - 1] - c1[t6];
                                ch[t6 - 1] = c1[t5 - 1] + c1[t6];
                                ch[t5] = c1[t5] + c1[t6 - 1];
                                ch[t6] = c1[t5] - c1[t6 - 1];
                                t5 += ido;
                                t6 += ido;
                                ++k;
                            }
                            i += 2;
                        }
                        ++j;
                    }
                }
                case 132: {
                    int i;
                    int t3;
                    int k;
                    int t2;
                    if (ido == 1) {
                        return;
                    }
                    int ik = 0;
                    while (ik < idl1) {
                        c2[ik] = ch2[ik];
                        ++ik;
                    }
                    int t1 = 0;
                    int j = 1;
                    while (j < ip) {
                        t2 = t1 += t0;
                        k = 0;
                        while (k < l1) {
                            c1[t2] = ch[t2];
                            t2 += ido;
                            ++k;
                        }
                        ++j;
                    }
                    if (nbd > l1) {
                        state = 139;
                        continue block10;
                    }
                    int is = -ido - 1;
                    t1 = 0;
                    j = 1;
                    while (j < ip) {
                        int idij = is += ido;
                        t2 = t1 += t0;
                        i = 2;
                        while (i < ido) {
                            idij += 2;
                            t3 = t2 += 2;
                            k = 0;
                            while (k < l1) {
                                c1[t3 - 1] = wa[index + idij - 1] * ch[t3 - 1] - wa[index + idij] * ch[t3];
                                c1[t3] = wa[index + idij - 1] * ch[t3] + wa[index + idij] * ch[t3 - 1];
                                t3 += ido;
                                ++k;
                            }
                            i += 2;
                        }
                        ++j;
                    }
                    return;
                }
                case 139: {
                    int i;
                    int t3;
                    int k;
                    int t2;
                    int is = -ido - 1;
                    int t1 = 0;
                    int j = 1;
                    while (j < ip) {
                        is += ido;
                        t2 = t1 += t0;
                        k = 0;
                        while (k < l1) {
                            int idij = is;
                            t3 = t2;
                            i = 2;
                            while (i < ido) {
                                c1[(t3 += 2) - 1] = wa[index + (idij += 2) - 1] * ch[t3 - 1] - wa[index + idij] * ch[t3];
                                c1[t3] = wa[index + idij - 1] * ch[t3] + wa[index + idij] * ch[t3 - 1];
                                i += 2;
                            }
                            t2 += ido;
                            ++k;
                        }
                        ++j;
                    }
                    break block10;
                }
                default: {
                    continue block10;
                }
            }
            break;
        }
    }

    static void drftb1(int n, float[] c, float[] ch, float[] wa, int index, int[] ifac) {
        int l2 = 0;
        int ip = 0;
        int ido = 0;
        int idl1 = 0;
        int nf = ifac[1];
        int na = 0;
        int l1 = 1;
        int iw = 1;
        int k1 = 0;
        while (k1 < nf) {
            int state = 100;
            block8: while (true) {
                switch (state) {
                    case 100: {
                        ip = ifac[k1 + 2];
                        l2 = ip * l1;
                        ido = n / l2;
                        idl1 = ido * l1;
                        if (ip != 4) {
                            state = 103;
                            continue block8;
                        }
                        int ix2 = iw + ido;
                        int ix3 = ix2 + ido;
                        if (na != 0) {
                            Drft.dradb4(ido, l1, ch, c, wa, index + iw - 1, wa, index + ix2 - 1, wa, index + ix3 - 1);
                        } else {
                            Drft.dradb4(ido, l1, c, ch, wa, index + iw - 1, wa, index + ix2 - 1, wa, index + ix3 - 1);
                        }
                        na = 1 - na;
                        state = 115;
                        continue block8;
                    }
                    case 103: {
                        if (ip != 2) {
                            state = 106;
                            continue block8;
                        }
                        if (na != 0) {
                            Drft.dradb2(ido, l1, ch, c, wa, index + iw - 1);
                        } else {
                            Drft.dradb2(ido, l1, c, ch, wa, index + iw - 1);
                        }
                        na = 1 - na;
                        state = 115;
                        continue block8;
                    }
                    case 106: {
                        if (ip != 3) {
                            state = 109;
                            continue block8;
                        }
                        int ix2 = iw + ido;
                        if (na != 0) {
                            Drft.dradb3(ido, l1, ch, c, wa, index + iw - 1, wa, index + ix2 - 1);
                        } else {
                            Drft.dradb3(ido, l1, c, ch, wa, index + iw - 1, wa, index + ix2 - 1);
                        }
                        na = 1 - na;
                        state = 115;
                        continue block8;
                    }
                    case 109: {
                        if (na != 0) {
                            Drft.dradbg(ido, ip, l1, idl1, ch, ch, ch, c, c, wa, index + iw - 1);
                        } else {
                            Drft.dradbg(ido, ip, l1, idl1, c, c, c, ch, ch, wa, index + iw - 1);
                        }
                        if (ido == 1) {
                            na = 1 - na;
                        }
                    }
                    case 115: {
                        l1 = l2;
                        iw += (ip - 1) * ido;
                        break block8;
                    }
                    default: {
                        continue block8;
                    }
                }
                break;
            }
            ++k1;
        }
        if (na == 0) {
            return;
        }
        int i = 0;
        while (i < n) {
            c[i] = ch[i];
            ++i;
        }
    }
}

