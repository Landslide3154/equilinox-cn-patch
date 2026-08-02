/*
 * Decompiled with CFR 0.152.
 */
package particleComponent;

import org.lwjgl.util.vector.Vector3f;
import particleSpawns.CircleSpawn;
import particleSpawns.LineSpawn;
import particleSpawns.ParticleSpawn;
import particleSpawns.PointSpawn;
import particleSpawns.SphereSpawn;
import particles.ParticleSystem;
import particles.ParticleTexture;
import resourceManagement.ParticleAtlasCache;
import toolbox.Colour;
import utils.CSVReader;

public class ParticleSystemLoader {
    public static ParticleSystem loadParticleSystem(CSVReader reader) {
        boolean isColour = reader.getNextLabelBool();
        reader.getNextString();
        ParticleSpawn spawn = ParticleSystemLoader.loadSpawn(reader);
        ParticleSystem system = ParticleSystemLoader.loadBasicSystem(reader, spawn, isColour);
        ParticleSystemLoader.loadDirection(reader, system);
        ParticleSystemLoader.loadOffset(reader, system);
        ParticleSystemLoader.loadErrors(reader, system);
        ParticleSystemLoader.loadRotationValues(reader, system);
        return system;
    }

    private static ParticleSystem loadBasicSystem(CSVReader reader, ParticleSpawn spawn, boolean isColour) {
        float pps = reader.getNextLabelFloat();
        float speed = reader.getNextLabelFloat();
        float gravity = reader.getNextLabelFloat();
        float life = reader.getNextLabelFloat();
        float scale = reader.getNextLabelFloat();
        ParticleSystem system = null;
        if (isColour) {
            Colour colour = new Colour(reader.getNextLabelVector());
            boolean additive = reader.getNextLabelBool();
            system = new ParticleSystem(colour, additive, spawn, pps, speed, gravity, life, scale);
            ParticleSystemLoader.loadFadeValues(reader, system);
            return system;
        }
        ParticleTexture texture = ParticleSystemLoader.loadAtlas(reader);
        system = new ParticleSystem(texture, spawn, pps, speed, gravity, life, scale);
        return system;
    }

    private static void loadOffset(CSVReader reader, ParticleSystem system) {
        system.setOffset(reader.getNextLabelVector());
    }

    private static void loadFadeValues(CSVReader reader, ParticleSystem system) {
        float alpha = reader.getNextLabelFloat();
        float fadeIn = reader.getNextLabelFloat();
        float fadeOut = reader.getNextLabelFloat();
        system.setFadeValues(alpha, fadeIn, fadeOut);
    }

    private static void loadRotationValues(CSVReader reader, ParticleSystem system) {
        if (reader.getNextLabelBool()) {
            system.randomizeRotation();
            if (reader.getNextLabelBool()) {
                system.setXRotation(reader.getNextLabelFloat());
            }
        }
    }

    private static void loadDirection(CSVReader reader, ParticleSystem system) {
        if (reader.getNextLabelBool()) {
            Vector3f direction = reader.getNextLabelVector();
            float deviation = reader.getNextLabelFloat();
            system.setDirection(direction, deviation);
        }
    }

    private static void loadErrors(CSVReader reader, ParticleSystem system) {
        system.setLifeError(reader.getNextLabelFloat());
        system.setScaleError(reader.getNextLabelFloat());
        system.setSpeedError(reader.getNextLabelFloat());
    }

    private static ParticleTexture loadAtlas(CSVReader reader) {
        int id = reader.getNextLabelInt();
        return ParticleAtlasCache.getAtlas(id);
    }

    private static ParticleSpawn loadSpawn(CSVReader reader) {
        int spawnId = reader.getNextInt();
        if (spawnId == 0) {
            return new PointSpawn();
        }
        if (spawnId == 1) {
            float radius = reader.getNextFloat();
            return new SphereSpawn(radius);
        }
        if (spawnId == 2) {
            float length = reader.getNextFloat();
            Vector3f direction = reader.getNextVector();
            return new LineSpawn(length, direction);
        }
        if (spawnId == 3) {
            float radius = reader.getNextFloat();
            Vector3f normal = reader.getNextVector();
            return new CircleSpawn(normal, radius);
        }
        return new PointSpawn();
    }
}

