/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import events.EventData;
import events.EventListener;
import events.EventManager;
import session.Session;
import tasks.CountReq;

public class HoleDigReq
extends CountReq {
    protected HoleDigReq() {
        super("Holes Dug", "Have Meerkats dig 5 holes", false, 5);
        this.addListener();
    }

    private void addListener() {
        EventManager.HOLE_DIG.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                HoleDigReq.super.increment();
            }
        }, new String[0]);
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }
}

