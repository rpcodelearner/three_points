package com.github.rpcodelearner.three_points;

import javax.swing.*;

import static javax.swing.SwingUtilities.invokeLater;


public class ThreePoints {

    public ThreePoints() {
        final ThreePointsModel model = new ThreePointsModel();
        invokeLater(() -> createAndShowGUI(model));
    }

    public static void main(String[] args) {
        new ThreePoints();
    }

    private void createAndShowGUI(ThreePointsModel model) {
        final JFrame window = createAndSetupWindow();
        final JPanel view = new ThreePointsPanel(model);

        window.setJMenuBar(new ThreePointsMenuBar(model, view));
        window.add(view);

        window.pack();
        window.setVisible(true);
    }

    public JFrame createAndSetupWindow() {
        JFrame frame = new JFrame();
        frame.setLocation(800, 150);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setUndecorated(false);
        frame.setTitle("Bands of constant sum of distances from the red points");
        return frame;
    }

}