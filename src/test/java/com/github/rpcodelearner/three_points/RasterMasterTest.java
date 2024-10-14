package com.github.rpcodelearner.three_points;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RasterMasterTest {
    RasterMaster rasterMaster;

    private static double compute(PlanePoint point) {
        return point.x + point.y;
    }

    @BeforeEach
    void setUp() {
        rasterMaster = new RasterMaster(1.0);
    }

    @Test
    void squareCenter() {
        PlanePoint center = new PlanePoint(0.0, 0.0);
        assertFalse(rasterMaster.crossesLevels(RasterMasterTest::compute, center, Collections.singletonList(1.0)));
    }

    @Test
    void outsideSquare() {
        PlanePoint pt = new PlanePoint(1.0, 1.0);
        assertFalse(rasterMaster.crossesLevels(RasterMasterTest::compute, pt, Collections.singletonList(1.0)));
    }

    @Test
    void squareBorder() {
        for (double x = -1.0; x < 1.0; x += 0.1) {
            double y = 1.0 - x;
            PlanePoint pt = new PlanePoint(x, y);
            assertTrue(rasterMaster.crossesLevels(RasterMasterTest::compute, pt, Collections.singletonList(1.0)));
        }
    }

}
