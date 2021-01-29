package com.github.rpcodelearner.three_points.plot;

import com.github.rpcodelearner.three_points.PlaneScreenCoordinates;

public class RasterMaster {
    private final double pixelSizeX;
    private final double pixelSizeY;

    public RasterMaster(double pixelSize) {
        this.pixelSizeX = pixelSize;
        this.pixelSizeY = pixelSize;
    }

    public RasterMaster(double pixelSizeX, double pixelSizeY) {
        this.pixelSizeX = pixelSizeX;
        this.pixelSizeY = pixelSizeY;
    }

    public  boolean crossesLevel(Equation equation, PlaneScreenCoordinates.PlanePoint point, double level) {
        double x = point.x;
        double y = point.y;
        PlaneScreenCoordinates.PlanePoint leftUpperCorner = new PlaneScreenCoordinates.PlanePoint(x - pixelSizeX / 2.0, y - pixelSizeY / 2.0);
        PlaneScreenCoordinates.PlanePoint rightUpperCorner = new PlaneScreenCoordinates.PlanePoint(x + pixelSizeX / 2.0, y - pixelSizeY / 2.0);
        PlaneScreenCoordinates.PlanePoint leftLowerCorner = new PlaneScreenCoordinates.PlanePoint(x - pixelSizeX / 2.0, y + pixelSizeY / 2.0);
        PlaneScreenCoordinates.PlanePoint rightLowerCorner = new PlaneScreenCoordinates.PlanePoint(x + pixelSizeX / 2.0, y + pixelSizeY / 2.0);

        double lu = equation.compute(leftUpperCorner) - level;
        double ru = equation.compute(rightUpperCorner) - level;
        double ll = equation.compute(leftLowerCorner) - level;
        double rl = equation.compute(rightLowerCorner) - level;
        double center = equation.compute(point) - level;

        // TODO invent a test [pattern] that shows the actual effect of this if-statement, if any
        if (lu * rl < 0 && ru * ll < 0) return true;

        if (lu * rl < 0 || ru * ll < 0) {
            if (Math.abs(lu - rl) > Math.abs(2*center)) return true;
            if (Math.abs(ru - ll) > Math.abs(2*center)) return true;
        }
        return false;
    }
}
