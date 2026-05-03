# JANTIO - Java GUI Builder

Modern ve profesyonel Java Swing arayüzleri tasarlamak için geliştirilmiş sürükle-bırak tabanlı GUI oluşturucu.

## Özellikler

- 🎨 **10 Farklı Bileşen Desteği**: Button, Label, TextField, TextArea, CheckBox, RadioButton, ComboBox, Panel, Slider, ProgressBar, Spinner
- 🖱️ **Sürükle-Bırak**: Kolay ve hızlı arayüz tasarımı
- ⚙️ **Gerçek Zamanlı Özellik Düzenleme**: X, Y, genişlik, yükseklik, text, font, renk vb.
- 📝 **Java Kodu Export**: Tasarımınızı anında çalıştırılabilir Java koduna dönüştürün
- 🌙 **Modern Karanlık Tema**: JetBrains tarzı şık arayüz
- 🚀 **Splash Screen**: Profesyonel açılış ekranı

## Proje Yapısı

```
Jantio/
├── src/
│   └── com/
│       └── jantio/
│           ├── JantioApp.java          # Ana uygulama başlatıcı
│           ├── components/
│           │   ├── ComponentType.java  # Bileşen tipleri enum
│           │   └── DesignerComponent.java  # Tasarım bileşeni sınıfı
│           └── ui/
│               ├── SplashScreen.java   # Açılış ekranı
│               ├── MainWindow.java     # Ana pencere
│               ├── JantioDesigner.java # Tasarım yüzeyi
│               ├── ComponentPalette.java # Bileşen paleti
│               └── PropertiesPanel.java # Özellikler paneli
├── resources/                          # Kaynak dosyalar
├── pom.xml                            # Maven yapılandırması
├── build.sh                           # Build script (Linux/Mac)
└── README.md                          # Bu dosya
```

## Gereksinimler

- Java 11 veya üzeri
- Maven 3.6 veya üzeri

## Kurulum

### 1. Projeyi Klonlayın

```bash
git clone <repository-url>
cd Jantio
```

### 2. Build Edin

**Linux/Mac:**
```bash
./build.sh
```

**Windows:**
```cmd
mvn clean package
```

### 3. Çalıştırın

```bash
java -jar target/jantio-1.0.0-jar-with-dependencies.jar
```

## Kullanım

1. **Bileşen Ekleyin**: Sol taraftaki paletten istediğiniz bileşene tıklayın
2. **Konumlandırın**: Bileşeni tasarım yüzeyinde sürükleyerek konumlandırın
3. **Özellikleri Düzenleyin**: Sağ taraftaki özellikler panelinden boyut, renk, text vb. ayarlayın
4. **Kodu Export Edin**: `Dosya > Kod Export Et` menüsünden Java kodunu kaydedin

## Ekran Görüntüleri

### Splash Screen
- Modern, karanlık tema
- "JANTIO" logosu sol üstte (JetBrains tarzı)
- İlerleme çubuğu ve durum mesajları

### Ana Arayüz
- **Sol Panel**: Bileşen paleti (ikon + isim)
- **Orta Panel**: Izgara desenli tasarım yüzeyi
- **Sağ Panel**: Özellikler düzenleyici

## Teknik Detaylar

### Mimari
- **MVC Pattern**: Model-View-Controller yaklaşımı
- **Modüler Yapı**: Her bileşen ayrı sınıflarda
- **Event-Driven**: Mouse eventleri ile etkileşim

### Kod Üretimi
- Temiz ve okunabilir Java kodu
- Otomatik değişken isimlendirme
- SystemLookAndFeel entegrasyonu
- Main metodu ile çalıştırılabilir sınıf

## Gelecek Geliştirmeler

- [ ] Layout Manager desteği (BorderLayout, GridLayout, vb.)
- [ ] Bileşenler arası bağlama
- [ ] Proje kaydetme/yükleme
- [ ] Özel bileşen oluşturma
- [ ] Template sistemleri
- [ ] Undo/Redo fonksiyonu

## Katkıda Bulunma

1. Fork edin
2. Feature branch oluşturun (`git checkout -b feature/amazing-feature`)
3. Commit yapın (`git commit -m 'Add amazing feature'`)
4. Push edin (`git push origin feature/amazing-feature`)
5. Pull Request açın

## Lisans

Bu proje MIT lisansı altında lisanslanmıştır.

## İletişim

Jantio Team - 2024

---

**Not**: Uygulama grafik arayüz gerektirdiği için headless (grafiksiz) ortamlarda çalışmayabilir.
