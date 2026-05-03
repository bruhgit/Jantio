package com.jantio.exporter;

import com.jantio.components.DesignerComponent;

import java.util.List;

/**
 * Kod Export Arayüzü - Java ve Kotlin kodu üretimi için ortak arayüz
 */
public interface CodeExporter {
    String generateCode(String className, List<DesignerComponent> components);
    String getFileExtension();
    String getLanguageName();
}
