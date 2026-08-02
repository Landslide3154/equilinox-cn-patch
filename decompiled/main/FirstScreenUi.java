/*
 * Decompiled with CFR 0.152.
 */
package main;

import basics.DisplayManager;
import gameManaging.GameManager;
import gameManaging.GameState;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiMaster;
import guis.GuiTexture;
import main.EquilinoxMusic;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import userInterfaces.GuiImage;
import utils.MyFile;
import visualFxDrivers.ConstantDriver;
import visualFxDrivers.SlideDriver;

public class FirstScreenUi
extends GuiComponent {
    private static final Texture BACK_TEX = Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, "startScreen2.png")).noFiltering().clampEdges().create();
    private static final Texture PLAIN_TEX = Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, "plain.png")).noFiltering().clampEdges().create();
    private static final Texture LOGO_TEX = Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, "leafIcon512.png")).noFiltering().clampEdges().create();
    private static final Texture TM_LOGO = Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, "tmSplash.png")).noFiltering().clampEdges().create();
    private static final Texture DS_LOGO = Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, "dsSplash.png")).noFiltering().clampEdges().create();
    private static final Texture JG_LOGO = Texture.newTexture(new MyFile(GuiMaster.GUIS_LOC, "jgmSplash.png")).noFiltering().clampEdges().create();
    private static final float START = 0.4f;
    private static final float SLIDE_SPEED = 3.0f;
    private static final float START_HEIGHT = 0.2f;
    private static final float END_HEIGHT = 0.15f;
    private static final float SLIDE_TIME = 1.0f;
    private static final float FLASH_TIME = 0.8f;
    private static final float EXTRAS_TIME = 1.4f;
    private static final float READY_TIME = 2.5f;
    private boolean logoFadingIn = false;
    private boolean extrasFadingIn = false;
    private boolean waiting = false;
    private boolean fadeOut = false;
    private boolean endPhase = false;
    private GuiTexture background = new GuiTexture(BACK_TEX);
    private GuiImage logo = this.createImage(LOGO_TEX, 1.0f);
    private GuiImage tmLogo = this.createImage(TM_LOGO, 4.0f);
    private GuiImage jgLogo = this.createImage(JG_LOGO, 4.0f);
    private GuiImage dsLogo = this.createImage(DS_LOGO, 4.0f);
    private GuiImage whiteFlash = new GuiImage(PLAIN_TEX);
    private float time = 0.0f;
    private int bufferFrames = 0;

    public FirstScreenUi() {
        this.whiteFlash.getTexture().setOverrideColour(ColourPalette.WHITE);
        this.whiteFlash.getTexture().setAlphaDriver(new ConstantDriver(0.0f));
        super.setRenderLevel(1);
    }

    @Override
    protected void init() {
        super.init();
        super.addCenteredComponent(this.logo, 0.5f, 0.2f, 0.13f);
        super.addCenteredComponentX(this.tmLogo, 0.2f, 0.75f, 0.1f);
        super.addCenteredComponentX(this.jgLogo, 0.5f, 0.75f, 0.1f);
        super.addCenteredComponentX(this.dsLogo, 0.8f, 0.75f, 0.1f);
        super.addComponent(this.whiteFlash, 0.0f, 0.0f, 1.0f, 1.0f);
    }

    public boolean isReady() {
        return this.waiting;
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void updateSelf() {
        this.background.update();
        this.time += DisplayManager.getDeltaSeconds();
        if (!this.logoFadingIn && this.time > 0.4f) {
            this.startAnimation();
        }
        if (this.logoFadingIn) {
            this.slideLogo();
        }
        if (!this.extrasFadingIn && this.time > 1.4f) {
            this.startExtrasFading();
        }
        if (!this.waiting && this.time > 2.5f) {
            this.waiting = true;
        }
        if (this.fadeOut) {
            this.doFlash();
        } else if (this.waiting) {
            this.checkFinishedLoading();
        }
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        if (!this.endPhase) {
            data.addTexture(this.getLevel(), this.background);
        }
    }

    private void startAnimation() {
        this.logo.getTexture().setAlphaDriver(new SlideDriver(0.0f, 1.0f, 1.0f));
        this.logoFadingIn = true;
    }

    private void slideLogo() {
        float difference = 0.15f - this.logo.getRelativeY();
        float change = difference * DisplayManager.getDeltaSeconds() * 3.0f;
        this.logo.setRelativeY(this.logo.getRelativeY() + change);
    }

    private void startExtrasFading() {
        this.extrasFadingIn = true;
        this.tmLogo.getTexture().setAlphaDriver(new SlideDriver(0.0f, 1.0f, 1.0f));
        this.jgLogo.getTexture().setAlphaDriver(new SlideDriver(0.0f, 1.0f, 1.0f));
        this.dsLogo.getTexture().setAlphaDriver(new SlideDriver(0.0f, 1.0f, 1.0f));
    }

    private GuiImage createImage(Texture texture, float aspect) {
        GuiImage image = new GuiImage(texture);
        image.setPreferredAspectRatio(aspect);
        image.getTexture().setAlphaDriver(new ConstantDriver(0.0f));
        return image;
    }

    private void checkFinishedLoading() {
        boolean loaded;
        boolean bl = loaded = GameManager.sessionManager.hasWorldReady() && EquilinoxMusic.isLoaded() && !GameManager.sessionManager.isLoading();
        if (loaded) {
            ++this.bufferFrames;
            if (this.bufferFrames == 5) {
                this.fadeOut = true;
                this.whiteFlash.getTexture().setAlphaDriver(new SlideDriver(0.0f, 1.0f, 0.8f));
            }
        }
    }

    private void doFlash() {
        if (this.endPhase) {
            if (this.whiteFlash.getTexture().getAlpha() <= 0.0f) {
                this.remove();
            }
        } else if (this.whiteFlash.getTexture().getAlpha() >= 1.0f) {
            this.initMenu();
        }
    }

    private void initMenu() {
        this.whiteFlash.getTexture().setAlphaDriver(new SlideDriver(1.0f, 0.0f, 0.8f));
        this.tmLogo.remove();
        this.jgLogo.remove();
        this.logo.remove();
        this.dsLogo.remove();
        GameManager.gameState.setState(GameState.GAME_MENU);
        EquilinoxMusic.startPlayingPlaylist();
        this.endPhase = true;
    }
}

