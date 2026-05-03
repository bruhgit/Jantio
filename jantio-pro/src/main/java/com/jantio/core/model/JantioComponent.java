package com.jantio.core.model;

import java.awt.*;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Jantio Component Model - Represents any GUI component in the designer
 */
public class JantioComponent implements Serializable, Cloneable {
    private static final long serialVersionUID = 2L;
    
    public enum ComponentType {
        // Basic Components
        BUTTON("JButton", "Button"),
        LABEL("JLabel", "Label"),
        TEXT_FIELD("JTextField", "TextField"),
        TEXT_AREA("JTextArea", "TextArea"),
        PASSWORD_FIELD("JPasswordField", "PasswordField"),
        
        // Selection Components
        CHECK_BOX("JCheckBox", "CheckBox"),
        RADIO_BUTTON("JRadioButton", "RadioButton"),
        COMBO_BOX("JComboBox", "ComboBox"),
        LIST("JList", "List"),
        SPINNER("JSpinner", "Spinner"),
        
        // Container Components
        PANEL("JPanel", "Panel"),
        SCROLL_PANE("JScrollPane", "ScrollPane"),
        TABBED_PANE("JTabbedPane", "TabbedPane"),
        SPLIT_PANE("JSplitPane", "SplitPane"),
        TOOLBAR("JToolBar", "ToolBar"),
        
        // Advanced Components
        TABLE("JTable", "Table"),
        TREE("JTree", "Tree"),
        COLOR_CHOOSER("JColorChooser", "ColorChooser"),
        FILE_CHOOSER("JFileChooser", "FileChooser"),
        PROGRESS_BAR("JProgressBar", "ProgressBar"),
        SLIDER("JSlider", "Slider"),
        
        // Layout Components
        FLOW_LAYOUT("FlowLayout", "Flow Layout"),
        BORDER_LAYOUT("BorderLayout", "Border Layout"),
        GRID_LAYOUT("GridLayout", "Grid Layout"),
        BOX_LAYOUT("BoxLayout", "Box Layout"),
        CARD_LAYOUT("CardLayout", "Card Layout"),
        GROUP_LAYOUT("GroupLayout", "Group Layout"),
        NULL_LAYOUT("null", "Null Layout");
        
        private final String className;
        private final String displayName;
        
        ComponentType(String className, String displayName) {
            this.className = className;
            this.displayName = displayName;
        }
        
        public String getClassName() { return className; }
        public String getDisplayName() { return displayName; }
        
        public boolean isContainer() {
            return this == PANEL || this == SCROLL_PANE || this == TABBED_PANE || 
                   this == SPLIT_PANE || this == TOOLBAR;
        }
        
        public boolean isLayout() {
            return this.ordinal() >= FLOW_LAYOUT.ordinal();
        }
    }
    
    private String id;
    private ComponentType type;
    private String name;
    private String text;
    private int x, y, width, height;
    private Color backgroundColor;
    private Color foregroundColor;
    private Font font;
    private String toolTipText;
    private boolean enabled;
    private boolean visible;
    private String borderType;
    private String alignment;
    private Map<String, Object> properties;
    private Map<String, String> events;
    private JantioComponent parent;
    
    public JantioComponent(ComponentType type) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.name = generateName(type);
        this.text = "";
        this.x = 10;
        this.y = 10;
        this.width = getDefaultWidth(type);
        this.height = getDefaultHeight(type);
        this.backgroundColor = null;
        this.foregroundColor = Color.BLACK;
        this.font = new Font("Segoe UI", Font.PLAIN, 14);
        this.toolTipText = "";
        this.enabled = true;
        this.visible = true;
        this.borderType = "none";
        this.alignment = "center";
        this.properties = new HashMap<>();
        this.events = new HashMap<>();
        this.parent = null;
        
        initializeDefaults();
    }
    
    private void initializeDefaults() {
        switch (type) {
            case BUTTON:
                this.text = "Button";
                break;
            case LABEL:
                this.text = "Label";
                this.alignment = "left";
                break;
            case TEXT_FIELD:
                this.text = "";
                this.width = 150;
                break;
            case TEXT_AREA:
                this.text = "";
                this.width = 200;
                this.height = 100;
                break;
            case CHECK_BOX:
                this.text = "CheckBox";
                break;
            case RADIO_BUTTON:
                this.text = "RadioButton";
                break;
            case COMBO_BOX:
                properties.put("items", new String[]{"Item 1", "Item 2", "Item 3"});
                break;
            case LIST:
                properties.put("items", new String[]{"Item 1", "Item 2", "Item 3"});
                properties.put("selectionMode", "SINGLE_SELECTION");
                break;
            case SPINNER:
                properties.put("min", 0);
                properties.put("max", 100);
                properties.put("value", 0);
                properties.put("stepSize", 1);
                break;
            case SLIDER:
                properties.put("min", 0);
                properties.put("max", 100);
                properties.put("value", 50);
                properties.put("orientation", "HORIZONTAL");
                break;
            case PROGRESS_BAR:
                properties.put("min", 0);
                properties.put("max", 100);
                properties.put("value", 0);
                properties.put("orientation", "HORIZONTAL");
                break;
            case TABLE:
                properties.put("columns", new String[]{"Col1", "Col2", "Col3"});
                properties.put("rows", 5);
                break;
            case TREE:
                properties.put("rootNode", "Root");
                break;
            case TABBED_PANE:
                properties.put("tabPlacement", "TOP");
                break;
            case SPLIT_PANE:
                properties.put("orientation", "HORIZONTAL");
                properties.put("dividerLocation", 150);
                break;
            case PANEL:
                properties.put("layout", "FlowLayout");
                break;
        }
    }
    
    private String generateName(ComponentType type) {
        String prefix = type.name().substring(0, 1) + type.name().substring(1).toLowerCase();
        return prefix; // In real implementation, would check for duplicates
    }
    
    private int getDefaultWidth(ComponentType type) {
        return switch (type) {
            case BUTTON -> 100;
            case LABEL -> 80;
            case TEXT_FIELD -> 150;
            case TEXT_AREA -> 200;
            case CHECK_BOX, RADIO_BUTTON -> 120;
            case COMBO_BOX, LIST, SPINNER -> 150;
            case PANEL, SCROLL_PANE -> 300;
            case TABBED_PANE, SPLIT_PANE -> 400;
            case TABLE, TREE -> 350;
            case COLOR_CHOOSER, FILE_CHOOSER -> 200;
            case PROGRESS_BAR, SLIDER -> 200;
            case TOOLBAR -> 500;
            default -> 100;
        };
    }
    
    private int getDefaultHeight(ComponentType type) {
        return switch (type) {
            case BUTTON, LABEL, CHECK_BOX, RADIO_BUTTON, COMBO_BOX, SPINNER -> 30;
            case TEXT_FIELD, PASSWORD_FIELD -> 30;
            case TEXT_AREA -> 100;
            case LIST -> 150;
            case PANEL -> 200;
            case SCROLL_PANE -> 250;
            case TABBED_PANE, SPLIT_PANE -> 300;
            case TABLE, TREE -> 200;
            case COLOR_CHOOSER, FILE_CHOOSER -> 100;
            case PROGRESS_BAR, SLIDER -> 30;
            case TOOLBAR -> 40;
            default -> 30;
        };
    }
    
    // Getters and Setters
    public String getId() { return id; }
    public ComponentType getType() { return type; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
    public Color getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(Color backgroundColor) { this.backgroundColor = backgroundColor; }
    public Color getForegroundColor() { return foregroundColor; }
    public void setForegroundColor(Color foregroundColor) { this.foregroundColor = foregroundColor; }
    public Font getFont() { return font; }
    public void setFont(Font font) { this.font = font; }
    public String getToolTipText() { return toolTipText; }
    public void setToolTipText(String toolTipText) { this.toolTipText = toolTipText; }
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public boolean isVisible() { return visible; }
    public void setVisible(boolean visible) { this.visible = visible; }
    public String getBorderType() { return borderType; }
    public void setBorderType(String borderType) { this.borderType = borderType; }
    public String getAlignment() { return alignment; }
    public void setAlignment(String alignment) { this.alignment = alignment; }
    public Map<String, Object> getProperties() { return properties; }
    public void setProperty(String key, Object value) { properties.put(key, value); }
    public Object getProperty(String key) { return properties.get(key); }
    public Map<String, String> getEvents() { return events; }
    public void setEvent(String eventName, String code) { events.put(eventName, code); }
    public String getEvent(String eventName) { return events.get(eventName); }
    public JantioComponent getParent() { return parent; }
    public void setParent(JantioComponent parent) { this.parent = parent; }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
    
    public void setBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
    
    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }
    
    public boolean containsPoint(int px, int py) {
        return px >= x && px <= x + width && py >= y && py <= y + height;
    }
    
    public boolean overlapsWith(JantioComponent other) {
        Rectangle thisRect = getBounds();
        Rectangle otherRect = other.getBounds();
        return thisRect.intersects(otherRect);
    }
    
    @Override
    public JantioComponent clone() {
        try {
            JantioComponent cloned = (JantioComponent) super.clone();
            cloned.id = UUID.randomUUID().toString();
            cloned.properties = new HashMap<>(this.properties);
            cloned.events = new HashMap<>(this.events);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
    
    @Override
    public String toString() {
        return String.format("%s[%s] (%d, %d, %d, %d)", 
            type.getDisplayName(), name, x, y, width, height);
    }
}
