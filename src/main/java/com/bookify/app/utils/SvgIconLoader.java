package com.bookify.app.utils;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;

/**
 * Utility class for loading and displaying SVG icons in JavaFX
 */
public class SvgIconLoader {
    private static final Map<String, String> svgCache = new HashMap<>();
    private static final String ICONS_PATH = "/assets/icons/";

    /**
     * Load an SVG icon and convert it to a JavaFX Node
     * 
     * @param iconName Name of the SVG file without extension
     * @param size     Size of the icon (width and height)
     * @param color    Color to apply to the SVG
     * @return JavaFX Node containing the SVG graphics
     */
    public static Node loadIcon(String iconName, double size, Color color) {
        try {
            String svgPath = extractSvgPath(iconName);
            SVGPath path = new SVGPath();
            path.setContent(svgPath);
            path.setFill(color);

            Group group = new Group(path);
            // Scale the SVG to the requested size
            double originalWidth = path.getBoundsInLocal().getWidth();
            double originalHeight = path.getBoundsInLocal().getHeight();
            double scale = size / Math.max(originalWidth, originalHeight);

            group.setScaleX(scale);
            group.setScaleY(scale);

            return group;
        } catch (Exception e) {
            System.err.println("Error loading SVG icon: " + iconName + " - " + e.getMessage());
            return createFallbackIcon(size);
        }
    }

    /**
     * Extract SVG path data from the SVG file
     */
    private static String extractSvgPath(String iconName) throws Exception {
        // Check the cache first
        if (svgCache.containsKey(iconName)) {
            return svgCache.get(iconName);
        }

        // Load the SVG file
        String resource = ICONS_PATH + iconName + ".svg";
        InputStream inputStream = SvgIconLoader.class.getResourceAsStream(resource);

        if (inputStream == null) {
            throw new Exception("SVG resource not found: " + resource);
        }

        // Read the SVG content
        String svgContent;
        try (Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name())) {
            svgContent = scanner.useDelimiter("\\A").next();
        }

        // Extract the path data (this is a simplistic approach)
        int pathStart = svgContent.indexOf("d=\"");
        if (pathStart == -1) {
            throw new Exception("SVG path data not found in: " + resource);
        }

        pathStart += 3; // Skip 'd="'
        int pathEnd = svgContent.indexOf("\"", pathStart);
        if (pathEnd == -1) {
            throw new Exception("Invalid SVG path format in: " + resource);
        }

        String pathData = svgContent.substring(pathStart, pathEnd);

        // Cache the result
        svgCache.put(iconName, pathData);

        return pathData;
    }

    /**
     * Create a fallback icon when SVG loading fails
     */
    private static Node createFallbackIcon(double size) {
        SVGPath fallback = new SVGPath();
        // Simple square with exclamation mark
        fallback.setContent("M2,2 L22,2 L22,22 L2,22 Z M12,17 L12,19 L14,19 L14,17 Z M12,7 L12,14 L14,14 L14,7 Z");
        fallback.setFill(Color.RED);

        Group group = new Group(fallback);
        group.setScaleX(size / 24.0);
        group.setScaleY(size / 24.0);

        return group;
    }
}