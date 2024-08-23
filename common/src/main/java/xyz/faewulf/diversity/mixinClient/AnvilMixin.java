package xyz.faewulf.diversity.mixinClient;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.world.entity.player.Abilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import xyz.faewulf.diversity.util.ModConfigs;

@Mixin(value = AnvilScreen.class)
public abstract class AnvilMixin {
    @WrapOperation(method = "renderLabels", at = @At(value = "FIELD", target = "Lnet/minecraft/world/entity/player/Abilities;instabuild:Z", opcode = 180))
    private boolean drawForeGroundWrap(Abilities instance, Operation<Boolean> original) {
        return instance.instabuild || ModConfigs.no_level_limit_anvil;
    }
}
