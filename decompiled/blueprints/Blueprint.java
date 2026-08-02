/*
 * Decompiled with CFR 0.152.
 */
package blueprints;

import blueprints.BlueprintLoader;
import blueprints.SubBlueprint;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentParams;
import componentArchitecture.ComponentType;
import componentArchitecture.ParamsBundle;
import components.InformationComponent;
import components.MeshComponent;
import gameManaging.GameManager;
import instances.Entity;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import languages.GameText;
import mainGuis.ColourPalette;
import org.lwjgl.util.vector.Vector3f;
import picking.AABB;
import resourceProcessing.RequestProcessor;
import resourceProcessing.ResourceRequest;
import saves.AdapterBundle;
import saves.SaveAdapter;
import saves.SaveAdapterMulti;
import session.EntityLoad;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import speciesInformation.TierGui;
import toolbox.Maths;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.MyFile;

public class Blueprint {
    private static final String DP_MESSAGE = GameText.getText(870);
    private static final String CATEGORY = GameText.getText(916);
    private final int id;
    private int mainSubBlueprint;
    private String overrideName;
    private Classification classification;
    private Classification speciesClassification;
    private boolean loaded = false;
    private boolean isSecret = false;
    private boolean canBeUnderwater = false;
    private boolean canBeOverwater = false;
    private boolean randomModelStages = false;
    public boolean alwaysVisible = false;
    private float acceptableHeightOffset = 0.0f;
    private float maxWidth;
    private float maxHeight;
    private boolean hasOverrideIconValues = false;
    private float overrideIconSize;
    private float overrideIconY;
    private List<SubBlueprint> subBlueprints;
    private Map<ComponentType, ComponentBlueprint> components = new LinkedHashMap<ComponentType, ComponentBlueprint>();
    private static final AdapterBundle saveAdapter = new AdapterBundle(new SaveAdapter(1, 84, ComponentType.MATERIAL), new SaveAdapter(2, 72, ComponentType.MATERIAL), new SaveAdapterMulti(3, 19, ComponentType.FRUITER, ComponentType.FOOD), new SaveAdapter(13, 89, ComponentType.MATERIAL), new SaveAdapter(13, 99, ComponentType.MATERIAL));

    private Blueprint(int id) {
        this.id = id;
        this.addStandardComponents();
    }

    public static Blueprint load(int id, MyFile file, boolean backgroundLoad) {
        Blueprint blueprint = new Blueprint(id);
        if (backgroundLoad) {
            Blueprint.backgroundLoadBlueprint(blueprint, file);
        } else {
            Blueprint.loadBlueprintNow(blueprint, file);
        }
        return blueprint;
    }

    public static Blueprint create(int id, float[] modelData) {
        Blueprint blueprint = new Blueprint(id);
        ArrayList<SubBlueprint> subs = new ArrayList<SubBlueprint>();
        subs.add(new SubBlueprint(modelData, new AABB(new Vector3f(), new Vector3f()), null, 1.0f));
        blueprint.setSubBlueprints(subs);
        blueprint.indicateLoaded();
        return blueprint;
    }

    public void setOverrideIconValues(float size, float y) {
        this.overrideIconSize = size;
        this.overrideIconY = y;
        this.hasOverrideIconValues = true;
    }

    public float getIconMaxSize() {
        return this.hasOverrideIconValues ? this.overrideIconSize * this.getMaxSize() : this.getMaxSize();
    }

    public float getIconY() {
        return this.hasOverrideIconValues ? this.overrideIconY : 0.0f;
    }

    public void setOverrideMainSubBlueprintIndex(Integer mainIndex) {
        if (mainIndex != null) {
            this.mainSubBlueprint = mainIndex;
        }
    }

    public void setRandomizeModelStages(boolean randomModelStage) {
        this.randomModelStages = randomModelStage;
    }

    public boolean isRandomModelStages() {
        return this.randomModelStages;
    }

    public SubBlueprint getMainSubBlueprint() {
        return this.getSubBlueprints().get(this.mainSubBlueprint);
    }

    public int getMainSubBlueprintId() {
        return this.mainSubBlueprint;
    }

    public Entity createInstance(ComponentParams ... params) {
        Entity entity = new Entity(this);
        ComponentBundle compBundle = new ComponentBundle(entity, params);
        this.createComponentInstances(entity, compBundle);
        return entity;
    }

    public Entity createInstance(ParamsBundle params) {
        Entity entity = new Entity(this);
        ComponentBundle compBundle = new ComponentBundle(entity, params);
        this.createComponentInstances(entity, compBundle);
        return entity;
    }

    public void setOverrideName(String name) {
        this.overrideName = name;
    }

    public String getName() {
        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)this.components.get((Object)ComponentType.INFO);
        if (info != null) {
            return info.getName();
        }
        if (this.overrideName != null) {
            return this.overrideName;
        }
        return "ERROR - NAME";
    }

    public Entity createInstance(BinaryReader reader, EntityLoad entities) {
        Entity entity = new Entity(this);
        try {
            entity.setId(reader.readInt());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
        ComponentBundle compBundle = new ComponentBundle(entity, entities);
        if (saveAdapter.tryAlternativeLoad(this.id, entity, this.components, compBundle, reader)) {
            return entity;
        }
        for (ComponentBlueprint componentBlueprint : this.components.values()) {
            try {
                Component component = componentBlueprint.createInstance();
                component.loadComponentTraits(componentBlueprint, reader);
                component.load(compBundle, reader);
                compBundle.addComponent(component);
            }
            catch (Exception e) {
                System.err.println("Couldn't load component!");
                e.printStackTrace();
            }
        }
        entity.setComponents(compBundle);
        return entity;
    }

    public int getId() {
        return this.id;
    }

    public boolean isAnimal() {
        return this.classification.isTypeOf(Classifier.getAnimalClassification());
    }

    public Classification getClassification() {
        return this.classification;
    }

    public Classification getSpeciesClassification() {
        return this.speciesClassification;
    }

    public float getMaxSize() {
        return Math.max(this.maxHeight, this.maxWidth);
    }

    public float getMaxHeight() {
        return this.maxHeight;
    }

    public float getMaxWidth() {
        return this.maxWidth;
    }

    public Set<ComponentType> getComponentTypes() {
        return this.components.keySet();
    }

    public Map<SpeciesInfoType, List<SpeciesInfoLine>> getInfo() {
        Map<SpeciesInfoType, List<SpeciesInfoLine>> guiComponents = this.initMap();
        this.addTierInfoLine(guiComponents);
        guiComponents.get((Object)SpeciesInfoType.GENERAL).add(new SpeciesInfoLine(CATEGORY, this.classification.getFullClassification()));
        for (ComponentBlueprint component : this.components.values()) {
            component.getInfo(guiComponents);
        }
        this.addDpInfoLine(guiComponents);
        return guiComponents;
    }

    private Map<SpeciesInfoType, List<SpeciesInfoLine>> initMap() {
        LinkedHashMap<SpeciesInfoType, List<SpeciesInfoLine>> map = new LinkedHashMap<SpeciesInfoType, List<SpeciesInfoLine>>();
        SpeciesInfoType[] speciesInfoTypeArray = SpeciesInfoType.values();
        int n = speciesInfoTypeArray.length;
        int n2 = 0;
        while (n2 < n) {
            SpeciesInfoType type = speciesInfoTypeArray[n2];
            map.put(type, new ArrayList());
            ++n2;
        }
        return map;
    }

    public void addComponent(ComponentBlueprint component) {
        this.components.put(component.getComponentType(), component);
    }

    public ComponentBlueprint getComponent(ComponentType type) {
        return this.components.get((Object)type);
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void setSecret(boolean secret) {
        this.isSecret = secret;
    }

    public boolean isSecret() {
        return this.isSecret;
    }

    public List<SubBlueprint> getSubBlueprints() {
        return this.subBlueprints;
    }

    public float[] getData() {
        float[][] allData = new float[this.subBlueprints.size()][];
        int i = 0;
        while (i < allData.length) {
            allData[i] = this.subBlueprints.get(i).getUniqueStageData();
            ++i;
        }
        return Maths.concatenateArrays(allData);
    }

    public void delete() {
        this.loaded = false;
        for (ComponentBlueprint component : this.components.values()) {
            component.delete();
        }
        GameManager.getSession().getSceneData().delete(this);
        this.subBlueprints = null;
    }

    public boolean canBeUnderwater() {
        return this.canBeUnderwater;
    }

    public boolean canBeOverwater() {
        return this.canBeOverwater;
    }

    public float getAcceptableHeightOffset() {
        return this.acceptableHeightOffset;
    }

    protected void indicateLoaded() {
        this.loaded = true;
    }

    protected void setSubBlueprints(List<SubBlueprint> subs) {
        this.subBlueprints = subs;
        this.setMaxSizes();
        this.mainSubBlueprint = subs.size() - 1;
    }

    protected void setClassification(Classification classification) {
        this.classification = classification;
        this.speciesClassification = classification.createSpeciesClassification(this.id);
    }

    protected void setWaterRequirements(boolean canUnderWater, boolean canOverWater, float offset) {
        this.canBeOverwater = canOverWater;
        this.canBeUnderwater = canUnderWater;
        this.acceptableHeightOffset = offset;
    }

    private void addDpInfoLine(Map<SpeciesInfoType, List<SpeciesInfoLine>> map) {
        InformationComponent.InformationCompBlueprint info = (InformationComponent.InformationCompBlueprint)this.getComponent(ComponentType.INFO);
        if (info.getBaseDppm() > 0) {
            SpeciesInfoLine line = new SpeciesInfoLine(DP_MESSAGE, String.valueOf(Maths.formatNumber(info.getBaseDppm())) + " dp");
            line.setOverrideColour(ColourPalette.GREEN);
            map.get((Object)SpeciesInfoType.GENERAL).add(line);
        }
    }

    private void addTierInfoLine(Map<SpeciesInfoType, List<SpeciesInfoLine>> map) {
        int tier = GameManager.BREED_TREES.getTier(this);
        if (tier > 0) {
            map.get((Object)SpeciesInfoType.GENERAL).add(new SpeciesInfoLine("Tier", new TierGui(GameManager.BREED_TREES.getTier(this))));
        }
    }

    private void setMaxSizes() {
        this.maxWidth = 0.0f;
        this.maxHeight = 0.0f;
        for (SubBlueprint sub : this.subBlueprints) {
            this.maxWidth = Math.max(sub.getAABB().getMaxWidth(), this.maxWidth);
            this.maxHeight = Math.max(sub.getAABB().getScale().y, this.maxHeight);
        }
    }

    private static void backgroundLoadBlueprint(final Blueprint blueprint, final MyFile file) {
        RequestProcessor.sendRequest(new ResourceRequest(){

            @Override
            public void doResourceRequest() {
                try {
                    BlueprintLoader.loadBlueprint(blueprint, file);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static void loadBlueprintNow(Blueprint blueprint, MyFile file) {
        try {
            BlueprintLoader.loadBlueprint(blueprint, file);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createComponentInstances(Entity entity, ComponentBundle compBundle) {
        for (ComponentBlueprint componentBlueprint : this.components.values()) {
            Component component = componentBlueprint.createInstance();
            component.createComponentTraits(componentBlueprint, compBundle);
            component.create(compBundle);
            compBundle.addComponent(component);
        }
        entity.setComponents(compBundle);
    }

    private void addStandardComponents() {
        this.addComponent(new Transformation.TransformBlueprint());
        this.addComponent(new MeshComponent.MeshCompBlueprint(this));
    }
}

