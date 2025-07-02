package ThemeProperties;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

public class ThemeGenerator {

    public static Map<String, String> generateHexTheme(Color baseColor) {
        Map<String, String> themeMap = new HashMap<>();

        Color sanitizedPrimary = sanitizeColor(baseColor);
        boolean isDark = Utils.ThemeUtils.isColorDark(sanitizedPrimary);

        Color primary = sanitizedPrimary;
        Color secondary = generateContrastColor(primary, 15, 0.15f, isDark);
        Color tertiary = generateContrastColor(primary, -15, 0.30f, isDark).darker();

        
        if (colorsTooSimilar(primary, secondary, 0.15)) {
            secondary = generateContrastColor(primary, 30, 0.2f, isDark);
        }
        if (colorsTooSimilar(primary, tertiary, 0.15) || colorsTooSimilar(secondary, tertiary, 0.15)) {
            tertiary = generateContrastColor(primary, -30, 0.3f, isDark);
        }

        Color neutral = generateTintedGray(primary, isDark ? 0.75f : 0.92f, 0.05f);
        Color neutral2 = generateTintedGray(primary, isDark ? 0.55f : 0.78f, 0.01f);
        Color text = generateTextColor(primary, isDark);
        Color success = generateShiftedHue(primary, 120, 0.7f, isDark ? 0.75f : 0.65f);
        Color error = generateShiftedHue(primary, 0, 0.8f, isDark ? 0.8f : 0.7f);

        themeMap.put("primary", toHex(primary));
        themeMap.put("secondary", toHex(secondary));
        themeMap.put("tertiary", toHex(tertiary));
        themeMap.put("neutral", toHex(neutral));
        themeMap.put("neutral_2", toHex(neutral2));
        themeMap.put("text", toHex(text));
        themeMap.put("success", toHex(success));
        themeMap.put("error", toHex(error));

        return themeMap;
    }

    private static Color sanitizeColor(Color color) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), new float[3]);
        if (hsb[1] > 0.85f) {
            hsb[1] = 0.85f;
        }
        if (hsb[2] > 0.9f) {
            hsb[2] = 0.9f;
        } else if (hsb[2] < 0.1f) {
            hsb[2] = 0.1f;
        }
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }

    private static Color generateTextColor(Color bg, boolean isDark) {
        return isDark ? new Color(0xF0F0F0) : new Color(0x333333);
    }

    private static Color generateContrastColor(Color base, float hueShift, float saturationAdjust, boolean isDark) {
        float[] hsb = Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), new float[3]);
        float newHue = (hsb[0] + hueShift / 360f) % 1.0f;
        if (newHue < 0) newHue += 1.0f;

        float newSaturation = clampFloat(hsb[1] * (1 - saturationAdjust));
        float newBrightness = isDark ?
                clampFloat(hsb[2] * 0.85f) :
                clampFloat(Math.min(0.95f, hsb[2] * 1.15f));

        return Color.getHSBColor(newHue, newSaturation, newBrightness);
    }

    private static Color generateTintedGray(Color base, float brightness, float tintAmount) {
        float[] baseHsb = Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), new float[3]);
        return Color.getHSBColor(baseHsb[0], tintAmount, brightness);
    }

    private static Color generateShiftedHue(Color base, float shiftDegrees, float saturation, float brightness) {
        float[] hsb = Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), new float[3]);
        float newHue = (hsb[0] + (shiftDegrees / 360f)) % 1.0f;
        if (newHue < 0) newHue += 1.0f;
        return Color.getHSBColor(newHue, saturation, brightness);
    }

    private static boolean colorsTooSimilar(Color c1, Color c2, double threshold) {
        double diff = Math.sqrt(
                Math.pow(c1.getRed() - c2.getRed(), 2) +
                Math.pow(c1.getGreen() - c2.getGreen(), 2) +
                Math.pow(c1.getBlue() - c2.getBlue(), 2)
        );
        return diff < (441 * threshold);
    }

    private static float clampFloat(float val) {
        return Math.min(1.0f, Math.max(0.0f, val));
    }

    private static String toHex(Color color) {
        return String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
    }
}
