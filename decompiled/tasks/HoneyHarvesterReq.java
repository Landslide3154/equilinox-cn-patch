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

public class HoneyHarvesterReq
extends CountReq {
    private static final int AMOUNT = 500;
    private static final String TITLE = GameText.getText(679);
    private static final String DESC = GameText.getText(680);

    protected HoneyHarvesterReq() {
        super(TITLE, DESC, false, 500);
        this.addListener();
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }

    private void addListener() {
        EventManager.HONEY_TAKEN.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                if (HoneyHarvesterReq.super.getTask().isLocked()) {
                    return;
                }
                HoneyHarvesterReq.super.increase(data.count);
            }
        }, new String[0]);
    }
}

