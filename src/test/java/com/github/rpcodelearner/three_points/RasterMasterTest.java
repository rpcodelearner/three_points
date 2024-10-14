package com.github.rpcodelearner.three_points;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RasterMasterTest {
    RasterMaster rasterMaster;


    @BeforeEach
    void setUp() {
        rasterMaster = new RasterMaster(0.001);
    }

    @Test
    void diamondCenter() {
        PlanePoint center = new PlanePoint(0.0, 0.0);
        assertFalse(rasterMaster.crossesLevels(RasterMasterTest::computeTaxicabDistance, center, Collections.singletonList(1.0)));
    }

    @Test
    void squareCenter() {
        PlanePoint center = new PlanePoint(0.0, 0.0);
        assertFalse(rasterMaster.crossesLevels(RasterMasterTest::computeChessboardDistance, center, Collections.singletonList(1.0)));
    }

    @Test
    void outsideDiamond() {
        PlanePoint pt = new PlanePoint(1.0, 1.0);
        assertFalse(rasterMaster.crossesLevels(RasterMasterTest::computeTaxicabDistance, pt, Collections.singletonList(1.0)));
    }

    @Test
    void outsideSquare() {
        PlanePoint pt = new PlanePoint(2.0, 1.0);
        assertFalse(rasterMaster.crossesLevels(RasterMasterTest::computeChessboardDistance, pt, Collections.singletonList(1.0)));
    }

    @Test
    void diamondBorder() {
        for (double x = -1.0; x < 1.0; x += 0.1) {
            double y = 1.0 - x;
            PlanePoint pt = new PlanePoint(x, y);
            assertTrue(rasterMaster.crossesLevels(RasterMasterTest::computeTaxicabDistance, pt, Collections.singletonList(1.0)));
        }
    }

    @Test
    void squareBorderUpperSide() {
        for (double x = -1.0; x < 1.0; x += 0.1) {
            double y = 1.0;
            PlanePoint pt = new PlanePoint(x, y);
            assertTrue(rasterMaster.crossesLevels(RasterMasterTest::computeChessboardDistance, pt, Collections.singletonList(1.0)));
        }
    }

    @Test
    void testEuclideanDistance() {
        Set<PlanePoint> testPoints = computePythagoreanTriplesRadius5();
        for (PlanePoint pt : testPoints) {
            assertTrue(rasterMaster.crossesLevels(RasterMasterTest::computeEuclideanDistance, pt, Collections.singletonList(5.0)));
            assertFalse(rasterMaster.crossesLevels(RasterMasterTest::computeEuclideanDistance, pt, Collections.singletonList(4.999)));
            assertFalse(rasterMaster.crossesLevels(RasterMasterTest::computeEuclideanDistance, pt, Collections.singletonList(5.001)));
        }
    }

    private Set<PlanePoint> computePythagoreanTriplesRadius5() {
        Set<PlanePoint> specialPoints = new HashSet<>();
        specialPoints.add(new PlanePoint(5.0, 0.0));
        specialPoints.add(new PlanePoint(3.0, 4.0));
        specialPoints.add(new PlanePoint(4.0, 3.0));
        specialPoints.add(new PlanePoint(0.0, 5.0));
        specialPoints.add(new PlanePoint(-3.0, 4.0));
        specialPoints.add(new PlanePoint(-4.0, 3.0));
        specialPoints.add(new PlanePoint(-5.0, 0.0));
        specialPoints.add(new PlanePoint(-4.0, -3.0));
        specialPoints.add(new PlanePoint(-3.0, -4.0));
        specialPoints.add(new PlanePoint(0.0, -5.0));
        specialPoints.add(new PlanePoint(3.0, -4.0));
        specialPoints.add(new PlanePoint(4.0, -3.0));
        assert specialPoints.size() == 12;
        return specialPoints;
    }

    private static double computeEuclideanDistance(PlanePoint point) {
        return Math.sqrt(point.x * point.x + point.y * point.y);
    }

    /**
     * Taxicab distance is easier to test wrt to Euclidean distance
     * A "circle" looks like a square with diagonals on vert/hor axes (a "diamond")
     * @param point a mathematical point
     * @return "taxicab" distance between point and (0,0)
     */
    private static double computeTaxicabDistance(PlanePoint point) {
        return point.x + point.y;
    }

    /**
     * Chessboard distance is easier to test wrt to Euclidean distance
     * A "circle" looks like a square
     * @param point a mathematical point
     * @return "chessboard" distance between point and (0,0)
     */

    private static double computeChessboardDistance(PlanePoint point) {
        return Math.max(point.x, point.y);
    }
}
