package com.github.rpcodelearner.three_points;

import java.util.ArrayList;
import java.util.List;

class ThreePointsUserChoices {
    int numPts = 3; // this default gives the app its name
    FociPattern currentFociPattern = FociPattern.CIRCULAR;
    DrawingStyle currentDrawingStyle = DrawingStyle.THICK;

    public int getNumPts() {
        return numPts;
    }

    public void setNumPts(int num) {
        numPts = num;
    }

    public String[] getFociPatternsArray() {
        List<String> list = new ArrayList<>();
        for (FociPattern p : FociPattern.values()) {
            list.add(p.name);
        }
        return list.toArray(new String[0]);
    }

    public String getFociPattern() {
        return currentFociPattern.name;
    }

    public void setFociPattern(String choice) {
        for (FociPattern p : FociPattern.values()) {
            if (p.name.equals(choice)) {
                currentFociPattern = p;
            }
        }
    }

    public String[] getDrawingStylesArray() {
        List<String> list = new ArrayList<>();
        for (DrawingStyle ds : DrawingStyle.values()) {
            list.add(ds.name);
        }
        return list.toArray(new String[0]);
    }

    public void setDrawingStyle(String style) {
        for (DrawingStyle ds : DrawingStyle.values()) {
            if (ds.name.equals(style)) {
                currentDrawingStyle = ds;
            }
        }
    }


    private enum FociPattern {
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
