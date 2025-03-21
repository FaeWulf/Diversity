package xyz.faewulf.diversity_better_bundle.util.config;

import xyz.faewulf.diversity_better_bundle.Constants;
import xyz.faewulf.lib.util.config.Entry;
import xyz.faewulf.lib.util.config.ModConfig;


@ModConfig(mod_id = Constants.MOD_ID)
public class ModConfigs {

    @Entry(category = "general", name = "Bundle's Enchantment", require_restart = true)
    public static boolean bundle_enchantment = true;

    @Entry(category = "general", name = "Bundle place function")
    public static boolean bundle_place_mode = true;

    @Entry(category = "general", name = "EasyShulkerBoxes Compatibility Layer", require_restart = true, group = "Compatibility Layer")
    public static boolean easy_shulker_box_compat = true;

    @Entry(category = "compatibility", name = "Enchancement Compatibility Layer", require_restart = true, group = "Compatibility Layer")
    public static boolean enchancement_compat = true;
}