/*
 * Decompiled with CFR 0.152.
 */
package particleComponent;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.MeshComponent;
import entityInfoGui.PopUpInfoGui;
import instances.Entity;
import java.util.List;
import main.Camera;
import main.IGameCam;
import materials.MaterialComponent;
import org.lwjgl.util.vector.Vector3f;
import particleComponent.ParticleCompBlueprint;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class ParticleComponent
extends Component {
    private MeshComponent mesh;
    private ParticleCompBlueprint blueprint;
    private Transformation transform;
    private MaterialComponent material;
    private Entity entity;
    private int currentStage = -1;
    private boolean active;

    protected ParticleComponent(ParticleCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.mesh = (MeshComponent)bundle.getComponent(ComponentType.MESH);
        this.material = (MaterialComponent)bundle.getComponent(ComponentType.MATERIAL);
        this.entity = bundle.getEntity();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void update() {
        if (this.mesh.getCurrentStageNumber() != this.currentStage) {
            this.updateCurrentStage();
        }
        if (this.active && this.entity.isVisible() && this.getDistanceFromCamera() < this.blueprint.getRangeSquared()) {
            this.emitParticles();
        }
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    private void emitParticles() {
        if (this.blueprint.doesTakeMaterial()) {
            this.blueprint.getSystem().generateParticles(this.transform.getModelMatrix(), this.material.getMaterial(), this.transform.getScale());
        } else {
            this.blueprint.getSystem().generateParticles(this.transform.getModelMatrix(), this.transform.getScale());
        }
    }

    private void updateCurrentStage() {
        this.currentStage = this.mesh.getCurrentStageNumber();
        this.active = this.blueprint.isActiveStage(this.currentStage);
    }

    private float getDistanceFromCamera() {
        IGameCam cam = Camera.getCamera();
        return Vector3f.sub(cam.getPosition(), this.transform.getPosition(), null).lengthSquared();
    }
}

