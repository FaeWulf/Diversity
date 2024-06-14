package faewulf.diversity.mixin.goldArmorTrimIsAlsoGold;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.component.DataComponentTypes;
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

@Mixin(PiglinBrain.class)
public class PiglinBrainMixin {

    @Inject(method = "wearsGoldArmor", at = @At("TAIL"), cancellable = true)
    private static void wearGoldArmorInject(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {

        if (!ModConfigs.piglin_goldenTrimmedArmor)
            return;

        for (ItemStack itemStack : entity.getAllArmorItems()) {
            Item item = itemStack.getItem();

            if (item instanceof ArmorItem) {
                ArmorTrim getTrim = itemStack.get(DataComponentTypes.TRIM);
                if (getTrim != null && getTrim.getMaterial().matchesKey(ArmorTrimMaterials.GOLD)) {
                    cir.setReturnValue(true);
                    cir.cancel();
                }
            }
        }
    }
}
