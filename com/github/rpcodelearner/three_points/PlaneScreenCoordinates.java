package com.github.rpcodelearner.three_points;


/**
 * PlaneScreenCoordinates converts coordinates of a portion of the mathematical plane
 * to coordinates of a portion of the screen and vice-versa.
 * It is up to the client to decide what to map to what; this class performs only
 * the relevant numerical computations.
 * <br>"plane" implies the x,y plane coordinates
 * <br>PlanePoint holds a (double x, double y) couple
 * <br>"screen" refers to x,y screen coordinates as integers and the
 * <br>Pixel holds a (int x, int y) couple of screen coordinates
 */
public class PlaneScreenCoordinates {
    private int screenWidth;
    private int screenHeight;
    private double planeMinX, planeMinY, planeMaxX, planeMaxY;

    public PlaneScreenCoordinates(int width, int height) {
        screenWidth = width;
        screenHeight = height;
        planeMinX = -1.0;
        planeMaxX = 1.0;
        planeMinY = -1.0;
        planeMaxY = 1.0;
    }

    public void setScreenSize(int width, int height) {
        screenHeight = height;
        screenWidth = width;
    }

    public void setPlaneLimitsX(double minX, double maxX) {
        planeMinX = minX;
        planeMaxX = maxX;
    }

    public void setPlaneLimitsY(double minY, double maxY) {
        planeMinY = minY;
        planeMaxY = maxY;
    }


    public Pixel planePointToPixel(PlanePoint point) {
        return planeToPixel(point.x, point.y);
    }

    public Pixel planeToPixel(double x, double y) {
        double xRatio = Math.abs((x - planeMinX) / (planeMaxX - planeMinX));
        int xScreen = (int) (screenWidth * xRatio);

        double yRatio = Math.abs((y - planeMaxY) / (planeMinY - planeMaxY));
        int yScreen = (int) (screenHeight * yRatio);

        return new Pixel(xScreen, yScreen);
    }

    public PlanePoint pixelToPlanePoint(Pixel pixel) {
        return screenToPlanePoint(pixel.x, pixel.y);
    }

    public PlanePoint screenToPlanePoint(int xS, int yS) {
        double xC = (planeMaxX - planeMinX) * (double) xS / screenWidth + planeMinX;
        double yC = planeMaxY - (planeMaxY - planeMinY) * (double) yS / screenHeight;
        return new PlanePoint(xC, yC);
    }


    public static class Pixel {
        public final int x;
        public final int y;

        public Pixel(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static class PlanePoint {
        public final double x;
        public final double y;

        public PlanePoint(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

}
