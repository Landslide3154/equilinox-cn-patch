/*
 * Decompiled with CFR 0.152.
 */
package components;

import blueprints.Blueprint;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentLoader;
import componentArchitecture.ComponentType;
import componentArchitecture.Requirement;
import entityInfoGui.PopUpInfoGui;
import gameManaging.GameManager;
import java.util.List;
import java.util.Map;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import toolbox.Maths;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;
import utils.CSVReader;

public class LilyComponent
extends Component {
    private Transformation transform;
    private float changeTime = Maths.RANDOM.nextFloat();

    protected LilyComponent(ComponentBlueprint blueprint) {
        super(blueprint);
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
    public void update() {
        this.changeTime += GameManager.getGameSeconds() * 0.3f;
        float offset = (float)(Math.sin(Math.PI * 2 * (double)this.changeTime) * (double)0.04f);
        this.transform.setYPosition(GameManager.getWorld().getWaterHeight() + offset);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
    }

    public static class LilyCompBlueprint
    extends ComponentBlueprint {
        protected LilyCompBlueprint() {
            super(ComponentType.LILY);
        }

        @Override
        public Component createInstance() {
            return new LilyComponent(this);
        }

        @Override
        public void delete() {
        }

        @Override
        public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        }
    }

    public static class LilyCompLoader
    implements ComponentLoader {
        @Override
        public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
            return new LilyCompBlueprint();
        }

        @Override
        public Requirement loadRequirement(CSVReader reader) {
            return null;
        }
    }
}

