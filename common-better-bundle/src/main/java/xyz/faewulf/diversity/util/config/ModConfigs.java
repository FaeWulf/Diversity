package xyz.faewulf.diversity.util.config;

public class ModConfigs {

    @Entry(category = "general", name = "Bundle's Enchantment", require_restart = true)
    public static boolean bundle_enchantment = true;

    @Entry(category = "item", name = "Bundle place function")
    public static boolean bundle_place_mode = true;

    @Entry(category = "recipe", name = "Bundle recipe", require_restart = true)
    public static boolean bundle_recipe = true;

}