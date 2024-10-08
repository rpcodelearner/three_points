package com.github.rpcodelearner.three_points;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class RangerXYTest {
    // pixels
    private final Pixel firstCornerPixel = new Pixel(0, 0);
    private final Pixel lastCornerPixel = new Pixel(512, 256); // with odd num of pixels there is a central point
    private final Dimension testDim = new Dimension(lastCornerPixel.x, lastCornerPixel.y);
    private final Pixel centerPixel = new Pixel(256, 128);
    // mathematical plane
    private final PlanePoint mathOrigin = new PlanePoint(0.0, 0.0);
    private final PlanePoint testBottomLeft = new PlanePoint(-1.0, -1.0);
    private final PlanePoint testTopRight = new PlanePoint(1.0, 1.0);
    // class under test
    private RangerXY testRanger;

    @BeforeEach
    void setUp() {
        testRanger = new RangerXY(testDim, testBottomLeft, testTopRight);
    }

    @Test
    void constructor() {
        assertAll(
                () -> assertEquals(testDim.width, testRanger.pixelDim.width),
                () -> assertEquals(testDim.height, testRanger.pixelDim.height),
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
        assertEquals(firstCornerPixel, testRanger.toPixel(testBottomLeft));
    }

    @Test
    void testFirstPoint() {
        assertEquals(testBottomLeft, testRanger.toMath(firstCornerPixel));
    }

    @Test
    void testLastPixels() {
        assertEquals(lastCornerPixel, testRanger.toPixel(testTopRight));
    }

    @Test
    void testLastPoint() {
        assertEquals(testTopRight, testRanger.toMath(lastCornerPixel));
    }

    @Test
    void testCenterPixel() {
        assertEquals(centerPixel, testRanger.toPixel(mathOrigin));
    }

    @Test
    void testMiddlePoint() {
        assertEquals(mathOrigin, testRanger.toMath(centerPixel));
    }

    @Test
    void testInvertedMathAxis() {
        final Dimension pixels = new Dimension(511, 511);
        final PlanePoint unusualBottomLeft = new PlanePoint(1.0, -1.0);
        final PlanePoint unusualTopRight = new PlanePoint(-1.0, 1.0);
        RangerXY invertedMathRange = new RangerXY(pixels, unusualBottomLeft, unusualTopRight);
        assertEquals(new Pixel(0, 0), invertedMathRange.toPixel(unusualBottomLeft));
        assertEquals(new Pixel(511, 511), invertedMathRange.toPixel(unusualTopRight));
    }

    @Test
    void testCenterWithTwoPixels() {
        final Dimension twoPixels = new Dimension(1, 1);
        final Pixel twoPixelCorner = new Pixel(1, 1);
        final RangerXY twoPixRanger = new RangerXY(twoPixels, testBottomLeft, testTopRight);
        final double delta = 0.0001;
        PlanePoint midMinusSmallOffset = new PlanePoint(mathOrigin.x - delta, mathOrigin.y - delta);
        PlanePoint midPlusSmallOffset = new PlanePoint(mathOrigin.x + delta, mathOrigin.y + delta);
        assertEquals(firstCornerPixel, twoPixRanger.toPixel(midMinusSmallOffset));
        assertEquals(twoPixelCorner, twoPixRanger.toPixel(midPlusSmallOffset));
    }

    @Test
    void testCenterWithEvenPixels() {
        final Dimension evenPixels = new Dimension(511, 511);
        final Pixel center = new Pixel(511 / 2, 511 / 2);
        final double min = -1234.5;
        final double max = 5432.1;
        final double mid = (max + min) / 2.0;
        final PlanePoint pBottomLeft = new PlanePoint(min, min);
        final PlanePoint midPoint = new PlanePoint(mid, mid);
        final PlanePoint pTopRight = new PlanePoint(max, max);
        final RangerXY ranger = new RangerXY(evenPixels, pBottomLeft, pTopRight);
        final double delta = ranger.getPixelSizeX() / 1.99999; // slightly larger than half pixel size, to accommodate for rounding

        // with an even number of pixels, the center could end onto either pixel in the middle
        final Pixel midPixel = ranger.toPixel(midPoint);
        assertTrue(center.x == midPixel.x || (center.x + 1) == midPixel.x);
        assertTrue(center.y == midPixel.y || (center.y + 1) == midPixel.y);
        assertEquals(midPoint.x, ranger.toMath(center).x, delta);
        assertEquals(midPoint.y, ranger.toMath(center).y, delta);
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
        assertEquals(mathOrigin, testRanger.toMath(centerPixel));
    }

    @Test
    void recenter() {
        final PlanePoint updatedCenter = new PlanePoint(2.0, 3.0);
        testRanger.recenter(updatedCenter);
        assertEquals(2.0, testRanger.topRight.x - testRanger.bottomLeft.x);
        assertEquals(2.0, testRanger.topRight.y - testRanger.bottomLeft.y);
        assertEquals(updatedCenter, testRanger.toMath(centerPixel));
    }

    @Test
    void setMathCenterToPixel() {
        final double knownPixelSizeX = testRanger.getPixelSizeX();
        final double knownPixelSizeY = testRanger.getPixelSizeY();
        final Pixel pixelOffset = new Pixel(100, 200);
        testRanger.setMathCenterToPixel(pixelOffset);
        assertEquals(pixelOffset, testRanger.toPixel(mathOrigin));
        // then we check that pixel dimensions are not changed
        assertEquals(knownPixelSizeX, testRanger.getPixelSizeX());
        assertEquals(knownPixelSizeY, testRanger.getPixelSizeY());
    }
}