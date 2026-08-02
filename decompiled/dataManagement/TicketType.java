/*
 * Decompiled with CFR 0.152.
 */
package dataManagement;

public enum TicketType {
    STATIC_ADD_URGENT(0),
    STATIC_ADD(10),
    STATIC_REMOVE_URGENT(0),
    STATIC_REMOVE(20),
    STATIC_DEFRAG(50),
    ENTITY_UPDATE(40),
    DYNAMIC_ADD(0),
    DYNAMIC_DELETE(100),
    DYNAMIC_DEFRAG(60),
    TERRAIN_UPTATE(25),
    SWITCH_TO_DYNAMIC(0),
    SWITCH_TO_STATIC(100);

    private int initialPriority;

    private TicketType(int initialPriority) {
        this.initialPriority = initialPriority;
    }

    public int getPriority() {
        return this.initialPriority;
    }
}

