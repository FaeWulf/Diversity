package xyz.faewulf.diversity.util.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import com.electronwill.nightconfig.core.file.FileConfig;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.util.config.ConfigLoaderFromAnnotation.EntryType;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

public class Config {

    private static final String path = "config/diversity.toml";
    private static final Class<?> configClass = ModConfigs.class;
    private static final String translatePath = "diversity.config.";

    public static void init() {
        ConfigLoaderFromAnnotation.initializeDefaults(configClass);
        loadFromFile(path);
        saveConfig(path);
    }

    public static void save() {
        saveConfig(path);
    }

    public static Screen createScreen(Screen screen) {

        //warning for restart
        Component requireRestart = Component.literal("\n\n").append(Component.translatable(translatePath + "require_restart").withStyle(ChatFormatting.GOLD));

        YetAnotherConfigLib.Builder config_builder = YetAnotherConfigLib
                .createBuilder()
                .title(Component.translatable(translatePath + "title"))
                .save(Config::save);

        Map<String, Collection<Option<?>>> configMap = new HashMap<>();

        // Iterate over all fields in the config class
        for (Field field : configClass.getDeclaredFields()) {
            if (field.isAnnotationPresent(Entry.class)) {
                Entry entry = field.getAnnotation(Entry.class);

                String category = entry.category();
                String name = entry.name();
                boolean require_restart = entry.require_restart();

                //this one get default value of the option
                Object defaultValue = ConfigLoaderFromAnnotation.getDefaultValue(name);

                // If category map doesn't exist, create it
                configMap.computeIfAbsent(category, k -> new ArrayList<>());

                //option description builder
                OptionDescription.Builder description = OptionDescription.createBuilder();

                //Text description
                Collection<Component> components = new HashSet<>();
                components.add(Component.translatable(translatePath + field.getName() + ".tooltip"));

                if (require_restart)
                    components.add(requireRestart);

                //add texts to description
                description.text(components);

                //this is option builder
                Option.Builder<?> option = null;

                //if boolean
                if (defaultValue instanceof Boolean bool) {
                    option = Option.<Boolean>createBuilder() // boolean is the type of option we'll be making
                            .name(Component.literal(name))
                            .description(description.build())
                            .binding(
                                    bool, // the default value
                                    () -> {
                                        try {
                                            return (Boolean) field.get(null);
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                            return bool;
                                        }
                                    },
                                    // a getter to get the current value from
                                    newVal -> {
                                        try {
                                            field.set(null, newVal);

                                        } catch (IllegalAccessException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                            )
                            .controller(opt -> BooleanControllerBuilder.create(opt)
                                    .formatValue(val -> val ? Component.literal("ON") : Component.literal("OFF"))
                                    .coloured(true)
                            );
                }

                //bruh for every enum
                if (defaultValue instanceof ModConfigs.weatherType weatherType) {
                    option = Option.<ModConfigs.weatherType>createBuilder() // boolean is the type of option we'll be making
                            .name(Component.literal(name))
                            .description(description.build())
                            .binding(
                                    weatherType, // the default value
                                    () -> {
                                        try {
                                            return (ModConfigs.weatherType) field.get(null);
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                            return weatherType;
                                        }
                                    },
                                    // a getter to get the current value from
                                    newVal -> {
                                        try {
                                            field.set(null, newVal);

                                        } catch (IllegalAccessException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                            )
                            .controller(opt -> EnumControllerBuilder.create(opt)
                                    .enumClass(ModConfigs.weatherType.class)
                            );
                }

                //bruh for every enum
                if (defaultValue instanceof ModConfigs.inspectType inspectType) {
                    option = Option.<ModConfigs.inspectType>createBuilder() // boolean is the type of option we'll be making
                            .name(Component.literal(name))
                            .description(description.build())
                            .binding(
                                    inspectType, // the default value
                                    () -> {
                                        try {
                                            return (ModConfigs.inspectType) field.get(null);
                                        } catch (IllegalAccessException e) {
                                            e.printStackTrace();
                                            return inspectType;
                                        }
                                    },
                                    // a getter to get the current value from
                                    newVal -> {
                                        try {
                                            field.set(null, newVal);

                                        } catch (IllegalAccessException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                            )
                            .controller(opt -> EnumControllerBuilder.create(opt)
                                    .enumClass(ModConfigs.inspectType.class)
                            );
                }


                if (option != null)
                    configMap.get(category).add(option.build());

            }
        }

        //each categories put into the config menu
        configMap.forEach((category, options) -> {
            config_builder.category(
                    ConfigCategory.createBuilder()
                            .name(Component.translatable(translatePath + "category." + category))
                            //.tooltip(Component.literal("This text will appear as a tooltip when you hover or focus the button with Tab. There is no need to add \n to wrap as YACL will do it for you."))
                            .options(options)
                            .build()
            );
        });

        return config_builder.build().generateScreen(screen);
    }

    private static void saveConfig(String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {

            //category <configName, value>
            Map<String, Map<String, EntryType>> configMap = ConfigLoaderFromAnnotation.loadConfig(configClass);


            for (Map.Entry<String, Map<String, EntryType>> entry : configMap.entrySet()) {
                //group
                writer.write("[" + entry.getKey() + "]\n");

                for (Map.Entry<String, EntryType> subEntry : entry.getValue().entrySet()) {

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
