package xyz.faewulf.diversity_better_bundle.platform.services;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public interface IPlatformHelper {

    /**
     * Gets the name of the current platform
     *
     * @return The name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     *
     * @param modId The mod to check if it is loaded.
     * @return True if the mod is loaded, false otherwise.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     *
     * @return True if in a development environment, false otherwise.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Gets the name of the environment type as a string.
     *
     * @return The name of the environment type.
     */
    default String getEnvironmentName() {

        return isDevelopmentEnvironment() ? "development" : "production";
    }

    /**
     * Finds all classes in the specified package that are annotated with a specific annotation.
     *
     * @param scannedPackage The package to search in.
     * @return A list of classes annotated with the specified annotation.
     */
    default List<Class<?>> findClasses(String scannedPackage) {
        String scannedPath = scannedPackage.replace('.', '/');
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(scannedPath);
        if (scannedUrl == null) {
            throw new IllegalArgumentException(String.format("Unable to get resources from path '%s'. Are you sure the package '%s' exists?", scannedPath, scannedPackage));
        }
        File scannedDir = new File(scannedUrl.getFile());
        List<Class<?>> classes = new ArrayList<>();

        try {
            for (File file : Objects.requireNonNull(scannedDir.listFiles())) {
                classes.addAll(find(file, scannedPackage));
            }
        } catch (NullPointerException e) {
            return classes;
        }

        return classes;
    }

    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<>();
        String resource = scannedPackage + '.' + file.getName();
        if (file.isDirectory()) {
            try {
                for (File child : Objects.requireNonNull(file.listFiles())) {
                    classes.addAll(find(child, resource));
                }
            } catch (NullPointerException e) {
                return classes;
            }
        } else if (resource.endsWith(".class")) {
            int endIndex = resource.length() - ".class".length();
            String className = resource.substring(0, endIndex);
            try {
                classes.add(Class.forName(className));
            } catch (ClassNotFoundException ignore) {
            }
        }
        return classes;
    }

}