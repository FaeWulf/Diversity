package faewulf.diversity.mixin.functionalNameTag;

import faewulf.diversity.util.ModConfigs;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PassiveEntity.class)
public abstract class babyNameTag extends PathAwareEntity {
    protected babyNameTag(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "growUp(IZ)V", at = @At("HEAD"), cancellable = true)
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
}
