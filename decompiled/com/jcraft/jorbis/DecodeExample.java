/*
 * Decompiled with CFR 0.152.
 */
package com.jcraft.jorbis;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;
import java.io.FileInputStream;

class DecodeExample {
    static int convsize = 8192;
    static byte[] convbuffer = new byte[convsize];

    DecodeExample() {
    }

    /*
     * Unable to fully structure code
     */
    public static void main(String[] arg) {
        input = System.in;
        if (arg.length > 0) {
            try {
                input = new FileInputStream(arg[0]);
            }
            catch (Exception e) {
                System.err.println(e);
            }
        }
        oy = new SyncState();
        os = new StreamState();
        og = new Page();
        op = new Packet();
        vi = new Info();
        vc = new Comment();
        vd = new DspState();
        vb = new Block(vd);
        bytes = 0;
        oy.init();
        while (true) {
            block37: {
                block36: {
                    eos = false;
                    index = oy.buffer(4096);
                    buffer = oy.data;
                    try {
                        bytes = input.read(buffer, index, 4096);
                    }
                    catch (Exception e) {
                        System.err.println(e);
                        System.exit(-1);
                    }
                    oy.wrote(bytes);
                    if (oy.pageout(og) != 1) {
                        if (bytes < 4096) break;
                        System.err.println("Input does not appear to be an Ogg bitstream.");
                        System.exit(1);
                    }
                    os.init(og.serialno());
                    vi.init();
                    vc.init();
                    if (os.pagein(og) < 0) {
                        System.err.println("Error reading first page of Ogg bitstream data.");
                        System.exit(1);
                    }
                    if (os.packetout(op) != 1) {
                        System.err.println("Error reading initial header packet.");
                        System.exit(1);
                    }
                    if (vi.synthesis_headerin(vc, op) < 0) {
                        System.err.println("This Ogg bitstream does not contain Vorbis audio data.");
                        System.exit(1);
                    }
                    i = 0;
                    break block36;
                    while ((result = oy.pageout(og)) != 0) {
                        if (result == 1) {
                            os.pagein(og);
                            while (i < 2) {
                                result = os.packetout(op);
                                if (result == 0) break;
                                if (result == -1) {
                                    System.err.println("Corrupt secondary header.  Exiting.");
                                    System.exit(1);
                                }
                                vi.synthesis_headerin(vc, op);
                                ++i;
                            }
                        }
lbl62:
                        // 5 sources

                        ** while (i >= 2)
lbl63:
                        // 1 sources

                    }
lbl64:
                    // 2 sources

                    index = oy.buffer(4096);
                    buffer = oy.data;
                    try {
                        bytes = input.read(buffer, index, 4096);
                    }
                    catch (Exception e) {
                        System.err.println(e);
                        System.exit(1);
                    }
                    if (bytes == 0 && i < 2) {
                        System.err.println("End of file before finding all Vorbis headers!");
                        System.exit(1);
                    }
                    oy.wrote(bytes);
                }
                if (i < 2) ** GOTO lbl62
                ptr = vc.user_comments;
                j = 0;
                while (j < ptr.length) {
                    if (ptr[j] == null) break;
                    System.err.println(new String(ptr[j], 0, ptr[j].length - 1));
                    ++j;
                }
                System.err.println("\nBitstream is " + vi.channels + " channel, " + vi.rate + "Hz");
                System.err.println("Encoded by: " + new String(vc.vendor, 0, vc.vendor.length - 1) + "\n");
                DecodeExample.convsize = 4096 / vi.channels;
                vd.synthesis_init(vi);
                vb.init(vd);
                _pcm = new float[1][][];
                _index = new int[vi.channels];
                break block37;
                while ((result = oy.pageout(og)) != 0) {
                    if (result == -1) {
                        System.err.println("Corrupt or missing data in bitstream; continuing...");
                    } else {
                        os.pagein(og);
                        while ((result = os.packetout(op)) != 0) {
                            if (result == -1) continue;
                            if (vb.synthesis(op) == 0) {
                                vd.synthesis_blockin(vb);
                            }
                            while ((samples = vd.synthesis_pcmout(_pcm, _index)) > 0) {
                                pcm = _pcm[0];
                                bout = samples < DecodeExample.convsize ? samples : DecodeExample.convsize;
                                i = 0;
                                while (i < vi.channels) {
                                    ptr = i * 2;
                                    mono = _index[i];
                                    j = 0;
                                    while (j < bout) {
                                        val = (int)((double)pcm[i][mono + j] * 32767.0);
                                        if (val > 32767) {
                                            val = 32767;
                                        }
                                        if (val < -32768) {
                                            val = -32768;
                                        }
                                        if (val < 0) {
                                            val |= 32768;
                                        }
                                        DecodeExample.convbuffer[ptr] = (byte)val;
                                        DecodeExample.convbuffer[ptr + 1] = (byte)(val >>> 8);
                                        ptr += 2 * vi.channels;
                                        ++j;
                                    }
                                    ++i;
                                }
                                System.out.write(DecodeExample.convbuffer, 0, 2 * vi.channels * bout);
                                vd.synthesis_read(bout);
                            }
                        }
                        if (og.eos() != 0) {
                            eos = true;
                        }
                    }
lbl136:
                    // 5 sources

                    ** while (eos)
lbl137:
                    // 1 sources

                }
lbl138:
                // 2 sources

                if (!eos) {
                    index = oy.buffer(4096);
                    buffer = oy.data;
                    try {
                        bytes = input.read(buffer, index, 4096);
                    }
                    catch (Exception e) {
                        System.err.println(e);
                        System.exit(1);
                    }
                    oy.wrote(bytes);
                    if (bytes == 0) {
                        eos = true;
                    }
                }
            }
            if (!eos) ** GOTO lbl136
            os.clear();
            vb.clear();
            vd.clear();
            vi.clear();
        }
        oy.clear();
        System.err.println("Done.");
    }
}

