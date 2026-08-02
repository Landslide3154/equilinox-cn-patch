/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import classification.Classification;
import classification.Classifier;
import componentArchitecture.ComponentType;
import entityBundle.EntityBundle;
import growth.GrowthComponent;
import instances.Entity;
import languages.GameText;
import session.Session;
import tasks.CountReq;

public class HalfGrownOakReq
extends CountReq {
    private static final String SHORT_DESC = GameText.getText(977);
    private static final String LONG_DESC = GameText.getText(978);
    private final Classification oakClassification = Classifier.getClassification("ptw2");

    protected HalfGrownOakReq() {
        super(SHORT_DESC, LONG_DESC, true, 1);
    }

    @Override
    protected int checkCount(Session session) {
        int counter = 0;
        EntityBundle entities = session.getWorld().getEntityGrid().getSortedEntities().getEntities(this.oakClassification);
        if (entities == null) {
            return 0;
        }
        for (Entity entity : entities) {
            GrowthComponent growth = (GrowthComponent)entity.getComponent(ComponentType.GROWTH);
            if (!(growth.getGrowthFactor() >= 0.5f)) continue;
            ++counter;
        }
        return counter;
    }
}

