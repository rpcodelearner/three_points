package com.github.rpcodelearner.three_points;


import com.github.rpcodelearner.three_points.PlaneScreenCoordinates.PlanePoint;
import com.github.rpcodelearner.three_points.plot.RasterMaster;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

class ThreePointsModel {
    private static final double RADIUS = 2.0 / 3; // tuned for viewing
    private static final int PRECISION = 1000;
    private final double xCenter = 0.0;
    private final double yCenter = 0.0;
    private final RasterMaster rasterMaker = new RasterMaster(2.0 / 512);
    private final EnumMap<DrawingStyle, Integer> steps;
    private final EnumMap<DrawingStyle, Integer> thicknesses;
    private DrawingStyle currentDrawingStyle = DrawingStyle.THICK;
    private FociPattern currentFociPattern = FociPattern.CIRCULAR;
    private static List<PlanePoint> focalPoints = null;
    private int numPts = 3; // this default gives the app its name

    ThreePointsModel() {
        steps = new EnumMap<>(DrawingStyle.class);
        initializeSteps();
        thicknesses = new EnumMap<>(DrawingStyle.class);
        initializeThicknesses();
        computePointsOnCircle(); // initializes focalPoints
    }

    private void initializeThicknesses() {
        thicknesses.put(DrawingStyle.THICK, 100);
        thicknesses.put(DrawingStyle.MEDIUM, 10);
        thicknesses.put(DrawingStyle.FINE, 5);
        thicknesses.put(DrawingStyle.PRECISION, null);
    }

    private void initializeSteps() {
        steps.put(DrawingStyle.THICK, 300);
        steps.put(DrawingStyle.MEDIUM, 42);
        steps.put(DrawingStyle.FINE, 60);
        steps.put(DrawingStyle.PRECISION, null);
    }

    public List<PlanePoint> getFoci() {
        return focalPoints;
    }

    public int getNumPts() {
        return numPts;
    }

    public void setNumPts(int num) {
        if (num < 1) return;
        this.numPts = num;
        computePoints();
    }

    public String[] getDrawingStyles() {
        List<String> list = new ArrayList<>();
        for (DrawingStyle ds : DrawingStyle.values()) list.add(ds.name);
        return list.toArray(new String[0]);
    }

    public void setDrawingStyle(String style) {
        for (DrawingStyle ds : DrawingStyle.values()) if (ds.name.equals(style)) currentDrawingStyle = ds;
    }


    private double computeSumDistance(PlanePoint point) {
        double dist = 0.0;
        for (PlanePoint focus : focalPoints) {
            dist += Math.sqrt(Math.pow(point.x - focus.x, 2) + Math.pow(point.y - focus.y, 2));
        }
        return dist;
    }

    private boolean isWithinBand(PlanePoint point) {
        double totDist = computeSumDistance(point);
        return (totDist * PRECISION) % steps.get(currentDrawingStyle) <= thicknesses.get(currentDrawingStyle);
    }

    public boolean isPlot(PlanePoint point) {
        if (currentDrawingStyle != DrawingStyle.PRECISION) return isWithinBand(point);
        for (double level = 0.125; level < numPts; level += 0.5 / numPts)
            if (rasterMaker.crossesLevel(this::computeSumDistance, point, level)) return true;
        return false;
    }

    private void computePointsOnCircle() {
        focalPoints = new ArrayList<>();

        double x0 = 0.0;
        double y0 = (numPts > 1) ? RADIUS : 0;
        PlanePoint pt = new PlanePoint(x0 + xCenter, yCenter - y0);
        focalPoints.add(pt);

        double angle = 2 * Math.PI / numPts;
        for (int n = 1; n < numPts; n++) {
            double x = Math.cos(n * angle) * x0 + Math.sin(n * angle) * y0;
            double y = -Math.sin(n * angle) * x0 + Math.cos(n * angle) * y0;
            pt = new PlanePoint(x + xCenter, yCenter - y);
            focalPoints.add(pt);
        }
    }

    private void computeAlignedPoints() {
        focalPoints = new ArrayList<>();

        if (numPts == 1) {
            focalPoints.add(new PlanePoint(xCenter, yCenter));
            return;
        }

        double step = (2 * RADIUS) / (numPts - 1);

        for (int i = 0; i < numPts; i++) {
            double x = xCenter - RADIUS + i * step;
            focalPoints.add(new PlanePoint(x, yCenter));
        }
    }

    private void computeRandomPoints() {
        focalPoints = new ArrayList<>();

        if (numPts == 1) {
            focalPoints.add(new PlanePoint(xCenter, yCenter));
            return;
        }
        int i = 0;
        do {
            double x = Math.random() * 2 * RADIUS - RADIUS;
            double y = Math.random() * 2 * RADIUS - RADIUS;
            if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) < RADIUS) {
                focalPoints.add(new PlanePoint(x, y));
                i++;
            }
        } while (i < numPts);
    }

    public String[] getFociPatterns() {
        List<String> list = new ArrayList<>();
        for (FociPattern p : FociPattern.values()) list.add(p.name);
        return list.toArray(new String[0]);
        //return selector.patterns;
    }

    public void setFociPattern(String choice) {
        for (FociPattern p : FociPattern.values())
            if (p.name.equals(choice)) currentFociPattern = p;
        computePoints();
    }

    private enum DrawingStyle {
        THICK("Thick"), MEDIUM("Medium"), FINE("Fine"), PRECISION("Precision");

        final String name;

        DrawingStyle(String arg) {
            name = arg;
        }
    }

    private enum FociPattern {
        CIRCULAR("Circular"), RANDOM("Random"), ALIGNED("Aligned");

        final String name;

        FociPattern(String arg) {
            name = arg;
        }
    }

    private void computePoints() {
        switch (currentFociPattern.name) {
            case "Circular":
                computePointsOnCircle();
                break;
            case "Random":
                computeRandomPoints();
                break;
            case "Aligned":
                computeAlignedPoints();
                break;
            default:
                System.err.println("undefined pattern for focal points" + currentFociPattern);
        }
    }
}
