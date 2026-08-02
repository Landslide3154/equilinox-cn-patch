/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import audio.SoundMaestro;
import gameMenu.GameMenuBackground;
import gameMenu.GameMenuGui;
import gameMenu.MenuButtonGui;
import graphicsOptions.GraphicsOptions;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import languages.GameText;
import org.lwjgl.util.vector.Vector2f;
import userInterfaces.ClickListener;
import userInterfaces.GuiButton;
import userInterfaces.GuiClickEvent;
import userInterfaces.Listener;
import visualFxDrivers.ConstantDriver;

public class OptionsScreenGui
extends GuiComponent {
    private static final String ON_TEXT = GameText.getText(10);
    private static final String OFF_TEXT = GameText.getText(11);
    private static final String SHADOWS = GameText.getText(60);
    private static final String WATER = GameText.getText(61);
    private static final String ANTIALIASING = GameText.getText(62);
    private static final String DOF = GameText.getText(63);
    private static final String SUN_SHAFTS = GameText.getText(64);
    private static final String LENS_FLARE = GameText.getText(65);
    private static final String MUSIC = GameText.getText(66);
    private static final String SOUND = GameText.getText(67);
    private static final int ROW_COUNT = 4;
    private static final int COL_COUNT = 2;
    private static final float Y_PADDING = 0.1f;
    private static final float X_PADDING = 0.15f;
    private static final float BUTTON_WIDTH = 0.25f;
    private static final float BUTTON_HEIGHT = 0.09f;
    private static final float COL_SPACING = 0.45f;
    private static final float ROW_SPACING = 0.23666668f;
    private int currentId = 0;
    private GameMenuGui gameMenu;

    protected OptionsScreenGui(GameMenuGui menu) {
        this.gameMenu = menu;
    }

    @Override
    protected void init() {
        this.createBackOption();
        this.addButton(SHADOWS, GraphicsOptions.SHADOWS, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.toggleChange) {
                    GraphicsOptions.SHADOWS = event.eventState;
                }
            }
        });
        this.addButton(WATER, GraphicsOptions.HD_WATER, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.toggleChange) {
                    GraphicsOptions.HD_WATER = event.eventState;
                }
            }
        });
        this.addButton(ANTIALIASING, GraphicsOptions.ANTI_ALIASING, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.toggleChange) {
                    GraphicsOptions.ANTI_ALIASING = event.eventState;
                }
            }
        });
        this.addButton(DOF, GraphicsOptions.DOF_EFFECT, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.toggleChange) {
                    GraphicsOptions.DOF_EFFECT = event.eventState;
                }
            }
        });
        this.addButton(SUN_SHAFTS, GraphicsOptions.SUN_RAYS, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.toggleChange) {
                    GraphicsOptions.SUN_RAYS = event.eventState;
                }
            }
        });
        this.addButton(LENS_FLARE, GraphicsOptions.LENS_FLARE, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.toggleChange) {
                    GraphicsOptions.LENS_FLARE = event.eventState;
                }
            }
        });
        this.addButton(MUSIC, SoundMaestro.getMusicPlayer().getVolume() > 0.0f, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.toggleChange) {
                    SoundMaestro.getMusicPlayer().setVolume(event.eventState ? 1 : 0);
                }
            }
        });
        this.addButton(SOUND, SoundMaestro.SOUND_VOLUME > 0.0f, new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.toggleChange) {
                    SoundMaestro.SOUND_VOLUME = !event.eventState ? 1 : 0;
                }
            }
        });
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    @Override
    protected void updateSelf() {
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    private void addButton(final String name, boolean on, ClickListener listener) {
        Vector2f pos = this.getNextPosition();
        final MenuButtonGui button = new MenuButtonGui(String.valueOf(name) + ": " + (on ? ON_TEXT : OFF_TEXT), false, true, 1.7f, -0.1f);
        if (on) {
            button.setOn();
        }
        button.addListener(new ClickListener(){

            @Override
            public void eventOccurred(GuiClickEvent event) {
                if (event.toggleChange) {
                    button.setText(String.valueOf(name) + ": " + (event.eventState ? ON_TEXT : OFF_TEXT));
                }
            }
        });
        button.addListener(listener);
        super.addComponent(button, pos.x, pos.y, 0.25f, 0.09f);
    }

    private Vector2f getNextPosition() {
        int rowId = this.currentId % 4;
        int colId = this.currentId / 4;
        float x = 0.15f + (float)colId * 0.45f;
        float y = 0.1f + (float)rowId * 0.23666668f;
        ++this.currentId;
        return new Vector2f(x, y);
    }

    private void createBackOption() {
        GuiButton backButton = new GuiButton(GameMenuGui.BACK_ICON);
        backButton.getGuiTexture().setOverrideColour(GameMenuBackground.getStandardColour());
        backButton.getGuiTexture().setAlphaDriver(new ConstantDriver(0.65f));
        super.addComponentY(backButton, GameMenuGui.BACK_BUTTON_POS.x, GameMenuGui.BACK_BUTTON_POS.y, 0.12f);
        Listener listener = new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                OptionsScreenGui.this.gameMenu.closeSecondaryScreen();
            }
        };
        backButton.addListener(listener);
    }
}

