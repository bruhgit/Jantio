package com.jantio.ui;

import com.jantio.components.ComponentType;
import com.jantio.components.DesignerComponent;

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
    
    public MainWindow() {
        initComponents();
        setupWindow();
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
        
        JSplitPane mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftSplit, propertiesPanel);
        mainSplit.setDividerLocation(800);
        
        add(mainSplit, BorderLayout.CENTER);
    }
    
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(35, 35, 45));
        
        // File menüsü
        JMenu fileMenu = new JMenu("Dosya");
        fileMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        fileMenu.setForeground(new Color(200, 200, 220));
        
        JMenuItem newItem = new JMenuItem("Yeni");
        newItem.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        newItem.addActionListener(e -> designer.clearAll());
        
        JMenuItem exportItem = new JMenuItem("Kod Export Et...");
        exportItem.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        exportItem.addActionListener(e -> exportCode());
        
        JMenuItem exitItem = new JMenuItem("Çıkış");
        exitItem.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(newItem);
        fileMenu.addSeparator();
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        // Edit menüsü
        JMenu editMenu = new JMenu("Düzenle");
        editMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        editMenu.setForeground(new Color(200, 200, 220));
        
        JMenuItem deleteItem = new JMenuItem("Seçili Bileşeni Sil");
        deleteItem.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        deleteItem.addActionListener(e -> designer.removeSelectedComponent());
        
        JMenuItem clearItem = new JMenuItem("Tümünü Temizle");
        clearItem.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        clearItem.addActionListener(e -> designer.clearAll());
        
        editMenu.add(deleteItem);
        editMenu.add(clearItem);
        
        // Help menüsü
        JMenu helpMenu = new JMenu("Yardım");
        helpMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        helpMenu.setForeground(new Color(200, 200, 220));
        
        JMenuItem aboutItem = new JMenuItem("Hakkında");
        aboutItem.setFont(new Font("Segoe UI", Font.PLAIN, 11));
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
        
        String code = designer.generateCode(className);
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Java Dosyasını Kaydet");
        fileChooser.setSelectedFile(new java.io.File(className + ".java"));
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
            "Java Files", "java"
        ));
        
        int userSelection = fileChooser.showSaveDialog(this);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            try (FileWriter writer = new FileWriter(fileChooser.getSelectedFile())) {
                writer.write(code);
                JOptionPane.showMessageDialog(
                    this,
                    "Kod başarıyla kaydedildi!",
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
    
    private void showAbout() {
        StringBuilder aboutText = new StringBuilder();
        aboutText.append("<html><body style='font-family: Segoe UI; padding: 20px;'>");
        aboutText.append("<h2 style='color: #00c8ff;'>JANTIO</h2>");
        aboutText.append("<p><b>Java GUI Builder v1.0.0</b></p>");
        aboutText.append("<p>Modern ve profesyonel Java Swing arayüzleri tasarlamak için geliştirilmiş sürükle-bırak tabanlı GUI oluşturucu.</p>");
        aboutText.append("<h3>Özellikler:</h3>");
        aboutText.append("<ul>");
        aboutText.append("<li>10 farklı Swing bileşeni desteği</li>");
        aboutText.append("<li>Sürükle-bırak ile kolay tasarım</li>");
        aboutText.append("<li>Gerçek zamanlı özellik düzenleme</li>");
        aboutText.append("<li>Java koduna otomatik dönüştürme</li>");
        aboutText.append("<li>Modern karanlık tema</li>");
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
        getContentPane().setBackground(new Color(35, 35, 45));
        
        // Pencere ikonları için (isteğe bağlı)
        try {
            // İkon dosyası varsa yüklenebilir
        } catch (Exception e) {
            // İkon yoksa devam et
        }
    }
}
