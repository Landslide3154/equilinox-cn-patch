/*
 * Decompiled with CFR 0.152.
 */
package toolbox;

import blueprints.Blueprint;
import breedingTraits.FloatTrait;
import breedingTraits.FloatTraitBlueprint;
import breedingTrees.ReqInfo;
import classification.Classifier;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentLoader;
import componentArchitecture.ComponentParams;
import componentArchitecture.ComponentType;
import componentArchitecture.ParamsBundle;
import componentArchitecture.Requirement;
import components.InformationComponent;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import instances.Entity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import languages.GameText;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import toolbox.Maths;
import toolbox.TransformChangeListener;
import utils.BinaryReader;
import utils.BinaryWriter;
import utils.CSVReader;
import world.UnplaceableReason;

public class Transformation
extends Component {
    private static final int SUITABLE_LOC_ATTEMPTS = 10;
    private static final int SUITABLE_LOC_ATTEMPTS_NEW = 25;
    private static final int DESPERATE_ATTEMPTS = 20;
    private final TransformBlueprint blueprint;
    private Vector3f position;
    private float rotX;
    private float rotY;
    private float rotZ = 0.0f;
    private FloatTrait scaleTrait;
    private float currentScale;
    private boolean dirty = true;
    private Matrix4f modelMatrix = new Matrix4f();
    private List<TransformChangeListener> listeners = new ArrayList<TransformChangeListener>();
    private Entity entity;

    public Transformation(TransformBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
    }

    @Override
    public TransformBlueprint getBlueprint() {
        return this.blueprint;
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.entity = bundle.getEntity();
        TransformParams params = (TransformParams)bundle.getParameters(ComponentType.TRANSFORM);
        if (params == null) {
            this.position = new Vector3f(0.0f, 0.0f, 0.0f);
        } else {
            this.position = params.position;
            this.rotY = params.rotY;
        }
        this.scaleTrait = (FloatTrait)this.getTrait(0);
        this.currentScale = this.scaleTrait.value;
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.entity = bundle.getEntity();
        this.position = reader.readVector();
        this.rotX = reader.readFloat();
        this.rotY = reader.readFloat();
        this.rotZ = reader.readFloat();
        this.scaleTrait = (FloatTrait)this.getTrait(0);
        this.currentScale = this.scaleTrait.getValue();
    }

    public void addChangeListener(TransformChangeListener listener) {
        this.listeners.add(listener);
    }

    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
        this.indicateChanged();
    }

    public void setXPosition(float x) {
        this.position.x = x;
        this.indicateChanged();
    }

    public void setYPosition(float y) {
        this.position.y = y;
        this.indicateChanged();
    }

    public void setZPosition(float z) {
        this.position.z = z;
        this.indicateChanged();
    }

    public void setPosition(Vector3f newPos) {
        this.position.set(newPos);
        this.indicateChanged();
    }

    public void setXRotation(float xRot) {
        this.rotX = xRot;
        this.indicateChanged();
    }

    public void returnXRotToZero(float changePerSec) {
        if (this.rotX == 0.0f) {
            return;
        }
        this.rotX = Transformation.returnToZero(this.rotX, changePerSec, GameManager.getGameSeconds());
        this.indicateChanged();
    }

    public void setYRotation(float yRot) {
        this.rotY = yRot % 360.0f;
        this.indicateChanged();
    }

    public void setZRotation(float zRot) {
        this.rotZ = zRot;
        this.indicateChanged();
    }

    public float getScale() {
        return this.currentScale;
    }

    public void setScale(float scale) {
        this.currentScale = scale;
        this.indicateChanged();
    }

    public FloatTrait getScaleTrait() {
        return this.scaleTrait;
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public float checkWithTerrain() {
        float height = this.getTerrainHeight();
        return this.testEntityAltitude(height);
    }

    public float checkWithTerrainAndWater() {
        float height = this.getTerrainOrWaterHeight();
        return this.testEntityAltitude(height);
    }

    private float testEntityAltitude(float height) {
        float entityHeight = this.position.y - height;
        if (entityHeight <= 0.0f) {
            this.position.y = height;
            this.dirty = true;
        }
        return entityHeight;
    }

    private static float returnToZero(float current, float changePerSec, float delta) {
        float difference = 0.0f - current;
        float maxAllowedChange = changePerSec * delta;
        if (Math.abs(difference) <= maxAllowedChange) {
            return 0.0f;
        }
        return current += maxAllowedChange * Math.signum(difference);
    }

    public float getTerrainHeight() {
        return GameManager.getWorld().getHeightOfTerrain(this.position.x, this.position.z);
    }

    public float getTerrainOrWaterHeight() {
        float terrainHeight = GameManager.getWorld().getHeightOfTerrain(this.position.x, this.position.z);
        return Math.max(terrainHeight, GameManager.getWorld().getWaterHeight());
    }

    public void clampToTerrain(float offset) {
        float height = GameManager.getWorld().getHeightOfTerrain(this.position.x, this.position.z);
        this.position.y = height + offset;
        this.dirty = true;
    }

    public float getRotX() {
        return this.rotX;
    }

    public float getRotY() {
        return this.rotY;
    }

    public float getRotZ() {
        return this.rotZ;
    }

    public void setModelMatrix(Matrix4f modelMat) {
        this.modelMatrix.load(modelMat);
        this.dirty = false;
    }

    public void updateModelMatrix(Vector3f up, Vector3f forward) {
        Matrix4f rotation = Maths.getRotationMatrix(up, forward);
        this.modelMatrix.setIdentity();
        Matrix4f.translate(this.position, this.modelMatrix, this.modelMatrix);
        Matrix4f.mul(this.modelMatrix, rotation, this.modelMatrix);
        Matrix4f.scale(new Vector3f(this.currentScale, this.currentScale, this.currentScale), this.modelMatrix, this.modelMatrix);
        this.dirty = false;
    }

    public void updateModelMatrix(float scaleUpZ) {
        Maths.updateTransformationMatrix(this.modelMatrix, this.position.x, this.position.y, this.position.z, this.rotX, this.rotY, this.rotZ, 1.0f);
        Matrix4f.scale(new Vector3f(this.currentScale, this.currentScale, this.currentScale * scaleUpZ), this.modelMatrix, this.modelMatrix);
        this.dirty = false;
    }

    public Matrix4f getModelMatrix() {
        if (this.dirty) {
            Maths.updateTransformationMatrix(this.modelMatrix, this.position.x, this.position.y, this.position.z, this.rotX, this.rotY, this.rotZ, this.currentScale);
            this.dirty = false;
        }
        return this.modelMatrix;
    }

    public void increasePosition(float dx, float dy, float dz) {
        this.position.x += dx;
        this.position.y += dy;
        this.position.z += dz;
        this.indicateChanged();
    }

    public void increasePosition(Vector3f change) {
        Vector3f.add(this.position, change, this.position);
        this.indicateChanged();
    }

    public void increaseRotation(float dx, float dy, float dz) {
        this.rotX += dx;
        this.rotY += dy;
        this.rotZ += dz;
        this.indicateChanged();
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public boolean reproduce(ParamsBundle params, boolean boosted, Entity entity) {
        TransformParams param = this.calculateSuitableTransform(boosted);
        if (param == null) {
            return false;
        }
        params.addParams(param);
        return true;
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeVector(this.position);
        writer.writeFloat(this.rotX);
        writer.writeFloat(this.rotY);
        writer.writeFloat(this.rotZ);
    }

    private void indicateChanged() {
        this.dirty = true;
        for (TransformChangeListener listener : this.listeners) {
            listener.transformChanged();
        }
    }

    public TransformParams calculateSuitableTransform(boolean select) {
        Vector3f pos = this.getSuitableSpawnLocation(true);
        if (pos == null) {
            return null;
        }
        return new TransformParams(pos, (float)(Math.random() * 360.0), this, select, this.entity);
    }

    public Vector3f getSuitableSpawnLocation(boolean canReturnNull) {
        if (this.entity.getBlueprint().getClassification().isTypeOf(Classifier.getAnimalClassification())) {
            Vector3f pos = new Vector3f(this.position);
            pos.y = GameManager.getWorld().getHeightOfTerrain(pos.x, pos.z);
            return pos;
        }
        InformationComponent info = (InformationComponent)this.entity.getComponent(ComponentType.INFO);
        Vector3f pos = new Vector3f();
        int count = 0;
        int maxAttempts = canReturnNull ? 10 : 25;
        do {
            pos = count <= 5 ? info.getEvenInRangePoint() : info.getRandomInRangePoint();
            UnplaceableReason reason = GameManager.getWorld().isAcceptableLocation(this.entity.getBlueprint(), pos.x, pos.z);
            if (reason != UnplaceableReason.NO_PROBLEM && (count <= 20 || reason != UnplaceableReason.ENTITY_TOO_CLOSE)) continue;
            pos.y = GameManager.getWorld().getHeightOfTerrain(pos.x, pos.z);
            return pos;
        } while (++count < maxAttempts);
        pos.y = GameManager.getWorld().getHeightOfTerrain(pos.x, pos.z);
        return canReturnNull ? null : pos;
    }

    public static class TransformBlueprint
    extends ComponentBlueprint {
        private static final String SIZE = GameText.getText(898);
        public static final float STANDARD_SCALE = 0.1f;
        private FloatTraitBlueprint sizeBlueprint = new FloatTraitBlueprint(SIZE, 0.1f, 7.0f, 11.0f){

            @Override
            public String formatTrait(float value) {
                return "x" + String.format("%.2f", Float.valueOf(value * 10.0f));
            }
        };

        public TransformBlueprint() {
            super(ComponentType.TRANSFORM);
            super.addTrait(this.sizeBlueprint);
        }

        @Override
        public Component createInstance() {
            return new Transformation(this);
        }

        public FloatTrait generateRandomScale() {
            return this.sizeBlueprint.createRandomInstance();
        }

        public FloatTraitBlueprint getSizeTraitBlueprint() {
            return this.sizeBlueprint;
        }

        @Override
        public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        }

        @Override
        public void delete() {
        }
    }

    public static class TransformLoader
    implements ComponentLoader {
        private static final float MIN = 0.005f;
        private static final String TRAIT_TEXT = GameText.getText(252);

        @Override
        public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
            return new TransformBlueprint();
        }

        @Override
        public Requirement loadRequirement(CSVReader reader) {
            final float targetSize = reader.getNextLabelFloat();
            return new Requirement(){

                @Override
                public boolean check(Entity entity) {
                    float difference = targetSize - entity.getTransform().getScaleTrait().value * 10.0f;
                    return difference <= 0.005f;
                }

                @Override
                public void getGuiInfo(List<ReqInfo> components) {
                    components.add(new ReqInfo(TRAIT_TEXT, String.valueOf(String.format("%.2f", Float.valueOf(targetSize))) + "x"));
                }

                @Override
                public boolean isSecret() {
                    return false;
                }
            };
        }
    }

    public static class TransformParams
    extends ComponentParams {
        public final Vector3f position;
        public final float rotY;

        public TransformParams(Vector3f pos, float rotY, Component component, boolean selected, Entity entity) {
            super(component, selected, entity);
            this.position = new Vector3f(pos);
            this.rotY = rotY;
        }

        public TransformParams(Vector3f pos, float rotY, FloatTrait scaleTrait) {
            super(ComponentType.TRANSFORM, scaleTrait);
            this.position = new Vector3f(pos);
            this.rotY = rotY;
        }
    }
}

