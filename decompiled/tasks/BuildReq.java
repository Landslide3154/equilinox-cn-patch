/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import building.BuildComponent;
import classification.Classification;
import events.EventData;
import events.EventListener;
import events.EventManager;
import session.Session;
import tasks.CountReq;

public class BuildReq
extends CountReq {
    protected BuildReq(Classification speciesClassification, String shortDesc, String longDesc) {
        super(shortDesc, longDesc, false, 1);
        EventManager.BUILD.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                BuildComponent buildComp = (BuildComponent)data.object;
                if (buildComp.isFullyBuilt()) {
                    BuildReq.super.increment();
                }
            }
        }, speciesClassification.getKey());
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }
}

