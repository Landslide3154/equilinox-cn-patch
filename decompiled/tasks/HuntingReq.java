/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import blueprints.Blueprint;
import classification.Classification;
import events.EventData;
import events.EventListener;
import events.EventManager;
import instances.Entity;
import languages.ComplexString;
import languages.GameText;
import session.Session;
import tasks.CountReq;

public class HuntingReq
extends CountReq {
    private static final ComplexString SHORT_DESC = GameText.getComplexText(864);
    private static final ComplexString LONG_DESC = GameText.getComplexText(865);
    private final String predatorKey;
    private final Classification prey;

    protected HuntingReq(Blueprint predator, Classification prey, int count) {
        super(SHORT_DESC.getString(prey.getName()), LONG_DESC.getString(Integer.toString(count), predator.getName(), prey.getName()), false, count);
        this.predatorKey = predator.getSpeciesClassification().getKey();
        this.prey = prey;
        this.addListener();
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }

    private void addListener() {
        EventListener listener = new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                Entity preyEntity = (Entity)data.object;
                if (preyEntity.getBlueprint().getSpeciesClassification().isTypeOf(HuntingReq.this.prey)) {
                    HuntingReq.super.increment();
                }
            }
        };
        EventManager.FIGHT_SUCCESS.addListener(listener, this.predatorKey);
    }
}

