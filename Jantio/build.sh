#!/bin/bash

# Jantio Build Script

echo "==================================="
echo "  JANTIO - Java GUI Builder"
echo "  Build Script"
echo "==================================="
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "Maven bulunamadı! Lütfen Maven yükleyin."
    echo "Ubuntu/Debian: sudo apt install maven"
    echo "macOS: brew install maven"
    echo "Windows: https://maven.apache.org/download.cgi"
    exit 1
fi

echo "Maven bulundu: $(mvn --version | head -1)"
echo ""

# Clean and build
echo "Proje temizleniyor ve derleniyor..."
mvn clean package -q

if [ $? -eq 0 ]; then
    echo ""
    echo "✓ Başarıyla derlendi!"
    echo ""
    echo "Çıktı dosyası: target/jantio-1.0.0-jar-with-dependencies.jar"
    echo ""
    echo "Uygulamayı çalıştırmak için:"
    echo "  java -jar target/jantio-1.0.0-jar-with-dependencies.jar"
else
    echo ""
    echo "✗ Derleme başarısız!"
    exit 1
fi
