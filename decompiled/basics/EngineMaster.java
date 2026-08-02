/*
 * Decompiled with CFR 0.152.
 */
package basics;

import audio.SoundMaestro;
import basics.CameraInterface;
import basics.DisplayManager;
import basics.Loader;
import basics.MasterRenderer;
import environment.EnvironmentVariables;
import frustumCulling.FrustumCuller;
import gameManaging.UserConfigs;
import glRequestProcessing.GlRequestProcessor;
import guis.GuiMaster;
import languages.GameText;
import main.EquilinoxMusic;
import particles.ParticleMaster;
import resourceProcessing.RequestProcessor;
import shadows.ShadowBox;
import textures.TextureManager;

public class EngineMaster {
    private static CameraInterface sceneCamera;
    private static FrustumCuller culler;

    public static void init(CameraInterface camera) {
        sceneCamera = camera;
        SoundMaestro.init(camera);
        EquilinoxMusic.loadMusic();
        UserConfigs.loadConfigs();
        GameText.init(UserConfigs.getLanguage().ordinal());
        DisplayManager.createDisplay(true);
        culler = new FrustumCuller(camera);
        MasterRenderer.init(camera);
        ParticleMaster.init(MasterRenderer.getProjectionMatrix());
    }

    public static ShadowBox getShadowBox() {
        return MasterRenderer.getShadowBox();
    }

    public static FrustumCuller getFrustumCuller() {
        return culler;
    }

    public static CameraInterface getCamera() {
        return sceneCamera;
    }

    public static void preRenderUpdate() {
        sceneCamera.moveCamera();
        culler.update();
        MasterRenderer.updateShadowBox();
        EnvironmentVariables.getVariables().update();
        GuiMaster.updateGuis();
        SoundMaestro.update(DisplayManager.getDeltaSeconds());
        ParticleMaster.update(sceneCamera);
    }

    public static void update() {
        DisplayManager.updateDisplay();
        GlRequestProcessor.dealWithTopRequests();
    }

    public static void close() {
        ParticleMaster.cleanUp();
        RequestProcessor.cleanUp();
        GlRequestProcessor.completeAllRequests();
        SoundMaestro.cleanUp();
        TextureManager.cleanUp();
        Loader.cleanUpModelMemory();
        MasterRenderer.cleanUp();
        GlRequestProcessor.completeAllRequests();
        DisplayManager.closeDisplay();
    }
}

