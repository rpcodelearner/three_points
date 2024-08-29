package com.github.rpcodelearner.three_points;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
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
        numPointsInputField.setHorizontalAlignment(JTextField.RIGHT);
        numPointsInputField.addActionListener(this::changePointsNumber);
        numPointsInputField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (isNumPointsUpdated()) {
                    model.computePoints();
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
        patternCtrl.setSelectedItem(patterns[0]);
        patternCtrl.addActionListener(this::selectPattern);
        patternCtrl.addKeyListener(menuKeyListener);
        patternCtrl.setToolTipText(PATTERN_CTRL_TOOLTIP_TEXT);
        add(patternCtrl);
    }

    public void selectPattern(ActionEvent ignoredChoice) {
        userChoices.setFociPattern((String) patternCtrl.getSelectedItem());
        model.computePoints();
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
            model.computePoints();
        }
        numPointsInputField.setText(Integer.toString(userChoices.getNumPts()));

        view.repaint();
    }

    private boolean isNumPointsUpdated() {
        try {
            final int readNumber = Integer.parseInt(numPointsInputField.getText());
            if (readNumber > 0) {
                userChoices.setNumPts(readNumber);
                return true;
            }
        } catch (NumberFormatException ignored) {
            numPointsInputField.setText(String.valueOf(userChoices.getNumPts()));
        }
        return false;
    }


    class MenuKeyListener extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getSource().equals(numPointsInputField)) {
                if (KeyEvent.getKeyText(e.getKeyCode()).matches("[a-zA-Z]")) {
                    numPointsInputField.setText(String.valueOf(userChoices.getNumPts()));
                }
            }
            super.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_F5) {
                userChoices.setFociPattern((String) patternCtrl.getSelectedItem());
                model.computePoints();
                view.repaint();
            }
            if (e.getSource().equals(numPointsInputField)) {
                if (KeyEvent.getKeyText(e.getKeyCode()).matches("[a-zA-Z]")) {
                    numPointsInputField.setText(String.valueOf(userChoices.getNumPts()));
                }
                if (KeyEvent.getKeyText(e.getKeyCode()).equals("Up")) {
                    userChoices.setNumPts(userChoices.getNumPts() + 1);
                    numPointsInputField.setText(String.valueOf(userChoices.getNumPts()));
                }
                if (KeyEvent.getKeyText(e.getKeyCode()).equals("Down")) {
                    userChoices.setNumPts(userChoices.getNumPts() - 1);
                    numPointsInputField.setText(String.valueOf(userChoices.getNumPts()));
                }
            }
            super.keyPressed(e);
        }
    }

}
