package ThemeProperties;

import JFramePlus.JavaWindowPlus;
import Utils.PathUtils;
import Utils.PropertyUtils;
import Utils.ThemeUtils;
import java.awt.Color;
import java.awt.Container;
import java.nio.file.Path;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.SwingUtilities;

public class ThemeManager {

    public static Properties getDarkThemeDefault() {
        return PropertyUtils.loadPropertiesFromResource("resources/DarkTheme.properties");
    }

    public static Properties getLightThemeDefault() {
        return PropertyUtils.loadPropertiesFromResource("resources/LightTheme.properties");
    }

    private static ThemeManager instance;
    private final Map<String, Theme> cache = new ConcurrentHashMap<>();
    private Theme theme;
    private Container mainWindow = new Container();
    private String themeFilePath = "";
    private String themeApply = "";

    private ThemeManager() {
    }

    public static synchronized ThemeManager getInstance() {
        if (instance == null) {
            instance = new ThemeManager();
        }
        return instance;
    }

    public void saveMainWindow(Container mainWindow_) {
        mainWindow = mainWindow_;
    }

    public void setThemeInMainWindow() {
        setTheme(mainWindow);
    }

    public void setThemeByToneInMainWindow() {
        setThemeByTone(mainWindow);
    }

    public void loadThemeFromFileOrDefault(String filePath, String resourcePath) {
        if (filePath.equals(themeFilePath)) {
            return;
        }
        themeFilePath = filePath;
        theme = cache.computeIfAbsent(filePath, path
                -> Theme.fromColorMap(PropertyUtils.loadPropertyMapOrDefault(path, resourcePath))
        );
    }

    public void loadThemeFromResource(String resourcePath) {
        if (resourcePath.equals(themeFilePath)) {
            return;
        }
        themeFilePath = resourcePath;
        theme = cache.computeIfAbsent(resourcePath, path
                -> Theme.fromColorMap(PropertyUtils.loadPropertyMapFromJar(path))
        );
    }

    public void loadThemeFromFilePath(String filePath) {
        if (filePath.equals(themeFilePath)) {
            return;
        }
        themeFilePath = filePath;
        theme = cache.computeIfAbsent(filePath, path
                -> Theme.fromColorMap(PropertyUtils.loadPropertyMap(path))
        );
    }

    public void loadThemeFromProperties(Properties props, String nameTheme) {
        if (nameTheme.equals(themeFilePath)) {
            return;
        }
        themeFilePath = nameTheme;
        theme = cache.computeIfAbsent(nameTheme, key
                -> Theme.fromColorMap(PropertyUtils.loadPropertyMapFromProperties(props))
        );
    }

    public void loadThemeFromColor(Color baseColor, String nameTheme) {
        if (nameTheme.equals(themeFilePath)) {
            return;
        }
        themeFilePath = nameTheme;
        theme = cache.computeIfAbsent(nameTheme, key
                -> Theme.fromColorMap(ThemeGenerator.generateHexTheme(baseColor))
        );
    }

    public void loadThemeFromColor(Color baseColor) {
        themeFilePath = "#fromColor:" + baseColor.getRGB();
        theme = Theme.fromColorMap(ThemeGenerator.generateHexTheme(baseColor));
    }

    // Métodos de aplicación de temas
    public void setTheme(Container content) {
        if (themeApply.equals(themeFilePath)) {
            return;
        }
        themeApply = themeFilePath;

        if (content instanceof JavaWindowPlus windowPlus) {
            applyWindowTheme(windowPlus);
        } else {
            applyContainerTheme(content);
        }
    }

    public void setThemeByTone(Container content) {
        if (themeApply.equals(themeFilePath)) {
            return;
        }
        themeApply = themeFilePath;

        if (content instanceof JavaWindowPlus windowPlus) {
            applyWindowThemeByTone(windowPlus);
        } else {
            applyContainerThemeByTone(content);
        }
    }

    private void applyWindowTheme(JavaWindowPlus window) {
        boolean isDarkTheme = ThemeUtils.isColorDark(theme.getColor(theme.PRIMARY));
        window.setBackgroundPlus(
                ThemeUtils.adjustColor(theme.getColor(theme.PRIMARY), 1, isDarkTheme)
        );
        window.setForegroundPlus(
                ThemeUtils.adjustColor(theme.getColor(theme.TEXT), 0.6, !isDarkTheme)
        );
        window.getContentPanelPlus().setBackground(
                ThemeUtils.safeDarken(theme.getColor(theme.SECONDARY), 0.1)
        );

        applyThemeRecursive(window.getContentPanelPlus());
    }

    private void applyContainerTheme(Container content) {
        content.setBackground(theme.getColor(theme.PRIMARY));
        content.setForeground(theme.getColor(theme.NEUTRAL));
        applyThemeRecursive(content);
    }

    private void applyWindowThemeByTone(JavaWindowPlus window) {
        boolean isDarkTheme = ThemeUtils.isColorDark(theme.getColor(theme.PRIMARY));
        window.setBackgroundPlus(
                ThemeUtils.adjustColor(theme.getColor(theme.PRIMARY), 3, !isDarkTheme)
        );
        window.setForegroundPlus(
                ThemeUtils.adjustColor(theme.getColor(theme.TEXT), 0.6, isDarkTheme)
        );
        window.getContentPanelPlus().setBackground(
                ThemeUtils.safeDarken(theme.getColor(theme.SECONDARY), 0.1)
        );

        applyThemeByToneRecursive(window.getContentPanelPlus());
    }

    private void applyContainerThemeByTone(Container content) {
        content.setBackground(theme.getColor(theme.PRIMARY));
        content.setForeground(theme.getColor(theme.NEUTRAL));
        applyThemeByToneRecursive(content);
    }

    private void applyThemeRecursive(Container container) {
        SwingUtilities.invokeLater(() -> {
            new ThemeLevel(theme).applyTheme(container, 0);
            if (container != null) {
                container.revalidate();
                container.repaint();
            }
        });
    }

    private void applyThemeByToneRecursive(Container container) {
        SwingUtilities.invokeLater(() -> {
            new ThemeTone(theme).applyThemeByTone(container);
            if (container != null) {
                container.revalidate();
                container.repaint();
            }
        });
    }
    
    public void saveTheme(Theme theme, String directoryPath, String themeName) {
    if (theme == null || directoryPath == null || themeName == null) return;
    
    String fileName = themeName.endsWith(".properties") ? themeName : themeName + ".properties";
    
    Path dirPath = PathUtils.getPath(directoryPath);
    if (!PathUtils.existsDirectory(dirPath)) {
        PathUtils.createDirectory(dirPath);
    }
    
    Path filePath = dirPath.resolve(fileName);
    if (!PathUtils.existsFile(filePath)) {
        PathUtils.createEmptyFile(filePath);
    }
    
    Properties props = new Properties();
    Map<String, Color> colorMap = theme.getColors();
    
    for (Map.Entry<String, Color> entry : colorMap.entrySet()) {
        Color color = entry.getValue();
        String hex = String.format("#%02X%02X%02X", 
            color.getRed(), 
            color.getGreen(), 
            color.getBlue()
        );
        props.setProperty(entry.getKey(), hex);
    }
    
    PropertyUtils.saveProperties(props, filePath.toString(), "Tema guardado: " + themeName);
    themeFilePath = filePath.toString();
}
}
