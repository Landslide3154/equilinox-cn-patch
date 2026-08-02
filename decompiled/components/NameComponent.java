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
import components.NamesLoader;
import entityInfoGui.EditableTextInfo;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.PopUpInfoGui;
import health.LifeComponent;
import instances.Entity;
import instances.EntityListener;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import languages.ComplexString;
import languages.GameText;
import mainGuis.EquilinoxGuis;
import mainGuis.GuiRepository;
import mainGuis.GuiSounds;
import speciesInformation.SpeciesInfoLine;
import speciesInformation.SpeciesInfoType;
import userInterfaces.Listener;
import utils.BinaryReader;
import utils.BinaryWriter;
import utils.CSVReader;

public class NameComponent
extends Component {
    private static final String TITLE = GameText.getText(658);
    private static final String NAME = GameText.getText(660);
    private static final ComplexString DESC = GameText.getComplexText(659);
    private static final int MAX_CHARS = 15;
    private String name;
    private Entity entity;
    private boolean renamed = false;

    private NameComponent(NameCompBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.name = NamesLoader.getRandomName();
        this.entity = bundle.getEntity();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.name = reader.readString();
        this.renamed = reader.readBoolean();
        this.entity = bundle.getEntity();
        if (this.renamed) {
            this.addDeathListener();
        }
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
        final EditableTextInfo textInfo = new EditableTextInfo(NAME, EntityInfoGui.FONT_SIZE, 15){

            @Override
            public String getValue() {
                return NameComponent.this.name;
            }
        };
        textInfo.addChangeListener(new Listener(){

            @Override
            public void eventOccurred(boolean on) {
                String newName = textInfo.getCurrentText();
                if (newName.length() > 0) {
                    NameComponent.this.name = textInfo.getCurrentText();
                    if (!NameComponent.this.renamed) {
                        NameComponent.this.addDeathListener();
                    }
                    NameComponent.this.renamed = true;
                    LifeComponent lifeComponent = (LifeComponent)NameComponent.this.entity.getComponent(ComponentType.LIFE);
                }
            }
        });
        info.add(textInfo);
    }

    private void addDeathListener() {
        this.entity.getNotifier().addDeadListener(new EntityListener(){

            @Override
            public void execute() {
                EquilinoxGuis.notify(TITLE, DESC.getString(NameComponent.this.name), GuiRepository.DISEASE, GuiSounds.NOTIFY);
            }
        });
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeString(this.name);
        writer.writeBoolean(this.renamed);
    }

    /* synthetic */ NameComponent(NameCompBlueprint nameCompBlueprint, NameComponent nameComponent) {
        this(nameCompBlueprint);
    }

    private static class NameCompBlueprint
    extends ComponentBlueprint {
        private NameCompBlueprint() {
            super(ComponentType.NAME);
        }

        @Override
        public Component createInstance() {
            return new NameComponent(this, null);
        }

        @Override
        public void getInfo(Map<SpeciesInfoType, List<SpeciesInfoLine>> info) {
        }

        @Override
        public void delete() {
        }
    }

    public static class NameCompLoader
    implements ComponentLoader {
        @Override
        public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
            return new NameCompBlueprint();
        }

        @Override
        public Requirement loadRequirement(CSVReader reader) {
            return null;
        }
    }
}

