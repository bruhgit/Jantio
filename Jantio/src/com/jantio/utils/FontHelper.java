package com.jantio.utils;

import java.awt.*;

/**
 * Font yardımcı sınıfı - Jantio için ortak font tanımları
 */
public class FontHelper {
    
    // Ortak font aileleri
    public static final String DEFAULT_FONT = "Segoe UI";
    public static final String MONOSPACE_FONT = "Consolas";
    
    /**
     * Varsayılan font oluşturur
     */
    public static Font createDefault(int style, int size) {
        return new Font(DEFAULT_FONT, style, size);
    }
    
    /**
     * Bold font oluşturur
     */
    public static Font createBold(int size) {
        return createDefault(Font.BOLD, size);
    }
    
    /**
     * Regular font oluşturur
     */
    public static Font createRegular(int size) {
        return createDefault(Font.PLAIN, size);
    }
    
    /**
     * Italic font oluşturur
     */
    public static Font createItalic(int size) {
        return createDefault(Font.ITALIC, size);
    }
    
    /**
     * Monospace font oluşturur (kod gösterimi için)
     */
    public static Font createMonospace(int style, int size) {
        return new Font(MONOSPACE_FONT, style, size);
    }
    
    /**
     * Font boyutunu büyütür
     */
    public static Font deriveLarger(Font baseFont, int increase) {
        return baseFont.deriveFont((float)(baseFont.getSize() + increase));
    }
    
    /**
     * Font stilini değiştirir
     */
    public static Font deriveStyle(Font baseFont, int newStyle) {
        return baseFont.deriveFont(newStyle);
    }
}
