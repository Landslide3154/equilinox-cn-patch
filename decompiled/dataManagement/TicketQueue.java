/*
 * Decompiled with CFR 0.152.
 */
package dataManagement;

import dataManagement.Ticket;
import java.util.ArrayList;
import java.util.List;

public class TicketQueue {
    private List<Ticket> waitingList = new ArrayList<Ticket>();
    private List<Ticket> ticketQueue = new ArrayList<Ticket>();

    protected boolean hasTickets() {
        return !this.ticketQueue.isEmpty();
    }

    protected void add(Ticket ticket) {
        this.waitingList.add(ticket);
    }

    protected void update() {
        this.increasePriorities();
        this.addNewTickets();
    }

    protected Ticket get(int i) {
        return this.ticketQueue.get(i);
    }

    protected Ticket remove(int i) {
        return this.ticketQueue.remove(i);
    }

    protected int size() {
        return this.ticketQueue.size();
    }

    private void increasePriorities() {
        for (Ticket ticket : this.ticketQueue) {
            ticket.increasePriority();
        }
    }

    private void addNewTickets() {
        while (!this.waitingList.isEmpty()) {
            this.addTicketToQueue(this.waitingList.remove(0));
        }
    }

    private void addTicketToQueue(Ticket ticket) {
        ArrayList<Ticket> associatedTickets = new ArrayList<Ticket>();
        associatedTickets.add(ticket);
        int index = this.ticketQueue.size() - 1;
        while (index >= 0) {
            Ticket compareTicket = this.ticketQueue.get(index);
            if (compareTicket.getPriority() < ticket.getPriority()) {
                this.ticketQueue.addAll(index + 1, associatedTickets);
                return;
            }
            if (compareTicket.relatedEntity != null && compareTicket.relatedEntity == ticket.relatedEntity) {
                this.ticketQueue.remove(index);
                compareTicket.setPriority(ticket.getPriority());
                associatedTickets.add(0, compareTicket);
            }
            --index;
        }
        this.ticketQueue.addAll(0, associatedTickets);
    }
}

