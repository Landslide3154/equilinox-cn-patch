/*
 * Decompiled with CFR 0.152.
 */
package monkeys;

import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentType;
import java.util.List;
import java.util.Map;
import languages.GameText;
import monkeys.ClingComponent;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;

public class ClingCompBlueprint
extends ComponentBlueprint {
    private static final String ABILITY = GameText.getText(419);
    protected final Vector3f offset;
    protected final float rotX;

    protected ClingCompBlueprint(Vector3f offset, float rotX) {
        super(ComponentType.CLING);
        this.offset = offset;
        this.rotX = rotX;
    }

    @Override
    public Component createInstance() {
        return new ClingComponent(this);
    }

    @Override
    public void delete() {
    }

    @Override
    public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        info.get((Object)SpeciesInfoType.ABILITIES).add(new SpeciesInfoLine("Ability", ABILITY));
    }

    public Matrix4f getOffsetMatrix(float parentScale, float childScale) {
        Matrix4f clingMatrix = new Matrix4f();
        Matrix4f.translate(this.offset, clingMatrix, clingMatrix);
        Matrix4f.rotate((float)Math.toRadians(this.rotX), new Vector3f(1.0f, 0.0f, 0.0f), clingMatrix, clingMatrix);
        float scale = childScale / parentScale;
        Matrix4f.scale(new Vector3f(scale, scale, scale), clingMatrix, clingMatrix);
        return clingMatrix;
    }
}

