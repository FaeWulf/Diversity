package faewulf.diversity.util.data;

import faewulf.diversity.Diversity;

import java.util.ArrayList;
import java.util.List;

public class CustomEnchantment {
    static public final List<String> small_protection = new ArrayList<>() {{
        add(Diversity.MODID + ":backup_protection");
        add(Diversity.MODID + ":backup_fire_protection");
        add(Diversity.MODID + ":backup_projectile_protection");
        add(Diversity.MODID + ":backup_blast_protection");
        add(Diversity.MODID + ":backup_protection");
    }};

    static public final List<String> bundle_enchantments = new ArrayList<>() {{
        add(Diversity.MODID + ":refill");
        add(Diversity.MODID + ":capacity");
    }};
}
