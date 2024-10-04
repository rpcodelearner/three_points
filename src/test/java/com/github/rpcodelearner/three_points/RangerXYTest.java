package com.github.rpcodelearner.three_points;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class RangerXYTest {
    private final int firstPixel = 0;
    private final int lastPixelX = 512;  // total is odd number 513px, so there is a centered pixel
    private final int lastPixelY = 256;  // total is odd number 257px, so there is a centered pixel
    private final Dimension testDim = new Dimension(lastPixelX, lastPixelY);
    private final int centerPixelX = 256;
    private final int centerPixelY = 128;
    private final PlanePoint testLeftBottom = new PlanePoint(-1.0, -1.0);
    private final PlanePoint testTopRight = new PlanePoint(1.0, 1.0);
    private final double midReal = 0.0;
    private RangerXY testRanger;

    @BeforeEach
    void setUp() {
        testRanger = new RangerXY(testDim, testLeftBottom, testTopRight);
    }

    @Test
    void constructor() {
        assertAll(
                () -> assertEquals(lastPixelX, testRanger.pixelDim.width),
                () -> assertEquals(lastPixelY, testRanger.pixelDim.height),
                () -> assertEquals(testTopRight.x, testRanger.topRight.x),
                () -> assertEquals(testTopRight.y, testRanger.topRight.y),
                () -> assertEquals(testLeftBottom.x, testRanger.bottomLeft.x),
                () -> assertEquals(testLeftBottom.y, testRanger.bottomLeft.y));
    }

    @Test
    void pixelSize() {
        assertAll(
                () -> assertEquals(2.0 / 512, testRanger.getPixelSizeX()),
                () -> assertEquals(2.0 / 256, testRanger.getPixelSizeY()));
    }


    @Test
    void testFirstPixels() {
        assertAll(
                () -> assertEquals(firstPixel, testRanger.toPixelX(testLeftBottom.x)),
                () -> assertEquals(firstPixel, testRanger.toPixelY(testLeftBottom.y)));
    }

    @Test
    void testFirstPoint() {
        assertAll(
                () -> assertEquals(testLeftBottom.x, testRanger.toMathX(firstPixel)),
                () -> assertEquals(testLeftBottom.y, testRanger.toMathY(firstPixel)));
    }

    @Test
    void testLastPixels() {
        assertAll(
                () -> assertEquals(lastPixelX, testRanger.toPixelX(testTopRight.x)),
                () -> assertEquals(lastPixelY, testRanger.toPixelY(testTopRight.y)));
    }

    @Test
    void testLastPoint() {
        assertAll(
                () -> assertEquals(testTopRight.x, testRanger.toMathX(lastPixelX)),
                () -> assertEquals(testTopRight.y, testRanger.toMathY(lastPixelY)));
    }

    @Test
    void testCenterPixel() {
        assertAll(
                () -> assertEquals(centerPixelX, testRanger.toPixelX(midReal)),
                () -> assertEquals(centerPixelY, testRanger.toPixelY(midReal)));
    }

    @Test
    void testMiddlePoint() {
        assertAll(
                () -> assertEquals(midReal, testRanger.toMathX(centerPixelX)),
                () -> assertEquals(midReal, testRanger.toMathY(centerPixelY)));
    }

    @Test
    void testInvertedMathRange() {
        final Dimension pixels = new Dimension(511, 511);
        final PlanePoint unusualLeftBottom = new PlanePoint(1.0, 0.0);
        final PlanePoint unusualRightBottom = new PlanePoint(-1.0, 0.0);
        RangerXY unusualRange = new RangerXY(pixels, unusualLeftBottom, unusualRightBottom);
        assertEquals(0, unusualRange.toPixelX(1.0));
        assertEquals(511, unusualRange.toPixelX(-1.0));
    }

    @Test
    void testCenterWithTwoPixels() {
        final double min = 0.0;
        final double max = 1.0;
        final PlanePoint leftBottom = new PlanePoint(min, min);
        final PlanePoint topRight = new PlanePoint(max, max);
        final Dimension twoPixels = new Dimension(1, 1);
        RangerXY twoPixRanger = new RangerXY(twoPixels, leftBottom, topRight);
        final double mid = (max - min) / 2.0;
        final double delta = 0.0001;
        assertEquals(0, twoPixRanger.toPixelX(mid - delta));
        assertEquals(0, twoPixRanger.toPixelY(mid - delta));
        assertEquals(1, twoPixRanger.toPixelX(mid + delta));
        assertEquals(1, twoPixRanger.toPixelY(mid + delta));
    }

    @Test
    void testCenterWithEvenPixels() {
        final Dimension evenPixels = new Dimension(511, 511);
        final int centerX = 511 / 2;
        final int centerY = 511 / 2;
        final double min = -1234.5;
        final double max = 5432.1;
        final double mid = (max + min) / 2.0;
        final PlanePoint pLeftBottom = new PlanePoint(min, min);
        final PlanePoint pTopRight = new PlanePoint(max, max);
        final RangerXY ranger = new RangerXY(evenPixels, pLeftBottom, pTopRight);
        final double delta = ranger.getPixelSizeX() / 1.99999; // slightly larger than half pixel size, to accommodate for rounding

        // with an even number of pixels, the center could end onto either pixel in the middle
        assertTrue(centerX == ranger.toPixelX(mid) || (centerX + 1) == ranger.toPixelX(mid));
        assertTrue(centerY == ranger.toPixelY(mid) || (centerY + 1) == ranger.toPixelY(mid));
        assertEquals(mid, ranger.toMathX(centerX), delta);
        assertEquals(mid, ranger.toMathY(centerY), delta);
    }

}