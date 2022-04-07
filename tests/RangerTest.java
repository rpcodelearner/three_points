package com.github.rpcodelearner.three_points.tests;

import com.github.rpcodelearner.three_points.Ranger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RangerTest {
    private final int firstPixel = 0;  // total 513, to allow for a perfect center
    private final int lastPixel = 512; // total 513, to allow for a perfect center
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

}