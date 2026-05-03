package com.jantio.engine;

import com.jantio.components.DesignerComponent;
import com.jantio.components.ComponentType;

import java.util.List;
import java.util.Map;

/**
 * Jantio Engine - Ana motor sınıfı
 * İki temel amacı vardır:
 * 1. I/O elementlerinin Java/Kotlin kodlarını oluşturur
 * 2. JavaFX, Swing render işlemlerini yönetir
 */
public class JantioEngine {
    
    private static JantioEngine instance;
    
    private JantioEngine() {
        // Singleton pattern
    }
    
    public static JantioEngine getInstance() {
        if (instance == null) {
            instance = new JantioEngine();
        }
        return instance;
    }
    
    /**
     * Java kodu üretir
     */
    public String generateJavaCode(List<DesignerComponent> components, String className, String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("package ").append(packageName).append(";\n\n");
        code.append("import javax.swing.*;\n");
        code.append("import java.awt.*;\n\n");
        code.append("public class ").append(className).append(" extends JFrame {\n\n");
        code.append("    public ").append(className).append("() {\n");
        code.append("        setTitle(\"").append(className).append("\");\n");
        code.append("        setSize(800, 600);\n");
        code.append("        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n");
        code.append("        setLayout(null);\n\n");
        
        // Bileşenleri oluştur
        for (DesignerComponent component : components) {
            code.append(generateJavaComponent(component));
        }
        
        code.append("    }\n\n");
        code.append("    public static void main(String[] args) {\n");
        code.append("        SwingUtilities.invokeLater(() -> {\n");
        code.append("            new ").append(className).append("().setVisible(true);\n");
        code.append("        });\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    /**
     * Kotlin kodu üretir
     */
    public String generateKotlinCode(List<DesignerComponent> components, String className, String packageName) {
        StringBuilder code = new StringBuilder();
        
        code.append("package ").append(packageName).append("\n\n");
        code.append("import javax.swing.*\n");
        code.append("import java.awt.*\n\n");
        code.append("class ").append(className).append(" : JFrame() {\n\n");
        code.append("    init {\n");
        code.append("        title = \"").append(className).append("\"\n");
        code.append("        setSize(800, 600)\n");
        code.append("        defaultCloseOperation = JFrame.EXIT_ON_CLOSE\n");
        code.append("        layout = null\n\n");
        
        // Bileşenleri oluştur
        for (DesignerComponent component : components) {
            code.append(generateKotlinComponent(component));
        }
        
        code.append("    }\n\n");
        code.append("    companion object {\n");
        code.append("        @JvmStatic\n");
        code.append("        fun main(args: Array<String>) {\n");
        code.append("            SwingUtilities.invokeLater {\n");
        code.append("                ").append(className).append("().isVisible = true\n");
        code.append("            }\n");
        code.append("        }\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    /**
     * Java bileşen kodu üretir
     */
    private String generateJavaComponent(DesignerComponent component) {
        StringBuilder code = new StringBuilder();
        ComponentType type = component.getType();
        String varName = component.getName();
        
        code.append("        ");
        
        switch (type) {
            case BUTTON:
                code.append("JButton ").append(varName).append(" = new JButton(\"").append(component.getText()).append("\");\n");
                break;
            case LABEL:
                code.append("JLabel ").append(varName).append(" = new JLabel(\"").append(component.getText()).append("\");\n");
                break;
            case TEXT_FIELD:
                code.append("JTextField ").append(varName).append(" = new JTextField();\n");
                break;
            case TEXT_AREA:
                code.append("JTextArea ").append(varName).append(" = new JTextArea();\n");
                break;
            case CHECK_BOX:
                code.append("JCheckBox ").append(varName).append(" = new JCheckBox(\"").append(component.getText()).append("\");\n");
                break;
            case RADIO_BUTTON:
                code.append("JRadioButton ").append(varName).append(" = new JRadioButton(\"").append(component.getText()).append("\");\n");
                break;
            case COMBO_BOX:
                code.append("JComboBox<String> ").append(varName).append(" = new JComboBox<>();\n");
                break;
            case PANEL:
                code.append("JPanel ").append(varName).append(" = new JPanel();\n");
                break;
            case SLIDER:
                code.append("JSlider ").append(varName).append(" = new JSlider();\n");
                break;
            case PROGRESS_BAR:
                code.append("JProgressBar ").append(varName).append(" = new JProgressBar();\n");
                break;
            case LIST:
                code.append("JList<String> ").append(varName).append(" = new JList<>();\n");
                break;
            case TABBED_PANE:
                code.append("JTabbedPane ").append(varName).append(" = new JTabbedPane();\n");
                break;
            case SCROLL_PANE:
                code.append("JScrollPane ").append(varName).append(" = new JScrollPane();\n");
                break;
            case TOOL_BAR:
                code.append("JToolBar ").append(varName).append(" = new JToolBar();\n");
                break;
            case COLOR_CHOOSER:
                code.append("JColorChooser ").append(varName).append(" = new JColorChooser();\n");
                break;
            default:
                code.append("// Unknown component: ").append(type).append("\n");
        }
        
        // Pozisyon ve boyut ayarla
        code.append("        ").append(varName)
            .append(".setBounds(")
            .append(component.getX()).append(", ")
            .append(component.getY()).append(", ")
            .append(component.getWidth()).append(", ")
            .append(component.getHeight())
            .append(");\n");
        
        code.append("        add(").append(varName).append(");\n\n");
        
        return code.toString();
    }
    
    /**
     * Kotlin bileşen kodu üretir
     */
    private String generateKotlinComponent(DesignerComponent component) {
        StringBuilder code = new StringBuilder();
        ComponentType type = component.getType();
        String varName = component.getName();
        
        code.append("        ");
        
        switch (type) {
            case BUTTON:
                code.append("val ").append(varName).append(" = JButton(\"").append(component.getText()).append("\")\n");
                break;
            case LABEL:
                code.append("val ").append(varName).append(" = JLabel(\"").append(component.getText()).append("\")\n");
                break;
            case TEXT_FIELD:
                code.append("val ").append(varName).append(" = JTextField()\n");
                break;
            case TEXT_AREA:
                code.append("val ").append(varName).append(" = JTextArea()\n");
                break;
            case CHECK_BOX:
                code.append("val ").append(varName).append(" = JCheckBox(\"").append(component.getText()).append("\")\n");
                break;
            case RADIO_BUTTON:
                code.append("val ").append(varName).append(" = JRadioButton(\"").append(component.getText()).append("\")\n");
                break;
            case COMBO_BOX:
                code.append("val ").append(varName).append(" = JComboBox<String>()\n");
                break;
            case PANEL:
                code.append("val ").append(varName).append(" = JPanel()\n");
                break;
            case SLIDER:
                code.append("val ").append(varName).append(" = JSlider()\n");
                break;
            case PROGRESS_BAR:
                code.append("val ").append(varName).append(" = JProgressBar()\n");
                break;
            case LIST:
                code.append("val ").append(varName).append(" = JList<String>()\n");
                break;
            case TABBED_PANE:
                code.append("val ").append(varName).append(" = JTabbedPane()\n");
                break;
            case SCROLL_PANE:
                code.append("val ").append(varName).append(" = JScrollPane()\n");
                break;
            case TOOL_BAR:
                code.append("val ").append(varName).append(" = JToolBar()\n");
                break;
            case COLOR_CHOOSER:
                code.append("val ").append(varName).append(" = JColorChooser()\n");
                break;
            default:
                code.append("// Unknown component: ").append(type).append("\n");
        }
        
        // Pozisyon ve boyut ayarla
        code.append("        ").append(varName)
            .append(".setBounds(")
            .append(component.getX()).append(", ")
            .append(component.getY()).append(", ")
            .append(component.getWidth()).append(", ")
            .append(component.getHeight())
            .append(")\n");
        
        code.append("        add(").append(varName).append(")\n\n");
        
        return code.toString();
    }
    
    /**
     * JavaFX render için hazırlık yapar
     */
    public String prepareJavaFXRender(List<DesignerComponent> components, String className) {
        StringBuilder code = new StringBuilder();
        
        code.append("// JavaFX Render Hazırlığı\n");
        code.append("import javafx.application.Application;\n");
        code.append("import javafx.scene.Scene;\n");
        code.append("import javafx.scene.control.*;\n");
        code.append("import javafx.scene.layout.Pane;\n");
        code.append("import javafx.stage.Stage;\n\n");
        code.append("public class ").append(className).append("FX extends Application {\n\n");
        code.append("    @Override\n");
        code.append("    public void start(Stage primaryStage) {\n");
        code.append("        Pane root = new Pane();\n");
        
        for (DesignerComponent component : components) {
            code.append(generateJavaFXComponent(component));
        }
        
        code.append("        Scene scene = new Scene(root, 800, 600);\n");
        code.append("        primaryStage.setTitle(\"").append(className).append("\");\n");
        code.append("        primaryStage.setScene(scene);\n");
        code.append("        primaryStage.show();\n");
        code.append("    }\n\n");
        code.append("    public static void main(String[] args) {\n");
        code.append("        launch(args);\n");
        code.append("    }\n");
        code.append("}\n");
        
        return code.toString();
    }
    
    /**
     * JavaFX bileşen kodu üretir
     */
    private String generateJavaFXComponent(DesignerComponent component) {
        StringBuilder code = new StringBuilder();
        ComponentType type = component.getType();
        String varName = component.getName();
        
        code.append("        ");
        
        switch (type) {
            case BUTTON:
                code.append("Button ").append(varName).append(" = new Button(\"").append(component.getText()).append("\");\n");
                break;
            case LABEL:
                code.append("Label ").append(varName).append(" = new Label(\"").append(component.getText()).append("\");\n");
                break;
            case TEXT_FIELD:
                code.append("TextField ").append(varName).append(" = new TextField();\n");
                break;
            case TEXT_AREA:
                code.append("TextArea ").append(varName).append(" = new TextArea();\n");
                break;
            case CHECK_BOX:
                code.append("CheckBox ").append(varName).append(" = new CheckBox(\"").append(component.getText()).append("\");\n");
                break;
            case RADIO_BUTTON:
                code.append("RadioButton ").append(varName).append(" = new RadioButton(\"").append(component.getText()).append("\");\n");
                break;
            case COMBO_BOX:
                code.append("ComboBox<String> ").append(varName).append(" = new ComboBox<>();\n");
                break;
            case SLIDER:
                code.append("Slider ").append(varName).append(" = new Slider();\n");
                break;
            case PROGRESS_BAR:
                code.append("ProgressBar ").append(varName).append(" = new ProgressBar();\n");
                break;
            case LIST:
                code.append("ListView<String> ").append(varName).append(" = new ListView<>();\n");
                break;
            case TABBED_PANE:
                code.append("TabPane ").append(varName).append(" = new TabPane();\n");
                break;
            case TOOL_BAR:
                code.append("ToolBar ").append(varName).append(" = new ToolBar();\n");
                break;
            case COLOR_CHOOSER:
                code.append("ColorPicker ").append(varName).append(" = new ColorPicker();\n");
                break;
            default:
                code.append("// JavaFX doesn't support: ").append(type).append("\n");
                return code.toString();
        }
        
        // Pozisyon ve boyut ayarla
        code.append("        ").append(varName)
            .append(".setLayoutX(").append(component.getX()).append(");\n");
        code.append("        ").append(varName)
            .append(".setLayoutY(").append(component.getY()).append(");\n");
        code.append("        ").append(varName)
            .append(".setPrefWidth(").append(component.getWidth()).append(");\n");
        code.append("        ").append(varName)
            .append(".setPrefHeight(").append(component.getHeight()).append(");\n");
        
        code.append("        root.getChildren().add(").append(varName).append(");\n\n");
        
        return code.toString();
    }
    
    /**
     * Swing render için hazırlık yapar
     */
    public String prepareSwingRender(List<DesignerComponent> components, String className) {
        return generateJavaCode(components, className, "com.jantio.generated");
    }
    
    /**
     * Render modunu belirle
     */
    public enum RenderMode {
        SWING,
        JAVA_FX
    }
    
    /**
     * Seçilen moda göre render al
     */
    public String render(List<DesignerComponent> components, String className, RenderMode mode) {
        switch (mode) {
            case JAVA_FX:
                return prepareJavaFXRender(components, className);
            case SWING:
            default:
                return prepareSwingRender(components, className);
        }
    }
}
