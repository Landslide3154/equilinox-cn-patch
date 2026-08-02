/*
 * Decompiled with CFR 0.152.
 */
package session;

import gameManaging.GameManager;
import gameManaging.GameState;
import gameManaging.UserConfigs;
import mainGuis.EquilinoxGuis;
import saves.SaveSlot;
import saves.Saves;
import session.GameMode;
import session.Session;
import world.WorldConfigs;

public class SessionManager {
    public static final int SAVE_SLOT_COUNT = 10;
    private Session currentSession;
    private Session loadingSession;
    private boolean loading = true;
    private final Saves SAVES = new Saves(10);

    public void loadSaveSlot(int slotID) {
        this.saveCurrentSession();
        SaveSlot slot = this.SAVES.getSaveSlot(slotID);
        this.setSession(Session.loadSession(slot));
    }

    public void startNewWorld(WorldConfigs configs, String name, GameMode mode) {
        this.saveCurrentSession();
        SaveSlot newSave = this.SAVES.createNewSave();
        newSave.setName(name);
        newSave.getInfo().setMode(mode);
        this.setSession(Session.createNewSession(newSave, configs, mode));
    }

    public Saves getSaves() {
        return this.SAVES;
    }

    public boolean saveCurrentSession() {
        return this.currentSession.save();
    }

    public void update() {
        if (this.loading) {
            this.checkLoaded();
        }
        if (this.currentSession != null && GameManager.getGameState() != GameState.GAME_MENU) {
            this.currentSession.update();
        } else if (this.currentSession != null && !this.loading) {
            this.currentSession.getWorld().updateAmbientSounds();
        }
    }

    public Session getSession() {
        return this.currentSession;
    }

    public boolean hasWorldReady() {
        return this.currentSession != null;
    }

    public boolean isLoading() {
        return this.loading;
    }

    public Session getLoadingSession() {
        return this.loadingSession;
    }

    public void loadInitialSession() {
        this.currentSession = null;
        SaveSlot slot = this.SAVES.getSaveSlot(UserConfigs.getSaveSlotId());
        if (slot == null || slot.isEmpty() || slot.isCorrupt()) {
            slot = this.SAVES.getFirstWorld();
        }
        if (slot == null) {
            SaveSlot newSaveSlot = this.SAVES.createNewSave();
            WorldConfigs configs = WorldConfigs.createDefault();
            this.setSession(Session.createNewSession(newSaveSlot, configs, GameMode.NORMAL));
        } else {
            this.setSession(Session.loadSession(slot));
        }
    }

    private void checkLoaded() {
        boolean bl = this.loading = !this.loadingSession.isLoaded();
        if (this.loading) {
            return;
        }
        if (this.currentSession != null) {
            this.currentSession.delete();
        }
        this.currentSession = this.loadingSession;
        this.currentSession.getStats().updateToolbar(this.currentSession.getMode());
        EquilinoxGuis.getBottomBar().reset();
        this.currentSession.getSceneData().update();
        this.loadingSession = null;
    }

    private void setSession(Session session) {
        this.loading = true;
        GameManager.entityPicker.reset();
        this.loadingSession = session;
    }
}

