package com.bookify.app.utils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/**
 * Utility class for loading and managing SVG icons
 */
public class IconManager {
    private static final String ICONS_PATH = "/assets/icons/";

    /**
     * Loads an SVG icon from the assets/icons directory
     * 
     * @param iconName Name of the SVG file without extension
     * @param size     Size of the icon (both width and height)
     * @param color    Color to apply to the icon
     * @return Node containing the SVG content
     */
    public Node loadSvgIcon(String iconName, double size, Color color) {
        try {
            Path iconPath = Paths.get(getClass().getResource(ICONS_PATH + iconName + ".svg").toURI());
            String svgContent = Files.readString(iconPath);

            // Basic hack to render the SVG content
            // In a real production app, you'd use a proper SVG library
            Text iconText = new Text(svgContent);
            iconText.setStyle("-fx-font-size: " + size + "px; -fx-fill: " + toRgbCode(color) + ";");

            return iconText;
        } catch (Exception e) {
            System.err.println("Error loading icon " + iconName + ": " + e.getMessage());
            // Return a fallback icon or text
            Text fallbackText = new Text("⚠️");
            fallbackText.setStyle("-fx-font-size: " + size + "px;");
            return fallbackText;
        }
    }

    /**
     * Convert a JavaFX Color to RGB code string
     */
    private String toRgbCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    /**
     * Load all icons from the assets/icons directory and print the available ones
     * Useful for debugging
     */
    public void listAvailableIcons() {
        try {
            Path iconsDir = Paths.get(getClass().getResource(ICONS_PATH).toURI());
            Files.list(iconsDir)
                    .filter(path -> path.toString().endsWith(".svg"))
                    .forEach(path -> System.out.println("Available icon: " + path.getFileName()));
        } catch (Exception e) {
            System.err.println("Error listing icons: " + e.getMessage());
        }
    }
}