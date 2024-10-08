package com.github.rpcodelearner.three_points;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PixelTest {
    final Pixel pixelA = new Pixel(0, 1);
    final Pixel sameAsPixelA = new Pixel(pixelA.x, pixelA.y);
    final Pixel pixelB = new Pixel(2, 3);
    final Pixel pixelC = new Pixel(0, 3); // added for branch coverage

    @Test
    void testEquals() {
        assertEquals(pixelA, pixelA); // added for branch coverage
        assertNotEquals(pixelA, null); // 'null' must be 2nd or else JUnit will skip the call to equals()
        assertNotEquals(pixelA, new Object());
        assertEquals(pixelA, sameAsPixelA);
        assertNotEquals(pixelA, pixelB);
        assertNotEquals(pixelA, pixelC);
    }

    @Test
    void testHashCode() {
        assertEquals(pixelA.hashCode(), sameAsPixelA.hashCode());
        assertNotEquals(pixelA.hashCode(), pixelB.hashCode());
    }
}