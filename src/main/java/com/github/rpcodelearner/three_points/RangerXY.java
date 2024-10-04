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
}
