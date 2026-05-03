package com.jantio.ui;

import com.jantio.components.ComponentType;
import com.jantio.components.DesignerComponent;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Bileşen paleti - sürükle bırak için bileşen listesi
 */
public class ComponentPalette extends JPanel {
    
    private List<ComponentType> componentTypes;
    private JantioDesigner designer;
    
    public ComponentPalette(JantioDesigner designer) {
        this.designer = designer;
        this.componentTypes = new ArrayList<>();
        initializeComponents();
        initComponents();
    }
    
    private void initializeComponents() {
        componentTypes.add(ComponentType.BUTTON);
        componentTypes.add(ComponentType.LABEL);
        componentTypes.add(ComponentType.TEXT_FIELD);
        componentTypes.add(ComponentType.PASSWORD_FIELD);
        componentTypes.add(ComponentType.FORMATTED_TEXT_FIELD);
        componentTypes.add(ComponentType.TEXT_AREA);
        componentTypes.add(ComponentType.CHECK_BOX);
        componentTypes.add(ComponentType.RADIO_BUTTON);
        componentTypes.add(ComponentType.COMBO_BOX);
        componentTypes.add(ComponentType.LIST);
        componentTypes.add(ComponentType.SLIDER);
        componentTypes.add(ComponentType.PROGRESS_BAR);
        componentTypes.add(ComponentType.SPINNER);
        componentTypes.add(ComponentType.SEPARATOR);
        componentTypes.add(ComponentType.TABBED_PANE);
        componentTypes.add(ComponentType.TOOL_BAR);
        componentTypes.add(ComponentType.SCROLL_PANE);
        componentTypes.add(ComponentType.PANEL);
        componentTypes.add(ComponentType.COLOR_CHOOSER);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 60)),
            "Bileşenler",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            new Color(100, 100, 120)
        ));
        setBackground(new Color(45, 45, 55));
        
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(45, 45, 55));
        
        for (ComponentType type : componentTypes) {
            contentPanel.add(createComponentButton(type));
        }
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUI(new PropertiesPanel.ModernScrollBarUI());
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private JPanel createComponentButton(ComponentType type) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        panel.setBackground(new Color(50, 50, 65));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 70)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // İkon
        JLabel iconLabel = new JLabel(getIconForType(type));
        iconLabel.setPreferredSize(new Dimension(24, 24));
        
        // İsim
        JLabel nameLabel = new JLabel(type.getDisplayName());
        nameLabel.setForeground(new Color(200, 200, 220));
        nameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        panel.add(iconLabel);
        panel.add(nameLabel);
        
        // Hover efekti
        panel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                panel.setBackground(new Color(60, 60, 80));
            }
            
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                panel.setBackground(new Color(50, 50, 65));
            }
            
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                designer.addComponent(type);
            }
        });
        
        return panel;
    }
    
    private Icon getIconForType(ComponentType type) {
        return new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                switch (type) {
                    case BUTTON:
                        g2d.setColor(new Color(100, 150, 200));
                        g2d.fillRoundRect(x + 2, y + 8, 18, 10, 3, 3);
                        g2d.setColor(new Color(200, 200, 220));
                        g2d.drawRect(x + 2, y + 8, 18, 10);
                        break;
                    case LABEL:
                        g2d.setColor(new Color(200, 200, 220));
                        g2d.setFont(new Font("Arial", Font.BOLD, 14));
                        g2d.drawString("Aa", x + 3, y + 18);
                        break;
                    case TEXT_FIELD:
                        g2d.setColor(new Color(60, 60, 70));
                        g2d.fillRect(x + 2, y + 10, 18, 8);
                        g2d.setColor(new Color(150, 150, 170));
                        g2d.drawRect(x + 2, y + 10, 18, 8);
                        break;
                    case PASSWORD_FIELD:
                        g2d.setColor(new Color(60, 60, 70));
                        g2d.fillRect(x + 2, y + 10, 18, 8);
                        g2d.setColor(new Color(150, 150, 170));
                        g2d.drawRect(x + 2, y + 10, 18, 8);
                        g2d.fillOval(x + 5, y + 13, 2, 2);
                        g2d.fillOval(x + 9, y + 13, 2, 2);
                        g2d.fillOval(x + 13, y + 13, 2, 2);
                        break;
                    case FORMATTED_TEXT_FIELD:
                        g2d.setColor(new Color(60, 60, 70));
                        g2d.fillRect(x + 2, y + 10, 18, 8);
                        g2d.setColor(new Color(150, 150, 170));
                        g2d.drawRect(x + 2, y + 10, 18, 8);
                        g2d.setFont(new Font("Monospaced", Font.PLAIN, 8));
                        g2d.drawString("##", x + 4, y + 16);
                        break;
                    case TEXT_AREA:
                        g2d.setColor(new Color(60, 60, 70));
                        g2d.fillRect(x + 2, y + 6, 18, 14);
                        g2d.setColor(new Color(150, 150, 170));
                        g2d.drawRect(x + 2, y + 6, 18, 14);
                        g2d.drawLine(x + 4, y + 11, x + 16, y + 11);
                        g2d.drawLine(x + 4, y + 15, x + 14, y + 15);
                        break;
                    case CHECK_BOX:
                        g2d.setColor(new Color(60, 60, 70));
                        g2d.fillRect(x + 2, y + 8, 10, 10);
                        g2d.setColor(new Color(100, 200, 100));
                        g2d.setStroke(new BasicStroke(2));
                        g2d.drawLine(x + 4, y + 13, x + 7, y + 16);
                        g2d.drawLine(x + 7, y + 16, x + 11, y + 9);
                        break;
                    case RADIO_BUTTON:
                        g2d.setColor(new Color(60, 60, 70));
                        g2d.fillOval(x + 2, y + 8, 10, 10);
                        g2d.setColor(new Color(100, 200, 100));
                        g2d.fillOval(x + 5, y + 11, 4, 4);
                        break;
                    case COMBO_BOX:
                        g2d.setColor(new Color(60, 60, 70));
                        g2d.fillRect(x + 2, y + 10, 18, 8);
                        g2d.setColor(new Color(150, 150, 170));
                        g2d.drawLine(x + 14, y + 12, x + 17, y + 12);
                        g2d.drawLine(x + 17, y + 12, x + 15, y + 15);
                        g2d.drawLine(x + 15, y + 15, x + 14, y + 15);
                        break;
                    case LIST:
                        g2d.setColor(new Color(60, 60, 70));
                        g2d.fillRect(x + 2, y + 6, 18, 14);
                        g2d.setColor(new Color(150, 150, 170));
                        g2d.drawRect(x + 2, y + 6, 18, 14);
                        g2d.drawLine(x + 4, y + 10, x + 16, y + 10);
                        g2d.drawLine(x + 4, y + 13, x + 16, y + 13);
                        g2d.drawLine(x + 4, y + 16, x + 12, y + 16);
                        break;
                    case PANEL:
                        g2d.setColor(new Color(80, 80, 100));
                        g2d.fillRect(x + 2, y + 6, 18, 14);
                        g2d.setColor(new Color(120, 120, 140));
                        g2d.drawRect(x + 2, y + 6, 18, 14);
                        break;
                    case SLIDER:
                        g2d.setColor(new Color(100, 100, 120));
                        g2d.fillRect(x + 2, y + 13, 18, 3);
                        g2d.setColor(new Color(150, 150, 170));
                        g2d.fillOval(x + 8, y + 10, 6, 6);
                        break;
                    case PROGRESS_BAR:
                        g2d.setColor(new Color(60, 60, 70));
                        g2d.fillRect(x + 2, y + 11, 18, 6);
                        g2d.setColor(new Color(100, 200, 100));
                        g2d.fillRect(x + 2, y + 11, 10, 6);
                        break;
                    case SPINNER:
                        g2d.setColor(new Color(60, 60, 70));
                        g2d.fillRect(x + 2, y + 8, 18, 10);
                        g2d.setColor(new Color(150, 150, 170));
                        g2d.drawLine(x + 15, y + 9, x + 15, y + 13);
                        g2d.drawLine(x + 15, y + 9, x + 17, y + 11);
                        g2d.drawLine(x + 15, y + 13, x + 17, y + 11);
                        break;
                    case SEPARATOR:
                        g2d.setColor(new Color(100, 100, 120));
                        g2d.setStroke(new BasicStroke(2));
                        g2d.drawLine(x + 2, y + 12, x + 20, y + 12);
                        break;
                    case TABBED_PANE:
                        g2d.setColor(new Color(80, 80, 100));
                        g2d.fillRect(x + 2, y + 8, 18, 12);
                        g2d.setColor(new Color(60, 60, 70));
                        g2d.fillRect(x + 4, y + 6, 6, 4);
                        g2d.fillRect(x + 12, y + 6, 6, 4);
                        break;
                    case TOOL_BAR:
                        g2d.setColor(new Color(70, 70, 90));
                        g2d.fillRect(x + 2, y + 10, 18, 6);
                        g2d.setColor(new Color(150, 150, 170));
                        g2d.fillOval(x + 4, y + 11, 3, 3);
                        g2d.fillOval(x + 9, y + 11, 3, 3);
                        g2d.fillOval(x + 14, y + 11, 3, 3);
                        break;
                    case SCROLL_PANE:
                        g2d.setColor(new Color(60, 60, 70));
                        g2d.fillRect(x + 2, y + 6, 16, 14);
                        g2d.setColor(new Color(100, 100, 120));
                        g2d.fillRect(x + 16, y + 6, 4, 12);
                        g2d.fillRect(x + 2, y + 18, 14, 2);
                        break;
                    case COLOR_CHOOSER:
                        g2d.setColor(new Color(200, 100, 100));
                        g2d.fillRect(x + 2, y + 8, 5, 5);
                        g2d.setColor(new Color(100, 200, 100));
                        g2d.fillRect(x + 8, y + 8, 5, 5);
                        g2d.setColor(new Color(100, 100, 200));
                        g2d.fillRect(x + 14, y + 8, 5, 5);
                        break;
                }
                
                g2d.dispose();
            }
            
            @Override
            public int getIconWidth() {
                return 24;
            }
            
            @Override
            public int getIconHeight() {
                return 24;
            }
        };
    }
}
