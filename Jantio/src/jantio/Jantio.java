package jantio;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Jantio - Java GUI Builder
 * A simple drag-and-drop GUI builder for Swing applications.
 */
public class Jantio extends JFrame {
    
    private JComponent palettePanel;
    private JComponent designPanel;
    private JScrollPane designScrollPane;
    private JPanel propertiesPanel;
    private JList<String> componentList;
    private JTextField selectedComponentName;
    private JComboBox<String> selectedComponentType;
    private JButton generateButton;
    private JButton clearButton;
    private List<GuiComponent> components = new ArrayList<>();
    private GuiComponent selectedComponent = null;
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    
    public Jantio() {
        setTitle("Jantio - Java GUI Builder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        
        initComponents();
        setupDragAndDrop();
    }
    
    private void initComponents() {
        // Main split pane
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        mainSplitPane.setResizeWeight(0.7);
        
        // Left panel - Palette and Design
        JPanel leftPanel = new JPanel(new BorderLayout());
        
        // Component Palette
        JPanel paletteContainer = new JPanel(new BorderLayout());
        paletteContainer.setBorder(BorderFactory.createTitledBorder("Components"));
        palettePanel = createPalettePanel();
        paletteContainer.add(palettePanel, BorderLayout.CENTER);
        
        // Design Area
        JPanel designContainer = new JPanel(new BorderLayout());
        designContainer.setBorder(BorderFactory.createTitledBorder("Design Canvas"));
        designScrollPane = new JScrollPane();
        designPanel = createDesignPanel();
        designScrollPane.setViewportView(designPanel);
        designContainer.add(designScrollPane, BorderLayout.CENTER);
        
        leftPanel.add(paletteContainer, BorderLayout.NORTH);
        leftPanel.add(designContainer, BorderLayout.CENTER);
        
        // Right panel - Properties and Actions
        JPanel rightPanel = new JPanel(new BorderLayout());
        
        // Component List
        JPanel listContainer = new JPanel(new BorderLayout());
        listContainer.setBorder(BorderFactory.createTitledBorder("Added Components"));
        componentList = new JList<>(listModel);
        componentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        componentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = componentList.getSelectedIndex();
                if (index >= 0 && index < components.size()) {
                    selectedComponent = components.get(index);
                    updatePropertiesPanel();
                    highlightSelectedComponent();
                }
            }
        });
        JScrollPane listScrollPane = new JScrollPane(componentList);
        listContainer.add(listScrollPane, BorderLayout.CENTER);
        
        // Properties Panel
        propertiesPanel = new JPanel();
        propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS));
        JScrollPane propertiesScrollPane = new JScrollPane(propertiesPanel);
        propertiesScrollPane.setBorder(BorderFactory.createTitledBorder("Properties"));
        
        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        generateButton = new JButton("Generate Code");
        generateButton.addActionListener(e -> generateCode());
        
        clearButton = new JButton("Clear All");
        clearButton.addActionListener(e -> clearAll());
        
        buttonPanel.add(generateButton);
        buttonPanel.add(clearButton);
        
        rightPanel.add(listContainer, BorderLayout.NORTH);
        rightPanel.add(propertiesScrollPane, BorderLayout.CENTER);
        rightPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        mainSplitPane.setLeftComponent(leftPanel);
        mainSplitPane.setRightComponent(rightPanel);
        
        add(mainSplitPane);
    }
    
    private JPanel createPalettePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setPreferredSize(new Dimension(300, 200));
        
        String[] componentTypes = {
            "JButton", "JLabel", "JTextField", "JTextArea",
            "JCheckBox", "JRadioButton", "JComboBox", "JPanel"
        };
        
        for (String type : componentTypes) {
            JButton btn = new JButton(type);
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            panel.add(btn);
        }
        
        return panel;
    }
    
    private JPanel createDesignPanel() {
        JPanel panel = new JPanel(null); // Null layout for absolute positioning
        panel.setPreferredSize(new Dimension(800, 600));
        panel.setBackground(Color.WHITE);
        panel.setName("designPanel");
        
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    // Double click to edit component properties
                    if (selectedComponent != null) {
                        editComponentProperties(selectedComponent);
                    }
                }
            }
        });
        
        return panel;
    }
    
    private void setupDragAndDrop() {
        // Setup drop target for design panel
        DropTarget dropTarget = new DropTarget(designPanel, new DropTargetAdapter() {
            @Override
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    Point dropPoint = dtde.getLocation();
                    
                    // Get the component type from the transferable
                    Transferable t = dtde.getTransferable();
                    if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                        String componentType = (String) t.getTransferData(DataFlavor.stringFlavor);
                        addComponentToDesign(componentType, dropPoint.x, dropPoint.y);
                    }
                    
                    dtde.dropComplete(true);
                } catch (Exception e) {
                    dtde.dropComplete(false);
                }
            }
        });
        
        // Setup drag source for palette buttons
        Component[] paletteComponents = palettePanel.getComponents();
        for (Component comp : paletteComponents) {
            if (comp instanceof JButton) {
                JButton btn = (JButton) comp;
                DragSource dragSource = DragSource.getDefaultDragSource();
                dragSource.createDefaultDragGestureRecognizer(btn, DnDConstants.ACTION_COPY, 
                    new DragGestureListener() {
                        @Override
                        public void dragGestureRecognized(DragGestureEvent dge) {
                            String componentType = ((JButton) dge.getComponent()).getText();
                            dge.startDrag(DragSource.DefaultCopyDrop, 
                                new StringSelection(componentType), new DragSourceAdapter() {});
                        }
                    });
            }
        }
    }
    
    private void addComponentToDesign(String componentType, int x, int y) {
        GuiComponent guiComp = new GuiComponent(componentType, x, y);
        components.add(guiComp);
        listModel.addElement(componentType + " (" + x + ", " + y + ")");
        
        JComponent swingComponent = createSwingComponent(componentType);
        swingComponent.setBounds(x, y, 100, 30);
        swingComponent.setName(guiComp.getId());
        
        // Add mouse listener for selection
        swingComponent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    selectComponent(guiComp);
                }
            }
        });
        
        // Make component draggable
        makeComponentDraggable(swingComponent, guiComp);
        
        designPanel.add(swingComponent);
        designPanel.revalidate();
        designPanel.repaint();
        
        // Select the newly added component
        selectComponent(guiComp);
    }
    
    private JComponent createSwingComponent(String type) {
        switch (type) {
            case "JButton":
                return new JButton("Button");
            case "JLabel":
                return new JLabel("Label");
            case "JTextField":
                return new JTextField("Text Field");
            case "JTextArea":
                JTextArea textArea = new JTextArea("Text Area");
                textArea.setRows(3);
                return textArea;
            case "JCheckBox":
                return new JCheckBox("Check Box");
            case "JRadioButton":
                return new JRadioButton("Radio Button");
            case "JComboBox":
                return new JComboBox<>(new String[]{"Item 1", "Item 2", "Item 3"});
            case "JPanel":
                JPanel panel = new JPanel();
                panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                panel.setPreferredSize(new Dimension(150, 100));
                return panel;
            default:
                return new JLabel("Unknown");
        }
    }
    
    private void makeComponentDraggable(JComponent component, GuiComponent guiComp) {
        final int[] startX = new int[1];
        final int[] startY = new int[1];
        
        component.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startX[0] = e.getX();
                startY[0] = e.getY();
                selectComponent(guiComp);
            }
        });
        
        component.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int newX = component.getX() + e.getX() - startX[0];
                int newY = component.getY() + e.getY() - startY[0];
                component.setLocation(newX, newY);
                guiComp.setX(newX);
                guiComp.setY(newY);
                
                // Update list item
                int index = components.indexOf(guiComp);
                if (index >= 0) {
                    listModel.set(index, guiComp.getType() + " (" + newX + ", " + newY + ")");
                }
            }
        });
    }
    
    private void selectComponent(GuiComponent guiComp) {
        selectedComponent = guiComp;
        int index = components.indexOf(guiComp);
        if (index >= 0) {
            componentList.setSelectedIndex(index);
        }
        updatePropertiesPanel();
        highlightSelectedComponent();
    }
    
    private void highlightSelectedComponent() {
        // Remove previous highlights
        for (Component comp : designPanel.getComponents()) {
            if (comp instanceof JComponent) {
                Border originalBorder = ((JComponent) comp).getBorder();
                if (originalBorder instanceof javax.swing.border.LineBorder) {
                    javax.swing.border.LineBorder lineBorder = (javax.swing.border.LineBorder) originalBorder;
                    if (lineBorder.getLineColor() == Color.BLUE) {
                        ((JComponent) comp).setBorder(null);
                    }
                }
            }
        }
        
        // Add highlight to selected component
        if (selectedComponent != null) {
            for (Component comp : designPanel.getComponents()) {
                if (comp instanceof JComponent && comp.getName() != null && 
                    comp.getName().equals(selectedComponent.getId())) {
                    ((JComponent) comp).setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
                    break;
                }
            }
        }
        
        designPanel.repaint();
    }
    
    private void updatePropertiesPanel() {
        propertiesPanel.removeAll();
        
        if (selectedComponent == null) {
            propertiesPanel.add(new JLabel("No component selected"));
        } else {
            propertiesPanel.add(new JLabel("Component: " + selectedComponent.getType()));
            propertiesPanel.add(Box.createVerticalStrut(10));
            
            // X Position
            JPanel xPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            xPanel.add(new JLabel("X: "));
            JSpinner xSpinner = new JSpinner(new SpinnerNumberModel(selectedComponent.getX(), 0, 2000, 1));
            xSpinner.addChangeListener(e -> {
                selectedComponent.setX((Integer) xSpinner.getValue());
                updateComponentPosition(selectedComponent);
            });
            xPanel.add(xSpinner);
            propertiesPanel.add(xPanel);
            
            // Y Position
            JPanel yPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            yPanel.add(new JLabel("Y: "));
            JSpinner ySpinner = new JSpinner(new SpinnerNumberModel(selectedComponent.getY(), 0, 2000, 1));
            ySpinner.addChangeListener(e -> {
                selectedComponent.setY((Integer) ySpinner.getValue());
                updateComponentPosition(selectedComponent);
            });
            yPanel.add(ySpinner);
            propertiesPanel.add(yPanel);
            
            // Width
            JPanel widthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            widthPanel.add(new JLabel("Width: "));
            JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel(100, 10, 500, 1));
            widthSpinner.addChangeListener(e -> {
                updateComponentSize(selectedComponent, (Integer) widthSpinner.getValue(), 
                    selectedComponent.getHeight());
            });
            widthPanel.add(widthSpinner);
            propertiesPanel.add(widthPanel);
            
            // Height
            JPanel heightPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            heightPanel.add(new JLabel("Height: "));
            JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel(30, 10, 500, 1));
            heightSpinner.addChangeListener(e -> {
                updateComponentSize(selectedComponent, selectedComponent.getWidth(), 
                    (Integer) heightSpinner.getValue());
            });
            heightPanel.add(heightSpinner);
            propertiesPanel.add(heightPanel);
            
            // Delete button
            JButton deleteBtn = new JButton("Delete Component");
            deleteBtn.addActionListener(e -> deleteSelectedComponent());
            propertiesPanel.add(Box.createVerticalStrut(10));
            propertiesPanel.add(deleteBtn);
        }
        
        propertiesPanel.revalidate();
        propertiesPanel.repaint();
    }
    
    private void updateComponentPosition(GuiComponent guiComp) {
        Component[] comps = designPanel.getComponents();
        for (Component comp : comps) {
            if (comp instanceof JComponent && comp.getName() != null && 
                comp.getName().equals(guiComp.getId())) {
                comp.setLocation(guiComp.getX(), guiComp.getY());
                
                // Update list item
                int index = components.indexOf(guiComp);
                if (index >= 0) {
                    listModel.set(index, guiComp.getType() + " (" + guiComp.getX() + ", " + guiComp.getY() + ")");
                }
                break;
            }
        }
        designPanel.repaint();
    }
    
    private void updateComponentSize(GuiComponent guiComp, int width, int height) {
        guiComp.setWidth(width);
        guiComp.setHeight(height);
        
        Component[] comps = designPanel.getComponents();
        for (Component comp : comps) {
            if (comp instanceof JComponent && comp.getName() != null && 
                comp.getName().equals(guiComp.getId())) {
                comp.setSize(width, height);
                break;
            }
        }
        designPanel.repaint();
    }
    
    private void deleteSelectedComponent() {
        if (selectedComponent == null) return;
        
        int index = components.indexOf(selectedComponent);
        if (index >= 0) {
            components.remove(index);
            listModel.remove(index);
            
            // Remove from design panel
            Component[] comps = designPanel.getComponents();
            for (Component comp : comps) {
                if (comp instanceof JComponent && comp.getName() != null && 
                    comp.getName().equals(selectedComponent.getId())) {
                    designPanel.remove(comp);
                    break;
                }
            }
            
            selectedComponent = null;
            updatePropertiesPanel();
            designPanel.revalidate();
            designPanel.repaint();
        }
    }
    
    private void editComponentProperties(GuiComponent guiComp) {
        String newName = JOptionPane.showInputDialog(this, 
            "Enter component variable name:", 
            guiComp.getId());
        if (newName != null && !newName.trim().isEmpty()) {
            guiComp.setId(newName.trim());
        }
    }
    
    private void generateCode() {
        if (components.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No components to generate. Add some components first.", 
                "Info", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        StringBuilder code = new StringBuilder();
        code.append("import javax.swing.*;\n");
        code.append("import java.awt.*;\n\n");
        code.append("public class GeneratedGUI extends JFrame {\n\n");
        code.append("    public GeneratedGUI() {\n");
        code.append("        setTitle(\"Generated by Jantio\");\n");
        code.append("        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);\n");
        code.append("        setSize(800, 600);\n");
        code.append("        setLocationRelativeTo(null);\n");
        code.append("        setLayout(null);\n\n");
        
        // Generate component declarations and initialization
        for (GuiComponent comp : components) {
            code.append(generateComponentCode(comp));
        }
        
        code.append("    }\n\n");
        code.append("    public static void main(String[] args) {\n");
        code.append("        SwingUtilities.invokeLater(() -> {\n");
        code.append("            new GeneratedGUI().setVisible(true);\n");
        code.append("        });\n");
        code.append("    }\n");
        code.append("}\n");
        
        // Show code in dialog
        JTextArea codeArea = new JTextArea(code.toString());
        codeArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(codeArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Generated Java Code:"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        JButton saveButton = new JButton("Save to File");
        saveButton.addActionListener(e -> saveCodeToFile(code.toString()));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(saveButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        JOptionPane.showMessageDialog(this, panel, "Generated Code", 
            JOptionPane.INFORMATION_MESSAGE, null);
    }
    
    private String generateComponentCode(GuiComponent comp) {
        StringBuilder code = new StringBuilder();
        String varName = comp.getId();
        String type = comp.getType();
        
        switch (type) {
            case "JButton":
                code.append("        JButton ").append(varName).append(" = new JButton(\"Button\");\n");
                break;
            case "JLabel":
                code.append("        JLabel ").append(varName).append(" = new JLabel(\"Label\");\n");
                break;
            case "JTextField":
                code.append("        JTextField ").append(varName).append(" = new JTextField(\"Text Field\");\n");
                break;
            case "JTextArea":
                code.append("        JTextArea ").append(varName).append(" = new JTextArea(\"Text Area\");\n");
                code.append("        ").append(varName).append(".setRows(3);\n");
                break;
            case "JCheckBox":
                code.append("        JCheckBox ").append(varName).append(" = new JCheckBox(\"Check Box\");\n");
                break;
            case "JRadioButton":
                code.append("        JRadioButton ").append(varName).append(" = new JRadioButton(\"Radio Button\");\n");
                break;
            case "JComboBox":
                code.append("        JComboBox<String> ").append(varName).append(" = new JComboBox<>(new String[]{\"Item 1\", \"Item 2\", \"Item 3\"});\n");
                break;
            case "JPanel":
                code.append("        JPanel ").append(varName).append(" = new JPanel();\n");
                code.append("        ").append(varName).append(".setBorder(BorderFactory.createLineBorder(Color.GRAY));\n");
                break;
        }
        
        code.append("        ").append(varName).append(".setBounds(")
            .append(comp.getX()).append(", ").append(comp.getY()).append(", ")
            .append(comp.getWidth()).append(", ").append(comp.getHeight()).append(");\n");
        code.append("        add(").append(varName).append(");\n\n");
        
        return code.toString();
    }
    
    private void saveCodeToFile(String code) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save Generated Code");
        fileChooser.setSelectedFile(new File("GeneratedGUI.java"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(code);
                JOptionPane.showMessageDialog(this, 
                    "Code saved successfully to: " + file.getAbsolutePath(), 
                    "Success", 
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error saving file: " + e.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearAll() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to clear all components?", 
            "Confirm Clear", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            components.clear();
            listModel.clear();
            designPanel.removeAll();
            selectedComponent = null;
            updatePropertiesPanel();
            designPanel.revalidate();
            designPanel.repaint();
        }
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            Jantio jantio = new Jantio();
            jantio.setVisible(true);
        });
    }
}

/**
 * Represents a GUI component in the designer.
 */
class GuiComponent {
    private String id;
    private String type;
    private int x, y;
    private int width = 100;
    private int height = 30;
    
    public GuiComponent(String type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.id = type.toLowerCase() + System.currentTimeMillis();
    }
    
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }
    public int getY() { return y; }
    public void setY(int y) { this.y = y; }
    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }
    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }
}
