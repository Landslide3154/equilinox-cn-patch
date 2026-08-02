/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import classification.Classification;
import classification.EntityStructure;
import componentArchitecture.ComponentType;
import entityBundle.EntityBundle;
import gameManaging.GameManager;
import growth.GrowthComponent;
import instances.Entity;
import languages.ComplexString;
import languages.GameText;
import materials.MaterialComponent;
import materials.PresetColour;
import tasks.EntityCountReq;
import toolbox.Colour;

public class ColourEntityReq
extends EntityCountReq {
    private static final float CLOSENESS = 0.35f;
    private static final ComplexString DESC = GameText.getComplexText(233);
    private static final ComplexString SHORT_DESC = GameText.getComplexText(232);
    private final Colour colour;
    private final Classification classification;

    protected ColourEntityReq(Classification classification, int target, PresetColour colour) {
        super(SHORT_DESC.getString(colour.getName(), classification.getName()), DESC.getString(Integer.toString(target), colour.getName(), classification.getName()), classification, target);
        this.colour = colour.getColour();
        this.classification = classification;
    }

    @Override
    protected void doCounting() {
        EntityStructure entities = GameManager.getWorld().getEntityGrid().getSortedEntities();
        EntityBundle bundle = entities.getEntities(this.classification);
        int count = 0;
        for (Entity entity : bundle) {
            MaterialComponent material = (MaterialComponent)entity.getComponent(ComponentType.MATERIAL);
            GrowthComponent grower = (GrowthComponent)entity.getComponent(ComponentType.GROWTH);
            if (!grower.isFullyGrown() || !(Colour.sub(material.getMaterial(), this.colour, null).length() < 0.35f)) continue;
            ++count;
        }
        super.setCount(count);
    }
}

