package com.github.rpcodelearner.three_points;

import javax.swing.*;
import java.awt.event.ActionEvent;

class ThreePointsMenuBar extends JMenuBar {
    private final ThreePointsModel model;
    private final JPanel view;
    private JTextField numPointsInputField;
    private JComboBox<String> patternCtrl;
    private JComboBox<String> drawingCtrl;

    public ThreePointsMenuBar(ThreePointsModel model, JPanel view) {
        this.model = model;
        this.view = view;

        this.add(new JLabel(" Focal points: "));
        addNumPointTextField(model);

        this.add(new JLabel(" Foci pattern: "));
        addPatternsCtrl();

        this.add(new JLabel(" Drawing: "));
        addGraphicsMenu();
    }

    private void addNumPointTextField(ThreePointsModel model) {
        numPointsInputField = new JTextField(Integer.toString(model.getNumPts()));
        numPointsInputField.setHorizontalAlignment(JTextField.RIGHT);
        numPointsInputField.addActionListener(this::changePointsNumber);
        this.add(numPointsInputField);
    }

    private void addPatternsCtrl() {
        String[] patterns = model.getFociPatterns();
        patternCtrl = new JComboBox<>(patterns);
        this.add(patternCtrl);
        patternCtrl.setSelectedItem(patterns[0]);
        patternCtrl.addActionListener(this::selectPattern);
    }

    public void selectPattern(ActionEvent choice) {
        tryGettingAndForwardingNumPoints();
        model.selector.selectPattern((String) patternCtrl.getSelectedItem());
        view.repaint();
    }

    private void addGraphicsMenu() {
        drawingCtrl = new JComboBox<>(model.getDrawingStyles());
        this.add(drawingCtrl);
        drawingCtrl.addActionListener(this::selectDrawingStyle);
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
