/*
 * Decompiled with CFR 0.152.
 */
package materials;

import blueprints.Blueprint;
import breedingTrees.ReqInfo;
import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentLoader;
import componentArchitecture.ComponentType;
import componentArchitecture.Requirement;
import entityInfoGui.PopUpInfoGui;
import instances.Entity;
import java.util.List;
import java.util.Map;
import languages.GameText;
import materials.ColourTrait;
import materials.ColourTraitBlueprint;
import materials.NaturalColour;
import materials.NaturalColoursGui;
import materials.PresetColour;
import session.GameMode;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import toolbox.Colour;
import utils.BinaryReader;
import utils.BinaryWriter;
import utils.CSVReader;

public class MaterialComponent
extends Component {
    private static final String NAT_COL = GameText.getText(913);
    private static final int COLOUR_TRAIT_INDEX = 0;
    private Colour material;

    private MaterialComponent(MaterialCompBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.material = ((ColourTrait)this.getTrait(0)).getValue();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.material = ((ColourTrait)this.getTrait(0)).getValue();
        if (reader.getSession().getMode() == GameMode.BUILD) {
            this.material = ((ColourTrait)this.getTrait(0)).getModifier();
        }
    }

    public void forceColourUpdate() {
        this.material = ((ColourTrait)this.getTrait(0)).getRealValue();
    }

    public Colour getMaterial() {
        return this.material;
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void export(BinaryWriter writer) {
    }

    /* synthetic */ MaterialComponent(MaterialCompBlueprint materialCompBlueprint, MaterialComponent materialComponent) {
        this(materialCompBlueprint);
    }

    public static class MaterialCompBlueprint
    extends ComponentBlueprint {
        private ColourTraitBlueprint traitBlueprint;

        public MaterialCompBlueprint(ColourTraitBlueprint traitBlueprint) {
            super(ComponentType.MATERIAL);
            this.traitBlueprint = traitBlueprint;
            super.addTrait(traitBlueprint);
        }

        @Override
        public Component createInstance() {
            return new MaterialComponent(this, null);
        }

        public ColourTraitBlueprint getTrait() {
            return this.traitBlueprint;
        }

        @Override
        public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
            info.get((Object)SpeciesInfoType.GENERAL).add(new SpeciesInfoLine(NAT_COL, new NaturalColoursGui(this)));
        }

        public Colour getExampleNaturalColour() {
            return this.traitBlueprint.createRandomInstance().getValue();
        }

        @Override
        public void delete() {
        }
    }

    public static class MaterialCompLoader
    implements ComponentLoader {
        private static final float MIN = 0.15f;

        @Override
        public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
            try {
                boolean hasSecondNatural = reader.getNextLabelBool();
                int colourCount = reader.getNextLabelInt();
                NaturalColour[] colours = new NaturalColour[colourCount];
                int i = 0;
                while (i < colours.length) {
                    PresetColour preset = PresetColour.valueOf(reader.getNextString());
                    Colour col = preset.getColour();
                    String name = preset.getName();
                    if (preset == PresetColour.CUSTOM) {
                        col = new Colour(reader.getNextVector());
                        name = i == 0 ? String.valueOf(NAT_COL) + (hasSecondNatural ? " 1" : "") : (hasSecondNatural && i == 1 ? String.valueOf(NAT_COL) + " 2" : null);
                    }
                    int price = reader.getNextInt();
                    colours[i] = new NaturalColour(col, price, name);
                    ++i;
                }
                ColourTraitBlueprint traitBlueprint = new ColourTraitBlueprint(colours, hasSecondNatural, blueprint);
                return new MaterialCompBlueprint(traitBlueprint);
            }
            catch (Exception hasSecondNatural) {
                NaturalColour[] colours = new NaturalColour[]{new NaturalColour(new Colour(1.0f, 0.0f, 0.0f), 100, "error")};
                ColourTraitBlueprint traitBlueprint = new ColourTraitBlueprint(colours, false, blueprint);
                return new MaterialCompBlueprint(traitBlueprint);
            }
        }

        @Override
        public Requirement loadRequirement(CSVReader reader) {
            final String text = GameText.getText(251);
            final PresetColour target = PresetColour.valueOf(reader.getNextLabelString());
            return new Requirement(){

                @Override
                public boolean check(Entity entity) {
                    Colour currentCol = ((MaterialComponent)entity.getComponent(ComponentType.MATERIAL)).material;
                    float diff = Colour.calculateDifference(currentCol, target.getColour());
                    return diff < 0.15f;
                }

                @Override
                public void getGuiInfo(List<ReqInfo> components) {
                    components.add(new ReqInfo(text, target.getName(), target.getColour()));
                }

                @Override
                public boolean isSecret() {
                    return false;
                }
            };
        }
    }
}

