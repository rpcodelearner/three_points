package com.github.rpcodelearner.three_points;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlanePointTest {
    final PlanePoint planePointA = new PlanePoint(0.1, 2.3);
    final PlanePoint sameAsPlanePointA = new PlanePoint(0.1, 2.3);
    final PlanePoint planePointB = new PlanePoint(4.5, 6.7);

    @Test
    void preTestPlanePointEquality() {
        assertEquals(planePointA, planePointA); // added for branch coverage
        assertNotEquals(planePointA, null); // 'null' must be 2nd or else JUnit will skip the call to equals()
        assertNotEquals(planePointA, new Object());
        assertEquals(planePointA, sameAsPlanePointA);
        assertEquals(planePointA.hashCode(), sameAsPlanePointA.hashCode());
        assertNotEquals(planePointA, planePointB);
        assertNotEquals(planePointA.hashCode(), planePointB.hashCode());
    }


}