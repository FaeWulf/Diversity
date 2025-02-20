package xyz.faewulf.diversity.util.config.ConfigScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.util.config.ConfigLoaderFromAnnotation;
import xyz.faewulf.diversity.util.config.ConfigScreen.Components.GroupButton;
import xyz.faewulf.diversity.util.config.ConfigScreen.Components.NumberButton;
import xyz.faewulf.diversity.util.config.ConfigScreen.Components.NumberButtonInfo;
import xyz.faewulf.diversity.util.config.ConfigScreen.Components.OptionButton;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;

import static xyz.faewulf.diversity.util.config.ConfigScreen.ConfigScreen.CONFIG_ENTRIES;
import static xyz.faewulf.diversity.util.config.ConfigScreen.ConfigScreen.CONFIG_VALUES;

public class ConfigTab implements Tab {

    public Map<ConfigLoaderFromAnnotation.EntryInfo, List<AbstractWidget>> tabEntries = new LinkedHashMap<>();

    Component Title;

    public ConfigTab(String title, Map<String, ConfigLoaderFromAnnotation.EntryInfo> entry) {

        this.Title = Component.translatable("diversity.config.category." + title);

        //creating options buttons
        ConfigLoaderFromAnnotation.getGroups(title).forEach(group -> {

            // Add GroupButton to the tab's Widget List
            GroupButton groupButton = new GroupButton(Component.literal(group));

            tabEntries.put(new ConfigLoaderFromAnnotation.EntryInfo(group), new ArrayList<>() {{
                add(groupButton);
            }});

            entry.forEach((s1, entryInfo) -> {

                // If not the same group then return
                if (!Objects.equals(entryInfo.group, group))
                    return;

                List<AbstractWidget> buttonList = new ArrayList<>();

                CONFIG_ENTRIES.add(entryInfo);
                CONFIG_VALUES.put(entryInfo.name, entryInfo.value);


                try {
                    Object ref = entryInfo.targetField.get(null);
                    // Handle for: Number
                    if (ref instanceof Number) {
                        buttonList.add(
                                new NumberButtonInfo(0, 20, Component.literal(s1), Minecraft.getInstance().font, entryInfo).alignLeft()
                        );
                        buttonList.add(
                                new NumberButton(Minecraft.getInstance().font, 0, 20, Component.literal(s1), entryInfo)
                        );
                    } else
                        // Handle for: boolean and enum
                        buttonList.add(
                                new OptionButton(20, 20, 20, 20,
                                        Component.literal(s1),
                                        button -> {
                                            //System.out.println("Button " + s1 + ": " + entryInfo.info + ", " + entryInfo.value + ", " + entryInfo.require_restart);

                                            // modconfig field
                                            Field field = entryInfo.targetField;
                                            Object value;
                                            try {
                                                value = field.get(null);

                                                if (value instanceof Boolean b) {
                                                    field.set(null, !b);
                                                }

                                                if (value instanceof Enum<?> enumValue) {
                                                    field.set(null, getNextEnumValue(enumValue));
                                                }

                                            } catch (IllegalAccessException e) {
                                                Constants.LOG.error("[Diversity] Something went wrong with the config system...");
                                                e.printStackTrace();
                                            }

                                        },
                                        entryInfo
                                ));

                } catch (IllegalAccessException e) {
                    Constants.LOG.error("[Diversity] Something went wrong with the config system...");
                    e.printStackTrace();
                }
                //CreateButton(Component.literal(s1), ));
                tabEntries.put(entryInfo, buttonList);
                groupButton.addToControlList(entryInfo);
            });
        });
    }

    public static <E extends Enum<E>> E getNextEnumValue(Enum<?> currentValue) {
        E[] enumValues = (E[]) currentValue.getDeclaringClass().getEnumConstants();  // Get all enum values of the type
        int currentIndex = currentValue.ordinal();  // Get current index
        int nextIndex = (currentIndex + 1) % enumValues.length;  // Calculate next index, wrap around if needed
        return enumValues[nextIndex];  // Return the next value
    }

    @Override
    public @NotNull Component getTabTitle() {
        return Title;
    }

    @Override
    public void visitChildren(@NotNull Consumer<AbstractWidget> consumer) {

    }

    @Override
    public void doLayout(@NotNull ScreenRectangle screenRectangle) {

    }
}

