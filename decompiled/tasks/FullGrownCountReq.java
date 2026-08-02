/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import classification.Classification;
import componentArchitecture.ComponentType;
import entityBundle.EntityBundle;
import growth.GrowthComponent;
import instances.Entity;
import languages.ComplexString;
import languages.GameText;
import session.Session;
import tasks.CountReq;

public class FullGrownCountReq
extends CountReq {
    private static final ComplexString SHORT_DESC = GameText.getComplexText(215);
    private static final ComplexString LONG_DESC = GameText.getComplexText(216);
    private final Classification classification;

    protected FullGrownCountReq(Classification classification, int target) {
        super(SHORT_DESC.getString(classification.getName()), LONG_DESC.getString(Integer.toString(target), classification.getName()), true, target);
        this.classification = classification;
    }

    @Override
    protected int checkCount(Session session) {
        int counter = 0;
        EntityBundle entities = session.getWorld().getEntityGrid().getSortedEntities().getEntities(this.classification);
        if (entities == null) {
            return 0;
        }
        for (Entity entity : entities) {
            GrowthComponent growth = (GrowthComponent)entity.getComponent(ComponentType.GROWTH);
            if (!growth.isFullyGrown()) continue;
            ++counter;
        }
        return counter;
    }
}

