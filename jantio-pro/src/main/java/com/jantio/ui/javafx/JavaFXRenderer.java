package com.jantio.ui.javafx;

import com.jantio.core.model.JantioComponent;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

/**
 * JavaFX Renderer - Renders Jantio components to JavaFX UI
 */
public class JavaFXRenderer {
    
    public void render(Pane parent, List<JantioComponent> components) {
        parent.getChildren().clear();
        
        for (JantioComponent comp : components) {
            if (comp.getType().isLayout()) continue;
            
            javafx.scene.Node fxNode = createJavaFXNode(comp);
            
            // Set position and size
            if (fxNode instanceof Region region) {
                region.setLayoutX(comp.getX());
                region.setLayoutY(comp.getY());
                region.setPrefWidth(comp.getWidth());
                region.setPrefHeight(comp.getHeight());
            }
            
            // Set text if applicable
            if (comp.getText() != null && !comp.getText().isEmpty()) {
                if (fxNode instanceof Labeled labeled) {
                    labeled.setText(comp.getText());
                } else if (fxNode instanceof TextInputControl textInput) {
                    textInput.setText(comp.getText());
                }
            }
            
            // Set tool tip
            if (comp.getToolTipText() != null && !comp.getToolTipText().isEmpty()) {
                Tooltip tooltip = new Tooltip(comp.getToolTipText());
                if (fxNode instanceof Control control) {
                    control.setTooltip(tooltip);
                }
            }
            
            // Set enabled/visible
            fxNode.setVisible(comp.isVisible());
            if (!comp.isEnabled() && fxNode instanceof Control control) {
                control.setDisable(true);
            }
            
            // Set colors
            if (comp.getBackgroundColor() != null) {
                if (fxNode instanceof Region region) {
                    Color fxColor = convertAwtColor(comp.getBackgroundColor());
                    region.setBackground(new Background(new BackgroundFill(fxColor, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
            if (comp.getForegroundColor() != null) {
                Color fxColor = convertAwtColor(comp.getForegroundColor());
                if (fxNode instanceof Labeled labeled) {
                    labeled.setTextFill(fxColor);
                }
            }
            
            // Set font
            if (comp.getFont() != null) {
                Font fxFont = new Font(comp.getFont().getName(), comp.getFont().getSize());
                if (fxNode instanceof Labeled labeled) {
                    labeled.setFont(fxFont);
                } else if (fxNode instanceof TextInputControl textInput) {
                    textInput.setFont(fxFont);
                }
            }
            
            parent.getChildren().add(fxNode);
        }
    }
    
    private javafx.scene.Node createJavaFXNode(JantioComponent comp) {
        return switch (comp.getType()) {
            case BUTTON -> new Button();
            case LABEL -> new Label();
            case TEXT_FIELD -> new TextField();
            case TEXT_AREA -> {
                TextArea area = new TextArea();
                area.setWrapText(true);
                yield area;
            }
            case PASSWORD_FIELD -> new PasswordField();
            case CHECK_BOX -> new CheckBox();
            case RADIO_BUTTON -> new RadioButton();
            case COMBO_BOX -> new ComboBox<>();
            case LIST -> new ListView<>();
            case SPINNER -> new Spinner<>(0, 100, 0);
            case PANEL -> new Pane();
            case SCROLL_PANE -> new ScrollPane();
            case TABBED_PANE -> new TabPane();
            case SPLIT_PANE -> new SplitPane();
            case TOOLBAR -> new ToolBar();
            case TABLE -> new TableView<>();
            case TREE -> new TreeView<>();
            case COLOR_CHOOSER -> new ColorPicker();
            case FILE_CHOOSER -> {
                VBox box = new VBox(5);
                box.getChildren().addAll(new Label("File:"), new Button("Browse"));
                yield box;
            }
            case PROGRESS_BAR -> new ProgressBar(0);
            case SLIDER -> new Slider(0, 100, 50);
            default -> new Label("Unknown");
        };
    }
    
    private Color convertAwtColor(java.awt.Color awtColor) {
        return Color.rgb(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(), awtColor.getAlpha() / 255.0);
    }
}
