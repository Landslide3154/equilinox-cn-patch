/*
 * Decompiled with CFR 0.152.
 */
package batchingSystem;

import batchingSystem.BatchMemory;
import batchingSystem.MemorySlot;
import batchingSystem.RemapSection;

public class Tester {
    static byte count = 0;

    public static void main(String[] args) {
        BatchMemory memory = BatchMemory.initBatch();
        byte by = count;
        count = (byte)(by + 1);
        byte[] data = Tester.getData(by, 5);
        RemapSection remap = memory.startRemapAdd(data.length);
        MemorySlot slot0 = remap.storeData(data);
        byte by2 = count;
        count = (byte)(by2 + 1);
        MemorySlot slot1 = remap.storeData(Tester.getData(by2, 6));
        remap.remap();
        memory.getMemoryChain().display();
        RemapSection remap2 = memory.startRemapRemove(slot0);
        System.out.println(remap2.getRemaining());
        memory.getMemoryChain().display();
        byte by3 = count;
        count = (byte)(by3 + 1);
        MemorySlot slot2 = remap2.storeData(Tester.getData(by3, 6));
        memory.getMemoryChain().display();
        remap2.remap();
        RemapSection remap3 = memory.startRemapRemove(slot2);
        System.out.println(remap3.getRemaining());
        memory.getMemoryChain().display();
        remap3.remap();
    }

    private static byte[] getData(byte b, int count) {
        byte[] data = new byte[count];
        int i = 0;
        while (i < data.length) {
            data[i] = b;
            ++i;
        }
        return data;
    }
}

