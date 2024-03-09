package com.github.rpcodelearner.three_points;


import com.github.rpcodelearner.three_points.PlaneScreenCoordinates.PlanePoint;
import com.github.rpcodelearner.three_points.plot.RasterMaster;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

class ThreePointsModel {

    private static final double RADIUS = 2.0 / 3; // tuned for viewing
    private static final int PRECISION = 1000;
    static List<PlanePoint> focalPoints = null;
    final PatternSelector selector = new PatternSelector();
    final EnumMap<DrawingStyle, Integer> steps;
    final EnumMap<DrawingStyle, Integer> thicknesses;
    private final double xCenter = 0.0;
    private final double yCenter = 0.0;
    private final RasterMaster rasterMaker = new RasterMaster(2.0 / 512);
    private DrawingStyle currentDrawingStyle = DrawingStyle.THICK;
    private int numPts = 3; // this default gives the app its name

    ThreePointsModel() {
        steps = new EnumMap<>(DrawingStyle.class);
        steps.put(DrawingStyle.THICK, 300);
        steps.put(DrawingStyle.MEDIUM, 42);
        steps.put(DrawingStyle.FINE, 60);
        steps.put(DrawingStyle.PRECISION, null);
        thicknesses = new EnumMap<>(DrawingStyle.class);
        thicknesses.put(DrawingStyle.THICK, 100);
        thicknesses.put(DrawingStyle.MEDIUM, 10);
        thicknesses.put(DrawingStyle.FINE, 5);
        thicknesses.put(DrawingStyle.PRECISION, null);
    }

    public List<PlanePoint> getFoci() {
        if (focalPoints == null) selector.computePoints();
        return focalPoints;
    }

    public int getNumPts() {
        return numPts;
    }

    public void setNumPts(int num) {
        if (num < 1) return;
        this.numPts = num;
        selector.computePoints();
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
        if (focalPoints == null) selector.computePoints();
        double dist = 0.0;
        for (PlanePoint focus : focalPoints) {
            dist += Math.sqrt(Math.pow(point.x - focus.x, 2) + Math.pow(point.y - focus.y, 2));
        }
        return dist;
    }

    public boolean isWithinBand(PlanePoint point) {
        double totDist = computeSumDistance(point);
        return (totDist * PRECISION) % steps.get(currentDrawingStyle) <= thicknesses.get(currentDrawingStyle);
    }

    public boolean isPlot(PlanePoint point) {
        if (currentDrawingStyle != DrawingStyle.PRECISION)
            return isWithinBand(point);
        for (double level = 0.125; level < numPts; level += 0.5 / numPts)
            if (rasterMaker.crossesLevel(this::computeSumDistance, point, level))
                return true;
        return false;
    }

    private void computeRegularPoints() {
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
        return selector.patterns;
    }

    enum DrawingStyle {
        THICK("Thick"), MEDIUM("Medium"), FINE("Fine"), PRECISION("Precision");

        final String name;

        DrawingStyle(String arg) {
            name = arg;
        }
    }

    class PatternSelector {
        private final String[] patterns = new String[]{"Regular", "Random", "Aligned"};
        private String pattern = "Regular";

        public void selectPattern(String choice) {
            pattern = choice;
            computePoints();
        }

        void computePoints() {
            switch (pattern) {
                case "Regular":
                    computeRegularPoints();
                    break;
                case "Random":
                    computeRandomPoints();
                    break;
                case "Aligned":
                    computeAlignedPoints();
                    break;
                default:
                    System.err.println("undefined pattern for focal points: " + pattern);
            }
        }
    }
}
