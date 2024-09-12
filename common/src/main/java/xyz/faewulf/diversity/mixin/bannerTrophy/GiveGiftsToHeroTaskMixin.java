package xyz.faewulf.diversity.mixin.bannerTrophy;

import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.CustomBanner;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.List;

@Mixin(GiveGiftToHero.class)
public abstract class GiveGiftsToHeroTaskMixin {

    @Inject(method = "getItemToThrow", at = @At("RETURN"), cancellable = true)
    private void getGiftsInject(Villager villager, CallbackInfoReturnable<List<ItemStack>> cir) {

        if (!ModConfigs.banner_trohpy) return;

        List<ItemStack> returnValue = cir.getReturnValue();
        if (villager.getVillagerData().getProfession() == VillagerProfession.SHEPHERD) {
            if (returnValue != null && villager.getRandom().nextInt(5) == 2) {
                returnValue.add(CustomBanner.heroBanner());
                cir.setReturnValue(returnValue);
                cir.cancel();
            }
        }
    }
}
