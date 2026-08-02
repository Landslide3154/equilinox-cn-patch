/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import classification.Classification;
import componentArchitecture.ComponentType;
import events.EventData;
import events.EventListener;
import events.EventManager;
import instances.Entity;
import languages.ComplexString;
import languages.GameText;
import materials.MaterialComponent;
import materials.PresetColour;
import session.Session;
import tasks.CountReq;
import toolbox.Colour;

public class EatReq
extends CountReq {
    private static final ComplexString DESC = GameText.getComplexText(552);
    private static final ComplexString SHORT_DESC = GameText.getComplexText(551);
    private static final ComplexString DESC_COL = GameText.getComplexText(582);
    private static final ComplexString SHORT_DESC_COL = GameText.getComplexText(581);

    protected EatReq(Classification food, Classification eater, int target) {
        super(SHORT_DESC.getString(food.getName()), DESC.getString(eater.getName().toLowerCase(), Integer.toString(target), food.getName().toLowerCase()), false, target);
        this.addListener(food, eater);
    }

    protected EatReq(Classification food, Classification eater, int target, PresetColour targetColour) {
        super(SHORT_DESC_COL.getString(targetColour.getName(), food.getName()), DESC_COL.getString(eater.getName().toLowerCase(), Integer.toString(target), targetColour.getName(), food.getName().toLowerCase()), false, target);
        this.addColourListener(food, eater, targetColour);
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }

    private void addListener(Classification food, Classification eater) {
        EventManager.EAT.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                EatReq.super.increment();
            }
        }, food.getKey(), eater.getKey());
    }

    private void addColourListener(Classification food, Classification eater, final PresetColour targetColour) {
        EventManager.EAT.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                Entity food = (Entity)data.object;
                MaterialComponent material = (MaterialComponent)food.getComponent(ComponentType.MATERIAL);
                float diff = Colour.calculateDifference(material.getMaterial(), targetColour.getColour());
                if (diff < 0.05f) {
                    EatReq.super.increment();
                }
            }
        }, food.getKey(), eater.getKey());
    }
}

