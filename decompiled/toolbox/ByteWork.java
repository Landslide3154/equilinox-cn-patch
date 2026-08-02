/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import java.nio.ByteBuffer;
import org.lwjgl.util.vector.Vector3f;

public class ByteWork {
    public static final int FLOAT_LENGTH = 4;
    public static final int INT_LENGTH = 4;
    public static final int SHORT_LENGTH = 2;
    public static final int LONG_LENGTH = 8;

    public static byte[] longToBytes(long number) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(number);
        return buffer.array();
    }

    public static int encodeShortIntoArray(short naughtyShort, byte[] array, int pointer) {
        byte[] shortInBytes = ByteWork.shortToBytes(naughtyShort);
        int i = 0;
        while (i < shortInBytes.length) {
            array[pointer++] = shortInBytes[i];
            ++i;
        }
        return pointer;
    }

    public static short getNextShortFromArray(byte[] array, int pointer) {
        byte[] shortInBytes = new byte[2];
        int i = 0;
        while (i < shortInBytes.length) {
            shortInBytes[i] = array[pointer++];
            ++i;
        }
        return ByteWork.bytesToShort(shortInBytes);
    }

    public static long bytesToLong(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getLong();
    }

    public static byte booleansToByte(boolean ... bools) {
        int pointer = 0;
        byte result = 0;
        boolean[] blArray = bools;
        int n = bools.length;
        int n2 = 0;
        while (n2 < n) {
            boolean b = blArray[n2];
            if (pointer == 8) {
                return result;
            }
            if (b) {
                result = (byte)(result | (byte)Math.pow(2.0, pointer));
            }
            ++pointer;
            ++n2;
        }
        return result;
    }

    public static boolean[] byteToBooleans(byte b) {
        boolean[] result = new boolean[8];
        int i = 0;
        while (i < 8) {
            byte mask = (byte)Math.pow(2.0, i);
            result[i] = (b & mask) == mask;
            ++i;
        }
        return result;
    }

    public static int encodeByteArrayIntoArray(byte[] data, byte[] additional, int pointer) {
        int i = 0;
        while (i < additional.length) {
            data[pointer++] = additional[i];
            ++i;
        }
        return pointer;
    }

    public static long getNextLongFromArray(byte[] array, int pointer) {
        byte[] longInBytes = new byte[8];
        int i = 0;
        while (i < 8) {
            longInBytes[i] = array[pointer++];
            ++i;
        }
        return ByteWork.bytesToLong(longInBytes);
    }

    public static int encodeLongIntoArray(long naughtyLong, byte[] array, int pointer) {
        byte[] longInBytes = ByteWork.longToBytes(naughtyLong);
        int i = 0;
        while (i < 8) {
            array[pointer++] = longInBytes[i];
            ++i;
        }
        return pointer;
    }

    public static int encodeFloatIntoArray(float naughtyFloat, byte[] array, int pointer) {
        byte[] floatInBytes = ByteWork.floatToBytes(naughtyFloat);
        int i = 0;
        while (i < 4) {
            array[pointer++] = floatInBytes[i];
            ++i;
        }
        return pointer;
    }

    public static int encodeVectorIntoArray(Vector3f vector, byte[] array, int pointer) {
        pointer = ByteWork.encodeFloatIntoArray(vector.x, array, pointer);
        pointer = ByteWork.encodeFloatIntoArray(vector.y, array, pointer);
        pointer = ByteWork.encodeFloatIntoArray(vector.z, array, pointer);
        return pointer;
    }

    public static int encodeIntIntoArray(int naughtyInt, byte[] array, int pointer) {
        byte[] intInBytes = ByteWork.intToBytes(naughtyInt);
        int i = 0;
        while (i < 4) {
            array[pointer++] = intInBytes[i];
            ++i;
        }
        return pointer;
    }

    public static float getNextFloatFromArray(byte[] array, int pointer) {
        byte[] floatInBytes = new byte[4];
        int i = 0;
        while (i < 4) {
            floatInBytes[i] = array[pointer++];
            ++i;
        }
        return ByteWork.bytesToFloat(floatInBytes);
    }

    public static int getNextIntFromArray(byte[] array, int pointer) {
        byte[] intInBytes = new byte[4];
        int i = 0;
        while (i < 4) {
            intInBytes[i] = array[pointer++];
            ++i;
        }
        return ByteWork.bytesToInt(intInBytes);
    }

    public static boolean testByteWithMask(byte test, byte mask) {
        return (test & mask) == mask;
    }

    public static int bytesToInt(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getInt();
    }

    public static byte[] intToBytes(int number) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putInt(number);
        return buffer.array();
    }

    public static float bytesToFloat(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getFloat();
    }

    public static byte[] floatToBytes(float number) {
        ByteBuffer buffer = ByteBuffer.allocate(4);
        buffer.putFloat(number);
        return buffer.array();
    }

    public static short bytesToShort(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.put(bytes);
        buffer.flip();
        return buffer.getShort();
    }

    public static byte[] shortToBytes(short number) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putShort(number);
        return buffer.array();
    }

    public static int shortsToInt(short x, short y) {
        int total = x;
        total <<= 16;
        return total += y;
    }
}

