package xyz.faewulf.diversity.util;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BannerPatterns;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class CustomBanner {
    public static ItemStack wardenBanner() {
        ItemStack itemStack = new ItemStack(Items.LIGHT_BLUE_BANNER);
        ListTag bannerTag = new BannerPattern.Builder()
                .addPattern(BannerPatterns.CURLY_BORDER, DyeColor.BLACK)
                .addPattern(BannerPatterns.CREEPER, DyeColor.LIGHT_BLUE)
                .addPattern(BannerPatterns.RHOMBUS_MIDDLE, DyeColor.CYAN)
                .addPattern(BannerPatterns.FLOWER, DyeColor.BLACK)
                .addPattern(BannerPatterns.STRIPE_TOP, DyeColor.BLACK)
                .addPattern(BannerPatterns.PIGLIN, DyeColor.BLACK)
                .addPattern(BannerPatterns.STRIPE_BOTTOM, DyeColor.BLACK)
                .toListTag();

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("Patterns", bannerTag);
        BlockItem.setBlockEntityData(itemStack, BlockEntityType.BANNER, compoundTag);
        itemStack.hideTooltipPart(ItemStack.TooltipPart.ADDITIONAL);
        itemStack.setHoverName(Component.literal("Warden Banner").withStyle(ChatFormatting.GOLD));

        return itemStack;
    }

    public static ItemStack witherBanner() {
        ItemStack itemStack = new ItemStack(Items.RED_BANNER);
        ListTag bannerTag = new BannerPattern.Builder()
                .addPattern(BannerPatterns.FLOWER, DyeColor.ORANGE)
                .addPattern(BannerPatterns.STRAIGHT_CROSS, DyeColor.BLACK)
                .addPattern(BannerPatterns.SKULL, DyeColor.WHITE)
                .addPattern(BannerPatterns.CREEPER, DyeColor.WHITE)
                .addPattern(BannerPatterns.BORDER, DyeColor.RED)
                .addPattern(BannerPatterns.CREEPER, DyeColor.BLACK)
                .addPattern(BannerPatterns.STRIPE_TOP, DyeColor.RED)
                .addPattern(BannerPatterns.SKULL, DyeColor.BLACK)
                .toListTag();

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("Patterns", bannerTag);
        BlockItem.setBlockEntityData(itemStack, BlockEntityType.BANNER, compoundTag);
        itemStack.hideTooltipPart(ItemStack.TooltipPart.ADDITIONAL);
        itemStack.setHoverName(Component.literal("Wither Banner").withStyle(ChatFormatting.GOLD));

        return itemStack;
    }

    public static ItemStack elderGuardianBanner() {
        ItemStack itemStack = new ItemStack(Items.PINK_BANNER);
        ListTag bannerTag = new BannerPattern.Builder()
                .addPattern(BannerPatterns.CIRCLE_MIDDLE, DyeColor.WHITE)
                .addPattern(BannerPatterns.HALF_HORIZONTAL, DyeColor.GRAY)
                .addPattern(BannerPatterns.STRIPE_BOTTOM, DyeColor.BLACK)
                .addPattern(BannerPatterns.STRIPE_TOP, DyeColor.LIGHT_GRAY)
                .addPattern(BannerPatterns.STRIPE_BOTTOM, DyeColor.LIGHT_GRAY)
                .addPattern(BannerPatterns.TRIANGLES_TOP, DyeColor.BLACK)
                .addPattern(BannerPatterns.TRIANGLES_BOTTOM, DyeColor.BLACK)
                .addPattern(BannerPatterns.TRIANGLES_TOP, DyeColor.BLUE)
                .addPattern(BannerPatterns.TRIANGLES_BOTTOM, DyeColor.BLUE)
                .toListTag();

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("Patterns", bannerTag);
        BlockItem.setBlockEntityData(itemStack, BlockEntityType.BANNER, compoundTag);
        itemStack.hideTooltipPart(ItemStack.TooltipPart.ADDITIONAL);
        itemStack.setHoverName(Component.literal("Elder Guardian Banner").withStyle(ChatFormatting.GOLD));

        return itemStack;
    }

    public static ItemStack enderDragonBanner() {
        ItemStack itemStack = new ItemStack(Items.BLACK_BANNER);
        ListTag bannerTag = new BannerPattern.Builder()
                .addPattern(BannerPatterns.HALF_HORIZONTAL_MIRROR, DyeColor.PURPLE)
                .addPattern(BannerPatterns.STRIPE_DOWNLEFT, DyeColor.PURPLE)
                .addPattern(BannerPatterns.CROSS, DyeColor.BLACK)
                .addPattern(BannerPatterns.STRIPE_BOTTOM, DyeColor.PURPLE)
                .addPattern(BannerPatterns.HALF_VERTICAL, DyeColor.PURPLE)
                .addPattern(BannerPatterns.BORDER, DyeColor.PURPLE)
                .addPattern(BannerPatterns.TRIANGLE_BOTTOM, DyeColor.PURPLE)
                .addPattern(BannerPatterns.SQUARE_TOP_LEFT, DyeColor.BLACK)
                .addPattern(BannerPatterns.CURLY_BORDER, DyeColor.PURPLE)
                .addPattern(BannerPatterns.SQUARE_TOP_RIGHT, DyeColor.PURPLE)
                .addPattern(BannerPatterns.STRIPE_BOTTOM, DyeColor.PURPLE)
                .addPattern(BannerPatterns.MOJANG, DyeColor.BLACK)
                .addPattern(BannerPatterns.TRIANGLES_BOTTOM, DyeColor.ORANGE)
                .addPattern(BannerPatterns.TRIANGLES_BOTTOM, DyeColor.BLACK)
                .toListTag();

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("Patterns", bannerTag);
        BlockItem.setBlockEntityData(itemStack, BlockEntityType.BANNER, compoundTag);
        itemStack.hideTooltipPart(ItemStack.TooltipPart.ADDITIONAL);
        itemStack.setHoverName(Component.literal("Ender Dragon Banner").withStyle(ChatFormatting.GOLD));


        return itemStack;
    }

    public static ItemStack enderEggBanner() {
        ItemStack itemStack = new ItemStack(Items.BLACK_BANNER);
        ListTag bannerTag = new BannerPattern.Builder()
                .addPattern(BannerPatterns.FLOWER, DyeColor.BLACK)
                .addPattern(BannerPatterns.FLOWER, DyeColor.BLACK)
                .addPattern(BannerPatterns.STRIPE_TOP, DyeColor.PURPLE)
                .addPattern(BannerPatterns.SKULL, DyeColor.BLACK)
                .addPattern(BannerPatterns.GLOBE, DyeColor.BLUE)
                .addPattern(BannerPatterns.GLOBE, DyeColor.MAGENTA)
                .addPattern(BannerPatterns.STRIPE_BOTTOM, DyeColor.BLACK)
                .addPattern(BannerPatterns.STRIPE_BOTTOM, DyeColor.GRAY)
                .addPattern(BannerPatterns.BORDER, DyeColor.PURPLE)
                .addPattern(BannerPatterns.CURLY_BORDER, DyeColor.PURPLE)
                .addPattern(BannerPatterns.CURLY_BORDER, DyeColor.PURPLE)
                .toListTag();
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("Patterns", bannerTag);
        BlockItem.setBlockEntityData(itemStack, BlockEntityType.BANNER, compoundTag);
        itemStack.hideTooltipPart(ItemStack.TooltipPart.ADDITIONAL);
        itemStack.setHoverName(Component.literal("Dragon Egg Banner").withStyle(ChatFormatting.GOLD));

        return itemStack;
    }

    public static ItemStack heroBanner() {
        ItemStack itemStack = new ItemStack(Items.WHITE_BANNER);
        ListTag bannerTag = new BannerPattern.Builder()
                .addPattern(BannerPatterns.FLOWER, DyeColor.RED)
                .addPattern(BannerPatterns.FLOWER, DyeColor.RED)
                .addPattern(BannerPatterns.STRIPE_BOTTOM, DyeColor.WHITE)
                .addPattern(BannerPatterns.HALF_HORIZONTAL, DyeColor.WHITE)
                .addPattern(BannerPatterns.RHOMBUS_MIDDLE, DyeColor.BLACK)
                .addPattern(BannerPatterns.RHOMBUS_MIDDLE, DyeColor.LIME)
                .addPattern(BannerPatterns.CIRCLE_MIDDLE, DyeColor.WHITE)
                .addPattern(BannerPatterns.CIRCLE_MIDDLE, DyeColor.LIME)
                .toListTag();

        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("Patterns", bannerTag);
        BlockItem.setBlockEntityData(itemStack, BlockEntityType.BANNER, compoundTag);
        itemStack.hideTooltipPart(ItemStack.TooltipPart.ADDITIONAL);
        itemStack.setHoverName(Component.literal("Hero of the Village Banner").withStyle(ChatFormatting.GOLD));

        return itemStack;
    }
}
