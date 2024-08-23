package xyz.faewulf.diversity.mixin.featherOnBrush;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.inter.entity.ICustomParrotEntity;
import xyz.faewulf.diversity.util.ModConfigs;

@Mixin(Parrot.class)
public abstract class ParrotEntityMixin extends ShoulderRidingEntity implements VariantHolder<Parrot.Variant>, FlyingAnimal, ICustomParrotEntity {

    @Shadow
    public abstract boolean isBaby();

    protected ParrotEntityMixin(EntityType<? extends ShoulderRidingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Unique
    private int featherCoolDown = 0;

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void InjectInteractMod(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

        if (!ModConfigs.brushable_parrot_chicken)
            return;

        if (player.level().isClientSide || featherCoolDown > 0)
            return;

        if (this.isBaby() || !player.isShiftKeyDown() || player.getItemInHand(InteractionHand.MAIN_HAND).getItem() != Items.BRUSH)
            return;

        player.getItemInHand(InteractionHand.MAIN_HAND).hurtAndBreak(1, player, getSlotForHand(InteractionHand.MAIN_HAND));
        this.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 0.8f, 1.0f);

        ItemStack drops = new ItemStack(Items.FEATHER);
        drops.setCount(random.nextIntBetweenInclusive(1, 2));
        this.spawnAtLocation(drops);

        featherCoolDown = 24000;
        cir.setReturnValue(InteractionResult.SUCCESS);
        cir.cancel();
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void tickMovementMixin(CallbackInfo ci) {
        if (featherCoolDown > 0) {
            featherCoolDown--;
        }
    }


    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putInt("diversity:featherCoolDown", this.featherCoolDown);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:featherCoolDown", Tag.TAG_ANY_NUMERIC)) {
            this.featherCoolDown = nbt.getInt("diversity:featherCoolDown");
        }
    }

    @Override
    public void setFeatherCoolDown(int value) {
        this.featherCoolDown = value;
    }

    @Override
    public int getFeatherCoolDown() {
        return this.featherCoolDown;
    }
}
