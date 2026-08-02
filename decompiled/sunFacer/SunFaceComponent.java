/*
 * Decompiled with CFR 0.152.
 */
package sunFacer;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import entityInfoGui.PopUpInfoGui;
import environment.EnvironmentVariables;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class SunFaceComponent
extends Component {
    private static final float OFFSET = 70.0f;
    private Transformation transform;
    private Vector2f faceDir = new Vector2f();

    protected SunFaceComponent(ComponentBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void update() {
        super.update();
        Vector3f lightDir = EnvironmentVariables.getVariables().getLightDirection();
        this.faceDir.set(lightDir.x, lightDir.z);
        this.faceDir.negate();
        float rot = Maths.calculateVectorRotationY(this.faceDir);
        this.transform.setYRotation(rot - 70.0f);
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
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }
}

