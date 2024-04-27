package com.github.rpcodelearner.three_points;

import java.util.EnumMap;

/**
 * Data about bands, relevant to {@link ThreePointsModel}
 * The available choices are defined in {@link ThreePointsUserChoices}
 */
class ThreePointsBands {
    final EnumMap<ThreePointsUserChoices.DrawingStyle, Integer> steps;
    final EnumMap<ThreePointsUserChoices.DrawingStyle, Integer> thicknesses;

    ThreePointsBands() {
        steps = new EnumMap<>(ThreePointsUserChoices.DrawingStyle.class);
        initializeSteps();
        thicknesses = new EnumMap<>(ThreePointsUserChoices.DrawingStyle.class);
        initializeThicknesses();
    }


    private void initializeSteps() {
        steps.put(ThreePointsUserChoices.DrawingStyle.THICK, 300);
        steps.put(ThreePointsUserChoices.DrawingStyle.MEDIUM, 42);
        steps.put(ThreePointsUserChoices.DrawingStyle.FINE, 60);
        steps.put(ThreePointsUserChoices.DrawingStyle.PRECISION, null);
    }

    private void initializeThicknesses() {
        thicknesses.put(ThreePointsUserChoices.DrawingStyle.THICK, 100);
        thicknesses.put(ThreePointsUserChoices.DrawingStyle.MEDIUM, 10);
        thicknesses.put(ThreePointsUserChoices.DrawingStyle.FINE, 5);
        thicknesses.put(ThreePointsUserChoices.DrawingStyle.PRECISION, null);
    }

}
