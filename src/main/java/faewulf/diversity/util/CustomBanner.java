package faewulf.diversity.util;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.block.entity.BannerPatterns;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BannerPatternsComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.util.Unit;

public class CustomBanner {
    public static ItemStack wardenBanner(RegistryEntryLookup<BannerPattern> bannerPatternLookup) {
        ItemStack itemStack = new ItemStack(Items.LIGHT_BLUE_BANNER);
        BannerPatternsComponent bannerPatternsComponent = new BannerPatternsComponent.Builder()
                .add(bannerPatternLookup, BannerPatterns.CURLY_BORDER, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.CREEPER, DyeColor.LIGHT_BLUE)
                .add(bannerPatternLookup, BannerPatterns.RHOMBUS, DyeColor.CYAN)
                .add(bannerPatternLookup, BannerPatterns.FLOWER, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.STRIPE_TOP, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.PIGLIN, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.BLACK)
                .build();
        itemStack.set(DataComponentTypes.BANNER_PATTERNS, bannerPatternsComponent);
        itemStack.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        itemStack.set(DataComponentTypes.ITEM_NAME, Text.literal("Warden Banner").formatted(Formatting.GOLD));
        return itemStack;
    }

    public static ItemStack witherBanner(RegistryEntryLookup<BannerPattern> bannerPatternLookup) {
        ItemStack itemStack = new ItemStack(Items.RED_BANNER);
        BannerPatternsComponent bannerPatternsComponent = new BannerPatternsComponent.Builder()
                .add(bannerPatternLookup, BannerPatterns.FLOWER, DyeColor.ORANGE)
                .add(bannerPatternLookup, BannerPatterns.STRAIGHT_CROSS, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.SKULL, DyeColor.WHITE)
                .add(bannerPatternLookup, BannerPatterns.CREEPER, DyeColor.WHITE)
                .add(bannerPatternLookup, BannerPatterns.BORDER, DyeColor.RED)
                .add(bannerPatternLookup, BannerPatterns.CREEPER, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.STRIPE_TOP, DyeColor.RED)
                .add(bannerPatternLookup, BannerPatterns.SKULL, DyeColor.BLACK)
                .build();
        itemStack.set(DataComponentTypes.BANNER_PATTERNS, bannerPatternsComponent);
        itemStack.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        itemStack.set(DataComponentTypes.ITEM_NAME, Text.literal("Wither Banner").formatted(Formatting.GOLD));
        return itemStack;
    }

    public static ItemStack elderGuardianBanner(RegistryEntryLookup<BannerPattern> bannerPatternLookup) {
        ItemStack itemStack = new ItemStack(Items.PINK_BANNER);
        BannerPatternsComponent bannerPatternsComponent = new BannerPatternsComponent.Builder()
                .add(bannerPatternLookup, BannerPatterns.CIRCLE, DyeColor.WHITE)
                .add(bannerPatternLookup, BannerPatterns.HALF_HORIZONTAL, DyeColor.GRAY)
                .add(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.STRIPE_TOP, DyeColor.LIGHT_GRAY)
                .add(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.LIGHT_GRAY)
                .add(bannerPatternLookup, BannerPatterns.TRIANGLES_TOP, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.TRIANGLES_BOTTOM, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.TRIANGLES_TOP, DyeColor.BLUE)
                .add(bannerPatternLookup, BannerPatterns.TRIANGLES_BOTTOM, DyeColor.BLUE)
                .build();
        itemStack.set(DataComponentTypes.BANNER_PATTERNS, bannerPatternsComponent);
        itemStack.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        itemStack.set(DataComponentTypes.ITEM_NAME, Text.literal("Elder Guardian Banner").formatted(Formatting.GOLD));
        return itemStack;
    }

    public static ItemStack enderDragonBanner(RegistryEntryLookup<BannerPattern> bannerPatternLookup) {
        ItemStack itemStack = new ItemStack(Items.BLACK_BANNER);
        BannerPatternsComponent bannerPatternsComponent = new BannerPatternsComponent.Builder()
                .add(bannerPatternLookup, BannerPatterns.HALF_HORIZONTAL_BOTTOM, DyeColor.PURPLE)
                .add(bannerPatternLookup, BannerPatterns.STRIPE_DOWNLEFT, DyeColor.PURPLE)
                .add(bannerPatternLookup, BannerPatterns.CROSS, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.PURPLE)
                .add(bannerPatternLookup, BannerPatterns.HALF_VERTICAL, DyeColor.PURPLE)
                .add(bannerPatternLookup, BannerPatterns.BORDER, DyeColor.PURPLE)
                .add(bannerPatternLookup, BannerPatterns.TRIANGLE_BOTTOM, DyeColor.PURPLE)
                .add(bannerPatternLookup, BannerPatterns.SQUARE_TOP_LEFT, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.CURLY_BORDER, DyeColor.PURPLE)
                .add(bannerPatternLookup, BannerPatterns.SQUARE_TOP_RIGHT, DyeColor.PURPLE)
                .add(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.PURPLE)
                .add(bannerPatternLookup, BannerPatterns.MOJANG, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.TRIANGLES_BOTTOM, DyeColor.ORANGE)
                .add(bannerPatternLookup, BannerPatterns.TRIANGLES_BOTTOM, DyeColor.BLACK)
                .build();
        itemStack.set(DataComponentTypes.BANNER_PATTERNS, bannerPatternsComponent);
        itemStack.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        itemStack.set(DataComponentTypes.ITEM_NAME, Text.literal("Ender Dragon Banner").formatted(Formatting.GOLD));
        return itemStack;
    }

    public static ItemStack enderEggBanner(RegistryEntryLookup<BannerPattern> bannerPatternLookup) {
        ItemStack itemStack = new ItemStack(Items.BLACK_BANNER);
        BannerPatternsComponent bannerPatternsComponent = new BannerPatternsComponent.Builder()
                .add(bannerPatternLookup, BannerPatterns.FLOWER, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.FLOWER, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.STRIPE_TOP, DyeColor.PURPLE)
                .add(bannerPatternLookup, BannerPatterns.SKULL, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.GLOBE, DyeColor.BLUE)
                .add(bannerPatternLookup, BannerPatterns.GLOBE, DyeColor.MAGENTA)
                .add(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.GRAY)
                .add(bannerPatternLookup, BannerPatterns.BORDER, DyeColor.PURPLE)
                .add(bannerPatternLookup, BannerPatterns.CURLY_BORDER, DyeColor.PURPLE)
                .add(bannerPatternLookup, BannerPatterns.CURLY_BORDER, DyeColor.PURPLE)
                .build();
        itemStack.set(DataComponentTypes.BANNER_PATTERNS, bannerPatternsComponent);
        itemStack.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        itemStack.set(DataComponentTypes.ITEM_NAME, Text.literal("Dragon Egg Banner").formatted(Formatting.GOLD));
        return itemStack;
    }

    public static ItemStack heroBanner(RegistryEntryLookup<BannerPattern> bannerPatternLookup) {
        ItemStack itemStack = new ItemStack(Items.WHITE_BANNER);
        BannerPatternsComponent bannerPatternsComponent = new BannerPatternsComponent.Builder()
                .add(bannerPatternLookup, BannerPatterns.FLOWER, DyeColor.RED)
                .add(bannerPatternLookup, BannerPatterns.FLOWER, DyeColor.RED)
                .add(bannerPatternLookup, BannerPatterns.STRIPE_BOTTOM, DyeColor.WHITE)
                .add(bannerPatternLookup, BannerPatterns.HALF_HORIZONTAL, DyeColor.WHITE)
                .add(bannerPatternLookup, BannerPatterns.RHOMBUS, DyeColor.BLACK)
                .add(bannerPatternLookup, BannerPatterns.RHOMBUS, DyeColor.LIME)
                .add(bannerPatternLookup, BannerPatterns.CIRCLE, DyeColor.WHITE)
                .add(bannerPatternLookup, BannerPatterns.CIRCLE, DyeColor.LIME)
                .build();
        itemStack.set(DataComponentTypes.BANNER_PATTERNS, bannerPatternsComponent);
        itemStack.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
        itemStack.set(DataComponentTypes.ITEM_NAME, Text.literal("Hero of the Village Banner").formatted(Formatting.GOLD));
        return itemStack;
    }
}
