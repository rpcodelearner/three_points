package com.github.rpcodelearner.three_points.src.test.java;

import com.github.rpcodelearner.three_points.src.main.java.Ranger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RangerTest {
    private final int firstPixel = 0;
    private final int lastPixel = 512;  // total is 513px, odd number, so there is a perfectly centered pixel
    private final int centerPixel = 256;
    private final double minReal = -1.0;
    private final double maxReal = 1.0;
    private final double midReal = 0.0;
    private Ranger ranger;

    @BeforeEach
    void setUp() {
        ranger = new Ranger(firstPixel, lastPixel, minReal, maxReal);
    }


    @Test
    void testFirstPixel() {
        assertEquals(firstPixel, ranger.toPixel(minReal));
    }

    @Test
    void testFirstPoint() {
        assertEquals(minReal, ranger.toMath(firstPixel));
    }

    @Test
    void testLastPixel() {
        assertEquals(lastPixel, ranger.toPixel(maxReal));
    }

    @Test
    void testLastPoint() {
        assertEquals(maxReal, ranger.toMath(lastPixel));
    }

    @Test
    void testCenterPixel() {
        assertEquals(centerPixel, ranger.toPixel(midReal));
    }

    @Test
    void testMiddlePoint() {
        assertEquals(midReal, ranger.toMath(centerPixel));
    }

    @Test
    void testInvertedPixelRange() {
        Ranger vertAxis = new Ranger(511, 0, -1.0, 1.0);
        assertEquals(0, vertAxis.toPixel(1.0));
        assertEquals(511, vertAxis.toPixel(-1.0));
    }

    @Test
    void testInvertedMathRange() {
        Ranger unusualAxis = new Ranger(0, 511, 1.0, -1.0);
        assertEquals(0, unusualAxis.toPixel(1.0));
        assertEquals(511, unusualAxis.toPixel(-1.0));
    }

    @Test
    void testInvertedPixelAndMathRanges() {
        Ranger doubleTrouble = new Ranger(511, 0, 1.0, -1.0);
        assertEquals(511, doubleTrouble.toPixel(1.0));
        assertEquals(0, doubleTrouble.toPixel(-1.0));
    }

    @Test
    void testCenterWithTwoPixels() {
        int first = 0;
        int last = 1;
        double min = 0.0;
        double max = 1.0;
        Ranger ranger = new Ranger(first, last, min, max);
        final double mid = (max - min) / 2.0;
        final double delta = 0.0001;
        assertEquals(first, ranger.toPixel(mid - delta));
        assertEquals(last, ranger.toPixel(mid + delta));
    }

    @Test
    void testCenterWithEvenPixels() {
        int first = 0;
        int last = 511;
        double min = -1234.5;
        double max = 5432.1;
        final double pixelSize = (max - min) / (last - first);
        Ranger ranger = new Ranger(first, last, min, max);
        final double mid = (max + min) / 2.0;
        final int center = (last + first) / 2;
        final double delta = pixelSize / 1.99999; // slightly larger than half pixel size, to accommodate for rounding

        // with an even number of pixels, the center could end onto either pixel in the middle
        assertTrue(center == ranger.toPixel(mid) || (center + 1) == ranger.toPixel(mid));
        assertEquals(mid, ranger.toMath(center), delta);
    }

}