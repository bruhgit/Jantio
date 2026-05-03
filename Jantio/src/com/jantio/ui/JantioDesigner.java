package com.jantio.ui;

import com.jantio.components.ComponentType;
import com.jantio.components.DesignerComponent;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ana tasarım yüzeyi - bileşenlerin yerleştirildiği alan
 */
public class JantioDesigner extends JPanel {
    
    private List<DesignerComponent> components;
    private DesignerComponent selectedComponent;
    private PropertiesPanel propertiesPanel;
    
    public JantioDesigner(PropertiesPanel propertiesPanel) {
        this.propertiesPanel = propertiesPanel;
        this.components = new ArrayList<>();
        initComponents();
    }
    
    private void initComponents() {
        setLayout(null);
        setBackground(new Color(240, 240, 245));
        setBorder(BorderFactory.createLineBorder(new Color(180, 180, 190), 1));
        
        // Mouse dinleyicileri
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                handleMousePress(e);
            }
        });
        
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            @Override
            public void mouseDragged(java.awt.event.MouseEvent e) {
                handleMouseDrag(e);
            }
        });
    }
    
    public void addComponent(ComponentType type) {
        DesignerComponent designerComp = new DesignerComponent(type);
        components.add(designerComp);
        add(designerComp.getComponent());
        
        // Bileşeni seç
        selectComponent(designerComp);
        
        revalidate();
        repaint();
    }
    
    private void handleMousePress(java.awt.event.MouseEvent e) {
        Component clickedComponent = findComponentAtPoint(e.getPoint());
        
        if (clickedComponent != null) {
            DesignerComponent designerComp = findDesignerComponent(clickedComponent);
            if (designerComp != null) {
                selectComponent(designerComp);
            }
        } else {
            deselectAll();
        }
    }
    
    private void handleMouseDrag(java.awt.event.MouseEvent e) {
        if (selectedComponent != null) {
            Point p = e.getPoint();
            selectedComponent.setBounds(p.x, p.y, selectedComponent.getWidth(), selectedComponent.getHeight());
            propertiesPanel.refreshProperties();
            repaint();
        }
    }
    
    private Component findComponentAtPoint(Point point) {
        for (int i = components.size() - 1; i >= 0; i--) {
            DesignerComponent dc = components.get(i);
            Rectangle bounds = dc.getComponent().getBounds();
            if (bounds.contains(point)) {
                return dc.getComponent();
            }
        }
        return null;
    }
    
    private DesignerComponent findDesignerComponent(Component swingComponent) {
        for (DesignerComponent dc : components) {
            if (dc.getComponent() == swingComponent || 
                (swingComponent.getParent() instanceof JScrollPane && 
                 ((JScrollPane) swingComponent.getParent()).getViewport().getView() == swingComponent)) {
                return dc;
            }
        }
        return null;
    }
    
    private void selectComponent(DesignerComponent component) {
        if (selectedComponent != null) {
            highlightComponent(selectedComponent, false);
        }
        
        selectedComponent = component;
        
        if (selectedComponent != null) {
            highlightComponent(selectedComponent, true);
            propertiesPanel.setSelectedComponent(selectedComponent);
        }
    }
    
    private void highlightComponent(DesignerComponent component, boolean selected) {
        JComponent comp = component.getComponent();
        if (selected) {
            comp.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 150, 255), 2),
                comp.getBorder()
            ));
        } else {
            comp.setBorder(null);
        }
    }
    
    private void deselectAll() {
        if (selectedComponent != null) {
            highlightComponent(selectedComponent, false);
            selectedComponent = null;
        }
        propertiesPanel.setSelectedComponent(null);
        repaint();
    }
    
    public void removeSelectedComponent() {
        if (selectedComponent != null) {
            components.remove(selectedComponent);
            remove(selectedComponent.getComponent());
            selectedComponent = null;
            propertiesPanel.setSelectedComponent(null);
            revalidate();
            repaint();
        }
    }
    
    public void clearAll() {
        components.clear();
        removeAll();
        selectedComponent = null;
        propertiesPanel.setSelectedComponent(null);
        revalidate();
        repaint();
    }
    
    public List<DesignerComponent> getDesignerComponents() {
        return components;
    }
    
    public String generateCode(String className) {
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
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Izgara çizgileri
        g2d.setColor(new Color(230, 230, 235));
        int gridSize = 20;
        for (int x = 0; x < getWidth(); x += gridSize) {
            g2d.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += gridSize) {
            g2d.drawLine(0, y, getWidth(), y);
        }
        
        g2d.dispose();
    }
}
