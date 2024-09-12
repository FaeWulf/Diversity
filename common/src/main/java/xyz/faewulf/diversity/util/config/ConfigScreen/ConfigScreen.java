package xyz.faewulf.diversity.util.config.ConfigScreen;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.components.tabs.TabNavigationBar;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.faewulf.diversity.util.config.Config;
import xyz.faewulf.diversity.util.config.ConfigLoaderFromAnnotation;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.awt.*;
import java.util.*;
import java.util.List;

import static net.minecraft.client.gui.screens.worldselection.CreateWorldScreen.LIGHT_DIRT_BACKGROUND;
import static xyz.faewulf.diversity.util.config.Config.configClass;

public class ConfigScreen extends Screen {

    private static final String translatePath = "diversity.config.";

    //client
    private final Screen parent;
    private final Minecraft client;

    //layout vars
    private static final int RIGHT_TAB_PADDING = 2;
    private Boolean isChanged = false;

    //config variables, for checking save
    //CONFIG_VALUES is for current values of all options
    public static Map<String, Object> CONFIG_VALUES = new LinkedHashMap<>();
    public static List<ConfigLoaderFromAnnotation.EntryInfo> CONFIG_ENTRIES = new ArrayList<>();

    //main components
    private final TabManager tabManager = new TabManager(this::addRenderableWidget, this::removeWidget);

    @Nullable
    private TabNavigationBar tabNavigationBar;

    @Nullable
    private ScrollableListWidget slw;

    @Nullable
    private Tab selectedTab;

    @Nullable
    private GridLayout rightTab;

    public static GridLayout infoTab;
    public static String currentInfo;

    //part components

    @Nullable
    private EditBox searchBar;

    @Nullable
    private Button ButtonReset_Cancel;
    @Nullable
    private Button ButtonUndo;
    @Nullable
    private Button ButtonDone_Save;

    public static MultiLineTextWidget infoTab_Title;
    public static MultiLineTextWidget infoTab_Info;

    protected ConfigScreen(Screen parent) {
        super(Component.translatable(translatePath + "title"));
        this.parent = parent;
        client = Minecraft.getInstance();
    }

    public static Screen getScreen(Screen parent) {
        return new ConfigScreen(parent);
    }

    @Override
    protected void init() {

        //data
        Map<String, Map<String, ConfigLoaderFromAnnotation.EntryInfo>> configMap = ConfigLoaderFromAnnotation.loadConfig(configClass);

        //top tab bar
        TabNavigationBar.Builder tabBuilder = TabNavigationBar.builder(this.tabManager, this.width);

        configMap.forEach((s, stringEntryTypeMap) -> { //create tab for each category
            tabBuilder.addTabs(new ConfigTab(Component.literal(s), stringEntryTypeMap));
        });


        this.tabNavigationBar = tabBuilder.build();

        //right tab
        this.rightTab = new GridLayout();

        GridLayout.RowHelper rightTabRowHelper = this.rightTab.createRowHelper(2);
        rightTabRowHelper.defaultCellSetting().alignHorizontallyCenter().alignVerticallyBottom().padding(1);

        //search bar
        this.searchBar = rightTabRowHelper.addChild(new EditBox(this.font, 0, 0, 150, 20, Component.translatable("diversity.config.searchbar.hint")), 2);
        searchBar.setHint(Component.translatable("diversity.config.searchbar.hint"));
        searchBar.setResponder(this::pushList);

        //3 buttons in right tab
        ButtonReset_Cancel = rightTabRowHelper.addChild(
                Button.builder(
                                Component.translatable("diversity.config.reset"),
                                button -> {
                                    //This is cancel mode
                                    if (isChanged) {
                                        this.undoConfig();
                                        this.onClose();
                                        return;
                                    }

                                    //reset mode
                                    Map<String, Object> defaultValue = ConfigLoaderFromAnnotation.getAllDefaultValues();
                                    //by using CONFIG_ENTRIES set value
                                    //System.out.println(defaultValue);

                                    CONFIG_ENTRIES.forEach(entryInfo -> {
                                        try {
                                            entryInfo.targetField.set(null, defaultValue.get(entryInfo.name));
                                        } catch (IllegalAccessException e) {

                                            throw new RuntimeException(e);
                                        }
                                    });
                                })
                        .width(75)
                        .tooltip(Tooltip.create(Component.translatable("diversity.config.reset.tooltip")))
                        .build()
        );

        ButtonUndo = rightTabRowHelper.addChild(
                Button.builder(
                                Component.translatable("diversity.config.undo"),
                                button -> {
                                    this.undoConfig();
                                })
                        .width(75)
                        .tooltip(Tooltip.create(Component.translatable("diversity.config.undo.tooltip")))
                        .build()
        );

        //disable by default
        ButtonUndo.active = false;

        ButtonDone_Save = rightTabRowHelper.addChild(
                Button.builder(
                                Component.translatable("diversity.config.exit"),
                                button -> {
                                    if (isChanged) {
                                        Config.save();
                                        this.updateConfig();
                                    } else {
                                        undoConfig();
                                        this.onClose();
                                    }
                                })
                        .width(154)
                        .tooltip(Tooltip.create(Component.translatable("diversity.config.exit.tooltip")))
                        .build(),
                2
        );

        //Info tab (right tab)
        infoTab = new GridLayout();
        GridLayout.RowHelper infoTabRowHelper = infoTab.createRowHelper(1);
        infoTabRowHelper.defaultCellSetting().padding(4);

        infoTab_Title = infoTabRowHelper.addChild(new MultiLineTextWidget(Component.literal(""), this.font));
        infoTab_Info = infoTabRowHelper.addChild(new MultiLineTextWidget(Component.literal(""), this.font));
        infoTab_Title.setMaxWidth(154);
        infoTab_Info.setMaxWidth(154);

        //register rendering to main Screen
        ScreenRectangle screenRectangle = new ScreenRectangle(0, this.tabNavigationBar.getRectangle().bottom(), this.width - this.rightTab.getWidth(), this.height - this.tabNavigationBar.getRectangle().bottom());
        this.slw = new ScrollableListWidget(this.client, screenRectangle.width(), screenRectangle.height(), 24, screenRectangle.height(), 20);

        this.tabNavigationBar.selectTab(0, false);
        this.selectedTab = null;

        this.addRenderableWidget(slw);

        this.infoTab.visitWidgets(this::addRenderableWidget);

        rightTab.visitWidgets(abstractWidget -> {
            abstractWidget.setTabOrderGroup(1);
            this.addRenderableWidget(abstractWidget);
        });

        this.addRenderableWidget(this.tabNavigationBar);

        this.repositionElements();
    }

    @Override
    protected void repositionElements() {

        if (this.tabNavigationBar != null && this.rightTab != null && this.slw != null) {
            this.tabNavigationBar.setWidth(this.width);
            this.tabNavigationBar.arrangeElements();

            this.rightTab.arrangeElements();
            this.infoTab.arrangeElements();

            int i = this.tabNavigationBar.getRectangle().bottom();

            //this.rightTab.setPosition(this.width - this.rightTab.getWidth(), i);
            FrameLayout.centerInRectangle(this.rightTab, this.width - this.rightTab.getWidth() - RIGHT_TAB_PADDING, this.height - this.rightTab.getHeight() - RIGHT_TAB_PADDING, this.rightTab.getWidth() + RIGHT_TAB_PADDING, this.rightTab.getHeight() + RIGHT_TAB_PADDING);

            FrameLayout.alignInRectangle(this.infoTab, this.width - this.rightTab.getWidth() - RIGHT_TAB_PADDING, i, this.rightTab.getWidth() + RIGHT_TAB_PADDING, this.height - i - this.rightTab.getHeight(), 0f, 0f);

            //list, slw
            ScreenRectangle screenRectangle = new ScreenRectangle(0, i, this.width - this.rightTab.getWidth() - RIGHT_TAB_PADDING, this.height);

            //resize
            this.slw.updateSize(screenRectangle.width(), screenRectangle.height(), screenRectangle.top(), screenRectangle.height());
            this.tabManager.setTabArea(screenRectangle);
        }

    }

    @Override
    public void tick() {
        super.tick();
        this.tabManager.tickCurrent();

        if (this.searchBar != null)
            this.searchBar.tick();

        //only change when change tab
        if (this.slw != null && this.searchBar != null && this.selectedTab != this.tabManager.getCurrentTab()) {
            pushList(this.searchBar.getValue());
        }

        //checking for save
        //checking between CONFIG_VALUES and CONFIG_ENTRIES, if different then trigger save button and cancel button
        //CONFIG_VALUES is for reference only, don't change it

        Map<String, ConfigLoaderFromAnnotation.EntryInfo> current_config_data = ConfigLoaderFromAnnotation.loadConfig_EntryOnly(ModConfigs.class);

        this.isChanged = false;
        current_config_data.forEach((s, entryInfo) -> {
            try {

                //value from CONFIG_VALUES
                Object pastValue = CONFIG_VALUES.get(entryInfo.name);
                Object currentValue = entryInfo.targetField.get(null);

                if (!pastValue.equals(currentValue)) {
                    isChanged = true;
                }

            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

        });

        if (ButtonUndo != null && isChanged) {
            this.ButtonDone_Save.setMessage(Component.translatable("diversity.config.save").withStyle(ChatFormatting.GREEN));
            this.ButtonDone_Save.setTooltip(Tooltip.create(Component.translatable("diversity.config.save.tooltip")));

            this.ButtonReset_Cancel.setMessage(Component.translatable("diversity.config.cancel").withStyle(ChatFormatting.RED));
            this.ButtonReset_Cancel.setTooltip(Tooltip.create(Component.translatable("diversity.config.cancel.tooltip")));

            this.ButtonUndo.active = true;
        } else {
            this.ButtonReset_Cancel.setMessage(Component.translatable("diversity.config.reset"));
            this.ButtonReset_Cancel.setTooltip(Tooltip.create(Component.translatable("diversity.config.reset.tooltip")));

            this.ButtonDone_Save.setMessage(Component.translatable("diversity.config.exit"));
            this.ButtonDone_Save.setTooltip(Tooltip.create(Component.translatable("diversity.config.exit.tooltip")));

            this.ButtonUndo.active = false;
        }
    }

    private void updateConfig() {
        for (ConfigLoaderFromAnnotation.EntryInfo configEntry : CONFIG_ENTRIES) {
            try {
                CONFIG_VALUES.put(configEntry.name, configEntry.targetField.get(null));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void undoConfig() {
        for (ConfigLoaderFromAnnotation.EntryInfo configEntry : CONFIG_ENTRIES) {
            try {
                Object value = CONFIG_VALUES.get(configEntry.name);
                configEntry.targetField.set(null, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    //method for reset scroll list content
    private void pushList(String filter) {
        if (this.slw == null)
            return;

        this.selectedTab = this.tabManager.getCurrentTab();
        this.slw.clear();

        if (this.selectedTab instanceof ConfigTab configTab) {
            configTab.tabEntries.forEach((entryType, buttons) -> {

                if (filter == null || filter.isEmpty()) {
                    this.slw.addRow(entryType, buttons.toArray(new AbstractWidget[]{}));
                } else if (entryType.name.contains(filter.toLowerCase())) {
                    this.slw.addRow(entryType, buttons.toArray(new AbstractWidget[]{}));
                }

            });
        }

        this.slw.setScrollAmount(0);
    }

    @Override
    public void onClose() {
        if (this.client != null)
            this.client.setScreen(this.parent);
        else
            super.onClose();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int $$1, int $$2, float $$3) {
        this.renderBackground(guiGraphics);

        guiGraphics.setColor(0.125F, 0.125F, 0.125F, 1.0F);

        assert this.rightTab != null;
        assert this.tabNavigationBar != null;

        guiGraphics.blit(
                BACKGROUND_LOCATION,
                0,
                this.tabNavigationBar.getRectangle().bottom(),
                0.0F,
                0.0F,
                this.width,
                this.height - this.tabNavigationBar.getRectangle().height(),
                32,
                32
        );

        guiGraphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        super.render(guiGraphics, $$1, $$2, $$3);
    }

    @Override
    public void renderDirtBackground(GuiGraphics guiGraphics) {
        int i = 32;
        guiGraphics.blit(LIGHT_DIRT_BACKGROUND, 0, 0, 0, 0.0F, 0.0F, this.width, this.height, 32, 32);
    }
}
