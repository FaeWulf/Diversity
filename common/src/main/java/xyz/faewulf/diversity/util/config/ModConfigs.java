package xyz.faewulf.diversity.util.config;

public class ModConfigs {

    @Entry(category = "general", name = "Enable permission system")
    public static boolean permission_enable = false;

    public static boolean bigger_radius_bookshelf_for_enchantingTable = true;

    @Entry(category = "general", name = "Backup Enchantments", require_restart = true)
    public static boolean more_enchantment = true;

    @Entry(category = "general", name = "Bundle's Enchantment", require_restart = true)
    public static boolean bundle_enchantment = true;

    @Entry(category = "general", name = "Banner trophies")
    public static boolean banner_trohpy = true;

    @Entry(category = "general", name = "Bonemeal small flowers")
    public static boolean bonemeal_small_flower = true;

    @Entry(category = "general", name = "Bonemeal coral fan/block")
    public static boolean bonemeal_coral_fan = true;

    @Entry(category = "general", name = "Day counter")
    public static boolean day_counter = true;

    @Entry(category = "general", name = "Deepslate generator")
    public static boolean deepslate_generator = true;

    @Entry(category = "general", name = "Glow berry gives glow effect")
    public static boolean glow_berry_glowing = true;

    @Entry(category = "general", name = "Wash filled map with Cauldron")
    public static boolean cauldron_washing_map = true;

    @Entry(category = "general", name = "Invisible item frame")
    public static boolean invisible_frame = true;

    @Entry(category = "general", name = "Click through sign/itemframe")
    public static boolean click_through_itemframe = true;

    @Entry(category = "general", name = "Faster oxidization")
    public static boolean faster_oxidization = true;

    @Entry(category = "general", name = "Sneak through sweet berry")
    public static boolean softer_sweetBery = true;

    @Entry(category = "general", name = "No level limit anvil")
    public static boolean no_level_limit_anvil = true;

    @Entry(category = "general", name = "Prevent farmland trampling")
    public static boolean prevent_farmland_trampling = true;

    @Entry(category = "general", name = "Torch burns target")
    public static boolean torch_burn_target = true;

    @Entry(category = "general", name = "No set spawn on sleep")
    public static boolean prevent_setSpawn_onSleep = true;

    @Entry(category = "general", name = "Sleep doesn't skip weather", info = "[DISABLE, RAIN_ONLY, ALL_WEATHER]")
    public static weatherType sleep_dont_skip_weather = weatherType.DISABLE;

    public enum weatherType {
        DISABLE, RAIN_ONLY, ALL_WEATHER
    }

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

    public enum inspectType {
        ALL, BLOCK_ONLY, ENTITY_ONLY, DISABLE
    }

    @Entry(category = "item", name = "Check Villagers schedule")
    public static boolean check_villager_schedule = true;

    @Entry(category = "item", name = "Trident can call thunder strom")
    public static boolean trident_call_thunder = true;

    @Entry(category = "item", name = "Spyglass what is that?", info = "[ALL, BLOCK_ONLY, ENTITY_ONLY, DISABLE]")
    public static inspectType spyglass_what_is_that = inspectType.ALL;

    @Entry(category = "item", name = "Waxed copper indicator")
    public static boolean waxed_copper_indicator = true;

    @Entry(category = "entity", name = "9 lives cat")
    public static boolean _9_lives_cat = false;

    @Entry(category = "entity", name = "Baby nametag")
    public static boolean baby_nametag = true;

    @Entry(category = "entity", name = "Silent nametag")
    public static boolean silent_nametag = true;

    @Entry(category = "entity", name = "Brushable parrot/chicken")
    public static boolean brushable_parrot_chicken = true;

    @Entry(category = "entity", name = "Chicken egg try hatch on despawn")
    public static boolean chicken_egg_despawn_tryhatch = true;

    @Entry(category = "entity", name = "Explosive sniffer")
    public static boolean explosive_sniffer = true;

    @Entry(category = "entity", name = "Sniffer sniff moss block")
    public static boolean sniffer_get_spore = true;

    @Entry(category = "entity", name = "Fox bury items")
    public static boolean fox_bury_items = true;

    @Entry(category = "entity", name = "Pat you pets")
    public static boolean pet_patting = true;

    @Entry(category = "entity", name = "Piglin treats golden trimmed armor as golden armor")
    public static boolean piglin_goldenTrimmedArmor = true;

    @Entry(category = "entity", name = "Horse can seat on boat")
    public static boolean horse_can_seat_on_boat = true;

    @Entry(category = "entity", name = "No tamed horse wandering")
    public static boolean prevent_tamed_horse_wandering = true;

    @Entry(category = "entity", name = "Rabbit breeds after eats carrot crops")
    public static boolean rabbit_eat_carrot_crops = true;

    @Entry(category = "entity", name = "Wandering trader announcer")
    public static boolean wandering_trader_announcer = true;

    @Entry(category = "cursed", name = "Hydrophobic elytra")
    public static boolean hydrophobic_elytra = false;

    @Entry(category = "command", name = "Emote commands")
    public static boolean emote = true;

    @Entry(category = "command", name = "Check slime chunk command")
    public static boolean slime_chunk_check = true;

}