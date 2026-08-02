/*
 * Decompiled with CFR 0.152.
 */
package main;

import basics.DisplayManager;
import basics.EngineMaster;
import basics.MasterRenderer;
import errors.ErrorManager;
import gameManaging.GameManager;
import glRequestProcessing.GlRequestProcessor;
import guis.GuiMaster;
import iconGenerator.IconRenderer;
import java.io.File;
import java.util.List;
import main.Camera;
import main.FirstScreenUi;
import main.TemporaryPlacement;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import shops.ShopItem;
import toolbox.MyKeyboard;
import toolbox.OpenGlError;

public class MainApp {
    public static final boolean CHEATS = false;
    private static final String VER = "0";
    public static final String VERSION = "Equilinox_0";
    public static final String VERSION_STRING = "Version 1.7.2";
    public static IconRenderer i;

    public static void main(String[] args) {
        ErrorManager.init(new File("EquilinoxErrorLogs"), "An error has caused the program to crash, sorry for the inconvenience! Please email the dev at thinmatrix@gmail.com and copy-paste the error message below:");
        EngineMaster.init(Camera.getCamera());
        FirstScreenUi screen = new FirstScreenUi();
        GuiMaster.addComponent(screen, 0.0f, 0.0f, 1.0f, 1.0f);
        MyKeyboard.getKeyboard().block(true);
        while (!screen.isReady()) {
            GuiMaster.updateGuis();
            MasterRenderer.renderGuis();
            EngineMaster.update();
        }
        GameManager.init();
        TemporaryPlacement.doTemporaryLoadingOfResources();
        GlRequestProcessor.completeAllRequests();
        OpenGlError.check("Requests");
        i = new IconRenderer(142, 142);
        i.generateBlueprintIcons();
        MyKeyboard.getKeyboard().update();
        MyKeyboard.getKeyboard().block(false);
        while (DisplayManager.isOpen()) {
            MainApp.checkCheats();
            GameManager.update();
            GameManager.render();
            OpenGlError.check("Render");
        }
        i.cleanUp();
        GameManager.cleanUp();
    }

    private static void checkCheats() {
        if (MyKeyboard.getKeyboard().isKeyDown(42)) {
            MyKeyboard.getKeyboard().keyDownEventOccurred(50);
        }
        if (MyKeyboard.getKeyboard().isKeyDown(42)) {
            MyKeyboard.getKeyboard().keyDownEventOccurred(22);
        }
    }

    private static void cheat() {
        List<ShopItem> items = GameManager.getShops().getPlantShop().getShopStock();
        for (ShopItem item : items) {
            item.unlock();
        }
        List<ShopItem> animals = GameManager.getShops().getAnimalShop().getShopStock();
        for (ShopItem item : animals) {
            item.unlock();
        }
        EquilinoxGuis.notify("Cheat!", "All species are now unlocked.", GuiRepository.UNLOCKED, null);
    }
}

