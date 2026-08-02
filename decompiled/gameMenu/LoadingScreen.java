/*
 * Decompiled with CFR 0.152.
 */
package gameMenu;

import basics.DisplayManager;
import fontRendering.Text;
import gameManaging.GameManager;
import gameMenu.GameMenuBackground;
import gameMenu.LoadChecker;
import gameMenu.LoadingBar;
import guiRendering.GuiRenderData;
import guis.GuiComponent;
import languages.GameText;
import mainGuis.ColourPalette;
import mainGuis.UiSettings;
import org.lwjgl.util.vector.Vector2f;

public class LoadingScreen
extends GuiComponent {
    private static final float MIN_TIME = 2.5f;
    private static final float BAR_SCALE_X = 0.4f;
    private static final float BAR_Y = 0.3f;
    private static final float TEXT_Y = 0.4f;
    private LoadChecker checker;
    private float time = 0.0f;
    private GameMenuBackground gameMenu;
    private boolean closed = false;
    private LoadingBar loadingBar;
    private boolean displayingError = false;

    public LoadingScreen(LoadChecker checker, GameMenuBackground gameMenu) {
        this.checker = checker;
        this.loadingBar = new LoadingBar();
        this.loadingBar.setPreferredAspectRatio(25.0f);
        super.addComponentX(this.loadingBar, 0.3f, 0.3f, 0.4f);
        this.gameMenu = gameMenu;
        gameMenu.setLoading();
    }

    @Override
    protected void init() {
        super.init();
        GameManager.checkError();
    }

    @Override
    protected void updateSelf() {
        if (!this.displayingError && GameManager.checkError()) {
            this.addText();
        }
        if (this.closed) {
            return;
        }
        this.time += Math.min(0.3f, DisplayManager.getDeltaSeconds());
        this.loadingBar.setValue(Math.min(1.0f, this.time / 2.5f));
        boolean finished = this.checker.isLoaded();
        if (this.time >= 2.5f && finished) {
            this.end();
        }
    }

    private void addText() {
        Text text = Text.newText(GameText.getText(1006)).setFontSize(UiSettings.TITLE_FONT).center().create();
        text.setColour(ColourPalette.BRIGHT_RED);
        super.addText(text, 0.0f, 0.4f, 1.0f);
        this.displayingError = true;
    }

    @Override
    protected void getGuiTextures(GuiRenderData data) {
    }

    @Override
    protected void updateGuiTexturePositions(Vector2f position, Vector2f scale) {
    }

    private void end() {
        this.closed = true;
        this.gameMenu.display(false);
    }
}

