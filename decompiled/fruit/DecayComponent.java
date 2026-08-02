/*
 * Decompiled with CFR 0.152.
 */
package fruit;

import componentArchitecture.Action;
import componentArchitecture.Component;
import componentArchitecture.ComponentBundle;
import componentArchitecture.ComponentType;
import death.FadeDeath;
import entityInfoGui.PopUpInfoGui;
import fruit.DecayCompBlueprint;
import fruit.DecayParams;
import gameManaging.GameManager;
import instances.Entity;
import java.io.IOException;
import java.util.List;
import org.lwjgl.util.vector.Vector3f;
import toolbox.Maths;
import utils.BinaryReader;
import utils.BinaryWriter;

public class DecayComponent
extends Component {
    private static final float FADE_TIME = 0.2f;
    private static final int BOUNCE_COUNT = 7;
    private static final float BOUNCE_FACTOR = 0.5f;
    private final DecayCompBlueprint blueprint;
    private Entity entity;
    private float hoursAlive = 0.0f;
    private boolean onFloor = false;
    private Vector3f velocity;
    private int bounce = 0;

    protected DecayComponent(DecayCompBlueprint blueprint) {
        super(blueprint);
        this.blueprint = blueprint;
        this.velocity = Maths.generateRandomUnitVector();
    }

    @Override
    public void update() {
        if (!this.onFloor) {
            this.move();
            if (this.entity.getTransform().getPosition().y < -20.0f) {
                this.entity.die(new FadeDeath(0.2f, this.entity), false);
            }
        }
        this.hoursAlive += GameManager.getDeltaHours();
        if (this.hoursAlive >= this.blueprint.lifeInHours) {
            this.entity.die(new FadeDeath(0.2f, this.entity), false);
        }
    }

    @Override
    public void getStatusInfo(List<PopUpInfoGui> info) {
    }

    @Override
    public void getActions(List<Action> actions) {
    }

    @Override
    public void export(BinaryWriter writer) throws IOException {
        writer.writeFloat(this.hoursAlive);
    }

    @Override
    public void create(ComponentBundle bundle) {
        this.entity = bundle.getEntity();
        DecayParams params = (DecayParams)bundle.getParameters(ComponentType.DECAY);
        if (params != null) {
            this.velocity = params.getInitialVelcoity();
        }
    }

    @Override
    public void load(ComponentBundle bundle, BinaryReader reader) throws Exception {
        this.create(bundle);
        this.hoursAlive = reader.readFloat();
    }

    private void move() {
        this.velocity.y -= GameManager.getGameSeconds() * 10.0f;
        Vector3f change = new Vector3f(this.velocity);
        change.scale(GameManager.getGameSeconds());
        Vector3f res = new Vector3f();
        Vector3f.add(change, this.entity.getTransform().getPosition(), res);
        this.entity.getTransform().setPosition(res);
        float height = GameManager.getWorld().isOnWorld(res) ? GameManager.getWorld().getHeightOfTerrain(res.x, res.z) : -50.0f;
        if (height >= res.y && this.velocity.y < 0.0f) {
            this.entity.getTransform().getPosition().setY(height);
            ++this.bounce;
            if (this.bounce >= 7) {
                this.onFloor = true;
            }
            this.velocity.scale(0.5f);
            this.velocity.y = -this.velocity.y;
        }
        GameManager.getWorld().getEntityGrid().updateInGrid(this.entity);
    }
}

