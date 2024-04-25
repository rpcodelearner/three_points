package com.github.rpcodelearner.three_points.src.main.java;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

class ThreePointsUserChoices {
    final EnumMap<DrawingStyle, Integer> steps;
    final EnumMap<DrawingStyle, Integer> thicknesses;
    int numPts = 3; // this default gives the app its name
    FociPattern currentFociPattern = FociPattern.CIRCULAR;
    DrawingStyle currentDrawingStyle = DrawingStyle.THICK;

    ThreePointsUserChoices() {
        steps = new EnumMap<>(DrawingStyle.class);
        initializeSteps();
        thicknesses = new EnumMap<>(DrawingStyle.class);
        initializeThicknesses();
    }

    private void initializeSteps() {
        steps.put(DrawingStyle.THICK, 300);
        steps.put(DrawingStyle.MEDIUM, 42);
        steps.put(DrawingStyle.FINE, 60);
        steps.put(DrawingStyle.PRECISION, null);
    }

    private void initializeThicknesses() {
        thicknesses.put(DrawingStyle.THICK, 100);
        thicknesses.put(DrawingStyle.MEDIUM, 10);
        thicknesses.put(DrawingStyle.FINE, 5);
        thicknesses.put(DrawingStyle.PRECISION, null);
    }


    public int getNumPts() {
        return numPts;
    }

    public void setNumPts(int num) {
        numPts = num;
    }

    public String[] getFociPatterns() {
        List<String> list = new ArrayList<>();
        for (FociPattern p : FociPattern.values()) list.add(p.name);
        return list.toArray(new String[0]);
    }

    public void setFociPattern(String choice) {
        for (FociPattern p : FociPattern.values()) {
            if (p.name.equals(choice)) currentFociPattern = p;
        }
    }

    public String[] getDrawingStyles() {
        List<String> list = new ArrayList<>();
        for (DrawingStyle ds : DrawingStyle.values()) list.add(ds.name);
        return list.toArray(new String[0]);
    }

    public void setDrawingStyle(String style) {
        for (DrawingStyle ds : DrawingStyle.values()) if (ds.name.equals(style)) currentDrawingStyle = ds;
    }


    enum FociPattern {
        CIRCULAR("Circular"), RANDOM("Random"), ALIGNED("Aligned");

        final String name;
        FociPattern(String arg) {
            name = arg;
        }
    }

    enum DrawingStyle {
        THICK("Thick"), MEDIUM("Medium"), FINE("Fine"), PRECISION("Precision");

        final String name;
        DrawingStyle(String arg) {
            name = arg;
        }
    }

}
