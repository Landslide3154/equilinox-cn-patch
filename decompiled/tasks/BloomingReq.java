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

public class BloomingReq
extends CountReq {
    protected BloomingReq() {
        super(GameText.getText(1163), GameText.getText(1164), false, 1);
        this.addListener();
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }

    private void addListener() {
        EventManager.BLOOMING.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                BloomingReq.super.increment();
            }
        }, new String[0]);
    }
}

