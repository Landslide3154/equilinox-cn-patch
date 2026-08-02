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

public class SpittingRequirement
extends CountReq {
    protected SpittingRequirement() {
        super(GameText.getText(1112), GameText.getText(1111), false, 25);
        this.addListener();
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }

    private void addListener() {
        EventManager.SPIT_HIT.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                SpittingRequirement.super.increment();
            }
        }, new String[0]);
    }
}

