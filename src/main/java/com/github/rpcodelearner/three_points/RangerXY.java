package com.github.rpcodelearner.three_points;

import java.awt.*;

/**
 * RangerXY maps pixels to mathematical points and vice versa
 */
class RangerXY {
    final int firstPixel = 0;
    Dimension pixelDim;
    PlanePoint bottomLeft;
    PlanePoint topRight;

    RangerXY(Dimension pixelDim, PlanePoint bottomLeft, PlanePoint topRight) {
        this.pixelDim = pixelDim;
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
    }

    double getPixelSizeX() {
        return (topRight.x - bottomLeft.x) / pixelDim.getWidth();
    }

    double getPixelSizeY() {
        return (topRight.y - bottomLeft.y) / pixelDim.getHeight();
    }

    private int toPixelX(double x) {
        return (int) Math.round(firstPixel + (x - bottomLeft.x) / getPixelSizeX());
    }

    private int toPixelY(double y) {
        return (int) Math.round(firstPixel + (y - bottomLeft.y) / getPixelSizeY());
    }

    Pixel toPixel(PlanePoint point) {
        return new Pixel(toPixelX(point.x), toPixelY(point.y));
    }

    private double toMathX(int pixel) {
        return bottomLeft.x + (pixel - firstPixel) * getPixelSizeX();
    }

    private double toMathY(int pixel) {
        return bottomLeft.y + (pixel - firstPixel) * getPixelSizeY();
    }

    PlanePoint toMath(Pixel pixel) {
        return new PlanePoint(toMathX(pixel.x), toMathY(pixel.y));
    }

    void setPixelDim(Dimension dimension) {
        pixelDim = dimension;
    }

    void setCornerPoints(PlanePoint bottomLeft, PlanePoint topRight) {
        this.bottomLeft = bottomLeft;
        this.topRight = topRight;
    }

    void rescaleX(double xFactor) {
        final double midPoint = (topRight.x + bottomLeft.x) / 2.0;
        final double halfSize = (topRight.x - midPoint) * xFactor;
        bottomLeft = new PlanePoint(midPoint - halfSize, bottomLeft.y);
        topRight = new PlanePoint(midPoint + halfSize, topRight.y);
    }

    void rescaleY(double yFactor) {
        final double midPoint = (topRight.y + bottomLeft.y) / 2.0;
        final double halfSize = (topRight.y - midPoint) * yFactor;
        bottomLeft = new PlanePoint(bottomLeft.x, midPoint - halfSize);
        topRight = new PlanePoint(topRight.x, midPoint + halfSize);
    }

    void recenter(PlanePoint updatedCenter) {
        bottomLeft = new PlanePoint(bottomLeft.x + updatedCenter.x, bottomLeft.y + updatedCenter.y);
        topRight = new PlanePoint(topRight.x + updatedCenter.x, topRight.y + updatedCenter.y);
    }

    void setMathCenterToPixel(Pixel newCenter) {
        double xOffset = toMathX(newCenter.x);
        double yOffset = toMathY(newCenter.y);
        recenter(new PlanePoint(-xOffset, -yOffset));
    }

}
