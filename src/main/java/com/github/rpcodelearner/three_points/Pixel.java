package com.github.rpcodelearner.three_points;

import java.util.Objects;

/**
 * A value object to store pixel coordinates
 */
class Pixel {
    public final int x;
    public final int y;

    Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        Pixel pixel = (Pixel) other;
        return x == pixel.x && y == pixel.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}