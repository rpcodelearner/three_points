package com.github.rpcodelearner.three_points.src.main.java;

import javax.swing.*;

import static javax.swing.SwingUtilities.invokeLater;


public class ThreePoints {

    public ThreePoints() {
        final ThreePointsUserChoices userChoices = new ThreePointsUserChoices();
        final ThreePointsModel model = new ThreePointsModel(userChoices);
        invokeLater(() -> createAndShowGUI(model, userChoices));
    }

    public static void main(String[] args) {
        new ThreePoints();
    }

    private void createAndShowGUI(ThreePointsModel model, ThreePointsUserChoices userChoices) {
        final JFrame window = createAndSetupWindow();
        final JPanel view = new ThreePointsPanel(model);

        window.setJMenuBar(new ThreePointsMenuBar(model, userChoices, view));
        window.add(view);

        window.pack();
        window.setVisible(true);
    }

    private JFrame createAndSetupWindow() {
        JFrame frame = new JFrame();
        frame.setLocation(800, 150);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.setResizable(false);
        frame.setTitle(ConfigAppStrings.getStringFor("AppWindowTitle"));
        return frame;
    }

}