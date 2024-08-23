package xyz.faewulf.diversity.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.ModConfigs;

@Mixin(ItemEntity.class)
public abstract class onGroundEggAutoHatch extends Entity implements TraceableEntity {
    @Shadow
    public abstract ItemStack getItem();

    public onGroundEggAutoHatch(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V", ordinal = 1, shift = At.Shift.BEFORE))
    private void tickInject(CallbackInfo ci) {

        if (!ModConfigs.chicken_egg_despawn_tryhatch)
            return;

        if (this.level().isClientSide)
            return;

        if (this.getItem().getItem() == Items.EGG) {

            BlockState blockState = this.level().getBlockState(this.blockPosition().below());

            //hatch on haybale only
            if (blockState.getBlock() != Blocks.HAY_BLOCK)
                return;

            int count = this.getItem().getCount();
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
                        Chicken chickenEntity = EntityType.CHICKEN.create(this.level());
                        if (chickenEntity != null) {
                            chickenEntity.setAge(-24000);
                            chickenEntity.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                            if (!chickenEntity.fudgePositionAfterSizeChange(EMPTY_DIMENSIONS)) {
                                break;
                            }

                            this.level().addFreshEntity(chickenEntity);

                        }
                    }
                }
            }
        }
    }
}
