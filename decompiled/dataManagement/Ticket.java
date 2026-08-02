/*
 * Decompiled with CFR 0.152.
 */
package dataManagement;

import dataManagement.TicketType;
import instances.Entity;

public abstract class Ticket {
    protected final Entity relatedEntity;
    private int priority;
    private TicketType type;
    private boolean vboUpdateRequired = true;

    protected Ticket(TicketType type, Entity relatedEntity) {
        this.relatedEntity = relatedEntity;
        this.priority = type.getPriority();
        this.type = type;
    }

    public String toString() {
        return this.type.toString();
    }

    protected boolean isType(TicketType testType) {
        return this.type == testType;
    }

    protected void indicateNoVboUpdate() {
        this.vboUpdateRequired = false;
    }

    protected void setPriority(int priority) {
        this.priority = priority;
    }

    protected boolean vboUpdated() {
        return this.vboUpdateRequired;
    }

    protected void increasePriority() {
        --this.priority;
    }

    protected int getPriority() {
        return this.priority;
    }

    protected boolean isCritical() {
        return this.priority <= 0;
    }

    protected abstract boolean carryOut();
}

