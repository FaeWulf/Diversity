package xyz.faewulf.diversity.util.config;

import com.electronwill.nightconfig.core.file.FileConfig;
import xyz.faewulf.diversity.Constants;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Config {

    public static final String path = "config/diversity.toml";
    public static final String old_path = "config/diversity.json";

    public static final Class<?> configClass = ModConfigs.class;
    public static final String translatePath = "diversity.config.";

    private static boolean alreadyInit = false;

    public static void init() {

        //prevent calling twice and so on
        if (alreadyInit)
            return;


        ConfigLoaderFromAnnotation.initializeDefaults(configClass);
        loadFromFile(path);
        saveConfig(path);
        alreadyInit = true;
    }

    public static void save() {
        saveConfig(path);
    }

    private static void saveConfig(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {

            //category <configName, value>
            Map<String, Map<String, ConfigLoaderFromAnnotation.EntryInfo>> configMap = ConfigLoaderFromAnnotation.loadConfig(configClass);


            for (Map.Entry<String, Map<String, ConfigLoaderFromAnnotation.EntryInfo>> entry : configMap.entrySet()) {
                //group
                writer.write("[" + entry.getKey() + "]\n");

                for (Map.Entry<String, ConfigLoaderFromAnnotation.EntryInfo> subEntry : entry.getValue().entrySet()) {

                    //put value
                    writer.write('"' + subEntry.getKey() + '"' + " = " + toTomlValue(subEntry.getValue().value));

                    //put info if it has
                    if (!subEntry.getValue().info.isEmpty())
                        writer.write("\t\t\t# " + subEntry.getValue().info);

                    //breakline
                    writer.write("\n");
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String toTomlValue(Object value) {
        //string
        if (value instanceof String) {
            return "\"" + value + "\"";
        }
        //int and bool
        else if (value instanceof Boolean || value instanceof Number) {
            return value.toString();
        }
        //enum
        else if (value.getClass().isEnum()) {
            return "\"" + value.toString() + "\"";
        }
        return "";
    }

    private static void loadFromFile(String filePath) {

        if (Files.exists(Path.of(old_path))) {
            Constants.LOG.warn("Detected the old configuration file 'Diversity.json'. Starting from version 2.0.1, the mod has transitioned to using 'Diversity.toml' for configuration. Sorry for the inconvenient!");
        }

        try (FileConfig reader = FileConfig.of(filePath)) {

            //create directory if doesn't exist
            File dir = new File("config");
            if (!dir.exists())
                dir.mkdir();

            // Parse the TOML file
            reader.load();

            // Iterate through the fields in the class
            for (Field field : configClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Entry.class)) {
                    Entry entry = field.getAnnotation(Entry.class);
                    String category = entry.category();
                    String name = entry.name();

                    // Retrieve the value from the TOML file
                    Object value = reader.get(category + "." + name);

                    // Validate and assign the value
                    if (value != null && isValidType(field, value)) {
                        field.set(null, convertValue(field.getType(), value));
                    } else {
                        Constants.LOG.warn("Invalid type for config '{}' in category '{}'. Override with default value...", name, category);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper method to check if the value type matches the field's type
    private static boolean isValidType(Field field, Object value) {
        Class<?> fieldType = field.getType();
        if (fieldType.isAssignableFrom(value.getClass())) {
            return true;
        }
        if (fieldType == int.class && value instanceof Number) {
            return true;
        } else if (fieldType == boolean.class && value instanceof Boolean) {
            return true;
        } else if (fieldType.isEnum() && value instanceof String) {
            try {
                Enum.valueOf((Class<Enum>) fieldType, (String) value);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
        return false;
    }

    // Helper method to convert values to their appropriate types
    private static Object convertValue(Class<?> fieldType, Object value) {
        if (fieldType == int.class && value instanceof Number) {
            return ((Number) value).intValue();
        } else if (fieldType == boolean.class && value instanceof Boolean) {
            return value;
        } else if (fieldType.isEnum() && value instanceof String) {
            return Enum.valueOf((Class<Enum>) fieldType, (String) value);
        }
        return value;
    }
}
