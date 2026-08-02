/*
 * Decompiled with CFR 0.152.
 */
package tasks;

import events.EventData;
import events.EventListener;
import events.EventManager;
import languages.ComplexString;
import languages.GameText;
import session.Session;
import tasks.CountReq;

public class TreeCutReq
extends CountReq {
    private static final ComplexString SHORT_DESC = GameText.getComplexText(664);
    private static final ComplexString LONG_DESC = GameText.getComplexText(665);

    protected TreeCutReq(int target) {
        super(SHORT_DESC.getString(Integer.toString(target)), LONG_DESC.getString(Integer.toString(target)), false, target);
        this.addListener();
    }

    @Override
    protected int checkCount(Session session) {
        return 0;
    }

    private void addListener() {
        EventManager.TREE_CUT.addListener(new EventListener(){

            @Override
            public void eventOccurred(EventData data) {
                if (!TreeCutReq.super.getTask().isLocked()) {
                    TreeCutReq.super.increment();
                }
            }
        }, new String[0]);
    }
}

