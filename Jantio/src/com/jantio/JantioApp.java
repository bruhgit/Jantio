package com.jantio;

import com.jantio.ui.SplashScreen;
import com.jantio.ui.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * Jantio - Java GUI Builder
 * Ana uygulama başlatıcı sınıfı
 */
public class JantioApp {
    
    public static void main(String[] args) {
        // Headless mode kontrolü
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("Uyarı: Headless ortam tespit edildi. GUI bileşenleri devre dışı bırakılıyor.");
            System.err.println("Jantio, grafik arayüz gerektiren bir uygulamadır.");
            System.err.println("Lütfen X11 DISPLAY ortam değişkenini ayarlayın veya grafik ortamlı bir sistemde çalıştırın.");
            return;
        }
        
        // Look and Feel ayarla
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // SwingUtilities ile thread-safe başlatma
        SwingUtilities.invokeLater(() -> {
            try {
                // Splash screen göster
                SplashScreen splash = new SplashScreen();
                splash.showSplash();
                
                // Ana pencereyi oluştur ve göster
                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);
                
                // Splash screen'i kapat
                splash.hideSplash();
            } catch (HeadlessException e) {
                JOptionPane.showMessageDialog(null, 
                    "Grafik ortamı bulunamadı!\n\n" +
                    "Jantio, görsel arayüz gerektiren bir uygulamadır.\n" +
                    "Lütfen grafik destekli bir sistemde çalıştırın.",
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Uygulama başlatılırken bir hata oluştu:\n" + e.getMessage(),
                    "Hata",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}
