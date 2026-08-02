/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import events.EventData;
import events.EventListener;
import events.EventManager;
import languages.ComplexString;
import languages.GameText;
import session.Session;
import tasks.CountReq;

public class ButterflyCatchReq
extends CountReq {
    private static final String SHORT_DESC = GameText.getText(609);
    private static final ComplexString LONG_DESC = GameText.getComplexText(610);

    protected ButterflyCatchReq(int target) {
        super(SHORT_DESC, LONG_DESC.getString(Integer.toString(target)), false, target);
        EventManager.INSECT_CATCH.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                ButterflyCatchReq.super.increment();
            }
        }, new String[0]);
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }
}

