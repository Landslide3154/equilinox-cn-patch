/*
 * Decompiled with CFR 0.152.
 */
package oggLoading;

import com.jcraft.jogg.Packet;
import com.jcraft.jogg.Page;
import com.jcraft.jogg.StreamState;
import com.jcraft.jogg.SyncState;
import com.jcraft.jorbis.Block;
import com.jcraft.jorbis.Comment;
import com.jcraft.jorbis.DspState;
import com.jcraft.jorbis.Info;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class OggInputStream
extends FilterInputStream {
    private float[][][] _pcm = new float[1][][];
    private int[] _index;
    private boolean eos = false;
    private SyncState syncState = new SyncState();
    private StreamState streamState = new StreamState();
    private Page page = new Page();
    private Packet packet = new Packet();
    private Info info = new Info();
    private Comment comment = new Comment();
    private DspState dspState = new DspState();
    private Block block = new Block(this.dspState);
    private static int convsize = 8192;
    private static byte[] convbuffer = new byte[convsize];
    private int convbufferOff = 0;
    private int convbufferSize = 0;
    private byte[] readDummy = new byte[1];

    public OggInputStream(InputStream input) {
        super(input);
        try {
            this.initVorbis();
            this._index = new int[this.info.channels];
        }
        catch (Exception e) {
            e.printStackTrace();
            this.eos = true;
        }
    }

    public int getFormat() {
        if (this.info.channels == 1) {
            return 4353;
        }
        return 4355;
    }

    public int getRate() {
        return this.info.rate;
    }

    @Override
    public int read() throws IOException {
        int retVal = this.read(this.readDummy, 0, 1);
        return retVal == -1 ? -1 : this.readDummy[0];
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (this.eos) {
            return -1;
        }
        int bytesRead = 0;
        while (!this.eos && len > 0) {
            this.fillConvbuffer();
            if (this.eos) continue;
            int bytesToCopy = Math.min(len, this.convbufferSize - this.convbufferOff);
            System.arraycopy(convbuffer, this.convbufferOff, b, off, bytesToCopy);
            this.convbufferOff += bytesToCopy;
            bytesRead += bytesToCopy;
            len -= bytesToCopy;
            off += bytesToCopy;
        }
        return bytesRead;
    }

    public int read(ByteBuffer b, int off, int len) throws IOException {
        if (this.eos) {
            return -1;
        }
        b.position(off);
        int bytesRead = 0;
        while (!this.eos && len > 0) {
            this.fillConvbuffer();
            if (this.eos) continue;
            int bytesToCopy = Math.min(len, this.convbufferSize - this.convbufferOff);
            b.put(convbuffer, this.convbufferOff, bytesToCopy);
            this.convbufferOff += bytesToCopy;
            bytesRead += bytesToCopy;
            len -= bytesToCopy;
        }
        return bytesRead;
    }

    public boolean isEndOfStream() {
        return this.eos;
    }

    @Override
    public int available() throws IOException {
        return this.eos ? 0 : 1;
    }

    @Override
    public void reset() throws IOException {
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public long skip(long n) throws IOException {
        int bytesRead = 0;
        while ((long)bytesRead < n) {
            int res = this.read();
            if (res == -1) break;
            ++bytesRead;
        }
        return bytesRead;
    }

    private void fillConvbuffer() throws IOException {
        if (this.convbufferOff >= this.convbufferSize) {
            this.convbufferSize = this.lazyDecodePacket();
            this.convbufferOff = 0;
            if (this.convbufferSize == -1) {
                this.eos = true;
            }
        }
    }

    /*
     * Unable to fully structure code
     */
    private void initVorbis() throws Exception {
        block12: {
            this.syncState.init();
            index = this.syncState.buffer(4096);
            buffer = this.syncState.data;
            bytes = this.in.read(buffer, index, 4096);
            this.syncState.wrote(bytes);
            if (this.syncState.pageout(this.page) != 1) {
                if (bytes < 4096) {
                    return;
                }
                throw new Exception("Input does not appear to be an Ogg bitstream.");
            }
            this.streamState.init(this.page.serialno());
            this.info.init();
            this.comment.init();
            if (this.streamState.pagein(this.page) < 0) {
                throw new Exception("Error reading first page of Ogg bitstream data.");
            }
            if (this.streamState.packetout(this.packet) != 1) {
                throw new Exception("Error reading initial header packet.");
            }
            if (this.info.synthesis_headerin(this.comment, this.packet) < 0) {
                throw new Exception("This Ogg bitstream does not contain Vorbis audio data.");
            }
            i = 0;
            break block12;
            while ((result = this.syncState.pageout(this.page)) != 0) {
                if (result == 1) {
                    this.streamState.pagein(this.page);
                    while (i < 2) {
                        result = this.streamState.packetout(this.packet);
                        if (result == 0) break;
                        if (result == -1) {
                            throw new Exception("Corrupt secondary header. Exiting.");
                        }
                        this.info.synthesis_headerin(this.comment, this.packet);
                        ++i;
                    }
                }
lbl35:
                // 5 sources

                ** while (i >= 2)
lbl36:
                // 1 sources

            }
lbl37:
            // 2 sources

            if ((bytes = this.in.read(buffer = this.syncState.data, index = this.syncState.buffer(4096), 4096)) < 0) {
                bytes = 0;
            }
            if (bytes == 0 && i < 2) {
                throw new Exception("End of file before finding all Vorbis headers!");
            }
            this.syncState.wrote(bytes);
        }
        if (i < 2) ** GOTO lbl35
        OggInputStream.convsize = 4096 / this.info.channels;
        this.dspState.synthesis_init(this.info);
        this.block.init(this.dspState);
    }

    private int decodePacket(Packet packet) {
        int samples;
        boolean bigEndian;
        boolean bl = bigEndian = ByteOrder.nativeOrder() == ByteOrder.BIG_ENDIAN;
        if (this.block.synthesis(packet) == 0) {
            this.dspState.synthesis_blockin(this.block);
        }
        int convOff = 0;
        while ((samples = this.dspState.synthesis_pcmout(this._pcm, this._index)) > 0) {
            float[][] pcm = this._pcm[0];
            int bout = samples < convsize ? samples : convsize;
            int i = 0;
            while (i < this.info.channels) {
                int ptr = (i << 1) + convOff;
                int mono = this._index[i];
                int j = 0;
                while (j < bout) {
                    int val = (int)((double)pcm[i][mono + j] * 32767.0);
                    OggInputStream.convbuffer[ptr + 0] = (byte)(bigEndian ? val >>> 8 : (val |= (val = Math.max(Short.MIN_VALUE, Math.min(Short.MAX_VALUE, val))) < 0 ? 32768 : 0));
                    OggInputStream.convbuffer[ptr + 1] = (byte)(bigEndian ? val : val >>> 8);
                    ptr += this.info.channels << 1;
                    ++j;
                }
                ++i;
            }
            convOff += 2 * this.info.channels * bout;
            this.dspState.synthesis_read(bout);
        }
        return convOff;
    }

    private int lazyDecodePacket() throws IOException {
        int result = this.getNextPacket(this.packet);
        if (result == -1) {
            return -1;
        }
        return this.decodePacket(this.packet);
    }

    private int getNextPacket(Packet packet) throws IOException {
        boolean fetchedPacket = false;
        while (!this.eos && !fetchedPacket) {
            int result1 = this.streamState.packetout(packet);
            if (result1 == 0) {
                int result2 = 0;
                while (!this.eos && result2 == 0) {
                    result2 = this.syncState.pageout(this.page);
                    if (result2 != 0) continue;
                    this.fetchData();
                }
                if (result2 == 0 && this.page.eos() != 0) {
                    return -1;
                }
                if (result2 == 0) {
                    this.fetchData();
                    continue;
                }
                if (result2 == -1) {
                    System.out.println("syncState.pageout(page) result == -1");
                    return -1;
                }
                int n = this.streamState.pagein(this.page);
                continue;
            }
            if (result1 == -1) {
                System.out.println("streamState.packetout(packet) result == -1");
                return -1;
            }
            fetchedPacket = true;
        }
        return 0;
    }

    private void fetchData() throws IOException {
        if (!this.eos) {
            int index = this.syncState.buffer(4096);
            if (index < 0) {
                this.eos = true;
                return;
            }
            int bytes = this.in.read(this.syncState.data, index, 4096);
            this.syncState.wrote(bytes);
            if (bytes == 0) {
                this.eos = true;
            }
        }
    }

    public String toString() {
        String s = "";
        s = String.valueOf(s) + "version         " + this.info.version + "\n";
        s = String.valueOf(s) + "channels        " + this.info.channels + "\n";
        s = String.valueOf(s) + "rate (hz)       " + this.info.rate;
        return s;
    }

    public static void main(String[] args) {
        try {
            InputStream in = new Object().getClass().getResourceAsStream("/audio/slash.ogg");
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream(262144);
            byteOut.reset();
            byte[] copyBuffer = new byte[4096];
            OggInputStream oggInput = new OggInputStream(in);
            boolean done = false;
            while (!done) {
                int bytesRead = oggInput.read(copyBuffer, 0, copyBuffer.length);
                byteOut.write(copyBuffer, 0, bytesRead);
                boolean bl = done = bytesRead != copyBuffer.length || bytesRead < 0;
            }
            System.out.println(String.valueOf(byteOut.size()) + " bytes read");
            System.out.println(oggInput);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}

