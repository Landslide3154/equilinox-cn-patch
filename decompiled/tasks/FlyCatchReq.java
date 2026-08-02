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

public class FlyCatchReq
extends CountReq {
    protected FlyCatchReq(int count) {
        super(GameText.getText(1174), GameText.getComplexText(1173).getString(Integer.toString(count)), false, count);
        this.addListener();
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }

    private void addListener() {
        EventManager.INSECT_TONGUE.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                FlyCatchReq.super.increment();
            }
        }, "ai175");
    }
}

