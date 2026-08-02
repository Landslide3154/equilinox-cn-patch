/*
 * Decompiled with CFR 0.152.
 */
package breeding;

import blueprints.Blueprint;
import breeding.BreedingCompBlueprint;
import breeding.BreedingComponent;
import componentArchitecture.ComponentType;
import gameManaging.GameManager;
import health.LifeCompBlueprint;
import iconSystem.StatusIcon;
import interpolation.Timer;
import java.io.IOException;
import languages.ComplexString;
import languages.GameText;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import particles.ParticleTexture;
import userInterfaces.Listener;
import utils.BinaryReader;
import utils.BinaryWriter;

public class EvolveProcess {
    private static final ParticleTexture PROGRESS_ICON = new ParticleTexture(GuiRepository.PROG_BAR, 4, false);
    private static final int PROGRESS_STAGES = 7;
    private static final int STOPPED_STAGE_START = 8;
    private static final float BAR_SIZE = 0.3f;
    private static final String NOTIFY_TITLE = GameText.getText(1044);
    private static final ComplexString NOTIFY_DESC = GameText.getComplexText(1045);
    private static final float EVOLVE_SPEED = 2.0f;
    private BreedingComponent parent = null;
    private final Blueprint childSpecies;
    private final Timer reqCheckTimer = Timer.createLoopingTimer(1.0f, false);
    private final float totalEvolvePoints;
    private float evolvePoints = 0.0f;
    private boolean reqsMet = false;
    private StatusIcon progressIcon;

    public EvolveProcess(Blueprint childSpecies) {
        this.childSpecies = childSpecies;
        BreedingCompBlueprint childBreedInfo = ((LifeCompBlueprint)childSpecies.getComponent((ComponentType)ComponentType.LIFE)).breedInfo;
        this.totalEvolvePoints = childBreedInfo.getRequiredEvolvePoints();
    }

    public Blueprint getChildSpecies() {
        return this.childSpecies;
    }

    public float getCurrentPoints() {
        return this.evolvePoints;
    }

    public boolean isActive() {
        return this.parent != null;
    }

    public boolean requirementsMet() {
        return this.reqsMet;
    }

    public float getProgressFactor() {
        return this.evolvePoints / this.totalEvolvePoints;
    }

    public float getRequiredPoints() {
        return this.totalEvolvePoints;
    }

    public boolean update() {
        if (this.parent == null || this.parent.getEntity().isGrabbed()) {
            return false;
        }
        if (this.parent.getEntity().isDead()) {
            EquilinoxGuis.notify(NOTIFY_TITLE, NOTIFY_DESC.getString(this.childSpecies.getName()), GuiRepository.HELP_ICON_BIG, GuiSounds.NOTIFY, new Listener(){

                @Override
                public void eventOccurred(boolean on) {
                }
            });
            this.pause();
            return false;
        }
        this.checkRequirements();
        this.updateProgressBar();
        if (this.reqsMet) {
            this.increaseEvolvePoints();
            if (this.evolvePoints == this.totalEvolvePoints) {
                this.evolve();
                this.parent.notifyProcessStop();
                return true;
            }
        }
        return false;
    }

    public boolean isComplete() {
        return this.evolvePoints >= this.totalEvolvePoints;
    }

    public void pause() {
        this.parent.notifyProcessStop();
        this.hideIcon();
        this.parent = null;
    }

    public void continueProcess(BreedingComponent parent) {
        if (this.parent != null) {
            this.pause();
        }
        this.parent = parent;
        this.showProgressBar();
        this.reqsMet = parent.checkRequirementsMet(this.childSpecies);
    }

    public void initialParentSet(BreedingComponent parent) {
        this.parent = parent;
        this.showProgressBar();
    }

    public void save(BinaryWriter writer) throws IOException {
        writer.writeFloat(this.evolvePoints);
    }

    public void load(BinaryReader reader) throws Exception {
        this.evolvePoints = reader.readFloat();
    }

    private void checkRequirements() {
        if (this.reqCheckTimer.check()) {
            this.reqsMet = this.parent.checkRequirementsMet(this.childSpecies);
        }
    }

    private void evolve() {
        this.hideIcon();
        this.parent.forceBreedToUnlock();
    }

    private void increaseEvolvePoints() {
        this.evolvePoints += GameManager.getGameSeconds() * 2.0f;
        this.evolvePoints = Math.min(this.evolvePoints, this.totalEvolvePoints);
    }

    private void showProgressBar() {
        this.progressIcon = this.parent.getEntity().iconDisplay.showStatusIcon(PROGRESS_ICON, 0.3f, true, 0);
    }

    private void updateProgressBar() {
        int progressStage = Math.min((int)(this.evolvePoints / this.totalEvolvePoints * 7.0f), 6);
        if (!this.reqsMet) {
            progressStage += 8;
        }
        this.progressIcon.setStage(progressStage);
    }

    private void hideIcon() {
        this.progressIcon = null;
        this.parent.getEntity().iconDisplay.removeStatusIcon(PROGRESS_ICON);
    }
}

