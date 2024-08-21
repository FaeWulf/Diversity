package faewulf.diversity.mixin.goldArmorTrimIsAlsoGold;

import faewulf.diversity.util.ModConfigs;

//? if >=1.21 {
/*import net.minecraft.component.DataComponentTypes;
*///?}

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.PiglinBrain;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.trim.ArmorTrim;
import net.minecraft.item.trim.ArmorTrimMaterials;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {

    @Inject(method = "wearsGoldArmor", at = @At("TAIL"), cancellable = true)
    private static void wearGoldArmorInject(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {

        if (!ModConfigs.piglin_goldenTrimmedArmor)
            return;

        //? if >=1.21 {
        
        /*for (ItemStack itemStack : entity.getAllArmorItems()) {
            Item item = itemStack.getItem();

            if (item instanceof ArmorItem) {
                ArmorTrim getTrim = itemStack.get(DataComponentTypes.TRIM);
                if (getTrim != null && getTrim.getMaterial().matchesKey(ArmorTrimMaterials.GOLD)) {
                    cir.setReturnValue(true);
                    cir.cancel();
                }
            }
        }
        
        *///?}


        //? if =1.20.1 {
        for (ItemStack itemStack : entity.getArmorItems()) {
            Item item = itemStack.getItem();
            if (item instanceof ArmorItem) {
                Optional<ArmorTrim> trimData = ArmorTrim.getTrim(entity.getWorld().getRegistryManager(), itemStack);
                if (trimData.isPresent() && trimData.get().getMaterial().matchesKey(ArmorTrimMaterials.GOLD)) {
                    cir.setReturnValue(true);
                    cir.cancel();
                }
            }
        }
        //?}
    }
}
