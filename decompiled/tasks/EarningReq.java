/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import languages.ComplexString;
import languages.GameText;
import session.Session;
import tasks.CountReq;
import toolbox.Maths;

public class EarningReq
extends CountReq {
    private static final String SHORT_DESC = GameText.getText(984);
    private static final ComplexString DESC = GameText.getComplexText(985);

    protected EarningReq(int amount) {
        super(SHORT_DESC, DESC.getString(Maths.formatNumber(amount)), true, amount);
    }

    @Override
    protected int checkCount(Session session) {
        return session.getStats().getDpPerMinute();
    }
}

