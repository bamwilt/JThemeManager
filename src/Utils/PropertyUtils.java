package Utils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertyUtils {

    public static Map<String, String> loadPropertyMapOrDefault(String filePath, String resourceFallbackPath) {
        Properties props = loadPropertiesOrDefault(filePath, resourceFallbackPath);
        Map<String, String> map = new HashMap<>();

        for (String key : props.stringPropertyNames()) {
            map.put(key, props.getProperty(key));
        }

        return map;
    }

    public static Map<String, String> loadPropertyMapFromProperties(Properties props) {
        Map<String, String> map = new HashMap<>();
        for (String key : props.stringPropertyNames()) {
            map.put(key, props.getProperty(key));
        }
        return map;
    }

    public static Map<String, String> loadPropertyMap(String filePath) {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream(filePath)) {
            props.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }

        Map<String, String> map = new HashMap<>();
        for (String key : props.stringPropertyNames()) {
            map.put(key, props.getProperty(key));
        }
        return map;
    }

    public static Map<String, String> loadPropertyMapFromJar(String resourcePath) {
        if (!resourcePath.startsWith("/")) {
            resourcePath = "/" + resourcePath;
        }
        Properties props = new Properties();
        try (InputStream input = PropertyUtils.class.getResourceAsStream(resourcePath)) {
            if (input == null) {
                System.err.println("Recurso no encontrado: " + resourcePath);
                return new HashMap<>();
            }
            props.load(input);
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }

        Map<String, String> map = new HashMap<>();
        for (String key : props.stringPropertyNames()) {
            map.put(key, props.getProperty(key));
        }
        return map;
    }

    public static Properties loadPropertiesOrDefault(String filePath, String resourceFallbackPath) {
        File externalFile = new File(filePath);

        if (externalFile.exists()) {
            System.out.println("Cargando propiedades desde archivo externo: " + filePath);
            return loadProperties(filePath);
        } else {
            System.out.println("Archivo externo no encontrado, cargando desde recurso interno: " + resourceFallbackPath);
            return loadPropertiesFromResource(resourceFallbackPath);
        }
    }

    public static Properties loadProperties(String filePath) {
        Properties props = new Properties();
        try (FileInputStream input = new FileInputStream(filePath)) {
            props.load(input);
        } catch (IOException e) {
            System.err.println("Error cargando propiedades: " + e.getMessage());
        }
        return props;
    }

    public static Properties loadPropertiesFromResource(String resourcePath) {
        if (!resourcePath.startsWith("/")) {
            resourcePath = "/" + resourcePath;
        }
        Properties props = new Properties();
        try (InputStream input = PropertyUtils.class.getResourceAsStream(resourcePath)) {
            if (input == null) {
                System.err.println("No se encontró recurso: " + resourcePath);
                return props;
            }
            props.load(input);
        } catch (IOException e) {
            System.err.println("Error cargando propiedades desde recurso: " + e.getMessage());
        }
        return props;
    }

    public static boolean saveProperties(Properties props, String filePath, String comment) {
        try (FileOutputStream output = new FileOutputStream(filePath)) {
            props.store(output, comment);
            return true;
        } catch (IOException e) {
            System.err.println("Error guardando propiedades: " + e.getMessage());
            return false;
        }
    }

    public static String getProperty(String filePath, String key, String defaultValue) {
        Properties props = loadProperties(filePath);
        return props.getProperty(key, defaultValue);
    }

    public static boolean containsProperty(String filePath, String key) {
        Properties props = loadProperties(filePath);
        return props.containsKey(key);
    }

    public static boolean setProperty(String filePath, String key, String value, String comment) {
        Properties props = loadProperties(filePath);
        props.setProperty(key, value);
        return saveProperties(props, filePath, comment);
    }

    public static Properties getFilteredProperties(String filePath, String pattern, int startIndex, String placeholder) {
        Properties original = loadProperties(filePath);
        Properties filtered = new Properties();

        int missingCount = 0;
        int index = startIndex;
        int maxMissing = 3;
        while (missingCount < maxMissing) {
            String key = pattern.replace(placeholder, String.valueOf(index));
            if (original.containsKey(key)) {
                filtered.setProperty(key, original.getProperty(key));
                missingCount = 0;
            } else {
                missingCount++;
            }
            index++;
        }

        return filtered;
    }

    public static boolean removeProperty(String filePath, String key, String comment) {
        Properties props = loadProperties(filePath);
        if (props.containsKey(key)) {
            props.remove(key);
            return saveProperties(props, filePath, comment);
        }
        return false;
    }
    
    public static Properties loadPropertiesFromJar(String resourcePath) {
    if (!resourcePath.startsWith("/")) resourcePath = "/" + resourcePath;

    Properties props = new Properties();
    try (InputStream is = PropertyUtils.class.getResourceAsStream(resourcePath)) {
        if (is != null) {
            props.load(is);
        } else {
            System.err.println("No se encontró recurso en el JAR: " + resourcePath);
        }
    } catch (IOException e) {
        System.err.println("Error cargando propiedades desde el JAR: " + e.getMessage());
    }
    return props;
}
    
}
