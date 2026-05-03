# JANTIO - Java GUI Builder

Modern ve profesyonel Java Swing arayüzleri tasarlamak için geliştirilmiş sürükle-bırak tabanlı GUI oluşturucu.

## Özellikler

- 🎨 **19 Farklı Bileşen Desteği**: Button, Label, TextField, PasswordField, FormattedTextField, TextArea, CheckBox, RadioButton, ComboBox, List, Panel, Slider, ProgressBar, Spinner, Separator, TabbedPane, ToolBar, ScrollPane, ColorChooser
- 🖱️ **Sürükle-Bırak**: Kolay ve hızlı arayüz tasarımı
- ⚙️ **Gerçek Zamanlı Özellik Düzenleme**: X, Y, genişlik, yükseklik, text, font, renk vb.
- ☕ **Java Kodu Export**: Tasarımınızı anında çalıştırılabilir Java koduna dönüştürün
- 📜 **Kotlin Kodu Export**: Tasarımınızı Kotlin koduna dönüştürün
- 🌙 **Modern Karanlık Tema**: JetBrains tarzı şık arayüz
- 🚀 **Splash Screen**: Profesyonel açılış ekranı (sol üstte JANTIO logosu)
- 🛠️ **Modüler Yapı**: Parçalara bölünmüş profesyonel kod organizasyonu
- 🎯 **Yardımcı Sınıflar**: ColorPalette, FontHelper, FileHelper ile gelişmiş özellikler
- 🏗️ **Exporter Pattern**: Genişletilebilir kod üretim mimarisi

## Proje Yapısı

```
Jantio/
├── src/
│   └── com/
│       └── jantio/
│           ├── JantioApp.java              # Ana uygulama başlatıcı
│           ├── components/
│           │   ├── ComponentType.java      # Bileşen tipleri enum
│           │   └── DesignerComponent.java  # Tasarım bileşeni sınıfı (Java/Kotlin kod üretimi)
│           ├── exporter/                   # YENİ: Kod export modülleri
│           │   ├── CodeExporter.java       # Export arayüzü
│           │   ├── JavaCodeExporter.java   # Java kod üretici
│           │   └── KotlinCodeExporter.java # Kotlin kod üretici
│           ├── ui/
│           │   ├── SplashScreen.java       # Açılış ekranı
│           │   ├── MainWindow.java         # Ana pencere
│           │   ├── JantioDesigner.java     # Tasarım yüzeyi
│           │   ├── ComponentPalette.java   # Bileşen paleti
│           │   └── PropertiesPanel.java    # Özellikler paneli
│           └── utils/
│               ├── ColorPalette.java       # Renk paleti yardımcı sınıfı
│               ├── FontHelper.java         # Font yardımcı sınıfı
│               └── FileHelper.java         # Dosya işlemleri yardımcı sınıfı
├── resources/                              # Kaynak dosyalar
├── out/
│   └── production/jantio/                  # Derlenmiş sınıflar
├── pom.xml                                 # Maven yapılandırması
├── build.sh                                # Build script (Linux/Mac)
├── jantio.jar                              # Çalıştırılabilir JAR
└── README.md                               # Bu dosya
```

## Gereksinimler

- Java 11 veya üzeri
- Maven 3.6 veya üzeri (isteğe bağlı, manuel derleme de yapılabilir)

## Kurulum

### 1. Projeyi Klonlayın

```bash
git clone <repository-url>
cd Jantio
```

### 2. Build Edin

**Manuel Derleme (Önerilen):**
```bash
mkdir -p out/production/classes
javac -d out/production/classes -sourcepath src $(find src -name "*.java")
jar cvfe jantio.jar com.jantio.JantioApp -C out/production/classes .
```

**Maven ile:**
```bash
mvn clean package
```

### 3. Çalıştırın

```bash
java -jar jantio.jar
```

veya Maven kullanıyorsanız:
```bash
java -jar target/jantio-1.0.0-jar-with-dependencies.jar
```

## Kullanım

1. **Bileşen Ekleyin**: Sol taraftaki paletten istediğiniz bileşene tıklayın
2. **Konumlandırın**: Bileşeni tasarım yüzeyinde sürükleyerek konumlandırın
3. **Özellikleri Düzenleyin**: Sağ taraftaki özellikler panelinden boyut, renk, text vb. ayarlayın
4. **Kodu Export Edin**: `Dosya > Kod Export Et` menüsünden Java veya Kotlin kodunu kaydedin

### Kotlin Desteği (YENİ!)

Jantio artık tasarımlarınızı Kotlin koduna dönüştürebilir:

1. `Dosya > Kod Export Et > Kotlin Olarak` seçeneğini tıklayın
2. Sınıf adını girin
3. `.kt` uzantılı dosyayı kaydedin
4. Oluşturulan Kotlin kodunu IntelliJ IDEA veya Android Studio'da kullanın

Örnek Kotlin çıktısı:
```kotlin
class MyForm : JFrame() {

    init {
        title = "MyForm"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 600)
        locationRelativeTo = null
        layout = null

        initComponents()
    }

    private fun initComponents() {
        val button1 = JButton("Button")
        button1.setBounds(50, 50, 120, 40)
        add(button1)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SwingUtilities.invokeLater {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                MyForm().isVisible = true
            }
        }
    }
}
```

## Ekran Görüntüleri

### Splash Screen
- Modern, karanlık tema
- "JANTIO" logosu sol üstte (JetBrains tarzı)
  - Büyük "J" harfi gradient mavi renkte
  - "ANTIO" kısmı gri tonunda
  - Altında "Java GUI Builder" tagline'ı
- İlerleme çubuğu ve durum mesajları
- Versiyon bilgisi sağ altta

### Ana Arayüz
- **Sol Panel**: Bileşen paleti (ikon + isim)
  - 11 farklı bileşen desteği
  - Hover efektli butonlar
  - Her bileşen için özel ikon
- **Orta Panel**: Izgara desenli tasarım yüzeyi
  - Sürükle-bırak desteği
  - Seçili bileşen vurgulama
  - Gerçek zamanlı konum değiştirme
- **Sağ Panel**: Özellikler düzenleyici
  - X, Y, Genişlik, Yükseklik
  - Text düzenleme
  - Font seçimi
  - Renk seçici (ön plan / arka plan)
  - Enabled/Visible checkbox'ları

## Teknik Detaylar

### Mimari
- **MVC Pattern**: Model-View-Controller yaklaşımı
- **Modüler Yapı**: Her bileşen ayrı sınıflarda
- **Event-Driven**: Mouse eventleri ile etkileşim
- **Utility Classes**: Yardımcı sınıflarla kod tekrarı önleme

### Bileşenler
| Bileşen | Sınıf | Açıklama |
|---------|-------|----------|
| Button | JButton | Tıklanabilir buton |
| Label | JLabel | Metin gösterimi |
| TextField | JTextField | Tek satır metin girişi |
| PasswordField | JPasswordField | Şifre girişi (gizli karakterler) |
| FormattedTextField | JFormattedTextField | Formatlı metin girişi |
| TextArea | JTextArea | Çok satır metin girişi |
| CheckBox | JCheckBox | İşaret kutusu |
| RadioButton | JRadioButton | Radyo buton |
| ComboBox | JComboBox | Açılır liste |
| List | JList | Liste gösterimi |
| Panel | JPanel | Konteyner panel |
| Slider | JSlider | Kaydırma çubuğu |
| ProgressBar | JProgressBar | İlerleme göstergesi |
| Spinner | JSpinner | Sayı seçici |
| Separator | JSeparator | Ayırıcı çizgi |
| TabbedPane | JTabbedPane | Sekmeli panel |
| ToolBar | JToolBar | Araç çubuğu |
| ScrollPane | JScrollPane | Kaydırılabilir panel |
| ColorChooser | JColorChooser | Renk seçici |

### Kod Üretimi

**Java Export:**
- Temiz ve okunabilir Java kodu
- Otomatik değişken isimlendirme
- SystemLookAndFeel entegrasyonu
- Main metodu ile çalıştırılabilir sınıf
- Bounds ayarları dahil

**Kotlin Export (YENİ!):**
- İdiyomatik Kotlin kodu
- `val` keyword kullanımı
- `init` bloğu ile constructor başlatma
- Companion object ile static main metodu
- Property syntax (`isEnabled = false` yerine `isEnabled = false`)
- ArrayOf ile dizi oluşturma
- Lambda syntax (`{ }` yerine `{ }`)

### Yardımcı Sınıflar

**ColorPalette**: 
- Tutarlı renk yönetimi
- Gradient oluşturma metodları
- Tema renkleri tanımları

**FontHelper**:
- Standart font oluşturma
- Font türetme metodları
- Monospace font desteği

**FileHelper**:
- Dosya okuma/yazma
- Geçerli sınıf/değişken adı oluşturma
- Dosya uzantısı işlemleri

## Gelecek Geliştirmeler

- [ ] Layout Manager desteği (BorderLayout, GridLayout, vb.)
- [ ] Bileşenler arası bağlama
- [ ] Proje kaydetme/yükleme (.jantio formatı)
- [ ] Özel bileşen oluşturma
- [ ] Template sistemleri
- [ ] Undo/Redo fonksiyonu
- [ ] Çoklu dil desteği
- [ ] Dark/Light tema değiştirme
- [ ] Bileşen hizalama araçları
- [ ] Grid snap özelliği

## Sorun Giderme

### Headless Ortam Hatası
Uygulama grafik arayüz gerektirdiği için headless (grafiksiz) ortamlarda çalışmayabilir.

**Çözüm**: Grafik ortamı olan bir sistemde çalıştırın.

### Font Bulunamadı Hatası
Segoe UI fontu bazı sistemlerde bulunmayabilir.

**Çözüm**: FontHelper.java dosyasından varsayılan fontu değiştirin.

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
