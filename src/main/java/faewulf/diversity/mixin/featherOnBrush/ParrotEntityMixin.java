package faewulf.diversity.mixin.featherOnBrush;

import faewulf.diversity.inter.entity.ICustomParrotEntity;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.VariantHolder;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParrotEntity.class)
public abstract class ParrotEntityMixin extends TameableShoulderEntity implements VariantHolder<ParrotEntity.Variant>, Flutterer, ICustomParrotEntity {

    @Shadow
    public abstract boolean isBaby();

    protected ParrotEntityMixin(EntityType<? extends TameableShoulderEntity> entityType, World world) {
        super(entityType, world);
    }

    @Unique
    private int featherCoolDown = 0;

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void InjectInteractMod(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {

        if (!ModConfigs.brushable_parrot_chicken)
            return;

        if (player.getWorld().isClient || featherCoolDown > 0)
            return;

        if (this.isBaby() || !player.isSneaking() || player.getStackInHand(Hand.MAIN_HAND).getItem() != Items.BRUSH)
            return;

        player.getStackInHand(Hand.MAIN_HAND).damage(1, player, getSlotForHand(Hand.MAIN_HAND));
        this.playSound(SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, 0.8f, 1.0f);

        ItemStack drops = new ItemStack(Items.FEATHER);
        drops.setCount(random.nextBetween(1, 2));
        this.dropStack(drops);

        featherCoolDown = 24000;
        cir.setReturnValue(ActionResult.SUCCESS);
        cir.cancel();
    }

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void tickMovementMixin(CallbackInfo ci) {
        if (featherCoolDown > 0) {
            featherCoolDown--;
        }
    }


    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("diversity:featherCoolDown", this.featherCoolDown);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:featherCoolDown", NbtElement.NUMBER_TYPE)) {
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
