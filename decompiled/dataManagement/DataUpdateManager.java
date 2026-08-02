/*
 * Decompiled with CFR 0.152.
 */
package dataManagement;

import batches.DynamicBatch;
import batches.StaticBatch;
import blueprints.Blueprint;
import dataManagement.DataUpdateTickets;
import dataManagement.Ticket;
import dataManagement.TicketQueue;
import glRequestProcessing.GlRequest;
import glRequestProcessing.GlRequestProcessor;
import instances.Entity;
import java.util.List;
import terrains.Terrain;
import toolbox.Maths;

public class DataUpdateManager {
    private StaticBatch[] staticBatches;
    private DynamicBatch dynamicBatch;
    private TicketQueue ticketQueue = new TicketQueue();
    private boolean loaded = false;
    private DataUpdateTickets tickets = new DataUpdateTickets(this);

    public DataUpdateManager(final int staticBatchCount) {
        GlRequestProcessor.sendRequest(new GlRequest(){

            @Override
            public void executeGlRequest() {
                DataUpdateManager.this.initEmptyStaticBatches(staticBatchCount);
                DataUpdateManager.this.dynamicBatch = new DynamicBatch();
                DataUpdateManager.this.createStaticDefragTickets();
                DataUpdateManager.this.addDynamicDefragTicket();
                DataUpdateManager.this.loaded = true;
            }
        });
    }

    public DataUpdateManager(final List<List<Entity>> staticEntities, final List<Entity> dynamicEntities) {
        GlRequestProcessor.sendRequest(new GlRequest(){

            @Override
            public void executeGlRequest() {
                DataUpdateManager.this.initStaticBatches(staticEntities);
                DataUpdateManager.this.dynamicBatch = new DynamicBatch(dynamicEntities);
                DataUpdateManager.this.createStaticDefragTickets();
                DataUpdateManager.this.addDynamicDefragTicket();
                DataUpdateManager.this.loaded = true;
            }
        });
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void delete() {
        GlRequestProcessor.sendRequest(new GlRequest(){

            @Override
            public void executeGlRequest() {
                DataUpdateManager.this.loaded = false;
                DataUpdateManager.this.deleteStaticBatches();
                DataUpdateManager.this.dynamicBatch.delete();
            }
        });
    }

    public StaticBatch getStaticBatch(int id) {
        id = Maths.clampInt(id, 0, this.staticBatches.length - 1);
        return this.staticBatches[id];
    }

    public DynamicBatch getDynamicBatch() {
        return this.dynamicBatch;
    }

    public StaticBatch[] getStaticBatches() {
        return this.staticBatches;
    }

    public synchronized void addStaticEntity(Entity entity, int batchId, boolean urgent) {
        DataUpdateTickets dataUpdateTickets = this.tickets;
        dataUpdateTickets.getClass();
        this.ticketQueue.add(new DataUpdateTickets.StaticAdd(dataUpdateTickets, entity, batchId, urgent));
    }

    public synchronized void addDynamicEntity(Entity entity) {
        if (this.dynamicBatch != null && this.dynamicBatch.attemptToAddWithoutUpdate(entity)) {
            return;
        }
        DataUpdateTickets dataUpdateTickets = this.tickets;
        dataUpdateTickets.getClass();
        this.ticketQueue.add(new DataUpdateTickets.DynamicAdd(dataUpdateTickets, entity));
    }

    public synchronized void removeStaticEntity(Entity entity, boolean urgent) {
        DataUpdateTickets dataUpdateTickets = this.tickets;
        dataUpdateTickets.getClass();
        this.ticketQueue.add(new DataUpdateTickets.StaticRemove(dataUpdateTickets, entity, urgent));
    }

    public synchronized void removeDynamicEntity(Entity entity) {
        this.dynamicBatch.removeNow(entity);
    }

    public synchronized void updateStaticEntity(Entity entity, int newBatchId) {
        DataUpdateTickets dataUpdateTickets = this.tickets;
        dataUpdateTickets.getClass();
        this.ticketQueue.add(new DataUpdateTickets.StaticUpdate(dataUpdateTickets, entity, newBatchId));
    }

    public synchronized void makeDynamic(Entity entity) {
        entity.setCurrentBatchLocation(false);
        DataUpdateTickets dataUpdateTickets = this.tickets;
        dataUpdateTickets.getClass();
        this.ticketQueue.add(new DataUpdateTickets.DynamicSwitch(dataUpdateTickets, entity, entity.getBatchId()));
    }

    public synchronized void makeStatic(Entity entity, int batchId) {
        entity.setCurrentBatchLocation(true);
        DataUpdateTickets dataUpdateTickets = this.tickets;
        dataUpdateTickets.getClass();
        this.ticketQueue.add(new DataUpdateTickets.StaticSwitch(dataUpdateTickets, entity, batchId));
    }

    public synchronized void delete(Blueprint blueprint) {
        DataUpdateTickets dataUpdateTickets = this.tickets;
        dataUpdateTickets.getClass();
        this.ticketQueue.add(new DataUpdateTickets.DynamicDelete(dataUpdateTickets, blueprint));
    }

    public synchronized void updateTerrain(Terrain terrain) {
        DataUpdateTickets dataUpdateTickets = this.tickets;
        dataUpdateTickets.getClass();
        this.ticketQueue.add(new DataUpdateTickets.TerrainUpdate(dataUpdateTickets, terrain));
    }

    public synchronized void update() {
        this.ticketQueue.update();
        if (this.ticketQueue.hasTickets()) {
            this.carryOutImportantTickets();
        }
    }

    protected void addStaticDefragTicket(int batchId) {
        DataUpdateTickets dataUpdateTickets = this.tickets;
        dataUpdateTickets.getClass();
        this.ticketQueue.add(new DataUpdateTickets.StaticDefrag(dataUpdateTickets, batchId));
    }

    protected void addDynamicDefragTicket() {
        this.ticketQueue.add(new DataUpdateTickets.DynamicDefrag(this.tickets));
    }

    private void carryOutImportantTickets() {
        boolean updateOccurred = false;
        int pointer = 0;
        int originalLength = this.ticketQueue.size();
        int updates = 0;
        do {
            boolean complete;
            if (complete = this.ticketQueue.get(pointer).carryOut()) {
                Ticket ticket = this.ticketQueue.remove(pointer);
                updateOccurred |= ticket.vboUpdated();
                updates = ticket.vboUpdated() ? updates + 1 : updates;
                --originalLength;
                continue;
            }
            ++pointer;
        } while (pointer < originalLength && (!updateOccurred || this.ticketQueue.get(pointer).isCritical()));
    }

    private void initEmptyStaticBatches(int count) {
        this.staticBatches = new StaticBatch[count];
        int i = 0;
        while (i < count) {
            this.staticBatches[i] = new StaticBatch();
            ++i;
        }
    }

    private void initStaticBatches(List<List<Entity>> entityBatches) {
        this.staticBatches = new StaticBatch[entityBatches.size()];
        int i = 0;
        while (i < entityBatches.size()) {
            this.staticBatches[i] = new StaticBatch(entityBatches.get(i), i);
            ++i;
        }
    }

    private void deleteStaticBatches() {
        StaticBatch[] staticBatchArray = this.staticBatches;
        int n = this.staticBatches.length;
        int n2 = 0;
        while (n2 < n) {
            StaticBatch batch = staticBatchArray[n2];
            batch.delete();
            ++n2;
        }
    }

    private void createStaticDefragTickets() {
        int i = 0;
        while (i < this.staticBatches.length) {
            this.addStaticDefragTicket(i);
            ++i;
        }
    }
}

