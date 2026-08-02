/*
 * Decompiled with CFR 0.152.
 */
package gameManaging;

import gameManaging.GameManager;
import guis.GuiMaster;
import main.Camera;

public enum GameState {
    GRABBING{

        @Override
        public void init() {
            GuiMaster.enableMouseInteraction(false);
            System.out.println("Grabbing mode");
        }
    }
    ,
    PLACING{

        @Override
        public void init() {
            GameManager.getEntityPicker().enable(false);
            GameManager.getEntityPicker().clear();
            System.out.println("Placing mode");
        }
    }
    ,
    IN_GUI{

        @Override
        public void init() {
            Camera.getCamera().enable(false);
            GameManager.getEntityPicker().clearMouseOver();
            GameManager.getEntityPicker().enable(false);
            System.out.println("Gui mode");
        }
    }
    ,
    BIOME_PICKING{

        @Override
        public void init() {
            GameManager.getEntityPicker().enable(false);
            GameManager.getEntityPicker().clear();
            System.out.println("Biome mode");
        }
    }
    ,
    CAMERA{

        @Override
        public void init() {
            GameManager.getEntityPicker().mouseOff();
            GameManager.getEntityPicker().enable(false);
            GuiMaster.enableMouseInteraction(false);
            System.out.println("Camera mode");
        }
    }
    ,
    GAME_MENU{

        @Override
        public void init() {
            GameManager.getEntityPicker().enable(false);
            GameManager.getEntityPicker().clear();
            Camera.getCamera().enable(false);
            System.out.println("Game Menu mode");
        }
    }
    ,
    SPLASH_SCREEN{

        @Override
        public void init() {
            GameManager.getEntityPicker().enable(false);
            GameManager.getEntityPicker().clear();
            Camera.getCamera().enable(false);
            GuiMaster.enableMouseInteraction(false);
            System.out.println("Splash Screen mode");
        }
    }
    ,
    INTENSE_GUI{

        @Override
        public void init() {
            GameManager.getEntityPicker().enable(false);
            GameManager.getEntityPicker().clear();
            Camera.getCamera().enable(true);
            System.out.println("Intense GUI");
        }
    }
    ,
    CONTROL{

        @Override
        public void init() {
            GameManager.getEntityPicker().enable(false);
            System.out.println("Control Mode");
        }
    };


    private GameState() {
    }

    public abstract void init();

    /* synthetic */ GameState(String string, int n, GameState gameState) {
        this();
    }
}

