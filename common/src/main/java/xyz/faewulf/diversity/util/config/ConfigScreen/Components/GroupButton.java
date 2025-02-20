package xyz.faewulf.diversity.util.config.ConfigScreen.Components;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import xyz.faewulf.diversity.util.config.ConfigLoaderFromAnnotation;
import xyz.faewulf.diversity.util.config.ConfigScreen.ConfigScreen;

import java.util.ArrayList;
import java.util.List;

public class GroupButton extends Button {

    List<ConfigLoaderFromAnnotation.EntryInfo> controlList = new ArrayList<>();
    private boolean hide = false;

    public GroupButton(Component message) {
        super(0, 0, 0, 20, message,
                button -> {
                }
                , DEFAULT_NARRATION);
    }

    public void addToControlList(ConfigLoaderFromAnnotation.EntryInfo entryInfo) {
        this.controlList.add(entryInfo);
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int i = this.active ? 16777215 : 10526880;
        this.renderString(guiGraphics, Minecraft.getInstance().font, i | Mth.ceil(this.alpha * 255.0F) << 24);

        Component indicator = Component.literal("🔽").withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN);

        if (hide)
            indicator = Component.literal("▶").withStyle(ChatFormatting.BOLD, ChatFormatting.GRAY);

        int leftTextX = this.getX() + 4;  // Left-aligned, 4 pixels from the left edge
        int textY = (int) (this.getY() + (this.height - Minecraft.getInstance().font.lineHeight) * 1.1f / 2);  // Vertically centered for text

        guiGraphics.drawString(Minecraft.getInstance().font, indicator, leftTextX, textY, 0xFFFFFF);  // Left value
    }

    @Override
    public void onPress() {
        this.controlList.forEach(entryInfo -> entryInfo.visibleInConfig = hide);
        hide = !hide;
        ConfigScreen.updateCall = true;
    }
}
