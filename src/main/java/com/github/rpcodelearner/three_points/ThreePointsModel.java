package com.github.rpcodelearner.three_points;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ThreePointsModel {
    private static final double RADIUS = 2.0 / 3; // tuned for viewing
    private final double xCenter = 0.0;
    private final double yCenter = 0.0;
    private final RasterMaster rasterMaker = new RasterMaster(2.0 / 512);
    private final ThreePointsUserChoices userChoices;
    private final ThreePointsBands bands = new ThreePointsBands();
    private List<PlanePoint> focalPoints = null;

    ThreePointsModel(ThreePointsUserChoices userChoices) {
        this.userChoices = userChoices;
        computePoints(); // must call to initialize focalPoints
    }

    List<PlanePoint> getFoci() {
        return focalPoints;
    }

    void computePoints() {
        if (userChoices.getNumPts() == 1) {
            focalPoints = Collections.singletonList(new PlanePoint(xCenter, yCenter));
            return;
        }
        switch (userChoices.getFociPattern()) {
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
                throw new RuntimeException("Unexpected pattern for focal points: " + userChoices.getFociPattern());
        }
    }

    private void computePointsOnCircle() {
        focalPoints = new ArrayList<>();
        final double angle = 2 * Math.PI / userChoices.getNumPts();
        for (int n = 0; n < userChoices.getNumPts(); n++) {
            double x = Math.cos(n * angle) * xCenter + Math.sin(n * angle) * RADIUS;
            double y = -Math.sin(n * angle) * xCenter + Math.cos(n * angle) * RADIUS;
            PlanePoint pt = new PlanePoint(x + xCenter, yCenter - y);
            focalPoints.add(pt);
        }
    }

    private void computeAlignedPoints() {
        focalPoints = new ArrayList<>();
        final double step = (2 * RADIUS) / (userChoices.getNumPts() - 1);
        for (int i = 0; i < userChoices.getNumPts(); i++) {
            double x = xCenter - RADIUS + i * step;
            focalPoints.add(new PlanePoint(x, yCenter));
        }
    }

    private void computeRandomPoints() {
        focalPoints = new ArrayList<>();
        int i = 0;
        do {
            double x = (Math.random() * 2 * RADIUS) - RADIUS;
            double y = (Math.random() * 2 * RADIUS) - RADIUS;
            if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) < RADIUS) {
                focalPoints.add(new PlanePoint(x, y));
                i++;
            }
        } while (i < userChoices.getNumPts());
    }

    boolean isPlot(PlanePoint point) {
        if (userChoices.currentDrawingStyle != ThreePointsUserChoices.DrawingStyle.PRECISION) {
            return isWithinBand(point);
        }
        // magic numbers chosen to obtain a reasonable drawing
        for (double level = 0.125; level < userChoices.getNumPts(); level += 0.5 / userChoices.getNumPts()) {
            if (rasterMaker.crossesLevel(this::computeSumDistance, point, level)) {
                return true;
            }
        }
        return false;
    }

    private boolean isWithinBand(PlanePoint point) {
        final int precision = 1000;
        double totDist = computeSumDistance(point);
        return (totDist * precision) % bands.steps.get(userChoices.currentDrawingStyle) <= bands.thicknesses.get(userChoices.currentDrawingStyle);
    }


    private double computeSumDistance(PlanePoint point) {
        double dist = 0.0;
        for (PlanePoint focus : focalPoints) {
            dist += Math.sqrt(Math.pow(point.x - focus.x, 2) + Math.pow(point.y - focus.y, 2));
        }
        return dist;
    }

}
