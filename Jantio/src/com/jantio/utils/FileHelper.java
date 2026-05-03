package com.jantio.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * Dosya işlemleri yardımcı sınıfı
 */
public class FileHelper {
    
    /**
     * Dosyayı string olarak okur
     */
    public static String readFileAsString(String filePath) throws IOException {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
        }
        return content.toString();
    }
    
    /**
     * String içeriği dosyaya yazar
     */
    public static void writeStringToFile(String filePath, String content) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.write(content);
        }
    }
    
    /**
     * Dosyanın var olup olmadığını kontrol eder
     */
    public static boolean fileExists(String filePath) {
        return new File(filePath).exists();
    }
    
    /**
     * Dosya uzantısını alır
     */
    public static String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return "";
        }
        return fileName.substring(lastDotIndex + 1);
    }
    
    /**
     * Dosya adını uzantısız alır
     */
    public static String getFileNameWithoutExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        if (lastDotIndex == -1) {
            return fileName;
        }
        return fileName.substring(0, lastDotIndex);
    }
    
    /**
     * Geçerli bir Java sınıf adı oluşturur
     */
    public static String createValidClassName(String name) {
        // Geçersiz karakterleri kaldır
        String validName = name.replaceAll("[^a-zA-Z0-9_]", "");
        
        // Rakamla başlayamaz
        if (!validName.isEmpty() && Character.isDigit(validName.charAt(0))) {
            validName = "Class" + validName;
        }
        
        // Boşsa varsayılan değer ver
        if (validName.isEmpty()) {
            validName = "MyClass";
        }
        
        return validName;
    }
    
    /**
     * Geçerli bir değişken adı oluşturur
     */
    public static String createValidVariableName(String name) {
        String validName = name.replaceAll("[^a-zA-Z0-9_]", "");
        
        // Rakamla başlayamaz
        if (!validName.isEmpty() && Character.isDigit(validName.charAt(0))) {
            validName = "var" + validName;
        }
        
        // Boşsa varsayılan değer ver
        if (validName.isEmpty()) {
            validName = "myVar";
        }
        
        // İlk harfi küçük yap (camelCase kuralı)
        if (!validName.isEmpty()) {
            validName = Character.toLowerCase(validName.charAt(0)) + validName.substring(1);
        }
        
        return validName;
    }
}
