package com.jantio.utils;

import java.awt.*;

/**
 * Renk paleti yardımcı sınıfı - Jantio tema renkleri
 */
public class ColorPalette {
    
    // Ana tema renkleri
    public static final Color PRIMARY = new Color(0, 200, 255);
    public static final Color PRIMARY_DARK = new Color(0, 150, 200);
    public static final Color PRIMARY_LIGHT = new Color(100, 220, 255);
    
    // Arka plan renkleri
    public static final Color BACKGROUND_DARK = new Color(15, 15, 25);
    public static final Color BACKGROUND_MEDIUM = new Color(25, 25, 35);
    public static final Color BACKGROUND_LIGHT = new Color(35, 35, 45);
    public static final Color BACKGROUND_PANEL = new Color(45, 45, 55);
    public static final Color BACKGROUND_COMPONENT = new Color(50, 50, 65);
    
    // Ön plan renkleri
    public static final Color TEXT_PRIMARY = new Color(220, 220, 230);
    public static final Color TEXT_SECONDARY = new Color(180, 180, 200);
    public static final Color TEXT_MUTED = new Color(150, 150, 170);
    public static final Color TEXT_DISABLED = new Color(100, 100, 120);
    
    // Vurgu renkleri
    public static final Color ACCENT_SUCCESS = new Color(100, 200, 100);
    public static final Color ACCENT_WARNING = new Color(255, 200, 100);
    public static final Color ACCENT_ERROR = new Color(255, 100, 100);
    public static final Color ACCENT_INFO = new Color(100, 150, 255);
    
    // Border ve divider renkleri
    public static final Color BORDER = new Color(60, 60, 70);
    public static final Color BORDER_LIGHT = new Color(80, 80, 90);
    public static final Color DIVIDER = new Color(50, 50, 60);
    
    // Grid rengi
    public static final Color GRID = new Color(230, 230, 235);
    
    /**
     * Gradient oluşturur
     */
    public static GradientPaint createGradient(Color start, Color end, int x1, int y1, int x2, int y2) {
        return new GradientPaint(x1, y1, start, x2, y2, end);
    }
    
    /**
     * Primary gradient oluşturur
     */
    public static GradientPaint createPrimaryGradient(int x1, int y1, int x2, int y2) {
        return createGradient(PRIMARY, PRIMARY_DARK, x1, y1, x2, y2);
    }
    
    /**
     * Arka plan gradient'i oluşturur
     */
    public static GradientPaint createBackgroundGradient(int height) {
        return createGradient(BACKGROUND_MEDIUM, BACKGROUND_DARK, 0, 0, 0, height);
    }
}
