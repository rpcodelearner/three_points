package com.github.rpcodelearner.three_points;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The purpose of {@link ThreePointsMenuBarWhiteBoxTest} is to do a high-coverage test of {@link ThreePointsMenuBar}.
 * <br>By "white-box testing" we mean that we <i>know</i> about the internals of the software under test,
 * but we act strictly through the class interface, like a client would.
 * <br><br>
 * <b>WARNING</b>: the tests below make use of the Focus Subsystem and may not work if the mouse or the keyboard
 * bring the focus outside the windows that are instantiated. It is recommended <u>not to interact with the computer</u> while the
 * tests are running.
 */
class ThreePointsMenuBarWhiteBoxTest {
    private final static int NUM_POINTS_INDEX = 1;
    private final static int FOCI_PATTERN_INDEX = 3;
    private final static int DRAWING_STYLE_INDEX = 5;
    TestFrame testFrame;

    @BeforeEach
    void setUp() {
        try {
            SwingUtilities.invokeAndWait(() -> testFrame = new TestFrame());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    void tearDown() {
        testFrame.dispose();
        pause(150);
        assertFalse(testFrame.isActive());
        assertFalse(testFrame.isVisible());
    }

    @Test
    void numPointsArrowKeys() {
        JTextField numPointsJTextField = (JTextField) testFrame.getJMenuBar().getComponent(NUM_POINTS_INDEX);
        numPointsJTextField.requestFocusInWindow();
        pause(200);
        pressNonUnicodeKey(numPointsJTextField, KeyEvent.VK_UP);
        assertEquals("4", numPointsJTextField.getText());
        pressNonUnicodeKey(numPointsJTextField, KeyEvent.VK_DOWN);
        assertEquals("3", numPointsJTextField.getText());
    }

    @Test
    void numPointsTypeDifferentNumber() {
        JTextField numPointsJTextField = (JTextField) testFrame.getJMenuBar().getComponent(NUM_POINTS_INDEX);
        pressNonUnicodeKey(numPointsJTextField, KeyEvent.VK_BACK_SPACE);
        typeUnicodeChar(numPointsJTextField, '5');
        pressNonUnicodeKey(numPointsJTextField, KeyEvent.VK_ENTER);
        assertEquals("5", numPointsJTextField.getText());
    }

    @Test
    void numPointsTypeSameNumber() {
        JTextField numPointsJTextField = (JTextField) testFrame.getJMenuBar().getComponent(NUM_POINTS_INDEX);
        numPointsJTextField.requestFocusInWindow();
        pause(200);
        pressNonUnicodeKey(numPointsJTextField, KeyEvent.VK_BACK_SPACE);
        typeUnicodeChar(numPointsJTextField, '3');
        pressNonUnicodeKey(numPointsJTextField, KeyEvent.VK_ENTER);
        assertEquals("3", numPointsJTextField.getText());
    }

    @Test
    void numPointsInvalidNumericInput() {
        JTextField numPointsJTextField = (JTextField) testFrame.getJMenuBar().getComponent(NUM_POINTS_INDEX);
        numPointsJTextField.requestFocusInWindow();
        pause(200);
        pressNonUnicodeKey(numPointsJTextField, KeyEvent.VK_BACK_SPACE);
        typeUnicodeChar(numPointsJTextField, '0');
        pressNonUnicodeKey(numPointsJTextField, KeyEvent.VK_ENTER);
        assertEquals("3", numPointsJTextField.getText());
    }

    @Test
    void numPointsInvalidNonNumericInputA() {
        JTextField numPointsJTextField = (JTextField) testFrame.getJMenuBar().getComponent(NUM_POINTS_INDEX);
        pressNonUnicodeKey(numPointsJTextField, KeyEvent.VK_BACK_SPACE);
        typeUnicodeChar(numPointsJTextField, 'a');
        pressNonUnicodeKey(numPointsJTextField, KeyEvent.VK_ENTER);
        assertEquals("3", numPointsJTextField.getText());
    }

    @Test
    void numPointsInvalidNonNumericInputB() {
        JTextField numPointsJTextField = (JTextField) testFrame.getJMenuBar().getComponent(NUM_POINTS_INDEX);
        pressNonUnicodeKey(numPointsJTextField, KeyEvent.VK_B);
        assertEquals("3", numPointsJTextField.getText());
    }

    @Test
    void numPointsUpdatedFocusLost() {
        JTextField numPointsJTextField = (JTextField) testFrame.getJMenuBar().getComponent(NUM_POINTS_INDEX);
        numPointsJTextField.requestFocusInWindow();
        pause(200);
        pressNonUnicodeKey(numPointsJTextField, KeyEvent.VK_BACK_SPACE);
        typeUnicodeChar(numPointsJTextField, '1');
        pressNonUnicodeKey(numPointsJTextField, KeyEvent.VK_TAB);
        assertFalse(numPointsJTextField.isFocusOwner());
        assertEquals("1", numPointsJTextField.getText());
    }

    @Test
    void numPointsUnchangedFocusLost() {
        JTextField numPointsJTextField = (JTextField) testFrame.getJMenuBar().getComponent(NUM_POINTS_INDEX);
        numPointsJTextField.requestFocusInWindow();
        pause(200);
        assertTrue(numPointsJTextField.isFocusOwner());
        pressNonUnicodeKey(numPointsJTextField, KeyEvent.VK_TAB);
        assertFalse(numPointsJTextField.isFocusOwner());
        assertEquals("3", numPointsJTextField.getText());
    }

    @Test
    void fociPatterSelection() {
        // We suppress the unchecked-cast warning because we expect the test to fail if the code under test changes
        @SuppressWarnings({"unchecked"}) JComboBox<String> fociPatternCombo = (JComboBox<String>) testFrame.getJMenuBar().getComponent(FOCI_PATTERN_INDEX);
        fociPatternCombo.setSelectedIndex(1); // an ActionEvent object is automatically produced
        assertEquals("Random", fociPatternCombo.getSelectedItem());
    }

    @Test
    void fociPatterF5() {
        // We suppress the unchecked-cast warning because we expect the test to fail if the code under test changes
        @SuppressWarnings({"unchecked"}) JComboBox<String> fociPatternCombo = (JComboBox<String>) testFrame.getJMenuBar().getComponent(FOCI_PATTERN_INDEX);
        final String currentSelection = (String) fociPatternCombo.getSelectedItem();
        pressNonUnicodeKey(fociPatternCombo, KeyEvent.VK_F5);
        assertEquals(currentSelection, fociPatternCombo.getSelectedItem());
    }

    @Test
    void drawingStyleSelection() {
        // We suppress the unchecked-cast warning because we expect the test to fail if the code under test changes
        @SuppressWarnings({"unchecked"}) JComboBox<String> drawingStyleCombo = (JComboBox<String>) testFrame.getJMenuBar().getComponent(DRAWING_STYLE_INDEX);
        drawingStyleCombo.setSelectedIndex(1); // an ActionEvent object is automatically produced
        assertEquals("Medium", drawingStyleCombo.getSelectedItem());
    }

    private void pressNonUnicodeKey(Component component, int keyCode) {
        component.dispatchEvent(new KeyEvent(component, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED));
        pause(50);
        component.dispatchEvent(new KeyEvent(component, KeyEvent.KEY_RELEASED, System.currentTimeMillis(), 0, keyCode, KeyEvent.CHAR_UNDEFINED));
        pause(50);
    }

    private void typeUnicodeChar(Component component, char keyChar) {
        component.dispatchEvent(new KeyEvent(component, KeyEvent.KEY_TYPED, System.currentTimeMillis(), 0, KeyEvent.VK_UNDEFINED, keyChar));
        pause(50);
    }

    /**
     * Small try-catch wrapper around Thread.sleep().
     * <br>The use of pauses/sleeping allows some time for the software under test to process the
     * [emulated] events it receives, or for components to obtain the focus after requesting it.
     *
     * @param time_ms Sleeping time in millisecond
     */
    private void pause(int time_ms) {
        try {
            Thread.sleep(time_ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * A containing-window and dependencies for {@link ThreePointsMenuBar}
     */
    private static class TestFrame extends JFrame {

        TestFrame() {
            ThreePointsUserChoices userChoices = new ThreePointsUserChoices();
            ThreePointsModel model = new ThreePointsModel(userChoices);
            final JPanel view = new ThreePointsPanel(model);
            ThreePointsMenuBar menu = new ThreePointsMenuBar(model, userChoices, view);
            this.setJMenuBar(menu);
            this.add(view);
            this.pack();
            this.setVisible(true);
        }

    }

}