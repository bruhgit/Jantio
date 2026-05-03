package com.jantio.exporter;

import com.jantio.components.ComponentType;
import com.jantio.components.DesignerComponent;

import java.util.List;

/**
 * Java Kod Exporter - Swing bileşenlerinden Java kodu üretir
 */
public class JavaCodeExporter implements CodeExporter {
    
    @Override
    public String generateCode(String className, List<DesignerComponent> components) {
        StringBuilder code = new StringBuilder();
        
        code.append("import javax.swing.*;\n");
        code.append("import java.awt.*;\n\n");
        code.append("public class ").append(className).append(" extends JFrame {\n\n");
        code.append("    public ").append(className).append("() {\n");
        code.append("        setTitle(\"").append(className).append("\");\n");
        code.append("        setSize(800, 600);\n");
        code.append("        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n");
        code.append("        setLocationRelativeTo(null);\n");
        code.append("        setLayout(null);\n\n");
        code.append("        initComponents();\n");
        code.append("    }\n\n");
        code.append("    private void initComponents() {\n");
        
        int counter = 1;
        for (DesignerComponent dc : components) {
            code.append("        ").append(dc.generateCode("component" + counter));
            code.append("        add(component").append(counter).append(");\n");
            counter++;
        }
        
        code.append("    }\n\n");
        code.append("    public static void main(String[] args) {\n");
        code.append("        SwingUtilities.invokeLater(() -> {\n");
        code.append("            try {\n");
        code.append("                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());\n");
        code.append("            } catch (Exception e) {\n");
        code.append("                e.printStackTrace();\n");
        code.append("            }\n");
        code.append("            new ").append(className).append("().setVisible(true);\n");
        code.append("        });\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    @Override
    public String getFileExtension() {
        return "java";
    }
    
    @Override
    public String getLanguageName() {
        return "Java";
    }
}
