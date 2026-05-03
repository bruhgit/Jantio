package com.jantio;

import com.jantio.ui.SplashScreen;
import com.jantio.ui.MainWindow;

import javax.swing.*;

/**
 * Jantio - Java GUI Builder
 * Ana uygulama başlatıcı sınıfı
 */
public class JantioApp {
    
    public static void main(String[] args) {
        // Look and Feel ayarla
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // SwingUtilities ile thread-safe başlatma
        SwingUtilities.invokeLater(() -> {
            // Splash screen göster
            SplashScreen splash = new SplashScreen();
            splash.showSplash();
            
            // Ana pencereyi oluştur ve göster
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
            
            // Splash screen'i kapat
            splash.hideSplash();
        });
    }
}
