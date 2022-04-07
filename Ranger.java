package com.github.rpcodelearner.three_points;


public class Ranger {
    private final int firstPixel;
    private final int lastPixel;
    private final double minReal;
    private final double maxReal;

    public Ranger(int firstPixel, int lastPixel, double minReal, double maxReal) {
        this.firstPixel = firstPixel;
        this.lastPixel = lastPixel;
        this.minReal = minReal;
        this.maxReal = maxReal;
    }

    public int toPixel(double x) {
        return (int) (firstPixel + (x - minReal) * (lastPixel - firstPixel) / (maxReal - minReal));
    }

    public double toMath(int p) {
        return minReal + (p - firstPixel) * (maxReal - minReal) / (lastPixel - firstPixel);
    }

}
