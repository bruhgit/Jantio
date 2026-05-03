package com.jantio.generators.java;

import com.jantio.core.engine.JantioEngine;
import com.jantio.core.model.JantioComponent;

import java.util.List;

/**
 * Java Code Generator - Generates idiomatic Java Swing code
 */
public class JavaCodeGenerator {
    private final JantioEngine engine;
    
    public JavaCodeGenerator(JantioEngine engine) {
        this.engine = engine;
    }
    
    public String generateFullClass() {
        StringBuilder code = new StringBuilder();
        
        // Package declaration
        code.append("package ").append(engine.getPackageName()).append(";\n\n");
        
        // Imports
        code.append(generateImports());
        
        // Class declaration
        code.append("public class ").append(engine.getProjectName()).append(" extends JFrame {\n\n");
        
        // Component declarations
        code.append(generateComponentDeclarations());
        
        // Constructor
        code.append(generateConstructor());
        
        // Initialization method
        code.append(generateInitializeComponents());
        
        // Event handlers
        code.append(generateEventHandlers());
        
        // Main method
        code.append(generateMainMethod());
        
        code.append("}\n");
        
        return code.toString();
    }
    
    private String generateImports() {
        StringBuilder imports = new StringBuilder();
        imports.append("import javax.swing.*;\n");
        imports.append("import javax.swing.border.*;\n");
        imports.append("import java.awt.*;\n");
        imports.append("import java.awt.event.*;\n\n");
        return imports.toString();
    }
    
    private String generateComponentDeclarations() {
        StringBuilder declarations = new StringBuilder();
        List<JantioComponent> components = engine.getComponents();
        
        for (JantioComponent comp : components) {
            if (comp.getType().isLayout()) continue;
            
            String type = comp.getType().getClassName();
            String name = comp.getName();
            declarations.append("    private ").append(type).append(" ").append(name).append(";\n");
        }
        
        if (!components.isEmpty()) {
            declarations.append("\n");
        }
        
        return declarations.toString();
    }
    
    private String generateConstructor() {
        StringBuilder constructor = new StringBuilder();
        constructor.append("    public ").append(engine.getProjectName()).append("() {\n");
        constructor.append("        setTitle(\"").append(engine.getProjectName()).append("\");\n");
        constructor.append("        setSize(800, 600);\n");
        constructor.append("        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n");
        constructor.append("        setLocationRelativeTo(null);\n");
        constructor.append("        setLayout(null);\n");
        constructor.append("        \n");
        constructor.append("        initializeComponents();\n");
        constructor.append("    }\n\n");
        return constructor.toString();
    }
    
    private String generateInitializeComponents() {
        StringBuilder init = new StringBuilder();
        init.append("    private void initializeComponents() {\n");
        
        List<JantioComponent> components = engine.getComponents();
        
        for (JantioComponent comp : components) {
            if (comp.getType().isLayout()) continue;
            
            init.append("        // Initialize ").append(comp.getName()).append("\n");
            init.append("        ").append(comp.getName()).append(" = new ")
                .append(comp.getType().getClassName()).append("();\n");
            
            // Set bounds
            init.append("        ").append(comp.getName()).append(".setBounds(")
                .append(comp.getX()).append(", ").append(comp.getY()).append(", ")
                .append(comp.getWidth()).append(", ").append(comp.getHeight()).append(");\n");
            
            // Set text if applicable
            if (comp.getText() != null && !comp.getText().isEmpty()) {
                init.append("        ").append(comp.getName()).append(".setText(\"")
                    .append(escapeString(comp.getText())).append("\");\n");
            }
            
            // Set tool tip
            if (comp.getToolTipText() != null && !comp.getToolTipText().isEmpty()) {
                init.append("        ").append(comp.getName()).append(".setToolTipText(\"")
                    .append(escapeString(comp.getToolTipText())).append("\");\n");
            }
            
            // Set enabled/visible
            if (!comp.isEnabled()) {
                init.append("        ").append(comp.getName()).append(".setEnabled(false);\n");
            }
            if (!comp.isVisible()) {
                init.append("        ").append(comp.getName()).append(".setVisible(false);\n");
            }
            
            // Set colors
            if (comp.getBackgroundColor() != null) {
                init.append("        ").append(comp.getName()).append(".setBackground(new Color(")
                    .append(comp.getBackgroundColor().getRed()).append(", ")
                    .append(comp.getBackgroundColor().getGreen()).append(", ")
                    .append(comp.getBackgroundColor().getBlue()).append("));\n");
            }
            if (comp.getForegroundColor() != null) {
                init.append("        ").append(comp.getName()).append(".setForeground(new Color(")
                    .append(comp.getForegroundColor().getRed()).append(", ")
                    .append(comp.getForegroundColor().getGreen()).append(", ")
                    .append(comp.getForegroundColor().getBlue()).append("));\n");
            }
            
            // Special properties
            generateSpecialProperties(init, comp);
            
            // Add to container
            init.append("        add(").append(comp.getName()).append(");\n\n");
        }
        
        init.append("    }\n\n");
        return init.toString();
    }
    
    private void generateSpecialProperties(StringBuilder init, JantioComponent comp) {
        switch (comp.getType()) {
            case COMBO_BOX, LIST -> {
                Object items = comp.getProperty("items");
                if (items instanceof String[] itemArray) {
                    for (String item : itemArray) {
                        init.append("        ").append(comp.getName()).append(".addItem(\"")
                            .append(escapeString(item)).append("\");\n");
                    }
                }
            }
            case SPINNER -> {
                Object min = comp.getProperty("min");
                Object max = comp.getProperty("max");
                Object value = comp.getProperty("value");
                if (min != null && max != null) {
                    init.append("        ").append(comp.getName())
                        .append(" = new JSpinner(new SpinnerNumberModel(")
                        .append(value).append(", ").append(min).append(", ")
                        .append(max).append(", 1));\n");
                }
            }
            case SLIDER, PROGRESS_BAR -> {
                Object min = comp.getProperty("min");
                Object max = comp.getProperty("max");
                Object value = comp.getProperty("value");
                Object orientation = comp.getProperty("orientation");
                
                String orientStr = "HORIZONTAL".equals(orientation) ? "JSlider.HORIZONTAL" : "JSlider.VERTICAL";
                
                if (comp.getType() == JantioComponent.ComponentType.SLIDER) {
                    init.append("        ").append(comp.getName())
                        .append(" = new JSlider(").append(orientStr).append(", ")
                        .append(min).append(", ").append(max).append(", ")
                        .append(value).append(");\n");
                } else {
                    init.append("        ").append(comp.getName())
                        .append(" = new JProgressBar(").append(orientStr).append(", ")
                        .append(min).append(", ").append(max).append(");\n");
                    init.append("        ").append(comp.getName()).append(".setValue(").append(value).append(");\n");
                }
            }
            case TABLE -> {
                Object columns = comp.getProperty("columns");
                if (columns instanceof String[] colArray) {
                    init.append("        String[] ").append(comp.getName()).append("Columns = {");
                    for (int i = 0; i < colArray.length; i++) {
                        init.append("\"").append(colArray[i]).append("\"");
                        if (i < colArray.length - 1) init.append(", ");
                    }
                    init.append("};\n");
                    init.append("        ").append(comp.getName())
                        .append(" = new JTable(new DefaultTableModel(").append(comp.getName())
                        .append("Columns, 0));\n");
                }
            }
        }
    }
    
    private String generateEventHandlers() {
        StringBuilder handlers = new StringBuilder();
        List<JantioComponent> components = engine.getComponents();
        
        for (JantioComponent comp : components) {
            String clickHandler = comp.getEvent("actionPerformed");
            if (clickHandler != null && !clickHandler.isEmpty()) {
                handlers.append("        ").append(comp.getName())
                    .append(".addActionListener(e -> {\n");
                handlers.append("            // TODO: Implement action\n");
                handlers.append("            ").append(clickHandler).append("\n");
                handlers.append("        });\n\n");
            }
        }
        
        if (handlers.length() > 0) {
            return "    // Event Handlers\n" + handlers.toString();
        }
        return "";
    }
    
    private String generateMainMethod() {
        StringBuilder main = new StringBuilder();
        main.append("    public static void main(String[] args) {\n");
        main.append("        try {\n");
        main.append("            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());\n");
        main.append("        } catch (Exception e) {\n");
        main.append("            e.printStackTrace();\n");
        main.append("        }\n");
        main.append("        \n");
        main.append("        SwingUtilities.invokeLater(() -> {\n");
        main.append("            new ").append(engine.getProjectName()).append("().setVisible(true);\n");
        main.append("        });\n");
        main.append("    }\n");
        return main.toString();
    }
    
    public String generateFXML() {
        StringBuilder fxml = new StringBuilder();
        fxml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n\n");
        fxml.append("<?import javafx.scene.control.*?>\n");
        fxml.append("<?import javafx.scene.layout.*?>\n\n");
        fxml.append("<AnchorPane xmlns=\"http://javafx.com/javafx/8\" \n");
        fxml.append("          xmlns:fx=\"http://javafx.com/fxml/1\"\n");
        fxml.append("          fx:controller=\"").append(engine.getPackageName()).append(".")
            .append(engine.getProjectName()).append("Controller\">\n\n");
        
        List<JantioComponent> components = engine.getComponents();
        for (JantioComponent comp : components) {
            if (comp.getType().isLayout()) continue;
            
            String fxId = comp.getName();
            String type = getJavaFXType(comp.getType());
            
            fxml.append("    <").append(type)
                .append(" fx:id=\"").append(fxId).append("\"")
                .append(" layoutX=\"").append(comp.getX()).append("\"")
                .append(" layoutY=\"").append(comp.getY()).append("\"")
                .append(" prefWidth=\"").append(comp.getWidth()).append("\"")
                .append(" prefHeight=\"").append(comp.getHeight()).append("\"");
            
            if (comp.getText() != null && !comp.getText().isEmpty()) {
                fxml.append(" text=\"").append(escapeString(comp.getText())).append("\"");
            }
            
            fxml.append("/>\n");
        }
        
        fxml.append("\n</AnchorPane>\n");
        return fxml.toString();
    }
    
    private String getJavaFXType(JantioComponent.ComponentType type) {
        return switch (type) {
            case BUTTON -> "Button";
            case LABEL -> "Label";
            case TEXT_FIELD -> "TextField";
            case TEXT_AREA -> "TextArea";
            case CHECK_BOX -> "CheckBox";
            case RADIO_BUTTON -> "RadioButton";
            case COMBO_BOX -> "ComboBox";
            case LIST -> "ListView";
            case PANEL -> "Pane";
            case SCROLL_PANE -> "ScrollPane";
            default -> "Label";
        };
    }
    
    private String escapeString(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
