package xyz.faewulf.diversity.mixin.general.bannerTrophy;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.level.storage.loot.LootTable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.Constants;
import xyz.faewulf.diversity.util.CustomLootTables;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(GiveGiftToHero.class)
public abstract class GiveGiftsToHeroTaskMixin {

    @Inject(method = "getLootTableToThrow", at = @At("RETURN"), cancellable = true)
    private static void getGiftsInject(Villager villager, CallbackInfoReturnable<ResourceKey<LootTable>> cir) {

        if (!ModConfigs.banner_trohpy) return;

        try {
            System.out.println("lmao 2");
            if (villager.getVillagerData().profession() == BuiltInRegistries.VILLAGER_PROFESSION.get(VillagerProfession.SHEPHERD).orElse(null) && !villager.isBaby()) {
                System.out.println("lmao");
                cir.setReturnValue(CustomLootTables.HERO_GIFT);
                cir.cancel();
            }
        } catch (NullPointerException e) {
            Constants.LOG.error(e.getMessage());
            e.printStackTrace();
        }

    }
}
