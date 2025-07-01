package xyz.faewulf.diversity.mixin.entity.featherOnBrush;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xyz.faewulf.diversity.inter.entity.ICustomParrotEntity;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(Parrot.class)
public abstract class ParrotEntityMixin extends ShoulderRidingEntity implements FlyingAnimal, ICustomParrotEntity {

    @Unique
    private int diversity_Multiloader$featherCoolDown = 0;

    protected ParrotEntityMixin(EntityType<? extends ShoulderRidingEntity> entityType, Level world) {
        super(entityType, world);
    }

    @Shadow
    public abstract boolean isBaby();

    @Inject(method = "mobInteract", at = @At("HEAD"), cancellable = true)
    private void InjectInteractMod(Player player, InteractionHand hand, CallbackInfoReturnable<InteractionResult> cir) {

        if (!ModConfigs.brushable_parrot_chicken)
            return;

        if (player.level().isClientSide || diversity_Multiloader$featherCoolDown > 0)
            return;

        if (this.isBaby() || !player.isShiftKeyDown() || player.getItemInHand(InteractionHand.MAIN_HAND).getItem() != Items.BRUSH)
            return;

        player.getItemInHand(InteractionHand.MAIN_HAND).hurtAndBreak(1, player, getSlotForHand(InteractionHand.MAIN_HAND));
        this.playSound(SoundEvents.ITEM_FRAME_REMOVE_ITEM, 0.8f, 1.0f);

        ItemStack drops = new ItemStack(Items.FEATHER);
        drops.setCount(random.nextIntBetweenInclusive(1, 2));
        this.spawnAtLocation((ServerLevel) player.level(), drops);

        player.swing(hand, true);
        diversity_Multiloader$featherCoolDown = 24000;
        cir.setReturnValue(InteractionResult.SUCCESS);
        cir.cancel();
    }

    @Inject(method = "aiStep", at = @At("HEAD"))
    private void tickMovementMixin(CallbackInfo ci) {
        if (diversity_Multiloader$featherCoolDown > 0) {
            diversity_Multiloader$featherCoolDown--;
        }
    }


    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(ValueOutput valueOutput, CallbackInfo ci) {
        valueOutput.putInt("diversity:featherCoolDown", this.diversity_Multiloader$featherCoolDown);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(ValueInput valueInput, CallbackInfo ci) {
        this.diversity_Multiloader$featherCoolDown = valueInput.getInt("diversity:featherCoolDown").orElse(0);
    }

    @Override
    public void diversity_Multiloader$setFeatherCoolDown(int value) {
        this.diversity_Multiloader$featherCoolDown = value;
    }

    @Override
    public int diversity_Multiloader$getFeatherCoolDown() {
        return this.diversity_Multiloader$featherCoolDown;
    }
}
