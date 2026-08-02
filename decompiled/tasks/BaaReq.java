/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import events.EventData;
import events.EventListener;
import events.EventManager;
import languages.GameText;
import session.Session;
import tasks.CountReq;

public class BaaReq
extends CountReq {
    private static final String NAME = GameText.getText(662);
    private static final String DESC = GameText.getText(663);

    protected BaaReq() {
        super(NAME, DESC, false, 1);
        EventManager.NOISE.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                BaaReq.super.increment();
            }
        }, new String[0]);
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }
}

