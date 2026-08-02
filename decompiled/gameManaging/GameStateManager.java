/*
 * Decompiled with CFR 0.152.
 */
package gameManaging;

import gameManaging.GameManager;
import gameManaging.GameState;
import guis.GuiMaster;
import main.Camera;

public class GameStateManager {
    private GameState currentGameState = null;
    private GameState nextGameState = GameState.SPLASH_SCREEN;
    private GameState suggestedGameState;

    public void setState(GameState state) {
        this.nextGameState = state;
    }

    public void endState(GameState state) {
        if (state == this.nextGameState) {
            this.nextGameState = null;
        }
    }

    public GameState getState() {
        return this.currentGameState;
    }

    public void suggestState(GameState state) {
        this.suggestedGameState = state;
    }

    protected void update() {
        if (this.nextGameState == null) {
            this.nextGameState = this.suggestedGameState;
        }
        this.suggestedGameState = null;
        if (this.nextGameState != this.currentGameState) {
            this.changeState(this.nextGameState);
        }
    }

    private void changeState(GameState state) {
        this.revertSettings();
        this.currentGameState = state;
        this.nextGameState = state;
        if (this.currentGameState != null) {
            this.currentGameState.init();
        } else {
            System.out.println("Normal mode");
        }
    }

    private void revertSettings() {
        Camera.getCamera().enable(true);
        GameManager.getEntityPicker().enable(true);
        GuiMaster.enableMouseInteraction(true);
    }
}

