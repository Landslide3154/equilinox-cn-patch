/*
 * Decompiled with CFR 0.152.
 */
package events;

import events.EventNode;

public class EventManager {
    public static EventNode POPULATION_CHANGE = new EventNode(null);
    public static EventNode POPULATION_ADD = new EventNode(POPULATION_CHANGE);
    public static EventNode POPULATION_REMOVE = new EventNode(POPULATION_CHANGE);
    public static EventNode SPECIES_UNLOCK = new EventNode(null);
    public static EventNode TASK_COMPLETE = new EventNode(null);
    public static EventNode HONEY_FULL = new EventNode(null);
    public static EventNode EVOLUTION = new EventNode(null);
    public static EventNode EAT = new EventNode(null);
    public static EventNode BUILD = new EventNode(null);
    public static EventNode NOISE = new EventNode(null);
    public static EventNode INSECT_CATCH = new EventNode(null);
    public static EventNode TREE_CUT = new EventNode(null);
    public static EventNode HONEY_TAKEN = new EventNode(null);
    public static EventNode FIGHT_SUCCESS = new EventNode(null);
    public static EventNode SPIT_HIT = new EventNode(null);
    public static EventNode BLOOMING = new EventNode(null);
    public static EventNode INSECT_TONGUE = new EventNode(null);
    public static EventNode EAGLE_CATCH = new EventNode(null);
    public static EventNode HOLE_DIG = new EventNode(null);
}

