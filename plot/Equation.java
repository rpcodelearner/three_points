package com.github.rpcodelearner.three_points.plot;

import com.github.rpcodelearner.three_points.PlaneScreenCoordinates;

public interface Equation {
    double compute(PlaneScreenCoordinates.PlanePoint point);
}
