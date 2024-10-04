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
    private final PlanePoint testBottomLeft = new PlanePoint(-1.0, -1.0);
    private final PlanePoint testTopRight = new PlanePoint(1.0, 1.0);
    private final double midReal = 0.0;
    private RangerXY testRanger;

    @BeforeEach
    void setUp() {
        testRanger = new RangerXY(testDim, testBottomLeft, testTopRight);
    }

    @Test
    void constructor() {
        assertAll(
                () -> assertEquals(lastPixelX, testRanger.pixelDim.width),
                () -> assertEquals(lastPixelY, testRanger.pixelDim.height),
                () -> assertEquals(testTopRight.x, testRanger.topRight.x),
                () -> assertEquals(testTopRight.y, testRanger.topRight.y),
                () -> assertEquals(testBottomLeft.x, testRanger.bottomLeft.x),
                () -> assertEquals(testBottomLeft.y, testRanger.bottomLeft.y));
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
                () -> assertEquals(firstPixel, testRanger.toPixelX(testBottomLeft.x)),
                () -> assertEquals(firstPixel, testRanger.toPixelY(testBottomLeft.y)));
    }

    @Test
    void testFirstPoint() {
        assertAll(
                () -> assertEquals(testBottomLeft.x, testRanger.toMathX(firstPixel)),
                () -> assertEquals(testBottomLeft.y, testRanger.toMathY(firstPixel)));
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
        final PlanePoint unusualBottomLeft = new PlanePoint(1.0, 0.0);
        final PlanePoint unusualTopRight = new PlanePoint(-1.0, 0.0);
        RangerXY unusualRange = new RangerXY(pixels, unusualBottomLeft, unusualTopRight);
        assertEquals(0, unusualRange.toPixelX(1.0));
        assertEquals(511, unusualRange.toPixelX(-1.0));
    }

    @Test
    void testCenterWithTwoPixels() {
        final double min = 0.0;
        final double max = 1.0;
        final PlanePoint bottomLeft = new PlanePoint(min, min);
        final PlanePoint topRight = new PlanePoint(max, max);
        final Dimension twoPixels = new Dimension(1, 1);
        RangerXY twoPixRanger = new RangerXY(twoPixels, bottomLeft, topRight);
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
        final PlanePoint pBottomLeft = new PlanePoint(min, min);
        final PlanePoint pTopRight = new PlanePoint(max, max);
        final RangerXY ranger = new RangerXY(evenPixels, pBottomLeft, pTopRight);
        final double delta = ranger.getPixelSizeX() / 1.99999; // slightly larger than half pixel size, to accommodate for rounding

        // with an even number of pixels, the center could end onto either pixel in the middle
        final int pixelMidX = ranger.toPixelX(mid);
        final int pixelMidY = ranger.toPixelY(mid);
        assertTrue(centerX == pixelMidX || (centerX + 1) == pixelMidX);
        assertTrue(centerY == pixelMidY || (centerY + 1) == pixelMidY);
        assertEquals(mid, ranger.toMathX(centerX), delta);
        assertEquals(mid, ranger.toMathY(centerY), delta);
    }

    @Test
    void modifyPixelsDimension() {
        final int width = 123;
        final int height = 456;
        final Dimension dimension = new Dimension(width, height);

        testRanger.setPixelDim(dimension);
        assertEquals(testRanger.pixelDim.width, dimension.width);
        assertEquals(testRanger.pixelDim.height, dimension.height);
        assertEquals((testTopRight.x - testBottomLeft.x) / width, testRanger.getPixelSizeX());
        assertEquals((testTopRight.y - testBottomLeft.y) / height, testRanger.getPixelSizeY());
    }

    @Test
    void modifyMathRange() {
        final PlanePoint bottomLeft = new PlanePoint(-1.23, -4.56);
        final PlanePoint topRight = new PlanePoint(7.89, 0.12);

        testRanger.setCornerPoints(bottomLeft, topRight);
        assertEquals((1.23 + 7.89)/testDim.getWidth(), testRanger.getPixelSizeX());
        assertEquals((4.56 + 0.12)/testDim.getHeight(), testRanger.getPixelSizeY());
    }

    @Test
    void rescaleMathRanges() {
        testRanger.rescaleX(2.0);
        testRanger.rescaleY(0.5);
        assertEquals(4.0, testRanger.topRight.x - testRanger.bottomLeft.x);
        assertEquals(1.0, testRanger.topRight.y - testRanger.bottomLeft.y);
        assertEquals(midReal, testRanger.toMathX(centerPixelX));
        assertEquals(midReal, testRanger.toMathY(centerPixelY));
    }

    @Test
    void recenter() {
        final PlanePoint updatedCenter = new PlanePoint(2.0, 3.0);
        testRanger.recenter(updatedCenter);
        assertEquals(2.0, testRanger.topRight.x - testRanger.bottomLeft.x);
        assertEquals(2.0, testRanger.topRight.y - testRanger.bottomLeft.y);
        assertEquals(updatedCenter.x, testRanger.toMathX(centerPixelX));
        assertEquals(updatedCenter.y, testRanger.toMathY(centerPixelY));
    }
}