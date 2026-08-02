/*
 * Decompiled with CFR 0.152.
 */
package dataManagement;

import blueprints.Blueprint;
import dataManagement.DataUpdateManager;
import dataManagement.Ticket;
import dataManagement.TicketType;
import instances.Entity;
import terrains.Terrain;

public class DataUpdateTickets {
    private DataUpdateManager updateManager;

    protected DataUpdateTickets(DataUpdateManager updateManager) {
        this.updateManager = updateManager;
    }

    protected class DynamicAdd
    extends Ticket {
        protected DynamicAdd(Entity relatedEntity) {
            super(TicketType.DYNAMIC_ADD, relatedEntity);
        }

        @Override
        protected boolean carryOut() {
            if (this.relatedEntity.getBlueprint().isLoaded()) {
                DataUpdateTickets.this.updateManager.getDynamicBatch().addEntity(this.relatedEntity);
                return true;
            }
            return false;
        }
    }

    protected class DynamicDefrag
    extends Ticket {
        protected DynamicDefrag() {
            super(TicketType.DYNAMIC_DEFRAG, null);
        }

        @Override
        protected boolean carryOut() {
            boolean updateOccurred = DataUpdateTickets.this.updateManager.getDynamicBatch().defrag();
            if (!updateOccurred) {
                this.indicateNoVboUpdate();
            }
            DataUpdateTickets.this.updateManager.addDynamicDefragTicket();
            return true;
        }
    }

    protected class DynamicDelete
    extends Ticket {
        private Blueprint blueprint;

        protected DynamicDelete(Blueprint blueprint) {
            super(TicketType.DYNAMIC_DELETE, null);
            this.blueprint = blueprint;
        }

        @Override
        protected boolean carryOut() {
            DataUpdateTickets.this.updateManager.getDynamicBatch().delete(this.blueprint);
            return true;
        }
    }

    protected class DynamicSwitch
    extends Ticket {
        protected DynamicSwitch(Entity relatedEntity, int batchId) {
            super(TicketType.SWITCH_TO_DYNAMIC, relatedEntity);
        }

        @Override
        protected boolean carryOut() {
            if (this.relatedEntity.getBlueprint().isLoaded()) {
                DataUpdateTickets.this.updateManager.getStaticBatch(this.relatedEntity.getBatchId()).removeEntity(this.relatedEntity);
                DataUpdateTickets.this.updateManager.getDynamicBatch().addEntity(this.relatedEntity);
                return true;
            }
            return false;
        }
    }

    protected class StaticAdd
    extends Ticket {
        private int batchId;

        protected StaticAdd(Entity relatedEntity, int batchId, boolean urgent) {
            super(urgent ? TicketType.STATIC_ADD_URGENT : TicketType.STATIC_ADD, relatedEntity);
            this.batchId = batchId;
        }

        @Override
        protected boolean carryOut() {
            if (this.relatedEntity.getBlueprint().isLoaded()) {
                DataUpdateTickets.this.updateManager.getStaticBatch(this.batchId).addEntity(this.relatedEntity);
                this.relatedEntity.setBatchId(this.batchId);
                return true;
            }
            return false;
        }
    }

    protected class StaticDefrag
    extends Ticket {
        private int batchId;

        protected StaticDefrag(int batchId) {
            super(TicketType.STATIC_DEFRAG, null);
            this.batchId = batchId;
        }

        @Override
        protected boolean carryOut() {
            boolean updateOccurred = DataUpdateTickets.this.updateManager.getStaticBatch(this.batchId).defrag();
            if (!updateOccurred) {
                this.indicateNoVboUpdate();
            }
            DataUpdateTickets.this.updateManager.addStaticDefragTicket(this.batchId);
            return true;
        }
    }

    protected class StaticRemove
    extends Ticket {
        protected StaticRemove(Entity relatedEntity, boolean urgent) {
            super(urgent ? TicketType.STATIC_REMOVE_URGENT : TicketType.STATIC_REMOVE, relatedEntity);
        }

        @Override
        protected boolean carryOut() {
            if (this.relatedEntity.getBlueprint().isLoaded()) {
                DataUpdateTickets.this.updateManager.getStaticBatch(this.relatedEntity.getBatchId()).removeEntity(this.relatedEntity);
                return true;
            }
            return false;
        }
    }

    protected class StaticSwitch
    extends Ticket {
        private int newBatchId;

        protected StaticSwitch(Entity relatedEntity, int batchId) {
            super(TicketType.SWITCH_TO_STATIC, relatedEntity);
            this.newBatchId = batchId;
        }

        @Override
        protected boolean carryOut() {
            if (this.relatedEntity.getBlueprint().isLoaded()) {
                DataUpdateTickets.this.updateManager.getDynamicBatch().removeNow(this.relatedEntity);
                DataUpdateTickets.this.updateManager.getStaticBatch(this.newBatchId).addEntity(this.relatedEntity);
                this.relatedEntity.setBatchId(this.newBatchId);
                return true;
            }
            return false;
        }
    }

    protected class StaticUpdate
    extends Ticket {
        private int newBatchId;

        protected StaticUpdate(Entity relatedEntity, int newBatchId) {
            super(TicketType.ENTITY_UPDATE, relatedEntity);
            this.newBatchId = newBatchId;
        }

        @Override
        protected boolean carryOut() {
            if (this.relatedEntity.getBlueprint().isLoaded()) {
                int oldBatchId = this.relatedEntity.getBatchId();
                if (oldBatchId == this.newBatchId) {
                    DataUpdateTickets.this.updateManager.getStaticBatch(this.newBatchId).updateEntity(this.relatedEntity);
                } else {
                    DataUpdateTickets.this.updateManager.getStaticBatch(oldBatchId).removeEntity(this.relatedEntity);
                    DataUpdateTickets.this.updateManager.getStaticBatch(this.newBatchId).addEntity(this.relatedEntity);
                    this.relatedEntity.setBatchId(this.newBatchId);
                }
                return true;
            }
            return false;
        }
    }

    protected class TerrainUpdate
    extends Ticket {
        private Terrain terrain;

        protected TerrainUpdate(Terrain terrain) {
            super(TicketType.TERRAIN_UPTATE, null);
            this.terrain = terrain;
        }

        @Override
        protected boolean carryOut() {
            this.terrain.updateVboData();
            return true;
        }
    }
}

