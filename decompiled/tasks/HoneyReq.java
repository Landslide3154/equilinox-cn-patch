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

public class HoneyReq
extends CountReq {
    private static final String TITLE = GameText.getText(101);
    private static final String DESC = GameText.getText(102);

    protected HoneyReq() {
        super(TITLE, DESC, false, 1);
        this.addListener();
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }

    private void addListener() {
        EventManager.HONEY_FULL.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                if (HoneyReq.super.isComplete()) {
                    return;
                }
                HoneyReq.super.setCount(1);
            }
        }, "full");
    }
}

