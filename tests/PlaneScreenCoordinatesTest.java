package com.github.rpcodelearner.three_points.tests;


import com.github.rpcodelearner.three_points.PlaneScreenCoordinates;
import com.github.rpcodelearner.three_points.PlaneScreenCoordinates.Pixel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlaneScreenCoordinatesTest {
    public static final int WIDTH = 111;
    public static final int HEIGHT = 222;
    public static final double LEFT = -1.1;
    public static final double RIGHT = 2.2;
    public static final double BOTTOM = -3.3;
    public static final double TOP = 4.4;
    private static final double PRECISION = 0.000001; // for comparison of double precision values
    PlaneScreenCoordinates converter;


    //TODO test precision as well

    @BeforeEach
    public void setUp() {
        converter = new PlaneScreenCoordinates(WIDTH, HEIGHT);
        converter.setPlaneLimitsX(LEFT, RIGHT);
        converter.setPlaneLimitsY(BOTTOM, TOP);
    }

    @Test
    public void upperLeftCornerTest() {
        Pixel upperLeftCorner = converter.planeToPixel(LEFT, TOP);
        assertEquals(0, upperLeftCorner.x);
        assertEquals(0, upperLeftCorner.y);
    }

    @Test
    public void lowerLeftCornerTest() {
        Pixel lowerLeftCorner = converter.planeToPixel(LEFT, BOTTOM);
        assertEquals(0, lowerLeftCorner.x);
        assertEquals(HEIGHT, lowerLeftCorner.y);
    }

    @Test
    public void upperRightCornerTest() {
        Pixel upperRightCorner = converter.planeToPixel(RIGHT, TOP);
        assertEquals(WIDTH, upperRightCorner.x);
        assertEquals(0, upperRightCorner.y);
    }

    @Test
    public void lowerRightCornerTest() {
        Pixel lowerRightCorner = converter.planeToPixel(RIGHT, BOTTOM);
        assertEquals(WIDTH, lowerRightCorner.x);
        assertEquals(HEIGHT, lowerRightCorner.y);
    }

    @Test
    public void pixelToCoords() {

        assertEquals(LEFT, converter.pixelToPlanePoint(new Pixel(0, 0)).x, PRECISION);
        assertEquals(TOP, converter.pixelToPlanePoint(new Pixel(0, 0)).y, PRECISION);

        assertEquals(LEFT, converter.pixelToPlanePoint(new Pixel(0, HEIGHT)).x, PRECISION);
        assertEquals(BOTTOM, converter.pixelToPlanePoint(new Pixel(0, HEIGHT)).y, PRECISION);

        assertEquals(RIGHT, converter.pixelToPlanePoint(new Pixel(WIDTH, 0)).x, PRECISION);
        assertEquals(TOP, converter.pixelToPlanePoint(new Pixel(WIDTH, 0)).y, PRECISION);

        assertEquals(RIGHT, converter.pixelToPlanePoint(new Pixel(WIDTH, HEIGHT)).x, PRECISION);
        assertEquals(BOTTOM, converter.pixelToPlanePoint(new Pixel(WIDTH, HEIGHT)).y, PRECISION);
    }

    @Test
    public void screenToCoords() {
        assertEquals(LEFT, converter.screenToPlanePoint(0, 0).x, PRECISION);
        assertEquals(TOP, converter.screenToPlanePoint(0, 0).y, PRECISION);

        assertEquals(LEFT, converter.screenToPlanePoint(0, HEIGHT).x, PRECISION);
        assertEquals(BOTTOM, converter.screenToPlanePoint(0, HEIGHT).y, PRECISION);

        assertEquals(RIGHT, converter.screenToPlanePoint(WIDTH, 0).x, PRECISION);
        assertEquals(TOP, converter.screenToPlanePoint(WIDTH, 0).y, PRECISION);

        assertEquals(RIGHT, converter.screenToPlanePoint(WIDTH, HEIGHT).x, PRECISION);
        assertEquals(BOTTOM, converter.screenToPlanePoint(WIDTH, HEIGHT).y, PRECISION);

        // center: we us lower precision comparison instead of hacking the numbers
        assertEquals((RIGHT + LEFT) / 2, converter.screenToPlanePoint(WIDTH / 2, HEIGHT / 2).x, 0.02);
        assertEquals((BOTTOM + TOP) / 2, converter.screenToPlanePoint(WIDTH / 2, HEIGHT / 2).y, 0.02);
    }

    // TODO run twice every test by using JUnit parameterized testing (or whatever their name is in JUnit 5)
    // possible source of hints: www.baeldung.com/parameterized-tests-junit-5
    @Test
    public void differentRanges() {

        final int width = 123;
        final int height = 456;
        converter.setScreenSize(width, height);
        final double minX = 12.34;
        final double maxX = -23.45;  // NOTE THAT max < min
        converter.setPlaneLimitsX(minX, maxX);
        final double minY = 34.56;
        final double maxY = -45.67;  // NOTE THAT max < min
        converter.setPlaneLimitsY(minY, maxY);

        {
            Pixel lowerLeftCorner = converter.planeToPixel(minX, minY);
            assertEquals(0, lowerLeftCorner.x);
            assertEquals(height, lowerLeftCorner.y);
        }
        {
            Pixel upperLeftCorner = converter.planeToPixel(minX, maxY);
            assertEquals(0, upperLeftCorner.x);
            assertEquals(0, upperLeftCorner.y);
        }
        {
            Pixel lowerRightCorner = converter.planeToPixel(maxX, minY);
            assertEquals(width, lowerRightCorner.x);
            assertEquals(height, lowerRightCorner.y);
        }
        {
            Pixel upperRightCorner = converter.planeToPixel(maxX, maxY);
            assertEquals(width, upperRightCorner.x);
            assertEquals(0, upperRightCorner.y);
        }

    }
}
