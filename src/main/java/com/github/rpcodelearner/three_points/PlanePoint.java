package com.github.rpcodelearner.three_points;


import java.util.Objects;

/**
 * PlanePoint holds a couple of double/s that represent mathematical coordinates in the plane
 */
class PlanePoint {
    public final double x;
    public final double y;

    PlanePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Indicates whether another {@link PlanePoint} is "equal to" this one.
     * <br>INFO: This method has been added to avoid (future) errors, but it is not (yet) in use in any part of code.
     * @param o the other {@link PlanePoint}
     * @return true if x and y have the values in both {@link PlanePoint} objects
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanePoint that = (PlanePoint) o;
        return x==that.x && y==that.y;
    }

    /**
     * Overridden so that hash codes of a,b are the same iff a.equals(b)
     * @return Objects.hash(x, y);
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}


