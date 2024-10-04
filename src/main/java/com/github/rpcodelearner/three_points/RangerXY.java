package com.github.rpcodelearner.three_points;

import java.awt.*;

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

    int toPixelX(double x) {
        return (int) Math.round(firstPixel + (x - bottomLeft.x) / getPixelSizeX());
    }

    int toPixelY(double y) {
        return (int) Math.round(firstPixel + (y - bottomLeft.y) / getPixelSizeY());
    }

    double toMathX(int pixel) {
        return bottomLeft.x + (pixel - firstPixel) * getPixelSizeX();
    }

    double toMathY(int pixel) {
        return bottomLeft.y + (pixel - firstPixel) * getPixelSizeY();
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
}
