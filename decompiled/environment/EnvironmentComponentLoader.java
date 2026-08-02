/*
 * Decompiled with CFR 0.152.
 */
package environment;

import biomes.Biome;
import breedingTrees.ReqInfo;
import classification.Classification;
import classification.Classifier;
import componentArchitecture.ComponentType;
import componentArchitecture.Requirement;
import components.InformationComponent;
import entityBundle.EntityBundle;
import environment.AltitudeFactor;
import environment.DislikedSpeciesFactor;
import environment.EnviroCompBlueprint;
import environment.EnviroFactorBlueprint;
import environment.FaveBiomeFactor;
import environment.LikedSpeciesFactor;
import environment.PreferredBiomeFactor;
import environment.UnsuitableBiomeFactor;
import gameManaging.GameManager;
import health.LifeComponent;
import instances.Entity;
import java.util.ArrayList;
import java.util.List;
import languages.GameText;
import materials.MaterialComponent;
import materials.PresetColour;
import org.lwjgl.util.vector.Vector3f;
import terrains.TerrainVertex;
import toolbox.Colour;
import toolbox.Transformation;
import utils.CSVReader;

public class EnvironmentComponentLoader {
    private static final String NEARBY_SPECIES = GameText.getText(223);
    private static final String SATISFACTION = GameText.getText(921);
    private static final String ALTITUDE = GameText.getText(155);
    private static final String ABOVE = GameText.getText(923);
    private static final String BELOW = GameText.getText(922);
    private static final String BETWEEN = GameText.getText(925);
    private static final String AND = GameText.getText(924);
    private static final int ENTITY_NEAR_ID = 0;
    private static final int BIOME_ID = 1;
    private static final int BAD_BIOME_ID = 4;
    private static final int SATISFIED_ID = 2;
    private static final int ALTITUDE_ID = 3;
    private static final int COLOUR_NEAR_ID = 6;
    private static final int THREE_SEARCH_ID = 44;

    public static EnviroCompBlueprint loadEnviroBlueprint(CSVReader reader) {
        ArrayList<EnviroFactorBlueprint> factors = new ArrayList<EnviroFactorBlueprint>();
        int count = reader.getNextLabelInt();
        boolean hasPreferred = false;
        UnsuitableBiomeFactor.UnsuitableBiomeFactorBlueprint badBiomeFactor = null;
        int i = 0;
        while (i < count) {
            EnviroFactorBlueprint factor = EnvironmentComponentLoader.loadFactor(reader, hasPreferred);
            if (factor instanceof PreferredBiomeFactor.PreferredBiomeFactorBlueprint) {
                if (badBiomeFactor != null) {
                    badBiomeFactor.setHasPreferred();
                } else {
                    hasPreferred = true;
                }
            } else if (factor instanceof UnsuitableBiomeFactor.UnsuitableBiomeFactorBlueprint) {
                badBiomeFactor = (UnsuitableBiomeFactor.UnsuitableBiomeFactorBlueprint)factor;
            }
            factors.add(factor);
            ++i;
        }
        return new EnviroCompBlueprint(factors);
    }

    private static EnviroFactorBlueprint loadFactor(CSVReader reader, boolean hasPreferred) {
        int id = reader.getNextLabelInt();
        if (id == 1) {
            return AltitudeFactor.AltitudeFactorBlueprint.loadAltitudeFactor(reader);
        }
        if (id == 2) {
            return PreferredBiomeFactor.PreferredBiomeFactorBlueprint.loadSuitableBiomeFactor(reader);
        }
        if (id == 3) {
            UnsuitableBiomeFactor.UnsuitableBiomeFactorBlueprint factor = UnsuitableBiomeFactor.UnsuitableBiomeFactorBlueprint.loadUnsuitableBiomeFactor(reader);
            if (hasPreferred) {
                factor.setHasPreferred();
            }
            return factor;
        }
        if (id == 4) {
            return FaveBiomeFactor.FaveBiomeFactorBlueprint.loadFaveBiomeFactor(reader);
        }
        if (id == 5) {
            return LikedSpeciesFactor.LikedSpeciesFactorBlueprint.loadLikedSpeciesFactor(reader);
        }
        if (id == 6) {
            return DislikedSpeciesFactor.DislikedSpeciesFactorBlueprint.loadDisikedSpeciesFactor(reader);
        }
        return null;
    }

    public static Requirement loadRequirement(CSVReader reader) {
        int id = reader.getNextLabelInt();
        if (id == 0) {
            final Classification classification = Classifier.getClassification(reader.getNextLabelString());
            final int count = reader.getNextLabelInt();
            return new Requirement(){

                @Override
                public boolean check(Entity entity) {
                    EntityBundle bundle;
                    InformationComponent info = (InformationComponent)entity.getComponent(ComponentType.INFO);
                    boolean animal = entity.getBlueprint().isAnimal();
                    Vector3f pos = animal ? info.getBasePosition() : entity.getTransform().getPosition();
                    int range = info.getRoamingRange();
                    if (animal && range <= 2) {
                        range = 4;
                    }
                    if ((bundle = GameManager.getWorld().getListOfSpecies(classification, range, pos.x, pos.z)) == null) {
                        return false;
                    }
                    return bundle.getSize() >= count;
                }

                @Override
                public void getGuiInfo(List<ReqInfo> components) {
                    components.add(new ReqInfo(NEARBY_SPECIES, String.valueOf(classification.getName()) + " (x" + count + ")"));
                }

                @Override
                public boolean isSecret() {
                    return false;
                }
            };
        }
        if (id == 1) {
            final Biome biome = Biome.valueOf(reader.getNextLabelString());
            final float target = reader.getNextLabelFloat();
            return new Requirement(){

                @Override
                public boolean check(Entity entity) {
                    InformationComponent info = (InformationComponent)entity.getComponent(ComponentType.INFO);
                    Vector3f pos = entity.getBlueprint().getClassification().isTypeOf(Classifier.getPlantClassification()) ? entity.getTransform().getPosition() : info.getBasePosition();
                    TerrainVertex terrainVertex = GameManager.getWorld().getTerrainVertex(pos.x, pos.z);
                    float amount = terrainVertex.getBiomeDecimal(biome);
                    return amount >= target;
                }

                @Override
                public void getGuiInfo(List<ReqInfo> components) {
                    components.add(new ReqInfo(GameText.getText(246), ">" + (int)(target * 100.0f) + "% " + biome.toString()));
                }

                @Override
                public boolean isSecret() {
                    return false;
                }
            };
        }
        if (id == 4) {
            final Biome biome = Biome.valueOf(reader.getNextLabelString());
            float target = reader.getNextLabelFloat();
            return new Requirement(){

                @Override
                public boolean check(Entity entity) {
                    InformationComponent info = (InformationComponent)entity.getComponent(ComponentType.INFO);
                    Vector3f pos = entity.getBlueprint().getClassification().isTypeOf(Classifier.getPlantClassification()) ? entity.getTransform().getPosition() : info.getBasePosition();
                    TerrainVertex terrainVertex = GameManager.getWorld().getTerrainVertex(pos.x, pos.z);
                    float amount = terrainVertex.getBiomeDecimal(biome);
                    return amount <= 0.08f;
                }

                @Override
                public void getGuiInfo(List<ReqInfo> components) {
                    components.add(new ReqInfo(GameText.getText(246), "Not " + biome.toString()));
                }

                @Override
                public boolean isSecret() {
                    return false;
                }
            };
        }
        if (id == 2) {
            final float target = reader.getNextLabelFloat();
            return new Requirement(){

                @Override
                public boolean check(Entity entity) {
                    LifeComponent lifeComp = (LifeComponent)entity.getComponent(ComponentType.LIFE);
                    float amount = lifeComp.getEnvironmentalSatisfaction();
                    return amount >= target;
                }

                @Override
                public void getGuiInfo(List<ReqInfo> components) {
                    components.add(new ReqInfo(SATISFACTION, ">" + (int)(target * 100.0f) + "% "));
                }

                @Override
                public boolean isSecret() {
                    return false;
                }
            };
        }
        if (id == 3) {
            final int minAlt = reader.getNextLabelInt();
            final int maxAlt = reader.getNextLabelInt();
            return new Requirement(){

                @Override
                public boolean check(Entity entity) {
                    Transformation transform = entity.getTransform();
                    float altitude = GameManager.getWorld().getAltitude(transform.getPosition().y);
                    return altitude >= (float)minAlt && altitude <= (float)maxAlt;
                }

                @Override
                public void getGuiInfo(List<ReqInfo> components) {
                    if (minAlt == 0) {
                        components.add(new ReqInfo(ALTITUDE, String.valueOf(BELOW) + " " + maxAlt + "m"));
                    } else if (maxAlt == 100) {
                        components.add(new ReqInfo(ALTITUDE, String.valueOf(ABOVE) + " " + minAlt + "m"));
                    } else if (maxAlt == 0) {
                        components.add(new ReqInfo(ALTITUDE, String.valueOf(ABOVE) + " " + minAlt + "m"));
                    } else if (minAlt == -100) {
                        components.add(new ReqInfo(ALTITUDE, String.valueOf(BELOW) + " " + maxAlt + "m"));
                    } else {
                        components.add(new ReqInfo(ALTITUDE, String.valueOf(BETWEEN) + " " + minAlt + "m " + AND + " " + maxAlt + "m"));
                    }
                }

                @Override
                public boolean isSecret() {
                    return false;
                }
            };
        }
        if (id == 44) {
            final Classification classification = Classifier.getClassification(reader.getNextLabelString());
            final int count = reader.getNextLabelInt();
            return new Requirement(){

                @Override
                public boolean check(Entity entity) {
                    InformationComponent info = (InformationComponent)entity.getComponent(ComponentType.INFO);
                    Vector3f pos = entity.getBlueprint().getClassification().isTypeOf(Classifier.getPlantClassification()) ? entity.getTransform().getPosition() : info.getBasePosition();
                    EntityBundle bundle = GameManager.getWorld().getListOfSpecies(classification, 4, pos.x, pos.z);
                    if (bundle == null) {
                        return false;
                    }
                    return bundle.getSize() >= count;
                }

                @Override
                public void getGuiInfo(List<ReqInfo> components) {
                    components.add(new ReqInfo(NEARBY_SPECIES, String.valueOf(classification.getName()) + " (x" + count + ")"));
                }

                @Override
                public boolean isSecret() {
                    return false;
                }
            };
        }
        if (id == 6) {
            final Classification classification = Classifier.getClassification(reader.getNextLabelString());
            final PresetColour target = PresetColour.valueOf(reader.getNextLabelString());
            return new Requirement(){

                @Override
                public boolean check(Entity entity) {
                    EntityBundle bundle;
                    InformationComponent info = (InformationComponent)entity.getComponent(ComponentType.INFO);
                    boolean animal = entity.getBlueprint().isAnimal();
                    Vector3f pos = animal ? info.getBasePosition() : entity.getTransform().getPosition();
                    int range = info.getRoamingRange();
                    if (range <= 2) {
                        range = 4;
                    }
                    if ((bundle = GameManager.getWorld().getListOfSpecies(classification, range, pos.x, pos.z)) == null) {
                        return false;
                    }
                    for (Entity goodEntity : bundle) {
                        MaterialComponent matComp = (MaterialComponent)goodEntity.getComponent(ComponentType.MATERIAL);
                        float diff = Colour.calculateDifference(matComp.getMaterial(), target.getColour());
                        if (!(diff < 0.15f)) continue;
                        return true;
                    }
                    return false;
                }

                @Override
                public void getGuiInfo(List<ReqInfo> components) {
                    components.add(new ReqInfo(NEARBY_SPECIES, String.valueOf(classification.getName()) + " (" + target.getName() + ")", target.getColour()));
                }

                @Override
                public boolean isSecret() {
                    return false;
                }
            };
        }
        return null;
    }
}

