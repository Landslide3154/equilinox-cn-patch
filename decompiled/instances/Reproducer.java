/*
 * Decompiled with CFR 0.152.
 */
package instances;

import componentArchitecture.Component;
import componentArchitecture.ComponentParams;
import componentArchitecture.ComponentType;
import componentArchitecture.ParamsBundle;
import components.InformationComponent;
import health.LifeComponent;
import instances.Entity;
import languages.ComplexString;
import languages.GameText;
import main.Camera;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import particles.ParticleTexture;
import toolbox.Transformation;
import userInterfaces.Listener;

public class Reproducer {
    private static final String MUTATION_TITLE = GameText.getText(1075);
    private static final ComplexString MUTATION_DESC = GameText.getComplexText(1076);
    private static final float ICON_SIZE = 0.2f;
    private static final ParticleTexture ICON_TEXTURE = new ParticleTexture(GuiRepository.NEW_SPECIES, 1, false);
    private final Entity entity;
    private boolean newSpecies;
    private boolean mutationOccurred = false;

    public Reproducer(Entity entity) {
        this.entity = entity;
    }

    public Entity reproduce(boolean boosted) {
        if (this.entity.isNewSpecies()) {
            return null;
        }
        LifeComponent life = (LifeComponent)this.entity.getComponent(ComponentType.LIFE);
        Entity newSpecies = life.getBreedComponent().tryToBreedNewSpecies();
        if (newSpecies != null) {
            return newSpecies;
        }
        return this.createOffspring(boosted);
    }

    public boolean isNewSpecies() {
        return this.newSpecies;
    }

    public void setNewSpecies() {
        this.newSpecies = true;
        this.entity.iconDisplay.showStatusIcon(ICON_TEXTURE, 0.2f);
    }

    protected void notifyNotNew() {
        if (this.newSpecies) {
            this.newSpecies = false;
            this.entity.iconDisplay.removeStatusIcon(ICON_TEXTURE);
        }
    }

    public void notifyMutation() {
        this.mutationOccurred = true;
    }

    private Entity createOffspring(boolean boosted) {
        ParamsBundle params = new ParamsBundle(new ComponentParams[0]);
        boolean allClear = true;
        for (Component component : this.entity.getComponents().values()) {
            allClear &= component.reproduce(params, boosted, this.entity);
        }
        if (!allClear) {
            this.mutationOccurred = false;
            return null;
        }
        Entity offspring = this.entity.getBlueprint().createInstance(params);
        ((InformationComponent)offspring.getComponent(ComponentType.INFO)).setParent(this.entity);
        if (this.mutationOccurred) {
            this.createNotification(offspring);
            this.mutationOccurred = false;
        }
        return offspring;
    }

    public Entity duplicate(Transformation.TransformParams transformParams) {
        ParamsBundle params = new ParamsBundle(new ComponentParams[0]);
        for (Component component : this.entity.getComponents().values()) {
            component.duplicate(params);
        }
        params.addParams(transformParams);
        Entity offspring = this.entity.getBlueprint().createInstance(params);
        return offspring;
    }

    private void createNotification(final Entity offspring) {
        EquilinoxGuis.notify(MUTATION_TITLE, MUTATION_DESC.getString(offspring.getBlueprint().getName()), GuiRepository.DNA_256, GuiSounds.NOTIFY, new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                Camera.getCamera().focusOn(offspring.getTransform().getPosition());
            }
        });
    }
}

