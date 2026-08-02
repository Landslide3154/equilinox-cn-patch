/*
 * Decompiled with CFR 0.152.
 */
package breeding;

import blueprints.Blueprint;
import breeding.BreedingCompBlueprint;
import breeding.EvolveProcess;
import componentArchitecture.ComponentType;
import components.InformationComponent;
import entityBundle.EntityBundle;
import events.EventData;
import events.EventManager;
import gameManaging.GameManager;
import health.LifeCompBlueprint;
import health.LifeComponent;
import instances.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import main.Camera;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import org.lwjgl.util.vector.Vector3f;
import particles.ParticleTexture;
import resourceManagement.BlueprintRepository;
import speciesInformation.SpeciesInfoGui;
import toolbox.Maths;
import toolbox.Transformation;
import userInterfaces.Listener;
import utils.BinaryReader;
import utils.BinaryWriter;
import world.GridSection;
import world.PerSectionCode;

public class BreedingComponent {
    private static final String NOTE_TITLE = GameText.getText(873);
    private static final String NOTE_DESC = GameText.getText(874);
    private static final ParticleTexture ICON_TEXTURE = new ParticleTexture(GuiRepository.BREED_ICON, 1, false);
    private static final float ICON_SIZE = 0.2f;
    private static final int CHECK_RANGE = 2;
    private float nextBreedTime;
    private boolean breedingBoost = false;
    private final BreedingCompBlueprint breedCompBlueprint;
    private Entity entity;
    private final LifeComponent life;
    private int generation = 1;
    private EvolveProcess evolveProcess = null;

    protected BreedingComponent(Entity entity, BreedingCompBlueprint breedCompBlueprint, boolean boosted, float nextBreedTime, int generation, LifeComponent life) {
        this.breedCompBlueprint = breedCompBlueprint;
        this.entity = entity;
        this.life = life;
        this.breedingBoost = boosted;
        this.generation = generation;
        if (this.breedingBoost) {
            entity.iconDisplay.showStatusIcon(ICON_TEXTURE, 0.2f);
        }
        this.nextBreedTime = nextBreedTime;
    }

    protected BreedingComponent(Entity entity, BreedingCompBlueprint breedCompBlueprint, LifeComponent life) {
        this.breedCompBlueprint = breedCompBlueprint;
        this.entity = entity;
        this.life = life;
        this.breedingBoost = false;
        this.nextBreedTime = breedCompBlueprint.breedMaturityAge;
        this.generateNextBreedTime();
    }

    protected void notifyProcessStop() {
        this.evolveProcess = null;
    }

    public Entity tryToBreedNewSpecies() {
        if (this.evolveProcess == null || !this.evolveProcess.isComplete()) {
            return null;
        }
        Blueprint child = this.evolveProcess.getChildSpecies();
        if (GameManager.getSession().getStats().getLockStatus().isUnlocked(child)) {
            return null;
        }
        Entity offspring = this.createNewSpecies(child);
        return offspring;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public int getGeneration() {
        return this.generation;
    }

    private Entity createNewSpecies(Blueprint species) {
        Transformation.TransformBlueprint transform = (Transformation.TransformBlueprint)species.getComponent(ComponentType.TRANSFORM);
        Transformation.TransformParams params = new Transformation.TransformParams(this.entity.getTransform().getSuitableSpawnLocation(false), Maths.RANDOM.nextFloat() * 360.0f, transform.generateRandomScale());
        Entity offspring = species.createInstance(params);
        boolean newSpecies = GameManager.getSession().getStats().getLockStatus().unlockBlueprint(species);
        if (newSpecies) {
            offspring.reproducer.setNewSpecies();
            this.doUnlockReward(offspring);
            GameManager.getShops().unlockItem(species.getId());
        }
        return offspring;
    }

    private void doUnlockReward(final Entity newEntity) {
        final Blueprint newBlueprint = newEntity.getBlueprint();
        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)newBlueprint.getComponent(ComponentType.INFO);
        EquilinoxGuis.notify(NOTE_TITLE, String.valueOf(NOTE_DESC) + info.getName() + ".", GuiRepository.DNA_256, GuiSounds.COMPLETE, new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                SpeciesInfoGui.createSpeciesInfoGui(newBlueprint);
                Camera.getCamera().focusOn(newEntity.getTransform().getPosition());
            }
        });
        Blueprint species = newEntity.getBlueprint();
        EventManager.EVOLUTION.registerEvent(new EventData(species), Integer.toString(species.getId()));
    }

    public void update() {
        float enviroFactor = Maths.smoothStep(0.4f, 0.75f, this.life.getEnvironmentalSatisfaction());
        float hunger = this.life.getHungerValue();
        float hungerFactor = Maths.smoothStep(0.3f, 0.75f, hunger);
        float populationBoost = Math.min(4.0f, 1.0f / this.life.health.getLocalPopulationFactor());
        if (populationBoost > 1.0f) {
            float extra = (populationBoost - 1.0f) * enviroFactor * hungerFactor;
            populationBoost = 1.0f + extra;
        }
        if (hunger > 0.35f) {
            this.nextBreedTime -= GameManager.getDeltaHours() * populationBoost;
        }
        if (this.nextBreedTime <= 0.0f) {
            this.breed(false);
            this.generateNextBreedTime();
        }
    }

    public void forceBreedToUnlock() {
        this.breed(true);
        this.generateNextBreedTime();
    }

    public EvolveProcess setBreedProcess(Blueprint child) {
        this.evolveProcess = GameManager.getEvolvingStatus().startProcess(child, this);
        return this.evolveProcess;
    }

    public void pauseBreedProcess() {
        if (this.evolveProcess != null) {
            this.evolveProcess.pause();
        }
    }

    public EvolveProcess getEvolveProcess() {
        return this.evolveProcess;
    }

    public boolean checkRequirementsMet(Blueprint child) {
        LifeCompBlueprint lifeComp = (LifeCompBlueprint)child.getComponent(ComponentType.LIFE);
        return lifeComp.breedInfo.checkRequirements(this.entity);
    }

    public void export(BinaryWriter writer) throws IOException {
        writer.writeBoolean(this.breedingBoost);
        writer.writeFloat(this.nextBreedTime);
        writer.writeInt(this.generation);
        if (this.evolveProcess != null && !this.evolveProcess.isComplete()) {
            writer.writeBoolean(true);
            writer.writeInt(this.evolveProcess.getChildSpecies().getId());
        } else {
            writer.writeBoolean(false);
        }
    }

    public void loadEvolveData(BinaryReader reader) throws Exception {
        boolean isEvolving = reader.readBoolean();
        if (isEvolving) {
            Blueprint child = BlueprintRepository.getBlueprint(reader.readInt());
            this.evolveProcess = GameManager.getEvolvingStatus().getProcess(child);
            if (this.evolveProcess != null) {
                this.evolveProcess.initialParentSet(this);
            }
        }
    }

    public boolean isBreedingBoosted() {
        return this.breedingBoost;
    }

    public void setBreedingBoost(boolean boost) {
        this.breedingBoost = boost;
        if (boost) {
            this.entity.iconDisplay.showStatusIcon(ICON_TEXTURE, 0.2f);
        } else {
            this.entity.iconDisplay.removeStatusIcon(ICON_TEXTURE);
        }
    }

    public void notifyRemoved() {
        this.entity.iconDisplay.removeStatusIcon(ICON_TEXTURE);
    }

    private void generateNextBreedTime() {
        this.nextBreedTime = (float)((double)this.nextBreedTime + (Maths.RANDOM.nextGaussian() * (double)this.breedCompBlueprint.standardDeviation + (double)this.breedCompBlueprint.averageBreedTime));
    }

    private void breed(boolean ignoreSelectiveBreeders) {
        Vector3f pos = this.entity.getTransform().getPosition();
        ArrayList<Entity> boostedEntities = new ArrayList<Entity>();
        GameManager.getWorld().iterateGridSquaresNew(pos.x, pos.z, 2, this.getPerSectionCode(boostedEntities));
        Entity chosenEntity = ignoreSelectiveBreeders ? this.entity : this.chooseEntityToReproduce(this.entity, boostedEntities);
        this.reproduceEntity(chosenEntity, !boostedEntities.isEmpty());
    }

    private Entity chooseEntityToReproduce(Entity entity, List<Entity> boostedEntities) {
        if (!boostedEntities.isEmpty()) {
            return boostedEntities.get(Maths.RANDOM.nextInt(boostedEntities.size()));
        }
        return entity;
    }

    private void reproduceEntity(Entity chosen, boolean boosted) {
        Entity offspring = chosen.reproduce(boosted);
        if (offspring != null) {
            LifeComponent lifeComp = (LifeComponent)offspring.getComponent(ComponentType.LIFE);
            if (!offspring.reproducer.isNewSpecies()) {
                lifeComp.getBreedComponent().generation = this.generation + 1;
            }
            GameManager.getWorld().addInstance(offspring, false);
        }
    }

    private PerSectionCode getPerSectionCode(final List<Entity> boostedEntities) {
        return new PerSectionCode(){

            @Override
            public void execute(GridSection gridSquare) {
                EntityBundle entities = gridSquare.getEntities(BreedingComponent.this.entity.getBlueprint());
                if (entities == null) {
                    return;
                }
                for (Entity entity : entities) {
                    LifeComponent wellbeingComp = (LifeComponent)entity.getComponent(ComponentType.LIFE);
                    if (!wellbeingComp.getBreedComponent().isBreedingBoosted()) continue;
                    boostedEntities.add(entity);
                }
            }
        };
    }
}

