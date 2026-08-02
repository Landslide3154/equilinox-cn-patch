/*
 * Decompiled with CFR 0.152.
 */
package evolutionUi;

import gameManaging.GameManager;
import mainGuis.ColourPalette;
import toolbox.Maths;
import userInterfaces.TextButtonUi;

public class PriceButtonUi
extends TextButtonUi {
    private final int cost;

    public PriceButtonUi(int cost, float font) {
        super(String.valueOf(Maths.formatNumber(cost)) + " dp", ColourPalette.GREEN, font);
        this.cost = cost;
    }

    @Override
    protected void init() {
        super.init();
        super.block(!this.isAffordable());
    }

    @Override
    protected void updateSelf() {
        super.block(!this.isAffordable());
        super.updateSelf();
    }

    private boolean isAffordable() {
        return GameManager.getSession().getStats().getDpCount() >= this.cost;
    }
}

