package com.jantio.ui;

import com.jantio.components.ComponentType;
import com.jantio.components.DesignerComponent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Özellikler paneli - seçili bileşenin özelliklerini düzenler
 */
public class PropertiesPanel extends JPanel {
    
    private DesignerComponent selectedComponent;
    private Map<String, JTextField> textFields = new HashMap<>();
    private Map<String, JComboBox<?>> comboFields = new HashMap<>();
    private Map<String, JCheckBox> checkFields = new HashMap<>();
    private JButton colorPickerButton;
    
    public PropertiesPanel() {
        initComponents();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 60)),
            "Özellikler",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(100, 100, 120)
        ));
        setBackground(new Color(45, 45, 55));
        
        JScrollPane scrollPane = new JScrollPane(createContentPanel());
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(45, 45, 55));
        
        // X pozisyonu
        panel.add(createPropertyRow("X:", "x", true));
        // Y pozisyonu
        panel.add(createPropertyRow("Y:", "y", true));
        // Genişlik
        panel.add(createPropertyRow("Genişlik:", "width", true));
        // Yükseklik
        panel.add(createPropertyRow("Yükseklik:", "height", true));
        // Text
        panel.add(createPropertyRow("Text:", "text", false));
        // Font
        panel.add(createFontSelector());
        // Foreground Color
        panel.add(createColorSelector("Ön Plan:", "foreground"));
        // Background Color
        panel.add(createColorSelector("Arka Plan:", "background"));
        // Enabled
        panel.add(createCheckBoxProperty("Aktif:", "enabled"));
        // Visible
        panel.add(createCheckBoxProperty("Görünür:", "visible"));
        
        panel.add(Box.createVerticalGlue());
        
        return panel;
    }
    
    private JPanel createPropertyRow(String labelText, String propertyName, boolean isNumeric) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(new Color(45, 45, 55));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        JLabel label = new JLabel(labelText);
        label.setForeground(new Color(180, 180, 200));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JTextField textField = new JTextField(10);
        textField.setBackground(new Color(35, 35, 45));
        textField.setForeground(new Color(220, 220, 230));
        textField.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70), 1));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        if (isNumeric) {
            textField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) {
                    updateComponent(propertyName, textField.getText(), true);
                }
                
                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) {
                    updateComponent(propertyName, textField.getText(), true);
                }
                
                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) {
                    updateComponent(propertyName, textField.getText(), true);
                }
            });
        } else {
            textField.addActionListener(e -> 
                updateComponent(propertyName, textField.getText(), false)
            );
            textField.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    updateComponent(propertyName, textField.getText(), false);
                }
            });
        }
        
        textFields.put(propertyName, textField);
        
        row.add(label);
        row.add(textField);
        
        return row;
    }
    
    private JPanel createFontSelector() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(new Color(45, 45, 55));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        JLabel label = new JLabel("Font:");
        label.setForeground(new Color(180, 180, 200));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        String[] fontNames = {"Arial", "Segoe UI", "Times New Roman", "Courier New", "Verdana", "Tahoma"};
        JComboBox<String> fontCombo = new JComboBox<>(fontNames);
        fontCombo.setBackground(new Color(35, 35, 45));
        fontCombo.setForeground(new Color(220, 220, 230));
        fontCombo.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        fontCombo.setMaximumRowCount(6);
        
        fontCombo.addActionListener(e -> {
            if (selectedComponent != null) {
                String fontName = (String) fontCombo.getSelectedItem();
                Font currentFont = selectedComponent.getComponent().getFont();
                Font newFont = new Font(fontName, currentFont.getStyle(), currentFont.getSize());
                selectedComponent.setProperty("font", newFont);
                refreshProperties();
            }
        });
        
        comboFields.put("fontName", fontCombo);
        
        row.add(label);
        row.add(fontCombo);
        
        return row;
    }
    
    private JPanel createColorSelector(String labelText, String propertyName) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(new Color(45, 45, 55));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        
        JLabel label = new JLabel(labelText);
        label.setForeground(new Color(180, 180, 200));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        JButton button = new JButton("Seç");
        button.setBackground(new Color(60, 60, 70));
        button.setForeground(new Color(220, 220, 230));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            if (selectedComponent != null) {
                Color initialColor = selectedComponent.getComponent().getForeground();
                if ("background".equals(propertyName)) {
                    initialColor = selectedComponent.getComponent().getBackground();
                }
                
                Color chosenColor = JColorChooser.showDialog(
                    this,
                    "Renk Seç",
                    initialColor
                );
                
                if (chosenColor != null) {
                    selectedComponent.setProperty(propertyName, chosenColor);
                    refreshProperties();
                }
            }
        });
        
        row.add(label);
        row.add(button);
        
        return row;
    }
    
    private JPanel createCheckBoxProperty(String labelText, String propertyName) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        row.setBackground(new Color(45, 45, 55));
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JCheckBox checkBox = new JCheckBox(labelText);
        checkBox.setForeground(new Color(180, 180, 200));
        checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        checkBox.setBackground(new Color(45, 45, 55));
        checkBox.setFocusPainted(false);
        
        checkBox.addActionListener(e -> {
            if (selectedComponent != null) {
                selectedComponent.setProperty(propertyName, checkBox.isSelected());
            }
        });
        
        checkFields.put(propertyName, checkBox);
        
        row.add(checkBox);
        
        return row;
    }
    
    private void updateComponent(String propertyName, String value, boolean isNumeric) {
        if (selectedComponent == null || value.isEmpty()) return;
        
        try {
            if (isNumeric) {
                int numValue = Integer.parseInt(value);
                switch (propertyName) {
                    case "x":
                        selectedComponent.setX(numValue);
                        break;
                    case "y":
                        selectedComponent.setY(numValue);
                        break;
                    case "width":
                        selectedComponent.setWidth(numValue);
                        break;
                    case "height":
                        selectedComponent.setHeight(numValue);
                        break;
                }
            } else {
                selectedComponent.setProperty(propertyName, value);
            }
        } catch (NumberFormatException e) {
            // Geçersiz sayısal değer, görmezden gel
        }
    }
    
    public void setSelectedComponent(DesignerComponent component) {
        this.selectedComponent = component;
        refreshProperties();
    }
    
    public void refreshProperties() {
        if (selectedComponent == null) {
            clearFields();
            return;
        }
        
        // Sayısal alanları doldur
        if (textFields.containsKey("x")) {
            textFields.get("x").setText(String.valueOf(selectedComponent.getX()));
        }
        if (textFields.containsKey("y")) {
            textFields.get("y").setText(String.valueOf(selectedComponent.getY()));
        }
        if (textFields.containsKey("width")) {
            textFields.get("width").setText(String.valueOf(selectedComponent.getWidth()));
        }
        if (textFields.containsKey("height")) {
            textFields.get("height").setText(String.valueOf(selectedComponent.getHeight()));
        }
        
        // Text alanını doldur
        if (textFields.containsKey("text")) {
            String text = "";
            if (selectedComponent.getComponent() instanceof AbstractButton) {
                text = ((AbstractButton) selectedComponent.getComponent()).getText();
            } else if (selectedComponent.getComponent() instanceof JLabel) {
                text = ((JLabel) selectedComponent.getComponent()).getText();
            } else if (selectedComponent.getComponent() instanceof JTextField) {
                text = ((JTextField) selectedComponent.getComponent()).getText();
            }
            textFields.get("text").setText(text);
        }
        
        // Font seçimini güncelle
        if (comboFields.containsKey("fontName")) {
            Font font = selectedComponent.getComponent().getFont();
            @SuppressWarnings("unchecked")
            JComboBox<String> fontCombo = (JComboBox<String>) comboFields.get("fontName");
            fontCombo.setSelectedItem(font.getFamily());
        }
        
        // Checkbox'ları güncelle
        if (checkFields.containsKey("enabled")) {
            checkFields.get("enabled").setSelected(selectedComponent.getComponent().isEnabled());
        }
        if (checkFields.containsKey("visible")) {
            checkFields.get("visible").setSelected(selectedComponent.getComponent().isVisible());
        }
    }
    
    private void clearFields() {
        for (JTextField tf : textFields.values()) {
            tf.setText("");
        }
        for (JCheckBox cb : checkFields.values()) {
            cb.setSelected(false);
        }
    }
    
    // Modern scrollbar UI
    static class ModernScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        public ModernScrollBarUI() {
            this.thumbColor = new Color(80, 80, 90);
            this.trackColor = new Color(45, 45, 55);
        }
        
        protected JButton createZeroButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            return button;
        }
        
        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            if (thumbBounds.isEmpty() || !scrollbar.isEnabled()) {
                return;
            }
            
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x, thumbBounds.y, thumbBounds.width, thumbBounds.height, 8, 8);
            g2.dispose();
        }
        
        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(trackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
            g2.dispose();
        }
    }
}
