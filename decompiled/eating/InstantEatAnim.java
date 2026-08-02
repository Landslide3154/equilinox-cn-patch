/*
 * Decompiled with CFR 0.152.
 */
package eating;

import eating.EatingAnimation;
import eating.StandardEatingAi;

public class InstantEatAnim
implements EatingAnimation {
    private final StandardEatingAi eater;

    public InstantEatAnim(StandardEatingAi eater) {
        this.eater = eater;
    }

    @Override
    public boolean doNomming(boolean targetAvailable) {
        this.eater.eat();
        return true;
    }

    @Override
    public void interrupt() {
    }
}

