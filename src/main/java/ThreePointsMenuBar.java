package com.github.rpcodelearner.three_points.src.main.java;

import javax.swing.*;
import java.awt.event.*;

class ThreePointsMenuBar extends JMenuBar {
    private final ThreePointsModel model;
    private final JPanel view;
    private static final String NUM_POINTS_TOOLTIP_TEXT = "<html>Number of focal points, put 2 for the classic ellipse<br>Increase gradually, as computation takes longer for higher values." +
            "<br>You can use Up/Down arrows (but you also have to press Enter or move the focus away).</html>";
    private static final String PATTERN_CTRL_TOOLTIP_TEXT = "Focal points (the red points) will be located according to this pattern";
    private static final String DRAWING_CTRL_TOOLTIP_TEXT = "<html>" +
            "Drawing style." +
            "<br><b>Thick</b>, <b>Medium</b> and <b>Fine</b> each draw a set of bands of roughly constant value." +
            "<br><b>Precision</b> draws a few lines of constant value.</html>";
    private JTextField numPointsInputField;
    private JComboBox<String> patternCtrl;
    private JComboBox<String> drawingCtrl;

    ThreePointsMenuBar(ThreePointsModel model, JPanel view) {
        this.model = model;
        this.view = view;

        final JLabel numPointsLabel = new JLabel(" Foci number: ");
        numPointsLabel.setToolTipText(NUM_POINTS_TOOLTIP_TEXT);
        this.add(numPointsLabel);
        addNumPointTextField();

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

    private void addNumPointTextField() {
        numPointsInputField = new JTextField(Integer.toString(model.getNumPts()));
        numPointsInputField.setHorizontalAlignment(JTextField.RIGHT);
        numPointsInputField.addActionListener(this::changePointsNumber);
        numPointsInputField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                int numPoints = tryGettingNumPoints();
                if (numPoints != model.getNumPts()) model.setNumPts(numPoints);
                numPointsInputField.setText(Integer.toString(numPoints));

                view.repaint();
            }
        });
        numPointsInputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int currentNumber = tryGettingNumPoints();
                if (KeyEvent.getKeyText(e.getKeyCode()).equals("Up")) {
                    numPointsInputField.setText(String.valueOf(++currentNumber));
                }
                if (KeyEvent.getKeyText(e.getKeyCode()).equals("Down")) {
                    numPointsInputField.setText(String.valueOf(--currentNumber));
                }
            }
        });
        numPointsInputField.setToolTipText(NUM_POINTS_TOOLTIP_TEXT);
        this.add(numPointsInputField);
    }

    private void addPatternsCtrl() {
        String[] patterns = model.getFociPatterns();
        patternCtrl = new JComboBox<>(patterns);
        patternCtrl.setSelectedItem(patterns[0]);
        patternCtrl.addActionListener(this::selectPattern);
        patternCtrl.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F5) {
                    model.setFociPattern((String) patternCtrl.getSelectedItem());
                    view.repaint();
                }
            }
        });
        patternCtrl.setToolTipText(PATTERN_CTRL_TOOLTIP_TEXT);
        this.add(patternCtrl);
    }

    public void selectPattern(ActionEvent ignoredChoice) {
        model.setFociPattern((String) patternCtrl.getSelectedItem());
        view.repaint();
    }

    private void addGraphicsMenu() {
        drawingCtrl = new JComboBox<>(model.getDrawingStyles());
        drawingCtrl.addActionListener(this::selectDrawingStyle);
        drawingCtrl.setToolTipText(DRAWING_CTRL_TOOLTIP_TEXT);
        this.add(drawingCtrl);
    }

    private void selectDrawingStyle(ActionEvent ignoredActionEvent) {
        model.setDrawingStyle((String) drawingCtrl.getSelectedItem());
        view.repaint();
    }

    private void changePointsNumber(ActionEvent ignoredTextInput) {
        int numPoints = tryGettingNumPoints();
        if (numPoints != model.getNumPts()) model.setNumPts(numPoints);
        numPointsInputField.setText(Integer.toString(numPoints));

        view.repaint();
    }

    private int tryGettingNumPoints() {
        int numPoints = model.getNumPts();
        try {
            final int readNumber = Integer.parseInt(numPointsInputField.getText());
            if (readNumber > 0) numPoints = readNumber;
        } catch (NumberFormatException ignored) {
        }
        return numPoints;
    }

}
