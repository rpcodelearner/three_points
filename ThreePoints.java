package ex.rfusr.ex02_3Points;

import javax.swing.*;

import static javax.swing.SwingUtilities.invokeLater;

// TODO list
// - reconsider where createAndSetupWindow() and createAndShowGUI() should belong
// - manage window resizing
// NICE TO HAVE
// - add key controls: ESC -> Exit, +/- -> add/remove a focus and redraw, F5 -> force redraw (makes sense for RND positions)

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