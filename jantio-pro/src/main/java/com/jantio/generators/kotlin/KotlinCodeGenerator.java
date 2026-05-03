package com.jantio.generators.kotlin;

import com.jantio.core.engine.JantioEngine;
import com.jantio.core.model.JantioComponent;

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
        val code = StringBuilder()
        
        // Package declaration
        code.append("package ${engine.packageName}\n\n")
        
        // Imports
        code.append(generateImports())
        
        // Class declaration
        code.append("class ${engine.projectName} : JFrame() {\n\n")
        
        // Component declarations
        code.append(generateComponentDeclarations())
        
        // Constructor (init block)
        code.append(generateConstructor())
        
        // Initialization method
        code.append(generateInitializeComponents())
        
        // Event handlers
        code.append(generateEventHandlers())
        
        // Companion object with main
        code.append(generateMainMethod())
        
        code.append("}\n")
        
        return code.toString()
    }
    
    private String generateImports() = buildString {
        append("import javax.swing.*\n")
        append("import javax.swing.border.*\n")
        append("import java.awt.*\n")
        append("import java.awt.event.*\n\n")
    }
    
    private String generateComponentDeclarations(): String {
        val declarations = StringBuilder()
        val components = engine.components
        
        for (comp in components) {
            if (comp.type.isLayout) continue
            
            val type = comp.type.className
            val name = comp.name
            declarations.append("    private lateinit var $name: $type\n")
        }
        
        if (components.isNotEmpty()) {
            declarations.append("\n")
        }
        
        return declarations.toString()
    }
    
    private String generateConstructor(): String = buildString {
        append("    init {\n")
        append("        title = \"${engine.projectName}\"\n")
        append("        defaultCloseOperation = EXIT_ON_CLOSE\n")
        append("        setSize(800, 600)\n")
        append("        locationRelativeTo = null\n")
        append("        layout = null\n")
        append("        \n")
        append("        initializeComponents()\n")
        append("    }\n\n")
    }
    
    private String generateInitializeComponents(): String {
        val init = StringBuilder()
        init.append("    private fun initializeComponents() {\n")
        
        val components = engine.components
        
        for (comp in components) {
            if (comp.type.isLayout) continue
            
            init.append("        // Initialize ${comp.name}\n")
            init.append("        ${comp.name} = ${comp.type.className}()\n")
            
            // Set bounds
            init.append("        ${comp.name}.setBounds(${comp.x}, ${comp.y}, ${comp.width}, ${comp.height})\n")
            
            // Set text if applicable
            if (!comp.text.isNullOrEmpty()) {
                init.append("        ${comp.name}.text = \"${escapeString(comp.text)}\"\n")
            }
            
            // Set tool tip
            if (!comp.toolTipText.isNullOrEmpty()) {
                init.append("        ${comp.name}.toolTipText = \"${escapeString(comp.toolTipText)}\"\n")
            }
            
            // Set enabled/visible
            if (!comp.enabled) {
                init.append("        ${comp.name}.isEnabled = false\n")
            }
            if (!comp.visible) {
                init.append("        ${comp.name}.isVisible = false\n")
            }
            
            // Set colors
            if (comp.backgroundColor != null) {
                init.append("        ${comp.name}.background = Color(${comp.backgroundColor.red}, ${comp.backgroundColor.green}, ${comp.backgroundColor.blue})\n")
            }
            if (comp.foregroundColor != null) {
                init.append("        ${comp.name}.foreground = Color(${comp.foregroundColor.red}, ${comp.foregroundColor.green}, ${comp.foregroundColor.blue})\n")
            }
            
            // Special properties
            generateSpecialProperties(init, comp)
            
            // Add to container
            init.append("        add(${comp.name})\n\n")
        }
        
        init.append("    }\n\n")
        return init.toString()
    }
    
    private void generateSpecialProperties(init: StringBuilder, comp: JantioComponent) {
        when (comp.type) {
            JantioComponent.ComponentType.COMBO_BOX, 
            JantioComponent.ComponentType.LIST -> {
                val items = comp.getProperty("items") as? Array<*>
                items?.forEach { item ->
                    init.append("        ${comp.name}.addItem(\"${escapeString(item.toString())}\")\n")
                }
            }
            JantioComponent.ComponentType.SPINNER -> {
                val min = comp.getProperty("min") as? Int ?: 0
                val max = comp.getProperty("max") as? Int ?: 100
                val value = comp.getProperty("value") as? Int ?: 0
                init.append("        ${comp.name} = JSpinner(SpinnerNumberModel($value, $min, $max, 1))\n")
            }
            JantioComponent.ComponentType.SLIDER -> {
                val min = comp.getProperty("min") as? Int ?: 0
                val max = comp.getProperty("max") as? Int ?: 100
                val value = comp.getProperty("value") as? Int ?: 50
                val orientation = comp.getProperty("orientation") as? String ?: "HORIZONTAL"
                val orientStr = if (orientation == "HORIZONTAL") "JSlider.HORIZONTAL" else "JSlider.VERTICAL"
                init.append("        ${comp.name} = JSlider($orientStr, $min, $max, $value)\n")
            }
            JantioComponent.ComponentType.PROGRESS_BAR -> {
                val min = comp.getProperty("min") as? Int ?: 0
                val max = comp.getProperty("max") as? Int ?: 100
                val value = comp.getProperty("value") as? Int ?: 0
                val orientation = comp.getProperty("orientation") as? String ?: "HORIZONTAL"
                val orientStr = if (orientation == "HORIZONTAL") "JProgressBar.HORIZONTAL" else "JProgressBar.VERTICAL"
                init.append("        ${comp.name} = JProgressBar($orientStr, $min, $max)\n")
                init.append("        ${comp.name}.value = $value\n")
            }
            JantioComponent.ComponentType.TABLE -> {
                val columns = comp.getProperty("columns") as? Array<*>
                if (columns != null) {
                    val colStr = columns.joinToString(", ") { "\"$it\"" }
                    init.append("        val ${comp.name}Columns = arrayOf($colStr)\n")
                    init.append("        ${comp.name} = JTable(DefaultTableModel(${comp.name}Columns, 0))\n")
                }
            }
            else -> {}
        }
    }
    
    private String generateEventHandlers(): String {
        val handlers = StringBuilder()
        val components = engine.components
        
        for (comp in components) {
            val clickHandler = comp.getEvent("actionPerformed")
            if (!clickHandler.isNullOrEmpty()) {
                handlers.append("        ${comp.name}.addActionListener { e ->\n")
                handlers.append("            // TODO: Implement action\n")
                handlers.append("            $clickHandler\n")
                handlers.append("        }\n\n")
            }
        }
        
        return if (handlers.isNotEmpty()) {
            "    // Event Handlers\n" + handlers.toString()
        } else {
            ""
        }
    }
    
    private String generateMainMethod(): String = buildString {
        append("    companion object {\n")
        append("        @JvmStatic\n")
        append("        fun main(args: Array<String>) {\n")
        append("            try {\n")
        append("                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel())\n")
        append("            } catch (e: Exception) {\n")
        append("                e.printStackTrace()\n")
        append("            }\n")
        append("            \n")
        append("            SwingUtilities.invokeLater {\n")
        append("                ${engine.projectName}().isVisible = true\n")
        append("            }\n")
        append("        }\n")
        append("    }\n")
    }
    
    private String escapeString(str: String?): String {
        if (str == null) return ""
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t")
    }
}
