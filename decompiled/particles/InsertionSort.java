/*
 * Decompiled with CFR 0.152.
 */
package particles;

import java.util.List;
import particles.Particle;

public class InsertionSort {
    public static void sortHighToLow(List<Particle> list) {
        int i = 1;
        while (i < list.size()) {
            Particle item = list.get(i);
            if (item.getDistance() > list.get(i - 1).getDistance()) {
                InsertionSort.sortUpHighToLow(list, i);
            }
            ++i;
        }
    }

    private static void sortUpHighToLow(List<Particle> list, int i) {
        Particle item = list.get(i);
        int attemptPos = i - 1;
        while (attemptPos != 0 && list.get(attemptPos - 1).getDistance() < item.getDistance()) {
            --attemptPos;
        }
        list.remove(i);
        list.add(attemptPos, item);
    }
}

