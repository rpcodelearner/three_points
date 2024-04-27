package com.github.rpcodelearner.three_points;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThreePointsUserChoicesTest {

    @Test
    void getNumPtsDefaultValue() {
        ThreePointsUserChoices toTest = new ThreePointsUserChoices();
        assertEquals(3, toTest.getNumPts());
    }

    @Test
    void numPts() {
        ThreePointsUserChoices toTest = new ThreePointsUserChoices();
        for (int i = 1; i < 10; i++) {
            toTest.setNumPts(i);
            assertEquals(i, toTest.getNumPts());
        }
    }

    @Test
    void getFociPatternsArray() {
        ThreePointsUserChoices toTest = new ThreePointsUserChoices();
        String[] defaultPatterns = toTest.getFociPatternsArray();
        String[] expectedDefaultPatterns = {
                "Circular",
                "Random",
                "Aligned"};
        assertArrayEquals(expectedDefaultPatterns, defaultPatterns);
    }

    @Test
    void getDrawingStylesArray() {
        ThreePointsUserChoices toTest = new ThreePointsUserChoices();
        String[] defaultDrawingStyles = toTest.getDrawingStylesArray();
        String[] expectedDrawingStyles = {
                "Thick",
                "Medium",
                "Fine",
                "Precision"};
        assertArrayEquals(expectedDrawingStyles, defaultDrawingStyles);
    }
}