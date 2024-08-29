package xyz.faewulf.diversity.mixin.functionalNameTag;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.faewulf.diversity.util.ModConfigs;

@Mixin(AgeableMob.class)
public abstract class babyNameTag extends PathfinderMob {
    @Shadow
    public abstract boolean isBaby();

    protected babyNameTag(EntityType<? extends PathfinderMob> entityType, Level world) {
        super(entityType, world);
    }

    @Inject(at = {@At("HEAD")}, method = {"ageUp*"}, cancellable = true)
    private void growUp(int age, boolean overGrow, CallbackInfo ci) {

        if (!ModConfigs.baby_nametag)
            return;

        if (this.getCustomName() == null)
            return;

        String name = this.getCustomName().getString().toLowerCase();

        if (name.contains("baby")) {
            ci.cancel();
        }
    }

    @Inject(at = {@At("HEAD")}, method = {"setAge"}, cancellable = true)
    private void setAge(int i, CallbackInfo cir) {

        if (!ModConfigs.baby_nametag)
            return;

        if (this.getCustomName() == null)
            return;

        String name = this.getCustomName().getString().toLowerCase();

        if (name.contains("baby") && this.isBaby()) {
            cir.cancel();
        }

    }
}
