package com.github.rpcodelearner.three_points;

import java.util.List;

/**
 * This class helps with "precision" drawing style.
 * It knows about pixel sizes from its constructor/s and its
 * method crossesLevel(...) checks whether a given equation
 * crosses a given level at a given point in the plane.
 */
class RasterMaster {
    private final double pixelSizeX;
    private final double pixelSizeY;

    RasterMaster(double pixelSize) {
        this.pixelSizeX = pixelSize;
        this.pixelSizeY = pixelSize;
    }

    /**
     * This method computes values of 'equation' around 'point' and checks whether the
     * value computed by equation has changed above/below each given 'level'
     * @param equation an implementation of Equation
     * @param point a PlanePoint, a simple object to represent a mathematical point
     * @param levels a list of reference levels
     * @return (boolean) whether the value computed through equation crosses 'level' in the neighbourhood of 'point'
     */
    public  boolean crossesLevels(Equation equation, PlanePoint point, List<Double> levels) {
        double x = point.x;
        double y = point.y;
        final double precision = 2.0;
        final PlanePoint leftUpperCorner = new PlanePoint(x - pixelSizeX / precision, y - pixelSizeY / precision);
        final PlanePoint rightUpperCorner = new PlanePoint(x + pixelSizeX / precision, y - pixelSizeY / precision);
        final PlanePoint leftLowerCorner = new PlanePoint(x - pixelSizeX / precision, y + pixelSizeY / precision);
        final PlanePoint rightLowerCorner = new PlanePoint(x + pixelSizeX / precision, y + pixelSizeY / precision);
        final double lUC = equation.compute(leftUpperCorner);
        final double rUC = equation.compute(rightUpperCorner);
        final double lLC = equation.compute(leftLowerCorner);
        final double rLC = equation.compute(rightLowerCorner);
        final double ctr = equation.compute(point);

        for (double level : levels) {
            double lu = lUC - level;
            double ru = rUC - level;
            double ll = lLC - level;
            double rl = rLC - level;
            double center = ctr - level;

            if (lu * rl < 0 && ru * ll < 0) {
                return true;
            }
            if (lu * rl < 0 || ru * ll < 0) {
                if (Math.abs(lu - rl) > Math.abs(2*center)) {
                    return true;
                }
                if (Math.abs(ru - ll) > Math.abs(2*center)) {
                    return true;
                }
            }
        }
        return false;
    }
}
