/*
 * Decompiled with CFR 0.152.
 */
package biomes;

import biomes.BiomeGridSquare;
import java.util.ArrayList;
import java.util.List;
import world.World;

public class BiomeGrid {
    protected static final int GRID_COUNT = 15;
    private List<BiomeGridSquare> biomeGrid = new ArrayList<BiomeGridSquare>();
    private List<BiomeGridSquare> squaresNeedingUpdating = new ArrayList<BiomeGridSquare>();
    private int nextSquareToUpdate = 0;

    public BiomeGrid(World world) {
        int z = 0;
        while (z < 15) {
            int x = 0;
            while (x < 15) {
                this.biomeGrid.add(new BiomeGridSquare(world, this, x, z));
                ++x;
            }
            ++z;
        }
    }

    public BiomeGridSquare getGridSquare(int x, int z) {
        int index = z * 15 + x;
        if (x < 0 || x >= 15 || z < 0 || z >= 15) {
            return null;
        }
        return this.biomeGrid.get(index);
    }

    public void addToUpdateList(BiomeGridSquare square) {
        this.squaresNeedingUpdating.add(square);
    }

    public void removeFromUpdateList(BiomeGridSquare square) {
        this.squaresNeedingUpdating.remove(square);
    }

    public void update() {
        this.updateBiomeFilledSquares();
        this.calcMajorityBiomeForNextSquare();
    }

    private void updateBiomeFilledSquares() {
        for (BiomeGridSquare square : this.squaresNeedingUpdating) {
            square.update();
        }
    }

    private void calcMajorityBiomeForNextSquare() {
        this.biomeGrid.get(this.nextSquareToUpdate++).calculateMajorityBiome();
        this.nextSquareToUpdate %= this.biomeGrid.size();
    }
}

