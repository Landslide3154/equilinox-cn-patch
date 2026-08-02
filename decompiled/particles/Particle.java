/*
 * Decompiled with CFR 0.152.
 */
package particles;

import basics.CameraInterface;
import gameManaging.GameManager;
import objectPools.Vec3Pool;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import particles.ParticleMaster;
import particles.ParticleTexture;
import toolbox.Colour;
import toolbox.Maths;
import toolbox.Transformation;

public class Particle {
    private Vector3f position;
    private Vector3f velocity;
    private float gravityEffect;
    private float lifeLength;
    private float rotation;
    private float scale;
    private float fadeIn = 0.0f;
    private float fadeOut = 1.0f;
    private float normalAlpha = 1.0f;
    private Colour colour;
    private boolean rotate3D = false;
    private float rotX = 0.0f;
    private float rotXSpeed = 0.0f;
    private float elapsedTime = 0.0f;
    private ParticleTexture texture;
    private Vector2f texOffset1 = new Vector2f();
    private Vector2f texOffset2 = new Vector2f();
    private float blend;
    private float distance;
    private boolean manualStages = false;
    private boolean decays = true;
    private Transformation parent = null;
    private float heightOffset = 0.0f;
    private static final Vector3f VEC = new Vector3f();

    public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        this.texture = texture;
        ParticleMaster.addParticle(this);
    }

    public Particle(Colour colour, boolean additive, Vector3f position, Vector3f velocity, float gravityEffect, float lifeLength, float rotation, float scale) {
        this.position = position;
        this.velocity = velocity;
        this.gravityEffect = gravityEffect;
        this.lifeLength = lifeLength;
        this.rotation = rotation;
        this.scale = scale;
        this.colour = colour;
        ParticleMaster.addColourParticle(this, additive);
    }

    public Particle(ParticleTexture texture, Vector3f position, float scale, float deathAnimationTime) {
        this.decays = false;
        this.position = new Vector3f(position);
        this.velocity = new Vector3f();
        this.gravityEffect = 0.0f;
        this.lifeLength = deathAnimationTime;
        this.rotation = 0.0f;
        this.scale = scale;
        this.texture = texture;
        ParticleMaster.addParticle(this);
    }

    public Particle(ParticleTexture texture, float scale, float deathAnimationTime, Transformation transform, float heightOffset) {
        this.decays = false;
        this.position = new Vector3f();
        this.velocity = new Vector3f();
        this.gravityEffect = 0.0f;
        this.lifeLength = deathAnimationTime;
        this.rotation = 0.0f;
        this.scale = scale;
        this.texture = texture;
        this.parent = transform;
        this.heightOffset = heightOffset;
        ParticleMaster.addParticle(this);
    }

    public void setHeightOffset(float offset) {
        this.heightOffset = offset;
    }

    public void set3dRotation(float speed) {
        this.rotate3D = true;
        this.rotX = Maths.RANDOM.nextFloat() * 360.0f;
        this.rotXSpeed = speed;
    }

    public void setPosition(Vector3f pos) {
        this.position.set(pos);
    }

    public void kill() {
        this.decays = true;
    }

    public void setFade(float normalAlpha, float fadeIn, float fadeOut) {
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
        this.normalAlpha = normalAlpha;
    }

    public void setManualStages(boolean manual) {
        this.manualStages = manual;
    }

    public void setStage(int index) {
        this.manualStages = true;
        this.setTextureOffset(this.texOffset1, index);
        this.setTextureOffset(this.texOffset2, index);
    }

    public void setStages(int prevIndex, int nextIndex, float blend) {
        this.manualStages = true;
        this.setTextureOffset(this.texOffset1, prevIndex);
        this.setTextureOffset(this.texOffset2, nextIndex);
        this.blend = blend;
    }

    public Colour getColour() {
        return this.colour;
    }

    public float getTransparency() {
        float lifeFactor = this.elapsedTime / this.lifeLength;
        if (lifeFactor < this.fadeIn) {
            float factor = lifeFactor / this.fadeIn;
            return factor * this.normalAlpha;
        }
        if (lifeFactor > this.fadeOut) {
            float factor = 1.0f - (lifeFactor - this.fadeOut) / (1.0f - this.fadeOut);
            return factor * this.normalAlpha;
        }
        return 1.0f;
    }

    protected float getDistance() {
        return 0.0f;
    }

    protected Vector2f getTexOffset1() {
        return this.texOffset1;
    }

    protected Vector2f getTexOffset2() {
        return this.texOffset2;
    }

    protected float getBlend() {
        return this.blend;
    }

    protected ParticleTexture getTexture() {
        return this.texture;
    }

    protected Vector3f getPosition() {
        return this.position;
    }

    protected float getRotation() {
        return this.rotation;
    }

    protected float getRotX() {
        return this.rotX;
    }

    protected float getScale() {
        return this.scale;
    }

    protected boolean update(CameraInterface camera) {
        if (this.parent == null) {
            this.moveParticleNaturally();
        } else {
            this.followParent();
        }
        this.rotate();
        if (this.colour == null && !this.manualStages) {
            this.updateTextureCoordInfo();
        }
        Vector3f temp = Vec3Pool.get();
        this.distance = Vector3f.sub(camera.getPosition(), this.position, temp).lengthSquared();
        Vec3Pool.release(temp);
        if (this.decays) {
            this.elapsedTime += GameManager.getGameSeconds();
        }
        return !this.decays || this.elapsedTime < this.lifeLength;
    }

    private void moveParticleNaturally() {
        this.velocity.y += -10.0f * this.gravityEffect * GameManager.getGameSeconds();
        VEC.set(this.velocity);
        VEC.scale(GameManager.getGameSeconds());
        Vector3f.add(VEC, this.position, this.position);
    }

    private void rotate() {
        if (this.rotate3D) {
            this.rotX += GameManager.getGameSeconds() * this.rotXSpeed;
        }
    }

    private void followParent() {
        Vector3f parentPos = this.parent.getPosition();
        this.position.set(parentPos.x, parentPos.y + this.heightOffset, parentPos.z);
    }

    private void updateTextureCoordInfo() {
        float lifeFactor = this.elapsedTime / this.lifeLength;
        int stageCount = this.texture.getNumberOfRows() * this.texture.getNumberOfRows();
        float atlasProgression = lifeFactor * (float)stageCount;
        int index1 = (int)Math.floor(atlasProgression);
        int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
        this.blend = atlasProgression % 1.0f;
        this.setTextureOffset(this.texOffset1, index1);
        this.setTextureOffset(this.texOffset2, index2);
    }

    private void setTextureOffset(Vector2f offset, int index) {
        int column = index % this.texture.getNumberOfRows();
        int row = index / this.texture.getNumberOfRows();
        offset.x = (float)column / (float)this.texture.getNumberOfRows();
        offset.y = (float)row / (float)this.texture.getNumberOfRows();
    }
}

