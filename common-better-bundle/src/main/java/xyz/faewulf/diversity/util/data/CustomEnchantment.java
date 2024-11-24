package xyz.faewulf.diversity.util.data;

import xyz.faewulf.diversity.Constants;

import java.util.ArrayList;
import java.util.List;

public class CustomEnchantment {
    static public final List<String> small_protection = new ArrayList<>() {{
        add(Constants.MOD_ID + ":backup_protection");
        add(Constants.MOD_ID + ":backup_fire_protection");
        add(Constants.MOD_ID + ":backup_projectile_protection");
        add(Constants.MOD_ID + ":backup_blast_protection");
        add(Constants.MOD_ID + ":backup_protection");
    }};

    static public final List<String> bundle_enchantments = new ArrayList<>() {{
        add(Constants.MOD_ID + ":refill");
        add(Constants.MOD_ID + ":capacity");
    }};
}
