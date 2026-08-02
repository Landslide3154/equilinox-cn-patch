/*
 * Decompiled with CFR 0.152.
 */
package breeding;

import blueprints.Blueprint;
import breeding.BreedingComponent;
import breeding.EvolveProcess;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import resourceManagement.BlueprintRepository;
import utils.BinaryReader;
import utils.BinaryWriter;

public class EvolvingStatus {
    private Map<Blueprint, EvolveProcess> currentProcesses = new HashMap<Blueprint, EvolveProcess>();

    public EvolveProcess startProcess(Blueprint childSpecies, BreedingComponent parent) {
        EvolveProcess process = this.currentProcesses.get(childSpecies);
        if (process == null) {
            process = new EvolveProcess(childSpecies);
            this.currentProcesses.put(childSpecies, process);
        }
        process.continueProcess(parent);
        return process;
    }

    public void pauseProcess(Blueprint childSpecies) {
        EvolveProcess process = this.currentProcesses.get(childSpecies);
        if (process != null) {
            process.pause();
        }
    }

    public void update() {
        ArrayList<Blueprint> toRemove = new ArrayList<Blueprint>();
        for (Map.Entry<Blueprint, EvolveProcess> entry : this.currentProcesses.entrySet()) {
            boolean finished = entry.getValue().update();
            if (!finished) continue;
            toRemove.add(entry.getKey());
        }
        for (Blueprint b : toRemove) {
            this.currentProcesses.remove(b);
        }
    }

    public void save(BinaryWriter writer) throws IOException {
        writer.writeInt(this.currentProcesses.size());
        for (EvolveProcess process : this.currentProcesses.values()) {
            writer.writeInt(process.getChildSpecies().getId());
            process.save(writer);
        }
    }

    public void load(BinaryReader reader) throws Exception {
        this.reset();
        int count = reader.readInt();
        int i = 0;
        while (i < count) {
            Blueprint child = BlueprintRepository.getBlueprint(reader.readInt());
            EvolveProcess process = new EvolveProcess(child);
            process.load(reader);
            this.currentProcesses.put(child, process);
            ++i;
        }
    }

    public void reset() {
        this.currentProcesses.clear();
    }

    public EvolveProcess getProcess(Blueprint childSpecies) {
        return this.currentProcesses.get(childSpecies);
    }
}

