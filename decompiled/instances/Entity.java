/*
 * Decompiled with CFR 0.152.
 */
package instances;

import aiComponent.AiComponent;
import audio.Sound;
import audio.SoundMaestro;
import blueprints.Blueprint;
import blueprints.SubBlueprint;
import breedingTraits.Trait;
import classification.Classifier;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import componentArchitecture.ControlBehaviour;
import components.InformationComponent;
import components.MeshComponent;
import controllerUi.ControlUi;
import death.DeathAi;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.InfoType;
import entityInfoGui.PopUpInfoGui;
import entityInfoGui.TextInfo;
import gameManaging.GameManager;
import gameManaging.GameState;
import iconSystem.StatusIconDisplay;
import instances.DpPerMinCounter;
import instances.EntityAlerts;
import instances.Reproducer;
import instances.Tinter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import languages.GameText;
import main.Camera;
import mainGuis.ColourPalette;
import mainGuis.EquilinoxGuis;
import objectPools.Vec3Pool;
import org.lwjgl.util.vector.Vector3f;
import picking.Box;
import picking.EntityBox;
import picking.ExtraBoundingBoxHandler;
import picking.GrabbedMechanic;
import resourceManagement.ParticleAtlasCache;
import resourceManagement.SoundCache;
import session.GameMode;
import text3D.Text3D;
import toolbox.Maths;
import toolbox.Transformation;
import userInterfaces.TextStatInfo;
import utils.BinaryWriter;
import world.AddableEntity;
import world.GridSection;
import world.World;

public class Entity
implements AddableEntity {
    private static final String REMOVE_MANY = GameText.getText(1113);
    public static final Sound THUD = SoundCache.CACHE.requestSound("thud", true).withVolume(0.4f);
    public static final Sound SPLASH = SoundCache.CACHE.requestSound("splash", true).withVolume(0.4f);
    private static final String TRANSPLANT = GameText.getText(926);
    private static final String REMOVE = GameText.getText(927);
    private static final String FOLLOW = GameText.getText(928);
    private static final String DP_TEXT = GameText.getText(929);
    private static final String VIEW = GameText.getText(986);
    private static final float TRANSPLANT_COST_FACTOR = 2.5f;
    private static final float VIEW_DISTANCE = 240.0f;
    private static final float TRANS_FACTOR = 0.1f;
    private static final float TRANSITION_PERIOD = 24.0f;
    private static final float FULL_VIS_RANGE = 216.0f;
    private static final float CLUTTER_SIZE = 0.3f;
    private int id;
    public final StatusIconDisplay iconDisplay;
    public final Reproducer reproducer;
    private Blueprint blueprint;
    private Transformation transform;
    private MeshComponent mesh;
    private boolean isStatic;
    private ExtraBoundingBoxHandler extraBoundingBoxes;
    private EntityBox boundingBox;
    private boolean dead = false;
    private boolean placed = false;
    private boolean currentlyInStaticBatch;
    private boolean noShadow = false;
    private int batchId;
    private Text3D text3D = null;
    private Tinter tinter = new Tinter();
    private boolean grabbed = false;
    private GrabbedMechanic grabMechanic = null;
    private GridSection currentGridSection;
    private Map<ComponentType, Component> components;
    private List<Component> activeComponents = new ArrayList<Component>();
    private EntityAlerts notifier = new EntityAlerts();
    private DpPerMinCounter dpPerMin = new DpPerMinCounter();
    private DeathAi deathAi;
    private float overallAlpha = 1.0f;

    public Entity(Blueprint blueprint) {
        this.iconDisplay = new StatusIconDisplay(this);
        this.blueprint = blueprint;
        this.reproducer = new Reproducer(this);
    }

    public void setComponents(ComponentBundle compBundle) {
        this.components = compBundle.getComponents();
        this.activeComponents = compBundle.getActiveComponents();
        this.transform = (Transformation)compBundle.getComponent(ComponentType.TRANSFORM);
        this.mesh = (MeshComponent)compBundle.getComponent(ComponentType.MESH);
        this.currentlyInStaticBatch = this.isStatic = !compBundle.isDynamic();
        this.boundingBox = new EntityBox(this.transform, this.mesh);
        this.extraBoundingBoxes = new ExtraBoundingBoxHandler(this.mesh, this.transform);
        this.iconDisplay.init();
        this.setBaseDp();
    }

    public void export(BinaryWriter writer) throws IOException {
        int blueprintId;
        if (this.dead) {
            writer.writeInt(-1);
            return;
        }
        if (this.grabbed) {
            this.transform.clampToTerrain(0.0f);
        }
        if ((blueprintId = this.blueprint.getId()) <= 0) {
            throw new IOException();
        }
        writer.writeInt(blueprintId);
        writer.writeBoolean(this.isStatic);
        writer.writeInt(this.id);
        for (Component component : this.components.values()) {
            component.fullyExport(writer);
        }
    }

    public boolean isNewSpecies() {
        return this.reproducer.isNewSpecies();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public DpPerMinCounter getDpCounter() {
        return this.dpPerMin;
    }

    public Box[] getExtraBoundingBoxes() {
        return this.extraBoundingBoxes.getExtraBoundingBoxes();
    }

    public void setCurrentBatchLocation(boolean staticBatch) {
        this.currentlyInStaticBatch = staticBatch;
    }

    public List<Action> getActions() {
        ArrayList<Action> actions = new ArrayList<Action>();
        if (!GameManager.isNormalMode() || this.isNewSpecies()) {
            this.addPickUpAction(actions);
        }
        if (GameManager.getGameMode() == GameMode.BUILD) {
            this.addDupeOption(actions);
        }
        this.addDieAction(actions);
        this.addMassDeleteAction(actions);
        this.addFollowOption(actions);
        for (Component component : this.components.values()) {
            component.getActions(actions);
        }
        if (GameManager.isNormalMode() && !this.isNewSpecies()) {
            this.addTransplantAction(actions);
        }
        return actions;
    }

    public List<TextStatInfo> getPerformanceBuffInfo() {
        ArrayList<TextStatInfo> info = new ArrayList<TextStatInfo>();
        for (Component component : this.components.values()) {
            component.getPerformanceBuffsInfo(info);
        }
        return info;
    }

    public List<ControlBehaviour> getControlableBehaviour() {
        ArrayList<ControlBehaviour> behaviours = new ArrayList<ControlBehaviour>();
        for (Component component : this.components.values()) {
            component.getControlableBehaviour(behaviours);
        }
        return behaviours;
    }

    private void addDieAction(List<Action> actions) {
        actions.add(new Action(REMOVE, 1){

            @Override
            public void carryOut() {
                Entity.this.die(null, true);
            }
        });
    }

    private void addMassDeleteAction(List<Action> actions) {
        actions.add(new Action(REMOVE_MANY, 1){

            @Override
            public void carryOut() {
                GameManager.getEntityPicker().setSpeciesForDeletion(Entity.this.blueprint);
                EquilinoxGuis.getToolBar().getEraserButton().toggle();
            }
        });
    }

    private void addFollowOption(List<Action> actions) {
        actions.add(new Action(this.blueprint.isAnimal() ? FOLLOW : VIEW, 1){

            @Override
            public void carryOut() {
                Entity.this.follow();
            }
        });
    }

    public void follow() {
        ControlUi.openControlUI(this, null);
        GameManager.getEntityPicker().indicateCurrentControlled(false);
        GameManager.gameState.setState(GameState.CONTROL);
    }

    public void set3dTextDisplay(Text3D text3D) {
        this.text3D = text3D;
    }

    public Text3D getText3D() {
        return this.text3D;
    }

    private void addPickUpAction(List<Action> actions) {
        actions.add(new Action(GameText.getText(1142), 1){

            @Override
            public void carryOut() {
                GameManager.getEntityPicker().grab(Entity.this);
            }
        });
    }

    private void addDupeOption(List<Action> actions) {
        actions.add(new Action("Duplicate", 1){

            @Override
            public void carryOut() {
                GameManager.getShops().getPlacementManager().duplicateEntity(Entity.this);
            }
        });
    }

    private void addTransplantAction(List<Action> actions) {
        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)this.blueprint.getComponent(ComponentType.INFO);
        int price = (int)((float)info.getPrice() * 2.5f);
        actions.add(new Action(TRANSPLANT, 1, price){

            @Override
            public void carryOut() {
                GameManager.getEntityPicker().grab(Entity.this);
            }
        });
    }

    public EntityAlerts getNotifier() {
        return this.notifier;
    }

    public List<Trait> getTraits() {
        ArrayList<Trait> traits = new ArrayList<Trait>();
        for (Component component : this.components.values()) {
            List<Trait> componentTraits = component.getTraits();
            if (componentTraits == null) continue;
            traits.addAll(componentTraits);
        }
        return traits;
    }

    protected Map<ComponentType, Component> getComponents() {
        return this.components;
    }

    public Entity reproduce(boolean boosted) {
        return this.reproducer.reproduce(boosted);
    }

    public Entity duplicate(Transformation.TransformParams transform) {
        return this.reproducer.duplicate(transform);
    }

    public void die(DeathAi deathAi, boolean urgent) {
        if (this.dead) {
            return;
        }
        this.dead = true;
        this.notifier.notifyDied();
        if (this.iconDisplay != null) {
            this.iconDisplay.removeAll();
        }
        this.iconDisplay.showStatusIcon(ParticleAtlasCache.getAtlas(15), 0.5f);
        if (deathAi == null || !deathAi.isEssential() && !this.isVisible()) {
            this.removeFromScene(urgent);
            return;
        }
        this.deathAi = deathAi;
        deathAi.init();
    }

    public boolean isDead() {
        return this.dead;
    }

    public boolean hasComponent(ComponentType type) {
        return this.components.containsKey((Object)type);
    }

    public Component getComponent(ComponentType type) {
        return this.components.get((Object)type);
    }

    public Transformation getTransform() {
        return this.transform;
    }

    public Blueprint getBlueprint() {
        return this.blueprint;
    }

    public float getMaxWidth() {
        return this.blueprint.getMaxWidth() * this.transform.getScaleTrait().getValue();
    }

    public void setCurrentGridSection(GridSection section) {
        this.currentGridSection = section;
        this.placed = true;
    }

    public GridSection getCurrentGridSection() {
        return this.currentGridSection;
    }

    public void unregisterFromCurrentGridSection() {
        if (this.currentGridSection != null) {
            this.currentGridSection.unregisterEntity(this);
            this.currentGridSection = null;
        }
    }

    public boolean isStatic() {
        return this.isStatic;
    }

    public List<PopUpInfoGui> getInfo() {
        ArrayList<PopUpInfoGui> info = new ArrayList<PopUpInfoGui>();
        for (Component component : this.components.values()) {
            component.getStatusInfo(info);
        }
        if (this.dpPerMin.producesDp()) {
            info.add(new TextInfo(DP_TEXT, EntityInfoGui.FONT_SIZE, ColourPalette.GREEN, InfoType.DP_COUNT){

                @Override
                public String getValue() {
                    return "+" + Entity.this.dpPerMin.getDpPerMinute();
                }
            });
        }
        return info;
    }

    public boolean isGrabbed() {
        return this.grabbed;
    }

    public void grab() {
        if (!this.grabbed) {
            this.reproducer.notifyNotNew();
            GameManager.gameState.setState(GameState.GRABBING);
            this.pickUp();
            this.notifier.notifyIncapacitated();
        }
        this.grabMechanic = new GrabbedMechanic(this);
    }

    public void pickUp() {
        this.unregisterFromCurrentGridSection();
        this.grabbed = true;
        GameManager.getWorld().updateBiome(this, false);
        if (this.isStatic && this.placed) {
            GameManager.getSession().getSceneData().makeDynamic(this);
        }
    }

    public void putDown() {
        this.grabbed = false;
        GameManager.getWorld().getEntityGrid().updateInGrid(this);
        if (this.isStatic) {
            int batchId = World.calculateBatchId(this);
            GameManager.getSession().getSceneData().makeStatic(this, batchId);
            GameManager.getWorld().updateBiome(this, true);
        }
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public int getBatchId() {
        return this.batchId;
    }

    public boolean isVisible() {
        if (this.currentGridSection != null) {
            return (this.currentGridSection.isVisible() || this.blueprint.alwaysVisible) && this.inCameraRange();
        }
        return true;
    }

    public void setAlpha(float alpha) {
        this.overallAlpha = alpha;
    }

    public void turnOffShadow() {
        this.noShadow = true;
    }

    public float getAlpha() {
        float alpha;
        if (this.currentGridSection == null || this.inFullyVisibleRange()) {
            alpha = 1.0f;
        } else if (!this.inCameraRange()) {
            alpha = 0.0f;
        } else {
            Vector3f temp = Vec3Pool.get();
            float disSquared = Vector3f.sub(Camera.getCamera().getPosition(), this.transform.getPosition(), temp).lengthSquared();
            Vec3Pool.release(temp);
            float fullVisRange = 216.0f * this.boundingBox.getMaxSize();
            fullVisRange *= fullVisRange;
            float maxViewDis = 240.0f * this.boundingBox.getMaxSize();
            maxViewDis *= maxViewDis;
            alpha = 1.0f - Maths.slope(disSquared, fullVisRange, maxViewDis);
        }
        return alpha * this.overallAlpha;
    }

    public Tinter getTinter() {
        return this.tinter;
    }

    public boolean hasShadow() {
        if (this.currentGridSection != null) {
            return !this.noShadow && this.currentGridSection.isShadowed() && this.inCameraRange() && this.overallAlpha > 0.5f;
        }
        return !this.noShadow && this.overallAlpha > 0.5f;
    }

    public void place() {
        this.grabMechanic.drop();
        InformationComponent info = (InformationComponent)this.getComponent(ComponentType.INFO);
        if (info != null) {
            info.updateBasePosition();
        }
        GameManager.gameState.endState(GameState.GRABBING);
    }

    public void returnToPickUpPosition() {
        this.grabMechanic.returnToStartPosition();
        this.setNotGrabbed();
        GameManager.gameState.endState(GameState.GRABBING);
    }

    public int update() {
        this.tinter.update();
        if (!this.dead) {
            if (!this.grabbed) {
                this.updateComponents();
            } else if (this.grabMechanic != null) {
                this.grabMechanic.update();
                this.checkDropped();
            }
            return this.dpPerMin.getDpPerMinute();
        }
        this.updateDeath();
        return 0;
    }

    public SubBlueprint getSubBlueprint() {
        return this.mesh.getCurrentModelStage();
    }

    @Override
    public Entity getEntity() {
        return this;
    }

    public EntityBox getBoundingBox() {
        return this.boundingBox;
    }

    public boolean isClutter() {
        return this.boundingBox.getMaxSize() < 0.3f && !this.blueprint.canBeUnderwater();
    }

    public boolean isCurrentlyInStaticBatch() {
        return this.currentlyInStaticBatch;
    }

    private void updateDeath() {
        if (this.deathAi == null) {
            return;
        }
        boolean remove = this.deathAi.update();
        if (remove) {
            this.removeFromScene(false);
        }
    }

    private void removeFromScene(boolean urgent) {
        GameManager.getWorld().removeEntity(this, urgent);
        this.reproducer.notifyNotNew();
        this.notifier.notifyRemoved();
    }

    private boolean inCameraRange() {
        return this.currentGridSection.getDistanceFromCam() < 240.0f * this.boundingBox.getMaxSize();
    }

    private boolean inFullyVisibleRange() {
        float fullVisibleRange;
        float furthestDisFromCam = this.currentGridSection.getDistanceFromCam() + GridSection.DIAGONAL_SIZE;
        return furthestDisFromCam < (fullVisibleRange = 216.0f * this.boundingBox.getMaxSize());
    }

    private void setBaseDp() {
        InformationComponent info = (InformationComponent)this.getComponent(ComponentType.INFO);
        if (info == null) {
            this.dpPerMin.setBaseDpPerMin(0);
        } else {
            this.dpPerMin.setBaseDpPerMin(info.getBaseDpPerMin());
        }
    }

    private void updateComponents() {
        for (Component component : this.activeComponents) {
            component.update();
        }
    }

    private void checkDropped() {
        if (this.grabMechanic.isOnFloor()) {
            if (!this.blueprint.getClassification().isTypeOf(Classifier.getAnimalClassification())) {
                SoundMaestro.playSystemSound(THUD);
            } else if (this.grabMechanic.isInWater()) {
                SoundMaestro.playSystemSound(SPLASH);
            }
            this.setNotGrabbed();
        }
    }

    private void setNotGrabbed() {
        this.grabMechanic = null;
        AiComponent aiComp = (AiComponent)this.components.get((Object)ComponentType.AI);
        if (aiComp != null) {
            aiComp.interruptCurrentAi();
        }
        this.putDown();
    }
}

