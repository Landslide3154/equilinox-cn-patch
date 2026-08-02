/*
 * Decompiled with CFR 0.152.
 */
package componentArchitecture;

import componentArchitecture.Component;
import componentArchitecture.ComponentParams;
import componentArchitecture.ComponentType;
import componentArchitecture.ParamsBundle;
import instances.DpPerMinCounter;
import instances.Entity;
import instances.EntityGetRequest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import session.EntityLoad;

public class ComponentBundle {
    private Entity entity;
    private Map<ComponentType, Component> components = new LinkedHashMap<ComponentType, Component>();
    private List<Component> activeComponents = new ArrayList<Component>();
    private ParamsBundle parameters;
    private EntityLoad entities;
    private boolean isDynamic = false;

    public ComponentBundle(Entity entity, ComponentParams ... params) {
        this.entity = entity;
        this.parameters = new ParamsBundle(params);
    }

    public ComponentBundle(Entity entity, EntityLoad entities) {
        this.entity = entity;
        this.entities = entities;
        this.parameters = new ParamsBundle(new ComponentParams[0]);
    }

    public void requestEntity(EntityGetRequest request) {
        this.entities.makeRequestForEntity(request);
    }

    public ComponentBundle(Entity entity, ParamsBundle paramsBundle) {
        this.entity = entity;
        this.parameters = paramsBundle;
    }

    public Entity getEntity() {
        return this.entity;
    }

    public DpPerMinCounter getDpCounter() {
        return this.entity.getDpCounter();
    }

    public void addComponent(Component component) {
        this.components.put(component.getType(), component);
        if (component.getType().isActive()) {
            this.activeComponents.add(component);
        }
        this.isDynamic |= component.getBlueprint().isDynamic();
    }

    public Component getComponent(ComponentType type) {
        return this.components.get((Object)type);
    }

    public ComponentParams getParameters(ComponentType type) {
        return this.parameters.getParameters(type);
    }

    public boolean isDynamic() {
        return this.isDynamic;
    }

    public Map<ComponentType, Component> getComponents() {
        return this.components;
    }

    public List<Component> getActiveComponents() {
        return this.activeComponents;
    }
}

