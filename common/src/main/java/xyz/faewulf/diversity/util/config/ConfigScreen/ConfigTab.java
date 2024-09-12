package xyz.faewulf.diversity.util.config.ConfigScreen;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.GridLayoutTab;
import net.minecraft.network.chat.Component;
import xyz.faewulf.diversity.util.config.ConfigLoaderFromAnnotation;

import java.lang.reflect.Field;
import java.util.*;

import static xyz.faewulf.diversity.util.config.ConfigScreen.ConfigScreen.CONFIG_ENTRIES;
import static xyz.faewulf.diversity.util.config.ConfigScreen.ConfigScreen.CONFIG_VALUES;

public class ConfigTab extends GridLayoutTab {

    public Map<ConfigLoaderFromAnnotation.EntryInfo, List<Button>> tabEntries = new LinkedHashMap<>();

    public ConfigTab(Component Text, Map<String, ConfigLoaderFromAnnotation.EntryInfo> entry) {
        super(Text);

        this.layout.defaultCellSetting().padding(5);

        //creating options buttons
        entry.forEach((s1, entryInfo) -> {

            List<Button> buttonList = new ArrayList<>();

            CONFIG_ENTRIES.add(entryInfo);
            CONFIG_VALUES.put(entryInfo.name, entryInfo.value);

            buttonList.add(
                    new OptionButton(20, 20, 20, 20, Component.literal(s1),
                            button -> {
                                //System.out.println("Button " + s1 + ": " + entryInfo.info + ", " + entryInfo.value + ", " + entryInfo.require_restart);

                                //modconfig field
                                Field field = entryInfo.targetField;
                                try {
                                    Object value = field.get(null);

                                    if (value instanceof Boolean b) {
                                        field.set(null, !b);
                                    }

                                    if (value instanceof Enum<?> enumValue) {
                                        field.set(null, getNextEnumValue(enumValue));
                                    }

                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }


                            },
                            entryInfo
                    ));
            //CreateButton(Component.literal(s1), ));
            tabEntries.put(entryInfo, buttonList);
        });
    }

    public static <E extends Enum<E>> E getNextEnumValue(Enum<?> currentValue) {
        E[] enumValues = (E[]) currentValue.getDeclaringClass().getEnumConstants();  // Get all enum values of the type
        int currentIndex = currentValue.ordinal();  // Get current index
        int nextIndex = (currentIndex + 1) % enumValues.length;  // Calculate next index, wrap around if needed
        return enumValues[nextIndex];  // Return the next value
    }
}

