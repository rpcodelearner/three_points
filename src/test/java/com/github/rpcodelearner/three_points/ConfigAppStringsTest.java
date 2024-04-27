package com.github.rpcodelearner.three_points;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigAppStringsTest {

    @Test
    void getStringFor() {
        String actual = ConfigAppStrings.getStringFor("AppWindowTitle");
        String expected = "Constant sum of distances from the red points";
        assertEquals(expected, actual);
    }

    @Test
    void confirmSingletonProperty() {
        // the set of configurations must be unique, so we test that the same
        // string-object is returned twice
        Object instance1 = ConfigAppStrings.getStringFor("AppWindowTitle");
        Object instance2 = ConfigAppStrings.getStringFor("AppWindowTitle");
        assertSame(instance1, instance2);
    }

}