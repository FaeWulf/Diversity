package xyz.faewulf.diversity.util.config;

import com.electronwill.nightconfig.core.file.FileConfig;
import xyz.faewulf.diversity.Constants;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;

public class Config {

    public static final String path = "config/diversity.toml";
    public static final Class<?> configClass = ModConfigs.class;
    public static final String translatePath = "diversity.config.";

    public static void init() {
        ConfigLoaderFromAnnotation.initializeDefaults(configClass);
        loadFromFile(path);
        saveConfig(path);
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
        try (FileConfig reader = FileConfig.of(filePath)) {
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
                        Constants.LOG.error("Invalid type for config '{}' in category '{}'. Override with default value...", name, category);
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
