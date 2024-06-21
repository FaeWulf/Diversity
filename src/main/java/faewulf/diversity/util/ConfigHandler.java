package faewulf.diversity.util;

import faewulf.diversity.Diversity;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.configurate.loader.HeaderMode;
import org.spongepowered.configurate.serialize.SerializationException;

import java.io.IOException;
import java.nio.file.Path;

import static org.spongepowered.configurate.ConfigurationNode.NUMBER_DEF;

public class ConfigHandler {

    private static final int config_version = 2;
    private static int version;
    private static final String path = "config/diversity.conf";

    private static final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
            .prettyPrinting(true)
            .headerMode(HeaderMode.PRESET)
            .path(Path.of(path)) // Set where we will load and save to
            .build();

    private static CommentedConfigurationNode root;

    public static void loadConfig() {
        try {
            root = loader.load();

            if (root.empty()) {
                generateConfig(root);
                saveConfig();
            }

            parseConfig(root);

            if (version == NUMBER_DEF || version < config_version) {
                Diversity.LOGGER.info("Loaded config's version is older than current version, updating...");
                generateConfig(root);
                saveConfig();
            }

            parseConfig(root);

        } catch (IOException e) {
            System.err.println("An error occurred while loading this configuration: " + e.getMessage());
            if (e.getCause() != null) {
                e.getCause().printStackTrace();
            }
            Diversity.LOGGER.error("Unable to save config file: {}", e);
        }
    }

    public static void saveConfig() {
        try {
            loader.save(root);
        } catch (final ConfigurateException e) {
            Diversity.LOGGER.error("Unable to save config file: {}", e);
        }
    }

    protected static void parseConfig(CommentedConfigurationNode root) {
        version = root.node("version").getInt();
        ModConfigs.permission_enable = root.node("permission").getBoolean(ModConfigs.permission_enable);

        ModConfigs.invisible_frame = root.node("general").node("invisible_item frame").getBoolean(ModConfigs.invisible_frame);
        ModConfigs.click_through_itemframe = root.node("general").node("click_through_item_frame").getBoolean(ModConfigs.click_through_itemframe);
        ModConfigs.usable_suspicious_block = root.node("general").node("usable_suspicious_block").getBoolean(ModConfigs.usable_suspicious_block);
        ModConfigs.shulker_label = root.node("general").node("shulker_label").getBoolean(ModConfigs.shulker_label);
        ModConfigs.faster_oxidization = root.node("general").node("faster_oxidization").getBoolean(ModConfigs.faster_oxidization);
        ModConfigs.prevent_farmland_trampling = root.node("general").node("prevent_farmLand_trampling").getBoolean(ModConfigs.prevent_farmland_trampling);
        ModConfigs.xp_crops = root.node("general").node("xp_crops").getBoolean(ModConfigs.xp_crops);
        ModConfigs.prevent_setSpawn_onSleep = root.node("general").node("prevent_setSpawnPoint_onSleep").getBoolean(ModConfigs.prevent_setSpawn_onSleep);
        ModConfigs.sleep_dont_skip_weather = root.node("general").node("sleep_dont_skip_weather").getInt(ModConfigs.sleep_dont_skip_weather);
        ModConfigs.no_level_limit_anvil = root.node("general").node("no_level_limit_anvil").getBoolean(ModConfigs.no_level_limit_anvil);
        ModConfigs.cauldron_washing_map = root.node("general").node("cauldron_washing_filled_map").getBoolean(ModConfigs.cauldron_washing_map);
        ModConfigs.softer_sweetBery = root.node("general").node("friendlier_sweetBerry_bush").getBoolean(ModConfigs.softer_sweetBery);

        //commands
        ModConfigs.emote = root.node("command").node("emote").getBoolean(ModConfigs.emote);
        ModConfigs.slime_chunk_check = root.node("command").node("check_slime_chunk").getBoolean(ModConfigs.slime_chunk_check);

        //entities

        ModConfigs.pet_patting = root.node("entity").node("pet_patting").getBoolean(ModConfigs.pet_patting);
        ModConfigs.silent_nametag = root.node("entity").node("silent_nametag").getBoolean(ModConfigs.silent_nametag);
        ModConfigs.baby_nametag = root.node("entity").node("baby_nametag").getBoolean(ModConfigs.baby_nametag);
        ModConfigs.fox_bury_items = root.node("entity").node("fox_bury_items").getBoolean(ModConfigs.fox_bury_items);
        ModConfigs.brushable_parrot_chicken = root.node("entity").node("brushable_chicken_parrot").getBoolean(ModConfigs.brushable_parrot_chicken);
        ModConfigs.prevent_tamed_horse_wandering = root.node("entity").node("prevent_tamed_horse_wandering").getBoolean(ModConfigs.prevent_tamed_horse_wandering);
        ModConfigs.horse_can_seat_on_boat = root.node("entity").node("horse_can_seat_on_boat").getBoolean(ModConfigs.horse_can_seat_on_boat);
        ModConfigs.explosive_sniffer = root.node("entity").node("explosive_sniffer").getBoolean(ModConfigs.explosive_sniffer);
        ModConfigs.sniffer_get_spore = root.node("entity").node("sniffer_moss_block").getBoolean(ModConfigs.sniffer_get_spore);
        ModConfigs.smaller_bee = root.node("entity").node("smaller_bee").getBoolean(ModConfigs.smaller_bee);
        ModConfigs.random_size_fishes = root.node("entity").node("random_size_fishes").getBoolean(ModConfigs.random_size_fishes);
        ModConfigs.piglin_goldenTrimmedArmor = root.node("entity").node("piglin_goldenTrimmedArmor").getBoolean(ModConfigs.piglin_goldenTrimmedArmor);

    }

    protected static void generateConfig(CommentedConfigurationNode root) {
        try {
            root.node("version").set(config_version)
                    .comment("CONFIG VERSION, DO NOT TOUCH!");

            //permission
            root.node("permission").set(ModConfigs.permission_enable)
                    .comment("Enable Permission mode, disable if did not install any permission manager mod.");

            //content

            root.node("general").node("invisible_item frame").set(ModConfigs.invisible_frame)
                    .comment("Create invisible item frame");

            root.node("general").node("click_through_item_frame").set(ModConfigs.click_through_itemframe)
                    .comment("Player can access containers block by right click item frame/sign attached to it.");

            root.node("general").node("usable_suspicious_block").set(ModConfigs.usable_suspicious_block)
                    .comment("Player can inserts an item into empty suspicious blocks.");

            root.node("general").node("shulker_label").set(ModConfigs.shulker_label)
                    .comment("Shulker box will shows its custom name upon placed");

            root.node("general").node("faster_oxidization").set(ModConfigs.faster_oxidization)
                    .comment("Copper block variants will oxidize faster in rain or underwater");

            root.node("general").node("prevent_farmLand_trampling").set(ModConfigs.prevent_farmland_trampling)
                    .comment("Prevent trampling by wearing feather falling boots, or potion of slow fall");

            root.node("general").node("xp_crops").set(ModConfigs.xp_crops)
                    .comment("Crops drop XP");

            root.node("general").node("prevent_setSpawnPoint_onSleep").set(ModConfigs.prevent_setSpawn_onSleep)
                    .comment("Player can shift + rightclick bed to sleep without set spawn point.");

            root.node("general").node("sleep_dont_skip_weather").set(ModConfigs.sleep_dont_skip_weather)
                    .comment("Keep the current weather after sleep, 0 is disable, 1: don't skip rain only, 2: don't skip rain and thunder (experiment, you can only sleep at night even in thunder)");

            root.node("general").node("no_level_limit_anvil").set(ModConfigs.no_level_limit_anvil)
                    .comment("No limit xp on anvil");

            root.node("general").node("cauldron_washing_filled_map").set(ModConfigs.cauldron_washing_map)
                    .comment("Washing filled map with cauldron");

            root.node("general").node("friendlier_sweetBerry_bush").set(ModConfigs.softer_sweetBery)
                    .comment("Prevent taking damage if sneaking in berry bush, or wearing a pant");


            //commands
            root.node("command").node("emote").set(ModConfigs.emote)
                    .comment("Emote commands /meow /woof /purr /purreow");

            root.node("command").node("check_slime_chunk").set(ModConfigs.slime_chunk_check)
                    .comment("/slimechunkcheck command");

            //entities

            root.node("entity").node("pet_patting").set(ModConfigs.pet_patting)
                    .comment("Player can pat their pets.");

            root.node("entity").node("silent_nametag").set(ModConfigs.silent_nametag)
                    .comment("Nametag 'silent' 'shutup' will make mobs silent");

            root.node("entity").node("baby_nametag").set(ModConfigs.baby_nametag)
                    .comment("Nametag 'baby' will force baby animals never grow up");

            root.node("entity").node("fox_bury_items").set(ModConfigs.fox_bury_items)
                    .comment("Additional behavior for fox: They can bury random items in sand/gravel");

            root.node("entity").node("brushable_chicken_parrot").set(ModConfigs.brushable_parrot_chicken)
                    .comment("Chicken/Parrot can be brushed to drop some feather");

            root.node("entity").node("prevent_tamed_horse_wandering").set(ModConfigs.prevent_tamed_horse_wandering)
                    .comment("Tamed horse/donkey/mule wearing saddle won't wandering around");

            root.node("entity").node("horse_can_seat_on_boat").set(ModConfigs.horse_can_seat_on_boat)
                    .comment("Horse/donkey/mule can be put on boat");

            root.node("entity").node("explosive_sniffer").set(ModConfigs.explosive_sniffer)
                    .comment("Sniffer interactions with some type of powder");

            root.node("entity").node("sniffer_moss_block").set(ModConfigs.sniffer_get_spore)
                    .comment("Sniffer will dig moss block for Spore Blossom/Small Dripleaf");

            root.node("entity").node("smaller_bee").set(ModConfigs.smaller_bee)
                    .comment("Make all bees smaller (50%)");

            root.node("entity").node("random_size_fishes").set(ModConfigs.random_size_fishes)
                    .comment("Random size for fishes, squid");

            root.node("entity").node("piglin_goldenTrimmedArmor").set(ModConfigs.piglin_goldenTrimmedArmor)
                    .comment("Piglin will treat golden trimmed armor as normal gold armor.");


        } catch (SerializationException e) {
            e.printStackTrace();
        }

    }

    public boolean reloadConfig() {
        try {
            loadConfig();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Diversity.LOGGER.warn("Error while reloading config file ", e);
            return false;
        }
    }
}