/*
 * Decompiled with CFR 0.152.
 */
package gameManaging;

import basics.DisplayManager;
import basics.EngineMaster;
import basics.MasterRenderer;
import breeding.EvolvingStatus;
import breedingTrees.BreedingTrees;
import errors.ErrorManager;
import gameManaging.AvailableSpeciesList;
import gameManaging.GameSpeed;
import gameManaging.GameState;
import gameManaging.GameStateManager;
import gameManaging.UserConfigs;
import java.io.IOException;
import main.Camera;
import mainGuis.EquilinoxGuis;
import picking.EntitySelectionManager;
import resourceManagement.BlueprintRepository;
import resourceManagement.ParticleAtlasCache;
import session.GameMode;
import session.Session;
import session.SessionManager;
import shopping.ShopManager;
import tasks.TaskManager;
import toolbox.MousePicker;
import toolbox.MyKeyboard;
import toolbox.MyMouse;
import world.World;

public class GameManager {
    public static final GameStateManager gameState = new GameStateManager();
    public static final SessionManager sessionManager = new SessionManager();
    public static EntitySelectionManager entityPicker;
    private static MousePicker terrainPicker;
    public static final BreedingTrees BREED_TREES;
    private static ShopManager shops;
    private static TaskManager taskManager;
    private static int ticker;
    private static float time;
    private static boolean error;
    private static EvolvingStatus evolvingStatus;
    private static AvailableSpeciesList unlockList;
    public static GameSpeed gameSpeed;
    private static float timeSpeed;

    static {
        BREED_TREES = new BreedingTrees();
        ticker = 0;
        time = 0.0f;
        error = false;
        evolvingStatus = new EvolvingStatus();
        unlockList = new AvailableSpeciesList();
        gameSpeed = new GameSpeed();
        timeSpeed = 1.0f;
    }

    public static void init() {
        terrainPicker = new MousePicker(Camera.getCamera(), false);
        EquilinoxGuis.init();
        ParticleAtlasCache.loadAll();
        entityPicker = new EntitySelectionManager();
        BlueprintRepository.loadAllBlueprints(false);
        BREED_TREES.initBreedingTrees(BlueprintRepository.getAllBlueprints());
        shops = new ShopManager();
        taskManager = new TaskManager();
        boolean successfulLoad = unlockList.load();
        if (!successfulLoad) {
            unlockList.generateNewList();
        }
        sessionManager.loadInitialSession();
    }

    public static void update() {
        MyMouse.getActiveMouse().update();
        MyKeyboard.getKeyboard().update();
        EngineMaster.preRenderUpdate();
        gameSpeed.update();
        timeSpeed = gameSpeed.getGameSpeed();
        terrainPicker.update();
        entityPicker.update();
        sessionManager.update();
        taskManager.update();
        shops.getPlacementManager().update();
        EquilinoxGuis.update();
        gameState.update();
        time += GameManager.getGameSeconds();
        ++ticker;
    }

    public static GameMode getGameMode() {
        Session currentSession = sessionManager.getSession();
        if (currentSession == null) {
            return sessionManager.getLoadingSession().getMode();
        }
        return currentSession.getMode();
    }

    public static boolean isNormalMode() {
        return GameManager.getGameMode() == GameMode.NORMAL;
    }

    public static GameSpeed getGameSpeed() {
        return gameSpeed;
    }

    public static EvolvingStatus getEvolvingStatus() {
        return evolvingStatus;
    }

    public static void registerError() {
        error = true;
    }

    public static boolean checkError() {
        boolean e = error;
        error = false;
        return e;
    }

    public static AvailableSpeciesList getUnlockList() {
        return unlockList;
    }

    public static int getTicker() {
        return ticker;
    }

    public static float getGameSeconds() {
        return DisplayManager.getDeltaSeconds() * timeSpeed;
    }

    public static float getDeltaHours() {
        return DisplayManager.getDeltaSeconds() * timeSpeed / 30.0f;
    }

    public static float getGameTime() {
        return time;
    }

    public static void render() {
        if (sessionManager.hasWorldReady()) {
            World world = GameManager.getWorld();
            MasterRenderer.render(world.getChunks(), GameManager.getSession().getSceneData().getDynamicBatch(), world.getWater(), true);
        }
        if (!MyKeyboard.getKeyboard().isKeyDown(35) || gameState.getState() == GameState.GAME_MENU || gameState.getState() == GameState.SPLASH_SCREEN) {
            MasterRenderer.renderGuis();
        }
        EngineMaster.update();
    }

    public static void cleanUp() {
        if (!sessionManager.isLoading()) {
            try {
                UserConfigs.saveConfigs();
            }
            catch (IOException e) {
                ErrorManager.createErrorLog("Failed Configs Save", e);
                e.printStackTrace();
            }
        }
        unlockList.save();
        EquilinoxGuis.cleanUp();
        entityPicker.cleanUp();
        if (!sessionManager.isLoading()) {
            sessionManager.saveCurrentSession();
        }
        EngineMaster.close();
    }

    public static World getWorld() {
        if (sessionManager.hasWorldReady()) {
            return sessionManager.getSession().getWorld();
        }
        return null;
    }

    public static ShopManager getShops() {
        return shops;
    }

    public static TaskManager getTaskManager() {
        return taskManager;
    }

    public static Session getSession() {
        return sessionManager.getSession();
    }

    public static MousePicker getTerrainPicker() {
        return terrainPicker;
    }

    public static GameState getGameState() {
        return gameState.getState();
    }

    public static EntitySelectionManager getEntityPicker() {
        return entityPicker;
    }
}

