package ex.rfusr.ex02_3Points;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ThreePointsMenuBar extends JMenuBar {
    private static final boolean SELECTED = true;
    private static final boolean NOT_SELECTED = false;
    private final ButtonGroup patterns = new ButtonGroup();
    private final ThreePointsModel model;
    private final JPanel view;
    private JTextField numPointsInputField;

    public ThreePointsMenuBar(ThreePointsModel model, JPanel view) {
        this.model = model;
        this.view = view;
        this.add(new JLabel(" Focus points: "));
        this.setMargin(new Insets(0, 1, 10, 1));

        addNumPointTextField(model);
        addPatternsCtrl();
        addGraphicMenu();
    }

    private void addNumPointTextField(ThreePointsModel model) {
        numPointsInputField = new JTextField(Integer.toString(model.getNumPts()));
        numPointsInputField.setHorizontalAlignment(JTextField.RIGHT);
        numPointsInputField.addActionListener(this::changePointsNumber);
        this.add(numPointsInputField);
    }

    private void addPatternsCtrl() {
        this.add(new JLabel(" Pattern: "));
        addRadioButtonItem("Regular", SELECTED);
        addRadioButtonItem("Random", NOT_SELECTED);
        addRadioButtonItem("Aligned", NOT_SELECTED);
    }

    private void addRadioButtonItem(String text, boolean isSelected) {
        JRadioButtonMenuItem btn = new JRadioButtonMenuItem(text, isSelected);
        patterns.add(btn);
        this.add(btn);
        btn.addActionListener(this::selectPattern);
    }

    private void addGraphicMenu() {
        // TODO allow to select features of the drawing, like thickness, shading, step...
        //  or combinations of the above, like "thick", "fine", "shaded"
    }

    public void selectPattern(ActionEvent choice) {
        tryGettingAndForwardingNumPoints();
        model.selector.selectPattern(choice);
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
