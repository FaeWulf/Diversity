package xyz.faewulf.diversity.util.config.ConfigScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import xyz.faewulf.diversity.util.config.ConfigLoaderFromAnnotation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScrollableListWidget extends ContainerObjectSelectionList<ScrollableListWidget.ListEntry> {

    private static final int SCROLLBAR_OFFSET = 7;
    private float scrollAmount;   // Current scroll amount (smooth)
    private float targetScroll;   // Target scroll position


    public ScrollableListWidget(Minecraft $$0, int $$1, int $$2, int $$3, int $$4, int $$5) {
        super($$0, $$1, $$2, $$3, $$4, $$5);

        this.scrollAmount = 0.0f;
        this.targetScroll = 0.0f;
        //this.setRenderBackground(false);
    }

    public void clear() {
        this.clearEntries();
    }

    public void addRow(ConfigLoaderFromAnnotation.EntryInfo entryInfo, AbstractWidget... widget) {
        ListEntry e = new ListEntry(entryInfo, widget);
        addEntry(e);

    }

    @Override
    protected int getScrollbarPosition() {
        return width - SCROLLBAR_OFFSET;
    }

    @Override
    public int getRowWidth() {
        return (int) Math.max(220, width * 0.85);
    }

    @Override
    public void render(GuiGraphics $$0, int $$1, int $$2, float $$3) {
        super.render($$0, $$1, $$2, $$3);
    }

    public static class ListEntry extends ContainerObjectSelectionList.Entry<ScrollableListWidget.ListEntry> {

        private final int DEFAULT_BUTTON_SIZE = 20;

        private final ArrayList<NarratableEntry> selectables = new ArrayList<>();
        private final ArrayList<AbstractWidget> elements = new ArrayList<>();
        private final ConfigLoaderFromAnnotation.EntryInfo entryInfo;
        private final DefaultButton defaultButton;

        public ListEntry(ConfigLoaderFromAnnotation.EntryInfo entryInfo, AbstractWidget... e) {
            this.entryInfo = entryInfo;
            elements.addAll(Arrays.asList(e));

            this.defaultButton = new DefaultButton(
                    20,
                    20,
                    20,
                    20,
                    Component.literal("↻"),
                    button -> {
                        //reset to default value
                        Object defaultValue = ConfigLoaderFromAnnotation.getDefaultValue(this.entryInfo.name);
                        if (defaultValue != null) {
                            try {
                                this.entryInfo.targetField.set(null, defaultValue);
                            } catch (IllegalAccessException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
            );
            this.defaultButton.active = false;
            elements.add(this.defaultButton);
        }

        @Override
        public @NotNull List<? extends NarratableEntry> narratables() {
            return selectables;
        }

        @Override
        public @NotNull List<? extends GuiEventListener> children() {
            return elements;
        }

        @Override
        public void render(GuiGraphics context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {

            for (int i = 0; i < elements.size() - 1; i++) {
                AbstractWidget abstractWidget = elements.get(i);
                int width = (entryWidth - 2 - DEFAULT_BUTTON_SIZE) / (elements.size() - 1);

                abstractWidget.setWidth(width - 2);
                abstractWidget.setX(x + i * width + 2 - SCROLLBAR_OFFSET / 2);
                abstractWidget.setY(y);
                abstractWidget.render(context, mouseX, mouseY, tickDelta);
            }

            this.defaultButton.setWidth(20);
            this.defaultButton.setX(x + entryWidth - DEFAULT_BUTTON_SIZE - 2 - SCROLLBAR_OFFSET / 2);
            this.defaultButton.setY(y);
            this.defaultButton.render(context, mouseX, mouseY, tickDelta);

            //check value
            Object value = null;

            try {
                value = this.entryInfo.targetField.get(null);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }

            Object defaultValue = ConfigLoaderFromAnnotation.getDefaultValue(this.entryInfo.name);

            this.defaultButton.active = !value.equals(defaultValue);
        }


    }

}