/*
 * Decompiled with CFR 0.152.
 */
package components;

import basics.DisplayManager;
import blueprints.Blueprint;
import classification.Classification;
import componentArchitecture.ComponentType;
import gameManaging.GameManager;
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import resourceManagement.BlueprintRepository;
import session.GameMode;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;

public class Mutator {
    private static final float START_MIN = 900.0f;
    private static final float START_MAX = 1800.0f;
    private static final float USUAL_MIN = 600.0f;
    private static final float USUAL_MAX = 3000.0f;
    private static float nextMutationTime;
    private static boolean mutationReady;
    private static Classification species;

    static {
        mutationReady = false;
    }

    public static void reset() {
        mutationReady = false;
        nextMutationTime = Maths.randomNumberBetween(900.0f, 1800.0f);
    }

    public static void load(BinaryReader reader) throws Exception {
        mutationReady = false;
        nextMutationTime = reader.readFloat();
        System.out.println("Next mutation in " + nextMutationTime);
    }

    public static void export(BinaryWriter writer) throws IOException {
        writer.writeFloat(nextMutationTime);
    }

    public static boolean testForMutation(Blueprint blueprint) {
        if (!mutationReady || GameManager.getGameMode() != GameMode.NORMAL) {
            return false;
        }
        if (blueprint.getSpeciesClassification().isTypeOf(species)) {
            mutationReady = false;
            return true;
        }
        return false;
    }

    public static void update() {
        if (GameManager.getGameMode() != GameMode.NORMAL) {
            return;
        }
        if ((nextMutationTime -= DisplayManager.getDeltaSeconds()) < 0.0f) {
            mutationReady = true;
            Mutator.chooseRandomSpecies();
            nextMutationTime = Maths.randomNumberBetween(600.0f, 3000.0f);
        }
    }

    private static void chooseRandomSpecies() {
        Blueprint blueprint = null;
        int count = 0;
        while (!Mutator.isAcceptableSpecies(blueprint = Mutator.chooseSpecies()) && ++count < 5) {
        }
        species = blueprint.getSpeciesClassification();
    }

    private static Blueprint chooseSpecies() {
        Set<Integer> unlockedSpecies = GameManager.getSession().getStats().getLockStatus().getUnlockedSpecies();
        int index = Maths.RANDOM.nextInt(unlockedSpecies.size());
        Iterator<Integer> iterator = unlockedSpecies.iterator();
        int i = 0;
        while (i < index) {
            iterator.next();
            ++i;
        }
        int blueprintID = iterator.next();
        return BlueprintRepository.getBlueprint(blueprintID);
    }

    private static boolean isAcceptableSpecies(Blueprint blueprint) {
        int number = GameManager.getWorld().getEntityGrid().getSortedEntities().getEntityCount(blueprint.getSpeciesClassification());
        if (number <= 0) {
            return false;
        }
        return blueprint.getComponent(ComponentType.MATERIAL) != null;
    }
}

