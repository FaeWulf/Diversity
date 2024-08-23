package xyz.faewulf.diversity.util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.block.entity.BannerPatterns;

public class CustomBanner {
    public static ItemStack wardenBanner(HolderGetter<BannerPattern> bannerPatternLookup) {
        ItemStack itemStack = new ItemStack(Items.LIGHT_BLUE_BANNER);
        BannerPatternLayers bannerPatternsComponent = new BannerPatternLayers.Builder()
                .addIfRegistered(bannerPatternLookup, BannerPatterns.CURLY_BORDER, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.CREEPER, DyeColor.LIGHT_BLUE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.RHOMBUS_MIDDLE, DyeColor.CYAN)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.FLOWER, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.STRIPE_TOP, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.PIGLIN, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.BLACK)
                .build();
        itemStack.set(DataComponents.BANNER_PATTERNS, bannerPatternsComponent);
        itemStack.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        itemStack.set(DataComponents.ITEM_NAME, Component.literal("Warden Banner").withStyle(ChatFormatting.GOLD));
        return itemStack;
    }

    public static ItemStack witherBanner(HolderGetter<BannerPattern> bannerPatternLookup) {
        ItemStack itemStack = new ItemStack(Items.RED_BANNER);
        BannerPatternLayers bannerPatternsComponent = new BannerPatternLayers.Builder()
                .addIfRegistered(bannerPatternLookup, BannerPatterns.FLOWER, DyeColor.ORANGE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.STRAIGHT_CROSS, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.SKULL, DyeColor.WHITE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.CREEPER, DyeColor.WHITE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.BORDER, DyeColor.RED)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.CREEPER, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.STRIPE_TOP, DyeColor.RED)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.SKULL, DyeColor.BLACK)
                .build();
        itemStack.set(DataComponents.BANNER_PATTERNS, bannerPatternsComponent);
        itemStack.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        itemStack.set(DataComponents.ITEM_NAME, Component.literal("Wither Banner").withStyle(ChatFormatting.GOLD));
        return itemStack;
    }

    public static ItemStack elderGuardianBanner(HolderGetter<BannerPattern> bannerPatternLookup) {
        ItemStack itemStack = new ItemStack(Items.PINK_BANNER);
        BannerPatternLayers bannerPatternsComponent = new BannerPatternLayers.Builder()
                .addIfRegistered(bannerPatternLookup, BannerPatterns.CIRCLE_MIDDLE, DyeColor.WHITE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.HALF_HORIZONTAL, DyeColor.GRAY)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.STRIPE_TOP, DyeColor.LIGHT_GRAY)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.LIGHT_GRAY)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.TRIANGLES_TOP, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.TRIANGLES_BOTTOM, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.TRIANGLES_TOP, DyeColor.BLUE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.TRIANGLES_BOTTOM, DyeColor.BLUE)
                .build();
        itemStack.set(DataComponents.BANNER_PATTERNS, bannerPatternsComponent);
        itemStack.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        itemStack.set(DataComponents.ITEM_NAME, Component.literal("Elder Guardian Banner").withStyle(ChatFormatting.GOLD));
        return itemStack;
    }

    public static ItemStack enderDragonBanner(HolderGetter<BannerPattern> bannerPatternLookup) {
        ItemStack itemStack = new ItemStack(Items.BLACK_BANNER);
        BannerPatternLayers bannerPatternsComponent = new BannerPatternLayers.Builder()
                .addIfRegistered(bannerPatternLookup, BannerPatterns.HALF_HORIZONTAL_MIRROR, DyeColor.PURPLE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.STRIPE_DOWNLEFT, DyeColor.PURPLE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.CROSS, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.PURPLE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.HALF_VERTICAL, DyeColor.PURPLE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.BORDER, DyeColor.PURPLE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.TRIANGLE_BOTTOM, DyeColor.PURPLE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.SQUARE_TOP_LEFT, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.CURLY_BORDER, DyeColor.PURPLE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.SQUARE_TOP_RIGHT, DyeColor.PURPLE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.PURPLE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.MOJANG, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.TRIANGLES_BOTTOM, DyeColor.ORANGE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.TRIANGLES_BOTTOM, DyeColor.BLACK)
                .build();
        itemStack.set(DataComponents.BANNER_PATTERNS, bannerPatternsComponent);
        itemStack.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        itemStack.set(DataComponents.ITEM_NAME, Component.literal("Ender Dragon Banner").withStyle(ChatFormatting.GOLD));
        return itemStack;
    }

    public static ItemStack enderEggBanner(HolderGetter<BannerPattern> bannerPatternLookup) {
        ItemStack itemStack = new ItemStack(Items.BLACK_BANNER);
        BannerPatternLayers bannerPatternsComponent = new BannerPatternLayers.Builder()
                .addIfRegistered(bannerPatternLookup, BannerPatterns.FLOWER, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.FLOWER, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.STRIPE_TOP, DyeColor.PURPLE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.SKULL, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.GLOBE, DyeColor.BLUE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.GLOBE, DyeColor.MAGENTA)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.GRAY)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.BORDER, DyeColor.PURPLE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.CURLY_BORDER, DyeColor.PURPLE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.CURLY_BORDER, DyeColor.PURPLE)
                .build();
        itemStack.set(DataComponents.BANNER_PATTERNS, bannerPatternsComponent);
        itemStack.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        itemStack.set(DataComponents.ITEM_NAME, Component.literal("Dragon Egg Banner").withStyle(ChatFormatting.GOLD));
        return itemStack;
    }

    public static ItemStack heroBanner(HolderGetter<BannerPattern> bannerPatternLookup) {
        ItemStack itemStack = new ItemStack(Items.WHITE_BANNER);
        BannerPatternLayers bannerPatternsComponent = new BannerPatternLayers.Builder()
                .addIfRegistered(bannerPatternLookup, BannerPatterns.FLOWER, DyeColor.RED)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.FLOWER, DyeColor.RED)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.WHITE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.HALF_HORIZONTAL, DyeColor.WHITE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.RHOMBUS_MIDDLE, DyeColor.BLACK)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.RHOMBUS_MIDDLE, DyeColor.LIME)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.CIRCLE_MIDDLE, DyeColor.WHITE)
                .addIfRegistered(bannerPatternLookup, BannerPatterns.CIRCLE_MIDDLE, DyeColor.LIME)
                .build();
        itemStack.set(DataComponents.BANNER_PATTERNS, bannerPatternsComponent);
        itemStack.set(DataComponents.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        itemStack.set(DataComponents.ITEM_NAME, Component.literal("Hero of the Village Banner").withStyle(ChatFormatting.GOLD));
        return itemStack;
    }
}
