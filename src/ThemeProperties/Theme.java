package ThemeProperties;

import java.awt.Color;
import java.util.LinkedHashMap;
import java.util.Map;

public class Theme {

    public static final String PRIMARY = "primary";
    public static final String SECONDARY = "secondary";
    public static final String TERTIARY = "tertiary";
    public static final String ERROR = "error";
    public static final String SUCCESS = "success";
    public static final String NEUTRAL = "neutral";
    public static final String NEUTRAL_2 = "neutral_2";
    public static final String TEXT = "text";

    private final Map<String, Color> colors = new LinkedHashMap<>();

    public Theme() {
        setColor(PRIMARY, Color.RED);
        setColor(SECONDARY, Color.RED);
        setColor(TERTIARY, Color.RED);
        setColor(ERROR, Color.RED);
        setColor(SUCCESS, Color.RED);
        setColor(NEUTRAL, Color.RED);
        setColor(NEUTRAL_2, Color.RED);
        setColor(TEXT, Color.RED);
    }

    public void setColor(String name, Color color) {
        colors.put(name, color);
    }

    public Color getColor(String name) {
        return colors.getOrDefault(name, Color.RED);
    }

    public Map<String, Color> getColors() {
        return new LinkedHashMap<>(colors);
    }

    public static Theme fromColorMap(Map<String, String> colorMap) {
        Theme theme = new Theme();
        colorMap.forEach((name, colorValue) -> theme.setColor(name, parseColor(colorValue)));
        return theme;
    }

    //=============Util
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
            System.err.println("Formato de color no reconocido: " + value);
            return Color.RED;
        }
    }

    private static Color parseHexColor(String hex) {
        if (!isHexColor(hex)) {
            System.err.println("Color hexadecimal inválido: " + hex);
            return Color.RED;
        }
        return Color.decode(hex);
    }

    private static boolean isHexColor(String hex) {
        return hex.matches("#[0-9a-fA-F]{6}") || hex.matches("#[0-9a-fA-F]{3}");
    }

    private static Color parseRGBColor(String rgb) {
        String[] parts = rgb.split(",");
        if (parts.length != 3) {
            System.err.println("Formato RGB inválido (debe tener 3 componentes): " + rgb);
            return Color.RED;
        }

        int[] values = new int[3];
        for (int i = 0; i < 3; i++) {
            String part = parts[i].trim();
            if (!isValidRGBComponent(part)) {
                System.err.println("Componente RGB inválido: '" + part + "'");
                return Color.RED;
            }
            values[i] = Integer.parseInt(part);
        }

        return new Color(values[0], values[1], values[2]);
    }

    private static boolean isValidRGBComponent(String component) {
        if (!component.matches("\\d+")) {
            return false;
        }
        int val = Integer.parseInt(component);
        return val >= 0 && val <= 255;
    }

}
