/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import languages.ComplexString;
import languages.GameText;
import session.Session;
import tasks.CountReq;

public class MoneyCountRequirement
extends CountReq {
    private static final ComplexString TITLE = GameText.getComplexText(103);
    private static final ComplexString DESC = GameText.getComplexText(104);

    protected MoneyCountRequirement(int amount) {
        super(TITLE.getString(Integer.toString(amount)), DESC.getString(Integer.toString(amount)), true, amount);
    }

    @Override
    protected int checkCount(Session session) {
        return session.getStats().getDpCount();
    }
}

