package xyz.faewulf.diversity.util.config;

import xyz.faewulf.diversity.Constants;
import xyz.faewulf.lib.util.config.Entry;
import xyz.faewulf.lib.util.config.ModConfig;


@ModConfig(mod_id = Constants.MOD_ID)
public class ModConfigs {

    @Entry(category = "general", name = "Bigger radius bookshelf for enchanting table", require_restart = true, group = "Enchantments")
    public static boolean bigger_radius_bookshelf_for_enchantingTable = true;

    @Entry(category = "general", name = "1 tick delay copper bulb")
    public static boolean copper_bulb_tick_delay = false;

    @Entry(category = "general", name = "Backup Enchantments", require_restart = true, group = "Enchantments")
    public static boolean more_enchantment = true;

    @Entry(category = "general", name = "Bundle's Enchantment", require_restart = true, group = "Enchantments")
    public static boolean bundle_enchantment = true;

    @Entry(category = "general", name = "Banner trophies")
    public static boolean banner_trohpy = true;

    @Entry(category = "general", name = "Beacon extended")
    public static boolean beacon_extended = true;

    @Entry(category = "general", name = "Bonemeal small flowers", group = "Bonemeal")
    public static boolean bonemeal_small_flower = true;

    @Entry(category = "general", name = "Bonemeal coral fan/block", group = "Bonemeal")
    public static boolean bonemeal_coral_fan = true;

    @Entry(category = "general", name = "Day counter", group = "Day counter")
    public static announceDay day_counter = announceDay.PER_1_DAY;

    @Entry(category = "general", name = "Ticks per day", group = "Day counter")
    public static int day_counter_tick_per_day = 24000;

    @Entry(category = "general", name = "Deepslate generator")
    public static boolean deepslate_generator = true;

    @Entry(category = "general", name = "Glow berry gives glow effect")
    public static boolean glow_berry_glowing = true;

    @Entry(category = "general", name = "Wash filled map with Cauldron")
    public static boolean cauldron_washing_map = true;

    @Entry(category = "general", name = "Invisible item frame", group = "Item frame/Sign")
    public static boolean invisible_frame = true;

    @Entry(category = "general", name = "Click through sign/itemframe", group = "Item frame/Sign")
    public static boolean click_through_itemframe = true;

    @Entry(category = "general", name = "Faster minecart")
    public static boolean faster_minecart = true;

    @Entry(category = "general", name = "Faster oxidization")
    public static boolean faster_oxidization = true;

    @Entry(category = "general", name = "Sneak through sweet berry")
    public static boolean softer_sweetBery = true;

    @Entry(category = "general", name = "No level limit anvil", require_restart = true)
    public static boolean no_level_limit_anvil = true;

    @Entry(category = "general", name = "Prevent farmland trampling")
    public static boolean prevent_farmland_trampling = true;

    @Entry(category = "general", name = "Torch burns target")
    public static boolean torch_burn_target = true;

    @Entry(category = "general", name = "No set spawn on sleep")
    public static boolean prevent_setSpawn_onSleep = true;

    @Entry(category = "general", name = "Sleep doesn't skip weather", info = "[DISABLE, RAIN_ONLY, ALL_WEATHER]")
    public static weatherType sleep_dont_skip_weather = weatherType.DISABLE;

    @Entry(category = "general", name = "Shulker label")
    public static boolean shulker_label = true;

    @Entry(category = "general", name = "Usable suspicious block")
    public static boolean usable_suspicious_block = true;

    @Entry(category = "general", name = "XP crops")
    public static boolean xp_crops = true;

    @Entry(category = "general", name = "Wet sponge dry in warm biome")
    public static boolean wet_sponge_dry_in_warm_biome = true;

    @Entry(category = "item", name = "Bundle place function")
    public static boolean bundle_place_mode = true;

    @Entry(category = "item", name = "Check Villagers schedule", group = "Item: Clock")
    public static boolean check_villager_schedule = true;

    @Entry(category = "item", name = "Clock shows current time", group = "Item: Clock")
    public static boolean clock_shows_time = true;

    @Entry(category = "item", name = "Hoe harvests crops")
    public static allowHarvestType hoe_harvest_crop = allowHarvestType.ALL;

    @Entry(category = "item", name = "Mace rotate blocks")
    public static boolean mace_rotate_block = true;

    @Entry(category = "item", name = "Trident can call thunder storm")
    public static boolean trident_call_thunder = true;

    @Entry(category = "item", name = "Shear can pickpocket villager", group = "Item: Shear")
    public static boolean shear_can_pickpocket_villager = true;

    @Entry(category = "item", name = "Shear can defuse TNT", group = "Item: Shear")
    public static boolean shear_defuses_tnt = true;

    @Entry(category = "item", name = "Shear prevents plant from growing", group = "Item: Shear")
    public static boolean shear_prevent_growing = true;

    @Entry(category = "item", name = "Spyglass what is that?", info = "[ALL, BLOCK_ONLY, ENTITY_ONLY, DISABLE]", group = "Item: Spyglass")
    public static inspectType spyglass_what_is_that = inspectType.ALL;

    @Entry(category = "item", name = "Normal inspect range", group = "Item: Spyglass")
    public static int spyglass_what_is_that_normal_distance = 5;

    @Entry(category = "item", name = "Normal mode: Show block's name", group = "Item: Spyglass")
    public static boolean spyglass_what_is_that_normal_show_block_name = true;

    @Entry(category = "item", name = "Normal mode: Show distance", group = "Item: Spyglass")
    public static boolean spyglass_what_is_that_normal_show_distance = false;

    @Entry(category = "item", name = "Normal mode: Show block's direction", group = "Item: Spyglass")
    public static boolean spyglass_what_is_that_normal_show_direction = true;

    @Entry(category = "item", name = "Normal mode: Show hidden infos", group = "Item: Spyglass")
    public static boolean spyglass_what_is_that_normal_show_infos = true;

    @Entry(category = "item", name = "Inspect range when zooming", group = "Item: Spyglass")
    public static int spyglass_what_is_that_zoom_distance = 32;

    @Entry(category = "item", name = "Zoom mode: Show block's name", group = "Item: Spyglass")
    public static boolean spyglass_what_is_that_zoom_show_block_name = true;

    @Entry(category = "item", name = "Zoom mode: Show distance", group = "Item: Spyglass")
    public static boolean spyglass_what_is_that_zoom_show_distance = true;

    @Entry(category = "item", name = "Zoom mode: Show block's direction", group = "Item: Spyglass")
    public static boolean spyglass_what_is_that_zoom_show_direction = true;

    @Entry(category = "item", name = "Zoom mode: Show hidden infos", group = "Item: Spyglass")
    public static boolean spyglass_what_is_that_zoom_show_infos = true;

    @Entry(category = "item", name = "Waxed copper indicator", group = "Item: Spyglass")
    public static boolean waxed_copper_indicator = true;

    @Entry(category = "item", name = "Slime chunk detector")
    public static boolean slime_chunk_check = true;

    @Entry(category = "entity", name = "9 lives cat")
    public static boolean _9_lives_cat = false;

    @Entry(category = "entity", name = "Baby nametag", group = "Nametag")
    public static boolean baby_nametag = true;

    @Entry(category = "entity", name = "Silent nametag", group = "Nametag")
    public static boolean silent_nametag = true;

    @Entry(category = "entity", name = "Brushable parrot/chicken")
    public static boolean brushable_parrot_chicken = true;

    @Entry(category = "entity", name = "Chicken egg try hatch on despawn")
    public static boolean chicken_egg_despawn_tryhatch = true;

    @Entry(category = "entity", name = "Explosive sniffer", group = "Sniffer")
    public static boolean explosive_sniffer = true;

    @Entry(category = "entity", name = "Sniffer sniff moss block", group = "Sniffer")
    public static boolean sniffer_get_spore = true;

    @Entry(category = "entity", name = "Fox bury items", group = "Fox")
    public static boolean fox_bury_items = true;

    @Entry(category = "entity", name = "Fox auto breed from berries", group = "Fox")
    public static boolean fox_auto_breed = true;

    @Entry(category = "entity", name = "Pat you pets", group = "Tameable mobs")
    public static boolean pet_patting = true;

    @Entry(category = "entity", name = "Piglin treats golden trimmed armor as golden armor")
    public static boolean piglin_goldenTrimmedArmor = true;

    @Entry(category = "entity", name = "Horse can seat on boat", group = "Tameable mobs")
    public static boolean horse_can_seat_on_boat = true;

    @Entry(category = "entity", name = "No saddled mobs wandering", group = "Tameable mobs")
    public static boolean prevent_tamed_horse_wandering = true;

    @Entry(category = "entity", name = "Rabbit breed after eats carrot crops")
    public static boolean rabbit_eat_carrot_crops = true;

    @Entry(category = "entity", name = "Random size fishes/squid")
    public static boolean random_size_fishes = true;

    @Entry(category = "entity", name = "Smaller bee")
    public static boolean smaller_bee = true;

    @Entry(category = "entity", name = "Snow Golem produces powder snow")
    public static boolean snow_golem_produces_powder_snow = true;

    @Entry(category = "entity", name = "Wandering trader announcer")
    public static boolean wandering_trader_announcer = true;

    @Entry(category = "recipe", name = "Suspicious sand/gravel recipe", require_restart = true)
    public static boolean sus_sand_recipe = true;

    @Entry(category = "cursed", name = "Hydrophobic elytra")
    public static boolean hydrophobic_elytra = false;

    @Entry(category = "cursed", name = "End stone is cheese")
    public static boolean endstone_is_cheese = false;

    @Entry(category = "cursed", name = "Reverse phantom spawn condition")
    public static boolean reverse_phantom = false;

    @Entry(category = "command", name = "Enable permission system")
    public static boolean permission_enable = false;

    @Entry(category = "command", name = "Emote commands")
    public static boolean emote = true;

    @Entry(category = "compatibility", name = "EasyShulkerBoxes Compatibility Layer", require_restart = true)
    public static boolean easy_shulker_box_compat = true;

    public enum weatherType {
        DISABLE, RAIN_ONLY, ALL_WEATHER
    }

    public enum announceDay {
        DISABLE, PER_1_DAY, PER_10_DAY, PER_50_DAY, PER_100_DAY
    }

    public enum inspectType {
        ALL, BLOCK_ONLY, ENTITY_ONLY, DISABLE
    }

    public enum allowHarvestType {
        ALL, HOE_ONLY, HAND_ONLY, DISABLE
    }
}