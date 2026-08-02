/*
 * Decompiled with CFR 0.152.
 */
package saves;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import instances.Entity;
import java.util.Map;
import utils.BinaryReader;

public class SaveAdapter {
    private final int versionOfChange;
    private final int speciesId;
    private final ComponentType addedCompType;

    public SaveAdapter(int version, int speciesId, ComponentType addedCompType) {
        this.versionOfChange = version;
        this.speciesId = speciesId;
        this.addedCompType = addedCompType;
    }

    public boolean required(int version, int speciesId) {
        return version < this.versionOfChange && speciesId == this.speciesId;
    }

    public Entity loadOldVersionComponents(Entity entity, Map<ComponentType, ComponentBlueprint> components, ComponentBundle compBundle, BinaryReader reader) {
        for (ComponentBlueprint componentBlueprint : components.values()) {
            try {
                Component component = componentBlueprint.createInstance();
                if (componentBlueprint.getComponentType() != this.addedCompType) {
                    component.loadComponentTraits(componentBlueprint, reader);
                    component.load(compBundle, reader);
                } else {
                    component.createComponentTraits(componentBlueprint, compBundle);
                    component.create(compBundle);
                }
                compBundle.addComponent(component);
            }
            catch (Exception e) {
                System.err.println("Couldn't load component!");
                e.printStackTrace();
            }
        }
        entity.setComponents(compBundle);
        return entity;
    }
}

