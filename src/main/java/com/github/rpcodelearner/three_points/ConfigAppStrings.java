package com.github.rpcodelearner.three_points;

import java.util.Properties;

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
                "<html>Number of focal points, put 2 for the classic ellipse" +
                        "<br>Increase gradually, as computation takes longer for higher values." +
                        "<br>You can use Up/Down arrows (but you also have to press Enter or move the focus away).</html>");
        appStrings.setProperty("patternCtrl_Tooltip", "Focal points (the red points) will be located according to this pattern");
        appStrings.setProperty("drawingCtrl_Tooltip",
                "<html>Drawing style." +
                        "<br><b>Thick</b>, <b>Medium</b> and <b>Fine</b> each draw a set of bands of roughly constant value." +
                        "<br><b>Precision</b> draws a few lines of constant value.</html>");
    }

    public static String getStringFor(String key) {
        if (INSTANCE == null) {
            INSTANCE = new ConfigAppStrings();
        }
        return INSTANCE.appStrings.getProperty(key);
    }
}
