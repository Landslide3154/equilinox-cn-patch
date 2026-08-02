/*
 * Decompiled with CFR 0.152.
 */
package clouds;

import blueprints.Blueprint;
import clouds.CloudManager;
import clouds.CloudSettings;
import clouds.CloudStructureGenerator;
import dataManagement.DataUpdateManager;
import instances.Entity;
import resourceManagement.BlueprintRepository;

public class CloudFactory {
    public static CloudManager create(DataUpdateManager sceneData, float worldSize) {
        Blueprint[] cloudModels = CloudFactory.getBlueprints(CloudSettings.CLOUD_BLUEPRINT_IDS);
        CloudStructureGenerator cloudCreator = new CloudStructureGenerator(cloudModels, 40, 0.45f, 43.47826f);
        Entity clouds = cloudCreator.generate();
        sceneData.addDynamicEntity(clouds);
        return new CloudManager(clouds, 12.0f, worldSize);
    }

    private static Blueprint[] getBlueprints(int[] ids) {
        Blueprint[] models = new Blueprint[ids.length];
        int i = 0;
        while (i < ids.length) {
            models[i] = BlueprintRepository.getBlueprint(ids[i]);
            ++i;
        }
        return models;
    }
}

