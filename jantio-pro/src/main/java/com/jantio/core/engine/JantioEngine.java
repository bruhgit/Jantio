package com.jantio.core.engine;

import com.jantio.core.model.JantioComponent;
import com.jantio.generators.java.JavaCodeGenerator;
import com.jantio.generators.kotlin.KotlinCodeGenerator;
import com.jantio.ui.swing.SwingRenderer;
import com.jantio.ui.javafx.JavaFXRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Jantio Engine - Core motor for code generation and rendering
 * Handles I/O operations, code generation (Java/Kotlin), and multi-platform rendering
 */
public class JantioEngine {
    private static final Logger logger = LoggerFactory.getLogger(JantioEngine.class);
    
    public enum RenderTarget {
        JAVA_SWING,
        JAVA_FX,
        KOTLIN_SWING,
        KOTLIN_FX
    }
    
    public enum OutputFormat {
        JAVA,
        KOTLIN,
        FXML,
        JSON
    }
    
    private final List<JantioComponent> components;
    private final Map<String, JantioComponent> componentMap;
    private final JavaCodeGenerator javaGenerator;
    private final KotlinCodeGenerator kotlinGenerator;
    private final SwingRenderer swingRenderer;
    private final JavaFXRenderer javaFXRenderer;
    private RenderTarget currentRenderTarget;
    private String projectName;
    private String packageName;
    
    public JantioEngine() {
        this.components = new ArrayList<>();
        this.componentMap = new HashMap<>();
        this.javaGenerator = new JavaCodeGenerator(this);
        this.kotlinGenerator = new KotlinCodeGenerator(this);
        this.swingRenderer = new SwingRenderer(this);
        this.javaFXRenderer = new JavaFXRenderer(this);
        this.currentRenderTarget = RenderTarget.JAVA_SWING;
        this.projectName = "JantioProject";
        this.packageName = "com.example";
        
        logger.info("Jantio Engine initialized");
    }
    
    // Component Management
    public void addComponent(JantioComponent component) {
        if (component == null) {
            logger.warn("Attempted to add null component");
            return;
        }
        
        // Check for overlaps and auto-adjust position
        adjustPositionForOverlap(component);
        
        components.add(component);
        componentMap.put(component.getId(), component);
        logger.debug("Added component: {}", component.getName());
    }
    
    private void adjustPositionForOverlap(JantioComponent newComponent) {
        boolean adjusted = false;
        int maxAttempts = 50;
        int attempts = 0;
        
        while (hasOverlap(newComponent) && attempts < maxAttempts) {
            newComponent.setLocation(newComponent.getX() + 10, newComponent.getY() + 10);
            adjusted = true;
            attempts++;
        }
        
        if (adjusted) {
            logger.debug("Auto-adjusted component position to avoid overlap: {} -> ({}, {})", 
                newComponent.getName(), newComponent.getX(), newComponent.getY());
        }
    }
    
    private boolean hasOverlap(JantioComponent component) {
        for (JantioComponent existing : components) {
            if (existing != component && component.overlapsWith(existing)) {
                return true;
            }
        }
        return false;
    }
    
    public void removeComponent(String componentId) {
        JantioComponent component = componentMap.remove(componentId);
        if (component != null) {
            components.remove(component);
            logger.debug("Removed component: {}", component.getName());
        }
    }
    
    public void removeComponent(JantioComponent component) {
        removeComponent(component.getId());
    }
    
    public JantioComponent getComponent(String id) {
        return componentMap.get(id);
    }
    
    public List<JantioComponent> getComponents() {
        return new ArrayList<>(components);
    }
    
    public JantioComponent findComponentAt(int x, int y) {
        // Search in reverse order (top-most first)
        for (int i = components.size() - 1; i >= 0; i--) {
            JantioComponent comp = components.get(i);
            if (comp.containsPoint(x, y)) {
                return comp;
            }
        }
        return null;
    }
    
    public void clearAllComponents() {
        components.clear();
        componentMap.clear();
        logger.info("Cleared all components");
    }
    
    // Code Generation
    public String generateJavaCode() {
        logger.info("Generating Java code for {} components", components.size());
        return javaGenerator.generateFullClass();
    }
    
    public String generateKotlinCode() {
        logger.info("Generating Kotlin code for {} components", components.size());
        return kotlinGenerator.generateFullClass();
    }
    
    public String generateFXML() {
        logger.info("Generating FXML for {} components", components.size());
        return javaGenerator.generateFXML();
    }
    
    public String generateCode(OutputFormat format) {
        return switch (format) {
            case JAVA -> generateJavaCode();
            case KOTLIN -> generateKotlinCode();
            case FXML -> generateFXML();
            case JSON -> generateJSON();
        };
    }
    
    private String generateJSON() {
        // Simple JSON serialization
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"projectName\": \"").append(projectName).append("\",\n");
        json.append("  \"packageName\": \"").append(packageName).append("\",\n");
        json.append("  \"components\": [\n");
        
        for (int i = 0; i < components.size(); i++) {
            JantioComponent comp = components.get(i);
            json.append("    {\n");
            json.append("      \"id\": \"").append(comp.getId()).append("\",\n");
            json.append("      \"type\": \"").append(comp.getType().name()).append("\",\n");
            json.append("      \"name\": \"").append(comp.getName()).append("\",\n");
            json.append("      \"x\": ").append(comp.getX()).append(",\n");
            json.append("      \"y\": ").append(comp.getY()).append(",\n");
            json.append("      \"width\": ").append(comp.getWidth()).append(",\n");
            json.append("      \"height\": ").append(comp.getHeight()).append("\n");
            json.append("    }");
            if (i < components.size() - 1) json.append(",");
            json.append("\n");
        }
        
        json.append("  ]\n");
        json.append("}\n");
        
        return json.toString();
    }
    
    // Rendering
    public void renderToSwing(java.awt.Container container) {
        logger.debug("Rendering {} components to Swing", components.size());
        swingRenderer.render(container, components);
    }
    
    public void renderToJavaFX(javafx.scene.Parent parent) {
        logger.debug("Rendering {} components to JavaFX", components.size());
        javaFXRenderer.render(parent, components);
    }
    
    public void setRenderTarget(RenderTarget target) {
        this.currentRenderTarget = target;
        logger.info("Render target set to: {}", target);
    }
    
    public RenderTarget getCurrentRenderTarget() {
        return currentRenderTarget;
    }
    
    // Properties
    public String getProjectName() {
        return projectName;
    }
    
    public void setProjectName(String projectName) {
        this.projectName = projectName;
        logger.debug("Project name set to: {}", projectName);
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public void setPackageName(String packageName) {
        this.packageName = packageName;
        logger.debug("Package name set to: {}", packageName);
    }
    
    public int getComponentCount() {
        return components.size();
    }
    
    // Validation
    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        
        // Check for duplicate names
        Map<String, Integer> nameCounts = new HashMap<>();
        for (JantioComponent comp : components) {
            nameCounts.merge(comp.getName(), 1, Integer::sum);
        }
        
        for (Map.Entry<String, Integer> entry : nameCounts.entrySet()) {
            if (entry.getValue() > 1) {
                errors.add("Duplicate component name: " + entry.getKey());
            }
        }
        
        // Check for components outside bounds
        for (JantioComponent comp : components) {
            if (comp.getX() < 0 || comp.getY() < 0) {
                errors.add("Component " + comp.getName() + " has negative coordinates");
            }
        }
        
        return errors;
    }
    
    public boolean isValid() {
        return validate().isEmpty();
    }
}
