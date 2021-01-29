package com.github.rpcodelearner.three_points;


import com.github.rpcodelearner.three_points.PlaneScreenCoordinates.PlanePoint;
import com.github.rpcodelearner.three_points.plot.RasterMaster;


import java.util.ArrayList;
import java.util.List;

public class ThreePointsModel {

    static final double RADIUS = 2.0 / 3; // tuned for viewing
    static List<PlanePoint> focusPoints = null;
    final PatternSelector selector = new PatternSelector();
    final BandsStyle bandsStyle = new BandsStyle();
    private final double xCenter = 0.0;
    private final double yCenter = 0.0;
    private int numPts = 3; // this default gives the app its name
    private RasterMaster rasterMaker = new RasterMaster(2.0/512);

    public List<PlanePoint> getPoints() {
        if (focusPoints == null)
            selector.computePoints();
        return focusPoints;
    }

    public int getNumPts() {
        return numPts;
    }

    public void setNumPts(int num) {
        if (num < 1)
            return;
        this.numPts = num;
        selector.computePoints();
    }

    public String[] getDrawingStyles() {
        return bandsStyle.drawingStyles;
    }

    public void setDrawingStyle(String style) {
        bandsStyle.setStyle(style);
    }


    private double computeSumDistance(PlanePoint point) {
        if (focusPoints == null)
            selector.computePoints();
        double dist = 0.0;
        for (PlanePoint focus : focusPoints) {
            dist += Math.sqrt(Math.pow(point.x - focus.x, 2)
                    + Math.pow(point.y - focus.y, 2));
        }
        return dist;
    }

    public boolean isWithinBand(PlanePoint point) {
        double totDist = computeSumDistance(point);
        return (totDist * bandsStyle.precision) % bandsStyle.step <= bandsStyle.thickness;
    }

    public boolean isPlot(PlaneScreenCoordinates.PlanePoint point) {
        if (bandsStyle.step != 0)
            return isWithinBand(point);
        if (rasterMaker == null)
            throw new RuntimeException(this.getClass() + ": RasterMaster object not initialized");
        for (double level = 0.125 ; level < numPts; level += 0.5/numPts) {
            if (rasterMaker.crossesLevel((p) -> computeSumDistance(p), point, level)) {
                return true;
            }
        }
        return false;
    }


    private void computeRegularPoints() {
        focusPoints = new ArrayList<>();

        double x0 = 0.0;
        double y0 = (numPts > 1) ? RADIUS : 0;
        PlanePoint pt = new PlanePoint(x0 + xCenter, yCenter - y0);
        focusPoints.add(pt);

        double angle = 2 * Math.PI / numPts;
        for (int n = 1; n < numPts; n++) {
            double x = Math.cos(n * angle) * x0 + Math.sin(n * angle) * y0;
            double y = -Math.sin(n * angle) * x0 + Math.cos(n * angle) * y0;
            pt = new PlanePoint(x + xCenter, yCenter - y);
            focusPoints.add(pt);
        }
    }

    private void computeAlignedPoints() {
        focusPoints = new ArrayList<>();

        if (numPts == 1) {
            focusPoints.add(new PlanePoint(xCenter, yCenter));
            return;
        }

        double step = (2 * RADIUS) / (numPts - 1);

        for (int i = 0; i < numPts; i++) {
            double x = xCenter - RADIUS + i * step;
            focusPoints.add(new PlanePoint(x, yCenter));
        }
    }

    private void computeRandomPoints() {
        focusPoints = new ArrayList<>();

        if (numPts == 1) {
            focusPoints.add(new PlanePoint(xCenter, yCenter));
            return;
        }
        int i = 0;
        do {
            double x = Math.random() * 2 * RADIUS - RADIUS;
            double y = Math.random() * 2 * RADIUS - RADIUS;
            if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) < RADIUS) {
                focusPoints.add(new PlanePoint(x, y));
                i++;
            }
        } while (i < numPts);
    }

    public String[] getFociPatterns() {
        return selector.patterns;
    }

    static class BandsStyle {
        private final String[] drawingStyles = {"Thick", "Medium", "Fine", "Precision"};
        private final int precision = 1000;
        private int step;
        private int thickness;

        public BandsStyle() {
            setStyle("Thick");
        }

        public void setStyle(String style) {
            switch (style) {
                case "Thick":
                    step = 300;
                    thickness = 100;
                    break;
                case "Medium":
                    step = 42;
                    thickness = 10;
                    break;
                case "Fine":
                    step = 60;
                    thickness = 5;
                    break;
                case "Precision":
                    step = 0;
                    thickness = 0;
                    break;
                default:
                    System.err.println("Undefined drawing style: " + style);
            }

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
                    System.err.println("undefined pattern for focus points: " + pattern);
            }
        }

    }

}
