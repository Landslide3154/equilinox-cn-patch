/*
 * Decompiled with CFR 0.152.
 */
package particles;

import gameManaging.GameManager;
import java.util.Random;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;
import particleSpawns.ParticleSpawn;
import particles.Particle;
import particles.ParticleTexture;
import toolbox.Colour;

public class ParticleSystem {
    private float pps;
    private float averageSpeed;
    private float gravityComplient;
    private float averageLifeLength;
    private float averageScale;
    private float speedError;
    private float lifeError;
    private float scaleError = 0.0f;
    private boolean randomRotation = false;
    private Vector4f direction;
    private Vector4f offset = new Vector4f();
    private float directionDeviation = 0.0f;
    private ParticleTexture texture;
    private ParticleSpawn spawn;
    private Colour colour;
    private boolean additive;
    private float alpha = 1.0f;
    private float fadeIn = 0.0f;
    private float fadeOut = 1.0f;
    private boolean hasXRotation = false;
    private float xRotSpeed = 0.0f;
    private boolean directionLocalSpace = false;
    private Matrix4f transformation = new Matrix4f();
    private Random random = new Random();

    public ParticleSystem(ParticleTexture texture, ParticleSpawn spawn, float pps, float speed, float gravityComplient, float lifeLength, float scale) {
        this.spawn = spawn;
        this.pps = pps;
        this.averageSpeed = speed;
        this.gravityComplient = gravityComplient;
        this.averageLifeLength = lifeLength;
        this.averageScale = scale;
        this.texture = texture;
    }

    public ParticleSystem(Colour colour, boolean additive, ParticleSpawn spawn, float pps, float speed, float gravityComplient, float lifeLength, float scale) {
        this.colour = colour;
        this.additive = additive;
        this.spawn = spawn;
        this.pps = pps;
        this.averageSpeed = speed;
        this.gravityComplient = gravityComplient;
        this.averageLifeLength = lifeLength;
        this.averageScale = scale;
    }

    public void setDirection(Vector3f direction, float deviation) {
        this.direction = new Vector4f(direction.x, direction.y, direction.z, 0.0f);
        this.directionDeviation = (float)((double)deviation * Math.PI);
    }

    public void setDirectionLocalSpace() {
        this.directionLocalSpace = true;
    }

    public void setFadeValues(float alpha, float fadeIn, float fadeOut) {
        this.alpha = alpha;
        this.fadeIn = fadeIn;
        this.fadeOut = fadeOut;
    }

    public void setOffset(Vector3f off) {
        this.offset.set(off.x, off.y, off.z, 0.0f);
    }

    public void setXRotation(float speed) {
        this.hasXRotation = true;
        this.xRotSpeed = speed;
    }

    public void randomizeRotation() {
        this.randomRotation = true;
    }

    public void setSpeedError(float error) {
        this.speedError = error * this.averageSpeed;
    }

    public void setLifeError(float error) {
        this.lifeError = error * this.averageLifeLength;
    }

    public void setScaleError(float error) {
        this.scaleError = error * this.averageScale;
    }

    public void pulseParticles(Vector3f center, float scale) {
        this.pulseParticles(this.translateMatrix(center), scale);
    }

    public void pulseParticles(Vector3f center, Colour colour, float scale) {
        this.pulseParticles(this.translateMatrix(center), colour, scale);
    }

    public void generateParticles(Vector3f center, float scale) {
        this.generateParticles(this.translateMatrix(center), scale);
    }

    public void generateParticles(Vector3f center, Colour colour, float scale) {
        this.generateParticles(this.translateMatrix(center), colour, scale);
    }

    private Matrix4f translateMatrix(Vector3f center) {
        this.transformation.setIdentity();
        this.transformation.translate(center);
        return this.transformation;
    }

    public void pulseParticles(Matrix4f transform, float scale) {
        this.pulseParticles(transform, null, scale);
    }

    public void pulseParticles(Matrix4f transform, Colour colour, float scale) {
        int i = 0;
        while ((float)i < this.pps) {
            this.emitParticle(transform, colour, scale);
            ++i;
        }
    }

    public void generateParticles(Matrix4f transform, float scale) {
        this.generateParticles(transform, null, scale);
    }

    public void generateParticles(Matrix4f transform, Colour colour, float scale) {
        float delta = GameManager.getGameSeconds();
        float particlesToCreate = this.pps * delta;
        int count = (int)Math.floor(particlesToCreate);
        float partialParticle = particlesToCreate % 1.0f;
        int i = 0;
        while (i < count) {
            this.emitParticle(transform, colour, scale);
            ++i;
        }
        if (Math.random() < (double)partialParticle) {
            this.emitParticle(transform, colour, scale);
        }
    }

    private void emitParticle(Matrix4f transform, Colour overrideColour, float scaleFactor) {
        Vector3f velocity = null;
        if (this.direction != null) {
            Vector4f actualDir = this.directionLocalSpace ? Matrix4f.transform(transform, this.direction, null) : this.direction;
            velocity = ParticleSystem.generateRandomUnitVectorWithinCone(new Vector3f(actualDir), this.directionDeviation);
        } else {
            velocity = this.generateRandomUnitVector();
        }
        velocity.normalise();
        velocity.scale(this.generateValue(this.averageSpeed, this.speedError) * scaleFactor);
        float scale = this.generateValue(this.averageScale, this.scaleError) * scaleFactor;
        float lifeLength = this.generateValue(this.averageLifeLength, this.lifeError) * scaleFactor;
        Vector4f spawnPosition = Vector4f.add(this.offset, this.spawn.getBaseSpawnPosition(), null);
        Matrix4f.transform(transform, spawnPosition, spawnPosition);
        Vector3f spawnPos = new Vector3f(spawnPosition);
        Particle p = this.colour == null ? new Particle(this.texture, spawnPos, velocity, this.gravityComplient, lifeLength, this.generateRotation(), scale) : new Particle(overrideColour == null ? this.colour : overrideColour, this.additive, spawnPos, velocity, this.gravityComplient, lifeLength, this.generateRotation(), scale);
        p.setFade(this.alpha, this.fadeIn, this.fadeOut);
        if (this.hasXRotation) {
            p.set3dRotation(this.xRotSpeed);
        }
    }

    private float generateValue(float average, float errorMargin) {
        float offset = (this.random.nextFloat() - 0.5f) * 2.0f * errorMargin;
        return average + offset;
    }

    private float generateRotation() {
        if (this.randomRotation) {
            return this.random.nextFloat() * 360.0f;
        }
        return 0.0f;
    }

    private static Vector3f generateRandomUnitVectorWithinCone(Vector3f coneDirection, float angle) {
        float cosAngle = (float)Math.cos(angle);
        Random random = new Random();
        float theta = (float)((double)(random.nextFloat() * 2.0f) * Math.PI);
        float z = cosAngle + random.nextFloat() * (1.0f - cosAngle);
        float rootOneMinusZSquared = (float)Math.sqrt(1.0f - z * z);
        float x = (float)((double)rootOneMinusZSquared * Math.cos(theta));
        float y = (float)((double)rootOneMinusZSquared * Math.sin(theta));
        Vector4f direction = new Vector4f(x, y, z, 1.0f);
        if (coneDirection.x != 0.0f || coneDirection.y != 0.0f || coneDirection.z != 1.0f && coneDirection.z != -1.0f) {
            Vector3f rotateAxis = Vector3f.cross(coneDirection, new Vector3f(0.0f, 0.0f, 1.0f), null);
            rotateAxis.normalise();
            float rotateAngle = (float)Math.acos(Vector3f.dot(coneDirection, new Vector3f(0.0f, 0.0f, 1.0f)));
            Matrix4f rotationMatrix = new Matrix4f();
            rotationMatrix.rotate(-rotateAngle, rotateAxis);
            Matrix4f.transform(rotationMatrix, direction, direction);
        } else if (coneDirection.z == -1.0f) {
            direction.z *= -1.0f;
        }
        return new Vector3f(direction);
    }

    private Vector3f generateRandomUnitVector() {
        float theta = (float)((double)(this.random.nextFloat() * 2.0f) * Math.PI);
        float z = this.random.nextFloat() * 2.0f - 1.0f;
        float rootOneMinusZSquared = (float)Math.sqrt(1.0f - z * z);
        float x = (float)((double)rootOneMinusZSquared * Math.cos(theta));
        float y = (float)((double)rootOneMinusZSquared * Math.sin(theta));
        return new Vector3f(x, y, z);
    }
}

