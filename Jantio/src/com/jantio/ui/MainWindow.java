package com.jantio.ui;

import com.jantio.components.ComponentType;
import com.jantio.components.DesignerComponent;
import com.jantio.exporter.CodeExporter;
import com.jantio.exporter.JavaCodeExporter;
import com.jantio.exporter.KotlinCodeExporter;
import com.jantio.utils.ColorPalette;
import com.jantio.utils.FontHelper;

import javax.swing.*;
import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Jantio Ana Pencere
 */
public class MainWindow extends JFrame {
    
    private JantioDesigner designer;
    private PropertiesPanel propertiesPanel;
    private ComponentPalette componentPalette;
    private CodeExporter currentExporter;
    
    public MainWindow() {
        initComponents();
        setupWindow();
        currentExporter = new JavaCodeExporter(); // Varsayılan olarak Java
    }
    
    private void initComponents() {
        setTitle("Jantio - Java GUI Builder");
        
        // Menü çubuğu
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        
        // Ana bölümler
        propertiesPanel = new PropertiesPanel();
        designer = new JantioDesigner(propertiesPanel);
        componentPalette = new ComponentPalette(designer);
        
        // Scroll pane for designer
        JScrollPane designerScroll = new JScrollPane(designer);
        designerScroll.setBorder(null);
        designerScroll.getVerticalScrollBar().setUI(new PropertiesPanel.ModernScrollBarUI());
        designerScroll.getHorizontalScrollBar().setUI(new PropertiesPanel.ModernScrollBarUI());
        
        // Split panes
        JSplitPane leftSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, componentPalette, designerScroll);
        leftSplit.setDividerLocation(200);
        leftSplit.setBackground(ColorPalette.BACKGROUND_LIGHT);
        
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplit, propertiesPanel);
        mainSplit.setDividerLocation(800);
        mainSplit.setBackground(ColorPalette.BACKGROUND_LIGHT);
        
        add(mainSplit, BorderLayout.CENTER);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(ColorPalette.BACKGROUND_MEDIUM);
        menuBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, ColorPalette.BORDER));
        
        // File menüsü
        JMenu fileMenu = new JMenu("Dosya");
        fileMenu.setFont(FontHelper.createRegular(12));
        fileMenu.setForeground(ColorPalette.TEXT_PRIMARY);
        
        JMenuItem newItem = new JMenuItem("Yeni");
        newItem.setFont(FontHelper.createRegular(11));
        newItem.addActionListener(e -> designer.clearAll());
        
        // Export menüsü
        JMenu exportMenu = new JMenu("Kod Export Et");
        exportMenu.setFont(FontHelper.createRegular(11));
        
        JMenuItem exportJavaItem = new JMenuItem("Java Olarak");
        exportJavaItem.setFont(FontHelper.createRegular(11));
        exportJavaItem.addActionListener(e -> {
            currentExporter = new JavaCodeExporter();
            exportCode();
        });
        
        JMenuItem exportKotlinItem = new JMenuItem("Kotlin Olarak");
        exportKotlinItem.setFont(FontHelper.createRegular(11));
        exportKotlinItem.addActionListener(e -> {
            currentExporter = new KotlinCodeExporter();
            exportCode();
        });
        
        exportMenu.add(exportJavaItem);
        exportMenu.add(exportKotlinItem);
        
        JMenuItem saveProjectItem = new JMenuItem("Projeyi Kaydet...");
        saveProjectItem.setFont(FontHelper.createRegular(11));
        saveProjectItem.addActionListener(e -> saveProject());
        
        JMenuItem openProjectItem = new JMenuItem("Proje Aç...");
        openProjectItem.setFont(FontHelper.createRegular(11));
        openProjectItem.addActionListener(e -> openProject());
        
        JMenuItem exitItem = new JMenuItem("Çıkış");
        exitItem.setFont(FontHelper.createRegular(11));
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(saveProjectItem);
        fileMenu.add(openProjectItem);
        fileMenu.addSeparator();
        fileMenu.add(exportMenu);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Edit menüsü
        JMenu editMenu = new JMenu("Düzenle");
        editMenu.setFont(FontHelper.createRegular(12));
        editMenu.setForeground(ColorPalette.TEXT_PRIMARY);
        
        JMenuItem deleteItem = new JMenuItem("Seçili Bileşeni Sil");
        deleteItem.setFont(FontHelper.createRegular(11));
        deleteItem.addActionListener(e -> designer.removeSelectedComponent());
        
        JMenuItem clearItem = new JMenuItem("Tümünü Temizle");
        clearItem.setFont(FontHelper.createRegular(11));
        clearItem.addActionListener(e -> designer.clearAll());
        
        editMenu.add(deleteItem);
        editMenu.add(clearItem);
        
        // Help menüsü
        JMenu helpMenu = new JMenu("Yardım");
        helpMenu.setFont(FontHelper.createRegular(12));
        helpMenu.setForeground(ColorPalette.TEXT_PRIMARY);
        
        JMenuItem aboutItem = new JMenuItem("Hakkında");
        aboutItem.setFont(FontHelper.createRegular(11));
        aboutItem.addActionListener(e -> showAbout());
        
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        
        return menuBar;
    }
    
    private void exportCode() {
        String className = JOptionPane.showInputDialog(
            this,
            "Sınıf adı girin:",
            "MyForm",
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (className == null || className.trim().isEmpty()) {
            return;
        }
        
        className = className.trim().replaceAll("[^a-zA-Z0-9_]", "");
        if (className.isEmpty()) {
            className = "MyForm";
        }
        
        String code = currentExporter.generateCode(className, designer.getDesignerComponents());
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle(currentExporter.getLanguageName() + " Dosyasını Kaydet");
        fileChooser.setSelectedFile(new java.io.File(className + "." + currentExporter.getFileExtension()));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            currentExporter.getLanguageName() + " Files", currentExporter.getFileExtension()
        ));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                writer.write(code);
                JOptionPane.showMessageDialog(
                    this,
                    currentExporter.getLanguageName() + " kodu başarıyla kaydedildi!",
                    "Başarılı",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(
                    this,
                    "Dosya kaydedilemedi: " + ex.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
    
    private void saveProject() {
        JOptionPane.showMessageDialog(
            this,
            "Proje kaydetme özelliği yakında eklenecek.",
            "Bilgi",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void openProject() {
        JOptionPane.showMessageDialog(
            this,
            "Proje açma özelliği yakında eklenecek.",
            "Bilgi",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void showAbout() {
        StringBuilder aboutText = new StringBuilder();
        aboutText.append("<html><body style='font-family: Segoe UI; padding: 20px;'>");
        aboutText.append("<h2 style='color: #00c8ff;'>JANTIO</h2>");
        aboutText.append("<p><b>Java GUI Builder v1.0.0</b></p>");
        aboutText.append("<p>Modern ve profesyonel Java Swing arayüzleri tasarlamak için geliştirilmiş sürükle-bırak tabanlı GUI oluşturucu.</p>");
        aboutText.append("<h3>Özellikler:</h3>");
        aboutText.append("<ul>");
        aboutText.append("<li>11 farklı Swing bileşeni desteği</li>");
        aboutText.append("<li>Sürükle-bırak ile kolay tasarım</li>");
        aboutText.append("<li>Gerçek zamanlı özellik düzenleme</li>");
        aboutText.append("<li>Java ve Kotlin koduna otomatik dönüştürme</li>");
        aboutText.append("<li>Modern karanlık tema</li>");
        aboutText.append("<li>JetBrains tarzı splash screen</li>");
        aboutText.append("</ul>");
        aboutText.append("<p style='color: #646478; margin-top: 20px;'>© 2024 Jantio Team</p>");
        aboutText.append("</body></html>");
        
        JLabel label = new JLabel(aboutText.toString());
        label.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JOptionPane.showMessageDialog(this, label, "Jantio Hakkında", JOptionPane.PLAIN_MESSAGE);
    }
    
    private void setupWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        
        // Modern görünüm
        getContentPane().setBackground(ColorPalette.BACKGROUND_LIGHT);
        
        // Pencere ikonları için (isteğe bağlı)
        try {
            // İkon dosyası varsa yüklenebilir
        } catch (Exception e) {
            // İkon yoksa devam et
        }
    }
}
