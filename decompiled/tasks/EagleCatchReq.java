/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import events.EventData;
import events.EventListener;
import events.EventManager;
import session.Session;
import tasks.CountReq;

public class EagleCatchReq
extends CountReq {
    protected EagleCatchReq() {
        super("Hares Caught", "Have an Eagle catch a Desert Hare", false, 1);
        this.addListener();
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }

    private void addListener() {
        EventManager.EAGLE_CATCH.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                EagleCatchReq.super.increment();
            }
        }, "ahs73");
    }
}

