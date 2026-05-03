# Jantio Pro - Ultimate Java GUI Builder

![Version](https://img.shields.io/badge/version-2.0.0--ULTIMATE-blue)
![Java](https://img.shields.io/badge/java-17+-orange)
![Kotlin](https://img.shields.io/badge/kotlin-1.9+-purple)
![License](https://img.shields.io/badge/license-MIT-green)

## 🚀 Features

### Core Features
- **Drag & Drop Design** - Intuitive visual designer
- **19+ Components** - Full Swing component library support
- **Real-time Preview** - See your design as you build
- **Code Export** - Generate clean, production-ready code

### Multi-Language Support
- **Java Swing** - Classic Java GUI code
- **Kotlin Swing** - Idiomatic Kotlin with modern syntax
- **FXML (JavaFX)** - Modern JavaFX markup

### Advanced Features
- **Right-click Resize** - Quick resize with context menu
- **Auto-overlap Prevention** - Smart component placement
- **Grid Snapping** - Precise alignment
- **Component Duplication** - Quick copy/paste
- **Property Editor** - Real-time property modification
- **Event Handler Support** - Add action listeners easily

### UI/UX
- **JetBrains-style Splash Screen** - Professional loading experience
- **Dark Theme Ready** - Modern dark interface
- **Status Bar** - Real-time feedback
- **Component Tree** - Navigate complex layouts

## 📦 Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+ (for building)

### Build from Source

```bash
cd jantio-pro
mvn clean package
```

### Run Application

```bash
# Using Maven
mvn exec:java

# Using JAR
java -jar target/jantio-pro-2.0.0-ULTIMATE.jar
```

## 🎯 Usage

### Adding Components
1. Click a component button in the toolbar
2. The component appears on the canvas
3. Drag to reposition
4. Right-click for resize options

### Exporting Code
1. Design your UI
2. Go to `File > Export Code`
3. Choose format: Java, Kotlin, or FXML
4. Copy to clipboard or save to file

### Keyboard Shortcuts
| Shortcut | Action |
|----------|--------|
| Ctrl+N | New Project |
| Ctrl+S | Save Project |
| Ctrl+O | Open Project |
| Delete | Delete Selected |
| Ctrl+D | Duplicate |
| Ctrl+P | Preview |

## 🏗️ Architecture

```
jantio-pro/
├── src/main/java/com/jantio/
│   ├── core/
│   │   ├── engine/      # JantioEngine - Core motor
│   │   └── model/       # Component models
│   ├── generators/
│   │   ├── java/        # Java code generator
│   │   └── kotlin/      # Kotlin code generator
│   ├── ui/
│   │   ├── swing/       # Swing designer & renderer
│   │   ├── javafx/      # JavaFX renderer
│   │   └── splash/      # Splash screen
│   └── JantioApplication.java
└── pom.xml
```

### Jantio Engine
The heart of Jantio, responsible for:
- Component management
- Code generation (Java/Kotlin)
- Rendering (Swing/JavaFX)
- Validation
- I/O operations

## 📝 Example Output

### Java Swing
```java
package com.example;

import javax.swing.*;
import java.awt.*;

public class MyApplication extends JFrame {
    private JButton button;
    private JLabel label;
    
    public MyApplication() {
        setTitle("MyApplication");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        initializeComponents();
    }
    
    private void initializeComponents() {
        button = new JButton();
        button.setBounds(50, 50, 100, 30);
        button.setText("Click Me");
        add(button);
        
        label = new JLabel();
        label.setBounds(50, 100, 200, 30);
        label.setText("Hello World");
        add(label);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> 
            new MyApplication().setVisible(true));
    }
}
```

### Kotlin Swing
```kotlin
package com.example

import javax.swing.*
import java.awt.*

class MyApplication : JFrame() {
    private lateinit var button: JButton
    private lateinit var label: JLabel
    
    init {
        title = "MyApplication"
        defaultCloseOperation = EXIT_ON_CLOSE
        setSize(800, 600)
        layout = null
        initializeComponents()
    }
    
    private fun initializeComponents() {
        button = JButton()
        button.setBounds(50, 50, 100, 30)
        button.text = "Click Me"
        add(button)
        
        label = JLabel()
        label.setBounds(50, 100, 200, 30)
        label.text = "Hello World"
        add(label)
    }
    
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SwingUtilities.invokeLater {
                MyApplication().isVisible = true
            }
        }
    }
}
```

## 🔧 Supported Components

### Basic
- JButton, JLabel, JTextField, JTextArea, JPasswordField

### Selection
- JCheckBox, JRadioButton, JComboBox, JList, JSpinner

### Containers
- JPanel, JScrollPane, JTabbedPane, JSplitPane, JToolBar

### Advanced
- JTable, JTree, JProgressBar, JSlider
- JColorChooser, JFileChooser

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## 📄 License

MIT License - see LICENSE file for details

## 👥 Credits

Developed by the Jantio Team © 2024

---
**Jantio Pro** - Building GUIs, One Component at a Time! 🎨
