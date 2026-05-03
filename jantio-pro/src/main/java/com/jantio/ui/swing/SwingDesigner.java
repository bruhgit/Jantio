package com.jantio.ui.swing;

import com.jantio.core.engine.JantioEngine;
import com.jantio.core.model.JantioComponent;
import com.jantio.generators.java.JavaCodeGenerator;
import com.jantio.generators.kotlin.KotlinCodeGenerator;
import com.jantio.ui.splash.SplashScreen;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * Swing Designer - Main GUI for Jantio
 */
public class SwingDesigner extends JFrame {
    private final JantioEngine engine;
    private final SwingRenderer renderer;
    private JPanel canvas;
    private JList<String> componentList;
    private PropertiesPanel propertiesPanel;
    private JantioComponent selectedComponent;
    
    private static final String[] COMPONENTS = {
        "Button", "Label", "TextField", "TextArea", "CheckBox",
        "RadioButton", "ComboBox", "List", "Slider", "ProgressBar",
        "Spinner", "Table", "Tree", "ScrollPane", "TabbedPane",
        "ToolBar", "StatusBar", "SplitPane", "Panel"
    };
    
    public SwingDesigner(JantioEngine engine) {
        this.engine = engine;
        this.renderer = new SwingRenderer(engine);
        
        initComponents();
        setupEventHandlers();
        
        setTitle("Jantio Pro - Swing Designer");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        
        // Create menu bar
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        
        // Left panel - Component palette
        JPanel leftPanel = createComponentPalette();
        add(leftPanel, BorderLayout.WEST);
        
        // Center - Canvas
        canvas = new JPanel();
        canvas.setBackground(Color.WHITE);
        canvas.setLayout(null);
        JScrollPane canvasScroll = new JScrollPane(canvas);
        canvasScroll.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        add(canvasScroll, BorderLayout.CENTER);
        
        // Right panel - Properties
        propertiesPanel = new PropertiesPanel();
        add(propertiesPanel, BorderLayout.EAST);
        
        // Status bar
        JLabel statusBar = new JLabel("Ready");
        statusBar.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        add(statusBar, BorderLayout.SOUTH);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        
        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem newProject = new JMenuItem("New Project");
        newProject.addActionListener(e -> engine.newProject());
        
        JMenuItem openProject = new JMenuItem("Open Project");
        openProject.addActionListener(e -> openProject());
        
        JMenuItem saveProject = new JMenuItem("Save Project");
        saveProject.addActionListener(e -> saveProject());
        
        JMenu exportMenu = new JMenu("Export Code");
        JMenuItem exportJava = new JMenuItem("Java");
        exportJava.addActionListener(e -> exportJavaCode());
        
        JMenuItem exportKotlin = new JMenuItem("Kotlin");
        exportKotlin.addActionListener(e -> exportKotlinCode());
        
        exportMenu.add(exportJava);
        exportMenu.add(exportKotlin);
        
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> System.exit(0));
        
        fileMenu.add(newProject);
        fileMenu.add(openProject);
        fileMenu.add(saveProject);
        fileMenu.add(exportMenu);
        fileMenu.addSeparator();
        fileMenu.add(exit);
        
        // Edit menu
        JMenu editMenu = new JMenu("Edit");
        JMenuItem undo = new JMenuItem("Undo");
        undo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK));
        
        JMenuItem redo = new JMenuItem("Redo");
        redo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK));
        
        JMenuItem delete = new JMenuItem("Delete");
        delete.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        delete.addActionListener(e -> deleteSelectedComponent());
        
        editMenu.add(undo);
        editMenu.add(redo);
        editMenu.addSeparator();
        editMenu.add(delete);
        
        // View menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem zoomIn = new JMenuItem("Zoom In");
        JMenuItem zoomOut = new JMenuItem("Zoom Out");
        JMenuItem preview = new JMenuItem("Preview");
        preview.addActionListener(e -> previewDesign());
        
        viewMenu.add(zoomIn);
        viewMenu.add(zoomOut);
        viewMenu.addSeparator();
        viewMenu.add(preview);
        
        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> showAbout());
        helpMenu.add(about);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(viewMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    private JPanel createComponentPalette() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(200, 0));
        panel.setBorder(BorderFactory.createTitledBorder("Components"));
        
        componentList = new JList<>(COMPONENTS);
        componentList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        componentList.setLayoutOrientation(JList.VERTICAL);
        componentList.setVisibleRowCount(-1);
        
        JScrollPane listScroll = new JScrollPane(componentList);
        panel.add(listScroll, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupEventHandlers() {
        // Component selection
        componentList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && componentList.getSelectedValue() != null) {
                addComponent(componentList.getSelectedValue());
            }
        });
        
        // Canvas click - select component
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                selectComponentAt(e.getPoint());
            }
        });
        
        // Drag and drop
        canvas.addMouseMotionListener(new MouseMotionAdapter() {
            private Point dragStart;
            private JantioComponent draggingComp;
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedComponent != null) {
                    if (dragStart == null) {
                        dragStart = e.getPoint();
                        draggingComp = selectedComponent;
                    } else {
                        int dx = e.getX() - dragStart.x;
                        int dy = e.getY() - dragStart.y;
                        draggingComp.setLocation(draggingComp.getX() + dx, draggingComp.getY() + dy);
                        dragStart = e.getPoint();
                        refreshCanvas();
                    }
                }
            }
            
            @Override
            public void mouseMoved(MouseEvent e) {
                dragStart = null;
                draggingComp = null;
            }
        });
        
        // Context menu for resize
        canvas.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                engine.setCanvasSize(canvas.getSize());
            }
        });
    }
    
    private void addComponent(String type) {
        try {
            JantioComponent.ComponentType compType = JantioComponent.ComponentType.valueOf(type.toUpperCase().replace(" ", "_"));
            JantioComponent comp = engine.createComponent(compType);
            
            // Find non-overlapping position
            Point pos = findNonOverlappingPosition(comp.getWidth(), comp.getHeight());
            comp.setLocation(pos.x, pos.y);
            
            refreshCanvas();
            propertiesPanel.setComponent(comp);
            selectedComponent = comp;
            
            JOptionPane.showMessageDialog(this, 
                "Added: " + type + " at (" + pos.x + ", " + pos.y + ")",
                "Component Added",
                JOptionPane.INFORMATION_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                "Unknown component type: " + type,
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private Point findNonOverlappingPosition(int width, int height) {
        List<JantioComponent> components = engine.getComponents();
        int x = 10;
        int y = 10;
        int step = 10;
        
        while (x + width < canvas.getWidth() - 50) {
            boolean overlap = false;
            Rectangle newRect = new Rectangle(x, y, width, height);
            
            for (JantioComponent comp : components) {
                if (comp.getBounds().intersects(newRect)) {
                    overlap = true;
                    break;
                }
            }
            
            if (!overlap) {
                return new Point(x, y);
            }
            
            x += step;
            if (x + width > canvas.getWidth() - 50) {
                x = 10;
                y += step;
            }
        }
        
        return new Point(x, y);
    }
    
    private void selectComponentAt(Point point) {
        List<JantioComponent> components = engine.getComponents();
        for (int i = components.size() - 1; i >= 0; i--) {
            JantioComponent comp = components.get(i);
            if (comp.getBounds().contains(point)) {
                selectedComponent = comp;
                propertiesPanel.setComponent(comp);
                refreshCanvas();
                return;
            }
        }
        selectedComponent = null;
        propertiesPanel.clear();
        refreshCanvas();
    }
    
    private void refreshCanvas() {
        canvas.removeAll();
        renderer.render(canvas, engine.getComponents());
        
        // Highlight selected component
        if (selectedComponent != null) {
            Graphics2D g2d = (Graphics2D) canvas.getGraphics();
            if (g2d != null) {
                g2d.setColor(Color.BLUE);
                g2d.setStroke(new BasicStroke(2));
                g2d.draw(selectedComponent.getBounds());
            }
        }
        
        canvas.revalidate();
        canvas.repaint();
    }
    
    private void deleteSelectedComponent() {
        if (selectedComponent != null) {
            engine.removeComponent(selectedComponent);
            selectedComponent = null;
            propertiesPanel.clear();
            refreshCanvas();
        }
    }
    
    private void exportJavaCode() {
        JavaCodeGenerator generator = new JavaCodeGenerator(engine);
        String code = generator.generateFullClass();
        saveCodeToFile(code, "*.java");
    }
    
    private void exportKotlinCode() {
        KotlinCodeGenerator generator = new KotlinCodeGenerator(engine);
        String code = generator.generateFullClass();
        saveCodeToFile(code, "*.kt");
    }
    
    private void saveCodeToFile(String code, String extension) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setSelectedFile(new java.io.File("GeneratedClass" + extension.substring(1)));
        
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(chooser.getSelectedFile())) {
                writer.write(code);
                JOptionPane.showMessageDialog(this,
                    "Code exported successfully!",
                    "Export Complete",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error saving file: " + e.getMessage(),
                    "Export Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void openProject() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                engine.loadProject(chooser.getSelectedFile().getAbsolutePath());
                refreshCanvas();
                JOptionPane.showMessageDialog(this,
                    "Project loaded successfully!",
                    "Open Complete",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error loading project: " + e.getMessage(),
                    "Open Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void saveProject() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setSelectedFile(new java.io.File("project.jantio"));
        
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                engine.saveProject(chooser.getSelectedFile().getAbsolutePath());
                JOptionPane.showMessageDialog(this,
                    "Project saved successfully!",
                    "Save Complete",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                    "Error saving project: " + e.getMessage(),
                    "Save Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void previewDesign() {
        JFrame previewFrame = new JFrame("Preview - " + engine.getProjectName());
        previewFrame.setSize(800, 600);
        previewFrame.setLocationRelativeTo(this);
        previewFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(null);
        previewPanel.setBackground(Color.WHITE);
        
        renderer.render(previewPanel, engine.getComponents());
        
        previewFrame.add(previewPanel);
        previewFrame.setVisible(true);
    }
    
    private void showAbout() {
        JOptionPane.showMessageDialog(this,
            "Jantio Pro v2.0\n\nA professional GUI builder for Java Swing and JavaFX.\n\nFeatures:\n- Drag & Drop Design\n- Java/Kotlin Code Export\n- Real-time Preview\n- 19+ Components\n\n© 2024 Jantio Team",
            "About Jantio",
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Properties Panel - Shows and edits component properties
     */
    private class PropertiesPanel extends JPanel {
        private JantioComponent component;
        private JTextField nameField;
        private JTextField textField;
        private JTextField xField, yField, widthField, heightField;
        private JButton bgColorButton, fgColorButton;
        
        public PropertiesPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setPreferredSize(new Dimension(250, 0));
            setBorder(BorderFactory.createTitledBorder("Properties"));
            
            createPropertyFields();
        }
        
        private void createPropertyFields() {
            JPanel mainPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            
            // Name
            gbc.gridx = 0; gbc.gridy = 0;
            mainPanel.add(new JLabel("Name:"), gbc);
            gbc.gridx = 1;
            nameField = new JTextField(15);
            nameField.addActionListener(e -> updateComponent());
            mainPanel.add(nameField, gbc);
            
            // Text
            gbc.gridx = 0; gbc.gridy = 1;
            mainPanel.add(new JLabel("Text:"), gbc);
            gbc.gridx = 1;
            textField = new JTextField(15);
            textField.addActionListener(e -> updateComponent());
            mainPanel.add(textField, gbc);
            
            // Position and Size
            gbc.gridx = 0; gbc.gridy = 2;
            mainPanel.add(new JLabel("X:"), gbc);
            gbc.gridx = 1;
            xField = new JTextField(5);
            xField.addActionListener(e -> updateComponent());
            mainPanel.add(xField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 3;
            mainPanel.add(new JLabel("Y:"), gbc);
            gbc.gridx = 1;
            yField = new JTextField(5);
            yField.addActionListener(e -> updateComponent());
            mainPanel.add(yField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 4;
            mainPanel.add(new JLabel("Width:"), gbc);
            gbc.gridx = 1;
            widthField = new JTextField(5);
            widthField.addActionListener(e -> updateComponent());
            mainPanel.add(widthField, gbc);
            
            gbc.gridx = 0; gbc.gridy = 5;
            mainPanel.add(new JLabel("Height:"), gbc);
            gbc.gridx = 1;
            heightField = new JTextField(5);
            heightField.addActionListener(e -> updateComponent());
            mainPanel.add(heightField, gbc);
            
            // Colors
            gbc.gridx = 0; gbc.gridy = 6;
            mainPanel.add(new JLabel("Background:"), gbc);
            gbc.gridx = 1;
            bgColorButton = new JButton("Choose");
            bgColorButton.addActionListener(e -> chooseBackgroundColor());
            mainPanel.add(bgColorButton, gbc);
            
            gbc.gridx = 0; gbc.gridy = 7;
            mainPanel.add(new JLabel("Foreground:"), gbc);
            gbc.gridx = 1;
            fgColorButton = new JButton("Choose");
            fgColorButton.addActionListener(e -> chooseForegroundColor());
            mainPanel.add(fgColorButton, gbc);
            
            add(mainPanel);
            
            // Context menu for resize
            JPopupMenu resizeMenu = new JPopupMenu();
            JMenuItem smallSize = new JMenuItem("Small (80x30)");
            smallSize.addActionListener(e -> resizeComponent(80, 30));
            
            JMenuItem mediumSize = new JMenuItem("Medium (120x40)");
            mediumSize.addActionListener(e -> resizeComponent(120, 40));
            
            JMenuItem largeSize = new JMenuItem("Large (200x60)");
            largeSize.addActionListener(e -> resizeComponent(200, 60));
            
            JMenuItem customSize = new JMenuItem("Custom...");
            customSize.addActionListener(e -> showCustomSizeDialog());
            
            resizeMenu.add(smallSize);
            resizeMenu.add(mediumSize);
            resizeMenu.add(largeSize);
            resizeMenu.addSeparator();
            resizeMenu.add(customSize);
            
            setComponentPopupMenu(resizeMenu);
        }
        
        private void resizeComponent(int w, int h) {
            if (component != null) {
                component.setSize(w, h);
                refreshCanvas();
            }
        }
        
        private void showCustomSizeDialog() {
            if (component == null) return;
            
            JTextField wField = new JTextField(String.valueOf(component.getWidth()), 5);
            JTextField hField = new JTextField(String.valueOf(component.getHeight()), 5);
            
            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("Width:"));
            panel.add(wField);
            panel.add(new JLabel("Height:"));
            panel.add(hField);
            
            int result = JOptionPane.showConfirmDialog(SwingDesigner.this, panel,
                "Set Custom Size", JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                try {
                    int w = Integer.parseInt(wField.getText());
                    int h = Integer.parseInt(hField.getText());
                    component.setSize(w, h);
                    refreshCanvas();
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(SwingDesigner.this,
                        "Invalid number format", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        
        public void setComponent(JantioComponent comp) {
            this.component = comp;
            if (comp != null) {
                nameField.setText(comp.getName());
                textField.setText(comp.getText());
                xField.setText(String.valueOf(comp.getX()));
                yField.setText(String.valueOf(comp.getY()));
                widthField.setText(String.valueOf(comp.getWidth()));
                heightField.setText(String.valueOf(comp.getHeight()));
            }
        }
        
        public void clear() {
            component = null;
            nameField.setText("");
            textField.setText("");
            xField.setText("");
            yField.setText("");
            widthField.setText("");
            heightField.setText("");
        }
        
        private void updateComponent() {
            if (component != null) {
                try {
                    component.setName(nameField.getText());
                    component.setText(textField.getText());
                    component.setX(Integer.parseInt(xField.getText()));
                    component.setY(Integer.parseInt(yField.getText()));
                    component.setWidth(Integer.parseInt(widthField.getText()));
                    component.setHeight(Integer.parseInt(heightField.getText()));
                    refreshCanvas();
                } catch (NumberFormatException e) {
                    // Ignore invalid input
                }
            }
        }
        
        private void chooseBackgroundColor() {
            if (component != null) {
                Color color = JColorChooser.showDialog(SwingDesigner.this,
                    "Choose Background Color",
                    component.getBackgroundColor() != null ? component.getBackgroundColor() : Color.WHITE);
                if (color != null) {
                    component.setBackgroundColor(color);
                    refreshCanvas();
                }
            }
        }
        
        private void chooseForegroundColor() {
            if (component != null) {
                Color color = JColorChooser.showDialog(SwingDesigner.this,
                    "Choose Foreground Color",
                    component.getForegroundColor() != null ? component.getForegroundColor() : Color.BLACK);
                if (color != null) {
                    component.setForegroundColor(color);
                    refreshCanvas();
                }
            }
        }
    }
}
