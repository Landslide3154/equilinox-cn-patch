/*
 * Decompiled with CFR 0.152.
 */
package picking;

import audio.SoundMaestro;
import blueprints.Blueprint;
import entityBundle.EntityBundle;
import entityInfoGui.EntityInfoGui;
import gameManaging.GameManager;
import guis.GuiMaster;
import instances.Entity;
import main.Camera;
import mainGuis.ColourPalette;
import mainGuis.EquilinoxGuis;
import mainGuis.EscListener;
import mainGuis.GuiSounds;
import org.lwjgl.util.vector.Vector3f;
import picking.Picker3D;
import speciesInformation.SpeciesInfoGui;
import toolbox.Colour;
import toolbox.Highlight;
import toolbox.Maths;
import toolbox.MyKeyboard;
import toolbox.MyMouse;
import toolbox.WorldHighlights;
import world.GridIterator;
import world.GridSection;
import world.UnplaceableReason;

public class EntitySelectionManager {
    private static final Colour MO_COLOUR = new Colour(0.8f, 0.8f, 0.8f);
    private static final Colour SELECTED_COLOUR = new Colour(1.0f, 1.0f, 0.35f);
    private static final Colour GRABBED_COLOUR = new Colour(0.3f, 1.0f, 0.3f);
    private static final Colour CONTROLLED_COLOUR = new Colour(162.0f, 86.0f, 190.0f, true);
    private static final Colour FOLLOW_COLOUR = new Colour(0.2f, 0.6f, 0.85f);
    private static final Colour NO_PLACE_COLOUR = new Colour(1.0f, 0.3f, 0.3f);
    private static final Colour DELETING_COLOUR = ColourPalette.BRIGHT_RED.duplicate().scale(1.45f);
    private static final Colour DELETE_COLOUR = ColourPalette.BRIGHT_RED.duplicate().scale(0.85f);
    private Entity currentlyMousedOver;
    private Entity currentlySelected;
    private Entity currentlyGrabbed;
    private MyMouse mouse = MyMouse.getActiveMouse();
    private Highlight primaryHighlight;
    private Highlight secondaryHighlight;
    private Picker3D picker = new Picker3D();
    private EntityInfoGui openInfoGui;
    private boolean enabled = true;
    private boolean following = false;
    private boolean delete = false;
    private float deleteRange;
    private Blueprint deleteSpecies;
    private boolean firstDeleteHold = false;

    public EntitySelectionManager() {
        this.primaryHighlight = WorldHighlights.getHighlights().getHighlight1();
        this.secondaryHighlight = WorldHighlights.getHighlights().getHighlight2();
        EquilinoxGuis.addEscListener(new EscListener(){

            @Override
            public boolean escPressed() {
                if (EntitySelectionManager.this.delete) {
                    if (EquilinoxGuis.getToolBar().getEraserButton().isToggledOn()) {
                        EquilinoxGuis.getToolBar().getEraserButton().toggle();
                    }
                    return true;
                }
                return EntitySelectionManager.this.deselect();
            }
        });
    }

    public int getPickerTexture() {
        return this.picker.getFboTexture();
    }

    public void update() {
        if (!this.delete && this.currentlySelected == null && MyKeyboard.getKeyboard().keyDownEventOccurred(211)) {
            if (!EquilinoxGuis.getToolBar().getEraserButton().isToggledOn()) {
                EquilinoxGuis.getToolBar().getEraserButton().toggle();
            }
        } else if (this.delete && MyKeyboard.getKeyboard().keyDownEventOccurred(211) && EquilinoxGuis.getToolBar().getEraserButton().isToggledOn()) {
            EquilinoxGuis.getToolBar().getEraserButton().toggle();
        }
        if (!this.delete) {
            this.update3dPicker();
            if (this.enabled) {
                if (this.currentlyGrabbed == null) {
                    this.dealWithNotHeld();
                } else {
                    this.checkPlacement();
                }
            }
            this.updateSelectedEntity();
        } else {
            this.updateDeleteMode();
        }
        WorldHighlights.getHighlights().updateHighlights();
    }

    private void updateDeleteMode() {
        this.primaryHighlight.setColour(ColourPalette.BRIGHT_RED);
        if (GuiMaster.isMouseInGui()) {
            if (this.mouse.isLeftClick() && !this.firstDeleteHold && EquilinoxGuis.getToolBar().getEraserButton().isToggledOn()) {
                EquilinoxGuis.getToolBar().getEraserButton().toggle();
            }
        } else if (this.mouse.isLeftButtonDown() && !this.firstDeleteHold) {
            this.primaryHighlight.setColour(DELETING_COLOUR);
            this.deleteAllInRange();
        }
        if ((this.mouse.shortRightClickOccurred() || this.mouse.shortMiddleClickOccurred()) && EquilinoxGuis.getToolBar().getEraserButton().isToggledOn()) {
            EquilinoxGuis.getToolBar().getEraserButton().toggle();
        }
        if (!this.mouse.isLeftButtonDown()) {
            this.firstDeleteHold = false;
        }
    }

    public void enable(boolean enable) {
        this.enabled = enable;
        if (!this.enabled) {
            this.returnToPickUp();
        }
    }

    public void setDeleteMode(float range) {
        if (this.delete) {
            return;
        }
        this.clear();
        this.deleteRange = range;
        this.delete = true;
        this.primaryHighlight.followMouse(DELETE_COLOUR, range);
        this.firstDeleteHold = true;
    }

    public void setSpeciesForDeletion(Blueprint blueprint) {
        this.deleteSpecies = blueprint;
    }

    public void turnOffDeleteMode() {
        if (!this.delete) {
            return;
        }
        this.deleteSpecies = null;
        this.primaryHighlight.hide();
        this.delete = false;
    }

    private void deleteAllInRange() {
        Vector3f terrainPoint = GameManager.getTerrainPicker().getCurrentTerrainPoint();
        if (terrainPoint == null) {
            return;
        }
        float rangeSquared = this.deleteRange * this.deleteRange;
        int gridRange = (int)Math.floor((this.deleteRange / 2.5f + 0.5f) * 2.0f + 0.999f);
        GridIterator iterator = GameManager.getWorld().getIterator(terrainPoint.x, terrainPoint.z, gridRange);
        while (iterator.hasNext()) {
            EntityBundle bundle;
            GridSection section = iterator.next();
            EntityBundle entityBundle = bundle = this.deleteSpecies == null ? section.getAllEntities() : section.getEntities(this.deleteSpecies);
            if (bundle == null || bundle.isEmpty()) continue;
            for (Entity entity : bundle) {
                Vector3f pos = entity.getTransform().getPosition();
                if (!(Maths.getComparitableDistance(pos.x, pos.z, terrainPoint.x, terrainPoint.z) < rangeSquared)) continue;
                entity.die(null, true);
            }
        }
    }

    public void grab(Entity entity) {
        this.deselect();
        this.currentlyMousedOver = null;
        this.currentlyGrabbed = entity;
        this.currentlyGrabbed.grab();
        this.secondaryHighlight.hide();
        this.primaryHighlight.followEntity(entity, GRABBED_COLOUR);
    }

    public void cleanUp() {
        this.picker.cleanUp();
    }

    public void clear() {
        this.deselect();
        this.mouseOff();
        this.returnToPickUp();
    }

    public void clearMouseOver() {
        this.mouseOff();
    }

    public void mouseOff() {
        if (this.currentlyMousedOver != null) {
            this.secondaryHighlight.hide();
            this.currentlyMousedOver = null;
        }
    }

    public void reset() {
        this.picker.reset();
    }

    public boolean deselect() {
        if (this.currentlySelected != null) {
            this.following = false;
            this.currentlySelected = null;
            this.primaryHighlight.hide();
            this.closeInfoGui();
            return true;
        }
        return false;
    }

    public void indicateCurrentControlled(boolean controlling) {
        if (this.currentlySelected != null) {
            this.mouseOff();
            this.following = true;
            Camera.getCamera().setTargetEntity(this.currentlySelected.getTransform().getPosition());
            this.primaryHighlight.setColour(controlling ? CONTROLLED_COLOUR : FOLLOW_COLOUR);
            this.closeInfoGui();
        }
    }

    private void updateSelectedEntity() {
        if (this.currentlySelected != null) {
            if (this.currentlySelected.isDead()) {
                this.deselect();
            } else if (MyKeyboard.getKeyboard().keyDownEventOccurred(33) && !this.following) {
                this.currentlySelected.follow();
            }
        }
    }

    private Entity getPickedEntity() {
        return this.picker.getPickedEntity();
    }

    private void update3dPicker() {
        if (GameManager.sessionManager.hasWorldReady() && !GameManager.sessionManager.isLoading()) {
            this.picker.update(GameManager.getWorld().getEntityGrid().getWorldEntities());
        }
    }

    private boolean canBeDropped() {
        Vector3f pos = this.currentlyGrabbed.getTransform().getPosition();
        UnplaceableReason reason = GameManager.getWorld().isAcceptableLocation(this.currentlyGrabbed.getBlueprint(), pos.x, pos.z);
        if (reason == UnplaceableReason.NO_PROBLEM) {
            this.primaryHighlight.setColour(GRABBED_COLOUR);
            return true;
        }
        this.primaryHighlight.setColour(NO_PLACE_COLOUR);
        return false;
    }

    private void dealWithNotHeld() {
        if (this.mouse.isLeftClick()) {
            this.deselect();
        }
        this.checkForNewPickedEntity();
    }

    private void checkForNewPickedEntity() {
        Entity pickedEntity = this.getPickedEntity();
        if (pickedEntity == null || pickedEntity == this.currentlySelected || pickedEntity.isDead()) {
            this.mouseOff();
        } else {
            this.checkForUserAction(pickedEntity);
        }
    }

    private void checkPlacement() {
        if (this.canBeDropped() && this.mouse.isLeftClick()) {
            this.place();
        }
    }

    private void checkForUserAction(Entity entity) {
        if (this.mouse.isLeftClick()) {
            if (entity.isNewSpecies()) {
                this.grab(entity);
                SpeciesInfoGui.createSpeciesInfoGui(entity.getBlueprint());
            } else {
                this.select(entity);
            }
        } else {
            this.mouseover(entity);
        }
    }

    private void select(Entity entity) {
        if (entity != this.currentlySelected) {
            this.deselect();
            this.currentlySelected = entity;
            this.secondaryHighlight.hide();
            this.primaryHighlight.followEntity(entity, SELECTED_COLOUR);
            this.openInfoGui = new EntityInfoGui(entity);
            SoundMaestro.playSystemSound(GuiSounds.SELECT);
        }
    }

    private void mouseover(Entity entity) {
        if (entity != this.currentlyMousedOver && entity != this.currentlySelected) {
            this.mouseOff();
            this.currentlyMousedOver = entity;
            this.secondaryHighlight.followEntity(entity, MO_COLOUR);
        }
    }

    private void place() {
        if (this.currentlyGrabbed != null) {
            this.currentlyGrabbed.place();
            this.currentlyGrabbed = null;
            this.primaryHighlight.hide();
        }
    }

    private void returnToPickUp() {
        if (this.currentlyGrabbed != null) {
            this.currentlyGrabbed.returnToPickUpPosition();
            this.currentlyGrabbed = null;
            this.primaryHighlight.hide();
        }
    }

    private void closeInfoGui() {
        if (this.openInfoGui != null) {
            this.openInfoGui.remove();
        }
    }
}

