package com.github.rpcodelearner.three_points;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class RangerXYTest {
    private final int firstPixel = 0;
    private final int lastPixel = 512;  // total is 513px, odd number, so there is a perfectly centered pixel
    private final int centerPixel = 256;
    private final double minReal = -1.0;
    private final double maxReal = 1.0;
    private final double midReal = 0.0;
    private RangerXY ranger;

    @BeforeEach
    void setUp() {
        int pixelSide = lastPixel - firstPixel;
        ranger = new RangerXY(new Dimension(pixelSide, pixelSide), new PlanePoint(minReal, minReal), new PlanePoint(maxReal, maxReal));
    }

    @Test
    void constructor() {
        assertAll("constructor test",
                () -> assertEquals(lastPixel, ranger.pixelDim.width),
                () -> assertEquals(maxReal, ranger.topRight.x),
                () -> assertEquals(maxReal, ranger.topRight.y),
                () -> assertEquals(minReal, ranger.bottomLeft.x),
                () -> assertEquals(minReal, ranger.bottomLeft.y)
        );
    }

    @Test
    void pixelSize() {
        assertEquals(2.0/512, ranger.getPixelSize());
    }


    @Test
    void testFirstPixel() {
        assertEquals(firstPixel, ranger.toPixelX(minReal));
    }

    @Test
    void testFirstPoint() {
        assertEquals(minReal, ranger.toMath(firstPixel));
    }

    @Test
    void testLastPixel() {
        assertEquals(lastPixel, ranger.toPixelX(maxReal));
    }

    @Test
    void testLastPoint() {
        assertEquals(maxReal, ranger.toMath(lastPixel));
    }

    @Test
    void testCenterPixel() {
        assertEquals(centerPixel, ranger.toPixelX(midReal));
    }

    @Test
    void testMiddlePoint() {
        assertEquals(midReal, ranger.toMath(centerPixel));
    }

//    @Test
//    void testInvertedPixelRange() {
//        Ranger vertAxis = new Ranger(511, 0, -1.0, 1.0);
//        assertEquals(0, vertAxis.toPixel(1.0));
//        assertEquals(511, vertAxis.toPixel(-1.0));
//    }

    @Test
    void testInvertedMathRange() {
        final Dimension pixels = new Dimension(511, 511);
        final PlanePoint unusualLeftBottom = new PlanePoint(1.0, 0.0);
        final PlanePoint unusualRightBottom = new PlanePoint(-1.0, 0.0);
        RangerXY unusualRange = new RangerXY(pixels, unusualLeftBottom, unusualRightBottom);
        assertEquals(0, unusualRange.toPixelX(1.0));
        assertEquals(511, unusualRange.toPixelX(-1.0));
    }

//    @Test
//    void testInvertedPixelAndMathRanges() {
//        Ranger doubleTrouble = new Ranger(511, 0, 1.0, -1.0);
//        assertEquals(511, doubleTrouble.toPixel(1.0));
//        assertEquals(0, doubleTrouble.toPixel(-1.0));
//    }

    @Test
    void testCenterWithTwoPixels() {
        int first = 0;
        int last = 1;
        final Dimension twoPixels = new Dimension(last - first, last - first);
        final PlanePoint leftBottom = new PlanePoint(0.0, 0.0);
        final PlanePoint topRight = new PlanePoint(1.0, 1.0);
        RangerXY ranger = new RangerXY(twoPixels, leftBottom, topRight);
        double min = 0.0;
        double max = 1.0;
        final double mid = (max - min) / 2.0;
        final double delta = 0.0001;
        assertEquals(first, ranger.toPixelX(mid - delta));
        assertEquals(last, ranger.toPixelX(mid + delta));
    }

//    @Test
//    void testCenterWithEvenPixels() {
//        int first = 0;
//        int last = 511;
//        double min = -1234.5;
//        double max = 5432.1;
//        final double pixelSize = (max - min) / (last - first);
//        Ranger ranger = new Ranger(first, last, min, max);
//        final double mid = (max + min) / 2.0;
//        final int center = (last + first) / 2;
//        final double delta = pixelSize / 1.99999; // slightly larger than half pixel size, to accommodate for rounding
//
//        // with an even number of pixels, the center could end onto either pixel in the middle
//        assertTrue(center == ranger.toPixel(mid) || (center + 1) == ranger.toPixel(mid));
//        assertEquals(mid, ranger.toMath(center), delta);
//    }

}