package com.github.rpcodelearner.three_points;

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
     * value computed by equation has changed above/below a given 'level'
     * @param equation an implementation of Equation
     * @param point a PlanePoint, a simple object to represent a mathematical point
     * @param level a reference level (double)
     * @return (boolean) whether the value computed through equation crosses 'level' in the neighbourhood of 'point'
     */
    public  boolean crossesLevel(Equation equation, PlanePoint point, double level) {
        double x = point.x;
        double y = point.y;
        PlanePoint leftUpperCorner = new PlanePoint(x - pixelSizeX / 2.0, y - pixelSizeY / 2.0);
        PlanePoint rightUpperCorner = new PlanePoint(x + pixelSizeX / 2.0, y - pixelSizeY / 2.0);
        PlanePoint leftLowerCorner = new PlanePoint(x - pixelSizeX / 2.0, y + pixelSizeY / 2.0);
        PlanePoint rightLowerCorner = new PlanePoint(x + pixelSizeX / 2.0, y + pixelSizeY / 2.0);

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
