package faewulf.diversity.mixin.client.noLevelLimitAnvil;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import faewulf.diversity.util.ModConfigs;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.entity.player.PlayerAbilities;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = AnvilScreen.class)
public abstract class AnvilMixin {
    @WrapOperation(method = "drawForeground", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/player/PlayerAbilities;creativeMode:Z", opcode = Opcodes.GETFIELD))
    private boolean drawForeGroundWrap(PlayerAbilities instance, Operation<Boolean> original) {
        return instance.creativeMode || ModConfigs.no_level_limit_anvil;
    }
}
