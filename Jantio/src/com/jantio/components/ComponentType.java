package com.jantio.components;

import javax.swing.*;
import java.awt.*;

/**
 * GUI bileşen tipleri enum'u
 */
public enum ComponentType {
    BUTTON("Button", JButton.class),
    LABEL("Label", JLabel.class),
    TEXT_FIELD("TextField", JTextField.class),
    TEXT_AREA("TextArea", JTextArea.class),
    CHECK_BOX("CheckBox", JCheckBox.class),
    RADIO_BUTTON("RadioButton", JRadioButton.class),
    COMBO_BOX("ComboBox", JComboBox.class),
    PANEL("Panel", JPanel.class),
    SLIDER("Slider", JSlider.class),
    PROGRESS_BAR("ProgressBar", JProgressBar.class),
    SPINNER("Spinner", JSpinner.class);
    
    private final String displayName;
    private final Class<?> swingClass;
    
    ComponentType(String displayName, Class<?> swingClass) {
        this.displayName = displayName;
        this.swingClass = swingClass;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public Class<?> getSwingClass() {
        return swingClass;
    }
    
    public JComponent createInstance() {
        try {
            return (JComponent) swingClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return new JLabel("Error");
        }
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}
