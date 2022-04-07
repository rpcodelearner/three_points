package com.github.rpcodelearner.three_points;


public class Ranger {
    private final int firstPixel;
    private final int lastPixel;
    private final double minReal;
    private final double maxReal;
    private final double pixelSize;


    public Ranger(int firstPixel, int lastPixel, double minReal, double maxReal) {
        this.firstPixel = firstPixel;
        this.lastPixel = lastPixel;
        this.minReal = minReal;
        this.maxReal = maxReal;
        pixelSize = (maxReal - minReal) / (lastPixel - firstPixel);
    }

    public int toPixel(double x) {
        return (int) Math.round(firstPixel + (x - minReal) / pixelSize);
    }

    public double toMath(int p) {
        return minReal + (p - firstPixel) * (maxReal - minReal) / (lastPixel - firstPixel);
    }

}
