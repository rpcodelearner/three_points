package com.github.rpcodelearner.three_points.src.main.java;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;

class ThreePointsMenuBar extends JMenuBar {
    private static final String NUM_POINTS_TOOLTIP_TEXT = ConfigAppStrings.getStringFor("numPoints_Tooltip");
    private static final String PATTERN_CTRL_TOOLTIP_TEXT = ConfigAppStrings.getStringFor("patternCtrl_Tooltip");
    private static final String DRAWING_CTRL_TOOLTIP_TEXT = ConfigAppStrings.getStringFor("drawingCtrl_Tooltip");
    private final ThreePointsModel model;
    private final JPanel view;
    private final EmptyBorder customEmptyBorder = new EmptyBorder(0, 5, 0, 5);
    private JTextField numPointsInputField;
    private JComboBox<String> patternCtrl;
    private JComboBox<String> drawingCtrl;
    private final MenuKeyListener menuKeyListener = new MenuKeyListener();

    ThreePointsMenuBar(ThreePointsModel model, JPanel view) {
        this.model = model;
        this.view = view;

        addNumPointsLabel();
        addNumPointTextField();

        addPatternCtrlLabel();
        addPatternsCtrl();

        addGraphicsMenuLabel();
        addGraphicsMenu();

        ToolTipManager.sharedInstance().setDismissDelay(15000);
    }

    private void addNumPointsLabel() {
        final JLabel numPointsLabel = new JLabel(ConfigAppStrings.getStringFor("numPointsLabel"));
        numPointsLabel.setBorder(customEmptyBorder);
        numPointsLabel.setToolTipText(NUM_POINTS_TOOLTIP_TEXT);
        add(numPointsLabel);
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
        numPointsInputField.addKeyListener(menuKeyListener);
        numPointsInputField.setToolTipText(NUM_POINTS_TOOLTIP_TEXT);
        add(numPointsInputField);
    }

    private void addPatternCtrlLabel() {
        final JLabel patternCtrlLabel = new JLabel(ConfigAppStrings.getStringFor("patternCtrlLabel"));
        patternCtrlLabel.setBorder(customEmptyBorder);
        patternCtrlLabel.setToolTipText(PATTERN_CTRL_TOOLTIP_TEXT);
        add(patternCtrlLabel);
    }

    private void addPatternsCtrl() {
        String[] patterns = model.getFociPatterns();
        patternCtrl = new JComboBox<>(patterns);
        patternCtrl.setSelectedItem(patterns[0]);
        patternCtrl.addActionListener(this::selectPattern);
        patternCtrl.addKeyListener(menuKeyListener);
        patternCtrl.setToolTipText(PATTERN_CTRL_TOOLTIP_TEXT);
        add(patternCtrl);
    }

    public void selectPattern(ActionEvent ignoredChoice) {
        model.setFociPattern((String) patternCtrl.getSelectedItem());
        view.repaint();
    }

    private void addGraphicsMenuLabel() {
        final JLabel graphicsMenuLabel = new JLabel(ConfigAppStrings.getStringFor("graphicsMenuLabel"));
        graphicsMenuLabel.setBorder(customEmptyBorder);
        graphicsMenuLabel.setToolTipText(DRAWING_CTRL_TOOLTIP_TEXT);
        add(graphicsMenuLabel);
    }

    private void addGraphicsMenu() {
        drawingCtrl = new JComboBox<>(model.getDrawingStyles());
        drawingCtrl.addActionListener(this::selectDrawingStyle);
        drawingCtrl.addKeyListener(menuKeyListener);
        drawingCtrl.setToolTipText(DRAWING_CTRL_TOOLTIP_TEXT);
        add(drawingCtrl);
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

    class MenuKeyListener extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_F5) {
                userChoices.setNumPts(tryGettingNumPoints());
                userChoices.setFociPattern((String) patternCtrl.getSelectedItem());
                model.computePoints();
                view.repaint();
            }
            if (e.getSource().equals(numPointsInputField)) {
                int currentNumber = tryGettingNumPoints();
                if (KeyEvent.getKeyText(e.getKeyCode()).equals("Up")) {
                    numPointsInputField.setText(String.valueOf(++currentNumber));
                }
                if (KeyEvent.getKeyText(e.getKeyCode()).equals("Down")) {
                    numPointsInputField.setText(String.valueOf(--currentNumber));
                }
            } else {
                super.keyPressed(e);
            }
        }
    }

}
