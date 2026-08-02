/*
 * Decompiled with CFR 0.152.
 */
package session;

import basics.DisplayManager;
import components.Mutator;
import dataManagement.DataUpdateManager;
import entityInfoGui.TabController;
import environment.EnvironmentVariables;
import errors.ErrorManager;
import gameManaging.GameManager;
import gameManaging.GameState;
import guis.GuiMaster;
import inventory.Inventory;
import main.Camera;
import main.EquilinoxMusic;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import particles.ParticleMaster;
import resourceProcessing.RequestProcessor;
import resourceProcessing.ResourceRequest;
import saves.SaveSlot;
import session.AutoSaveUi;
import session.EntityLoad;
import session.GameMode;
import sessionStats.Stats;
import utils.BinaryReader;
import utils.BinaryWriter;
import world.World;
import world.WorldConfigs;

public class Session {
    private static final int TEXT_HEIGHT = 30;
    private static final int MIN_TIME_BETWEEN_SAVES = 300;
    private static final int MAX_TIME_BETWEEN_SAVES = 480;
    private World world;
    private Stats stats;
    private SaveSlot saveFile;
    private Inventory inventory;
    private DataUpdateManager sceneData;
    private GameMode gameMode;
    private float lastTime = 0.0f;
    private boolean save = false;
    private boolean save2 = false;
    private float timeSinceSave = 0.0f;

    public DataUpdateManager getSceneData() {
        return this.sceneData;
    }

    public World getWorld() {
        return this.world;
    }

    public Stats getStats() {
        return this.stats;
    }

    public GameMode getMode() {
        return this.gameMode;
    }

    public SaveSlot getSave() {
        return this.saveFile;
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public void delete() {
        this.world.delete();
        this.sceneData.delete();
    }

    public boolean isLoaded() {
        boolean loaded = this.stats != null && this.stats.isLoaded();
        loaded &= this.sceneData != null && this.sceneData.isLoaded();
        loaded &= this.world != null && this.world.isLoaded();
        return loaded &= this.inventory != null && this.inventory.isLoaded();
    }

    public void update() {
        Mutator.update();
        this.world.update();
        this.world.updateAmbientSounds();
        this.stats.update();
        this.sceneData.update();
        GameManager.getEvolvingStatus().update();
        this.updateAutoSave();
    }

    private void updateAutoSave() {
        this.timeSinceSave += DisplayManager.getDeltaSeconds();
        if (this.save2) {
            boolean successfulSave = this.save();
            if (!successfulSave) {
                EquilinoxGuis.notify("SAVING ERROR!", "The autosave failed. Try saving manually, but if the problem continues contact thinmatrix@gmail.com", GuiRepository.INFO, GuiSounds.NEGATIVE);
            }
            this.save = false;
            this.save2 = false;
        } else if (this.save) {
            this.save2 = true;
        } else if (GameManager.getGameState() != GameState.GAME_MENU) {
            float time = GameManager.getSession().getStats().getCalendar().getRawTime();
            if (this.timeSinceSave > 480.0f || this.timeSinceSave > 300.0f && time < this.lastTime && time < 0.02f) {
                this.save = true;
                float height = 1.0f - 50.0f / (float)DisplayManager.getUiHeight();
                GuiMaster.addComponent(new AutoSaveUi(), 0.0f, height, 1.0f, 1.0f);
            }
            this.lastTime = time;
        }
    }

    public boolean save() {
        if (this.gameMode == GameMode.NORMAL) {
            GameManager.getUnlockList().addSpecies(this.stats.getLockStatus());
        }
        try {
            BinaryWriter writer = this.saveFile.getWriter(this);
            Camera.getCamera().saveState(writer);
            writer.writeInt(this.gameMode.ordinal());
            this.stats.export(writer);
            Mutator.export(writer);
            GameManager.getEvolvingStatus().save(writer);
            this.world.export(writer);
            this.inventory.export(writer);
            GameManager.getTaskManager().exportState(writer);
            writer.close();
            this.saveFile.finishSaving();
            this.timeSinceSave = 0.0f;
            return true;
        }
        catch (Exception e) {
            System.err.println("Failed to save!!");
            ErrorManager.createErrorLog("Failed To Save", e);
            e.printStackTrace();
            return false;
        }
    }

    public static Session createNewSession(SaveSlot newSave, final WorldConfigs configs, final GameMode mode) {
        final Session session = new Session(newSave);
        RequestProcessor.sendRequest(new ResourceRequest(){

            @Override
            public void doResourceRequest() {
                Session.initializeSession(session, configs, mode);
            }
        });
        return session;
    }

    public static Session loadSession(final SaveSlot save) {
        final Session session = new Session(save);
        RequestProcessor.sendRequest(new ResourceRequest(){

            @Override
            public void doResourceRequest() {
                try {
                    Session.loadSession(session);
                }
                catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Couldn't load save file " + save.getName());
                    save.setCorrupt();
                    GameManager.registerError();
                    GameManager.sessionManager.loadInitialSession();
                }
            }
        });
        return session;
    }

    private static void loadSession(Session session) throws Exception {
        BinaryReader reader = session.saveFile.getReader();
        reader.setSession(session);
        Camera.getCamera().loadState(reader);
        GameManager.getShops().reset();
        EquilinoxMusic.resetMusic();
        EquilinoxGuis.getToolBar().stopButtonWobbles();
        session.gameMode = reader.getVersion() < 6 ? GameMode.NORMAL : GameMode.values()[reader.readInt()];
        session.stats = Stats.loadStats(reader, session);
        Mutator.load(reader);
        if (session.gameMode == GameMode.NORMAL) {
            GameManager.getShops().unlockNecessaryItems(session.stats.getLockStatus().getUnlockedSpecies());
        }
        GameManager.getEvolvingStatus().load(reader);
        EntityLoad entityLoad = EntityLoad.loadEntities(reader);
        session.world = World.loadWorld(reader, entityLoad);
        session.sceneData = new DataUpdateManager(entityLoad.getStaticBatches(), entityLoad.getDynamicBatch());
        session.world.createClouds(session.sceneData);
        session.inventory = Inventory.loadInventory(reader);
        GameManager.getTaskManager().loadState(reader, session);
        TabController.reset();
        reader.close();
        GameManager.getGameSpeed().normalSpeed();
        if (session.gameMode == GameMode.SIM) {
            if (GameManager.getUnlockList().failedToLoad()) {
                GameManager.getShops().unlockAll();
            } else {
                GameManager.getShops().unlockItems(GameManager.getUnlockList().getAvailableSpecies());
            }
        } else if (session.gameMode == GameMode.BUILD) {
            GameManager.getShops().unlockAll();
        }
        EquilinoxGuis.getNotificationLog().clear();
    }

    private static void initializeSession(Session session, WorldConfigs configs, GameMode mode) {
        GameManager.getShops().reset();
        GameManager.getEvolvingStatus().reset();
        EquilinoxMusic.resetMusic();
        EquilinoxGuis.getToolBar().stopButtonWobbles();
        Camera.getCamera().resetPosition();
        session.gameMode = mode;
        session.stats = Stats.createNewStats(session);
        Mutator.reset();
        session.world = World.generateWorld(configs);
        session.sceneData = new DataUpdateManager(50);
        session.world.createClouds(session.sceneData);
        session.inventory = Inventory.newInventory();
        if (mode == GameMode.NORMAL) {
            GameManager.getShops().updateLockStatus(session.stats.getLockStatus());
        }
        GameManager.getTaskManager().reset(session);
        GameManager.getGameSpeed().normalSpeed();
        if (mode == GameMode.SIM) {
            if (GameManager.getUnlockList().failedToLoad()) {
                GameManager.getShops().unlockAll();
            } else {
                GameManager.getShops().unlockItems(GameManager.getUnlockList().getAvailableSpecies());
            }
        } else if (mode == GameMode.BUILD) {
            GameManager.getShops().unlockAll();
        }
        EquilinoxGuis.getNotificationLog().clear();
    }

    private Session(SaveSlot save) {
        this.saveFile = save;
        ParticleMaster.reset();
        EnvironmentVariables.cycle.addSun();
    }
}

