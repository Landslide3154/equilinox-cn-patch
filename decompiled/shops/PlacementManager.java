/*
 * Decompiled with CFR 0.152.
 */
package shops;

import audio.SoundMaestro;
import basics.DisplayManager;
import blueprints.Blueprint;
import breedingTraits.FloatTrait;
import classification.Classifier;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import environmentWarning.EnviroTipUi;
import environmentWarning.EnvironmentProbe;
import gameManaging.GameManager;
import gameManaging.GameState;
import guis.GuiMaster;
import health.LifeCompBlueprint;
import instances.Entity;
import main.MainApp;
import mainGuis.ColourPalette;
import mainGuis.EquilinoxGuis;
import mainGuis.EscListener;
import mainGuis.GuiSounds;
import materials.MaterialComponent;
import org.lwjgl.util.vector.Vector3f;
import particleSpawns.PointSpawn;
import particles.ParticleSystem;
import particles.ParticleTexture;
import placementUi.PlacementUi;
import resourceManagement.ParticleAtlasCache;
import session.GameMode;
import textures.Texture;
import toolbox.Maths;
import toolbox.MyKeyboard;
import toolbox.MyMouse;
import toolbox.Transformation;
import userInterfaces.TabButtonUi;
import world.UnplaceableReason;
import world.World;

public class PlacementManager {
    private static final float MOVE_THRESHOLD = 0.25f;
    private static final float BABY_PITCH = 1.2f;
    private static final ParticleSystem system = PlacementManager.createRockParticleSystem();
    private static final ParticleSystem dustSystem = PlacementManager.createDustParticleSystem();
    private static final ParticleSystem bigDustSystem = PlacementManager.createBigDustParticleSystem();
    private Blueprint selectedItem;
    private Entity entityToDupe;
    private MyMouse mouse = MyMouse.getActiveMouse();
    private PlacementUi heldGui;
    private EnviroTipUi enviroUi;
    private TabButtonUi shopButton;
    private boolean first = false;
    private float rightButtonTime = 0.0f;
    private float middleButtonTime = 0.0f;

    public PlacementManager() {
        EquilinoxGuis.addEscListener(new EscListener(){

            @Override
            public boolean escPressed() {
                return PlacementManager.this.clear();
            }
        });
    }

    public void selectItem(Blueprint item, TabButtonUi shopButton, boolean dupe) {
        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)item.getComponent(ComponentType.INFO);
        this.selectItem(item, shopButton, info.getIcon(), dupe);
    }

    public void selectItem(Blueprint item, TabButtonUi shopButton, Texture icon, boolean duplicate) {
        this.clear();
        this.selectedItem = item;
        this.shopButton = shopButton;
        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)this.selectedItem.getComponent(ComponentType.INFO);
        this.heldGui = new PlacementUi(icon, info.getPrice(), duplicate);
        LifeCompBlueprint life = (LifeCompBlueprint)this.selectedItem.getComponent(ComponentType.LIFE);
        this.enviroUi = new EnviroTipUi(life == null || GameManager.getGameMode() == GameMode.BUILD ? null : new EnvironmentProbe(life.enviroBlueprint, info.getRange()));
        EquilinoxGuis.getToolBar().closeGuestPanel();
        this.first = true;
        GameManager.gameState.setState(GameState.PLACING);
        SoundMaestro.playSystemSound(GuiSounds.SELECT);
    }

    public void duplicateEntity(Entity entity) {
        this.entityToDupe = entity;
        MaterialComponent material = (MaterialComponent)entity.getComponent(ComponentType.MATERIAL);
        if (material != null) {
            Texture icon = MainApp.i.getColourIcon(entity.getBlueprint(), material.getMaterial());
            this.selectItem(entity.getBlueprint(), null, icon, true);
        } else {
            this.selectItem(entity.getBlueprint(), null, true);
        }
    }

    public boolean clear() {
        if (this.selectedItem != null) {
            this.heldGui.remove();
            this.heldGui = null;
            this.enviroUi.remove();
            this.enviroUi = null;
            this.selectedItem = null;
            this.entityToDupe = null;
            this.shopButton = null;
            GameManager.gameState.endState(GameState.PLACING);
            return true;
        }
        return false;
    }

    public void update() {
        if (this.selectedItem == null) {
            return;
        }
        this.checkPlacement();
        this.checkClear();
    }

    private void checkPlacement() {
        Vector3f terrainPoint = GameManager.getTerrainPicker().getCurrentTerrainPoint();
        UnplaceableReason reason = null;
        if (!GuiMaster.isMouseInGui() && terrainPoint != null && (reason = this.canPlace(this.selectedItem, terrainPoint)) == UnplaceableReason.NO_PROBLEM) {
            this.enviroUi.updateTerrainPoint(terrainPoint);
            this.enviroUi.show(true);
            this.enviroUi.showEnviro();
            this.heldGui.indicatePlaceable(true);
            if (this.mouse.isLeftClick()) {
                InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)this.selectedItem.getComponent(ComponentType.INFO);
                if (GameManager.getGameMode() != GameMode.NORMAL || GameManager.getSession().getStats().getDpCount() >= info.getPrice()) {
                    this.placeEntity(terrainPoint, info);
                } else {
                    SoundMaestro.playSystemSound(GuiSounds.NEGATIVE);
                }
            }
        } else {
            if (!GuiMaster.isMouseInGui() && terrainPoint != null) {
                this.enviroUi.updateTerrainPoint(terrainPoint);
                this.enviroUi.show(true);
                this.enviroUi.setOneLineText(reason.toString(), ColourPalette.BEIGE);
            } else {
                this.enviroUi.show(false);
            }
            this.heldGui.indicatePlaceable(false);
            if (this.mouse.isLeftClick() && !this.first) {
                SoundMaestro.playSystemSound(GuiSounds.NEGATIVE);
            }
        }
    }

    private void placeEntity(Vector3f terrainPoint, InformationComponent.InformationCompBlueprint info) {
        if (GameManager.getGameMode() != GameMode.NORMAL) {
            if (MyKeyboard.getKeyboard().isKeyDown(50)) {
                this.doMultiplePlacement(terrainPoint, info);
            } else if (MyKeyboard.getKeyboard().isKeyDown(49) && this.selectedItem.isAnimal()) {
                int i = 0;
                while (i < 50) {
                    this.addEntity(terrainPoint);
                    ++i;
                }
            }
        }
        this.addEntity(terrainPoint);
        boolean animal = this.selectedItem.isAnimal();
        if (animal) {
            SoundMaestro.playSystemSound(info.getPlacementSound(), 1.2f);
        } else {
            if (this.selectedItem.getSpeciesClassification().isTypeOf(Classifier.getClassification("erl"))) {
                bigDustSystem.pulseParticles(terrainPoint, 1.0f);
            } else {
                system.pulseParticles(terrainPoint, 1.0f);
                dustSystem.pulseParticles(terrainPoint, 1.0f);
            }
            SoundMaestro.playSystemSound(info.getPlacementSound());
        }
        GameManager.getSession().getStats().increaseDp(-info.getPrice());
        this.heldGui.pulse();
    }

    private void doMultiplePlacement(Vector3f terrainPoint, InformationComponent.InformationCompBlueprint info) {
        int range = info.getRange();
        LifeCompBlueprint life = (LifeCompBlueprint)this.selectedItem.getComponent(ComponentType.LIFE);
        int pop = 5;
        if (life != null) {
            pop = (int)Math.floor(life.averagePopulation);
        }
        int i = 0;
        while (i < pop) {
            Vector3f newPoint = Maths.randomPointInSquare(terrainPoint.x, terrainPoint.z, range);
            newPoint.y = GameManager.getWorld().getHeightOfTerrain(newPoint.x, newPoint.z);
            if (this.canPlace(this.selectedItem, newPoint) == UnplaceableReason.NO_PROBLEM) {
                this.addEntity(newPoint);
            }
            ++i;
        }
    }

    private void addEntity(Vector3f terrainPoint) {
        if (this.entityToDupe == null) {
            Transformation.TransformBlueprint transform = (Transformation.TransformBlueprint)this.selectedItem.getComponent(ComponentType.TRANSFORM);
            Transformation.TransformParams params = new Transformation.TransformParams(new Vector3f(terrainPoint), Maths.RANDOM.nextFloat() * 360.0f, transform.generateRandomScale());
            Entity entity = this.selectedItem.createInstance(params);
            GameManager.getSession().getWorld().addInstance(entity, true);
        } else {
            Transformation transform = this.entityToDupe.getTransform();
            Transformation.TransformParams params = new Transformation.TransformParams(new Vector3f(terrainPoint), Maths.RANDOM.nextFloat() * 360.0f, (FloatTrait)transform.getScaleTrait().duplicate());
            Entity newEntity = this.entityToDupe.duplicate(params);
            GameManager.getSession().getWorld().addInstance(newEntity, true);
        }
    }

    private static ParticleSystem createRockParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(9);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 12.0f, 1.0f, 0.35f, 0.5f, 0.035f);
        system.setDirection(Maths.UP, 0.2f);
        system.setScaleError(0.5f);
        system.setSpeedError(0.3f);
        system.setLifeError(0.3f);
        system.randomizeRotation();
        return system;
    }

    private static ParticleSystem createDustParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(14);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 1.0f, 0.18f, 0.01f, 1.0f, 0.6f);
        system.setDirection(Maths.UP, 0.25f);
        system.setLifeError(0.3f);
        system.randomizeRotation();
        return system;
    }

    private static ParticleSystem createBigDustParticleSystem() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(14);
        ParticleSystem system = new ParticleSystem(atlas, new PointSpawn(), 4.0f, 0.5f, 0.015f, 1.0f, 0.95f);
        system.setDirection(Maths.UP, 0.3f);
        system.setLifeError(0.3f);
        system.setScaleError(0.15f);
        system.randomizeRotation();
        return system;
    }

    private UnplaceableReason canPlace(Blueprint blueprint, Vector3f pos) {
        World world = GameManager.getWorld();
        return world.isAcceptableLocation(blueprint, pos.x, pos.z);
    }

    private void checkClear() {
        if (this.mouse.isRightClickRelease() && this.rightButtonTime < 0.25f) {
            if (this.shopButton != null) {
                this.shopButton.toggle();
            }
            this.clear();
        } else if (!this.first && GameManager.getGameState() != GameState.PLACING || MyKeyboard.getKeyboard().keyDownEventOccurred(28) || MyKeyboard.getKeyboard().keyDownEventOccurred(211) || this.mouse.isMiddleClickRelease() && this.middleButtonTime < 0.25f) {
            this.clear();
        }
        this.rightButtonTime = this.mouse.isRightButtonDown() ? (this.rightButtonTime += DisplayManager.getDeltaSeconds() + (float)(Math.abs(this.mouse.getDX()) + Math.abs(this.mouse.getDY())) * 0.001f) : 0.0f;
        this.middleButtonTime = this.mouse.isMouseWheelDown() ? (this.middleButtonTime += DisplayManager.getDeltaSeconds() + (float)(Math.abs(this.mouse.getDX()) + Math.abs(this.mouse.getDY())) * 0.005f) : 0.0f;
        this.first = false;
    }
}

