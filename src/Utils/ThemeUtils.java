package Utils;

import java.awt.Color;
import java.util.Map;
import java.util.Properties;

public class ThemeUtils {

    public static Color safeDarken(Color c, double factor) {
        if (c == null) {
            return Color.RED;
        }
        int r = (int) Math.max(0, Math.min(255, c.getRed() * (1 - factor)));
        int g = (int) Math.max(0, Math.min(255, c.getGreen() * (1 - factor)));
        int b = (int) Math.max(0, Math.min(255, c.getBlue() * (1 - factor)));
        return new Color(r, g, b);
    }

    public static Color safeLighten(Color c, double factor) {
        if (c == null) {
            return Color.RED;
        }
        int r = (int) Math.max(0, Math.min(255, c.getRed() + 255 * factor));
        int g = (int) Math.max(0, Math.min(255, c.getGreen() + 255 * factor));
        int b = (int) Math.max(0, Math.min(255, c.getBlue() + 255 * factor));
        return new Color(r, g, b);
    }

    public static Color adjustColor(Color base, double depth, boolean isDarkTheme) {
        double factor = 0.08 * depth;
        return isDarkTheme ? safeLighten(base, factor) : safeDarken(base, factor);
    }

    public static boolean isColorDark(Color c) {
        if (c == null) {
            return false;
        }
        double luminance = (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue()) / 255;
        return luminance < 0.5;
    }

    public static int calculateLightness(Color c) {
        if (c == null) {
            return 0;
        }
        double luminance = (0.299 * c.getRed() + 0.587 * c.getGreen() + 0.114 * c.getBlue()) / 2.55;
        return (int) Math.round(luminance);
    }

    public static Color toneToColor(int tone, Color low, Color mid, Color high) {
        if (low == null) {
            low = Color.RED;
        }
        if (mid == null) {
            mid = Color.RED;
        }
        if (high == null) {
            high = Color.RED;
        }
        return tone < 33 ? low : tone < 66 ? mid : high;
    }

    public static String colorToHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }

    public static Color parseColor(String value) {
        if (value == null) {
            return Color.RED;
        }

        value = value.trim();

        if (value.startsWith("#")) {
            return parseHexColor(value);
        } else if (value.contains(",")) {
            return parseRGBColor(value);
        } else {
            return Color.RED;
        }
    }

    private static Color parseHexColor(String hex) {
        try {
            return Color.decode(hex);
        } catch (NumberFormatException e) {
            return Color.RED;
        }
    }

    private static Color parseRGBColor(String rgb) {
        String[] parts = rgb.split(",");
        if (parts.length != 3) {
            return Color.RED;
        }

        try {
            int r = Integer.parseInt(parts[0].trim());
            int g = Integer.parseInt(parts[1].trim());
            int b = Integer.parseInt(parts[2].trim());
            return new Color(r, g, b);
        } catch (NumberFormatException e) {
            return Color.RED;
        }
    }

    public static Properties colorsToProperties(Map<String, Color> colorMap) {
        Properties props = new Properties();
        colorMap.forEach((key, color)
                -> props.setProperty(key, colorToHex(color))
        );
        return props;
    }

    public static boolean areColorsSimilar(Color c1, Color c2, double threshold) {
        double diff = Math.sqrt(
                Math.pow(c1.getRed() - c2.getRed(), 2)
                + Math.pow(c1.getGreen() - c2.getGreen(), 2)
                + Math.pow(c1.getBlue() - c2.getBlue(), 2)
        );
        return diff < (441 * threshold);
    }

    public static Color darken(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                null
        );
        return Color.getHSBColor(
                hsb[0],
                hsb[1],
                Math.max(0, hsb[2] - factor)
        );
    }

    public static Color lighten(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                null
        );
        return Color.getHSBColor(
                hsb[0],
                hsb[1],
                Math.min(1, hsb[2] + factor)
        );
    }
}
