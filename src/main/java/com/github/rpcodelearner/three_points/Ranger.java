package com.github.rpcodelearner.three_points;

/**
 * A Ranger object maps doubles to and from integers along a line.
 * For example, two Ranger objects can help with plotting graphs by mapping the value
 * of the independent variable to the horizontal pixel-position on the screen and the
 * value of the dependent variable to the pixel-measured offset on the vertical axis.
 * <br>See the constructor for further detail.
 */
public class Ranger {
    private final int firstPixel;
    private final double minReal;
    private final double pixelSize;

    /**
     * A Ranger object maps mathematical numbers (double) to and from pixels along a line.
     * <br>It is OK to specify firstPixel > lastPixel, which is useful, for example,
     * when working with a vertical axis on the screen.
     * <br>It is also OK to specify minReal > maxReal if the user needs.
     * <br> Example for mapping [-1.0, 1.0]x[-1.0, 1.0] onto a 640x480px area.
     * <br> Ranger horizontalAxis = new Ranger(0, 639, -1.0, 1.0);
     * <br> Ranger verticalAxis = new Ranger(479, 0, -1.0, 1.0); // OR (0, 479, 1.0, -1.0)
     * <br> The values at the extremes are included and mapped to the centers of the respective pixels.
     *
     * @param firstPixel integer coordinate of the first pixel of the segment
     * @param lastPixel integer coordinate of the last pixel of the segment
     * @param minReal mathematical value associated to the first pixel
     * @param maxReal mathematical value associated to the last pixel
     */
    public Ranger(int firstPixel, int lastPixel, double minReal, double maxReal) {
        this.firstPixel = firstPixel;
        this.minReal = minReal;
        pixelSize = (maxReal - minReal) / (lastPixel - firstPixel);
    }

    /**
     * Returns the pixel number where the provided value falls.
     * @param v A value along the interval of double-precision numbers.
     * @return Integer coordinate of the pixel.
     */
    public int toPixel(double v) {
        return (int) Math.round(firstPixel + (v - minReal) / pixelSize);
    }

    /**
     * Returns the mathematical coordinate of a pixel
     * @param p integer offset of the pixel
     * @return position of the center of the pixel along the interval
     */
    public double toMath(int p) {
        return minReal + (p - firstPixel) * pixelSize;
    }

}
