/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import blueprints.Blueprint;
import classification.Classification;
import events.EventData;
import events.EventListener;
import events.EventManager;
import languages.ComplexString;
import languages.GameText;
import session.Session;
import tasks.CountReq;

public class EvolutionReq
extends CountReq {
    private static final ComplexString DESC = GameText.getComplexText(547);
    private static final ComplexString DESC2 = GameText.getComplexText(1073);
    private static final ComplexString SHORT_DESC = GameText.getComplexText(548);
    private final Classification speciesClass;

    protected EvolutionReq(Classification species, int target) {
        super(SHORT_DESC.getString(species.getName()), species.isSpecies() ? DESC2.getString(species.getName()) : DESC.getString(Integer.toString(target), species.getName()), false, target);
        this.speciesClass = species;
        this.addListener();
    }

    protected EvolutionReq(String shortDest, String longDesc, Classification species, int target) {
        super(shortDest, longDesc, false, target);
        this.speciesClass = species;
        this.addListener();
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }

    private void addListener() {
        EventManager.EVOLUTION.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                Blueprint unlockedSpecies = (Blueprint)data.object;
                if (unlockedSpecies.getSpeciesClassification().isTypeOf(EvolutionReq.this.speciesClass)) {
                    EvolutionReq.super.increment();
                }
            }
        }, new String[0]);
    }
}

