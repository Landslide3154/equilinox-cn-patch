/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

import com.jcraft.jogg.Buffer;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.CodeBook;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.FuncResidue;
import com.jcraft.jorbis.Info;
import com.jcraft.jorbis.InfoMode;
import com.jcraft.jorbis.Util;

class Residue0
extends FuncResidue {
    private static int[][][] _01inverse_partword = new int[2][][];
    static int[][] _2inverse_partword = null;

    Residue0() {
    }

    @Override
    void pack(Object vr, Buffer opb) {
        InfoResidue0 info = (InfoResidue0)vr;
        int acc = 0;
        opb.write(info.begin, 24);
        opb.write(info.end, 24);
        opb.write(info.grouping - 1, 24);
        opb.write(info.partitions - 1, 6);
        opb.write(info.groupbook, 8);
        int j = 0;
        while (j < info.partitions) {
            int i = info.secondstages[j];
            if (Util.ilog(i) > 3) {
                opb.write(i, 3);
                opb.write(1, 1);
                opb.write(i >>> 3, 5);
            } else {
                opb.write(i, 4);
            }
            acc += Util.icount(i);
            ++j;
        }
        j = 0;
        while (j < acc) {
            opb.write(info.booklist[j], 8);
            ++j;
        }
    }

    @Override
    Object unpack(Info vi, Buffer opb) {
        int acc = 0;
        InfoResidue0 info = new InfoResidue0();
        info.begin = opb.read(24);
        info.end = opb.read(24);
        info.grouping = opb.read(24) + 1;
        info.partitions = opb.read(6) + 1;
        info.groupbook = opb.read(8);
        int j = 0;
        while (j < info.partitions) {
            int cascade = opb.read(3);
            if (opb.read(1) != 0) {
                cascade |= opb.read(5) << 3;
            }
            info.secondstages[j] = cascade;
            acc += Util.icount(cascade);
            ++j;
        }
        j = 0;
        while (j < acc) {
            info.booklist[j] = opb.read(8);
            ++j;
        }
        if (info.groupbook >= vi.books) {
            this.free_info(info);
            return null;
        }
        j = 0;
        while (j < acc) {
            if (info.booklist[j] >= vi.books) {
                this.free_info(info);
                return null;
            }
            ++j;
        }
        return info;
    }

    @Override
    Object look(DspState vd, InfoMode vm, Object vr) {
        int k;
        InfoResidue0 info = (InfoResidue0)vr;
        LookResidue0 look = new LookResidue0();
        int acc = 0;
        int maxstage = 0;
        look.info = info;
        look.map = vm.mapping;
        look.parts = info.partitions;
        look.fullbooks = vd.fullbooks;
        look.phrasebook = vd.fullbooks[info.groupbook];
        int dim = look.phrasebook.dim;
        look.partbooks = new int[look.parts][];
        int j = 0;
        while (j < look.parts) {
            int i = info.secondstages[j];
            int stages = Util.ilog(i);
            if (stages != 0) {
                if (stages > maxstage) {
                    maxstage = stages;
                }
                look.partbooks[j] = new int[stages];
                k = 0;
                while (k < stages) {
                    if ((i & 1 << k) != 0) {
                        look.partbooks[j][k] = info.booklist[acc++];
                    }
                    ++k;
                }
            }
            ++j;
        }
        look.partvals = (int)Math.rint(Math.pow(look.parts, dim));
        look.stages = maxstage;
        look.decodemap = new int[look.partvals][];
        j = 0;
        while (j < look.partvals) {
            int val = j;
            int mult = look.partvals / look.parts;
            look.decodemap[j] = new int[dim];
            k = 0;
            while (k < dim) {
                int deco = val / mult;
                val -= deco * mult;
                mult /= look.parts;
                look.decodemap[j][k] = deco;
                ++k;
            }
            ++j;
        }
        return look;
    }

    @Override
    void free_info(Object i) {
    }

    @Override
    void free_look(Object i) {
    }

    static synchronized int _01inverse(Block vb, Object vl, float[][] in, int ch, int decodepart) {
        LookResidue0 look = (LookResidue0)vl;
        InfoResidue0 info = look.info;
        int samples_per_partition = info.grouping;
        int partitions_per_word = look.phrasebook.dim;
        int n = info.end - info.begin;
        int partvals = n / samples_per_partition;
        int partwords = (partvals + partitions_per_word - 1) / partitions_per_word;
        if (_01inverse_partword.length < ch) {
            _01inverse_partword = new int[ch][][];
        }
        int j = 0;
        while (j < ch) {
            if (_01inverse_partword[j] == null || _01inverse_partword[j].length < partwords) {
                Residue0._01inverse_partword[j] = new int[partwords][];
            }
            ++j;
        }
        int s = 0;
        while (s < look.stages) {
            int i = 0;
            int l = 0;
            while (i < partvals) {
                if (s == 0) {
                    j = 0;
                    while (j < ch) {
                        int temp = look.phrasebook.decode(vb.opb);
                        if (temp == -1) {
                            return 0;
                        }
                        Residue0._01inverse_partword[j][l] = look.decodemap[temp];
                        if (_01inverse_partword[j][l] == null) {
                            return 0;
                        }
                        ++j;
                    }
                }
                int k = 0;
                while (k < partitions_per_word && i < partvals) {
                    j = 0;
                    while (j < ch) {
                        CodeBook stagebook;
                        int offset = info.begin + i * samples_per_partition;
                        int index = _01inverse_partword[j][l][k];
                        if ((info.secondstages[index] & 1 << s) != 0 && (stagebook = look.fullbooks[look.partbooks[index][s]]) != null && (decodepart == 0 ? stagebook.decodevs_add(in[j], offset, vb.opb, samples_per_partition) == -1 : decodepart == 1 && stagebook.decodev_add(in[j], offset, vb.opb, samples_per_partition) == -1)) {
                            return 0;
                        }
                        ++j;
                    }
                    ++k;
                    ++i;
                }
                ++l;
            }
            ++s;
        }
        return 0;
    }

    static synchronized int _2inverse(Block vb, Object vl, float[][] in, int ch) {
        LookResidue0 look = (LookResidue0)vl;
        InfoResidue0 info = look.info;
        int samples_per_partition = info.grouping;
        int partitions_per_word = look.phrasebook.dim;
        int n = info.end - info.begin;
        int partvals = n / samples_per_partition;
        int partwords = (partvals + partitions_per_word - 1) / partitions_per_word;
        if (_2inverse_partword == null || _2inverse_partword.length < partwords) {
            _2inverse_partword = new int[partwords][];
        }
        int s = 0;
        while (s < look.stages) {
            int i = 0;
            int l = 0;
            while (i < partvals) {
                if (s == 0) {
                    int temp = look.phrasebook.decode(vb.opb);
                    if (temp == -1) {
                        return 0;
                    }
                    Residue0._2inverse_partword[l] = look.decodemap[temp];
                    if (_2inverse_partword[l] == null) {
                        return 0;
                    }
                }
                int k = 0;
                while (k < partitions_per_word && i < partvals) {
                    CodeBook stagebook;
                    int offset = info.begin + i * samples_per_partition;
                    int index = _2inverse_partword[l][k];
                    if ((info.secondstages[index] & 1 << s) != 0 && (stagebook = look.fullbooks[look.partbooks[index][s]]) != null && stagebook.decodevv_add(in, offset, ch, vb.opb, samples_per_partition) == -1) {
                        return 0;
                    }
                    ++k;
                    ++i;
                }
                ++l;
            }
            ++s;
        }
        return 0;
    }

    @Override
    int inverse(Block vb, Object vl, float[][] in, int[] nonzero, int ch) {
        int used = 0;
        int i = 0;
        while (i < ch) {
            if (nonzero[i] != 0) {
                in[used++] = in[i];
            }
            ++i;
        }
        if (used != 0) {
            return Residue0._01inverse(vb, vl, in, used, 0);
        }
        return 0;
    }

    class InfoResidue0 {
        int begin;
        int end;
        int grouping;
        int partitions;
        int groupbook;
        int[] secondstages = new int[64];
        int[] booklist = new int[256];
        float[] entmax = new float[64];
        float[] ampmax = new float[64];
        int[] subgrp = new int[64];
        int[] blimit = new int[64];

        InfoResidue0() {
        }
    }

    class LookResidue0 {
        InfoResidue0 info;
        int map;
        int parts;
        int stages;
        CodeBook[] fullbooks;
        CodeBook phrasebook;
        int[][] partbooks;
        int partvals;
        int[][] decodemap;
        int postbits;
        int phrasebits;
        int frames;

        LookResidue0() {
        }
    }
}

