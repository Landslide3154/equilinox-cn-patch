/*
 * Decompiled with CFR 0.152.
 */
package clouds;

import blueprints.Blueprint;
import clouds.CloudBatcher;
import componentArchitecture.ComponentParams;
import instances.Entity;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;

public class CloudStructureGenerator {
    private final Blueprint[] cloudModels;
    private final int cloudCount;
    private final float cloudSize;
    private final float range;

    protected CloudStructureGenerator(Blueprint[] cloudModels, int cloudCount, float cloudSize, float range) {
        this.cloudModels = cloudModels;
        this.cloudCount = cloudCount;
        this.cloudSize = cloudSize;
        this.range = range;
    }

    protected Entity generate() {
        CloudBatcher batcher = new CloudBatcher();
        this.generateClouds(batcher);
        Blueprint blueprint = Blueprint.create(0, batcher.getInstanceData());
        Entity entity = blueprint.createInstance(new ComponentParams[0]);
        entity.turnOffShadow();
        entity.getTransform().setScale(1.0f);
        return entity;
    }

    private void generateClouds(CloudBatcher batcher) {
        int i = 0;
        while (i < this.cloudCount) {
            Vector3f position = this.generatePosition();
            float rotation = Maths.RANDOM.nextFloat() * 360.0f;
            float scale = this.cloudSize;
            Blueprint model = this.cloudModels[Maths.RANDOM.nextInt(this.cloudModels.length)];
            batcher.addModel(model.getData(), position, rotation, scale);
            ++i;
        }
    }

    private Vector3f generatePosition() {
        Vector3f position = Maths.randomPointOnCircle(Maths.UP, this.range);
        position.y = Maths.RANDOM.nextFloat() * 4.0f - 2.0f;
        return position;
    }
}

