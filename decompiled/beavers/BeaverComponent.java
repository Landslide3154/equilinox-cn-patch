/*
 * Decompiled with CFR 0.152.
 */
package beavers;

import aiComponent.AiComponent;
import aiComponent.AiProvidingComponent;
import baseMovement.MovementComp;
import beavers.BeaverAi;
import beavers.BeaverCompBlueprint;
import beavers.BeaverParams;
import building.BuildComponent;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import componentArchitecture.ParamsBundle;
import components.InformationComponent;
import entityInfoGui.PopUpInfoGui;
import equipping.EquipComponent;
import growth.GrowthComponent;
import instances.Entity;
import instances.EntityListener;
import interpolation.Timer;
import java.io.IOException;
import java.util.List;
import languages.GameText;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector3f;
import userInterfaces.TextStatInfo;
import utils.BinaryReader;
import utils.BinaryWriter;

public class BeaverComponent
extends Component
implements AiProvidingComponent {
    private static final int FULLY_BUILT_POINTS = 280;
    private static final String PROBLEM = GameText.getText(200);
    private static final String NO_DEN = GameText.getText(201);
    private static final float BUILD_TIME_MIN = 30.0f;
    private static final float BUILD_TIME_MAX = 50.0f;
    private final Timer timer = Timer.createLoopingTimer(30.0f, 50.0f, true).randomize();
    private MovementComp mover;
    private InformationComponent info;
    private GrowthComponent grower;
    private AiComponent aiComponent;
    private EquipComponent equipComp;
    private boolean inProgress = false;
    private boolean denLocationImpossible = false;
    private Entity lastUsedDen = null;

    protected BeaverComponent(BeaverCompBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void update() {
        super.update();
        if (this.inProgress || this.grower.getStageNumber() == 0 || this.denLocationImpossible) {
            return;
        }
        if (this.timer.check() && !this.isDenAlreadyFullyBuilt()) {
            this.aiComponent.queueAiProgram(new BeaverAi(this, this.mover, this.info, this.equipComp));
            this.inProgress = true;
        }
    }

    private boolean isDenAlreadyFullyBuilt() {
        if (this.lastUsedDen != null && !this.lastUsedDen.isGrabbed() && !this.lastUsedDen.isDead()) {
            Vector3f denPos = this.lastUsedDen.getTransform().getPosition();
            if (this.info.isPointInHomeArea(denPos.x, denPos.z)) {
                BuildComponent denBuild = (BuildComponent)this.lastUsedDen.getComponent(ComponentType.BUILD);
                return denBuild.getBuildPoints() > 280;
            }
        }
        return false;
    }

    protected void notifyDenBuildingImpossible() {
        this.denLocationImpossible = true;
    }

    @Override
    public void notifyAiFinished() {
        this.inProgress = false;
    }

    @Override
    public boolean reproduce(ParamsBundle params, boolean selected, Entity entity) {
        params.addParams(new BeaverParams(this.denLocationImpossible));
        return true;
    }

    public void setLastUsedDen(Entity den) {
        this.lastUsedDen = den;
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getPerformanceBuffsInfo(List<TextStatInfo> info) {
        if (this.denLocationImpossible) {
            info.add(new TextStatInfo(PROBLEM, NO_DEN, ColourPalette.BRIGHT_RED, "The beaver is unable to build a den here due to the fact that there is not enough water nearby. Move the beavers to an area with more water presetn."));
        }
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeBoolean(this.denLocationImpossible);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.mover = (MovementComp)((Object)bundle.getComponent(ComponentType.MOVEMENT));
        this.info = (InformationComponent)bundle.getComponent(ComponentType.INFO);
        this.aiComponent = (AiComponent)bundle.getComponent(ComponentType.AI);
        this.equipComp = (EquipComponent)bundle.getComponent(ComponentType.EQUIP);
        this.grower = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        BeaverParams params = (BeaverParams)bundle.getParameters(ComponentType.BEAVER);
        if (params != null) {
            this.denLocationImpossible = params.denBuildingImpossible;
        }
        this.addBasePositionChangeListener();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
        this.denLocationImpossible = reader.readBoolean();
    }

    private void addBasePositionChangeListener() {
        this.info.addBasePositionListener(new EntityListener(){

            @Override
            public void execute() {
                BeaverComponent.this.denLocationImpossible = false;
            }
        });
    }
}

