package faewulf.diversity.mixin.edibleEndStone;

//? if >=1.20.6 {

/*import faewulf.diversity.util.ModConfigs;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.Attackable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements Attackable {

    @Shadow
    public abstract boolean clearStatusEffects();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "eatFood", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyFoodEffects(Lnet/minecraft/component/type/FoodComponent;)V"))
    private void eatFoodInject(World world, ItemStack stack, FoodComponent foodComponent, CallbackInfoReturnable<ItemStack> cir) {

        if (world.isClient)
            return;

        if (!ModConfigs.endstone_is_cheese)
            return;

        //chance to clea effect
        if (stack.getItem() == Items.END_STONE && this.random.nextFloat() < 0.2) {
            this.clearStatusEffects();
        }
    }
}
 
*///?}
