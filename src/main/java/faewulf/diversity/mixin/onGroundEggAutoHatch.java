package faewulf.diversity.mixin;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class onGroundEggAutoHatch extends Entity implements Ownable {
    @Shadow
    public abstract ItemStack getStack();

    public onGroundEggAutoHatch(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/ItemEntity;discard()V", ordinal = 1, shift = At.Shift.BEFORE))
    private void tickInject(CallbackInfo ci) {

        if (!ModConfigs.chicken_egg_despawn_tryhatch)
            return;

        if (this.getStack().getItem() == Items.EGG) {
            int count = this.getStack().getCount();
            //try hatch
            for (int i = 0; i < count; i++) {
                //from EggEntity onCollision method
                if (this.random.nextInt(8) == 0) {
                    int chickenSpawnCount = 1;
                    if (this.random.nextInt(32) == 0) {
                        chickenSpawnCount = 4;
                    }

                    final EntityDimensions EMPTY_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);

                    for (int j = 0; j < chickenSpawnCount; ++j) {
                        ChickenEntity chickenEntity = EntityType.CHICKEN.create(this.getWorld());
                        if (chickenEntity != null) {
                            chickenEntity.setBreedingAge(-24000);
                            chickenEntity.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0F);
                            if (!chickenEntity.recalculateDimensions(EMPTY_DIMENSIONS)) {
                                break;
                            }

                            this.getWorld().spawnEntity(chickenEntity);

                        }
                    }
                }
            }
        }
    }
}
