package xyz.faewulf.diversity_better_bundle.registry;

import xyz.faewulf.diversity_better_bundle.Constants;

import java.util.ArrayList;
import java.util.List;

public class CustomEnchantment {
    static public final List<String> bundle_enchantments = new ArrayList<>() {{
        add(Constants.MOD_ID + ":refill");
        add(Constants.MOD_ID + ":capacity");
        add(Constants.MOD_ID + ":vacuum");
        add(Constants.MOD_ID + ":selective_vacuum");
    }};
}
