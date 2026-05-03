package com.jantio.ui.swing;

import com.jantio.core.engine.JantioEngine;
import com.jantio.core.model.JantioComponent;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Swing Renderer - Renders Jantio components to Swing UI
 */
public class SwingRenderer {
    private final JantioEngine engine;
    
    public SwingRenderer(JantioEngine engine) {
        this.engine = engine;
    }
    
    public void render(Container container, List<JantioComponent> components) {
        container.removeAll();
        container.setLayout(null);
        
        for (JantioComponent comp : components) {
            if (comp.getType().isLayout()) continue;
            
            JComponent swingComp = createSwingComponent(comp);
            swingComp.setBounds(comp.getBounds());
            
            if (comp.getText() != null && !comp.getText().isEmpty()) {
                if (swingComp instanceof AbstractButton) {
                    ((AbstractButton) swingComp).setText(comp.getText());
                } else if (swingComp instanceof JLabel) {
                    ((JLabel) swingComp).setText(comp.getText());
                } else if (swingComp instanceof JTextComponent) {
                    ((JTextComponent) swingComp).setText(comp.getText());
                }
            }
            
            if (comp.getToolTipText() != null && !comp.getToolTipText().isEmpty()) {
                swingComp.setToolTipText(comp.getToolTipText());
            }
            
            swingComp.setEnabled(comp.isEnabled());
            swingComp.setVisible(comp.isVisible());
            
            if (comp.getBackgroundColor() != null) {
                swingComp.setBackground(comp.getBackgroundColor());
            }
            if (comp.getForegroundColor() != null) {
                swingComp.setForeground(comp.getForegroundColor());
            }
            
            swingComp.setFont(comp.getFont());
            
            // Set border
            swingComp.setBorder(createBorder(comp.getBorderType()));
            
            // Add special configurations
            configureSpecialComponent(swingComp, comp);
            
            container.add(swingComp);
        }
        
        container.repaint();
        container.revalidate();
    }
    
    private JComponent createSwingComponent(JantioComponent comp) {
        return switch (comp.getType()) {
            case BUTTON -> new JButton();
            case LABEL -> new JLabel();
            case TEXT_FIELD -> new JTextField();
            case TEXT_AREA -> {
                JTextArea area = new JTextArea();
                area.setLineWrap(true);
                area.setWrapStyleWord(true);
                yield new JScrollPane(area);
            }
            case PASSWORD_FIELD -> new JPasswordField();
            case CHECK_BOX -> new JCheckBox();
            case RADIO_BUTTON -> new JRadioButton();
            case COMBO_BOX -> new JComboBox<>();
            case LIST -> {
                JList<?> list = new JList<>();
                yield new JScrollPane(list);
            }
            case SPINNER -> new JSpinner();
            case PANEL -> new JPanel();
            case SCROLL_PANE -> new JScrollPane();
            case TABBED_PANE -> new JTabbedPane();
            case SPLIT_PANE -> new JSplitPane();
            case TOOLBAR -> new JToolBar();
            case TABLE -> {
                JTable table = new JTable();
                yield new JScrollPane(table);
            }
            case TREE -> {
                JTree tree = new JTree();
                yield new JScrollPane(tree);
            }
            case COLOR_CHOOSER -> {
                JPanel panel = new JPanel();
                panel.add(new JLabel("Color:"));
                panel.add(new JButton("Choose"));
                yield panel;
            }
            case FILE_CHOOSER -> {
                JPanel panel = new JPanel();
                panel.add(new JLabel("File:"));
                panel.add(new JButton("Browse"));
                yield panel;
            }
            case PROGRESS_BAR -> new JProgressBar();
            case SLIDER -> new JSlider();
            default -> new JLabel("Unknown");
        };
    }
    
    private void configureSpecialComponent(JComponent swingComp, JantioComponent comp) {
        switch (comp.getType()) {
            case COMBO_BOX -> {
                Object items = comp.getProperty("items");
                if (items instanceof String[] itemArray && swingComp instanceof JComboBox) {
                    JComboBox<?> comboBox = (JComboBox<?>) swingComp;
                    comboBox.removeAllItems();
                    for (String item : itemArray) {
                        comboBox.addItem(item);
                    }
                }
            }
            case LIST -> {
                Object items = comp.getProperty("items");
                if (items instanceof String[] itemArray) {
                    JList<?> list = findChildComponent(swingComp, JList.class);
                    if (list != null) {
                        list.setListData(itemArray);
                    }
                }
            }
            case SLIDER, PROGRESS_BAR -> {
                Object min = comp.getProperty("min");
                Object max = comp.getProperty("max");
                Object value = comp.getProperty("value");
                
                if (comp.getType() == JantioComponent.ComponentType.SLIDER && swingComp instanceof JSlider) {
                    JSlider slider = (JSlider) swingComp;
                    if (min != null) slider.setMinimum((Integer) min);
                    if (max != null) slider.setMaximum((Integer) max);
                    if (value != null) slider.setValue((Integer) value);
                    
                    String orientation = (String) comp.getProperty("orientation");
                    if ("VERTICAL".equals(orientation)) {
                        slider.setOrientation(JSlider.VERTICAL);
                    }
                } else if (comp.getType() == JantioComponent.ComponentType.PROGRESS_BAR && swingComp instanceof JProgressBar) {
                    JProgressBar progressBar = (JProgressBar) swingComp;
                    if (min != null) progressBar.setMinimum((Integer) min);
                    if (max != null) progressBar.setMaximum((Integer) max);
                    if (value != null) progressBar.setValue((Integer) value);
                    
                    String orientation = (String) comp.getProperty("orientation");
                    if ("VERTICAL".equals(orientation)) {
                        progressBar.setOrientation(JProgressBar.VERTICAL);
                    }
                }
            }
            case TABLE -> {
                Object columns = comp.getProperty("columns");
                if (columns instanceof String[] colArray) {
                    JTable table = findChildComponent(swingComp, JTable.class);
                    if (table != null) {
                        DefaultTableModel model = new DefaultTableModel(colArray, 0);
                        table.setModel(model);
                    }
                }
            }
            case TREE -> {
                Object rootNode = comp.getProperty("rootNode");
                if (rootNode != null) {
                    JTree tree = findChildComponent(swingComp, JTree.class);
                    if (tree != null) {
                        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootNode);
                        tree.setModel(new DefaultTreeModel(root));
                    }
                }
            }
            case SPLIT_PANE -> {
                if (swingComp instanceof JSplitPane splitPane) {
                    Object orientation = comp.getProperty("orientation");
                    if ("VERTICAL".equals(orientation)) {
                        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
                    }
                    Object dividerLocation = comp.getProperty("dividerLocation");
                    if (dividerLocation != null) {
                        splitPane.setDividerLocation((Integer) dividerLocation);
                    }
                }
            }
            case TABBED_PANE -> {
                if (swingComp instanceof JTabbedPane tabbedPane) {
                    Object tabPlacement = comp.getProperty("tabPlacement");
                    if (tabPlacement != null) {
                        switch ((String) tabPlacement) {
                            case "TOP" -> tabbedPane.setTabPlacement(JTabbedPane.TOP);
                            case "BOTTOM" -> tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);
                            case "LEFT" -> tabbedPane.setTabPlacement(JTabbedPane.LEFT);
                            case "RIGHT" -> tabbedPane.setTabPlacement(JTabbedPane.RIGHT);
                        }
                    }
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private <T extends Component> T findChildComponent(Component parent, Class<T> clazz) {
        if (clazz.isInstance(parent)) {
            return clazz.cast(parent);
        }
        if (parent instanceof Container container) {
            for (Component child : container.getComponents()) {
                T result = findChildComponent(child, clazz);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
    
    private Border createBorder(String borderType) {
        if (borderType == null || "none".equals(borderType)) {
            return null;
        }
        return switch (borderType) {
            case "line" -> BorderFactory.createLineBorder(Color.GRAY, 1);
            case "bevel" -> BorderFactory.createBevelBorder(BevelBorder.RAISED);
            case "etched" -> BorderFactory.createEtchedBorder();
            case "titled" -> BorderFactory.createTitledBorder("Title");
            case "empty" -> BorderFactory.createEmptyBorder(5, 5, 5, 5);
            default -> null;
        };
    }
}
