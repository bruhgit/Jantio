package com.jantio.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * Jantio Splash Screen - JetBrains tarzı açılış ekranı
 */
public class SplashScreen extends JWindow {
    
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int PROGRESS_WIDTH = 300;
    private static final int PROGRESS_HEIGHT = 4;
    
    private JProgressBar progressBar;
    private JLabel statusLabel;
    
    public SplashScreen() {
        initComponents();
        setupWindow();
    }
    
    private void initComponents() {
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient arka plan
                Color color1 = new Color(25, 25, 35);
                Color color2 = new Color(15, 15, 25);
                GradientPaint gradient = new GradientPaint(0, 0, color1, 0, getHeight(), color2);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Dekoratif çizgiler
                g2d.setColor(new Color(100, 100, 120, 50));
                for (int i = 0; i < getHeight(); i += 30) {
                    g2d.drawLine(0, i, getWidth(), i);
                }
                
                g2d.dispose();
            }
        };
        mainPanel.setLayout(null);
        mainPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        
        // JANTIO logosu - sol üstte
        JPanel logoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                
                // "J" harfi - büyük ve renkli
                Font jFont = new Font("Segoe UI", Font.BOLD, 72);
                g2d.setFont(jFont);
                
                // J için gradient
                GradientPaint jGradient = new GradientPaint(
                    20, 20, new Color(0, 200, 255),
                    80, 80, new Color(0, 100, 200)
                );
                g2d.setPaint(jGradient);
                g2d.drawString("J", 20, 90);
                
                // "ANTIO" kısmı
                Font antioFont = new Font("Segoe UI", Font.BOLD, 48);
                g2d.setFont(antioFont);
                g2d.setColor(new Color(200, 200, 220));
                g2d.drawString("ANTIO", 75, 90);
                
                // Alt çizgi
                g2d.setColor(new Color(0, 200, 255));
                g2d.setStroke(new BasicStroke(3));
                g2d.drawLine(20, 100, 280, 100);
                
                // Tagline
                g2d.setFont(new Font("Segoe UI", Font.ITALIC, 14));
                g2d.setColor(new Color(150, 150, 170));
                g2d.drawString("Java GUI Builder", 25, 125);
                
                g2d.dispose();
            }
        };
        logoPanel.setBounds(0, 40, 300, 150);
        logoPanel.setOpaque(false);
        
        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setBounds((WIDTH - PROGRESS_WIDTH) / 2, HEIGHT - 80, PROGRESS_WIDTH, PROGRESS_HEIGHT);
        progressBar.setBorderPainted(false);
        progressBar.setForeground(new Color(0, 200, 255));
        progressBar.setBackground(new Color(50, 50, 70));
        
        // Status label
        statusLabel = new JLabel("Başlatılıyor...");
        statusLabel.setBounds((WIDTH - PROGRESS_WIDTH) / 2, HEIGHT - 50, PROGRESS_WIDTH, 20);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        statusLabel.setForeground(new Color(150, 150, 170));
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Sürüm bilgisi
        JLabel versionLabel = new JLabel("v1.0.0");
        versionLabel.setBounds(WIDTH - 60, HEIGHT - 25, 50, 20);
        versionLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        versionLabel.setForeground(new Color(100, 100, 120));
        versionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        mainPanel.add(logoPanel);
        mainPanel.add(progressBar);
        mainPanel.add(statusLabel);
        mainPanel.add(versionLabel);
        
        add(mainPanel);
    }
    
    private void setupWindow() {
        pack();
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
    }
    
    public void updateProgress(int progress, String status) {
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(progress);
            statusLabel.setText(status);
        });
        
        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void showSplash() {
        setVisible(true);
        
        // Simüle edilmiş yükleme adımları
        updateProgress(10, "Bileşenler yükleniyor...");
        updateProgress(30, "Arayüz hazırlanıyor...");
        updateProgress(50, "Eklentiler kontrol ediliyor...");
        updateProgress(70, "Tema uygulanıyor...");
        updateProgress(90, "Son hazırlıklar yapılıyor...");
        updateProgress(100, "Tamamlandı!");
        
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    public void hideSplash() {
        setVisible(false);
        dispose();
    }
}
