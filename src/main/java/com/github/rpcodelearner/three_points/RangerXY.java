package com.github.rpcodelearner.three_points;

import java.awt.*;
import java.awt.geom.Dimension2D;

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

    double getPixelSize() {
        return (topRight.x - bottomLeft.x) / pixelDim.getWidth();
    }

    int toPixelX(double x) {
        return (int) Math.round(firstPixel + (x - bottomLeft.x) / getPixelSize());
    }

    double toMath(int pixel) {
        return bottomLeft.x + (pixel - firstPixel) * getPixelSize();
    }
}
