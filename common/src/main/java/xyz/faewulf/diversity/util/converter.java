package xyz.faewulf.diversity.util;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class converter {
    public static Holder<Enchantment> getEnchant(Level world, ResourceKey<Enchantment> enchant) {
        HolderGetter<Enchantment> registryEntryLookup = world.registryAccess()
                .asGetterLookup()
                .lookupOrThrow(Registries.ENCHANTMENT);

        return registryEntryLookup.getOrThrow(enchant);
    }

    public static Holder<Enchantment> getEnchant(Level world, String namespace, String path) {
        return world.registryAccess().registryOrThrow(Registries.ENCHANTMENT)
                .getHolder(ResourceLocation.fromNamespaceAndPath(namespace, path))
                .orElse(null);

    }

    public static String getNoteCharacter(int note) {
        return switch (note) {
            case 0 -> "F#";
            case 1 -> "G";
            case 2 -> "G#";
            case 3 -> "A";
            case 4 -> "A#";
            case 5 -> "B";
            case 6 -> "C";
            case 7 -> "C#";
            case 8 -> "D";
            case 9 -> "D#";
            case 10 -> "E";
            case 11 -> "F";
            case 12 -> "F#";
            case 13 -> "G";
            case 14 -> "G#";
            case 15 -> "A";
            case 16 -> "A#";
            case 17 -> "B";
            case 18 -> "C";
            case 19 -> "C#";
            case 20 -> "D";
            case 21 -> "D#";
            case 22 -> "E";
            case 23 -> "F";
            case 24 -> "F#";
            default -> "Unknown";
        };
    }

    public static String tick2Time(long ticks) {
        long seconds = ticks / 20;
        long minutes = seconds / 60;
        long hours = minutes / 60;

        //ensure in range 60
        seconds %= 60;
        minutes %= 60;

        if (hours > 0)
            return String.format("%02dh%02dm%02ds", hours, minutes, seconds);
        if (minutes > 0)
            return String.format("%02dm%02ds", minutes, seconds);

        return String.format("%02ds", seconds);
    }

    public static String tick2MinecraftTime(long ticks) {
        // Wrap around 24000 ticks
        long dayTime = ticks % 24000;

        // Calculate hours, minutes, and seconds
        long hours = (dayTime / 1000 + 6) % 24;  // Minecraft time starts at 6:00 AM, so add 6 hours
        long minutes = (dayTime % 1000) * 60 / 1000;
        long seconds = ((dayTime % 1000) * 60 % 1000) * 60 / 1000;

        // Format the time as HH:MM:SS
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    public static double horseJumpStrength2JumpHeight(double strength) {
        double height = 0;
        double velocity = strength;
        while (velocity > 0) {
            height += velocity;
            velocity = (velocity - .08) * .98 * .98;
        }
        return height;
    }

    public static double genericSpeed2BlockPerSecond(double speed) {
        return 42.157796 * speed;
    }

    public static String UppercaseFirstLetter(String input) {
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}
