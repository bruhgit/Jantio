package com.jantio.ui.splash;

import javax.swing.*;
import java.awt.*;

/**
 * Jantio Splash Screen - JetBrains-style splash with JANTIO logo
 */
public class SplashScreen extends JWindow {
    private final JProgressBar progressBar;
    private final JLabel statusLabel;
    
    public SplashScreen() {
        JPanel content = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(30, 30, 30),
                    getWidth(), getHeight(), new Color(60, 60, 60));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw decorative elements
                g2d.setColor(new Color(0, 120, 215));
                g2d.fillRect(0, 0, 8, getHeight());
            }
        };
        content.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 40, 20, 40);
        
        // JANTIO Logo (top-left style like JetBrains)
        JPanel logoPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setOpaque(false);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw "JANTIO" text in JetBrains style
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 48));
                g2d.setColor(new Color(0, 120, 215));
                g2d.drawString("JANTIO", 0, 40);
                
                // Subtitle
                g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                g2d.setColor(new Color(180, 180, 180));
                g2d.drawString("Ultimate GUI Builder", 0, 60);
            }
        };
        logoPanel.setPreferredSize(new Dimension(400, 80));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        content.add(logoPanel, gbc);
        
        // Progress bar
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(false);
        progressBar.setPreferredSize(new Dimension(300, 4));
        progressBar.setForeground(new Color(0, 120, 215));
        progressBar.setBorder(null);
        
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 40, 10, 40);
        content.add(progressBar, gbc);
        
        // Status label
        statusLabel = new JLabel("Initializing...");
        statusLabel.setForeground(new Color(150, 150, 150));
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 40, 20, 40);
        content.add(statusLabel, gbc);
        
        setContentPane(content);
        setSize(500, 250);
        setLocationRelativeTo(null);
        
        // Add shadow effect
        applyShadow();
    }
    
    private void applyShadow() {
        // Shadow is handled by the window border on most systems
    }
    
    public void display() {
        setVisible(true);
        
        // Simulate loading progress
        SwingWorker<Void, Integer> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                for (int i = 0; i <= 100; i += 5) {
                    publish(i);
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
                return null;
            }
            
            @Override
            protected void process(java.util.List<Integer> chunks) {
                int value = chunks.get(chunks.size() - 1);
                progressBar.setValue(value);
                
                if (value < 30) {
                    statusLabel.setText("Loading core modules...");
                } else if (value < 60) {
                    statusLabel.setText("Initializing engine...");
                } else if (value < 80) {
                    statusLabel.setText("Preparing UI components...");
                } else if (value < 100) {
                    statusLabel.setText("Finalizing...");
                } else {
                    statusLabel.setText("Ready!");
                }
            }
        };
        
        worker.execute();
    }
    
    public void setStatus(String status) {
        SwingUtilities.invokeLater(() -> statusLabel.setText(status));
    }
}
