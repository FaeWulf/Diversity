package xyz.faewulf.diversity.util.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ConfigLoaderFromAnnotation {

    private static final Map<String, Object> defaultValues = new HashMap<>();

    public static Map<String, Map<String, EntryType>> loadConfig(Class<?> configClass) {
        Map<String, Map<String, EntryType>> configMap = new HashMap<>();

        // Iterate over all fields in the config class
        for (Field field : configClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Entry.class)) {
                Entry entry = field.getAnnotation(Entry.class);

                String category = entry.category();
                String name = entry.name();
                String info = entry.info();
                boolean require_restart = entry.require_restart();

                // Add the field's value to the map
                try {
                    Object value = field.get(null); // Access static field value

                    // If category map doesn't exist, create it
                    configMap.computeIfAbsent(category, k -> new HashMap<>());
                    configMap.get(category).put(name, new EntryType(value, info, require_restart));

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return configMap;
    }

    // Method to retrieve the default value by config name
    public static Object getDefaultValue(String configName) {
        return defaultValues.get(configName);
    }

    // Method to retrieve all default values
    public static Map<String, Object> getAllDefaultValues() {
        return defaultValues;
    }

    // Method to initialize and store default values from the config class
    public static void initializeDefaults(Class<?> configClass) {
        try {
            for (Field field : configClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Entry.class)) {
                    // Capture the field's current (default) value
                    Object defaultValue = field.get(null);
                    Entry entry = field.getAnnotation(Entry.class);

                    // Store the default value in the map
                    defaultValues.put(entry.name(), defaultValue);
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static class EntryType {
        Object value;
        String info;
        boolean require_restart;

        public EntryType(Object value, String info, boolean require_restart) {
            this.value = value;
            this.info = info;
            this.require_restart = require_restart;
        }
    }
}
