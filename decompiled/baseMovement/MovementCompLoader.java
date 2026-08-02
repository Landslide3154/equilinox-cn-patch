/*
 * Decompiled with CFR 0.152.
 */
package baseMovement;

import baseMovement.BaseMovement;
import blueprints.Blueprint;
import bounceMovement.BounceBaseBlueprint;
import bounceMovement.FlouncerBlueprint;
import bounceMovement.WaddleMoveBlueprint;
import breedingTraits.FloatTrait;
import breedingTrees.ReqInfo;
import componentArchitecture.ComponentBlueprint;
import componentArchitecture.ComponentLoader;
import componentArchitecture.ComponentType;
import componentArchitecture.Requirement;
import dolphinMovement.DolphinMoveBlueprint;
import floatyMovement.FloatyMoveBlueprint;
import flying.BeeMovementBlueprint;
import flying.BirdMoveBlueprint;
import flying.FlyBlueprint;
import frogMovement.FrogMovementBlueprint;
import gallopMovement.GallopMovementBlueprint;
import gallopMovement.RabbitMovementBlueprint;
import instances.Entity;
import java.util.List;
import languages.GameText;
import rockingMovement.RockingBlueprint;
import utils.CSVReader;

public class MovementCompLoader
implements ComponentLoader {
    @Override
    public ComponentBlueprint load(CSVReader reader, Blueprint blueprint) {
        int typeId = reader.getNextInt();
        if (typeId == 6) {
            float speed = reader.getNextLabelFloat();
            float bouncePower = reader.getNextLabelFloat();
            float waitTime = reader.getNextLabelFloat();
            float bounciness = reader.getNextLabelFloat();
            return new FrogMovementBlueprint(blueprint, speed, bouncePower, waitTime, bounciness);
        }
        if (typeId == 7) {
            return new RabbitMovementBlueprint(reader);
        }
        if (typeId == 8) {
            float speed = reader.getNextLabelFloat();
            float rotSpeed = reader.getNextLabelFloat();
            float bouncePower = reader.getNextLabelFloat();
            float bounceRotation = reader.getNextLabelFloat();
            float standardHeight = reader.getNextLabelFloat();
            return new FlouncerBlueprint(speed, rotSpeed, bouncePower, bounceRotation, standardHeight);
        }
        if (typeId == 9) {
            float speed = reader.getNextLabelFloat();
            int xRot = reader.getNextLabelInt();
            float minRot = reader.getNextLabelFloat();
            float maxRot = reader.getNextLabelFloat();
            float rotSpeed = reader.getNextLabelFloat();
            if (reader.isEndOfLine()) {
                return new RockingBlueprint(speed, xRot, minRot, maxRot, rotSpeed);
            }
            float swimHeight = reader.getNextLabelFloat();
            if (reader.isEndOfLine()) {
                return new RockingBlueprint(speed, xRot, minRot, maxRot, rotSpeed, swimHeight);
            }
            boolean eggStage = reader.getNextLabelBool();
            float swimFactor = reader.getNextLabelFloat();
            if (reader.isEndOfLine()) {
                return new RockingBlueprint(speed, xRot, minRot, maxRot, rotSpeed, swimHeight, eggStage, swimFactor);
            }
            float swimInertia = reader.getNextLabelFloat();
            return new RockingBlueprint(speed, xRot, minRot, maxRot, rotSpeed, swimHeight, eggStage, swimFactor, swimInertia);
        }
        if (typeId == 10) {
            return new FlyBlueprint();
        }
        if (typeId == 11) {
            return new BeeMovementBlueprint(reader.getNextLabelFloat());
        }
        if (typeId == 12) {
            if (!reader.isEndOfLine()) {
                return new BirdMoveBlueprint(reader.getNextLabelFloat());
            }
            return new BirdMoveBlueprint(-0.6f);
        }
        if (typeId == 13) {
            return new GallopMovementBlueprint(reader);
        }
        if (typeId == 14) {
            float speed = reader.getNextLabelFloat();
            float rotSpeed = reader.getNextLabelFloat();
            float bouncePower = reader.getNextLabelFloat();
            return new BounceBaseBlueprint(speed, rotSpeed, bouncePower);
        }
        if (typeId == 15) {
            float speed = reader.getNextLabelFloat();
            float rotSpeed = reader.getNextLabelFloat();
            float bouncePower = reader.getNextLabelFloat();
            return new WaddleMoveBlueprint(speed, rotSpeed, bouncePower);
        }
        if (typeId == 21) {
            return new FloatyMoveBlueprint();
        }
        if (typeId == 45) {
            float speed = reader.getNextLabelFloat();
            int xRot = reader.getNextLabelInt();
            float minRot = reader.getNextLabelFloat();
            float maxRot = reader.getNextLabelFloat();
            float rotSpeed = reader.getNextLabelFloat();
            return new DolphinMoveBlueprint(speed, xRot, minRot, maxRot, rotSpeed);
        }
        System.err.println("No Movement with ID: " + typeId);
        return null;
    }

    @Override
    public Requirement loadRequirement(CSVReader reader) {
        final float target = reader.getNextLabelFloat();
        return new Requirement(){

            @Override
            public boolean check(Entity entity) {
                BaseMovement mover = (BaseMovement)entity.getComponent(ComponentType.MOVEMENT);
                return (double)(((FloatTrait)mover.getTrait((int)0)).value * 10.0f) >= (double)target - 0.05;
            }

            @Override
            public void getGuiInfo(List<ReqInfo> components) {
                components.add(new ReqInfo(GameText.getText(1138), String.valueOf(String.format("%.1f", Float.valueOf(target))) + " m/s"));
            }

            @Override
            public boolean isSecret() {
                return false;
            }
        };
    }
}

