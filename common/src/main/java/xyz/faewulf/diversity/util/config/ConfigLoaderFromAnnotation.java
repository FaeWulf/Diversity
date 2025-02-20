package xyz.faewulf.diversity.util.config;

import java.lang.reflect.Field;
import java.util.*;

public class ConfigLoaderFromAnnotation {

    private static final Map<String, Object> defaultValues = new LinkedHashMap<>();
    private static final Map<String, List<String>> groupOfCategories = new HashMap<>();

    public static Map<String, Map<String, EntryInfo>> loadConfig(Class<?> configClass) {
        Map<String, Map<String, EntryInfo>> configMap = new LinkedHashMap<>();

        Field[] fields = configClass.getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));

        // Iterate over all fields in the config class
        for (Field field : fields) {
            if (field.isAnnotationPresent(Entry.class)) {
                Entry entry = field.getAnnotation(Entry.class);

                String category = entry.category();
                String name = entry.name();
                String info = entry.info();
                boolean require_restart = entry.require_restart();
                String group = entry.group();

                // Add the field's value to the map
                try {
                    Object value = field.get(null); // Access static field value

                    // If category map doesn't exist, create it
                    configMap.computeIfAbsent(category, k -> new LinkedHashMap<>());
                    configMap.get(category).put(name, new EntryInfo(field, field.getName(), name, info, value, require_restart, group));

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        return configMap;
    }

    public static Map<String, EntryInfo> loadConfig_EntryOnly(Class<?> configClass) {
        Map<String, EntryInfo> configMap = new LinkedHashMap<>();

        Field[] fields = configClass.getDeclaredFields();
        Arrays.sort(fields, Comparator.comparing(Field::getName));

        // Iterate over all fields in the config class
        for (Field field : fields) {
            if (field.isAnnotationPresent(Entry.class)) {
                Entry entry = field.getAnnotation(Entry.class);

                String name = entry.name();
                String info = entry.info();
                boolean require_restart = entry.require_restart();
                String group = entry.group();

                // Add the field's value to the map
                try {
                    Object value = field.get(null); // Access static field value

                    // If category map doesn't exist, create it
                    configMap.put(name, new EntryInfo(field, field.getName(), name, info, value, require_restart, group));

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

    public static List<String> getGroups(String category) {
        if (!groupOfCategories.containsKey(category))
            return new ArrayList<>();

        return groupOfCategories.get(category);
    }

    // Method to initialize and store default values from the config class
    public static void initializeDefaults(Class<?> configClass) {
        try {
            for (Field field : configClass.getDeclaredFields()) {
                if (field.isAnnotationPresent(Entry.class)) {
                    // Capture the field's current (default) value
                    Object defaultValue = field.get(null);


                    // Store the default value in the map
                    defaultValues.put(field.getName(), defaultValue);

                    // Store group into a category map
                    Entry entry = field.getAnnotation(Entry.class);
                    String group = entry.group();
                    String category = entry.category();

                    // If category map doesn't exist, create it
                    groupOfCategories.computeIfAbsent(category, k -> new ArrayList<>());

                    if (!groupOfCategories.get(category).contains(group))
                        groupOfCategories.get(category).add(group);
                }
            }

            groupOfCategories.forEach((s, strings) -> {
                Collections.sort(strings);
            });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static class EntryInfo {
        public String info;
        public boolean require_restart;
        public String name;
        public Field targetField;
        public String humanizeName;
        public Object value;
        public String group;

        public boolean pseudoEntry = true;
        public boolean visibleInConfig = true;

        public EntryInfo(Field field, String name, String humanizeName, String info, Object value, boolean require_restart, String group) {
            this.name = name;
            this.info = info;
            this.require_restart = require_restart;
            this.targetField = field;
            this.humanizeName = humanizeName;
            this.value = value;
            this.group = group;
            this.pseudoEntry = false;
        }

        public EntryInfo(String name) {
            this.name = name;
        }
    }
}
