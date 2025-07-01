package Utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class PathUtils {

    private static final Path USER_HOME = Paths.get(System.getProperty("user.home"));

    public static boolean existsDirectory(Path path) {
        return Files.isDirectory(path);
    }

    public static boolean existsFile(Path path) {
        return Files.isRegularFile(path);
    }

    public static boolean existsDirectoryInHome(String relativePath) {
        return existsDirectory(getUserHomePath(relativePath));
    }

    public static List<String> missingDirectoriesInHome(String[] relativePaths) {
        List<String> missing = new ArrayList<>();
        for (String relativePath : relativePaths) {
            if (!existsDirectoryInHome(relativePath)) {
                missing.add(relativePath);
            }
        }
        return missing;
    }

    public static Path joinPath(String... parts) {
        return Paths.get("", parts);
    }

    public static String joinPathString(String... parts) {
        return Paths.get("", parts).toString();
    }

    public static String getFileSeparator() {
        return System.getProperty("file.separator");
    }

    public static boolean existsFileInHome(String relativePath) {
        return existsFile(getUserHomePath(relativePath));
    }

    public static boolean createDirectoryInUserHome(String relativePath) {
        return createDirectory(getUserHomePath(relativePath));
    }

    public static boolean createDirectory(Path path) {
        try {
            Files.createDirectories(path);
            return true;
        } catch (IOException e) {
            System.err.println("Error creando carpeta: " + e.getMessage());
            return false;
        }
    }

    public static boolean createEmptyFile(Path path) {
        try {
            if (!Files.exists(path.getParent())) {
                Files.createDirectories(path.getParent());
            }
            if (!Files.exists(path)) {
                Files.createFile(path);
                return true;
            }
            return false; // ya existía el archivo
        } catch (IOException e) {
            System.err.println("Error creando archivo: " + e.getMessage());
            return false;
        }
    }

    public static boolean copyFromResources(InputStream in, Path targetPath) {
        try {
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Error copiando archivo desde JAR: " + e.getMessage());
            return false;
        }
    }

    public static boolean copyFile(Path sourcePath, Path targetPath) {
        try {
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Error copiando archivo: " + e.getMessage());
            return false;
        }
    }

    public static void copyFiles(List<Path> sourcePaths, Path targetFolder) {
        for (Path sourcePath : sourcePaths) {
            if (Files.exists(sourcePath) && Files.isRegularFile(sourcePath)) {
                Path targetPath = targetFolder.resolve(sourcePath.getFileName());
                copyFile(sourcePath, targetPath);
            }
        }
    }

    public static void copyDirectory(Path sourceDir, Path targetDir) throws IOException {
        if (!Files.exists(targetDir)) {
            Files.createDirectories(targetDir);
        }

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceDir)) {
            for (Path entry : stream) {
                Path targetPath = targetDir.resolve(entry.getFileName());
                if (Files.isDirectory(entry)) {
                    copyDirectory(entry, targetPath);
                } else {
                    Files.copy(entry, targetPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    public static void copyFilesFromResources(String jarFolder, String[] fileNames, Path targetFolder) {
        for (String fileName : fileNames) {
            copyResourceFile(jarFolder, fileName, targetFolder);
        }
    }

    public static boolean copyResourceFile(String jarFolder, String fileName, Path targetFolder) {
        if (!jarFolder.endsWith("/")) {
            jarFolder += "/";
        }

        String resourcePath = jarFolder + fileName;
        Path targetPath = targetFolder.resolve(fileName);

        try (InputStream in = PathUtils.class.getResourceAsStream(resourcePath)) {
            if (in == null) {
                System.err.println("Archivo no encontrado en recursos: " + resourcePath);
                return false;
            }
            if (!Files.exists(targetFolder)) {
                Files.createDirectories(targetFolder);
            }
            Files.copy(in, targetPath, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            System.err.println("Error copiando archivo desde recursos: " + e.getMessage());
            return false;
        }
    }

    public static Path getUserHomePath(String relativePath) {
        return USER_HOME.resolve(relativePath);
    }

    public static List<Path> listFilesInHome(String relativePath) {
        return listFiles(getUserHomePath(relativePath));
    }

    public static List<Path> listFiles(Path path) {
        List<Path> archivos = new ArrayList<>();
        if (Files.exists(path) && Files.isDirectory(path)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                for (Path entry : stream) {
                    if (Files.isRegularFile(entry)) {
                        archivos.add(entry);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error listando archivos: " + e.getMessage());
            }
        }
        return archivos;
    }

    public static Path getPath(String path) {
        return Paths.get(path);
    }

    public static Path getPathInHome(String relativePath) {
        return getUserHomePath(relativePath);
    }
}
