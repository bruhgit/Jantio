package com.jantio;

import com.jantio.core.engine.JantioEngine;
import com.jantio.core.model.JantioComponent;
import com.jantio.ui.swing.SwingDesigner;

import javax.swing.*;

/**
 * Jantio Application - Main entry point
 */
public class JantioApplication {
    
    public static void main(String[] args) {
        // Check for headless environment
        if (GraphicsEnvironment.isHeadless()) {
            System.err.println("Jantio requires a graphical environment to run.");
            System.err.println("Please run this application on a system with a display.");
            System.exit(1);
        }
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                JantioEngine engine = new JantioEngine();
                SwingDesigner designer = new SwingDesigner(engine);
                designer.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error starting Jantio: " + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}
