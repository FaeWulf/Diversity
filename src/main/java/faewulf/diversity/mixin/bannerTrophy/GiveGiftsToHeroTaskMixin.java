package faewulf.diversity.mixin.bannerTrophy;

import faewulf.diversity.util.CustomBanner;
import net.minecraft.entity.ai.brain.task.GiveGiftsToHeroTask;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.village.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(GiveGiftsToHeroTask.class)
public abstract class GiveGiftsToHeroTaskMixin {

    @Inject(method = "getGifts", at = @At("RETURN"), cancellable = true)
    private void getGiftsInject(VillagerEntity villager, CallbackInfoReturnable<List<ItemStack>> cir) {
        List<ItemStack> returnValue = cir.getReturnValue();
        if (villager.getVillagerData().getProfession() == VillagerProfession.SHEPHERD) {
            if (returnValue != null && villager.getRandom().nextInt(5) == 2) {
                returnValue.add(CustomBanner.heroBanner(villager.getRegistryManager().getWrapperOrThrow(RegistryKeys.BANNER_PATTERN)));
                cir.setReturnValue(returnValue);
                cir.cancel();
            }
        }
    }
}
