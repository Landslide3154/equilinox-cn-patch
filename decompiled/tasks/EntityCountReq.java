/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import classification.Classification;
import classification.EntityStructure;
import events.EventData;
import events.EventListener;
import events.EventManager;
import gameManaging.GameManager;
import languages.ComplexString;
import languages.GameText;
import session.Session;
import tasks.CountReq;

public class EntityCountReq
extends CountReq {
    private static final ComplexString DESC = GameText.getComplexText(97);
    private static final ComplexString SHORT_DESC = GameText.getComplexText(98);
    private Classification classification;

    protected EntityCountReq(Classification classification, int target) {
        super(SHORT_DESC.getString(classification.getName()), DESC.getString(Integer.toString(target), classification.getName()), false, target);
        this.classification = classification;
        this.addListener();
    }

    protected EntityCountReq(String shortDesc, String desc, Classification classification, int target) {
        super(shortDesc, desc, false, target);
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
        EventManager.POPULATION_REMOVE.addListener(listener, keys);
    }

    protected void doCounting() {
        EntityStructure entities = GameManager.getWorld().getEntityGrid().getSortedEntities();
        int count = entities.getEntityCount(this.classification);
        super.setCount(count);
    }

    private EventListener getListener() {
        return new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                if (EntityCountReq.super.isComplete()) {
                    return;
                }
                EntityCountReq.this.doCounting();
            }
        };
    }
}

