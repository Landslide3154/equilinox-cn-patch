/*
 * Decompiled with CFR 0.152.
 */
package iconSystem;

import iconSystem.StatusIcon;
import instances.Entity;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import particles.ParticleTexture;
import toolbox.TransformChangeListener;

public class StatusIconDisplay
implements TransformChangeListener {
    private static final float ENTITY_PAD = 0.05f;
    private static final float ICON_PAD = 0.02f;
    private final Entity entity;
    private Map<ParticleTexture, StatusIcon> shownIcons = new LinkedHashMap<ParticleTexture, StatusIcon>();
    private boolean initialized = false;
    private Set<StatusIcon> initialIcons = new HashSet<StatusIcon>();
    private float totalIconSizes = 0.05f;
    private boolean checkingTransform = false;
    private boolean dead = false;

    public StatusIconDisplay(Entity entity) {
        this.entity = entity;
    }

    public StatusIcon showStatusIcon(ParticleTexture iconTexture, float size) {
        return this.showStatusIcon(iconTexture, size, false, 0);
    }

    public StatusIcon showStatusIcon(ParticleTexture iconTexture, float size, boolean manual, int index) {
        if (this.entity.isDead()) {
            return null;
        }
        StatusIcon icon = new StatusIcon(iconTexture, size, manual, index);
        if (this.initialized) {
            this.addIcon(icon);
        } else {
            this.initialIcons.add(icon);
        }
        return icon;
    }

    public void removeAll() {
        this.dead = true;
        for (StatusIcon icon : this.shownIcons.values()) {
            icon.kill();
        }
    }

    public boolean removeStatusIcon(ParticleTexture iconTexture) {
        StatusIcon icon = this.shownIcons.remove(iconTexture);
        if (icon != null) {
            icon.kill();
            this.updateIconHeights();
            return true;
        }
        return false;
    }

    public void init() {
        this.initialized = true;
        this.totalIconSizes = this.entity.getBoundingBox().getHeight() + 0.05f;
        for (StatusIcon icon : this.initialIcons) {
            this.addIcon(icon);
        }
        this.initialIcons = null;
    }

    public void updateIconHeights() {
        this.totalIconSizes = this.entity.getBoundingBox().getHeight() + 0.05f;
        for (StatusIcon icon : this.shownIcons.values()) {
            icon.setHeight(this.totalIconSizes + icon.getSize() * 0.5f);
            this.totalIconSizes += icon.getSize() + 0.02f;
        }
    }

    private void addIcon(StatusIcon icon) {
        if (!this.checkingTransform) {
            this.entity.getTransform().addChangeListener(this);
            this.updateIconHeights();
            this.checkingTransform = true;
        }
        icon.initIcon(this.entity.getTransform(), this.totalIconSizes + icon.getSize() * 0.5f);
        this.shownIcons.put(icon.getTexture(), icon);
        this.totalIconSizes += icon.getSize() + 0.02f;
    }

    @Override
    public void transformChanged() {
        if (!this.shownIcons.isEmpty()) {
            this.updateIconHeights();
        }
    }
}

