package xyz.faewulf.diversity.mixin.goldArmorTrimIsAlsoGold;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.util.config.ModConfigs;

import java.util.Optional;

@Mixin(PiglinAi.class)
public class PiglinBrainMixin {

    @Inject(method = "isWearingGold", at = @At("TAIL"), cancellable = true)
    private static void wearGoldArmorInject(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {

        if (!ModConfigs.piglin_goldenTrimmedArmor)
            return;

        //1.21 method:
//        for (ItemStack itemStack : entity.getArmorAndBodyArmorSlots()) {
//            Item item = itemStack.getItem();
//
//            if (item instanceof ArmorItem) {
//                ArmorTrim getTrim = itemStack.get(DataComponents.TRIM);
//                if (getTrim != null && getTrim.material().is(TrimMaterials.GOLD)) {
//                    cir.setReturnValue(true);
//                    cir.cancel();
//                }
//            }
//        }

        for (ItemStack itemStack : entity.getArmorSlots()) {
            Item item = itemStack.getItem();
            if (item instanceof ArmorItem) {
                Optional<ArmorTrim> trimData = ArmorTrim.getTrim(entity.level().registryAccess(), itemStack);
                if (trimData.isPresent() && trimData.get().material().is(TrimMaterials.GOLD)) {
                    cir.setReturnValue(true);
                    cir.cancel();
                }
            }
        }
    }
}
