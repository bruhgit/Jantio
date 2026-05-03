package com.jantio.exporter;

import com.jantio.components.ComponentType;
import com.jantio.components.DesignerComponent;

import java.util.List;

/**
 * Kotlin Kod Exporter - Swing bileşenlerinden Kotlin kodu üretir
 */
public class KotlinCodeExporter implements CodeExporter {
    
    @Override
    public String generateCode(String className, List<DesignerComponent> components) {
        StringBuilder code = new StringBuilder();
        
        code.append("import javax.swing.*\n");
        code.append("import java.awt.*\n\n");
        code.append("class ").append(className).append(" : JFrame() {\n\n");
        code.append("    init {\n");
        code.append("        title = \"").append(className).append("\"\n");
        code.append("        defaultCloseOperation = EXIT_ON_CLOSE\n");
        code.append("        setSize(800, 600)\n");
        code.append("        locationRelativeTo = null\n");
        code.append("        layout = null\n\n");
        code.append("        initComponents()\n");
        code.append("    }\n\n");
        code.append("    private fun initComponents() {\n");
        
        int counter = 1;
        for (DesignerComponent dc : components) {
            code.append(dc.generateKotlinCode("component" + counter));
            code.append("        add(component").append(counter).append(")\n");
            counter++;
        }
        
        code.append("    }\n\n");
        code.append("    companion object {\n");
        code.append("        @JvmStatic\n");
        code.append("        fun main(args: Array<String>) {\n");
        code.append("            SwingUtilities.invokeLater {\n");
        code.append("                try {\n");
        code.append("                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())\n");
        code.append("                } catch (e: Exception) {\n");
        code.append("                    e.printStackTrace()\n");
        code.append("                }\n");
        code.append("                ").append(className).append("().isVisible = true\n");
        code.append("            }\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    @Override
    public String getFileExtension() {
        return "kt";
    }
    
    @Override
    public String getLanguageName() {
        return "Kotlin";
    }
}
