package xyz.faewulf.diversity.registry;

import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LayeredCauldronBlock;
import xyz.faewulf.diversity.util.config.ModConfigs;

public class CauldronInteractionRegister {
    public static void register() {

        if (!ModConfigs.cauldron_washing_map)
            return;
    }
}
