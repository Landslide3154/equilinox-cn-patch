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
import saves.SaveAdapter;
import utils.BinaryReader;

public class SaveAdapterMulti
extends SaveAdapter {
    private ComponentType[] addedCompTypes;

    public SaveAdapterMulti(int version, int speciesId, ComponentType ... addedCompTypes) {
        super(version, speciesId, null);
        this.addedCompTypes = addedCompTypes;
    }

    @Override
    public Entity loadOldVersionComponents(Entity entity, Map<ComponentType, ComponentBlueprint> components, ComponentBundle compBundle, BinaryReader reader) {
        for (ComponentBlueprint componentBlueprint : components.values()) {
            try {
                Component component = componentBlueprint.createInstance();
                if (this.isAddedComponent(componentBlueprint.getComponentType())) {
                    component.createComponentTraits(componentBlueprint, compBundle);
                    component.create(compBundle);
                } else {
                    component.loadComponentTraits(componentBlueprint, reader);
                    component.load(compBundle, reader);
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

    private boolean isAddedComponent(ComponentType testType) {
        ComponentType[] componentTypeArray = this.addedCompTypes;
        int n = this.addedCompTypes.length;
        int n2 = 0;
        while (n2 < n) {
            ComponentType type = componentTypeArray[n2];
            if (testType == type) {
                return true;
            }
            ++n2;
        }
        return false;
    }
}

