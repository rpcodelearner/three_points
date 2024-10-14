package com.github.rpcodelearner.three_points;

import java.util.Properties;

/**
 * A singleton class to store strings used by the application
 */
class ConfigAppStrings {
    private static ConfigAppStrings INSTANCE;
    private final Properties appStrings;

    private ConfigAppStrings() {
        appStrings = new Properties();

        appStrings.setProperty("AppWindowTitle", "Constant sum of distances from the red points");

        appStrings.setProperty("numPointsLabel", "Foci number:");
        appStrings.setProperty("patternCtrlLabel", "Foci pattern:");
        appStrings.setProperty("graphicsMenuLabel", "Drawing:");

        appStrings.setProperty("numPoints_Tooltip",
                "<html>Number of focal points, put 2 for the classic ellipse." +
                        "<br>Computation takes longer for higher values." +
                        "<br>Use Up/Down (then press Enter or move focus away).</html>");
        appStrings.setProperty("patternCtrl_Tooltip", "Focal points (the red points) will be located according to this pattern" +
                "<br>Press F5 to update Random.");
        appStrings.setProperty("drawingCtrl_Tooltip",
                "<html>Drawing style." +
                        "<br><b>Thick</b>, <b>Medium</b>, <b>Fine</b>: draw bands of roughly constant values." +
                        "<br><b>Precision</b>: draws a few lines of constant value.</html>");
    }

    public static String getStringFor(String key) {
        if (INSTANCE == null) {
            INSTANCE = new ConfigAppStrings();
        }
        return INSTANCE.appStrings.getProperty(key);
    }
}
