package com.github.rpcodelearner.three_points;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;

class ThreePointsMenuBar extends JMenuBar {
    private static final String NUM_POINTS_TOOLTIP_TEXT = ConfigAppStrings.getStringFor("numPoints_Tooltip");
    private static final String PATTERN_CTRL_TOOLTIP_TEXT = ConfigAppStrings.getStringFor("patternCtrl_Tooltip");
    private static final String DRAWING_CTRL_TOOLTIP_TEXT = ConfigAppStrings.getStringFor("drawingCtrl_Tooltip");
    private final ThreePointsUserChoices userChoices;
    private final ThreePointsModel model;
    private final JPanel view;
    private final EmptyBorder customEmptyBorder = new EmptyBorder(0, 5, 0, 5);
    private final MenuKeyListener menuKeyListener = new MenuKeyListener();
    private JTextField numPointsInputField;
    private JComboBox<String> patternCtrl;
    private JComboBox<String> drawingCtrl;

    ThreePointsMenuBar(ThreePointsModel model, ThreePointsUserChoices userChoices, JPanel view) {
        this.model = model;
        this.userChoices = userChoices;
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
        numPointsInputField = new JTextField(Integer.toString(userChoices.getNumPts()));
        numPointsInputField.setMinimumSize(new Dimension(45, 24));
        numPointsInputField.setMaximumSize(new Dimension(45, 24));
        numPointsInputField.setHorizontalAlignment(JTextField.RIGHT);
        numPointsInputField.addActionListener(this::changePointsNumber);
        numPointsInputField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (isNumPointsUpdated()) {
                    model.computeFoci();
                }
                numPointsInputField.setText(Integer.toString(userChoices.getNumPts()));

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
        String[] patterns = userChoices.getFociPatternsArray();
        patternCtrl = new JComboBox<>(patterns);
        patternCtrl.setMaximumSize(new Dimension(93, 24));
        patternCtrl.setSelectedItem(patterns[0]);
        patternCtrl.addActionListener(this::selectPattern);
        patternCtrl.addKeyListener(menuKeyListener);
        patternCtrl.setToolTipText(PATTERN_CTRL_TOOLTIP_TEXT);
        add(patternCtrl);
    }

    public void selectPattern(ActionEvent ignoredChoice) {
        userChoices.setFociPattern((String) patternCtrl.getSelectedItem());
        model.computeFoci();
        view.repaint();
    }

    private void addGraphicsMenuLabel() {
        final JLabel graphicsMenuLabel = new JLabel(ConfigAppStrings.getStringFor("graphicsMenuLabel"));
        graphicsMenuLabel.setBorder(customEmptyBorder);
        graphicsMenuLabel.setToolTipText(DRAWING_CTRL_TOOLTIP_TEXT);
        add(graphicsMenuLabel);
    }

    private void addGraphicsMenu() {
        drawingCtrl = new JComboBox<>(userChoices.getDrawingStylesArray());
        drawingCtrl.setMaximumSize(new Dimension(95, 24));
        drawingCtrl.addActionListener(this::selectDrawingStyle);
        drawingCtrl.addKeyListener(menuKeyListener);
        drawingCtrl.setToolTipText(DRAWING_CTRL_TOOLTIP_TEXT);
        add(drawingCtrl);
    }

    private void selectDrawingStyle(ActionEvent ignoredActionEvent) {
        userChoices.setDrawingStyle((String) drawingCtrl.getSelectedItem());
        view.repaint();
    }

    private void changePointsNumber(ActionEvent ignoredTextInput) {
        if (isNumPointsUpdated()) {
            model.computeFoci();
        }
        numPointsInputField.setText(Integer.toString(userChoices.getNumPts()));
        view.repaint();
    }

    private boolean isNumPointsUpdated() {
        final int validReading = getValidReading();
        if (validReading != userChoices.getNumPts()) {
            userChoices.setNumPts(validReading);
            return true;
        }
        return false;
    }

    private int getValidReading() {
        try {
            int result = Integer.parseInt(numPointsInputField.getText());
            if (result > 0) return result;
        } catch (NumberFormatException ignored) {
        }
        return userChoices.getNumPts();
    }

    class MenuKeyListener extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            keyReleasedOnNumPoints(e);
        }

        private void keyReleasedOnNumPoints(KeyEvent e) {
            if (e.getSource().equals(numPointsInputField)) {
                if (KeyEvent.getKeyText(e.getKeyCode()).matches("[a-zA-Z]")) {
                    numPointsInputField.setText(String.valueOf(userChoices.getNumPts()));
                }
            }
        }

        @Override
        public void keyPressed(KeyEvent e) {
            keyPressedF5(e);
            keyPressedOnNumPoints(e);
        }

        private void keyPressedOnNumPoints(KeyEvent e) {
            if (e.getSource().equals(numPointsInputField)) {
                int currentNumPoints = getValidReading();
                if (KeyEvent.getKeyText(e.getKeyCode()).equals("Up")) {
                    numPointsInputField.setText(String.valueOf(currentNumPoints + 1));
                }
                if (KeyEvent.getKeyText(e.getKeyCode()).equals("Down")) {
                    numPointsInputField.setText(String.valueOf(currentNumPoints - 1));
                }
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    model.computeFoci();
                    view.repaint();
                }
            }
        }

        private void keyPressedF5(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_F5) {
                if (userChoices.getFociPattern().toLowerCase().contains("random")) {
                    if (getValidReading() != userChoices.getNumPts()) {
                        userChoices.setNumPts(getValidReading());
                    }
                    model.computeFoci();
                    view.repaint();
                }
            }
        }

    }

}
