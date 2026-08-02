/*
 * Decompiled with CFR 0.152.
 */
package beavers;

import gameManaging.GameManager;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import toolbox.BinaryLineSearch;
import toolbox.Maths;
import world.GridSection;
import world.PerSectionCode;

public class BuildSpotFinder {
    private static final float IDEAL_HEIGHT = GameManager.getWorld().getWaterHeight() - 0.2f;
    private List<Vector3f> abovePoints = new ArrayList<Vector3f>();
    private List<Vector3f> belowPoints = new ArrayList<Vector3f>();

    public Vector3f findBuildSpot(Vector3f basePos, int range) {
        this.samplePoints(basePos, range);
        if (this.abovePoints.isEmpty() || this.belowPoints.isEmpty()) {
            return null;
        }
        Vector3f abovePoint = this.selectRandomPoint(this.abovePoints);
        Vector3f belowPoint = this.selectRandomPoint(this.belowPoints);
        BinaryLineSearch binarySearch = new BinaryLineSearch(abovePoint, belowPoint, 10, IDEAL_HEIGHT);
        return binarySearch.doSearch();
    }

    private Vector3f selectRandomPoint(List<Vector3f> list) {
        int index = Maths.RANDOM.nextInt(list.size());
        return list.get(index);
    }

    private void samplePoints(Vector3f basePos, int range) {
        GameManager.getWorld().iterateGridSquaresNew(basePos.x, basePos.z, range, new PerSectionCode(){

            @Override
            public void execute(GridSection gridSquare) {
                Vector2f[] points;
                Vector2f[] vector2fArray = points = gridSquare.getFourPoints();
                int n = points.length;
                int n2 = 0;
                while (n2 < n) {
                    Vector2f point = vector2fArray[n2];
                    float height = GameManager.getWorld().getHeightOfTerrain(point.x, point.y);
                    if (height < IDEAL_HEIGHT) {
                        BuildSpotFinder.this.belowPoints.add(new Vector3f(point.x, height, point.y));
                    } else {
                        BuildSpotFinder.this.abovePoints.add(new Vector3f(point.x, height, point.y));
                    }
                    ++n2;
                }
            }
        });
    }
}

