/*
 * Decompiled with CFR 0.152.
 */
package classification;

public class Key {
    private int hash;
    private char[] value;
    private int pointer = 0;

    public Key(String key) {
        this.value = key.toCharArray();
    }

    private Key(char[] value, int pointer) {
        this.value = value;
        this.pointer = pointer;
    }

    public char getHead() {
        return this.value[this.pointer];
    }

    public Key getSubKey() {
        return new Key(this.value, this.pointer + 1);
    }

    public boolean equals(Object obj) {
        Key other = (Key)obj;
        int i = 0;
        while (i < this.value.length) {
            if (this.value[i] != other.value[i]) {
                return false;
            }
            ++i;
        }
        return true;
    }

    public int hashCode() {
        int h = this.hash;
        if (h == 0 && this.value.length > 0) {
            char[] val = this.value;
            int i = this.pointer;
            while (i < this.value.length) {
                h = 31 * h + val[i];
                ++i;
            }
            this.hash = h;
        }
        return h;
    }
}

