package com.jantio.ui;

import com.jantio.components.ComponentType;
import com.jantio.components.DesignerComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Ana tasarım yüzeyi - bileşenlerin yerleştirildiği alan
 */
public class JantioDesigner extends JPanel {
    
    private List<DesignerComponent> components;
    private DesignerComponent selectedComponent;
    private PropertiesPanel propertiesPanel;
    private JPopupMenu resizePopupMenu;
    
    public JantioDesigner(PropertiesPanel propertiesPanel) {
        this.propertiesPanel = propertiesPanel;
        this.components = new ArrayList<>();
        initComponents();
        createResizePopupMenu();
    }
    
    private void createResizePopupMenu() {
        resizePopupMenu = new JPopupMenu();
        
        JMenuItem resizeSmall = new JMenuItem("Küçük Boyut (80x30)");
        resizeSmall.addActionListener(e -> resizeSelected(80, 30));
        resizePopupMenu.add(resizeSmall);
        
        JMenuItem resizeMedium = new JMenuItem("Orta Boyut (150x50)");
        resizeMedium.addActionListener(e -> resizeSelected(150, 50));
        resizePopupMenu.add(resizeMedium);
        
        JMenuItem resizeLarge = new JMenuItem("Büyük Boyut (250x80)");
        resizeLarge.addActionListener(e -> resizeSelected(250, 80));
        resizePopupMenu.add(resizeLarge);
        
        resizePopupMenu.addSeparator();
        
        JMenuItem customSize = new JMenuItem("Özel Boyut...");
        customSize.addActionListener(e -> showCustomSizeDialog());
        resizePopupMenu.add(customSize);
    }
    
    private void resizeSelected(int width, int height) {
        if (selectedComponent != null) {
            selectedComponent.setBounds(
                selectedComponent.getX(), 
                selectedComponent.getY(), 
                width, 
                height
            );
            propertiesPanel.refreshProperties();
            repaint();
        }
    }
    
    private void showCustomSizeDialog() {
        if (selectedComponent == null) return;
        
        JTextField widthField = new JTextField(String.valueOf(selectedComponent.getWidth()), 10);
        JTextField heightField = new JTextField(String.valueOf(selectedComponent.getHeight()), 10);
        
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        panel.add(new JLabel("Genişlik:"));
        panel.add(widthField);
        panel.add(new JLabel("Yükseklik:"));
        panel.add(heightField);
        
        int result = JOptionPane.showConfirmDialog(
            this, 
            panel, 
            "Boyut Ayarla", 
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                int width = Integer.parseInt(widthField.getText().trim());
                int height = Integer.parseInt(heightField.getText().trim());
                
                if (width > 0 && height > 0) {
                    selectedComponent.setBounds(
                        selectedComponent.getX(), 
                        selectedComponent.getY(), 
                        width, 
                        height
                    );
                    propertiesPanel.refreshProperties();
                    repaint();
                } else {
                    JOptionPane.showMessageDialog(this, "Boyutlar pozitif olmalıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Geçersiz sayı formatı!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void initComponents() {
        setLayout(null);
        setBackground(new Color(240, 240, 245));
        setBorder(BorderFactory.createLineBorder(new Color(180, 180, 190), 1));
        
        // Mouse dinleyicileri
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMousePress(e);
                checkRightClick(e);
            }
        });
        
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                handleMouseDrag(e);
            }
        });
    }
    
    private void checkRightClick(MouseEvent e) {
        if (e.isPopupTrigger()) {
            Component clickedComponent = findComponentAtPoint(e.getPoint());
            if (clickedComponent != null) {
                DesignerComponent designerComp = findDesignerComponent(clickedComponent);
                if (designerComp != null) {
                    selectComponent(designerComp);
                    resizePopupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        }
    }
    
    public void addComponent(ComponentType type) {
        DesignerComponent designerComp = new DesignerComponent(type);
        
        // Çakışma kontrolü - yeni bileşeni boş bir yere yerleştir
        Point newPosition = findNonOverlappingPosition(designerComp);
        designerComp.setBounds(newPosition.x, newPosition.y, designerComp.getWidth(), designerComp.getHeight());
        
        components.add(designerComp);
        add(designerComp.getComponent());
        
        // Bileşeni seç
        selectComponent(designerComp);
        
        revalidate();
        repaint();
    }
    
    /**
     * Bileşenler için çakışmayan pozisyon bulur
     */
    private Point findNonOverlappingPosition(DesignerComponent newComp) {
        int startX = 50;
        int startY = 50;
        int step = 20;
        int maxX = getWidth() - newComp.getWidth();
        int maxY = getHeight() - newComp.getHeight();
        
        // Mevcut tüm pozisyonları dene
        for (int x = startX; x <= maxX; x += step) {
            for (int y = startY; y <= maxY; y += step) {
                Rectangle newRect = new Rectangle(x, y, newComp.getWidth(), newComp.getHeight());
                boolean overlaps = false;
                
                for (DesignerComponent existing : components) {
                    Rectangle existingRect = existing.getComponent().getBounds();
                    if (newRect.intersects(existingRect)) {
                        overlaps = true;
                        break;
                    }
                }
                
                if (!overlaps) {
                    return new Point(x, y);
                }
            }
        }
        
        // Hiç boş yer yoksa, en alta yerleştir
        int lowestY = startY;
        for (DesignerComponent comp : components) {
            int bottomY = comp.getY() + comp.getHeight() + step;
            if (bottomY > lowestY) {
                lowestY = bottomY;
            }
        }
        
        return new Point(startX, lowestY);
    }
    
    /**
     * Taşıma sırasında çakışmayı önle
     */
    private void preventOverlapWhileDragging(DesignerComponent comp, int newX, int newY) {
        Rectangle testRect = new Rectangle(newX, newY, comp.getWidth(), comp.getHeight());
        
        for (DesignerComponent existing : components) {
            if (existing == comp) continue;
            
            Rectangle existingRect = existing.getComponent().getBounds();
            if (testRect.intersects(existingRect)) {
                // Çakışma var - bileşeni it
                if (newX < existing.getX()) {
                    newX = existing.getX() - comp.getWidth() - 5;
                } else if (newX + comp.getWidth() > existing.getX() + existing.getWidth()) {
                    newX = existing.getX() + existing.getWidth() + 5;
                }
                
                if (newY < existing.getY()) {
                    newY = existing.getY() - comp.getHeight() - 5;
                } else if (newY + comp.getHeight() > existing.getY() + existing.getHeight()) {
                    newY = existing.getY() + existing.getHeight() + 5;
                }
                
                // Yeni pozisyonu tekrar kontrol et
                testRect.setLocation(newX, newY);
            }
        }
        
        comp.setBounds(newX, newY, comp.getWidth(), comp.getHeight());
    }
    
    private void handleMousePress(MouseEvent e) {
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
    
    private void handleMouseDrag(MouseEvent e) {
        if (selectedComponent != null) {
            Point p = e.getPoint();
            // Çakışma önleme ile taşı
            preventOverlapWhileDragging(selectedComponent, p.x, p.y);
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
