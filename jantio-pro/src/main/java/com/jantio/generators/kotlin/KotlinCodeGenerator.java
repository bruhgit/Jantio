package com.jantio.generators.kotlin;

import com.jantio.core.engine.JantioEngine;
import com.jantio.core.model.JantioComponent;

import java.awt.Color;
import java.util.List;

/**
 * Kotlin Code Generator - Generates idiomatic Kotlin Swing/JavaFX code
 */
public class KotlinCodeGenerator {
    private final JantioEngine engine;
    
    public KotlinCodeGenerator(JantioEngine engine) {
        this.engine = engine;
    }
    
    public String generateFullClass() {
        StringBuilder code = new StringBuilder();
        
        // Package declaration
        code.append("package ").append(engine.getPackageName()).append("\n\n");
        
        // Imports
        code.append(generateImports());
        
        // Class declaration
        code.append("class ").append(engine.getProjectName()).append(" : JFrame() {\n\n");
        
        // Component declarations
        code.append(generateComponentDeclarations());
        
        // Constructor (init block)
        code.append(generateConstructor());
        
        // Initialization method
        code.append(generateInitializeComponents());
        
        // Event handlers
        code.append(generateEventHandlers());
        
        // Companion object with main
        code.append(generateMainMethod());
        
        code.append("}\n");
        
        return code.toString();
    }
    
    private String generateImports() {
        StringBuilder imports = new StringBuilder();
        imports.append("import javax.swing.*\n");
        imports.append("import javax.swing.border.*\n");
        imports.append("import java.awt.*\n");
        imports.append("import java.awt.event.*\n\n");
        return imports.toString();
    }
    
    private String generateComponentDeclarations() {
        StringBuilder declarations = new StringBuilder();
        List<JantioComponent> components = engine.getComponents();
        
        for (JantioComponent comp : components) {
            if (comp.getType().isLayout()) continue;
            
            String type = comp.getType().getClassName();
            String name = comp.getName();
            declarations.append("    private lateinit var ").append(name).append(": ").append(type).append("\n");
        }
        
        if (!components.isEmpty()) {
            declarations.append("\n");
        }
        
        return declarations.toString();
    }
    
    private String generateConstructor() {
        StringBuilder constructor = new StringBuilder();
        constructor.append("    init {\n");
        constructor.append("        title = \"").append(engine.getProjectName()).append("\"\n");
        constructor.append("        defaultCloseOperation = EXIT_ON_CLOSE\n");
        constructor.append("        setSize(800, 600)\n");
        constructor.append("        locationRelativeTo = null\n");
        constructor.append("        layout = null\n");
        constructor.append("        \n");
        constructor.append("        initializeComponents()\n");
        constructor.append("    }\n\n");
        return constructor.toString();
    }
    
    private String generateInitializeComponents() {
        StringBuilder init = new StringBuilder();
        init.append("    private fun initializeComponents() {\n");
        
        List<JantioComponent> components = engine.getComponents();
        
        for (JantioComponent comp : components) {
            if (comp.getType().isLayout()) continue;
            
            init.append("        // Initialize ").append(comp.getName()).append("\n");
            init.append("        ").append(comp.getName()).append(" = ").append(comp.getType().getClassName()).append("()\n");
            
            // Set bounds
            init.append("        ").append(comp.getName()).append(".setBounds(")
                .append(comp.getX()).append(", ").append(comp.getY()).append(", ")
                .append(comp.getWidth()).append(", ").append(comp.getHeight()).append(")\n");
            
            // Set text if applicable
            if (comp.getText() != null && !comp.getText().isEmpty()) {
                init.append("        ").append(comp.getName()).append(".text = \"")
                    .append(escapeString(comp.getText())).append("\"\n");
            }
            
            // Set tool tip
            if (comp.getToolTipText() != null && !comp.getToolTipText().isEmpty()) {
                init.append("        ").append(comp.getName()).append(".toolTipText = \"")
                    .append(escapeString(comp.getToolTipText())).append("\"\n");
            }
            
            // Set enabled/visible
            if (!comp.isEnabled()) {
                init.append("        ").append(comp.getName()).append(".isEnabled = false\n");
            }
            if (!comp.isVisible()) {
                init.append("        ").append(comp.getName()).append(".isVisible = false\n");
            }
            
            // Set colors
            if (comp.getBackgroundColor() != null) {
                Color bg = comp.getBackgroundColor();
                init.append("        ").append(comp.getName()).append(".background = Color(")
                    .append(bg.getRed()).append(", ").append(bg.getGreen()).append(", ")
                    .append(bg.getBlue()).append(")\n");
            }
            if (comp.getForegroundColor() != null) {
                Color fg = comp.getForegroundColor();
                init.append("        ").append(comp.getName()).append(".foreground = Color(")
                    .append(fg.getRed()).append(", ").append(fg.getGreen()).append(", ")
                    .append(fg.getBlue()).append(")\n");
            }
            
            // Special properties
            generateSpecialProperties(init, comp);
            
            // Add to container
            init.append("        add(").append(comp.getName()).append(")\n\n");
        }
        
        init.append("    }\n\n");
        return init.toString();
    }
    
    private void generateSpecialProperties(StringBuilder init, JantioComponent comp) {
        switch (comp.getType()) {
            case COMBO_BOX:
            case LIST:
                Object[] items = (Object[]) comp.getProperty("items");
                if (items != null) {
                    for (Object item : items) {
                        init.append("        ").append(comp.getName()).append(".addItem(\"")
                            .append(escapeString(item.toString())).append("\")\n");
                    }
                }
                break;
            case SPINNER:
                Integer min = (Integer) comp.getProperty("min");
                Integer max = (Integer) comp.getProperty("max");
                Integer value = (Integer) comp.getProperty("value");
                if (min == null) min = 0;
                if (max == null) max = 100;
                if (value == null) value = 0;
                init.append("        ").append(comp.getName()).append(" = JSpinner(SpinnerNumberModel(")
                    .append(value).append(", ").append(min).append(", ").append(max).append(", 1))\n");
                break;
            case SLIDER:
                Integer sMin = (Integer) comp.getProperty("min");
                Integer sMax = (Integer) comp.getProperty("max");
                Integer sValue = (Integer) comp.getProperty("value");
                String orientation = (String) comp.getProperty("orientation");
                if (sMin == null) sMin = 0;
                if (sMax == null) sMax = 100;
                if (sValue == null) sValue = 50;
                if (orientation == null) orientation = "HORIZONTAL";
                String orientStr = orientation.equals("HORIZONTAL") ? "JSlider.HORIZONTAL" : "JSlider.VERTICAL";
                init.append("        ").append(comp.getName()).append(" = JSlider(")
                    .append(orientStr).append(", ").append(sMin).append(", ").append(sMax).append(", ")
                    .append(sValue).append(")\n");
                break;
            case PROGRESS_BAR:
                Integer pMin = (Integer) comp.getProperty("min");
                Integer pMax = (Integer) comp.getProperty("max");
                Integer pValue = (Integer) comp.getProperty("value");
                String pbOrientation = (String) comp.getProperty("orientation");
                if (pMin == null) pMin = 0;
                if (pMax == null) pMax = 100;
                if (pValue == null) pValue = 0;
                if (pbOrientation == null) pbOrientation = "HORIZONTAL";
                String pbOrientStr = pbOrientation.equals("HORIZONTAL") ? "JProgressBar.HORIZONTAL" : "JProgressBar.VERTICAL";
                init.append("        ").append(comp.getName()).append(" = JProgressBar(")
                    .append(pbOrientStr).append(", ").append(pMin).append(", ").append(pMax).append(")\n");
                init.append("        ").append(comp.getName()).append(".value = ").append(pValue).append("\n");
                break;
            case TABLE:
                Object[] columns = (Object[]) comp.getProperty("columns");
                if (columns != null) {
                    StringBuilder colStr = new StringBuilder();
                    for (int i = 0; i < columns.length; i++) {
                        if (i > 0) colStr.append(", ");
                        colStr.append("\"").append(escapeString(columns[i].toString())).append("\"");
                    }
                    init.append("        val ").append(comp.getName()).append("Columns = arrayOf(")
                        .append(colStr).append(")\n");
                    init.append("        ").append(comp.getName()).append(" = JTable(DefaultTableModel(")
                        .append(comp.getName()).append("Columns, 0))\n");
                }
                break;
            default:
                break;
        }
    }
    
    private String generateEventHandlers() {
        StringBuilder handlers = new StringBuilder();
        List<JantioComponent> components = engine.getComponents();
        
        for (JantioComponent comp : components) {
            String clickHandler = comp.getEvent("actionPerformed");
            if (clickHandler != null && !clickHandler.isEmpty()) {
                handlers.append("        ").append(comp.getName()).append(".addActionListener { e ->\n");
                handlers.append("            // TODO: Implement action\n");
                handlers.append("            ").append(clickHandler).append("\n");
                handlers.append("        }\n\n");
            }
        }
        
        if (handlers.length() > 0) {
            return "    // Event Handlers\n" + handlers.toString();
        } else {
            return "";
        }
    }
    
    private String generateMainMethod() {
        StringBuilder main = new StringBuilder();
        main.append("    companion object {\n");
        main.append("        @JvmStatic\n");
        main.append("        fun main(args: Array<String>) {\n");
        main.append("            try {\n");
        main.append("                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel())\n");
        main.append("            } catch (e: Exception) {\n");
        main.append("                e.printStackTrace()\n");
        main.append("            }\n");
        main.append("            \n");
        main.append("            SwingUtilities.invokeLater {\n");
        main.append("                ").append(engine.getProjectName()).append("().isVisible = true\n");
        main.append("            }\n");
        main.append("        }\n");
        main.append("    }\n");
        return main.toString();
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
