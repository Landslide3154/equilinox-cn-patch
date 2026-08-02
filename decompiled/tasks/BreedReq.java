/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import classification.Classification;
import events.EventData;
import events.EventListener;
import events.EventManager;
import languages.ComplexString;
import languages.GameText;
import session.Session;
import tasks.CountReq;

public class BreedReq
extends CountReq {
    private static final ComplexString SHORT_DESC = GameText.getComplexText(555);
    private static final ComplexString LONG_DESC = GameText.getComplexText(556);
    private Classification classification;

    protected BreedReq(Classification classification, int target) {
        super(SHORT_DESC.getString(classification.getName()), LONG_DESC.getString(Integer.toString(target), classification.getName().toLowerCase()), false, target);
        this.classification = classification;
        this.addListener();
    }

    protected BreedReq(String shortDesc, ComplexString longDesc, Classification classification, int target) {
        super(shortDesc, longDesc.getString(Integer.toString(target)), false, target);
        this.classification = classification;
        this.addListener();
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }

    private void addListener() {
        EventListener listener = this.getListener();
        String[] keys = this.classification.getKeyAsArray();
        EventManager.POPULATION_ADD.addListener(listener, keys);
    }

    private EventListener getListener() {
        return new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                if (BreedReq.super.isComplete()) {
                    return;
                }
                BreedReq.super.increment();
            }
        };
    }
}

