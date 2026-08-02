/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import basics.DisplayManager;
import gameManaging.GameManager;
import gameManaging.GameState;
import gameMenu.SplashScreenLogo;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import guis.GuiTexture;
import main.EquilinoxMusic;
import mainGuis.GuiRepository;
import org.lwjgl.util.vector.Vector2f;
import textures.Texture;
import userInterfaces.GuiImage;
import visualFxDrivers.SlideDriver;

public class SplashScreen
extends GuiComponent {
    private static final float SHOW_TIME = 0.0f;
    private static final float FADE_TIME = 1.2f;
    private static final float FADE_OUT_TIME = 1.5f;
    private static final int FRAME_PAD = 30;
    private static final float ICON_WIDTH = 0.45f;
    private static final float ICON_Y_START = 0.2f;
    private static final float SIDE_PADDING = 0.5f;
    private final GuiTexture background;
    private final Texture[] logoTextures = new Texture[]{GuiRepository.TM, GuiRepository.JGM, GuiRepository.DS};
    private boolean finished = false;
    private boolean fadeOut = false;
    private float shownTime = 0.0f;
    private SplashScreenLogo[] images;
    private int currentImage = 0;
    private float fadeTime = 0.0f;
    private boolean fadingIn = true;
    private int frameCount = 0;

    public SplashScreen() {
        this.background = new GuiTexture(GuiRepository.BLACKOUT);
        this.initImages();
    }

    @Override
    protected void updateSelf() {
        if (!this.checkStarting()) {
            return;
        }
        this.background.update();
        if (this.fadingIn) {
            this.updateFadingInIcons();
        }
        if (!this.fadingIn && !this.finished) {
            this.checkWhetherFinishedShowing();
        }
        if (this.finished && !this.fadeOut && this.images[0].getTexture().getAlpha() <= 0.0f) {
            this.startFadingBackground();
        }
        if (this.fadeOut && this.background.getAlpha() <= 0.0f) {
            this.remove();
        }
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
        this.background.setPosition(position.x, position.y, scale.x, scale.y);
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
        data.addTexture(this.getLevel(), this.background);
    }

    private boolean checkStarting() {
        if (this.frameCount < 30) {
            ++this.frameCount;
            if (this.frameCount == 30) {
                this.fadeInImage(this.images[0]);
            } else {
                return false;
            }
        }
        return true;
    }

    private void fadeInImage(GuiImage image) {
        image.show(true);
        image.getTexture().setAlphaDriver(new SlideDriver(0.0f, 1.0f, 1.2f));
    }

    private void fadeOutIcons() {
        SplashScreenLogo[] splashScreenLogoArray = this.images;
        int n = this.images.length;
        int n2 = 0;
        while (n2 < n) {
            SplashScreenLogo image = splashScreenLogoArray[n2];
            image.getTexture().setAlphaDriver(new SlideDriver(1.0f, 0.0f, 1.2f));
            ++n2;
        }
    }

    private void updateFadingInIcons() {
        this.fadeTime += DisplayManager.getDeltaSeconds();
        if (this.fadeTime >= 1.2f) {
            this.fadeInNextImage();
        }
    }

    private void fadeInNextImage() {
        this.fadeTime = 0.0f;
        ++this.currentImage;
        if (this.currentImage < this.images.length) {
            this.fadeInImage(this.images[this.currentImage]);
        } else {
            this.fadingIn = false;
        }
    }

    private void startFadingBackground() {
        this.fadeOut = true;
        this.background.setAlphaDriver(new SlideDriver(this.background.getAlpha(), 0.0f, 1.5f));
        GameManager.gameState.setState(GameState.GAME_MENU);
        EquilinoxMusic.startPlayingPlaylist();
    }

    private void checkWhetherFinishedShowing() {
        this.shownTime += DisplayManager.getDeltaSeconds();
        boolean bl = this.finished = this.shownTime >= 0.0f && GameManager.sessionManager.hasWorldReady() && EquilinoxMusic.isLoaded();
        if (this.finished) {
            this.fadeOutIcons();
        }
    }

    private void initImages() {
        this.images = new SplashScreenLogo[this.logoTextures.length];
        int i = 0;
        while (i < this.logoTextures.length) {
            this.images[i] = new SplashScreenLogo(this.logoTextures[i], i % 2 == 0);
            this.images[i].setPreferredAspectRatio(4.0f);
            super.addCenteredComponent(this.images[i], this.getCenterX(i), 0.2f + (float)i * 0.3f, 0.45f);
            this.images[i].show(false);
            ++i;
        }
    }

    private float getCenterX(int index) {
        if (index % 2 == 0) {
            return 0.5f;
        }
        return 0.5f;
    }
}

