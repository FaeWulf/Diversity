package xyz.faewulf.diversity_better_bundle.mixin.general.bundleEnchantments;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity_better_bundle.registry.CustomEnchantmentTags;
import xyz.faewulf.diversity_better_bundle.util.config.ModConfigs;

import static net.minecraft.world.entity.npc.VillagerTrades.TRADES;

@Mixin(VillagerTrades.class)
public class VillagerTradesMixin {

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void addCustomTrades(CallbackInfo ci) {

        if (!ModConfigs.bundle_enchantment)
            return;

        // Get the existing trades for the FARMER profession
        Int2ObjectMap<VillagerTrades.ItemListing[]> leatherTrades = TRADES.get(VillagerProfession.LEATHERWORKER);
        if (leatherTrades != null) {
            leatherTrades.put(1, diversity$extendTradeArray(leatherTrades.get(1), new VillagerTrades.EnchantBookForEmeralds(1, CustomEnchantmentTags.LEATHER_WORKER_BOOK_TRADE)));
            leatherTrades.put(3, diversity$extendTradeArray(leatherTrades.get(3), new VillagerTrades.EnchantBookForEmeralds(1, CustomEnchantmentTags.LEATHER_WORKER_BOOK_TRADE)));
        }

        // Update the TRADES map
        TRADES.put(VillagerProfession.FARMER, leatherTrades);
    }

    @Unique
    private static VillagerTrades.ItemListing[] diversity$extendTradeArray(VillagerTrades.ItemListing[] oldArray, VillagerTrades.ItemListing newTrade) {
        VillagerTrades.ItemListing[] newArray = new VillagerTrades.ItemListing[oldArray.length + 1];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        newArray[oldArray.length] = newTrade;
        return newArray;
    }
}
