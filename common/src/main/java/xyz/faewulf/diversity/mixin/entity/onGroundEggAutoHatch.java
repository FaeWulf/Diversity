package xyz.faewulf.diversity.mixin.entity;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.config.ModConfigs;
import xyz.faewulf.lib.util.Compare;

import java.util.Optional;

@Mixin(ItemEntity.class)
public abstract class onGroundEggAutoHatch extends Entity implements TraceableEntity {
    public onGroundEggAutoHatch(EntityType<?> type, Level world) {
        super(type, world);
    }

    @Unique
    private static final EntityDimensions $$Diversity_ZERO_SIZED_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);

    @Shadow
    public abstract ItemStack getItem();

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/item/ItemEntity;discard()V", ordinal = 1))
    private void tickInject(CallbackInfo ci) {

        if (!ModConfigs.chicken_egg_despawn_tryhatch)
            return;

        if (this.level().isClientSide())
            return;

        // If Item is Eggs (normal, blue, brown variant)
        if (Compare.isHasTag(this.getItem().getItem(), String.valueOf(ItemTags.EGGS.location()))) {

            BlockState blockState = this.level().getBlockState(this.blockPosition().below());

            //hatch on haybale only
            if (!Compare.isHasTag(blockState.getBlock(), "diversity:egg_hatchable"))
                return;

            int count = this.getItem().getCount();
            //try hatch
            for (int i = 0; i < count; i++) {
                //from EggEntity onCollision method
                if (this.random.nextInt(8) == 0) {
                    int count_per_egg = 1;
                    if (this.random.nextInt(32) == 0) {
                        count_per_egg = 4;
                    }

                    for (int j = 0; j < count_per_egg; j++) {
                        Chicken chicken = EntityType.CHICKEN.create(this.level(), EntitySpawnReason.TRIGGERED);
                        if (chicken != null) {
                            chicken.setAge(-24000);
                            chicken.snapTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), 0.0F);
                            Optional.ofNullable(this.getItem().get(DataComponents.CHICKEN_VARIANT)).ifPresent(chicken::setVariant);
                            if (!chicken.fudgePositionAfterSizeChange($$Diversity_ZERO_SIZED_DIMENSIONS)) {
                                break;
                            }

                            this.level().addFreshEntity(chicken);
                        }
                    }
                }
            }

            this.level().playSound(null, this.getOnPos(), SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 1.0F, (1.0F + this.level().getRandom().nextFloat() * 0.2F) * 0.7F);
        }
    }
}
