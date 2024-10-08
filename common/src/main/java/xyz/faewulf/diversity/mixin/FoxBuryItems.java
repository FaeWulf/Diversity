package xyz.faewulf.diversity.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.feature.entity.fox.BuryItemGoal;
import xyz.faewulf.diversity.inter.entity.ICustomFoxEntity;
import xyz.faewulf.diversity.util.config.ModConfigs;

@Mixin(Fox.class)
public abstract class FoxBuryItems extends Mob implements VariantHolder<Fox.Type>, ICustomFoxEntity {

    @Unique
    private int multiLoader_1_20_1$BuryCoolDown = 0;

    protected FoxBuryItems(EntityType<? extends Animal> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(method = "registerGoals", at = @At("TAIL"))
    private void inJectInitGoal(CallbackInfo ci) {

        if (!ModConfigs.fox_bury_items)
            return;

        this.goalSelector.addGoal(10, new BuryItemGoal((Fox) (Object) this, 0.9F, 16, 2));
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectTick(CallbackInfo ci) {
        if (multiLoader_1_20_1$BuryCoolDown > 0) {
            multiLoader_1_20_1$BuryCoolDown--;
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void addAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        nbt.putInt("diversity:buryCooldown", this.multiLoader_1_20_1$BuryCoolDown);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void readAdditionalSaveData(CompoundTag nbt, CallbackInfo ci) {
        if (nbt.contains("diversity:buryCooldown", Tag.TAG_ANY_NUMERIC)) {
            this.multiLoader_1_20_1$BuryCoolDown = nbt.getInt("diversity:buryCooldown");
        }
    }

    public int multiLoader_1_20_1$getBuryCoolDown() {
        return multiLoader_1_20_1$BuryCoolDown;
    }

    public void multiLoader_1_20_1$setBuryCoolDown(int value) {
        this.multiLoader_1_20_1$BuryCoolDown = value;
    }
}
