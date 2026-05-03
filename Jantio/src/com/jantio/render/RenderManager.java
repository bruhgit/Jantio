package com.jantio.render;

import com.jantio.components.DesignerComponent;
import com.jantio.engine.JantioEngine;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Render Manager - Farklı render motorlarını yönetir
 */
public class RenderManager {
    
    private JantioEngine engine;
    private Component currentComponent;
    private RenderMode activeMode;
    
    public RenderManager() {
        this.engine = JantioEngine.getInstance();
        this.activeMode = RenderMode.SWING;
    }
    
    /**
     * Render modunu ayarla
     */
    public void setRenderMode(RenderMode mode) {
        this.activeMode = mode;
    }
    
    /**
     * Aktif render modunu al
     */
    public RenderMode getRenderMode() {
        return activeMode;
    }
    
    /**
     * Bileşenleri render al ve göster
     */
    public Component render(List<DesignerComponent> components, String className) {
        switch (activeMode) {
            case JAVA_FX:
                return renderJavaFX(components, className);
            case SWING:
            default:
                return renderSwing(components, className);
        }
    }
    
    /**
     * Swing ile render al
     */
    private Component renderSwing(List<DesignerComponent> components, String className) {
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(800, 600));
        
        for (DesignerComponent component : components) {
            JComponent swingComponent = createSwingComponent(component);
            if (swingComponent != null) {
                swingComponent.setBounds(
                    component.getX(),
                    component.getY(),
                    component.getWidth(),
                    component.getHeight()
                );
                panel.add(swingComponent);
            }
        }
        
        this.currentComponent = panel;
        return panel;
    }
    
    /**
     * JavaFX ile render al (Preview olarak)
     */
    private Component renderJavaFX(List<DesignerComponent> components, String className) {
        // JavaFX preview için Swing wrapper kullanıyoruz
        // Gerçek JavaFX render için ayrı bir pencere açılır
        JPanel panel = new JPanel(null);
        panel.setPreferredSize(new Dimension(800, 600));
        
        JLabel label = new JLabel("JavaFX Render Modu - Önizleme");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(0, 0, 800, 600);
        panel.add(label);
        
        // Bileşenleri yine de ekleyelim
        for (DesignerComponent component : components) {
            JComponent swingComponent = createSwingComponent(component);
            if (swingComponent != null) {
                swingComponent.setBounds(
                    component.getX(),
                    component.getY(),
                    component.getWidth(),
                    component.getHeight()
                );
                panel.add(swingComponent);
            }
        }
        
        this.currentComponent = panel;
        return panel;
    }
    
    /**
     * DesignerComponent'ten Swing bileşeni oluştur
     */
    private JComponent createSwingComponent(DesignerComponent component) {
        switch (component.getType()) {
            case BUTTON:
                JButton button = new JButton(component.getText());
                applyStyle(button, component);
                return button;
                
            case LABEL:
                JLabel label = new JLabel(component.getText());
                applyStyle(label, component);
                return label;
                
            case TEXT_FIELD:
                JTextField textField = new JTextField();
                applyStyle(textField, component);
                return textField;
                
            case TEXT_AREA:
                JTextArea textArea = new JTextArea();
                applyStyle(textArea, component);
                return textArea;
                
            case CHECK_BOX:
                JCheckBox checkBox = new JCheckBox(component.getText());
                applyStyle(checkBox, component);
                return checkBox;
                
            case RADIO_BUTTON:
                JRadioButton radioButton = new JRadioButton(component.getText());
                applyStyle(radioButton, component);
                return radioButton;
                
            case COMBO_BOX:
                JComboBox<String> comboBox = new JComboBox<>();
                applyStyle(comboBox, component);
                return comboBox;
                
            case PANEL:
                JPanel panel = new JPanel();
                applyStyle(panel, component);
                return panel;
                
            case SLIDER:
                JSlider slider = new JSlider();
                applyStyle(slider, component);
                return slider;
                
            case PROGRESS_BAR:
                JProgressBar progressBar = new JProgressBar();
                applyStyle(progressBar, component);
                return progressBar;
                
            case LIST:
                JList<String> list = new JList<>();
                applyStyle(list, component);
                return list;
                
                
            case TABBED_PANE:
                JTabbedPane tabbedPane = new JTabbedPane();
                applyStyle(tabbedPane, component);
                return tabbedPane;
                
            case SCROLL_PANE:
                JScrollPane scrollPane = new JScrollPane();
                applyStyle(scrollPane, component);
                return scrollPane;
                
            case TOOL_BAR:
                JToolBar toolBar = new JToolBar();
                applyStyle(toolBar, component);
                return toolBar;
                
                
            case COLOR_CHOOSER:
                JColorChooser colorChooser = new JColorChooser();
                applyStyle(colorChooser, component);
                return colorChooser;
                
                
            default:
                return null;
        }
    }
    
    /**
     * Bileşene stil uygula
     */
    private void applyStyle(JComponent component, DesignerComponent designerComponent) {
        if (designerComponent.getBackgroundColor() != null) {
            component.setBackground(designerComponent.getBackgroundColor());
        }
        if (designerComponent.getForegroundColor() != null) {
            component.setForeground(designerComponent.getForegroundColor());
        }
        if (designerComponent.getFont() != null) {
            component.setFont(designerComponent.getFont());
        }
        component.setToolTipText(designerComponent.getTooltip());
    }
    
    /**
     * Java kodu üret
     */
    public String generateJavaCode(List<DesignerComponent> components, String className, String packageName) {
        return engine.generateJavaCode(components, className, packageName);
    }
    
    /**
     * Kotlin kodu üret
     */
    public String generateKotlinCode(List<DesignerComponent> components, String className, String packageName) {
        return engine.generateKotlinCode(components, className, packageName);
    }
    
    /**
     * JavaFX kodu üret
     */
    public String generateJavaFXCode(List<DesignerComponent> components, String className) {
        return engine.prepareJavaFXRender(components, className);
    }
    
    /**
     * Render modu enum
     */
    public enum RenderMode {
        SWING("Swing"),
        JAVA_FX("JavaFX");
        
        private final String displayName;
        
        RenderMode(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}
