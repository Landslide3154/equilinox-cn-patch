/*
 * Decompiled with CFR 0.152.
 */
package nightBloom;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import components.MeshComponent;
import entityInfoGui.EntityInfoGui;
import entityInfoGui.PopUpInfoGui;
import entityInfoGui.TextInfo;
import events.EventData;
import events.EventManager;
import gameManaging.GameManager;
import growth.GrowthComponent;
import instances.Entity;
import java.io.IOException;
import java.util.List;
import languages.GameText;
import org.lwjgl.util.vector.Vector3f;
import particleSpawns.SphereSpawn;
import particles.ParticleSystem;
import particles.ParticleTexture;
import resourceManagement.ParticleAtlasCache;
import time.Calendar;
import toolbox.Maths;
import toolbox.Transformation;
import utils.BinaryReader;
import utils.BinaryWriter;

public class BloomComponent
extends Component {
    private static final ParticleSystem particles1 = BloomComponent.createParticleSystem1();
    private static final String NEXT_BLOOM = GameText.getText(1158);
    private static final String BLOOMING = GameText.getText(1159);
    private static final String DAYS = GameText.getText(1160);
    private static final float END_TIME = 0.2f;
    private static final float RANGE = 65.0f;
    private static final int PREPARE_STAGE = 3;
    private static final int BLOOM_STAGE = 4;
    private static final int PREPARE_DAY = 10;
    private GrowthComponent growth;
    private MeshComponent mesh;
    private Transformation transform;
    private Entity entity;
    private BloomState state = BloomState.NORMAL;
    private float timer;

    protected BloomComponent(ComponentBlueprint blueprint) {
        super(blueprint);
    }

    @Override
    public void update() {
        super.update();
        if (!this.growth.isFullyGrown()) {
            return;
        }
        if (this.state == BloomState.BLOOM) {
            this.updateBloom();
        } else if (this.state == BloomState.PREPARING) {
            this.updatePrepare();
        } else {
            this.updateNormal();
        }
    }

    private void updateNormal() {
        int day = GameManager.getSession().getStats().getCalendar().getDay();
        if (day == 10) {
            this.prepare();
        }
    }

    private void updatePrepare() {
        if (this.checkTimer()) {
            this.bloom();
        }
    }

    private void updateBloom() {
        int day = GameManager.getSession().getStats().getCalendar().getDay();
        if (day == 1 && GameManager.getSession().getStats().getCalendar().getRawTime() < 0.2f && this.entity.getCurrentGridSection().getDistanceFromCam() < 65.0f) {
            Vector3f pos = new Vector3f(this.transform.getPosition());
            pos.y += 0.5f;
            particles1.generateParticles(pos, 1.0f);
        }
        if (this.checkTimer()) {
            this.endBloom();
        }
    }

    private void bloom() {
        this.timer = 720.0f;
        this.state = BloomState.BLOOM;
        this.mesh.updateModelStage(4);
        EventManager.BLOOMING.registerEvent(new EventData(), new String[0]);
    }

    private void prepare() {
        this.timer = 720.0f;
        this.state = BloomState.PREPARING;
        this.mesh.updateModelStage(3);
    }

    private void endBloom() {
        this.state = BloomState.NORMAL;
        this.mesh.updateModelStage(2);
    }

    private boolean checkTimer() {
        this.timer -= GameManager.getGameSeconds();
        return this.timer < 0.0f;
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
        if (!this.growth.isFullyGrown() || this.state == BloomState.BLOOM) {
            return;
        }
        info.add(new TextInfo(NEXT_BLOOM, EntityInfoGui.FONT_SIZE){

            @Override
            public String getValue() {
                if (BloomComponent.this.state == BloomState.BLOOM) {
                    return BLOOMING;
                }
                if (BloomComponent.this.state == BloomState.PREPARING) {
                    return Calendar.formatTimeSeconds(BloomComponent.this.timer);
                }
                int day = GameManager.getSession().getStats().getCalendar().getDay();
                return String.valueOf(11 - day) + " " + DAYS;
            }
        });
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeInt(this.state.ordinal());
        writer.writeFloat(this.timer);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.growth = (GrowthComponent)bundle.getComponent(ComponentType.GROWTH);
        this.mesh = (MeshComponent)bundle.getComponent(ComponentType.MESH);
        this.transform = (Transformation)bundle.getComponent(ComponentType.TRANSFORM);
        this.entity = bundle.getEntity();
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
        this.state = BloomState.values()[reader.readInt()];
        this.timer = reader.readFloat();
    }

    private static ParticleSystem createParticleSystem1() {
        ParticleTexture atlas = ParticleAtlasCache.getAtlas(19);
        ParticleSystem system = new ParticleSystem(atlas, new SphereSpawn(0.5f), 10.0f, 1.0f, 0.02f, 10.0f, 0.07f);
        system.setDirection(Maths.UP, 0.4f);
        system.setScaleError(0.5f);
        system.setSpeedError(0.25f);
        system.setLifeError(0.4f);
        system.randomizeRotation();
        return system;
    }

    private static enum BloomState {
        NORMAL,
        PREPARING,
        BLOOM;

    }
}

