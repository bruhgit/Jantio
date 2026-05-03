package com.jantio.components;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Tasarım yüzeyindeki bileşeni temsil eden sınıf
 */
public class DesignerComponent {
    private String id;
    private ComponentType type;
    private JComponent component;
    private int x, y, width, height;
    private Map<String, Object> properties;
    
    public DesignerComponent(ComponentType type) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.component = type.createInstance();
        this.x = 50;
        this.y = 50;
        this.width = 120;
        this.height = 40;
        this.properties = new HashMap<>();
        
        initializeDefaults();
    }
    
    private void initializeDefaults() {
        switch (type) {
            case BUTTON:
                ((JButton) component).setText("Button");
                break;
            case LABEL:
                ((JLabel) component).setText("Label");
                break;
            case TEXT_FIELD:
                ((JTextField) component).setText("");
                break;
            case PASSWORD_FIELD:
                ((JPasswordField) component).setText("");
                break;
            case FORMATTED_TEXT_FIELD:
                ((JFormattedTextField) component).setText("");
                break;
            case TEXT_AREA:
                ((JTextArea) component).setText("");
                ((JTextArea) component).setRows(3);
                ((JTextArea) component).setColumns(15);
                JScrollPane scrollPane = new JScrollPane((JTextArea) component);
                component = scrollPane;
                width = 150;
                height = 80;
                break;
            case CHECK_BOX:
                ((JCheckBox) component).setText("CheckBox");
                break;
            case RADIO_BUTTON:
                ((JRadioButton) component).setText("RadioButton");
                break;
            case COMBO_BOX:
                JComboBox<String> comboBox = new JComboBox<>(new String[]{"Item 1", "Item 2", "Item 3"});
                component = comboBox;
                break;
            case LIST:
                JList<String> list = new JList<>(new String[]{"Item 1", "Item 2", "Item 3"});
                component = new JScrollPane(list);
                width = 150;
                height = 80;
                break;
            case PANEL:
                component.setBackground(new Color(240, 240, 240));
                width = 200;
                height = 150;
                break;
            case SLIDER:
                ((JSlider) component).setMinimum(0);
                ((JSlider) component).setMaximum(100);
                ((JSlider) component).setValue(50);
                break;
            case PROGRESS_BAR:
                ((JProgressBar) component).setValue(50);
                break;
            case SPINNER:
                ((JSpinner) component).setValue(0);
                break;
            case SEPARATOR:
                ((JSeparator) component).setOrientation(JSeparator.HORIZONTAL);
                width = 150;
                height = 10;
                break;
            case TABBED_PANE:
                JTabbedPane tabbedPane = new JTabbedPane();
                tabbedPane.addTab("Tab 1", new JPanel());
                tabbedPane.addTab("Tab 2", new JPanel());
                component = tabbedPane;
                width = 200;
                height = 150;
                break;
            case TOOL_BAR:
                JToolBar toolBar = new JToolBar();
                toolBar.add(new JButton("Btn1"));
                toolBar.add(new JButton("Btn2"));
                component = toolBar;
                width = 150;
                height = 40;
                break;
            case SCROLL_PANE:
                JPanel dummyPanel = new JPanel();
                dummyPanel.setPreferredSize(new Dimension(300, 200));
                component = new JScrollPane(dummyPanel);
                width = 200;
                height = 150;
                break;
            case COLOR_CHOOSER:
                width = 200;
                height = 100;
                break;
        }
        
        component.setBounds(x, y, width, height);
    }
    
    public String getId() {
        return id;
    }
    
    public ComponentType getType() {
        return type;
    }
    
    public JComponent getComponent() {
        return component;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
        component.setLocation(x, y);
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
        component.setLocation(x, y);
    }
    
    public int getWidth() {
        return width;
    }
    
    public void setWidth(int width) {
        this.width = width;
        component.setSize(width, height);
    }
    
    public int getHeight() {
        return height;
    }
    
    public void setHeight(int height) {
        this.height = height;
        component.setSize(width, height);
    }
    
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        component.setBounds(x, y, width, height);
    }
    
    public Map<String, Object> getProperties() {
        return properties;
    }
    
    public void setProperty(String key, Object value) {
        properties.put(key, value);
        applyProperty(key, value);
    }
    
    public Object getProperty(String key) {
        return properties.get(key);
    }
    
    private void applyProperty(String key, Object value) {
        switch (key) {
            case "text":
                if (component instanceof AbstractButton) {
                    ((AbstractButton) component).setText(value.toString());
                } else if (component instanceof JLabel) {
                    ((JLabel) component).setText(value.toString());
                } else if (component instanceof JTextField) {
                    ((JTextField) component).setText(value.toString());
                }
                break;
            case "foreground":
                if (value instanceof Color) {
                    component.setForeground((Color) value);
                }
                break;
            case "background":
                if (value instanceof Color) {
                    component.setBackground((Color) value);
                }
                break;
            case "font":
                if (value instanceof Font) {
                    component.setFont((Font) value);
                }
                break;
            case "enabled":
                if (value instanceof Boolean) {
                    component.setEnabled((Boolean) value);
                }
                break;
            case "visible":
                if (value instanceof Boolean) {
                    component.setVisible((Boolean) value);
                }
                break;
        }
    }
    
    public String generateCode(String variableName) {
        StringBuilder code = new StringBuilder();
        
        switch (type) {
            case BUTTON:
                code.append(String.format("JButton %s = new JButton(\"%s\");%n", 
                    variableName, ((JButton) component).getText()));
                break;
            case LABEL:
                code.append(String.format("JLabel %s = new JLabel(\"%s\");%n", 
                    variableName, ((JLabel) component).getText()));
                break;
            case TEXT_FIELD:
                code.append(String.format("JTextField %s = new JTextField(\"%s\");%n", 
                    variableName, ((JTextField) component).getText()));
                break;
            case PASSWORD_FIELD:
                code.append(String.format("JPasswordField %s = new JPasswordField(\"%s\");%n", 
                    variableName, ((JPasswordField) component).getPassword() != null ? new String(((JPasswordField) component).getPassword()) : ""));
                break;
            case FORMATTED_TEXT_FIELD:
                code.append(String.format("JFormattedTextField %s = new JFormattedTextField();%n", variableName));
                break;
            case TEXT_AREA:
                JTextArea textArea = (JTextArea) ((JScrollPane) component).getViewport().getView();
                code.append(String.format("JTextArea %s = new JTextArea(\"%s\");%n", 
                    variableName, textArea.getText()));
                code.append(String.format("%s.setRows(%d);%n", variableName, textArea.getRows()));
                code.append(String.format("%s.setColumns(%d);%n", variableName, textArea.getColumns()));
                code.append(String.format("JScrollPane %sScroll = new JScrollPane(%s);%n", 
                    variableName, variableName));
                break;
            case CHECK_BOX:
                code.append(String.format("JCheckBox %s = new JCheckBox(\"%s\");%n", 
                    variableName, ((JCheckBox) component).getText()));
                break;
            case RADIO_BUTTON:
                code.append(String.format("JRadioButton %s = new JRadioButton(\"%s\");%n", 
                    variableName, ((JRadioButton) component).getText()));
                break;
            case COMBO_BOX:
                @SuppressWarnings("unchecked")
                JComboBox<String> comboBox = (JComboBox<String>) component;
                code.append(String.format("String[] %sItems = {", variableName));
                for (int i = 0; i < comboBox.getItemCount(); i++) {
                    if (i > 0) code.append(", ");
                    code.append("\"").append(comboBox.getItemAt(i)).append("\"");
                }
                code.append("};%n");
                code.append(String.format("JComboBox<String> %s = new JComboBox<>(%sItems);%n", 
                    variableName, variableName));
                break;
            case LIST:
                @SuppressWarnings("unchecked")
                JList<String> list = (JList<String>) ((JScrollPane) component).getViewport().getView();
                ListModel<String> listModel = list.getModel();
                code.append(String.format("String[] %sItems = {", variableName));
                for (int i = 0; i < listModel.getSize(); i++) {
                    if (i > 0) code.append(", ");
                    code.append("\"").append(listModel.getElementAt(i)).append("\"");
                }
                code.append("};%n");
                code.append(String.format("JList<String> %s = new JList<>(%sItems);%n", variableName, variableName));
                code.append(String.format("JScrollPane %sScroll = new JScrollPane(%s);%n", variableName, variableName));
                break;
            case PANEL:
                code.append(String.format("JPanel %s = new JPanel();%n", variableName));
                code.append(String.format("%s.setLayout(null);%n", variableName));
                break;
            case SLIDER:
                JSlider slider = (JSlider) component;
                code.append(String.format("JSlider %s = new JSlider(%d, %d, %d);%n", 
                    variableName, slider.getMinimum(), slider.getMaximum(), slider.getValue()));
                break;
            case PROGRESS_BAR:
                code.append(String.format("JProgressBar %s = new JProgressBar();%n", variableName));
                code.append(String.format("%s.setValue(%d);%n", variableName, ((JProgressBar) component).getValue()));
                break;
            case SPINNER:
                code.append(String.format("JSpinner %s = new JSpinner();%n", variableName));
                break;
            case SEPARATOR:
                code.append(String.format("JSeparator %s = new JSeparator(JSeparator.HORIZONTAL);%n", variableName));
                break;
            case TABBED_PANE:
                code.append(String.format("JTabbedPane %s = new JTabbedPane();%n", variableName));
                code.append(String.format("%s.addTab(\"Tab 1\", new JPanel());%n", variableName));
                code.append(String.format("%s.addTab(\"Tab 2\", new JPanel());%n", variableName));
                break;
            case TOOL_BAR:
                code.append(String.format("JToolBar %s = new JToolBar();%n", variableName));
                code.append(String.format("%s.add(new JButton(\"Btn1\"));%n", variableName));
                code.append(String.format("%s.add(new JButton(\"Btn2\"));%n", variableName));
                break;
            case SCROLL_PANE:
                code.append(String.format("JPanel %sPanel = new JPanel();%n", variableName));
                code.append(String.format("JScrollPane %s = new JScrollPane(%sPanel);%n", variableName, variableName));
                break;
            case COLOR_CHOOSER:
                code.append(String.format("JColorChooser %s = new JColorChooser();%n", variableName));
                break;
        }
        
        code.append(String.format("%s.setBounds(%d, %d, %d, %d);%n", 
            variableName, x, y, width, height));
        
        if (!component.isEnabled()) {
            code.append(String.format("%s.setEnabled(false);%n", variableName));
        }
        
        return code.toString();
    }
    
    /**
     * Kotlin kodu üretir
     */
    public String generateKotlinCode(String variableName) {
        StringBuilder code = new StringBuilder();
        
        switch (type) {
            case BUTTON:
                code.append(String.format("val %s = JButton(\"%s\")%n", 
                    variableName, ((JButton) component).getText()));
                break;
            case LABEL:
                code.append(String.format("val %s = JLabel(\"%s\")%n", 
                    variableName, ((JLabel) component).getText()));
                break;
            case TEXT_FIELD:
                code.append(String.format("val %s = JTextField(\"%s\")%n", 
                    variableName, ((JTextField) component).getText()));
                break;
            case TEXT_AREA:
                JTextArea textArea = (JTextArea) ((JScrollPane) component).getViewport().getView();
                code.append(String.format("val %s = JTextArea(\"%s\")%n", 
                    variableName, textArea.getText()));
                code.append(String.format("%s.rows = %d%n", variableName, textArea.getRows()));
                code.append(String.format("%s.columns = %d%n", variableName, textArea.getColumns()));
                code.append(String.format("val %sScroll = JScrollPane(%s)%n", 
                    variableName, variableName));
                break;
            case CHECK_BOX:
                code.append(String.format("val %s = JCheckBox(\"%s\")%n", 
                    variableName, ((JCheckBox) component).getText()));
                break;
            case RADIO_BUTTON:
                code.append(String.format("val %s = JRadioButton(\"%s\")%n", 
                    variableName, ((JRadioButton) component).getText()));
                break;
            case COMBO_BOX:
                @SuppressWarnings("unchecked")
                JComboBox<String> comboBox = (JComboBox<String>) component;
                code.append(String.format("val %sItems = arrayOf(", variableName));
                for (int i = 0; i < comboBox.getItemCount(); i++) {
                    if (i > 0) code.append(", ");
                    code.append("\"").append(comboBox.getItemAt(i)).append("\"");
                }
                code.append(")%n");
                code.append(String.format("val %s = JComboBox(%sItems)%n", 
                    variableName, variableName));
                break;
            case PANEL:
                code.append(String.format("val %s = JPanel()%n", variableName));
                code.append(String.format("%s.layout = null%n", variableName));
                break;
            case SLIDER:
                JSlider slider = (JSlider) component;
                code.append(String.format("val %s = JSlider(%d, %d, %d)%n", 
                    variableName, slider.getMinimum(), slider.getMaximum(), slider.getValue()));
                break;
            case PROGRESS_BAR:
                code.append(String.format("val %s = JProgressBar()%n", variableName));
                code.append(String.format("%s.value = %d%n", variableName, ((JProgressBar) component).getValue()));
                break;
            case SPINNER:
                code.append(String.format("val %s = JSpinner()%n", variableName));
                break;
        }
        
        code.append(String.format("%s.setBounds(%d, %d, %d, %d)%n", 
            variableName, x, y, width, height));
        
        if (!component.isEnabled()) {
            code.append(String.format("%s.isEnabled = false%n", variableName));
        }
        
        return code.toString();
    }
}
