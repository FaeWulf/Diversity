package faewulf.diversity.mixin;

import faewulf.diversity.feature.entity.fox.BuryItemGoal;
import faewulf.diversity.inter.entity.ICustomFoxEntity;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.VariantHolder;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoxEntity.class)
public abstract class FoxBuryItems extends MobEntity implements VariantHolder<FoxEntity.Type>, ICustomFoxEntity {

    @Unique
    private int BuryCoolDown = 0;

    protected FoxBuryItems(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "initGoals", at = @At("TAIL"))
    private void inJectInitGoal(CallbackInfo ci) {

        if (!ModConfigs.fox_bury_items)
            return;

        this.goalSelector.add(10, new BuryItemGoal((FoxEntity) (Object) this, 0.9F, 16, 2));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectTick(CallbackInfo ci) {
        if (BuryCoolDown > 0) {
            BuryCoolDown--;
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("TAIL"))
    private void writeCustomDataToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("diversity:buryCooldown", this.BuryCoolDown);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("TAIL"))
    private void readCustomDataFromNbt(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:buryCooldown", NbtElement.NUMBER_TYPE)) {
            this.BuryCoolDown = nbt.getInt("diversity:buryCooldown");
        }
    }

    public int getBuryCoolDown() {
        return BuryCoolDown;
    }

    public void setBuryCoolDown(int value) {
        this.BuryCoolDown = value;
    }
}
