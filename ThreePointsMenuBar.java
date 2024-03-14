package com.github.rpcodelearner.three_points;

import javax.swing.*;
import java.awt.event.ActionEvent;

class ThreePointsMenuBar extends JMenuBar {
    private final ThreePointsModel model;
    private final JPanel view;
    private static final String NUMPOINTS_TOOLTIP_TEXT = "Number of focal points, put 2 for the classic ellipse";
    private static final String PATTERN_CTRL_TOOLTIP_TEXT = "Focal points (the red points) will be put according to this pattern";
    private static final String DRAWING_CTRL_TOOLTIP_TEXT = "<html>" +
            "How to draw. Scan the window pixel by pixel." +
            "<br>Precision checks if the sum of distances crosses any of a few given level within that pixel." +
            "<br>The other three methods roughly check if the sum of distances is withing given range(s)</html>";
    private JTextField numPointsInputField;
    private JComboBox<String> patternCtrl;
    private JComboBox<String> drawingCtrl;

    ThreePointsMenuBar(ThreePointsModel model, JPanel view) {
        this.model = model;
        this.view = view;

        final JLabel numPointsLabel = new JLabel(" Foci number: ");
        numPointsLabel.setToolTipText(NUMPOINTS_TOOLTIP_TEXT);
        this.add(numPointsLabel);
        addNumPointTextField(model);

        final JLabel patternCtrlLabel = new JLabel(" Foci pattern: ");
        patternCtrlLabel.setToolTipText(PATTERN_CTRL_TOOLTIP_TEXT);
        this.add(patternCtrlLabel);
        addPatternsCtrl();

        final JLabel graphicsMenuLabel = new JLabel(" Drawing: ");
        graphicsMenuLabel.setToolTipText(DRAWING_CTRL_TOOLTIP_TEXT);
        this.add(graphicsMenuLabel);
        addGraphicsMenu();

        ToolTipManager.sharedInstance().setDismissDelay(15000);
    }

    private void addNumPointTextField(ThreePointsModel model) {
        numPointsInputField = new JTextField(Integer.toString(model.getNumPts()));
        numPointsInputField.setHorizontalAlignment(JTextField.RIGHT);
        numPointsInputField.addActionListener(this::changePointsNumber);
        numPointsInputField.setToolTipText(NUMPOINTS_TOOLTIP_TEXT);
        this.add(numPointsInputField);
    }

    private void addPatternsCtrl() {
        String[] patterns = model.getFociPatterns();
        patternCtrl = new JComboBox<>(patterns);
        patternCtrl.setSelectedItem(patterns[0]);
        patternCtrl.addActionListener(this::selectPattern);
        patternCtrl.setToolTipText(PATTERN_CTRL_TOOLTIP_TEXT);
        this.add(patternCtrl);
    }

    public void selectPattern(ActionEvent choice) {
        tryGettingAndForwardingNumPoints();
        model.setFociPattern((String) patternCtrl.getSelectedItem());
        view.repaint();
    }

    private void addGraphicsMenu() {
        drawingCtrl = new JComboBox<>(model.getDrawingStyles());
        drawingCtrl.addActionListener(this::selectDrawingStyle);
        drawingCtrl.setToolTipText(DRAWING_CTRL_TOOLTIP_TEXT);
        this.add(drawingCtrl);
    }

    private void selectDrawingStyle(ActionEvent actionEvent) {
        tryGettingAndForwardingNumPoints();
        model.setDrawingStyle((String) drawingCtrl.getSelectedItem());
        view.repaint();
    }


    private void changePointsNumber(ActionEvent textInput) {
        tryGettingAndForwardingNumPoints();
        view.repaint();
    }

    private void tryGettingAndForwardingNumPoints() {
        try {
            int num = Integer.parseInt(numPointsInputField.getText());
            model.setNumPts(num);
        } catch (NumberFormatException e) {
            numPointsInputField.setText("");
        }
    }

}
