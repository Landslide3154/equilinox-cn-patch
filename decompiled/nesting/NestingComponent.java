/*
 * Decompiled with CFR 0.152.
 */
package nesting;

import building.BuildComponent;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import componentArchitecture.ParamsBundle;
import components.InformationComponent;
import components.MeshComponent;
import entityBundle.EntityBundle;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import instances.Entity;
import java.util.List;
import nesting.NestingCompBlueprint;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class NestingComponent
extends Component {
    private static final float STAND_OFFSET = 0.02f;
    private static final int NESTS_CHECK = 4;
    private NestingCompBlueprint blueprint;
    private InformationComponent info;
    private Transformation birdTransform;
    private Entity entity;

    protected NestingComponent(NestingCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public boolean reproduce(ParamsBundle params, boolean selected, Entity entity) {
        Entity[] nests;
        super.reproduce(params, selected, entity);
        EntityBundle bundle = this.getNearbyNests();
        if (bundle.isEmpty()) {
            return false;
        }
        Entity[] entityArray = nests = bundle.getRandomList(4);
        int n = nests.length;
        int n2 = 0;
        while (n2 < n) {
            Entity nest = entityArray[n2];
            boolean spawned = this.tryToSpawnInNest(nest, params, selected);
            if (spawned) {
                return true;
            }
            ++n2;
        }
        return false;
    }

    private EntityBundle getNearbyNests() {
        Vector3f basePos = this.info.getBasePosition();
        EntityBundle bundle = GameManager.getWorld().getListOfSpecies(this.blueprint.nest, this.info.getRoamingRange(), basePos.x, basePos.z);
        return bundle;
    }

    private boolean tryToSpawnInNest(Entity nest, ParamsBundle params, boolean selected) {
        MeshComponent mesh = (MeshComponent)nest.getComponent(ComponentType.MESH);
        if (mesh.getCurrentStageNumber() >= this.blueprint.hatchStage) {
            if (this.blueprint.decreasesModel) {
                this.decreaseBuild(nest);
            }
            this.addTransformParams(nest, params, selected);
            return true;
        }
        return false;
    }

    private void decreaseBuild(Entity nest) {
        BuildComponent buildComp = (BuildComponent)nest.getComponent(ComponentType.BUILD);
        buildComp.build(-buildComp.getPointsPerStage(), false);
    }

    private void addTransformParams(Entity nest, ParamsBundle params, boolean selected) {
        Transformation transform = nest.getTransform();
        Vector3f pos = new Vector3f(transform.getPosition());
        pos.y += 0.02f;
        params.addParams(new Transformation.TransformParams(pos, (float)(Math.random() * 360.0), this.birdTransform, selected, this.entity));
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        this.birdTransform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.entity = bundle.getEntity();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }
}

