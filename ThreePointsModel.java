package ex.rfusr.ex02_3Points;


import ex.rfusr.ex02_3Points.PlaneScreenCoordinates.PlanePoint;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ThreePointsModel {

    static final double RADIUS = 2.0 / 3; // tuned for viewing
    static List<PlanePoint> focusPoints = null;
    final PatternSelector selector = new PatternSelector();
    private final double xCenter = 0.0;
    private final double yCenter = 0.0;
    private int numPts = 3; // default

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

    // TODO this method reasons about visibility: should the "blackness duty cycle" be guided from outside?
    public boolean isWithinBand(PlanePoint point) {
        double totDist = computeSumDistance(point);

        final int precision = 1000;
        final int step = 500;
        final int thickness = 256;
        return (int) (totDist * precision) % step <= thickness;
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

    class PatternSelector {

        private String pattern = "Regular"; // default choice

        public void selectPattern(ActionEvent choice) {
            pattern = choice.getActionCommand();
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
